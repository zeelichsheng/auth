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
 * Defines the data structure that represents an API exception.
 */
public class InternalException extends Exception {

  // The error code.
  private ErrorType error;

  // The error description.
  private String errorDescription;

  /**
   * Constructs an InternalException object.
   *
   * @param errorDescription The error detail.
   */
  public InternalException(
      ErrorType error,
      String errorDescription) {
    super(errorDescription);
    this.error = error;
    this.errorDescription = errorDescription;
  }

  public Response.Status getHttpStatusCode() {
    return error.getHttpStatus();
  }

  public String getErrorCode() {
    return error.name();
  }

  public String getErrorDescription() {
    return errorDescription;
  }
}
