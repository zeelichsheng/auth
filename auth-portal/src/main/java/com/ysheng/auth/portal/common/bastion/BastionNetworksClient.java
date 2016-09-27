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

package com.ysheng.auth.portal.common.bastion;

import com.ysheng.auth.common.restful.RestClient;
import com.ysheng.auth.portal.common.bastion.api.InitializationApi;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;

import java.security.cert.X509Certificate;


/**
 * Defines a restful client that execute Bastion Networks related commands.
 */
public class BastionNetworksClient {

  // The client that performs RESTful operations.
  private final RestClient restClient;

  // The initialization API.
  private InitializationApi initializationApi;

  /**
   * Constructs an BastionNetworksClient object.
   *
   * @param target The address of the RESTful endpoint.
   */
  public BastionNetworksClient(String target) {
    TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
      public boolean isTrusted(X509Certificate[] certificate, String authType) {
        return true;
      }
    };

    SSLContext sslContext = null;
    try {
      sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
    } catch (Exception e) {
      // Handle error
    }

    CloseableHttpAsyncClient client = HttpAsyncClients.custom()
        .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
        .setSSLContext(sslContext).build();

    this.restClient = new RestClient(target, client);
    initialize();
  }

  /**
   * Constructs an BastionNetworksClient object.
   *
   * @param target The address of the RESTful endpoint.
   * @param httpClient The asynchronous HTTP client.
   */
  public BastionNetworksClient(
      String target,
      CloseableHttpAsyncClient httpClient) {
    this.restClient = new RestClient(target, httpClient);
    initialize();
  }

  private void initialize() {
    this.initializationApi = new InitializationApi(restClient);
  }

  ///
  /// Getters.
  ///

  public InitializationApi getInitializationApi() {
    return initializationApi;
  }
}
