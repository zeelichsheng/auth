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

package com.ysheng.auth.frontend.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysheng.auth.model.configuration.backend.BackendConfiguration;
import com.ysheng.auth.model.configuration.core.CoreConfiguration;
import io.dropwizard.Configuration;

/**
 * Defines the data structure that contains API configuration.
 */
public class ApiConfiguration extends Configuration {

  @JsonProperty
  private CoreConfiguration coreConfiguration;

  @JsonProperty
  private BackendConfiguration backendConfiguration;

  ///
  /// Getters and Setters.
  ///

  public CoreConfiguration getCoreConfiguration() {
    return coreConfiguration;
  }

  public void setCoreConfiguration(CoreConfiguration coreConfiguration) {
    this.coreConfiguration = coreConfiguration;
  }

  public BackendConfiguration getBackendConfiguration() {
    return backendConfiguration;
  }

  public void setBackendConfiguration(BackendConfiguration backendConfiguration) {
    this.backendConfiguration = backendConfiguration;
  }
}
