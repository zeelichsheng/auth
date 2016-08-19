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

/**
 * Implementation of the generator that generates auth related values.
 */
public class AuthValueGeneratorImpl implements AuthValueGenerator {

  // Client ID generator.
  private ValueGenerator clientIdGenerator;

  // Client secret generator.
  private ValueGenerator clientSecretGenerator;

  // Authorization code generator.
  private ValueGenerator authCodeGenerator;

  // Access token generator.
  private ValueGenerator accessTokenGenerator;

  /**
   * Constructs a AuthValueGeneratorImpl object.
   *
   * @param clientIdGenerator The client ID generator.
   * @param clientSecretGenerator The client secret generator.
   * @param authCodeGenerator The authorization token generator.
   * @param accessTokenGenerator The access token generator.
   */
  public AuthValueGeneratorImpl(
      ValueGenerator clientIdGenerator,
      ValueGenerator clientSecretGenerator,
      ValueGenerator authCodeGenerator,
      ValueGenerator accessTokenGenerator) {
    this.clientIdGenerator = clientIdGenerator;
    this.clientSecretGenerator = clientSecretGenerator;
    this.authCodeGenerator = authCodeGenerator;
    this.accessTokenGenerator = accessTokenGenerator;
  }

  /**
   * Generates a client identifier.
   *
   * @return An unique client identifier.
   */
  public String generateClientId() {
    return clientIdGenerator.generate().toString();
  }

  /**
   * Generates a client secret.
   *
   * @return An unique client secret.
   */
  public String generateClientSecret() {
    return clientSecretGenerator.generate().toString();
  }

  /**
   * Generates an authorization code.
   *
   * @return An unique authorization code.
   */
  public String generateAuthCode() {
    return authCodeGenerator.generate().toString();
  }

  /**
   * Generates an access token.
   *
   * @return An unique access token.
   */
  public String generateAccessToken() {
    return accessTokenGenerator.generate().toString();
  }
}
