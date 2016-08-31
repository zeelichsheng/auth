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

package com.ysheng.auth.sdk.api.resource;

import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.model.api.client.ClientRegistrationSpec;
import com.ysheng.auth.model.api.client.ClientUnregistrationSpec;
import com.ysheng.auth.sdk.api.RestClient;
import org.apache.http.HttpStatus;
import org.apache.http.concurrent.FutureCallback;

import javax.ws.rs.core.UriBuilder;

/**
 * Defines the APIs related to client operations.
 */
public class ClientApi extends BaseClient {

  private static final String CLIENTS_PATH = "/clients";

  private static final String CLIENT_PATH = CLIENTS_PATH + "/{id}";

  private static final String UNREGISTER_CLIENT_PATH = CLIENT_PATH + "/unregister";

  /**
   * Constructs a ClientApi object.
   *
   * @param restClient The client that performs RESTful operations.
   */
  public ClientApi(RestClient restClient) {
    super(restClient);
  }

  /**
   * Registers a client synchronously.
   *
   * @param spec The client registration spec.
   * @return The client object newly registered.
   * @throws Exception The error that contains detail information.
   */
  public Client register(final ClientRegistrationSpec spec) throws Exception {
    return post(
        CLIENTS_PATH,
        spec,
        HttpStatus.SC_CREATED);
  }

  /**
   * Registers a client asynchronously.
   *
   * @param spec The client registration spec.
   * @param responseHandler The handler that handles the newly registered client.
   * @throws Exception The error that contains detail information.
   */
  public void registerAsync(
      final ClientRegistrationSpec spec,
      final FutureCallback<Client> responseHandler) throws Exception {
    postAsync(
        CLIENTS_PATH,
        spec,
        HttpStatus.SC_CREATED,
        responseHandler);
  }

  /**
   * Lists the clients synchronously.
   *
   * @return A list of clients.
   * @throws Exception The error that contains detail information.
   */
  public ApiList<Client> list() throws Exception {
    return get(
        CLIENTS_PATH,
        HttpStatus.SC_OK);
  }

  /**
   * Lists the clients asynchronously.
   *
   * @param responseHandler The handler that handles the retrieved client list.
   * @throws Exception The error that contains detail information.
   */
  public void listAsync(
      final FutureCallback<ApiList<Client>> responseHandler) throws Exception {
    getAsync(
        CLIENTS_PATH,
        HttpStatus.SC_OK,
        responseHandler);
  }

  /**
   * Gets a client synchronously that matches the client identifier.
   *
   * @param clientId The client identifier to be matched.
   * @@return The client object.
   * @throws Exception The error that contains detail information.
   */
  public Client get(final String clientId) throws Exception {
    return get(
        UriBuilder.fromPath(CLIENT_PATH).build(clientId).toString(),
        HttpStatus.SC_OK);
  }

  /**
   * Gets a client asynchronously that matches the client identifier.
   *
   * @param clientId The client identifier to be matched.
   * @param responseHandler The handler that handles the retrieved client.
   * @throws Exception The error that contains detail information.
   */
  public void getAsync(
      final String clientId,
      final FutureCallback<Client> responseHandler) throws Exception {
    getAsync(
        UriBuilder.fromPath(CLIENT_PATH).build(clientId).toString(),
        HttpStatus.SC_OK,
        responseHandler);
  }

  /**
   * Unregisters a client synchronously that matches the client identifier.
   *
   * @param clientId The client identifier to be matched.
   * @param spec The client unregistration spec.
   * @throws Exception The error that contains detail information.
   */
  public void unregister(
      final String clientId,
      final ClientUnregistrationSpec spec) throws Exception {
    post(
        UriBuilder.fromPath(UNREGISTER_CLIENT_PATH).build(clientId).toString(),
        spec,
        HttpStatus.SC_CREATED);
  }

  /**
   * Unregisters a client asynchronously that matches the client identifier.
   *
   * @param clientId The client identifier to be matched.
   * @param spec The client unregistration spec.
   * @param responseHandler The handler that handles the client unregistration.
   * @throws Exception The error that contains detail information.
   */
  public void unregisterAsync(
      final String clientId,
      final ClientUnregistrationSpec spec,
      final FutureCallback<Void> responseHandler) throws Exception {
    postAsync(
        UriBuilder.fromPath(UNREGISTER_CLIENT_PATH).build(clientId).toString(),
        spec,
        HttpStatus.SC_CREATED,
        responseHandler);
  }
}
