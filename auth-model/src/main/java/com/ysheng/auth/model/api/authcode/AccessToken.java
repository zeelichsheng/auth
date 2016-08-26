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

package com.ysheng.auth.model.api.authcode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysheng.auth.model.api.AccessTokenType;

/**
 * Defines the data structure of access token.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessToken {

  // The client identifier.
  @JsonProperty(required = true)
  private String clientId;

  // REQUIRED. The access token issued by the authorization server.
  @JsonProperty(required = true)
  private String accessToken;

  // REQUIRED. The type of the token.
  @JsonProperty
  private AccessTokenType tokenType;

  // RECOMMENDED. The lifetime in seconds of the access token.
  @JsonProperty
  private Long expiresIn;

  // OPTIONAL. The refresh token can be used to obtain new access tokens
  // using the same authorization grant.
  @JsonProperty
  private String refreshToken;

  // OPTIONAL if identical to the scope requested by the client. Otherwise
  // REQUIRED.
  @JsonProperty
  private String scope;

  ///
  /// Getters and Setters.
  ///

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public AccessTokenType getTokenType() {
    return tokenType;
  }

  public void setTokenType(AccessTokenType tokenType) {
    this.tokenType = tokenType;
  }

  public Long getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(Long expiresIn) {
    this.expiresIn = expiresIn;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }
}
