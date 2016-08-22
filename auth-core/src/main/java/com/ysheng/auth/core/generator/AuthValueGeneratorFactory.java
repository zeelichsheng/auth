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

package com.ysheng.auth.core.generator;

import com.ysheng.auth.model.configuration.core.AuthValueGeneratorConfiguration;

/**
 * Defines a factory that produces auth value generator.
 */
public class AuthValueGeneratorFactory {

  /**
   * Produces an auth value generator.
   *
   * @param configuration The auth value generator configuration.
   * @return An auth value generator.
   */
  public AuthValueGenerator produce(AuthValueGeneratorConfiguration configuration) {
    if (configuration == null) {
      configuration = new AuthValueGeneratorConfiguration();
    }

    return new AuthValueGeneratorImpl(
        produceValueGenerator(configuration.getClientIdGeneratorType()),
        produceValueGenerator(configuration.getClientSecretGeneratorType()),
        produceValueGenerator(configuration.getAuthCodeGeneratorType()),
        produceValueGenerator(configuration.getAccessTokenGeneratorType())
    );
  }

  private ValueGenerator produceValueGenerator(String valueGeneratorType) {
    if (valueGeneratorType == null) {
      throw new IllegalArgumentException("Value generator type cannot be null");
    }

    if (valueGeneratorType.trim().equalsIgnoreCase("UUID")) {
      return new UuidGenerator();
    }

    throw new IllegalArgumentException("Unknown value generator type: " + valueGeneratorType);
  }
}
