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

package com.ysheng.auth.frontend.resource.authcode;

import com.ysheng.auth.core.AuthCodeGrantService;
import com.ysheng.auth.frontend.resource.ResponseBuilder;
import com.ysheng.auth.frontend.resource.route.AuthCodeRoute;
import com.ysheng.auth.model.api.authcode.AuthorizationGrantSpec;
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
 * Defines the RESTful endpoints related to auth code grant operations for a specific client.
 */
@Path(AuthCodeRoute.AUTHORIZATIONS_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthCodesResource {

  // The auth code grant service that performs backend operation.
  private final AuthCodeGrantService authCodeGrantService;

  /**
   * Constructs a AuthCodeGrantService object.
   *
   * @param authCodeGrantService The auth code grant service that performs backend operation.
   */
  public AuthCodesResource(AuthCodeGrantService authCodeGrantService) {
    this.authCodeGrantService = authCodeGrantService;
  }

  @POST
  public Response authorize(
      @PathParam(AuthCodeRoute.CLIENT_ID_PATH_PARAM) String clientId,
      AuthorizationGrantSpec request) throws InternalException {
    return ResponseBuilder.build(
        Response.Status.CREATED,
        authCodeGrantService.authorize(clientId, request));
  }

  @GET
  public Response list(
      @PathParam(AuthCodeRoute.CLIENT_ID_PATH_PARAM) String clientId) throws InternalException {
    return ResponseBuilder.build(
        Response.Status.OK,
        authCodeGrantService.listAuthorizationTickets(clientId));
  }
}
