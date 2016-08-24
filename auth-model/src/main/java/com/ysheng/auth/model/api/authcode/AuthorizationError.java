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
 * Defines the data structure of authorization error for Authorization Code Grant.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationError extends InternalException {

  // REQUIRED. A single ASCII error code.
  @JsonProperty
  private AuthorizationErrorType error;

  // OPTIONAL. Human-readable ASCII text providing additional
  // information.
  @JsonProperty
  private String errorDescription;

  // OPTIONAL. A URI identifying a human-readable web page with
  // information about the error.
  @JsonProperty
  private String errorUri;

  // REQUIRED if a "state" parameter was present in the client
  // authorization request.
  @JsonProperty
  private String state;

  /**
   * Constructs an AuthorizationError object.
   *
   * @param error The error code.
   * @param errorDescription The error message.
   */
  public AuthorizationError(
      AuthorizationErrorType error,
      String errorDescription) {
    super(errorDescription);
    this.error = error;
    this.errorDescription = errorDescription;
  }

  @Override
  public Response.Status getHttpStatusCode() {
    switch (error) {
      case INVALID_REQUEST:
      case UNSUPPORTED_RESPONSE_TYPE:
        return Response.Status.BAD_REQUEST;
      case UNAUTHORIZED_CLIENT:
      case ACCESS_DENIDED:
      case INVALID_SCOPE:
        return Response.Status.UNAUTHORIZED;
      case SERVER_ERROR:
        return Response.Status.INTERNAL_SERVER_ERROR;
      case TEMPORARILY_UNAVAILABLE:
        return Response.Status.SERVICE_UNAVAILABLE;
    }

    return Response.Status.INTERNAL_SERVER_ERROR;
  }

  @Override
  public String getInternalErrorCode() {
    return error.toString();
  }

  @Override
  public String getInternalErrorDescription() {
    return errorDescription;
  }

  ///
  /// Getters and Setters.
  ///

  public AuthorizationErrorType getError() {
    return error;
  }

  public void setError(AuthorizationErrorType error) {
    this.error = error;
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }

  public String getErrorUri() {
    return errorUri;
  }

  public void setErrorUri(String errorUri) {
    this.errorUri = errorUri;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
