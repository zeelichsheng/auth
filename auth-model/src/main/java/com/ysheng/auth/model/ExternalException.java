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

package com.ysheng.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines the data structure of the API external exception.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalException {

  // The error code.
  @JsonProperty
  private String errorCode;

  // The error description.
  @JsonProperty
  private String errorDescription;

  /**
   * Constructs an ExternalException object.
   * Note that this constructor is used by Jackson
   * to construct an empty object for deserialization
   * purpose.
   */
  public ExternalException() {
  }

  /**
   * Constructs an ExternalException object.
   *
   * @param errorCode The error code.
   * @param errorDescription The error detail.
   */
  public ExternalException(
      String errorCode,
      String errorDescription) {
    this.errorCode = errorCode;
    this.errorDescription = errorDescription;
  }

  ///
  /// Getters and Setters.
  ///

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }
}
