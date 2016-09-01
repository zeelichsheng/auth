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
 * Defines the constant routes related to implict grant operations.
 */
public class ImplicitRoute {

  ///
  /// Path Param.
  ///

  // The path parameter of client identifier.
  public static final String CLIENT_ID_PATH_PARAM = "clientId";

  // The path parameter of access token.
  public static final String ACCESS_TOKEN_PATH_PARAM = "accessToken";

  ///
  /// Path.
  ///

  // This is the auth codes root path, i.e. /implicit/{clientId}
  private static final String ROOT_PATH = BaseRoute.BASE_PATH + "/implicit/{" + CLIENT_ID_PATH_PARAM + "}";

  // This is the access tokens path, i.e. /implicit/{cliendId}/access-tokens
  public static final String ACCESS_TOKENS_PATH = ROOT_PATH +  "/access-tokens";

  // This is the individual access token path, i.e. /implicit/{clientId}/access-tokens/{accessToken}
  public static final String ACCESS_TOKEN_PATH = ACCESS_TOKENS_PATH + "/{" + ACCESS_TOKEN_PATH_PARAM + "}";

  // This is the path to revoke access token, i.e /implicit/{clientId}/access-tokens/{accessToken}/revoke-access-token
  public static final String REVOKE_ACCESS_TOKEN_ACTION = "/revoke";
}
