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

package com.ysheng.auth.backend.redis.entity;

import com.ysheng.auth.common.utility.AnnotationUtil;
import com.ysheng.auth.model.database.Client;
import com.ysheng.auth.model.database.DatabaseObjectDataField;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Defines the data structure of a client entity in Redis format.
 */
public class ClientEntity implements Entity<Client> {

  // The template for the entity key.
  private static final String ENTITY_KEY_TEMPLATE = "auth-client:id:%s";

  // The client database object.
  private Client client;

  /**
   * Constructs a ClientEntity from a client database object.
   *
   * @param client The client database object.
   */
  public ClientEntity(Client client) {
    this.client = client;
  }

  /**
   * Returns the Redis key for the object.
   *
   * @return The key for the object.
   */
  public String getKey() {
    return String.format(ENTITY_KEY_TEMPLATE, client.getId());
  }

  /**
   * Returns the Redis hash for the object.
   *
   * @return The hash for the object.
   */
  public Map<String, String> getHash() {
    try {
      return AnnotationUtil.getAnnotatedFields(
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
}
