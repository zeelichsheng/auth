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
import com.ysheng.auth.frontend.resource.client.ClientResource;
import com.ysheng.auth.frontend.resource.route.ClientRoute;
import com.ysheng.auth.frontend.test.resource.ResourceTestHelper;
import com.ysheng.auth.model.api.ExternalException;
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.model.api.exception.ClientNotFoundException;
import com.ysheng.auth.model.api.client.ClientUnregistrationSpec;
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
 * Tests for {@link com.ysheng.auth.frontend.resource.client.ClientResource}.
 */
public class ClientResourceTest {

  // The helper related to resource test.
  private ResourceTestHelper testHelper;

  // The client service that performs client related operations.
  private ClientService clientService;

  // The client identifier.
  private String clientId = "clientId";

  // The client route.
  private String clientRoute =
      UriBuilder.fromPath(ClientRoute.CLIENT_PATH).build(clientId).toString();

  // The client unregisteration route.
  private String clientUnregistrationRoute =
      UriBuilder.fromPath(ClientRoute.CLIENT_PATH + ClientRoute.UNREGISTER_CLIENT_ACTION).build(clientId).toString();

  @BeforeMethod
  public void setUpTest() throws Throwable {
    clientService = mock(ClientService.class);

    testHelper = new ResourceTestHelper();
    testHelper.addResource(new ClientResource(clientService));
    testHelper.setup();
  }

  @AfterMethod
  public void tearDownTest() throws Throwable {
    testHelper.destroy();
  }

  @Test
  public void succeedsToGet() throws Throwable {
    Client client = new Client();
    client.setId("clientId");

    doReturn(client).when(clientService).get(anyString());

    Client actualClient = testHelper.get(
        clientRoute,
        Client.class);

    assertThat(actualClient.getId(), equalTo(client.getId()));
  }

  @Test
  public void failsToGet() throws Throwable {
    ClientNotFoundException error = new ClientNotFoundException("clientId");

    doThrow(error).when(clientService).get(anyString());

    ExternalException actualError = testHelper.get(
        clientRoute,
        ExternalException.class);

    assertThat(actualError.getErrorDescription(), equalTo(error.getErrorDescription()));
  }

  @Test
  public void succeedsToUnregister() throws Throwable {
    ClientUnregistrationSpec request = new ClientUnregistrationSpec();
    request.setClientSecret("clientSecret");

    doNothing().when(clientService).unregister(anyString(), any(ClientUnregistrationSpec.class));

    testHelper.post(
        clientUnregistrationRoute,
        request);
  }

  @Test
  public void failsToUnregister() throws Throwable {
    ClientUnregistrationSpec request = new ClientUnregistrationSpec();
    request.setClientSecret("clientSecret");
    InvalidRequestException error = new InvalidRequestException("Invalid request");

    doThrow(error).when(clientService).unregister(anyString(), any(ClientUnregistrationSpec.class));

    ExternalException actualError = testHelper.post(
        clientUnregistrationRoute,
        request,
        ExternalException.class);

    assertThat(actualError.getErrorCode(), equalTo(error.getErrorCode()));
    assertThat(actualError.getErrorDescription(), equalTo(error.getErrorDescription()));
  }
}
