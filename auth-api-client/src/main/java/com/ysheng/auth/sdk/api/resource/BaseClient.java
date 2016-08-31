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

import com.fasterxml.jackson.core.type.TypeReference;
import com.ysheng.auth.sdk.api.RestClient;
import com.ysheng.auth.sdk.api.util.JsonSerializer;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import java.io.IOException;

/**
 * Defines the base class for API clients.
 */
public class BaseClient {

  // The client that performs RESTful operations.
  private final RestClient restClient;

  /**
   * Constructs a BaseClient object.
   *
   * @param restClient The client that performs RESTful operations.
   */
  protected BaseClient(RestClient restClient) {
    this.restClient = restClient;
  }

  /**
   * Performs a synchronous POST operation.
   *
   * @param path The path of the RESTful operation.
   * @param payload The payload of the RESTful operation.
   * @param expectedHttpStatus The expected HTTP status of the operation.
   * @param <T> The type of the response.
   * @return The response of the RESTful operation.
   * @throws IOException The error that contains detail information.
   */
  public final <T> T post(
      final String path,
      final Object payload,
      final int expectedHttpStatus) throws IOException {
    HttpResponse httpResponse = restClient.perform(
        RestClient.Method.POST,
        path,
        JsonSerializer.serialize(payload));

    restClient.checkResponse(httpResponse, expectedHttpStatus);

    return JsonSerializer.deserialize(
        httpResponse.getEntity(),
        new TypeReference<T>() {});
  }

  /**
   * Performs an asynchronous POST operation.
   *
   * @param path The path of the RESTful operation.
   * @param payload The payload of the RESTful operation.
   * @param expectedHttpStatus The expected HTTP status of the operation.
   * @param responseHandler The asynchronous response handler.
   * @param <T> The type of the response.
   * @throws IOException The error that contains detail information.
   */
  public final <T> void postAsync(
      final String path,
      final Object payload,
      final int expectedHttpStatus,
      final FutureCallback<T> responseHandler) throws IOException {
    restClient.performAsync(
        RestClient.Method.POST,
        path,
        JsonSerializer.serialize(payload),
        new FutureCallback<HttpResponse>() {
          @Override
          public void completed(HttpResponse httpResponse) {
            T response = null;

            try {
              restClient.checkResponse(httpResponse, expectedHttpStatus);
              response = JsonSerializer.deserialize(
                  httpResponse.getEntity(),
                  new TypeReference<T>() {});
            } catch (Exception e) {
              responseHandler.failed(e);
            }

            responseHandler.completed(response);
          }

          @Override
          public void failed(Exception e) {
            responseHandler.failed(e);
          }

          @Override
          public void cancelled() {
            responseHandler.cancelled();
          }
        }
    );
  }

  /**
   * Performs an synchronous DELETE operation.
   *
   * @param path The path of the RESTful operation.
   * @param expectedHttpStatus The expected HTTP status of the operation.
   * @throws IOException The error that contains detail information.
   */
  public final void delete(
      final String path,
      final int expectedHttpStatus) throws IOException {
    HttpResponse httpResponse = restClient.perform(
        RestClient.Method.DELETE,
        path,
        null);

    restClient.checkResponse(httpResponse, expectedHttpStatus);
  }

  /**
   * Performs an asynchronous DELETE operation.
   *
   * @param path The path of the RESTful operation.
   * @param expectedHttpStatus The expected HTTP status of the operation.
   * @param responseHandler The asynchronous response handler.
   * @throws IOException The error that contains detail information.
   */
  public final void deleteAsync(
      final String path,
      final int expectedHttpStatus,
      final FutureCallback<Void> responseHandler) throws IOException {
    restClient.performAsync(
        RestClient.Method.DELETE,
        path,
        null,
        new FutureCallback<HttpResponse>() {
          @Override
          public void completed(HttpResponse httpResponse) {
            try {
              restClient.checkResponse(httpResponse, expectedHttpStatus);
            } catch (Exception e) {
              responseHandler.failed(e);
            }

            responseHandler.completed(null);
          }

          @Override
          public void failed(Exception e) {
            responseHandler.failed(e);
          }

          @Override
          public void cancelled() {
            responseHandler.cancelled();
          }
        }
    );
  }

  /**
   * Performs an synchronous GET operation.
   *
   * @param path The path of the RESTful operation.
   * @param expectedHttpStatus The expected HTTP status of the operation.
   * @param <T> The type of the response.
   * @return The response of the RESTful operation.
   * @throws IOException The error that contains detail information.
   */
  public final <T> T get(
      final String path,
      final int expectedHttpStatus) throws IOException {
    HttpResponse httpResponse = restClient.perform(
        RestClient.Method.GET,
        path,
        null);

    restClient.checkResponse(httpResponse, expectedHttpStatus);

    return JsonSerializer.deserialize(
        httpResponse.getEntity(),
        new TypeReference<T>() {});
  }

  /**
   * Performs an asynchronous GET operation.
   *
   * @param path The path of the RESTful operation.
   * @param expectedHttpStatus The expected HTTP status of the operation.
   * @param responseHandler The asynchronous response handler.
   * @param <T> The type of the response.
   * @throws IOException The error that contains detail information.
   */
  public final <T> void getAsync(
      final String path,
      final int expectedHttpStatus,
      final FutureCallback<T> responseHandler) throws IOException {
    restClient.performAsync(
        RestClient.Method.GET,
        path,
        null,
        new FutureCallback<HttpResponse>() {
          @Override
          public void completed(HttpResponse httpResponse) {
            T response = null;
            try {
              restClient.checkResponse(httpResponse, expectedHttpStatus);
              response = JsonSerializer.deserialize(
                  httpResponse.getEntity(),
                  new TypeReference<T>() {});
            } catch (Exception e) {
              responseHandler.failed(e);
            }

            responseHandler.completed(response);
          }

          @Override
          public void failed(Exception e) {
            responseHandler.failed(e);
          }

          @Override
          public void cancelled() {
            responseHandler.cancelled();
          }
        }
    );
  }
}
