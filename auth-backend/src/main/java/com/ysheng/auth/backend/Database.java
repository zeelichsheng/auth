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

package com.ysheng.auth.backend;

import com.ysheng.auth.model.api.authcode.AuthorizationTicket;
import com.ysheng.auth.model.api.client.Client;

import java.util.List;

/**
 * Defines the interface of backend database related functions.
 */
public interface Database {

  ///
  /// Client related functions.
  ///

  /**
   * Stores a client object in database.
   *
   * @param client The client object to be stored.
   */
  void storeClient(Client client);

  /**
   * Removes a client object from database.
   *
   * @param clientId The client identifier.
   */
  void removeClient(String clientId);

  /**
   * Finds a client object by client identifier.
   *
   * @param clientId The client identifier to be matched.
   * @return A client object that matches the client identifier.
   */
  Client findClientById(String clientId);

  /**
   * Gets a list of all clients in database.
   *
   * @return A list of all clients in database.
   */
  List<Client> listClients();

  ///
  /// Auth Code Grant related functions.
  ///

  /**
   * Stores an authorization ticket object in database.
   *
   * @param authorizationTicket The authorization ticket object to be stored.
   */
  void storeAuthorizationTicket(AuthorizationTicket authorizationTicket);

  /**
   * Gets a list of authorization tickets that belong to the client.
   *
   * @param clientId The client identifier.
   * @return A list of authorization tickets.
   */
  List<AuthorizationTicket> listAuthorizationTickets(String clientId);

  /**
   * Removes an authorization ticket object from database.
   *
   * @param clientId  The client identifier.
   * @param code The authorization code.
   */
  void removeAuthorizationTicket(String clientId, String code);

  /**
   * Finds an authorization ticket object by client ID and authorization code.
   *
   * @param code The authorization code to be matched.
   * @param clientId The client identifier to be matched.
   * @return An authorization ticket object that matches the authorization code and client identifier.
   */
  AuthorizationTicket findAuthorizationTicketByCodeAndClientId(
      String code,
      String clientId);

  /**
   * Stores an access token object in database.
   *
   * @param accessToken The access token object to be stored.
   */
  void storeAccessToken(com.ysheng.auth.model.api.authcode.AccessToken accessToken);

  /**
   * Gets a list of access tokens that belong to the client.
   *
   * @param clientId The clietn identifier.
   * @return A list of access tokens.
   */
  List<com.ysheng.auth.model.api.authcode.AccessToken> listAccessTokens(String clientId);

  /**
   * Removes an access token object from database.
   *
   * @param clientId The client identifier.
   * @param accessToken The access token.
   */
  void removeAccessToken(String clientId, String accessToken);

  /**
   * Finds an access token object by client ID and token.
   *
   * @param clientId The client identifier.
   * @param accessToken The access token.
   * @return An access token object that matches the client ID and token.
   */
  com.ysheng.auth.model.api.authcode.AccessToken findAccessTokenByClientIdAndToken(
      String clientId,
      String accessToken);

  ///
  /// Implicit Grant related functions.
  ///

  /**
   * Stores an implicit access token object in database.
   *
   * @param accessToken The access token object to be stored.
   */
  void storeImplictAccessToken(com.ysheng.auth.model.api.implicit.AccessToken accessToken);

  /**
   * Gets a list of implicit access tokens that belong to the client.
   *
   * @param clientId The clietn identifier.
   * @return A list of access tokens.
   */
  List<com.ysheng.auth.model.api.implicit.AccessToken> listImplicitAccessTokens(String clientId);

  /**
   * Removes an implicit access token object from database.
   *
   * @param clientId The client identifier.
   * @param accessToken The access token.
   */
  void removeImplictAccessToken(String clientId, String accessToken);

  /**
   * Finds an implicit access token by client ID and token.
   *
   * @param clientId The client identifier.
   * @param accessToken The access token.
   * @return An implicit access token object that matches the client ID and token.
   */
  com.ysheng.auth.model.api.implicit.AccessToken findImplicitAccessTokenByClientIdAndToken(
      String clientId,
      String accessToken);
}
