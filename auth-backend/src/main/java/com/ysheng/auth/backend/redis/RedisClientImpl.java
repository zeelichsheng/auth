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

import com.ysheng.auth.backend.redis.entity.Entity;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

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
   * Implements hmset command in Redis.
   *
   * @param entity The entity to be executed with hmset command.
   */
  public void hmset(Entity entity) {
    doRedis(
        resource -> {
          resource.hmset(
              entity.getKey(),
              entity.getHash()
          );
        }
    );
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
