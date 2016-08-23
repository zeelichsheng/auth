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

package com.ysheng.auth.frontend.test.resource.client;

import com.ysheng.auth.core.ClientService;
import com.ysheng.auth.frontend.resource.client.ClientsResource;
import com.ysheng.auth.frontend.resource.route.ClientRoute;
import com.ysheng.auth.frontend.test.resource.ResourceTestHelper;
import com.ysheng.auth.model.ClientType;
import com.ysheng.auth.model.client.ClientRegistrationRequest;
import com.ysheng.auth.model.client.ClientRegistrationResponse;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 * Tests for {@link com.ysheng.auth.frontend.resource.client.ClientsResource}.
 */
public class ClientsResourceTest {

  // The helper related to resource test.
  private ResourceTestHelper testHelper;

  // The client service that performs client related operations.
  private ClientService clientService;

  @BeforeMethod
  public void setUpTest() throws Throwable {
    clientService = mock(ClientService.class);

    testHelper = new ResourceTestHelper();
    testHelper.addResource(new ClientsResource(clientService));
    testHelper.setup();
  }

  @AfterMethod
  public void tearDownTest() throws Throwable {
    testHelper.destroy();
  }

  @Test
  public void succeedsToRegister() throws Throwable {
    ClientRegistrationRequest request = new ClientRegistrationRequest();
    request.setType(ClientType.CONFIDENTIAL);
    ClientRegistrationResponse response = new ClientRegistrationResponse();
    response.setClientId("clientId");

    doReturn(response).when(clientService).registerClient(eq(request));

    ClientRegistrationResponse actualResponse = testHelper.getClient()
        .target(ClientRoute.API)
        .request()
        .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE))
        .readEntity(ClientRegistrationResponse.class);

    assertThat(actualResponse, is(response));
  }
}
