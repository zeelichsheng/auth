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

package com.ysheng.auth.frontend.resource;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Defines a utility class that builds response for RESTful API calls.
 */
public class ResponseBuilder {

  /**
   * Builds a response with empty entity.
   *
   * @param responseStatus The response status.
   * @return A response object.
   */
  public static Response build(
      Response.Status responseStatus) {
    return build(responseStatus, null);
  }

  /**
   * Builds a response with entity object.
   *
   * @param responseStatus The response status.
   * @param entity The entity contained in the response.
   * @return A response object.
   */
  public static Response build(
      Response.Status responseStatus,
      Object entity) {
    Response.ResponseBuilder builder = Response
        .status(responseStatus);

    if (entity != null) {
      builder = builder
          .entity(entity)
          .type(MediaType.APPLICATION_JSON_TYPE);
    }

    // TODO: this is temporary since we need to allow our management server
    // (auth-web-server) to do cross-domain AJAX call. We will need to
    // fix this as it opens security hole.
    builder.header("Access-Control-Allow-Origin", "*");
    builder.header("Access-Control-Allow-Methods", "POST, GET, DELETE");

    return builder.build();
  }
}
