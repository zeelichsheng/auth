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

package com.ysheng.auth.core;

import com.ysheng.auth.model.api.authcode.AccessTokenError;
import com.ysheng.auth.model.api.authcode.AccessTokenRequest;
import com.ysheng.auth.model.api.authcode.AccessTokenResponse;
import com.ysheng.auth.model.api.authcode.AuthorizationError;
import com.ysheng.auth.model.api.authcode.AuthorizationRequest;
import com.ysheng.auth.model.api.authcode.AuthorizationResponse;

/**
 * Defines the interface of authorization code grant related functions.
 */
public interface AuthCodeGrantService {

  /**
   * Authorizes an authorization request of Authorization Code Grant type.
   *
   * @param request The authorization request that contains required information.
   * @return The authorization response that contains the authorization code.
   * @throws AuthorizationError The exception that contains error details.
   */
  AuthorizationResponse authorize(AuthorizationRequest request) throws AuthorizationError;

  /**
   * Issues an access token for a client with an auth code received from authorization of
   * Authorization Code Grant type.
   *
   * @param request The access token request that contains required information.
   * @return The access token response that contains the access token.
   * @throws AccessTokenError The exception that contains error details.
   */
  AccessTokenResponse issueAccessToken(AccessTokenRequest request) throws AccessTokenError;
}
