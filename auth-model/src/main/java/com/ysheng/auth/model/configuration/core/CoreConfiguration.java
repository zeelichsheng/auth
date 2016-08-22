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

package com.ysheng.auth.model.configuration.core;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines the configuration of the core.
 */
public class CoreConfiguration {

  // The auth value generator configuration.
  @JsonProperty
  private AuthValueGeneratorConfiguration authValueGeneratorConfiguration;

  ///
  /// Getters and Setters.
  ///

  public AuthValueGeneratorConfiguration getAuthValueGeneratorConfiguration() {
    return authValueGeneratorConfiguration;
  }

  public void setAuthValueGeneratorConfiguration(AuthValueGeneratorConfiguration authValueGeneratorConfiguration) {
    this.authValueGeneratorConfiguration = authValueGeneratorConfiguration;
  }
}
