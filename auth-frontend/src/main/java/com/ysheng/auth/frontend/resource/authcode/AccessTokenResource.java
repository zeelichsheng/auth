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
import com.ysheng.auth.model.api.authcode.AccessToken;
import com.ysheng.auth.model.api.authcode.AccessTokenRevokeSpec;
import com.ysheng.auth.model.api.exception.InternalException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Defines the RESTful endpoints related to access token related operations for
 * a specific client and access token combination.
 */
@Path(AuthCodeRoute.ACCESS_TOKEN_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccessTokenResource {

  // The auth code grant service that performs backend operation.
  private final AuthCodeGrantService authCodeGrantService;

  /**
   * Constructs a AccessTokenResource object.
   *
   * @param authCodeGrantService The auth code grant service that performs backend operation.
   */
  public AccessTokenResource(AuthCodeGrantService authCodeGrantService) {
    this.authCodeGrantService = authCodeGrantService;
  }

  @GET
  public AccessToken get(
      @PathParam(AuthCodeRoute.CLIENT_ID_PATH_PARAM) String clientId,
      @PathParam(AuthCodeRoute.ACCESS_TOKEN_PATH_PARAM) String accessToken) throws InternalException {
    return authCodeGrantService.getAccessToken(clientId, accessToken);
  }

  @POST
  @Path(AuthCodeRoute.REVOKE_ACCESS_TOKEN_ACTION)
  public void revoke(
      @PathParam(AuthCodeRoute.CLIENT_ID_PATH_PARAM) String clientId,
      @PathParam(AuthCodeRoute.ACCESS_TOKEN_PATH_PARAM) String accessToken,
      AccessTokenRevokeSpec request) throws InternalException {
        authCodeGrantService.revokeAccessToken(clientId, accessToken, request);
  }
}
