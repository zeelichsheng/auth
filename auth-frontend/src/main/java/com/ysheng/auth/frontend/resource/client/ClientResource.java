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

package com.ysheng.auth.frontend.resource.client;

import com.ysheng.auth.core.ClientService;
import com.ysheng.auth.frontend.resource.route.ClientRoute;
import com.ysheng.auth.model.client.ClientUnregistrationError;
import com.ysheng.auth.model.client.ClientUnregistrationRequest;
import com.ysheng.auth.model.client.ClientUnregistrationResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Defines the RESTful endpoints related to individual client operations.
 */
@Path(ClientRoute.CLIENT_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientResource {

  // The client service that performs backend operation.
  private final ClientService clientService;

  /**
   * Constructs a ClientResource object.
   *
   * @param clientService The client service that performs backend operation.
   */
  public ClientResource(ClientService clientService) {
    this.clientService = clientService;
  }

  @POST
  @Path(ClientRoute.CLIENT_UNREGISTER_ACTION)
  public ClientUnregistrationResponse unregister(
      ClientUnregistrationRequest request) throws ClientUnregistrationError {
    return clientService.unregisterClient(request);
  }
}