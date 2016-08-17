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

package com.ysheng.auth.common.core;

import com.ysheng.auth.common.core.exception.AuthCodeAccessTokenException;
import com.ysheng.auth.common.core.exception.AuthCodeAuthorizationException;
import com.ysheng.auth.model.authcode.AccessTokenRequest;
import com.ysheng.auth.model.authcode.AccessTokenResponse;
import com.ysheng.auth.model.authcode.AuthorizationRequest;
import com.ysheng.auth.model.authcode.AuthorizationResponse;

/**
 * Defines the interface of authorization related functions.
 */
public interface AuthorizationService {

  /**
   * Authorizes an authorization request of Authorization Code Grant type.
   *
   * @param request The authorization request that contains required information.
   * @return The authorization response that contains the authorization code.
   * @throws AuthCodeAuthorizationException The exception contains error details.
   */
  AuthorizationResponse authorize(AuthorizationRequest request) throws AuthCodeAuthorizationException;

  /**
   * Issues an access token for a client with an auth code received from authorization of
   * Authorization Code Grant type.
   *
   * @param request The access token request that contains required information.
   * @return The access token response that contains the access token.
   * @throws AuthCodeAccessTokenException The exception contains error details.
   */
  AccessTokenResponse issueAccessToken(AccessTokenRequest request) throws AuthCodeAccessTokenException;
}
