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

import com.ysheng.auth.core.exception.ClientRegistrationException;
import com.ysheng.auth.model.client.ClientRegistrationRequest;
import com.ysheng.auth.model.client.ClientRegistrationResponse;

/**
 * Defines the interface of client related functions.
 */
public interface ClientService {

  /**
   * Registers a client with the authentication server.
   *
   * @param request The client registration request that contains required information.
   * @return The client registration response that contains the client identifier and secret.
   * @throws ClientRegistrationException The exception that contains error details.
   */
  ClientRegistrationResponse registerClient(ClientRegistrationRequest request) throws ClientRegistrationException;
}
