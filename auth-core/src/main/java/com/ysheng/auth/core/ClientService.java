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
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.model.api.client.ClientNotFoundError;
import com.ysheng.auth.model.api.client.ClientRegistrationError;
import com.ysheng.auth.model.api.client.ClientRegistrationRequest;
import com.ysheng.auth.model.api.client.ClientRegistrationResponse;
import com.ysheng.auth.model.api.client.ClientUnregistrationError;
import com.ysheng.auth.model.api.client.ClientUnregistrationRequest;
import com.ysheng.auth.model.api.client.ClientUnregistrationResponse;

/**
 * Defines the interface of client related functions.
 */
public interface ClientService {

  /**
   * Registers a client with the authorization server.
   *
   * @param request The client registration request that contains required information.
   * @return The client registration response that contains the client identifier and secret.
   * @throws ClientRegistrationError The exception that contains error details.
   */
  ClientRegistrationResponse register(ClientRegistrationRequest request) throws ClientRegistrationError;

  /**
   * Unregisters a client with the authorization server.
   *
   * @param clientId The client identifier.
   * @param request The client unregistration request that contains required information.
   * @return The client unregistration response.
   * @throws ClientUnregistrationError The exception that contains error details.
   */
  ClientUnregistrationResponse unregister(
      String clientId,
      ClientUnregistrationRequest request) throws ClientUnregistrationError;

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
   * @throws ClientNotFoundError The error that contains detail information.
   */
  Client get(String clientId) throws ClientNotFoundError;
}
