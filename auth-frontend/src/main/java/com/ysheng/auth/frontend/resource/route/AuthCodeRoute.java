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

package com.ysheng.auth.frontend.resource.route;

/**
 * Defines the constant routes related to authorization code grant operations.
 */
public class AuthCodeRoute {

  ///
  /// Path Param.
  ///

  // The path parameter of client identifier.
  public static final String CLIENT_ID_PATH_PARAM = "clientId";

  // The path parameter of authorization code.
  public static final String CODE_PATH_PARAM = "code";

  // The path parameter of access token.
  public static final String ACCESS_TOKEN_PATH_PARAM = "accessToken";

  ///
  /// Path.
  ///

  // This is the auth codes root path, i.e. /auth-codes/{clientId}
  private static final String ROOT_PATH = "/auth-codes/{" + CLIENT_ID_PATH_PARAM + "}";

  // This is tha authorization codes path, i.e. /auth-codes/{clientId}/authorizations
  public static final String AUTHORIZATIONS_PATH = ROOT_PATH + "/authorizations";

  // This is the individual auth code path, i.e. /auth-codes/{clientId}/authorizations/{code}
  public static final String AUTHORIZATION_PATH = AUTHORIZATIONS_PATH + "/{" + CODE_PATH_PARAM + "}";

  // This is the path to revoke authorization, i.e. /auth-codes/{clientId}/authorizations/{code}/revoke-authorization
  public static final String REVOKE_AUTHORIZATION_ACTION = "/revoke-authorization";

  // This is the access tokens path, i.e. /auth-codes/{cliendId}/access-tokens
  public static final String ACCESS_TOKENS_PATH = ROOT_PATH +  "/access-tokens";

  // This is the individual access token path, i.e. /auth-codes/{clientId}/access-tokens/{accessToken}
  public static final String ACCESS_TOKEN_PATH = ACCESS_TOKENS_PATH + "/{" + ACCESS_TOKEN_PATH_PARAM + "}";

  // This is the path to revoke access token, i.e /auth-codes/{clientId}/access-tokens/{accessToken}/revoke-access-token
  public static final String REVOKE_ACCESS_TOKEN_ACTION = "/revoke-access-token";
}
