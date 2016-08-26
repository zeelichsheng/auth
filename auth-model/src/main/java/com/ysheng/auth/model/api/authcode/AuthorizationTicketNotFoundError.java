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
import com.ysheng.auth.model.api.InternalException;

import javax.ws.rs.core.Response;

/**
 * Defines the data structure of authorization not found error.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationTicketNotFoundError extends InternalException {

  // The client identifier.
  @JsonProperty
  private String clientId;

  // The authorization code.
  @JsonProperty
  private String code;

  /**
   * Constructs a AuthorizationTicketNotFoundError object.
   *
   * @param clientId The client identifier.
   * @param code The authorization code.
   */
  public AuthorizationTicketNotFoundError(
      String clientId,
      String code) {
    super("Authorization ticket not found with client ID: " + clientId +
      " and code: " + code);
    this.clientId = clientId;
    this.code = code;
  }

  @Override
  public Response.Status getHttpStatusCode() {
    return Response.Status.NOT_FOUND;
  }

  @Override
  public String getInternalErrorCode() {
    return "AuthorizationTicketNotFound";
  }

  @Override
  public String getInternalErrorDescription() {
    return getMessage();
  }
}
