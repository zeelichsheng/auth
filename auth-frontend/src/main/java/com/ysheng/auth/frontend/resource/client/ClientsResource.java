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
import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.model.api.client.ClientRegistrationError;
import com.ysheng.auth.model.api.client.ClientRegistrationRequest;
import com.ysheng.auth.model.api.client.ClientRegistrationResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Defines the RESTful endpoints related to client operations.
 */
@Path(ClientRoute.API)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientsResource {

  // The client service that performs backend operation.
  private final ClientService clientService;

  /**
   * Constructs a ClientsResource object.
   *
   * @param clientService The client service that performs backend operation.
   */
  public ClientsResource(ClientService clientService) {
    this.clientService = clientService;
  }

  @POST
  public ClientRegistrationResponse register(
      ClientRegistrationRequest request) throws ClientRegistrationError {
    return clientService.register(request);
  }

  @GET
  public ApiList<Client> list() {
    return clientService.list();
  }
}
