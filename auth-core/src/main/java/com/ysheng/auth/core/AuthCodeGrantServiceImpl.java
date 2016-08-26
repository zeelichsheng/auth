/*
 * Copyright 2016 Yu Sheng. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, without warranties or
 * conditions of any kind, EITHER EXPRESS OR IMPLIED.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.ysheng.auth.core;

import com.ysheng.auth.backend.Database;
import com.ysheng.auth.core.generator.AuthValueGenerator;
import com.ysheng.auth.model.api.AccessTokenType;
import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.exception.InternalException;
import com.ysheng.auth.model.api.authcode.AccessToken;
import com.ysheng.auth.model.api.authcode.AccessTokenSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationTicket;
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.model.api.exception.AuthorizationTicketNotFoundError;
import com.ysheng.auth.model.api.exception.ClientNotFoundException;
import com.ysheng.auth.model.api.exception.GrantTypeUnsupportedException;
import com.ysheng.auth.model.api.exception.InvalidClientException;
import com.ysheng.auth.model.api.exception.InvalidRequestException;
import com.ysheng.auth.model.api.exception.ResponseTypeUnsupportedException;

/**
 * Implements authorization code grant related functions.
 */
public class AuthCodeGrantServiceImpl implements AuthCodeGrantService{

  // The database handler.
  private Database database;

  // The auth code generator.
  private AuthValueGenerator authValueGenerator;

  /**
   * Constructs an AuthCodeGrantServiceImpl object.
   *
   * @param database The database object to interact with persistence store.
   * @param authValueGenerator The generator object to generate auth related values.
   */
  public AuthCodeGrantServiceImpl(
      Database database,
      AuthValueGenerator authValueGenerator) {
    this.database = database;
    this.authValueGenerator = authValueGenerator;
  }

  /**
   * Authorizes an authorization request of Authorization Code Grant type.
   *
   * @param clientId The client identifier.
   * @param request The authorization request that contains required information.
   * @return The authorization ticket object.
   * @throws InternalException The exception contains error details.
   */
  public AuthorizationTicket authorize(
      String clientId,
      AuthorizationSpec request) throws InternalException {
    // Validate the request.
    if (!AuthorizationSpec.VALID_RESPONSE_TYPE.equals(request.getResponseType())) {
      throw new ResponseTypeUnsupportedException(request.getResponseType());
    }

    Client client = database.findClientById(clientId);
    if (client == null) {
      throw new ClientNotFoundException(clientId);
    }

    String authCode = authValueGenerator.generateAuthCode();

    // Store authorization ticket in database.
    AuthorizationTicket authorizationTicket = new AuthorizationTicket();
    authorizationTicket.setCode(authCode);
    authorizationTicket.setClientId(clientId);
    authorizationTicket.setRedirectUri(request.getRedirectUri());
    authorizationTicket.setScope(request.getScope());
    authorizationTicket.setState(request.getState());
    database.storeAuthorizationTicket(authorizationTicket);

    return authorizationTicket;
  }

  /**
   * Gets a list of authorization tickets granted to a particular client.
   *
   * @param clientId The client identifier for which the authorization ticket was granted to.
   * @return A list of authorization tickets.
   * @throws InternalException The error that contains detail information.
   */
  public ApiList<AuthorizationTicket> listAuthorizationTicket(String clientId) throws InternalException {
    Client client = database.findClientById(clientId);
    if (client == null) {
      throw new ClientNotFoundException(clientId);
    }

    return new ApiList<>(database.listAuthorizationTickets(clientId));
  }

  /**
   * Gets an authorization ticket with the given client identifier and code.
   *
   * @param clientId The client identifier.
   * @param code The authorization code.
   * @return An authorization ticket that matches the criteria.
   * @throws InternalException The error that contains detail information.
   */
  public AuthorizationTicket getAuthorizationTicket(
      String clientId,
      String code) throws InternalException {
    Client client = database.findClientById(clientId);
    if (client == null) {
      throw new ClientNotFoundException(clientId);
    }

    AuthorizationTicket ticket = database.findAuthorizationTicketByCodeAndClientId(code, clientId);
    if (ticket == null) {
      throw new AuthorizationTicketNotFoundError(clientId, code);
    }

    return ticket;
  }

  /**
   * Issues an access token for a client with an auth code received from authorization of
   * Authorization Code Grant type.
   *
   * @param request The access token request that contains required information.
   * @return The access token response that contains the access token.
   * @throws InternalException The exception contains error details.
   */
  public AccessToken issueAccessToken(AccessTokenSpec request) throws InternalException {
    // Validate the request.
    if (!AccessTokenSpec.VALID_GRANT_TYPE.equals(request.getGrantType())) {
      throw new GrantTypeUnsupportedException(request.getGrantType());
    }

    Client client = database.findClientById(request.getClientId());
    if (client == null) {
      throw new ClientNotFoundException(request.getClientId());
    }

    AuthorizationTicket authorizationTicket = database.findAuthorizationTicketByCodeAndClientId(
        request.getCode(),
        request.getClientId());
    if (authorizationTicket == null) {
      throw new InvalidRequestException("Unable to find authorization code: " + request.getCode());
    }

    if (authorizationTicket.getRedirectUri() != null &&
        !authorizationTicket.getRedirectUri().equals(request.getRedirectUri())) {
      throw new InvalidRequestException("Mismatch of redirect URI: " + request.getRedirectUri());
    }

    if (!client.getId().equals(authorizationTicket.getClientId())) {
      throw new InvalidClientException(request.getClientId());
    }

    // Build the response.
    AccessToken response = new AccessToken();
    response.setAccessToken(authValueGenerator.generateAccessToken());
    response.setTokenType(AccessTokenType.BEARER);
    // TODO: change how we calculate the token expiration.
    // Also consider how to set refresh token and scop.
    response.setExpiresIn((long) Integer.MAX_VALUE);

    return response;
  }
}
