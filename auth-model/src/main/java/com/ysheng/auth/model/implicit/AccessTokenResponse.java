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
package com.ysheng.auth.model.implicit;

/**
 * Defines the data structure of access token response for Implicit Grant.
 */
public class AccessTokenResponse {

  // REQUIRED. The access token issued by the authorization server.
  private String accessToken;

  // REQUIRED. The type of the token.
  private String tokenType;

  // RECOMMENDED. The lifetime in seconds of the access token.
  private Long expiresIn;

  // OPTIONAL if identical to the scope requested by the client. Otherwise
  // REQUIRED.
  private String scope;

  // REQUIRED if the "state" parameter was present in the client
  // authorization request.
  private String state;

  ///
  /// Getters and Setters.
  ///

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public Long getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(Long expiresIn) {
    this.expiresIn = expiresIn;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
