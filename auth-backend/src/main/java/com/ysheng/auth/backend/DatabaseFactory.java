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

package com.ysheng.auth.backend;

import com.ysheng.auth.backend.redis.RedisClientImpl;
import com.ysheng.auth.backend.redis.RedisDatabase;
import com.ysheng.auth.backend.redis.connection.RedisConnection;
import com.ysheng.auth.backend.redis.connection.RedisSentinelConnection;
import com.ysheng.auth.backend.redis.connection.RedisSimpleConnection;
import com.ysheng.auth.model.configuration.backend.BackendConfiguration;
import com.ysheng.auth.model.configuration.backend.RedisConfiguration;

/**
 * Defines a database factory that produces database object.
 */
public class DatabaseFactory {

  /**
   * Produces a database object.
   *
   * @param backendConfiguration The backend configuration.
   * @return A database object.
   * @throws Exception The exception that contains detail error description.
   */
  public Database produce(BackendConfiguration backendConfiguration) throws Exception {
    if (backendConfiguration.getDatabaseType() == null) {
      throw new IllegalArgumentException("Database type cannot be null");
    }

    String databaseType = backendConfiguration.getDatabaseType().trim();

    if (databaseType.equalsIgnoreCase("redis")) {
      return produceRedisDatabase(backendConfiguration.getRedisConfiguration());
    }

    throw new IllegalArgumentException("Unknown database type: " + databaseType);
  }

  private Database produceRedisDatabase(RedisConfiguration redisConfiguration) {
    RedisConnection connection;
    if (redisConfiguration.getConnectionType().equalsIgnoreCase("simple")) {
      connection = new RedisSimpleConnection(redisConfiguration);
    } else if (redisConfiguration.getConnectionType().equalsIgnoreCase("sentinel")) {
      connection = new RedisSentinelConnection(redisConfiguration);
    } else {
      throw new IllegalArgumentException("Unknown Redis connection type: " + redisConfiguration.getConnectionType());
    }

    return new RedisDatabase(new RedisClientImpl(connection));
  }
}
