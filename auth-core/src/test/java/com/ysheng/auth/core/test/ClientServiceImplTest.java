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
import com.ysheng.auth.core.generator.AuthValueGenerator;
import com.ysheng.auth.core.ClientServiceImpl;
import com.ysheng.auth.model.ClientType;
import com.ysheng.auth.model.client.ClientRegistrationError;
import com.ysheng.auth.model.client.ClientRegistrationErrorType;
import com.ysheng.auth.model.client.ClientRegistrationRequest;
import com.ysheng.auth.model.client.ClientRegistrationResponse;
import com.ysheng.auth.backend.model.Client;
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

/**
 * Tests for {@link com.ysheng.auth.core.ClientServiceImpl}.
 */
public class ClientServiceImplTest {

  @Test(enabled = false)
  public void dummy() {
  }

  /**
   * Tests for {@link com.ysheng.auth.core.ClientServiceImpl#registerClient}.
   */
  public static class RegisterClientTest {

    @Test
    public void failsWithNullRedirectUri() {
      ClientRegistrationRequest request = new ClientRegistrationRequest();
      request.setType(ClientType.CONFIDENTIAL);
      request.setRedirectUri(null);

      ClientServiceImpl service = new ClientServiceImpl(null, null);

      try {
        service.registerClient(request);
        fail("Client registration should fail with null redirect URI");
      } catch (ClientRegistrationError ex) {
        assertThat(ex.getError(), is(ClientRegistrationErrorType.INVALID_REQUEST));
        assertThat(ex.getMessage(), equalTo("Redirect URI cannot be null"));
      }
    }

    @Test
    public void failsWithInvalidRedirectUri() {
      ClientRegistrationRequest request = new ClientRegistrationRequest();
      request.setType(ClientType.CONFIDENTIAL);
      request.setRedirectUri("invalidUri");

      ClientServiceImpl service = new ClientServiceImpl(null, null);

      try {
        service.registerClient(request);
        fail("Client registration should fail with invalid redirect URI");
      } catch (ClientRegistrationError ex) {
        assertThat(ex.getError(), is(ClientRegistrationErrorType.INVALID_REQUEST));
        assertThat(ex.getMessage(), equalTo("Invalid redirect URI: invalidUri"));
      }
    }

    @Test
    public void failsWithExistingClient() {
      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientByRedirectUri(anyString());

      ClientRegistrationRequest request = new ClientRegistrationRequest();
      request.setType(ClientType.CONFIDENTIAL);
      request.setRedirectUri("http://1.2.3.4");

      ClientServiceImpl service = new ClientServiceImpl(database, null);

      try {
        service.registerClient(request);
        fail("Client registration should fail with existing client");
      } catch (ClientRegistrationError ex) {
        assertThat(ex.getError(), is(ClientRegistrationErrorType.ALREADY_REGISTERED));
        assertThat(ex.getMessage(), equalTo("Client already registered with redirect URI: http://1.2.3.4"));
      }
    }

    @Test
    public void succeedsToRegister() throws Throwable {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientByRedirectUri(anyString());
      doNothing().when(database).storeClient(any(Client.class));

      AuthValueGenerator authValueGenerator = mock(AuthValueGenerator.class);
      doReturn("clientId").when(authValueGenerator).generateClientId();
      doReturn("clientSecret").when(authValueGenerator).generateClientSecret();

      ClientRegistrationRequest request = new ClientRegistrationRequest();
      request.setType(ClientType.CONFIDENTIAL);
      request.setRedirectUri("http://1.2.3.4");

      ClientServiceImpl service = new ClientServiceImpl(database, authValueGenerator);

      ClientRegistrationResponse response = service.registerClient(request);
      assertThat(response.getClientId(), equalTo("clientId"));
      assertThat(response.getClientSecret(), equalTo("clientSecret"));
    }
  }
}
