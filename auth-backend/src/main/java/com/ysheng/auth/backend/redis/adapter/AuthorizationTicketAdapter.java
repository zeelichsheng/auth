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
import com.ysheng.auth.model.database.AuthorizationTicket;
import com.ysheng.auth.model.database.DatabaseObjectDataField;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Defines the adapter for an authorization ticket entity in Redis.
 */
public class AuthorizationTicketAdapter {

  // The template for the entity key.
  private static final String ENTITY_KEY_TEMPLATE = "auth-authorization-ticket:%s:%s";

  /**
   * Returns the Redis key for the object.
   *
   * @param clientId The client identifier.
   * @param code The authorization code.
   * @return The key for the object.
   */
  public static String getKey(
      String clientId,
      String code) {
    if (clientId == null) {
      clientId = "*";
    }

    if (code == null) {
      code = "*";
    }

    return String.format(ENTITY_KEY_TEMPLATE, clientId, code);
  }

  /**
   * Returns the Redis hash for the object.
   *
   * @param authorizationTicket The database object.
   * @return The hash for the object.
   */
  public static Map<String, String> toHash(AuthorizationTicket authorizationTicket) {
    try {
      return ReflectionUtil.getAnnotatedFieldNamesAndValues(
          AuthorizationTicket.class,
          DatabaseObjectDataField.class,
          authorizationTicket)
          .entrySet()
          .stream()
          .collect(Collectors.toMap(e -> e.getKey(),
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
  public static AuthorizationTicket fromHash(Map<String, String> hash) {
    AuthorizationTicket authorizationTicket = new AuthorizationTicket();
    try {
      ReflectionUtil.setFieldValues(
          AuthorizationTicket.class,
          authorizationTicket,
          hash);
    } catch (Throwable t) {
      authorizationTicket = null;
    }

    return authorizationTicket;
  }
}
