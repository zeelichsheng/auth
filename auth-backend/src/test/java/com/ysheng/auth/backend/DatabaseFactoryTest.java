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

import com.sun.javaws.exceptions.InvalidArgumentException;
import com.ysheng.auth.backend.redis.RedisDatabase;
import com.ysheng.auth.model.configuration.backend.BackendConfiguration;
import com.ysheng.auth.model.configuration.backend.RedisConfiguration;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.fail;

/**
 * Tests for {@link com.ysheng.auth.backend.DatabaseFactory}
 */
public class DatabaseFactoryTest {

  @Test
  public void failsWithNullDatabaseType() throws Throwable {
    BackendConfiguration backendConfiguration = new BackendConfiguration();
    backendConfiguration.setDatabaseType(null);

    try {
      DatabaseFactory.produce(backendConfiguration);
      fail("Database factory should fail with null database type");
    } catch (IllegalArgumentException ex) {
      assertThat(ex.getMessage(), equalTo("Database type cannot be null"));
    }
  }

  @Test
  public void failsWithUnknownDatabaseType() throws Throwable {
    BackendConfiguration backendConfiguration = new BackendConfiguration();
    backendConfiguration.setDatabaseType("unknownDatabaseType");

    try {
      DatabaseFactory.produce(backendConfiguration);
      fail("Database factory should fail with invalid database type");
    } catch (IllegalArgumentException ex) {
      assertThat(ex.getMessage(), equalTo("Unknown database type: unknownDatabaseType"));
    }
  }

  @Test(dataProvider = "RedisDatabaseType")
  public void succeedsToProduceRedisDatabase(String databaseType) throws Throwable {
    RedisConfiguration redisConfiguration = new RedisConfiguration();
    BackendConfiguration backendConfiguration = new BackendConfiguration();
    backendConfiguration.setDatabaseType(databaseType);
    backendConfiguration.setRedisConfiguration(redisConfiguration);

    Database database = DatabaseFactory.produce(backendConfiguration);
    assertThat(database.getClass(), equalTo(RedisDatabase.class));
  }

  @DataProvider(name = "RedisDatabaseType")
  public Object[][] provideRedisDatabaseType() {
    return new Object[][] {
        { "Redis" },
        { "redis" },
        { "REDIS" },
        { "rEdIs" },
        { " Redis "}
    };
  }
}
