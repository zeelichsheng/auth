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

package com.ysheng.auth.model.client;

import com.ysheng.auth.model.InternalException;

import javax.ws.rs.core.Response;

/**
 * Defines the data structure of client registration error response.
 */
public class ClientRegistrationError extends InternalException {

  // Client registration error code.
  private ClientRegistrationErrorType error;

  // Human-readable ASCII text providing additional information.
  private String errorDescription;

  /**
   * Constructs a ClientRegistrationError object.
   *
   * @param error The error code.
   * @param errorDescription The error message.
   */
  public ClientRegistrationError(
      ClientRegistrationErrorType error,
      String errorDescription) {
    super(errorDescription);
    this.error = error;
    this.errorDescription = errorDescription;
  }

  @Override
  public Response.Status getHttpStatusCode() {
    switch (error) {
      case INVALID_REQUEST:
      case ALREADY_REGISTERED:
        return Response.Status.BAD_REQUEST;
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

  public ClientRegistrationErrorType getError() {
    return error;
  }

  public void setError(ClientRegistrationErrorType error) {
    this.error = error;
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }
}
