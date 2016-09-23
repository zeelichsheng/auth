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

package com.ysheng.auth.common.restful;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Defines a HTTP client that performs RESTful operations.
 */
public class RestClient {

  /**
   * Defines a list of HTTP verbs used in RESTful operations.
   */
  public enum Method {
    GET,
    PUT,
    POST,
    DELETE
  }

  // The asynchronous HTTP client.
  private final CloseableHttpAsyncClient httpClient;

  // The address of the RESTful endpoint.
  private final String target;

  /**
   * Constructs a RestClient object.
   *
   * @param target The address of the RESTful endpoint.
   * @param httpClient The asynchronous HTTP client.
   */
  public RestClient(String target, CloseableHttpAsyncClient httpClient) {
    if (null == httpClient) {
      throw new IllegalArgumentException("Client cannot be null");
    }

    if (null == target) {
      throw new IllegalArgumentException("Target cannot be null");
    }

    this.target = target;
    this.httpClient = httpClient;

    if (!this.httpClient.isRunning()) {
      this.httpClient.start();
    }
  }

  /**
   * Perform an HTTP request synchronously.
   *
   * @param method The HTTP verb.
   * @param path The path that the HTTP request being sent to.
   * @param payload The request payload.
   * @return The HTTP response.
   * @throws IOException The error that contains the detail information.
   */
  public HttpResponse perform(
      final Method method,
      final String path,
      final HttpEntity payload) throws IOException {
    try {
      return performAsync(method, path, payload, null).get();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Perform an HTTP request asynchronously.
   *
   * @param method The HTTP verb.
   * @param path The path that the HTTP request being sent to.
   * @param payload The request payload.
   * @param responseHandler The asynchronous callback handler.
   * @return The future response callback object.
   * @throws IOException The error that contains the detail information.
   */
  public Future<HttpResponse> performAsync(
      final Method method,
      final String path,
      final HttpEntity payload,
      final FutureCallback<HttpResponse> responseHandler) throws IOException {
    HttpUriRequest request = createHttpRequest(method, path, payload);
    return httpClient.execute(request, new BasicHttpContext(), responseHandler);
  }

  /**
   * Checks if the HTTP response meets the expected status code.
   *
   * @param httpResponse The HTTP response object.
   * @param expectedStatusCode The expected HTTP status code.
   */
  public void checkResponse(HttpResponse httpResponse, int expectedStatusCode) {
    int statusCode = httpResponse.getStatusLine().getStatusCode();

    if (statusCode != expectedStatusCode) {
      StringBuilder msg = new StringBuilder();
      msg.append("HTTP request failed with: ");
      msg.append(statusCode);
      if (httpResponse.getEntity() != null) {
        try {
          msg.append(", ");
          msg.append(EntityUtils.toString(httpResponse.getEntity()));
        } catch (IOException e) {
          // ignore exception here and use partial error message.
        }
      }
      throw new RuntimeException(msg.toString());
    }
  }

  /**
   * Creates a HTTP request object.
   *
   * @param method The HTTP verb.
   * @param path The path that the HTTP request being sent to.
   * @param payload The request payload.
   * @return The HTTP request object.
   * @throws IOException The error that contains the detail information.
   */
  private HttpUriRequest createHttpRequest(final Method method, final String path, final HttpEntity payload) throws
      IOException {
    String uri = target + path;
    HttpUriRequest request;

    switch (method) {
      case GET:
        request = new HttpGet(uri);
        break;
      case PUT:
        request = new HttpPut(uri);
        ((HttpPut) request).setEntity(payload);
        break;
      case POST:
        request = new HttpPost(uri);
        ((HttpPost) request).setEntity(payload);
        break;
      case DELETE:
        request = new HttpDelete(uri);
        break;

      default:
        throw new RuntimeException("Unknown method: " + method.name());
    }

    return request;
  }
}
