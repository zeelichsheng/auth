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
import com.ysheng.auth.frontend.resource.ResponseBuilder;
import com.ysheng.auth.frontend.resource.route.ClientRoute;
import com.ysheng.auth.model.api.client.ClientUnregistrationSpec;
import com.ysheng.auth.model.api.exception.InternalException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

  @GET
  public Response get(
      @PathParam(ClientRoute.CLIENT_ID_PATH_PARAM) String clientId) throws InternalException {
    return ResponseBuilder.build(
        Response.Status.OK,
        clientService.get(clientId));
  }

  @POST
  @Path(ClientRoute.UNREGISTER_CLIENT_ACTION)
  public Response unregister(
      @PathParam(ClientRoute.CLIENT_ID_PATH_PARAM) String clientId,
      ClientUnregistrationSpec request) throws InternalException {
    clientService.unregister(clientId, request);
    return ResponseBuilder.build(Response.Status.CREATED);
  }
}
