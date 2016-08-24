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

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Defines the interface of Redis related functions.
 */
public interface RedisClient {

  /**
   * Implements set command in Redis.
   *
   * @param key The key of the database entity.
   * @param value The value of the database entity.
   */
  void set(String key, String value);

  /**
   * Implements get command in Redis.
   *
   * @param key The key of the database entity.
   * @return The value of the database entity.
   */
  String get(String key);

  /**
   * Implements mget command in Redis.
   *
   * @param keys The set of keys of the database entities.
   * @return The list of database entites.
   */
  List<String> mget(Set<String> keys);

  /**
   * Implements del command in Redis.
   *
   * @param key The key of the database entity.
   */
  void remove(String key);

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

  /**
   * Implements keys command in Redis.
   *
   * @param keyPattern The key pattern.
   * @return The set of keys that matches the key pattern.
   */
  Set<String> keys(String keyPattern);
}
