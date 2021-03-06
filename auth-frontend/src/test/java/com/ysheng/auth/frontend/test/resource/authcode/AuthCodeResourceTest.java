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

package com.ysheng.auth.frontend.test.resource.authcode;

import com.ysheng.auth.core.AuthCodeGrantService;
import com.ysheng.auth.frontend.resource.authcode.AuthCodeResource;
import com.ysheng.auth.frontend.resource.route.AuthCodeRoute;
import com.ysheng.auth.frontend.test.resource.ResourceTestHelper;
import com.ysheng.auth.model.api.ExternalException;
import com.ysheng.auth.model.api.authcode.AuthorizationRevokeSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationTicket;
import com.ysheng.auth.model.api.exception.AuthorizationTicketNotFoundError;
import com.ysheng.auth.model.api.exception.ClientNotFoundException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import javax.ws.rs.core.UriBuilder;

/**
 * Tests for {@link com.ysheng.auth.frontend.resource.authcode.AuthCodeResource}.
 */
public class AuthCodeResourceTest {

  // The helper related to resource test.
  private ResourceTestHelper testHelper;

  // The auth code grant service that performs authorization related operations.
  private AuthCodeGrantService authCodeGrantService;

  // The client identifier.
  private String clientId = "clientId";

  // The authorization code.
  private String code = "code";

  // The authorization route.
  private String authorizationRoute =
      UriBuilder.fromPath(AuthCodeRoute.AUTHORIZATION_PATH).build(clientId, code).toString();

  // The authorization revocation route.
  private String revokeAuthorizationRoute =
      UriBuilder.fromPath(AuthCodeRoute.AUTHORIZATION_PATH + AuthCodeRoute.REVOKE_AUTHORIZATION_ACTION)
        .build(clientId, code).toString();

  @BeforeMethod
  public void setUpTest() throws Throwable {
    authCodeGrantService = mock(AuthCodeGrantService.class);

    testHelper = new ResourceTestHelper();
    testHelper.addResource(new AuthCodeResource(authCodeGrantService));
    testHelper.setup();
  }

  @AfterMethod
  public void tearDownTest() throws Throwable {
    testHelper.destroy();
  }

  @Test
  public void succeedsToGet() throws Throwable {
    AuthorizationTicket response = new AuthorizationTicket();
    response.setClientId(clientId);
    response.setCode(code);

    doReturn(response).when(authCodeGrantService).getAuthorizationTicket(anyString(), anyString());

    AuthorizationTicket actualResponse = testHelper.get(
        authorizationRoute,
        AuthorizationTicket.class);

    assertThat(actualResponse.getClientId(), equalTo(clientId));
    assertThat(actualResponse.getCode(), equalTo(code));
  }

  @Test
  public void failsToGet() throws Throwable {
    AuthorizationTicketNotFoundError error = new AuthorizationTicketNotFoundError(clientId, code);

    doThrow(error).when(authCodeGrantService).getAuthorizationTicket(anyString(), anyString());

    ExternalException actualError =  testHelper.get(
        authorizationRoute,
        ExternalException.class);

    assertThat(actualError.getErrorCode(), equalTo(error.getErrorCode()));
    assertThat(actualError.getErrorDescription(), equalTo(error.getErrorDescription()));
  }

  @Test
  public void succeedsToRevoke() throws Throwable {
    doNothing().when(authCodeGrantService)
        .revokeAuthorization(anyString(), anyString(), any(AuthorizationRevokeSpec.class));

    testHelper.post(
        revokeAuthorizationRoute,
        new AuthorizationRevokeSpec());
  }

  @Test
  public void failsToRevoke() throws Throwable {
    ClientNotFoundException error = new ClientNotFoundException("clientId");

    doThrow(error).when(authCodeGrantService)
        .revokeAuthorization(anyString(), anyString(), any(AuthorizationRevokeSpec.class));

    ExternalException actualError =  testHelper.post(
        revokeAuthorizationRoute,
        new AuthorizationRevokeSpec(),
        ExternalException.class);

    assertThat(actualError.getErrorCode(), equalTo(error.getErrorCode()));
    assertThat(actualError.getErrorDescription(), equalTo(error.getErrorDescription()));
  }
}
