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

import com.ysheng.auth.backend.redis.adapter.ClientAdapter;
import com.ysheng.auth.common.backend.Database;
import com.ysheng.auth.model.database.AuthorizationTicket;
import com.ysheng.auth.model.database.Client;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    redisClient.hmset(
        ClientAdapter.getKey(client.getId(), client.getRedirectUri()),
        ClientAdapter.toHash(client));
  }

  /**
   * Finds a client object by client identifier.
   *
   * @param clientId The client identifier to be matched.
   * @return A client object that matches the client identifier.
   */
  public Client findClientById(String clientId) {
    Set<String> keys = redisClient.keys(ClientAdapter.getKey(clientId, null));
    return ClientAdapter.fromHash(
        redisClient.hgetAll(keys.iterator().next()));
  }

  /**
   * Finds a client object by redirect URI.
   *
   * @param redirectUri The client redirect URI to be matched.
   * @return A client object that matches the redirect URI.
   */
  public Client findClientByRedirectUri(String redirectUri) {
    Set<String> keys = redisClient.keys(ClientAdapter.getKey(null, redirectUri));
    return ClientAdapter.fromHash(
        redisClient.hgetAll(keys.iterator().next()));
  }

  /**
   * Stores an authorization ticket object in database.
   *
   * @param authorizationTicket The authorization ticket object to be stored.
   */
  public void storeAuthorizationTicket(AuthorizationTicket authorizationTicket) {
    throw new NotImplementedException();

  }

  /**
   * Finds an authorization ticket object by authorization code.
   *
   * @param code The authorization code to be matched.
   * @return An authorization ticket object that matches the authorization code.
   */
  public AuthorizationTicket findAuthorizationTicketByCode(String code) {
    throw new NotImplementedException();
  }
}
