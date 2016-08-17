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

/**
 * Defines the error types used in the access token error response.
 */
public enum AccessTokenErrorType {
  INVALID_REQUEST,
  INVALID_CLIENT,
  INVALID_GRANT,
  UNAUTHORIZED_CLIENT,
  UNSUPPORTED_GRANT_TYPE,
  INVALID_SCOPE
}
