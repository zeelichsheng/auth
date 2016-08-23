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

package com.ysheng.auth.backend.redis;

import com.ysheng.auth.backend.Database;
import com.ysheng.auth.backend.model.AuthorizationTicket;
import com.ysheng.auth.backend.model.Client;
import com.ysheng.auth.backend.redis.adapter.AuthorizationTicketAdapter;
import com.ysheng.auth.backend.redis.adapter.ClientAdapter;

import java.util.Set;

/**
 * Implementation of the backend database based on Redis.
 */
public class RedisDatabase implements Database {

  // The Redis client.
  private RedisClient redisClient;

  /**
   * Constructs a RedisDatabase object.
   *
   * @param redisClient The Redis client.
   */
  public RedisDatabase(
      RedisClient redisClient) {
    this.redisClient = redisClient;
  }

  /**
   * Stores a client object in database.
   *
   * @param client The client object to be stored.
   */
  public void storeClient(Client client) {
    redisClient.set(
        ClientAdapter.getKey(client.getId()),
        ClientAdapter.toHash(client));
  }

  /**
   * Removes a client object from database.
   *
   * @param clientId The client identifier.
   */
  public void removeClient(String clientId) {
    redisClient.remove(ClientAdapter.getKey(clientId));
  }

  /**
   * Finds a client object by client identifier.
   *
   * @param clientId The client identifier to be matched.
   * @return A client object that matches the client identifier.
   */
  public Client findClientById(String clientId) {
    return ClientAdapter.fromHash(
        redisClient.get(ClientAdapter.getKey(clientId)));
  }

  /**
   * Stores an authorization ticket object in database.
   *
   * @param authorizationTicket The authorization ticket object to be stored.
   */
  public void storeAuthorizationTicket(AuthorizationTicket authorizationTicket) {
    redisClient.set(
        AuthorizationTicketAdapter.getKey(authorizationTicket.getClientId(), authorizationTicket.getCode()),
        AuthorizationTicketAdapter.toHash(authorizationTicket));
  }

  /**
   * Finds an authorization ticket object by authorization code.
   *
   * @param code The authorization code to be matched.
   * @param clientId The client identifier to be matched.
   * @return An authorization ticket object that matches the authorization code and client identifier.
   */
  public AuthorizationTicket findAuthorizationTicketByCodeAndClientId(
      String code,
      String clientId) {
    return AuthorizationTicketAdapter.fromHash(
        redisClient.get(AuthorizationTicketAdapter.getKey(clientId, code)));
  }
}
