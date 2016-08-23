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

package com.ysheng.auth.frontend.mapper;

import com.ysheng.auth.model.ExternalException;
import com.ysheng.auth.model.InternalException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Defines the mapper that maps an internal exception to an external exception
 * wrapped in a RESTful response.
 */
public class InternalExceptionMapper implements ExceptionMapper<InternalException> {

  /**
   * Converts an internal exception to an external exception.
   *
   * @param ex The internal exception.
   * @return A RESTful response that contains the external exception.
   */
  @Override
  public Response toResponse(InternalException ex) {
    ExternalException externalException = new ExternalException(
        ex.getInternalErrorCode(),
        ex.getInternalErrorDescription());

    Response.ResponseBuilder builder = Response
        .status(ex.getHttpStatusCode())
        .entity(externalException)
        .type(MediaType.APPLICATION_JSON);

    return builder.build();
  }
}
