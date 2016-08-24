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
import com.ysheng.auth.model.api.ExternalException;
import com.ysheng.auth.model.api.authcode.AuthorizationError;
import com.ysheng.auth.model.api.authcode.AuthorizationErrorType;
import com.ysheng.auth.model.api.authcode.AuthorizationRequest;
import com.ysheng.auth.model.api.authcode.AuthorizationResponse;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link com.ysheng.auth.frontend.resource.authcode.AuthCodesResource}.
 */
public class AuthCodesResourceTest {

  // The helper related to resource test.
  private ResourceTestHelper testHelper;

  // The auth code grant service that performs authorization related operations.
  private AuthCodeGrantService authCodeGrantService;

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
    AuthorizationRequest request = new AuthorizationRequest();
    request.setClientId("clientId");
    AuthorizationResponse response = new AuthorizationResponse();
    response.setCode("code");

    doReturn(response).when(authCodeGrantService).authorize(any(AuthorizationRequest.class));

    AuthorizationResponse actualResponse = testHelper.post(
        AuthCodeRoute.API,
        request,
        AuthorizationResponse.class);

    assertThat(actualResponse.getCode(), equalTo("code"));
  }

  @Test
  public void failsToAuthorize() throws Throwable {
    AuthorizationRequest request = new AuthorizationRequest();
    request.setClientId("clientId");
    AuthorizationError error = new AuthorizationError(
        AuthorizationErrorType.INVALID_REQUEST,
        "Invalid request");

    doThrow(error).when(authCodeGrantService).authorize(any(AuthorizationRequest.class));

    ExternalException actualError = testHelper.post(
        AuthCodeRoute.API,
        request,
        ExternalException.class);

    assertThat(actualError.getErrorCode(), equalTo(error.getInternalErrorCode()));
    assertThat(actualError.getErrorDescription(), equalTo(error.getInternalErrorDescription()));
  }
}
