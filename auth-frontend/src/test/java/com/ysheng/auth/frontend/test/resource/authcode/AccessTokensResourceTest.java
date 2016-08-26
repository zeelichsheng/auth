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
import com.ysheng.auth.model.api.ExternalException;
import com.ysheng.auth.model.api.authcode.AccessToken;
import com.ysheng.auth.model.api.authcode.AccessTokenSpec;
import com.ysheng.auth.model.api.exception.InvalidRequestException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import javax.ws.rs.core.UriBuilder;

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

  // The authorization code.
  private String code = "code";

  // The access tokens route.
  private String accessTokensRoute =
      UriBuilder.fromPath(AuthCodeRoute.ACCESS_TOKENS_API).build(clientId, code).toString();

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
  public void succeedsToIssueAccessToken() throws Throwable {
    AccessTokenSpec request = new AccessTokenSpec();
    AccessToken response = new AccessToken();
    response.setAccessToken("accessToken");

    doReturn(response).when(authCodeGrantService)
        .issueAccessToken(anyString(), anyString(), any(AccessTokenSpec.class));

    AccessToken actualResponse = testHelper.post(
        accessTokensRoute,
        request,
        AccessToken.class);

    assertThat(actualResponse.getAccessToken(), equalTo(response.getAccessToken()));
  }

  @Test
  public void failsToIssueAccessToken() throws Throwable {
    AccessTokenSpec request = new AccessTokenSpec();
    InvalidRequestException error = new InvalidRequestException("Invalid request");

    doThrow(error).when(authCodeGrantService)
        .issueAccessToken(anyString(), anyString(), any(AccessTokenSpec.class));

    ExternalException actualError = testHelper.post(
        accessTokensRoute,
        request,
        ExternalException.class);

    assertThat(actualError.getErrorCode(), equalTo(error.getErrorCode()));
    assertThat(actualError.getErrorDescription(), equalTo(error.getErrorDescription()));
  }
}