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

package com.ysheng.auth.model.api.exception;

/**
 * Defines the data structure of access token not found error.
 */
public class AccessTokenNotFoundException extends InternalException {

  /**
   * Constructs a AccessTokenNotFoundException object.
   *
   * @param clientId The client identifier.
   * @param accessToken The access token.
   */
  public AccessTokenNotFoundException(
      String clientId,
      String accessToken) {
    super(ErrorType.ACCESS_TOKEN_NOT_FOUND,
        "Access token not found with client ID: " + clientId + " and token: " + accessToken);
  }
}
