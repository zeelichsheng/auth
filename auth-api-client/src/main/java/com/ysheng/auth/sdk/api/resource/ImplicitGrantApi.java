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
import com.ysheng.auth.model.api.implicit.AccessToken;
import com.ysheng.auth.model.api.implicit.AccessTokenRevokeSpec;
import com.ysheng.auth.model.api.implicit.AuthorizationGrantSpec;
import com.ysheng.auth.sdk.api.RestClient;
import com.ysheng.auth.sdk.api.util.JsonSerializer;
import org.apache.http.HttpStatus;
import org.apache.http.concurrent.FutureCallback;

import javax.ws.rs.core.UriBuilder;

/**
 * Defines the APIs related to implicit grant operations.
 */
public class ImplicitGrantApi extends BaseClient {

  private static final String ACCESS_TOKENS_PATH = "/implicit/{clientId}";

  private static final String ACCESS_TOKEN_PATH = ACCESS_TOKENS_PATH + "/{accessToken}";

  private static final String REVOKE_ACCESS_TOKEN_PATH = ACCESS_TOKEN_PATH + "/revoke";

  /**
   * Constructs an ImplicitGrantApi object.
   *
   * @param restClient The client that performs RESTful operations.
   */
  public ImplicitGrantApi(RestClient restClient) {
    super(restClient);
  }

  /**
   * Issues an access token to the client asynchronously.
   *
   * @param clientId The client identifier.
   * @param spec The access token issue spec.
   * @param responseHandler The response handler that handles the newly issued access token.
   * @throws Exception The error that contains detail information.
   */
  public void issueAsync(
      final String clientId,
      final AuthorizationGrantSpec spec,
      final FutureCallback<AccessToken> responseHandler) throws Exception {
    postAsync(
        UriBuilder.fromPath(ACCESS_TOKENS_PATH).build(clientId).toString(),
        JsonSerializer.serialize(spec),
        HttpStatus.SC_CREATED,
        responseHandler);
  }

  /**
   * Lists the access tokens that belong to the client asynchronously.
   *
   * @param clientId The client identifier.
   * @param responseHandler The response handler that handles the retrieved access token list.
   * @throws Exception The error that contains detail information.
   */
  public void listAccessTokensAsync(
      final String clientId,
      final FutureCallback<ApiList<AccessToken>> responseHandler) throws Exception {
    getAsync(
        UriBuilder.fromPath(ACCESS_TOKENS_PATH).build(clientId).toString(),
        HttpStatus.SC_OK,
        responseHandler);
  }

  /**
   * Gets an access token asynchronously that matches the client identifier and the token.
   *
   * @param clientId The client identifier.
   * @param accessToken The access token.
   * @param responseHandler The response handler that handles the retrieved access token.
   * @throws Exception The error that contains detail information.
   */
  public void getAccessTokenAsync(
      final String clientId,
      final String accessToken,
      final FutureCallback<AccessToken> responseHandler) throws Exception {
    getAsync(
        UriBuilder.fromPath(ACCESS_TOKEN_PATH).build(clientId, accessToken).toString(),
        HttpStatus.SC_OK,
        responseHandler);
  }

  /**
   * Revokes an access token from a client asynchronously.
   *
   * @param clientId The client identifier.
   * @param accessToken The access token.
   * @param spec The access token revoke spec.
   * @param responseHandler The response handler that handles the revocation.
   * @throws Exception The error that contains detail information.
   */
  public void revokeAccessTokenAsync(
      final String clientId,
      final String accessToken,
      final AccessTokenRevokeSpec spec,
      final FutureCallback<Void> responseHandler) throws Exception {
    postAsync(
        UriBuilder.fromPath(REVOKE_ACCESS_TOKEN_PATH).build(clientId, accessToken).toString(),
        JsonSerializer.serialize(spec),
        HttpStatus.SC_CREATED,
        responseHandler);
  }
}
