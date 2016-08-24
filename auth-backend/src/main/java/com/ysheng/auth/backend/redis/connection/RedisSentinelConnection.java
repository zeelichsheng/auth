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

package com.ysheng.auth.backend.redis.connection;

import com.ysheng.auth.model.configuration.backend.RedisConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * Defines a class that represents a Redis sentinel connection.
 */
public class RedisSentinelConnection implements RedisConnection {

  // The connection pool.
  private JedisSentinelPool connectionPool;

  // The configuration of Redis.
  private RedisConfiguration configuration;

  /**
   * Constructs a RedisSentinelConnection object.
   *
   * @param configuration The configuration of Redis.
   */
  public RedisSentinelConnection(
      RedisConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Opens a Redis connection.
   */
  public void open() {
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
  public void close() {
    if (connectionPool == null) {
      connectionPool.close();
    }
  }

  /**
   * Gets a Redis connection resource.
   *
   * @return A Redis connection resource.
   */
  public Jedis getResource() {
    return connectionPool.getResource();
  }
}
