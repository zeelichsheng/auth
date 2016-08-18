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

import com.ysheng.auth.common.utility.ReflectionUtil;
import com.ysheng.auth.model.database.Client;
import com.ysheng.auth.model.database.DatabaseObjectDataField;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Defines the adapter for a client entity in Redis.
 */
public class ClientAdapter {

  // The template for the entity key.
  private static final String ENTITY_KEY_TEMPLATE = "auth-client:id:%s";

  /**
   * Returns the Redis key for the object.
   *
   * @param clientId The client identifier.
   * @return The key for the object.
   */
  public static String getKey(String clientId) {
    return String.format(ENTITY_KEY_TEMPLATE, clientId);
  }

  /**
   * Returns the Redis hash for the object.
   *
   * @param client The database object.
   * @return The hash for the object.
   */
  public static Map<String, String> toHash(Client client) {
    try {
      return ReflectionUtil.getAnnotatedFieldNamesAndValues(
          Client.class,
          DatabaseObjectDataField.class,
          client)
          .entrySet()
          .stream()
          .collect(Collectors.toMap(e -> e.getKey().toString(),
              e -> e.getValue() == null ? null : e.getValue().toString()));
    } catch (Throwable t) {
      return null;
    }
  }

  /**
   * Parses the Redis hash.
   *
   * @param hash The Redis hash.
   * @return The database object.
   */
  public static Client fromHash(Map<String, String> hash) {
    Client client = new Client();
    try {
      ReflectionUtil.setFieldValues(
          Client.class,
          client,
          hash);
    } catch (Throwable t) {
      client = null;
    }

    return client;
  }
}
