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
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.model.api.exception.AccessTokenNotFoundException;
import com.ysheng.auth.model.api.exception.ClientNotFoundException;
import com.ysheng.auth.model.api.exception.ClientUnauthorizedException;
import com.ysheng.auth.model.api.exception.InternalException;
import com.ysheng.auth.model.api.exception.ResponseTypeUnsupportedException;
import com.ysheng.auth.model.api.implicit.AccessToken;
import com.ysheng.auth.model.api.implicit.AccessTokenRevokeSpec;
import com.ysheng.auth.model.api.implicit.AuthorizationGrantSpec;

/**
 * Implements implicit grant related functions.
 */
public class ImplicitGrantServiceImpl implements ImplicitGrantService {

  // The database handler.
  private Database database;

  // The auth code generator.
  private AuthValueGenerator authValueGenerator;

  /**
   * Constructs an ImplicitGrantServiceImpl object.
   *
   * @param database The database object to interact with persistence store.
   * @param authValueGenerator The generator object to generate auth related values.
   */
  public ImplicitGrantServiceImpl(
      Database database,
      AuthValueGenerator authValueGenerator) {
    this.database = database;
    this.authValueGenerator = authValueGenerator;
  }

  /**
   * Issues an implicit access token for a client.
   *
   * @param clientId The client identifier.
   * @param request The access token request that contains required information.
   * @return The access token response that contains the access token.
   * @throws com.ysheng.auth.model.api.exception.InternalException The exception that contains error detail.
   */
  public AccessToken issueAccessToken(
      String clientId,
      AuthorizationGrantSpec request) throws InternalException {
    // Validate the request.
    if (!AuthorizationGrantSpec.VALID_RESPONSE_TYPE.equals(request.getResponseType())) {
      throw new ResponseTypeUnsupportedException(request.getResponseType());
    }

    Client client = database.findClientById(clientId);
    if (client == null) {
      throw new ClientNotFoundException(clientId);
    }

    String accessToken = authValueGenerator.generateAccessToken();

    // Store access token in databse.
    AccessToken token = new AccessToken();
    token.setClientId(clientId);
    token.setAccessToken(accessToken);
    token.setTokenType(AccessTokenType.BEARER);
    // TODO: change how we calculate the token expiration.
    // Also consider how to set scope.
    token.setExpiresIn((long) Integer.MAX_VALUE);
    token.setState(request.getState());

    database.storeImplictAccessToken(token);

    return token;
  }

  /**
   * Gets a list of implicit access tokens issued to a particular client.
   *
   * @param clientId The client identifier for which the access token was issued to.
   * @return A list of access tokens.
   * @throws InternalException The error that contains detail information.
   */
  public ApiList<AccessToken> listAccessTokens(String clientId) throws InternalException {
    Client client = database.findClientById(clientId);
    if (client == null) {
      throw new ClientNotFoundException(clientId);
    }

    return new ApiList<>(database.listImplicitAccessTokens(clientId));
  }

  /**
   * Revokes an implicit access token from a client.
   *
   * @param clientId The client identifier.
   * @param accessToken The access token.
   * @param request The access token revocation request that contains required information.
   * @throws InternalException The error that contains detail information.
   */
  public void revokeAccessToken(
      String clientId,
      String accessToken,
      AccessTokenRevokeSpec request) throws InternalException {
    Client client = database.findClientById(clientId);
    if (client == null) {
      throw new ClientNotFoundException(clientId);
    }

    if (client.getSecret() != null &&
        !client.getSecret().equals(request.getClientSecret())) {
      throw new ClientUnauthorizedException(clientId);
    }

    AccessToken token = database.findImplicitAccessTokenByClientIdAndToken(clientId, accessToken);
    if (token == null) {
      throw new AccessTokenNotFoundException(clientId, accessToken);
    }

    database.removeAccessToken(clientId, accessToken);
  }

  /**
   * Gets an implicit access token with the given client identifier and token.
   *
   * @param clientId The client identifier.
   * @param accessToken The access token.
   * @return An access token that matches the criteria.
   * @throws InternalException The error that contains detail information.
   */
  public AccessToken getAccessToken(
      String clientId,
      String accessToken) throws InternalException {
    Client client = database.findClientById(clientId);
    if (client == null) {
      throw new ClientNotFoundException(clientId);
    }

    AccessToken token = database.findImplicitAccessTokenByClientIdAndToken(clientId, accessToken);
    if (token == null) {
      throw new AccessTokenNotFoundException(clientId, accessToken);
    }

    return token;
  }
}
