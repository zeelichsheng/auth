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

package com.ysheng.auth.common.core.exception;

import com.ysheng.auth.model.authcode.AccessTokenErrorType;

/**
 * Defines an exception type related to access token failure for Authorization Code Grant.
 */
public class AuthCodeAccessTokenException extends Exception {

  // The error code.
  private AccessTokenErrorType error;

  // The error description.
  private String errorDescription;

  /**
   * Constructs an AuthCodeAccessTokenException object.
   *
   * @param error The error code.
   * @param errorDescription The error description.
   */
  public AuthCodeAccessTokenException(
      AccessTokenErrorType error,
      String errorDescription) {
    super(errorDescription);

    this.error = error;
    this.errorDescription = errorDescription;
  }

  ///
  /// Getters and Setters.
  ///

  public AccessTokenErrorType getError() {
    return error;
  }

  public void setError(AccessTokenErrorType error) {
    this.error = error;
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }
}
