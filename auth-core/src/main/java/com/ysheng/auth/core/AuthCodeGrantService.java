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
import com.ysheng.auth.model.api.authcode.AccessTokenRevokeSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationRevokeSpec;
import com.ysheng.auth.model.api.exception.InternalException;
import com.ysheng.auth.model.api.authcode.AccessToken;
import com.ysheng.auth.model.api.authcode.AccessTokenIssueSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationGrantSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationTicket;

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
   * @throws InternalException The exception that contains error details.
   */
  AuthorizationTicket authorize(
      String clientId,
      AuthorizationGrantSpec request) throws InternalException;

  /**
   * Revokes an authorization ticket from a client.
   *
   * @param clientId The client identifier.
   * @param code The authorization code.
   * @param request The authorization revocation request that contains required information.
   * @throws InternalException The exception that contains error details.
   */
  void revokeAuthorization(
      String clientId,
      String code,
      AuthorizationRevokeSpec request) throws InternalException;

  /**
   * Gets a list of authorization tickets granted to a particular client.
   *
   * @param clientId The client identifier for which the authorization ticket was granted to.
   * @return A list of authorization tickets.
   * @throws InternalException The error that contains detail information.
   */
  ApiList<AuthorizationTicket> listAuthorizationTickets(String clientId) throws InternalException;

  /**
   * Gets an authorization ticket with the given client identifier and code.
   *
   * @param clientId The client identifier.
   * @param code The authorization code.
   * @return An authorization ticket that matches the criteria.
   * @throws InternalException The error that contains detail information.
   */
  AuthorizationTicket getAuthorizationTicket(
      String clientId,
      String code) throws InternalException;

  /**
   * Issues an access token for a client with an auth code received from authorization of
   * Authorization Code Grant type.
   *
   * @param clientId The client identifier.
   * @param request The access token request that contains required information.
   * @return The access token response that contains the access token.
   * @throws InternalException The exception that contains error details.
   */
  AccessToken issueAccessToken(
      String clientId,
      AccessTokenIssueSpec request) throws InternalException;

  /**
   * Gets a list of access tokens issued to a particular client.
   *
   * @param clientId The client identifier for which the access token was issued to.
   * @return A list of access tokens.
   * @throws InternalException The error that contains detail information.
   */
  ApiList<AccessToken> listAccessTokens(String clientId) throws InternalException;

  /**
   * Revokes an access token from a client.
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
   * Gets an access token with the given client identifier and token.
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
