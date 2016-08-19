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

package com.ysheng.auth.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines the data structure of an authorization ticket that contains auth
 * related information being persisted in the database.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationTicket {

  // The authorization code.
  @JsonProperty(required = true)
  private String code;

  // The client identifier.
  @JsonProperty(required = true)
  private String clientId;

  // The URI the resource owner is redirected to.
  @JsonProperty
  private String redirectUri;

  // The scope of the access request.
  @JsonProperty
  private String scope;

  // An opaque value used by the client to maintain state between
  // the request and the callback.
  @JsonProperty
  private String state;

  ///
  /// Getters and Setters.
  ///

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
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
