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

import javax.ws.rs.core.Response;

/**
 * Defines the enum of error types.
 */
public enum ErrorType {
  ACCESS_DENIDED(Response.Status.UNAUTHORIZED),
  SERVER_ERROR(Response.Status.INTERNAL_SERVER_ERROR),
  TEMPORARY_UNAVAILABLE(Response.Status.SERVICE_UNAVAILABLE),
  INVALID_REQUEST(Response.Status.BAD_REQUEST),
  INVALID_CLIENT(Response.Status.BAD_REQUEST),
  CLIENT_NOT_FOUND(Response.Status.NOT_FOUND),
  CLIENT_UNAUTHORIZED(Response.Status.UNAUTHORIZED),
  AUTHORIZATION_TICKET_NOT_FOUND(Response.Status.NOT_FOUND),
  INVALID_GRANT(Response.Status.BAD_REQUEST),
  GRANT_TYPE_UNSUPPORTED(Response.Status.BAD_REQUEST),
  INVALID_SCOPE(Response.Status.BAD_REQUEST),
  RESPONSE_TYPE_UNSUPPORTED(Response.Status.BAD_REQUEST),
  ACCESS_TOKEN_NOT_FOUND(Response.Status.NOT_FOUND);

  private Response.Status httpStatus;

  ErrorType(Response.Status httpStatus) {
    this.httpStatus = httpStatus;
  }

  ///
  /// Getters.
  ///

  public Response.Status getHttpStatus() {
    return httpStatus;
  }
}
