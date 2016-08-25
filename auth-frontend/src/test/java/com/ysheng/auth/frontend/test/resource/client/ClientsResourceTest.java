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
import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.ClientType;
import com.ysheng.auth.model.api.ExternalException;
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.model.api.client.ClientRegistrationError;
import com.ysheng.auth.model.api.client.ClientRegistrationErrorType;
import com.ysheng.auth.model.api.client.ClientRegistrationRequest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import javax.ws.rs.core.GenericType;

import java.util.Arrays;

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
    Client response = new Client();
    response.setId("clientId");

    doReturn(response).when(clientService).register(any(ClientRegistrationRequest.class));

    Client actualResponse = testHelper.post(
        ClientRoute.API,
        request,
        Client.class);

    assertThat(actualResponse.getId(), equalTo(response.getId()));
  }

  @Test
  public void failsToRegister() throws Throwable {
    ClientRegistrationRequest request = new ClientRegistrationRequest();
    request.setType(ClientType.CONFIDENTIAL);
    ClientRegistrationError error = new ClientRegistrationError(
        ClientRegistrationErrorType.INVALID_REQUEST,
        "Invalid request");

    doThrow(error).when(clientService).register(any(ClientRegistrationRequest.class));

    ExternalException actualError = testHelper.post(
        ClientRoute.API,
        request,
        ExternalException.class);

    assertThat(actualError.getErrorCode(), equalTo(error.getError().toString()));
    assertThat(actualError.getErrorDescription(), equalTo(error.getErrorDescription()));
  }

  @Test
  public void succeedsToList() throws Throwable {
    Client client1 = new Client();
    client1.setId("clientId1");
    Client client2 = new Client();
    client2.setId("clientId2");
    ApiList<Client> clientList = new ApiList<>(Arrays.asList(client1, client2));

    doReturn(clientList).when(clientService).list();

    ApiList<Client> actualClientList = testHelper.get(
        ClientRoute.API,
        new GenericType<ApiList<Client>>(){});

    assertThat(actualClientList.getItems().size(), equalTo(clientList.getItems().size()));
    assertThat(actualClientList.getItems().get(0).getId(),
        equalTo(clientList.getItems().get(0).getId()));
    assertThat(actualClientList.getItems().get(1).getId(),
        equalTo(clientList.getItems().get(1).getId()));
  }
}
