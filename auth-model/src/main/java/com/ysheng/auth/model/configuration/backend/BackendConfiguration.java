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

package com.ysheng.auth.model.configuration.backend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines the configuration for backend.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BackendConfiguration {

  // The type of the database;
  @JsonProperty(required = true)
  private String databaseType;

  // Redis specific configuration.
  @JsonProperty
  private RedisConfiguration redisConfiguration;

  ///
  /// Getters and Setters.
  ///

  public String getDatabaseType() {
    return databaseType;
  }

  public void setDatabaseType(String databaseType) {
    this.databaseType = databaseType;
  }

  public RedisConfiguration getRedisConfiguration() {
    return redisConfiguration;
  }

  public void setRedisConfiguration(RedisConfiguration redisConfiguration) {
    this.redisConfiguration = redisConfiguration;
  }
}
