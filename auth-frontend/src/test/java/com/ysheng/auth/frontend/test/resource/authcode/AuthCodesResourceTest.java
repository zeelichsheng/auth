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
import com.ysheng.auth.frontend.resource.authcode.AuthCodesResource;
import com.ysheng.auth.frontend.resource.route.AuthCodeRoute;
import com.ysheng.auth.frontend.test.resource.ResourceTestHelper;
import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.ExternalException;
import com.ysheng.auth.model.api.authcode.AuthorizationGrantSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationTicket;
import com.ysheng.auth.model.api.exception.ClientNotFoundException;
import com.ysheng.auth.model.api.exception.InvalidRequestException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriBuilder;

import java.util.Arrays;

/**
 * Tests for {@link com.ysheng.auth.frontend.resource.authcode.AuthCodesResource}.
 */
public class AuthCodesResourceTest {

  // The helper related to resource test.
  private ResourceTestHelper testHelper;

  // The auth code grant service that performs authorization related operations.
  private AuthCodeGrantService authCodeGrantService;

  // The client identifier.
  private String clientId = "clientId";

  // The authorization route.
  private String authorizationRoute =
      UriBuilder.fromPath(AuthCodeRoute.API).build(clientId).toString();

  @BeforeMethod
  public void setUpTest() throws Throwable {
    authCodeGrantService = mock(AuthCodeGrantService.class);

    testHelper = new ResourceTestHelper();
    testHelper.addResource(new AuthCodesResource(authCodeGrantService));
    testHelper.setup();
  }

  @AfterMethod
  public void tearDownTest() throws Throwable {
    testHelper.destroy();
  }

  @Test
  public void succeedsToAuthorize() throws Throwable {
    AuthorizationGrantSpec request = new AuthorizationGrantSpec();
    AuthorizationTicket response = new AuthorizationTicket();
    response.setCode("code");

    doReturn(response).when(authCodeGrantService).authorize(anyString(), any(AuthorizationGrantSpec.class));

    AuthorizationTicket actualResponse = testHelper.post(
        authorizationRoute,
        request,
        AuthorizationTicket.class);

    assertThat(actualResponse.getCode(), equalTo("code"));
  }

  @Test
  public void failsToAuthorize() throws Throwable {
    AuthorizationGrantSpec request = new AuthorizationGrantSpec();
    InvalidRequestException error = new InvalidRequestException("Invalid request");

    doThrow(error).when(authCodeGrantService).authorize(anyString(), any(AuthorizationGrantSpec.class));

    ExternalException actualError = testHelper.post(
        authorizationRoute,
        request,
        ExternalException.class);

    assertThat(actualError.getErrorCode(), equalTo(error.getErrorCode()));
    assertThat(actualError.getErrorDescription(), equalTo(error.getErrorDescription()));
  }

  @Test
  public void succeedsToList() throws Throwable {
    ApiList<AuthorizationTicket> response = new ApiList<>(Arrays.asList(new AuthorizationTicket()));

    doReturn(response).when(authCodeGrantService).listAuthorizationTickets(anyString());

    ApiList<AuthorizationTicket> actualResponse = testHelper.get(
        authorizationRoute,
        new GenericType<ApiList<AuthorizationTicket>>() {});

    assertThat(actualResponse.getItems().size(), is(1));
  }

  @Test
  public void failsToList() throws Throwable {
    ClientNotFoundException error = new ClientNotFoundException("clientId");

    doThrow(error).when(authCodeGrantService).listAuthorizationTickets(anyString());

    ExternalException actualError = testHelper.get(
        authorizationRoute,
        ExternalException.class);

    assertThat(actualError.getErrorCode(), equalTo(error.getErrorCode()));
    assertThat(actualError.getErrorDescription(), equalTo(error.getErrorDescription()));
  }
}
