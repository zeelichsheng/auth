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

  public static final String CLIENT_ID_PATH_PARAM = "clientId";

  public static final String CODE_PATH_PARAM = "code";

  // This is the auth codes path, i.e. /auth-codes/{clientId}
  public static final String API = "/auth-codes/{" + CLIENT_ID_PATH_PARAM + "}";

  // This is the individual auth code path, i.e. /auth-codes/{clientId}/{code}
  public static final String AUTHORIZATION_PATH = API + "/{" + CODE_PATH_PARAM + "}";

  // This is the access tokens path, i.e. /auth-codes/{cliendId}/{code}/access-tokens
  public static final String ACCESS_TOKENS_API = AUTHORIZATION_PATH +  "/access-tokens";
}
