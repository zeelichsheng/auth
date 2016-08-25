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
import com.ysheng.auth.model.api.authcode.AccessTokenError;
import com.ysheng.auth.model.api.authcode.AccessTokenSpec;
import com.ysheng.auth.model.api.authcode.AccessToken;
import com.ysheng.auth.model.api.authcode.AuthorizationError;
import com.ysheng.auth.model.api.authcode.AuthorizationSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationTicket;
import com.ysheng.auth.model.api.client.ClientNotFoundError;

import java.util.Optional;

/**
 * Defines the interface of authorization code grant related functions.
 */
public interface AuthCodeGrantService {

  /**
   * Authorizes an authorization request of Authorization Code Grant type.
   *
   * @param request The authorization request that contains required information.
   * @return The authorization ticket object.
   * @throws AuthorizationError The exception that contains error details.
   */
  AuthorizationTicket authorize(AuthorizationSpec request) throws AuthorizationError;

  /**
   * Gets a list of authorization tickets. If client identifier is given, then return
   * all authorization tickets granted to that particular client.
   *
   * @param clientId The client identifier for which the authorization ticket was granted to.
   * @return A list of authorization tickets.
   * @throws ClientNotFoundError The error that contains detail information.
   */
  ApiList<AuthorizationTicket> listAuthorizationTicket(Optional<String> clientId) throws ClientNotFoundError;

  /**
   * Issues an access token for a client with an auth code received from authorization of
   * Authorization Code Grant type.
   *
   * @param request The access token request that contains required information.
   * @return The access token response that contains the access token.
   * @throws AccessTokenError The exception that contains error details.
   */
  AccessToken issueAccessToken(AccessTokenSpec request) throws AccessTokenError;
}
