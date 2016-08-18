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

package com.ysheng.auth.model.database;

import com.ysheng.auth.model.ClientType;

/**
 * Defines the data structure of auth client.
 */
public class Client implements DatabaseObject {

  // Type of the client.
  @DatabaseObjectDataField
  private ClientType type;

  // ID of the client.
  @DatabaseObjectDataField
  private String id;

  // Secret of the client.
  @DatabaseObjectDataField
  private String secret;

  // Redirect URI of the client.
  @DatabaseObjectDataField
  private String redirectUri;

  ///
  /// Getters and Setters.
  ///

  public ClientType getType() {
    return type;
  }

  public void setType(ClientType type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }
}
