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
import com.ysheng.auth.frontend.resource.authcode.AccessTokenResource;
import com.ysheng.auth.frontend.resource.route.AuthCodeRoute;
import com.ysheng.auth.frontend.test.resource.ResourceTestHelper;
import com.ysheng.auth.model.api.ExternalException;
import com.ysheng.auth.model.api.authcode.AccessToken;
import com.ysheng.auth.model.api.authcode.AccessTokenRevokeSpec;
import com.ysheng.auth.model.api.exception.InvalidRequestException;
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
 * Tests for {@link com.ysheng.auth.frontend.resource.authcode.AccessTokenResource}.
 */
public class AccessTokenResourceTest {

  // The helper related to resource test.
  private ResourceTestHelper testHelper;

  // The auth code grant service that performs authorization related operations.
  private AuthCodeGrantService authCodeGrantService;

  // The client identifier.
  private String clientId = "clientId";

  // The access token.
  private String accessToken = "accessToken";

  // The access token route.
  private String accessTokenRoute =
      UriBuilder.fromPath(AuthCodeRoute.ACCESS_TOKEN_PATH).build(clientId, accessToken).toString();

  // The access token revocation route.
  private String revokeAccessTokenRoute =
      UriBuilder.fromPath(AuthCodeRoute.ACCESS_TOKEN_PATH + AuthCodeRoute.REVOKE_ACCESS_TOKEN_ACTION)
        .build(clientId, accessToken).toString();

  @BeforeMethod
  public void setUpTest() throws Throwable {
    authCodeGrantService = mock(AuthCodeGrantService.class);

    testHelper = new ResourceTestHelper();
    testHelper.addResource(new AccessTokenResource(authCodeGrantService));
    testHelper.setup();
  }

  @AfterMethod
  public void tearDownTest() throws Throwable {
    testHelper.destroy();
  }

  @Test
  public void succeedsToGet() throws Throwable {
    AccessToken response = new AccessToken();
    response.setClientId(clientId);
    response.setAccessToken(accessToken);

    doReturn(response).when(authCodeGrantService).getAccessToken(anyString(), anyString());

    AccessToken actualResponse = testHelper.get(
        accessTokenRoute,
        AccessToken.class);

    assertThat(actualResponse.getClientId(), equalTo(clientId));
    assertThat(actualResponse.getAccessToken(), equalTo(accessToken));
  }

  @Test
  public void failsToGet() throws Throwable {
    InvalidRequestException error = new InvalidRequestException("Invalid request");

    doThrow(error).when(authCodeGrantService).getAccessToken(anyString(), anyString());

    ExternalException actualError =  testHelper.get(
        accessTokenRoute,
        ExternalException.class);

    assertThat(actualError.getErrorCode(), equalTo(error.getErrorCode()));
    assertThat(actualError.getErrorDescription(), equalTo(error.getErrorDescription()));
  }

  @Test
  public void succeedsToRevoke() throws Throwable {
    doNothing().when(authCodeGrantService)
        .revokeAccessToken(anyString(), anyString(), any(AccessTokenRevokeSpec.class));

    testHelper.post(
        revokeAccessTokenRoute,
        new AccessTokenRevokeSpec());
  }

  @Test
  public void failsToRevoke() throws Throwable {
    InvalidRequestException error = new InvalidRequestException("Invalid request");

    doThrow(error).when(authCodeGrantService)
        .revokeAccessToken(anyString(), anyString(), any(AccessTokenRevokeSpec.class));

    ExternalException actualError =  testHelper.post(
        revokeAccessTokenRoute,
        new AccessTokenRevokeSpec(),
        ExternalException.class);

    assertThat(actualError.getErrorCode(), equalTo(error.getErrorCode()));
    assertThat(actualError.getErrorDescription(), equalTo(error.getErrorDescription()));
  }
}
