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
 * Defines the configuration of auth value generator.
 */
public class AuthValueGeneratorConfiguration {

  // The default value generator type.
  private static final String DEFAULT_GENERATOR_TYPE = "UUID";

  // Type of client ID generator.
  @JsonProperty
  private String clientIdGeneratorType = DEFAULT_GENERATOR_TYPE;

  // Type of client secret generator.
  @JsonProperty
  private String clientSecretGeneratorType = DEFAULT_GENERATOR_TYPE;

  // Type of auth code generator.
  @JsonProperty
  private String authCodeGeneratorType = DEFAULT_GENERATOR_TYPE;

  // Type of access token generator.
  @JsonProperty
  private String accessTokenGeneratorType = DEFAULT_GENERATOR_TYPE;

  ///
  /// Getters and Setters.
  ///

  public String getClientIdGeneratorType() {
    return clientIdGeneratorType;
  }

  public void setClientIdGeneratorType(String clientIdGeneratorType) {
    this.clientIdGeneratorType = clientIdGeneratorType;
  }

  public String getClientSecretGeneratorType() {
    return clientSecretGeneratorType;
  }

  public void setClientSecretGeneratorType(String clientSecretGeneratorType) {
    this.clientSecretGeneratorType = clientSecretGeneratorType;
  }

  public String getAuthCodeGeneratorType() {
    return authCodeGeneratorType;
  }

  public void setAuthCodeGeneratorType(String authCodeGeneratorType) {
    this.authCodeGeneratorType = authCodeGeneratorType;
  }

  public String getAccessTokenGeneratorType() {
    return accessTokenGeneratorType;
  }

  public void setAccessTokenGeneratorType(String accessTokenGeneratorType) {
    this.accessTokenGeneratorType = accessTokenGeneratorType;
  }
}
