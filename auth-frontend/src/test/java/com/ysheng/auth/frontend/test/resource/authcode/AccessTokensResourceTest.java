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
import com.ysheng.auth.frontend.resource.authcode.AccessTokensResource;
import com.ysheng.auth.frontend.resource.route.AuthCodeRoute;
import com.ysheng.auth.frontend.test.resource.ResourceTestHelper;
import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.ExternalException;
import com.ysheng.auth.model.api.authcode.AccessToken;
import com.ysheng.auth.model.api.authcode.AccessTokenIssueSpec;
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
 * Tests for {@link com.ysheng.auth.frontend.resource.authcode.AccessTokensResource}.
 */
public class AccessTokensResourceTest {

  // The helper related to resource test.
  private ResourceTestHelper testHelper;

  // The auth code grant service that performs authorization related operations.
  private AuthCodeGrantService authCodeGrantService;

  // The client identifier.
  private String clientId = "clientId";

  // The access tokens route.
  private String accessTokensRoute =
      UriBuilder.fromPath(AuthCodeRoute.ACCESS_TOKENS_PATH).build(clientId).toString();

  @BeforeMethod
  public void setUpTest() throws Throwable {
    authCodeGrantService = mock(AuthCodeGrantService.class);

    testHelper = new ResourceTestHelper();
    testHelper.addResource(new AccessTokensResource(authCodeGrantService));
    testHelper.setup();
  }

  @AfterMethod
  public void tearDownTest() throws Throwable {
    testHelper.destroy();
  }

  @Test
  public void succeedsToIssue() throws Throwable {
    AccessTokenIssueSpec request = new AccessTokenIssueSpec();
    AccessToken response = new AccessToken();
    response.setAccessToken("accessToken");

    doReturn(response).when(authCodeGrantService)
        .issueAccessToken(anyString(), any(AccessTokenIssueSpec.class));

    AccessToken actualResponse = testHelper.post(
        accessTokensRoute,
        request,
        AccessToken.class);

    assertThat(actualResponse.getAccessToken(), equalTo(response.getAccessToken()));
  }

  @Test
  public void failsToIssue() throws Throwable {
    AccessTokenIssueSpec request = new AccessTokenIssueSpec();
    InvalidRequestException error = new InvalidRequestException("Invalid request");

    doThrow(error).when(authCodeGrantService)
        .issueAccessToken(anyString(), any(AccessTokenIssueSpec.class));

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

    doReturn(response).when(authCodeGrantService).listAccessTokens(anyString());

    ApiList<AccessToken> actualResponse = testHelper.get(
        accessTokensRoute,
        new GenericType<ApiList<AccessToken>>() {});

    assertThat(actualResponse.getItems().size(), is(1));
  }

  @Test
  public void failsToList() throws Throwable {
    ClientNotFoundException error = new ClientNotFoundException("clientId");

    doThrow(error).when(authCodeGrantService).listAccessTokens(anyString());

    ExternalException actualError = testHelper.get(
        accessTokensRoute,
        ExternalException.class);

    assertThat(actualError.getErrorCode(), equalTo(error.getErrorCode()));
    assertThat(actualError.getErrorDescription(), equalTo(error.getErrorDescription()));
  }
}
