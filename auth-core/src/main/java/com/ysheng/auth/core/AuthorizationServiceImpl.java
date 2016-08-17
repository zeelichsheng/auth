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

import com.ysheng.auth.common.backend.Database;
import com.ysheng.auth.common.core.AuthorizationService;
import com.ysheng.auth.common.core.exception.AuthCodeAuthorizationException;
import com.ysheng.auth.common.core.generator.AuthValueGenerator;
import com.ysheng.auth.model.authcode.AuthorizationErrorType;
import com.ysheng.auth.model.authcode.AuthorizationRequest;
import com.ysheng.auth.model.authcode.AuthorizationResponse;
import com.ysheng.auth.model.client.Client;

/**
 * Implementation of the authorization related functions.
 */
public class AuthorizationServiceImpl implements AuthorizationService {

  // The database handler.
  private Database database;

  // The auth code generator.
  private AuthValueGenerator authValueGenerator;

  /**
   * Constructs an AuthorizationServiceImpl object.
   *
   * @param database The database object to interact with persistence store.
   */
  public AuthorizationServiceImpl(
      Database database,
      AuthValueGenerator authValueGenerator) {
    this.database = database;
    this.authValueGenerator = authValueGenerator;
  }

  /**
   * Authorizes an authorization request of Authorization Code Grant type.
   *
   * @param request The authorization request that contains required information.
   * @return The authorization response that contains the authorization code.
   */
  public AuthorizationResponse authorize(AuthorizationRequest request) throws AuthCodeAuthorizationException {
    // Validate the request.
    if (!AuthorizationRequest.VALID_RESPONSE_TYPE.equals(request.getResponseType())) {
      throw new AuthCodeAuthorizationException(
          AuthorizationErrorType.UNSUPPORTED_RESPONSE_TYPE,
          "Unsupported response type in request: " + request.getResponseType().toString());
    }

    Client client = database.findClientById(request.getClientId());
    if (client == null) {
      throw new AuthCodeAuthorizationException(
          AuthorizationErrorType.UNAUTHORIZED_CLIENT,
          "Client ID is invalid in request");
    }

    // Build the response.
    AuthorizationResponse response = new AuthorizationResponse();
    response.setCode(authValueGenerator.generateAuthCode());
    if (request.getState() != null) {
      response.setState(request.getState());
    }

    return response;
  }
}
