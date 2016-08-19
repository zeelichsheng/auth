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

import com.ysheng.auth.common.backend.Database;
import com.ysheng.auth.common.core.AuthorizationService;
import com.ysheng.auth.common.core.exception.AuthCodeAccessTokenException;
import com.ysheng.auth.common.core.exception.AuthCodeAuthorizationException;
import com.ysheng.auth.common.core.exception.ClientRegistrationException;
import com.ysheng.auth.common.core.generator.AuthValueGenerator;
import com.ysheng.auth.common.utility.UriUtil;
import com.ysheng.auth.model.AccessTokenType;
import com.ysheng.auth.model.ClientType;
import com.ysheng.auth.model.authcode.AccessTokenErrorType;
import com.ysheng.auth.model.authcode.AccessTokenRequest;
import com.ysheng.auth.model.authcode.AccessTokenResponse;
import com.ysheng.auth.model.authcode.AuthorizationErrorType;
import com.ysheng.auth.model.authcode.AuthorizationRequest;
import com.ysheng.auth.model.authcode.AuthorizationResponse;
import com.ysheng.auth.model.client.ClientRegistrationErrorType;
import com.ysheng.auth.model.client.ClientRegistrationRequest;
import com.ysheng.auth.model.client.ClientRegistrationResponse;
import com.ysheng.auth.model.database.AuthorizationTicket;
import com.ysheng.auth.model.database.Client;

/**
 * Implementation of the authorization related functions.
 */
public class AuthorizationServiceImpl implements AuthorizationService {

  // The database handler.
  private Database database;

  // The auth code generator.
  private AuthValueGenerator authValueGenerator;

  /**
   * Constructs an AuthorizationServiceImpl object.
   *
   * @param database The database object to interact with persistence store.
   * @param authValueGenerator The generator object to generate auth related values.
   */
  public AuthorizationServiceImpl(
      Database database,
      AuthValueGenerator authValueGenerator) {
    this.database = database;
    this.authValueGenerator = authValueGenerator;
  }

  /**
   * Registers a client with the authentication server.
   *
   * @param request The client registration request that contains required information.
   * @return The client registration response that contains the client identifier and secret.
   * @throws com.ysheng.auth.common.core.exception.ClientRegistrationException The exception that contains error details.
   */
  public ClientRegistrationResponse registerClient(ClientRegistrationRequest request)
      throws ClientRegistrationException {
    // Validate the request.
    if (request.getRedirectUri() == null) {
      throw new ClientRegistrationException(
          ClientRegistrationErrorType.INVALID_REQUEST,
          "Redirect URI cannot be null");
    }

    if (!UriUtil.isValidUri(request.getRedirectUri())) {
      throw new ClientRegistrationException(
          ClientRegistrationErrorType.INVALID_REQUEST,
          "Invalid redirect URI: " + request.getRedirectUri());
    }

    Client existingClient = database.findClientByRedirectUri(request.getRedirectUri());
    if (existingClient != null) {
      throw new ClientRegistrationException(
          ClientRegistrationErrorType.ALREADY_REGISTERED,
          "Client already registered with redirect URI: " + request.getRedirectUri());
    }

    String clientId = authValueGenerator.generateClientId();
    String clientSecret = ClientType.CONFIDENTIAL.equals(request.getType()) ?
        authValueGenerator.generateClientSecret() : null;

    // Store client in database.
    Client client = new Client();
    client.setType(request.getType());
    client.setId(clientId);
    client.setSecret(clientSecret);
    client.setRedirectUri(request.getRedirectUri());
    database.storeClient(client);

    // Build the response.
    ClientRegistrationResponse response = new ClientRegistrationResponse();
    response.setClientId(clientId);
    response.setClientSecret(clientSecret);

    return response;
  }

  /**
   * Authorizes an authorization request of Authorization Code Grant type.
   *
   * @param request The authorization request that contains required information.
   * @return The authorization response that contains the authorization code.
   * @throws AuthCodeAuthorizationException The exception contains error details.
   */
  public AuthorizationResponse authorize(AuthorizationRequest request) throws AuthCodeAuthorizationException {
    // Validate the request.
    if (!AuthorizationRequest.VALID_RESPONSE_TYPE.equals(request.getResponseType())) {
      throw new AuthCodeAuthorizationException(
          AuthorizationErrorType.UNSUPPORTED_RESPONSE_TYPE,
          "Unsupported response type in request: " + request.getResponseType().toString());
    }

    Client client = database.findClientById(request.getClientId());
    if (client == null) {
      throw new AuthCodeAuthorizationException(
          AuthorizationErrorType.UNAUTHORIZED_CLIENT,
          "Unable to find client with ID: " + request.getClientId());
    }

    String authCode = authValueGenerator.generateAuthCode();

    // Store authorization ticket in database.
    AuthorizationTicket authorizationTicket = new AuthorizationTicket();
    authorizationTicket.setCode(authCode);
    authorizationTicket.setClientId(request.getClientId());
    authorizationTicket.setRedirectUri(request.getRedirectUri());
    authorizationTicket.setScope(request.getScope());
    authorizationTicket.setState(request.getState());
    database.storeAuthorizationTicket(authorizationTicket);

    // Build the response.
    AuthorizationResponse response = new AuthorizationResponse();
    response.setCode(authCode);
    if (request.getState() != null) {
      response.setState(request.getState());
    }

    return response;
  }

  /**
   * Issues an access token for a client with an auth code received from authorization of
   * Authorization Code Grant type.
   *
   * @param request The access token request that contains required information.
   * @return The access token response that contains the access token.
   * @throws AuthCodeAccessTokenException The exception contains error details.
   */
  public AccessTokenResponse issueAccessToken(AccessTokenRequest request) throws AuthCodeAccessTokenException {
    // Validate the request.
    if (!AccessTokenRequest.VALID_GRANT_TYPE.equals(request.getGrantType())) {
      throw new AuthCodeAccessTokenException(
          AccessTokenErrorType.UNSUPPORTED_GRANT_TYPE,
          "Unsupported grant type in request: " + request.getGrantType().toString());
    }

    Client client = database.findClientById(request.getClientId());
    if (client == null) {
      throw new AuthCodeAccessTokenException(
          AccessTokenErrorType.UNAUTHORIZED_CLIENT,
          "Unable to find client with ID: " + request.getClientId());
    }

    AuthorizationTicket authorizationTicket = database.findAuthorizationTicketByCodeAndClientId(
        request.getCode(),
        request.getClientId());
    if (authorizationTicket == null) {
      throw new AuthCodeAccessTokenException(
          AccessTokenErrorType.INVALID_REQUEST,
          "Unable to find authorization code: " + request.getCode());
    }

    if (authorizationTicket.getRedirectUri() != null &&
        !authorizationTicket.getRedirectUri().equals(request.getRedirectUri())) {
      throw new AuthCodeAccessTokenException(
          AccessTokenErrorType.INVALID_REQUEST,
          "Mismatch of redirect URI: " + request.getRedirectUri());
    }

    if (!client.getId().equals(authorizationTicket.getClientId())) {
      throw new AuthCodeAccessTokenException(
          AccessTokenErrorType.INVALID_CLIENT,
          "Invalid client ID: " + request.getClientId());
    }

    // Build the response.
    AccessTokenResponse response = new AccessTokenResponse();
    response.setAccessToken(authValueGenerator.generateAccessToken());
    response.setTokenType(AccessTokenType.BEARER);
    // TODO: change how we calculate the token expiration.
    // Also consider how to set refresh token and scop.
    response.setExpiresIn((long) Integer.MAX_VALUE);

    return response;
  }
}
