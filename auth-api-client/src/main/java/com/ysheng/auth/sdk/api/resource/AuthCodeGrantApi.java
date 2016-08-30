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
import com.ysheng.auth.model.api.authcode.AuthorizationGrantSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationRevokeSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationTicket;
import com.ysheng.auth.sdk.api.RestClient;
import com.ysheng.auth.sdk.api.util.JsonSerializer;
import org.apache.http.HttpStatus;
import org.apache.http.concurrent.FutureCallback;

import javax.ws.rs.core.UriBuilder;

/**
 * Defines the APIs related to auth code grant operations.
 */
public class AuthCodeGrantApi extends BaseClient {

  private static final String AUTHORIZATIONS_PATH = "/auth-code/{clientId}/authorizations";

  private static final String AUTHORIZATION_PATH = AUTHORIZATIONS_PATH + "/{code}";

  private static final String REVOKE_AUTHORIZATION_PATH = AUTHORIZATION_PATH + "/revoke";

  private static final String ACCESS_TOKENS_PATH = "/auth-code/{clientId}/access-tokens";

  private static final String ACCESS_TOKEN_PATH = ACCESS_TOKENS_PATH + "/{accessToken}";

  private static final String REVOKE_ACCESS_TOKEN_PATH = ACCESS_TOKEN_PATH + "/revoke";

  /**
   * Constructs an AuthCodeGrantApi object.
   *
   * @param restClient The client that performs RESTful operations.
   */
  public AuthCodeGrantApi(RestClient restClient) {
    super(restClient);
  }

  /**
   * Grants an authorization ticket to the client asynchronously.
   *
   * @param clientId The client identifier.
   * @param spec The authorization grant spec.
   * @param responseHandler The response handler that handles the newly created authorization ticket.
   * @throws Exception The error that contains detail information.
   */
  public void authorizeAsync(
      final String clientId,
      final AuthorizationGrantSpec spec,
      final FutureCallback<AuthorizationTicket> responseHandler) throws Exception {
    postAsync(
        AUTHORIZATIONS_PATH,
        JsonSerializer.serialize(spec),
        HttpStatus.SC_CREATED,
        responseHandler);
  }

  /**
   * Lists the authorization tickets asynchronously.
   *
   * @param clientId The client identifier.
   * @param responseHandler The response handler that handles the retrieved authorization ticket list.
   * @throws Exception The error that contains detail information.
   */
  public void listAuthorizationTicketsAsync(
      final String clientId,
      final FutureCallback<ApiList<AuthorizationTicket>> responseHandler) throws Exception {
    getAsync(
        AUTHORIZATIONS_PATH,
        HttpStatus.SC_OK,
        responseHandler);
  }

  /**
   * Gets an authorization ticket asynchronously that matches the client identifier and code.
   *
   * @param clientId The client identifier.
   * @param code The authorization code.
   * @param responseHandler The response handler that handles the retrieved authorization ticket.
   * @throws Exception The error that contains detail information.
   */
  public void getAuthorizationTicketAsync(
      final String clientId,
      final String code,
      final FutureCallback<AuthorizationTicket> responseHandler) throws Exception {
    getAsync(
        UriBuilder.fromPath(AUTHORIZATION_PATH).build(clientId, code).toString(),
        HttpStatus.SC_OK,
        responseHandler);
  }

  /**
   * Revokes an authorization ticket from a client asynchronously.
   *
   * @param clientId The client identifier.
   * @param code The authorization code.
   * @param spec The authorization revoke spec.
   * @param responseHandler The response handler that handles the revocation.
   * @throws Exception The error that contains detail information.
   */
  public void revokeAuthorizationTicketAsync(
      final String clientId,
      final String code,
      final AuthorizationRevokeSpec spec,
      final FutureCallback<Void> responseHandler) throws Exception {
    postAsync(
        UriBuilder.fromPath(REVOKE_AUTHORIZATION_PATH).build(clientId, code).toString(),
        JsonSerializer.serialize(spec),
        HttpStatus.SC_CREATED,
        responseHandler);
  }
}
