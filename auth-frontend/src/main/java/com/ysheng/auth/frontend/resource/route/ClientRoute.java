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
 * Defines the constant routes related to client operations.
 */
public class ClientRoute {

  ///
  /// Path Param.
  ///

  // The path parameter of client identifier.
  public static final String CLIENT_ID_PATH_PARAM = "id";

  ///
  /// Path.
  ///

  // This is the clients root path, i.e. /clients
  public static final String CLIENTS_PATH = BaseRoute.BASE_PATH + "/clients";

  // This is the individual client path, i.e. /clients/{id}
  public static final String CLIENT_PATH = CLIENTS_PATH + "/{" + CLIENT_ID_PATH_PARAM + "}";

  // This is the path to unregister client, i.e. /clients/{id}/unregister
  public static final String UNREGISTER_CLIENT_ACTION = "/unregister";
}
