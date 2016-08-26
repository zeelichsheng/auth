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
import com.ysheng.auth.model.api.authcode.AccessToken;
import com.ysheng.auth.model.api.error.AccessTokenError;
import com.ysheng.auth.model.api.authcode.AccessTokenSpec;
import com.ysheng.auth.model.api.error.AuthorizationError;
import com.ysheng.auth.model.api.authcode.AuthorizationSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationTicket;
import com.ysheng.auth.model.api.error.AuthorizationTicketNotFoundError;
import com.ysheng.auth.model.api.error.ClientNotFoundError;

/**
 * Defines the interface of authorization code grant related functions.
 */
public interface AuthCodeGrantService {

  /**
   * Authorizes an authorization request of Authorization Code Grant type.
   *
   * @param clientId The client identifier.
   * @param request The authorization request that contains required information.
   * @return The authorization ticket object.
   * @throws AuthorizationError The exception that contains error details.
   */
  AuthorizationTicket authorize(
      String clientId,
      AuthorizationSpec request) throws AuthorizationError;

  /**
   * Gets a list of authorization tickets granted to a particular client.
   *
   * @param clientId The client identifier for which the authorization ticket was granted to.
   * @return A list of authorization tickets.
   * @throws ClientNotFoundError The error that contains detail information.
   */
  ApiList<AuthorizationTicket> listAuthorizationTicket(String clientId) throws ClientNotFoundError;

  /**
   * Gets an authorization ticket with the given client identifier and code.
   *
   * @param clientId The client identifier.
   * @param code The authorization code.
   * @return An authorization ticket that matches the criteria.
   * @throws ClientNotFoundError The error that contains detail information.
   * @throws AuthorizationTicketNotFoundError The error that contains detail information.
   */
  AuthorizationTicket getAuthorizationTicket(
      String clientId,
      String code) throws ClientNotFoundError, AuthorizationTicketNotFoundError;

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
