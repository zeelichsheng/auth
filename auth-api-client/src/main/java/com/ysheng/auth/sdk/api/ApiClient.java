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

package com.ysheng.auth.sdk.api;

import com.ysheng.auth.sdk.api.resource.AuthCodeGrantApi;
import com.ysheng.auth.sdk.api.resource.ClientApi;
import com.ysheng.auth.sdk.api.resource.ImplicitGrantApi;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

/**
 * Defines the API client class.
 */
public class ApiClient {

  // The client that performs RESTful operations.
  private final RestClient restClient;

  // The auth client related APIs.
  private ClientApi clientApi;

  // The authorization code grant related APIs.
  private AuthCodeGrantApi authCodeGrantApi;

  // The implicit grant related APIs.
  private ImplicitGrantApi implicitGrantApi;

  /**
   * Constructs an ApiClient object.
   *
   * @param target The address of the RESTful endpoint.
   */
  public ApiClient(String target) {
    this(target,
        HttpAsyncClientBuilder.create().build());
  }

  /**
   * Constructs an ApiClient object.
   *
   * @param target The address of the RESTful endpoint.
   * @param httpClient The asynchronous HTTP client.
   */
  public ApiClient(
      String target,
      CloseableHttpAsyncClient httpClient) {
    this.restClient = new RestClient(target, httpClient);

    this.clientApi = new ClientApi(restClient);
    this.authCodeGrantApi = new AuthCodeGrantApi(restClient);
    this.implicitGrantApi = new ImplicitGrantApi(restClient);
  }

  ///
  /// Getters.
  ///

  public ClientApi getClientApi() {
    return clientApi;
  }

  public AuthCodeGrantApi getAuthCodeGrantApi() {
    return authCodeGrantApi;
  }

  public ImplicitGrantApi getImplicitGrantApi() {
    return implicitGrantApi;
  }
}
