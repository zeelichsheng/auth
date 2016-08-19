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
 * Defines the interface to generate auth related values.
 */
public interface AuthValueGenerator {

  /**
   * Generates a client identifier.
   *
   * @return An unique client identifier.
   */
  String generateClientId();

  /**
   * Generates a client secret.
   *
   * @return An unique client secret.
   */
  String generateClientSecret();

  /**
   * Generates an authorization code.
   *
   * @return An unique authorization code.
   */
  String generateAuthCode();

  /**
   * Generates an access token.
   *
   * @return An unique access token.
   */
  String generateAccessToken();
}
