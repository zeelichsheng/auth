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

package com.ysheng.auth.backend.redis.adapter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysheng.auth.backend.model.Client;

import java.io.IOException;

/**
 * Defines the adapter for a client entity in Redis.
 */
public class ClientAdapter {

  // The template for the entity key.
  private static final String ENTITY_KEY_TEMPLATE = "auth-client:%s:%s";

  // The JSON object mapper.
  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Returns the Redis key for the object.
   *
   * @param clientId The client identifier.
   * @param redirectUri The client redirect URI.
   * @return The key for the object.
   */
  public static String getKey(
      String clientId,
      String redirectUri) {
    if (clientId == null) {
      clientId = "*";
    }

    if (redirectUri == null) {
      redirectUri = "*";
    }

    return String.format(ENTITY_KEY_TEMPLATE, clientId, redirectUri);
  }

  /**
   * Returns the Redis hash for the object.
   *
   * @param client The database object.
   * @return The hash for the object.
   */
  public static String toHash(Client client) {
    try {
      return objectMapper.writeValueAsString(client);
    } catch (IOException ex) {
      return null;
    }
  }

  /**
   * Parses the Redis hash.
   *
   * @param hash The Redis hash.
   * @return The database object.
   */
  public static Client fromHash(String hash) {
    try {
      return objectMapper.readValue(
          hash,
          new TypeReference<Client>() {
          });
    } catch (IOException ex) {
      return null;
    }
  }
}
