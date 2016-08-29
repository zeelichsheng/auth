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

package com.ysheng.auth.frontend.resource.implicit;

import com.ysheng.auth.core.ImplicitGrantService;
import com.ysheng.auth.frontend.resource.route.ImplicitRoute;
import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.exception.InternalException;
import com.ysheng.auth.model.api.implicit.AccessToken;
import com.ysheng.auth.model.api.implicit.AuthorizationGrantSpec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Defines the RESTful endpoints related to implicit access token related operations for
 * a specific client.
 */
@Path(ImplicitRoute.ACCESS_TOKENS_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccessTokensResource {

  // The implicit grant service that performs backend operation.
  private final ImplicitGrantService implicitGrantService;

  /**
   * Constructs a AccessTokensResource object.
   *
   * @param implicitGrantService The auth code grant service that performs backend operation.
   */
  public AccessTokensResource(ImplicitGrantService implicitGrantService) {
    this.implicitGrantService = implicitGrantService;
  }

  @POST
  public AccessToken issue(
      @PathParam(ImplicitRoute.CLIENT_ID_PATH_PARAM) String clientId,
      AuthorizationGrantSpec request) throws InternalException {
    return implicitGrantService.issueAccessToken(clientId, request);
  }

  @GET
  public ApiList<AccessToken> list(
      @PathParam(ImplicitRoute.CLIENT_ID_PATH_PARAM) String clientId) throws InternalException {
    return implicitGrantService.listAccessTokens(clientId);
  }
}
