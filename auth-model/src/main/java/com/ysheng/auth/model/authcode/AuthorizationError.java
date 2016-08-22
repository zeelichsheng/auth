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

package com.ysheng.auth.model.authcode;

import com.ysheng.auth.model.BaseException;

/**
 * Defines the data structure of authorization error for Authorization Code Grant.
 */
public class AuthorizationError extends BaseException {

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

  // REQUIRED. A single ASCII error code.
  private AuthorizationErrorType error;

  // OPTIONAL. Human-readable ASCII text providing additional
  // information.
  private String errorDescription;

  // OPTIONAL. A URI identifying a human-readable web page with
  // information about the error.
  private String errorUri;

  // REQUIRED if a "state" parameter was present in the client
  // authorization request.
  private String state;

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
