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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysheng.auth.model.ResponseType;

/**
 * Defines the data structure of authorization request for Implicit Grant.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationRequest {

  // Valid response type.
  public static final ResponseType VALID_RESPONSE_TYPE = ResponseType.TOKEN;

  // REQUIRED. Value must be set to "token".
  @JsonProperty
  private ResponseType responseType;

  // REQUIRED. The client identifier.
  @JsonProperty
  private String clientId;

  // OPTIONAL. The URI the resource owner is redirected to after the request
  // is fulfilled.
  @JsonProperty
  private String redirectUri;

  // OPTIONAL. The scope of the access request.
  @JsonProperty
  private String scope;

  // RECOMMENDED. An opaque value used by the client to maintain state between
  // the request and callback.
  @JsonProperty
  private String state;

  ///
  /// Getters and Setters.
  ///

  public ResponseType getResponseType() {
    return responseType;
  }

  public void setResponseType(ResponseType responseType) {
    this.responseType = responseType;
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
