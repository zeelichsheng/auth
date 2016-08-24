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

  /**
   * Stores an authorization ticket object in database.
   *
   * @param authorizationTicket The authorization ticket object to be stored.
   */
  void storeAuthorizationTicket(AuthorizationTicket authorizationTicket);

  /**
   * Finds an authorization ticket object by authorization code.
   *
   * @param code The authorization code to be matched.
   * @param clientId The client identifier to be matched.
   * @return An authorization ticket object that matches the authorization code and client identifier.
   */
  AuthorizationTicket findAuthorizationTicketByCodeAndClientId(
      String code,
      String clientId);
}
