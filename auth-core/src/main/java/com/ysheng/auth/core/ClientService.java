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

import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.exception.InternalException;
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.model.api.client.ClientRegistrationSpec;
import com.ysheng.auth.model.api.client.ClientUnregistrationSpec;

/**
 * Defines the interface of client related functions.
 */
public interface ClientService {

  /**
   * Registers a client with the authorization server.
   *
   * @param request The client registration request that contains required information.
   * @return The client object.
   * @throws InternalException The exception that contains error details.
   */
  Client register(ClientRegistrationSpec request) throws InternalException;

  /**
   * Unregisters a client with the authorization server.
   *
   * @param clientId The client identifier.
   * @param request The client unregistration request that contains required information.
   * @throws InternalException The exception that contains error details.
   */
  void unregister(
      String clientId,
      ClientUnregistrationSpec request) throws InternalException;

  /**
   * Gets a list of all clients.
   *
   * @return The list of all clients.
   */
  ApiList<Client> list();

  /**
   * Gets a client with the given identifier.
   *
   * @param clientId The client identifier.
   * @return A client object with the give identifier.
   * @throws InternalException The error that contains detail information.
   */
  Client get(String clientId) throws InternalException;
}
