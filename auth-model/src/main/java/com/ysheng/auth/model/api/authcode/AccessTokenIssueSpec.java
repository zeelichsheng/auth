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
import com.ysheng.auth.model.api.GrantType;

/**
 * Defines the data structure of access token request for Authorization Code Grant.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenIssueSpec {

  // Valid grant type.
  public static final GrantType VALID_GRANT_TYPE = GrantType.AUTHORIZATION_CODE;

  // REQUIRED. The authorization code.
  @JsonProperty
  private String code;

  // REQUIRED. Value must be set to "authorization_code".
  @JsonProperty
  private GrantType grantType;

  // REQUIRED if the "redirect_uri" parameter was included in the
  // authorization request, and their value must be identical.
  @JsonProperty
  private String redirectUri;

  // REQUIRED. The client secret used to validate if the request is authorized.
  @JsonProperty
  private String clientSecret;

  ///
  /// Getters and Setters.
  ///

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public GrantType getGrantType() {
    return grantType;
  }

  public void setGrantType(GrantType grantType) {
    this.grantType = grantType;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }
}
