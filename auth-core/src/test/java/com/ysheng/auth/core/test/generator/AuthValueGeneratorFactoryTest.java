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

package com.ysheng.auth.core.test.generator;

import com.ysheng.auth.core.generator.AuthValueGenerator;
import com.ysheng.auth.core.generator.AuthValueGeneratorFactory;
import com.ysheng.auth.model.configuration.core.AuthValueGeneratorConfiguration;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.fail;

import java.util.UUID;

/**
 * Tests for {@link com.ysheng.auth.core.generator.AuthValueGeneratorFactory}.
 */
public class AuthValueGeneratorFactoryTest {

  @Test
  public void failsWithNullGeneratorType() {
    AuthValueGeneratorConfiguration authValueGeneratorConfiguration = new AuthValueGeneratorConfiguration();
    authValueGeneratorConfiguration.setAuthCodeGeneratorType(null);

    try {
      AuthValueGeneratorFactory.produce(authValueGeneratorConfiguration);
      fail("Auth value generator factory should fail with null generator type");
    } catch (IllegalArgumentException ex) {
      assertThat(ex.getMessage(), equalTo("Value generator type cannot be null"));
    }
  }

  @Test
  public void failsWithUnknownGeneratorType() {
    AuthValueGeneratorConfiguration authValueGeneratorConfiguration = new AuthValueGeneratorConfiguration();
    authValueGeneratorConfiguration.setAuthCodeGeneratorType("unknownGeneratorType");

    try {
      AuthValueGeneratorFactory.produce(authValueGeneratorConfiguration);
      fail("Auth value generator factory should fail with unknown generator type");
    } catch (IllegalArgumentException ex) {
      assertThat(ex.getMessage(), equalTo("Unknown value generator type: unknownGeneratorType"));
    }
  }

  @Test
  public void succeedsToProduceWithNullConfiguration() {
    AuthValueGenerator generator = AuthValueGeneratorFactory.produce(null);
    assertThat(generator, notNullValue());
  }

  @Test(dataProvider = "UuidGeneratorType")
  public void succeedsToProduceUuidGenerator(String generatorType) {
    AuthValueGeneratorConfiguration authValueGeneratorConfiguration = new AuthValueGeneratorConfiguration();
    authValueGeneratorConfiguration.setAuthCodeGeneratorType(generatorType);

    AuthValueGenerator generator = AuthValueGeneratorFactory.produce(authValueGeneratorConfiguration);
    String authCode = generator.generateAuthCode();
    UUID authCodeUuid = UUID.fromString(authCode);
    assertThat(authCode, equalTo(authCodeUuid.toString()));
  }

  @DataProvider(name = "UuidGeneratorType")
  public Object[][] produceUuidGeneratorType() {
    return new Object[][] {
        { "Uuid" },
        { "uuid" },
        { "UUID" },
        { "uUiD" },
        { " uuid " },
    };
  }
}
