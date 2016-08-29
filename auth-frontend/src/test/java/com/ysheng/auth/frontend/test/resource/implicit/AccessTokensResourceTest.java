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

package com.ysheng.auth.frontend.test.resource.implicit;

import com.ysheng.auth.core.ImplicitGrantService;
import com.ysheng.auth.frontend.resource.implicit.AccessTokensResource;
import com.ysheng.auth.frontend.resource.route.ImplicitRoute;
import com.ysheng.auth.frontend.test.resource.ResourceTestHelper;
import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.ExternalException;
import com.ysheng.auth.model.api.exception.ClientNotFoundException;
import com.ysheng.auth.model.api.exception.InvalidRequestException;
import com.ysheng.auth.model.api.implicit.AccessToken;
import com.ysheng.auth.model.api.implicit.AuthorizationGrantSpec;
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
 * Tests for {@link com.ysheng.auth.frontend.resource.implicit.AccessTokensResource}.
 */
public class AccessTokensResourceTest {

  // The helper related to resource test.
  private ResourceTestHelper testHelper;

  // The implicit grant service that performs backend operations.
  private ImplicitGrantService implicitGrantService;

  // The client identifier.
  private String clientId = "clientId";

  // The access tokens route.
  private String accessTokensRoute =
      UriBuilder.fromPath(ImplicitRoute.ACCESS_TOKENS_PATH).build(clientId).toString();

  @BeforeMethod
  public void setUpTest() throws Throwable {
    implicitGrantService = mock(ImplicitGrantService.class);

    testHelper = new ResourceTestHelper();
    testHelper.addResource(new AccessTokensResource(implicitGrantService));
    testHelper.setup();
  }

  @AfterMethod
  public void tearDownTest() throws Throwable {
    testHelper.destroy();
  }

  @Test
  public void succeedsToIssue() throws Throwable {
    AuthorizationGrantSpec request = new AuthorizationGrantSpec();
    AccessToken response = new AccessToken();
    response.setAccessToken("accessToken");

    doReturn(response).when(implicitGrantService)
        .issueAccessToken(anyString(), any(AuthorizationGrantSpec.class));

    AccessToken actualResponse = testHelper.post(
        accessTokensRoute,
        request,
        AccessToken.class);

    assertThat(actualResponse.getAccessToken(), equalTo(response.getAccessToken()));
  }

  @Test
  public void failsToIssue() throws Throwable {
    AuthorizationGrantSpec request = new AuthorizationGrantSpec();
    InvalidRequestException error = new InvalidRequestException("Invalid request");

    doThrow(error).when(implicitGrantService)
        .issueAccessToken(anyString(), any(AuthorizationGrantSpec.class));

    ExternalException actualError = testHelper.post(
        accessTokensRoute,
        request,
        ExternalException.class);

    assertThat(actualError.getErrorCode(), equalTo(error.getErrorCode()));
    assertThat(actualError.getErrorDescription(), equalTo(error.getErrorDescription()));
  }

  @Test
  public void succeedsToList() throws Throwable {
    ApiList<AccessToken> response = new ApiList<>(Arrays.asList(new AccessToken()));

    doReturn(response).when(implicitGrantService).listAccessTokens(anyString());

    ApiList<AccessToken> actualResponse = testHelper.get(
        accessTokensRoute,
        new GenericType<ApiList<AccessToken>>() {});

    assertThat(actualResponse.getItems().size(), is(1));
  }

  @Test
  public void failsToList() throws Throwable {
    ClientNotFoundException error = new ClientNotFoundException("clientId");

    doThrow(error).when(implicitGrantService).listAccessTokens(anyString());

    ExternalException actualError = testHelper.get(
        accessTokensRoute,
        ExternalException.class);

    assertThat(actualError.getErrorCode(), equalTo(error.getErrorCode()));
    assertThat(actualError.getErrorDescription(), equalTo(error.getErrorDescription()));
  }
}
