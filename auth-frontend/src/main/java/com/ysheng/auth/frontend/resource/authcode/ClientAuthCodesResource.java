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
import com.ysheng.auth.frontend.resource.route.AuthCodeRoute;
import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.authcode.AuthorizationError;
import com.ysheng.auth.model.api.authcode.AuthorizationSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationTicket;
import com.ysheng.auth.model.api.client.ClientNotFoundError;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Defines the RESTful endpoints related to auth code grant operations for a specific client.
 */
@Path(AuthCodeRoute.API)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientAuthCodesResource {

  // The auth code grant service that performs backend operation.
  private final AuthCodeGrantService authCodeGrantService;

  /**
   * Constructs a ClientAuthCodesResource object.
   *
   * @param authCodeGrantService The auth code grant service that performs backend operation.
   */
  public ClientAuthCodesResource(AuthCodeGrantService authCodeGrantService) {
    this.authCodeGrantService = authCodeGrantService;
  }

  @POST
  public AuthorizationTicket authorize(
      @PathParam(AuthCodeRoute.CLIENT_ID_PATH_PARAM) String clientId,
      AuthorizationSpec request) throws AuthorizationError {
    return authCodeGrantService.authorize(clientId, request);
  }

  @GET
  public ApiList<AuthorizationTicket> list(
      @PathParam(AuthCodeRoute.CLIENT_ID_PATH_PARAM) String clientId) throws ClientNotFoundError {
    return authCodeGrantService.listAuthorizationTicket(clientId);
  }
}
