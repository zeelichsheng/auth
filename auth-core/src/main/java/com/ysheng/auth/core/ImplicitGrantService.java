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

import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.exception.InternalException;
import com.ysheng.auth.model.api.implicit.AccessToken;
import com.ysheng.auth.model.api.implicit.AccessTokenRevokeSpec;
import com.ysheng.auth.model.api.implicit.AuthorizationGrantSpec;

/**
 * Defines the interface of implicit grant related functions.
 */
public interface ImplicitGrantService {

  /**
   * Issues an implicit access token for a client.
   *
   * @param clientId The client identifier.
   * @param request The access token request that contains required information.
   * @return The access token response that contains the access token.
   * @throws InternalException The exception that contains error detail.
   */
  AccessToken issueAccessToken(
      String clientId,
      AuthorizationGrantSpec request) throws InternalException;

  /**
   * Gets a list of implicit access tokens issued to a particular client.
   *
   * @param clientId The client identifier for which the access token was issued to.
   * @return A list of access tokens.
   * @throws InternalException The error that contains detail information.
   */
  ApiList<AccessToken> listAccessTokens(String clientId) throws InternalException;

  /**
   * Revokes an implicit access token from a client.
   *
   * @param clientId The client identifier.
   * @param accessToken The access token.
   * @param request The access token revocation request that contains required information.
   * @throws InternalException The error that contains detail information.
   */
  void revokeAccessToken(
      String clientId,
      String accessToken,
      AccessTokenRevokeSpec request) throws InternalException;

  /**
   * Gets an implicit access token with the given client identifier and token.
   *
   * @param clientId The client identifier.
   * @param accessToken The access token.
   * @return An access token that matches the criteria.
   * @throws InternalException The error that contains detail information.
   */
  AccessToken getAccessToken(
      String clientId,
      String accessToken) throws InternalException;
}
