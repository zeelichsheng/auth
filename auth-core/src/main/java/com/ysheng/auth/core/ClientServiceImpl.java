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
import com.ysheng.auth.core.util.UriUtil;
import com.ysheng.auth.core.generator.AuthValueGenerator;
import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.ClientType;
import com.ysheng.auth.model.api.exception.InternalException;
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.model.api.client.ClientRegistrationSpec;
import com.ysheng.auth.model.api.client.ClientUnregistrationSpec;
import com.ysheng.auth.model.api.exception.ClientNotFoundException;
import com.ysheng.auth.model.api.exception.ClientUnauthorizedException;
import com.ysheng.auth.model.api.exception.InvalidRequestException;

import java.util.List;

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
   * @return The client object.
   * @throws InternalException The exception that contains error details.
   */
  public Client register(ClientRegistrationSpec request)
      throws InternalException {
    // Validate the request.
    if (request.getRedirectUri() == null) {
      throw new InvalidRequestException("Redirect URI cannot be null");
    }

    if (!UriUtil.isValidUri(request.getRedirectUri())) {
      throw new InvalidRequestException("Invalid redirect URI: " + request.getRedirectUri());
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

    return client;
  }

  /**
   * Unregisters a client with the authorization server.
   *
   * @param clientId The client identifier.
   * @param request The client unregistration request that contains required information.
   * @throws InternalException The exception that contains error details.
   */
  public void unregister(
      String clientId,
      ClientUnregistrationSpec request)
      throws InternalException {
    // Validate the request.
    if (clientId == null) {
      throw new InvalidRequestException("Client ID cannot be null");
    }

    Client client = database.findClientById(clientId);
    if (client == null) {
      throw new ClientNotFoundException(clientId);
    }

    if (client.getSecret() != null &&
        !client.getSecret().equals(request.getClientSecret())) {
      throw new ClientUnauthorizedException(clientId);
    }

    // Remove client from database.
    database.removeClient(clientId);
  }

  /**
   * Gets a list of all clients.
   *
   * @return The list of all clients.
   */
  public ApiList<Client> list() {
    List<Client> clients = database.listClients();

    return new ApiList<>(clients);
  }

  /**
   * Gets a client with the given identifier.
   *
   * @param clientId The client identifier.
   * @return A client object with the give identifier.
   * @throws ClientNotFoundException The error that contains detail information.
   */
  public Client get(String clientId) throws ClientNotFoundException {
    Client client = database.findClientById(clientId);

    if (client == null) {
      throw new ClientNotFoundException(clientId);
    }

    return client;
  }
}
