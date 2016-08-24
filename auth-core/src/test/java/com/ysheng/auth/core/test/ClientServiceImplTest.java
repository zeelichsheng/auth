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

package com.ysheng.auth.core.test;

import com.ysheng.auth.backend.Database;
import com.ysheng.auth.core.ClientServiceImpl;
import com.ysheng.auth.core.generator.AuthValueGenerator;
import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.ClientType;
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.model.api.client.ClientRegistrationError;
import com.ysheng.auth.model.api.client.ClientRegistrationErrorType;
import com.ysheng.auth.model.api.client.ClientRegistrationRequest;
import com.ysheng.auth.model.api.client.ClientRegistrationResponse;
import com.ysheng.auth.model.api.client.ClientUnregistrationError;
import com.ysheng.auth.model.api.client.ClientUnregistrationErrorType;
import com.ysheng.auth.model.api.client.ClientUnregistrationRequest;
import com.ysheng.auth.model.api.client.ClientUnregistrationResponse;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link com.ysheng.auth.core.ClientServiceImpl}.
 */
public class ClientServiceImplTest {

  @Test(enabled = false)
  public void dummy() {
  }

  /**
   * Tests for {@link com.ysheng.auth.core.ClientServiceImpl#register}.
   */
  public static class RegisterTest {

    @Test
    public void failsWithNullRedirectUri() {
      ClientRegistrationRequest request = new ClientRegistrationRequest();
      request.setType(ClientType.CONFIDENTIAL);
      request.setRedirectUri(null);

      ClientServiceImpl service = new ClientServiceImpl(null, null);

      try {
        service.register(request);
        fail("Client registration should fail with null redirect URI");
      } catch (ClientRegistrationError ex) {
        assertThat(ex.getError(), is(ClientRegistrationErrorType.INVALID_REQUEST));
        assertThat(ex.getErrorDescription(), equalTo("Redirect URI cannot be null"));
      }
    }

    @Test
    public void failsWithInvalidRedirectUri() {
      ClientRegistrationRequest request = new ClientRegistrationRequest();
      request.setType(ClientType.CONFIDENTIAL);
      request.setRedirectUri("invalidUri");

      ClientServiceImpl service = new ClientServiceImpl(null, null);

      try {
        service.register(request);
        fail("Client registration should fail with invalid redirect URI");
      } catch (ClientRegistrationError ex) {
        assertThat(ex.getError(), is(ClientRegistrationErrorType.INVALID_REQUEST));
        assertThat(ex.getErrorDescription(), equalTo("Invalid redirect URI: invalidUri"));
      }
    }

    @Test
    public void succeedsToRegister() throws Throwable {
      Database database = mock(Database.class);
      doNothing().when(database).storeClient(any(Client.class));

      AuthValueGenerator authValueGenerator = mock(AuthValueGenerator.class);
      doReturn("clientId").when(authValueGenerator).generateClientId();
      doReturn("clientSecret").when(authValueGenerator).generateClientSecret();

      ClientRegistrationRequest request = new ClientRegistrationRequest();
      request.setType(ClientType.CONFIDENTIAL);
      request.setRedirectUri("http://1.2.3.4");

      ClientServiceImpl service = new ClientServiceImpl(database, authValueGenerator);

      ClientRegistrationResponse response = service.register(request);
      assertThat(response.getClientId(), equalTo("clientId"));
      assertThat(response.getClientSecret(), equalTo("clientSecret"));
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.ClientServiceImpl#unregister}.
   */
  public static class UnregisterTest {

    @Test
    public void failsWithNullClientId() {
      ClientUnregistrationRequest request = new ClientUnregistrationRequest();

      ClientServiceImpl service = new ClientServiceImpl(null, null);

      try {
        service.unregister(null, request);
        fail("Client unregistration should fail with null client ID");
      } catch (ClientUnregistrationError ex) {
        assertThat(ex.getError(), is(ClientUnregistrationErrorType.INVALID_REQUEST));
        assertThat(ex.getErrorDescription(), equalTo("Client ID cannot be null"));
      }
    }

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      ClientUnregistrationRequest request = new ClientUnregistrationRequest();

      ClientServiceImpl service = new ClientServiceImpl(database, null);

      try {
        service.unregister("clientId", request);
        fail("Client unregistration should fail with non-exist client ID");
      } catch (ClientUnregistrationError ex) {
        assertThat(ex.getError(), is(ClientUnregistrationErrorType.CLIENT_NOT_FOUND));
        assertThat(ex.getErrorDescription(), equalTo("Unable to find client with ID: clientId"));
      }
    }

    @Test
    public void failsWithUnauthorizedClient() {
      Client client = new Client();
      client.setSecret("clientSecret1");

      Database database = mock(Database.class);
      doReturn(client).when(database).findClientById(anyString());

      ClientUnregistrationRequest request = new ClientUnregistrationRequest();
      request.setClientSecret("clientSecret2");

      ClientServiceImpl service = new ClientServiceImpl(database, null);

      try {
        service.unregister("clientId", request);
        fail("Client unregistration should fail with unauthorized client");
      } catch (ClientUnregistrationError ex) {
        assertThat(ex.getError(), is(ClientUnregistrationErrorType.UNAUTHOURIZED_CLIENT));
        assertThat(ex.getErrorDescription(),
            equalTo("Unauthorized client unregistration with invalid client secret: clientSecret2"));
      }
    }

    @Test
    public void succeedsToUnregister() throws Throwable {
      Client client = new Client();
      client.setSecret("clientSecret");

      Database database = mock(Database.class);
      doReturn(client).when(database).findClientById(anyString());

      ClientUnregistrationRequest request = new ClientUnregistrationRequest();
      request.setClientSecret("clientSecret");

      ClientServiceImpl service = new ClientServiceImpl(database, null);
      ClientUnregistrationResponse response = service.unregister("clientId", request);
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.ClientServiceImpl#list}.
   */
  public static class ListTest {

    @Test
    public void succeedsToList() {
      Client client1 = new Client();
      client1.setId("clientId1");
      Client client2 = new Client();
      client2.setId("clientId2");
      List<Client> clients = Arrays.asList(client1, client2);

      Database database = mock(Database.class);
      doReturn(clients).when(database).listClients();

      ClientServiceImpl service = new ClientServiceImpl(database, null);
      ApiList<Client> clientApiList = service.list();

      assertThat(clientApiList.getItems().size(), is(2));
      assertThat(clientApiList.getItems().get(0).getId(), equalTo(client1.getId()));
      assertThat(clientApiList.getItems().get(1).getId(), equalTo(client2.getId()));
    }
  }
}
