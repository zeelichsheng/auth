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

import com.ysheng.auth.model.configuration.backend.RedisConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Implementation of Redis related functions.
 */
public class RedisClientImpl implements RedisClient {

  // The configuration of Redis.
  private RedisConfiguration configuration;

  // The connection pool.
  private JedisSentinelPool connectionPool;

  /**
   * Constructs a RedisConnectorImpl object.
   *
   * @param configuration The configuration of Redis.
   */
  public RedisClientImpl(
      RedisConfiguration configuration) {
    this.configuration = configuration;
    this.connectionPool = null;
  }

  /**
   * Opens a Redis connection.
   */
  public void openConnection() {
    if (connectionPool == null) {
      JedisPoolConfig config = new JedisPoolConfig();
      config.setMaxTotal(configuration.getMaxConnections());
      config.setBlockWhenExhausted(configuration.isBlockWhenExhausted());

      connectionPool = new JedisSentinelPool(
          configuration.getMasterAddress(),
          configuration.getSentinels(),
          config);
    }
  }

  /**
   * Closes a Redis connection.
   */
  public void closeConnection() {
    connectionPool.close();
  }

  /**
   * Implements set command in Redis.
   *
   * @param key The key of the database entity.
   * @param value The value of the database entity.
   */
  public void set(String key, String value) {
    doRedis(
        resource -> resource.set(key, value)
    );
  }

  /**
   * Implements get command in Redis.
   *
   * @param key The key of the database entity.
   * @return The value of the database entity.
   */
  public String get(String key) {
    final List<String> hash = new ArrayList<>();
    doRedis(
        resource -> hash.add(resource.get(key))
    );

    return hash.iterator().next();
  }

  /**
   * Implements hmset command in Redis.
   *
   * @param key The key of the database entity.
   * @param hash The value of the database entity.
   */
  public void hmset(String key, Map<String, String> hash) {
    doRedis(
        resource -> resource.hmset(key, hash)
    );
  }

  /**
   * Implements hgetall command in Redis.
   *
   * @param key The key of the database entity.
   * @return The value of the database entity.
   */
  public Map<String, String> hgetAll(String key) {
    final List<Map<String, String>> hash = new ArrayList<>();
    doRedis(
        resource -> hash.add(resource.hgetAll(key))
    );

    return hash.iterator().next();
  }

  /**
   * Implements keys command in Redis.
   *
   * @param keyPattern The key pattern.
   * @return The set of keys that matches the key pattern.
   */
  public Set<String> keys(String keyPattern) {
    final List<Set<String>> keys = new ArrayList<>();
    doRedis(
      resource -> keys.add(resource.keys(keyPattern))
    );

    return keys.iterator().next();
  }

  /**
   * Executes a Redis command by picking a resource from the connection pool.
   *
   * @param consumer The Redis command that consumes the connection resource.
   */
  private void doRedis(Consumer<Jedis> consumer) {
    Jedis resource = connectionPool.getResource();

    try {
      consumer.accept(resource);
    } finally {
      resource.close();
    }
  }
}
