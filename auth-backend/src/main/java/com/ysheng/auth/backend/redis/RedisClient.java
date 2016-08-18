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

import java.util.Map;

/**
 * Defines the interface of Redis related functions.
 */
public interface RedisClient {

  /**
   * Opens a Redis connection.
   */
  void openConnection();

  /**
   * Closes a Redis connection.
   */
  void closeConnection();

  /**
   * Implements hmset command in Redis.
   *
   * @param key The key of the database entity.
   * @param hash The value of the database entity.
   */
  void hmset(String key, Map<String, String> hash);

  /**
   * Implements hgetall command in Redis.
   *
   * @param key The key of the database entity.
   * @return The value of the database entity.
   */
  Map<String, String> hgetAll(String key);
}