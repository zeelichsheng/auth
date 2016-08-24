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

import com.ysheng.auth.backend.Database;
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.common.utility.UriUtil;
import com.ysheng.auth.core.generator.AuthValueGenerator;
import com.ysheng.auth.model.api.ClientType;
import com.ysheng.auth.model.api.client.ClientRegistrationError;
import com.ysheng.auth.model.api.client.ClientRegistrationErrorType;
import com.ysheng.auth.model.api.client.ClientRegistrationRequest;
import com.ysheng.auth.model.api.client.ClientRegistrationResponse;
import com.ysheng.auth.model.api.client.ClientUnregistrationError;
import com.ysheng.auth.model.api.client.ClientUnregistrationErrorType;
import com.ysheng.auth.model.api.client.ClientUnregistrationRequest;
import com.ysheng.auth.model.api.client.ClientUnregistrationResponse;

/**
 * Implements client related functions.
 */
public class ClientServiceImpl implements ClientService {

  // The database handler.
  private Database database;

  // The auth code generator.
  private AuthValueGenerator authValueGenerator;

  /**
   * Constructs an ClientServiceImpl object.
   *
   * @param database The database object to interact with persistence store.
   * @param authValueGenerator The generator object to generate auth related values.
   */
  public ClientServiceImpl(
      Database database,
      AuthValueGenerator authValueGenerator) {
    this.database = database;
    this.authValueGenerator = authValueGenerator;
  }

  /**
   * Registers a client with the authentication server.
   *
   * @param request The client registration request that contains required information.
   * @return The client registration response that contains the client identifier and secret.
   * @throws ClientRegistrationError The exception that contains error details.
   */
  public ClientRegistrationResponse registerClient(ClientRegistrationRequest request)
      throws ClientRegistrationError {
    // Validate the request.
    if (request.getRedirectUri() == null) {
      throw new ClientRegistrationError(
          ClientRegistrationErrorType.INVALID_REQUEST,
          "Redirect URI cannot be null");
    }

    if (!UriUtil.isValidUri(request.getRedirectUri())) {
      throw new ClientRegistrationError(
          ClientRegistrationErrorType.INVALID_REQUEST,
          "Invalid redirect URI: " + request.getRedirectUri());
    }

    String clientId = authValueGenerator.generateClientId();
    String clientSecret = ClientType.CONFIDENTIAL.equals(request.getType()) ?
        authValueGenerator.generateClientSecret() : null;

    // Store client in database.
    Client client = new Client();
    client.setType(request.getType());
    client.setId(clientId);
    client.setSecret(clientSecret);
    client.setRedirectUri(request.getRedirectUri());
    database.storeClient(client);

    // Build the response.
    ClientRegistrationResponse response = new ClientRegistrationResponse();
    response.setClientId(clientId);
    response.setClientSecret(clientSecret);

    return response;
  }

  /**
   * Unregisters a client with the authorization server.
   *
   * @param request The client unregistration request that contains required information.
   * @return The client unregistration response.
   * @throws ClientUnregistrationError The exception that contains error details.
   */
  public ClientUnregistrationResponse unregisterClient(ClientUnregistrationRequest request)
      throws ClientUnregistrationError {
    // Validate the request.
    if (request.getClientId() == null) {
      throw new ClientUnregistrationError(
          ClientUnregistrationErrorType.INVALID_REQUEST,
          "Client ID cannot be null");
    }

    Client client = database.findClientById(request.getClientId());
    if (client == null) {
      throw new ClientUnregistrationError(
          ClientUnregistrationErrorType.CLIENT_NOT_FOUND,
          "Unable to find client with ID: " + request.getClientId());
    }

    if (client.getSecret() != null &&
        !client.getSecret().equals(request.getClientSecret())) {
      throw new ClientUnregistrationError(
          ClientUnregistrationErrorType.UNAUTHOURIZED_CLIENT,
          "Unauthorized client unregistration with invalid client secret: " + request.getClientSecret());
    }

    // Remove client from database.
    database.removeClient(request.getClientId());

    // Build the response.
    ClientUnregistrationResponse response = new ClientUnregistrationResponse();

    return response;
  }
}
