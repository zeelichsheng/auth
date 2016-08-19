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

import com.ysheng.auth.common.backend.Database;
import com.ysheng.auth.common.core.exception.AuthCodeAccessTokenException;
import com.ysheng.auth.common.core.exception.AuthCodeAuthorizationException;
import com.ysheng.auth.common.core.exception.ClientRegistrationException;
import com.ysheng.auth.common.core.generator.AuthValueGenerator;
import com.ysheng.auth.core.AuthorizationServiceImpl;
import com.ysheng.auth.model.AccessTokenType;
import com.ysheng.auth.model.ClientType;
import com.ysheng.auth.model.GrantType;
import com.ysheng.auth.model.ResponseType;
import com.ysheng.auth.model.authcode.AccessTokenErrorType;
import com.ysheng.auth.model.authcode.AccessTokenRequest;
import com.ysheng.auth.model.authcode.AccessTokenResponse;
import com.ysheng.auth.model.authcode.AuthorizationErrorType;
import com.ysheng.auth.model.authcode.AuthorizationRequest;
import com.ysheng.auth.model.authcode.AuthorizationResponse;
import com.ysheng.auth.model.client.ClientRegistrationErrorType;
import com.ysheng.auth.model.client.ClientRegistrationRequest;
import com.ysheng.auth.model.client.ClientRegistrationResponse;
import com.ysheng.auth.model.database.AuthorizationTicket;
import com.ysheng.auth.model.database.Client;
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
 * Tests for {@link com.ysheng.auth.core.AuthorizationServiceImpl}.
 */
public class AuthorizationServiceImplTest {

  @Test(enabled = false)
  public void dummy() {
  }

  /**
   * Tests for {@link com.ysheng.auth.core.AuthorizationServiceImpl#registerClient}.
   */
  public static class RegisterClientTest {

    @Test
    public void failsWithNullRedirectUri() {
      ClientRegistrationRequest request = new ClientRegistrationRequest();
      request.setType(ClientType.CONFIDENTIAL);
      request.setRedirectUri(null);

      AuthorizationServiceImpl service = new AuthorizationServiceImpl(null, null);

      try {
        service.registerClient(request);
        fail("Client registration should fail with null redirect URI");
      } catch (ClientRegistrationException ex) {
        assertThat(ex.getError(), is(ClientRegistrationErrorType.INVALID_REQUEST));
        assertThat(ex.getMessage(), equalTo("Redirect URI cannot be null"));
      }
    }

    @Test
    public void failsWithInvalidRedirectUri() {
      ClientRegistrationRequest request = new ClientRegistrationRequest();
      request.setType(ClientType.CONFIDENTIAL);
      request.setRedirectUri("invalidUri");

      AuthorizationServiceImpl service = new AuthorizationServiceImpl(null, null);

      try {
        service.registerClient(request);
        fail("Client registration should fail with invalid redirect URI");
      } catch (ClientRegistrationException ex) {
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

      AuthorizationServiceImpl service = new AuthorizationServiceImpl(database, null);

      try {
        service.registerClient(request);
        fail("Client registration should fail with existing client");
      } catch (ClientRegistrationException ex) {
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

      AuthorizationServiceImpl service = new AuthorizationServiceImpl(database, authValueGenerator);

      ClientRegistrationResponse response = service.registerClient(request);
      assertThat(response.getClientId(), equalTo("clientId"));
      assertThat(response.getClientSecret(), equalTo("clientSecret"));
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.AuthorizationServiceImpl#authorize}.
   */
  public static class AuthorizeTest {

    @Test
    public void failsWithUnsupportedResponseType() {
      AuthorizationRequest request = new AuthorizationRequest();
      request.setResponseType(ResponseType.TOKEN);

      AuthorizationServiceImpl service = new AuthorizationServiceImpl(null, null);

      try {
        service.authorize(request);
        fail("Authorization should fail with unsupported response type");
      } catch (AuthCodeAuthorizationException ex) {
        assertThat(ex.getError(), is(AuthorizationErrorType.UNSUPPORTED_RESPONSE_TYPE));
        assertThat(ex.getMessage(), equalTo("Unsupported response type in request: TOKEN"));
      }
    }

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      AuthorizationRequest request = new AuthorizationRequest();
      request.setResponseType(ResponseType.CODE);
      request.setClientId("clientId");

      AuthorizationServiceImpl service = new AuthorizationServiceImpl(database, null);

      try {
        service.authorize(request);
        fail("Authorization should fail with non-exist client");
      } catch (AuthCodeAuthorizationException ex) {
        assertThat(ex.getError(), is(AuthorizationErrorType.UNAUTHORIZED_CLIENT));
        assertThat(ex.getMessage(), equalTo("Unable to find client with ID: clientId"));
      }
    }

    @Test
    public void succeedsToAuthorize() throws Throwable {
      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientById(anyString());

      AuthValueGenerator authValueGenerator = mock(AuthValueGenerator.class);
      doReturn("authCode").when(authValueGenerator).generateAuthCode();

      AuthorizationRequest request = new AuthorizationRequest();
      request.setResponseType(ResponseType.CODE);
      request.setClientId("clientId");
      request.setState("state");

      AuthorizationServiceImpl service = new AuthorizationServiceImpl(database, authValueGenerator);

      AuthorizationResponse response = service.authorize(request);
      assertThat(response.getCode(), equalTo("authCode"));
      assertThat(response.getState(), equalTo("state"));
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.AuthorizationServiceImpl#issueAccessToken}.
   */
  public static class IssueAccessTokenTest {

    @Test
    public void failsWithUnsupportedGrantType() {
      AccessTokenRequest request = new AccessTokenRequest();
      request.setGrantType(GrantType.IMPLICIT);

      AuthorizationServiceImpl service = new AuthorizationServiceImpl(null, null);

      try {
        service.issueAccessToken(request);
        fail("Issuing access token should fail with unsupported grant type");
      } catch (AuthCodeAccessTokenException ex) {
        assertThat(ex.getError(), is(AccessTokenErrorType.UNSUPPORTED_GRANT_TYPE));
        assertThat(ex.getMessage(), equalTo("Unsupported grant type in request: IMPLICIT"));
      }
    }

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      AccessTokenRequest request = new AccessTokenRequest();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);
      request.setClientId("clientId");

      AuthorizationServiceImpl service = new AuthorizationServiceImpl(database, null);

      try {
        service.issueAccessToken(request);
        fail("Issuing access token should fail with non-exist client");
      } catch (AuthCodeAccessTokenException ex) {
        assertThat(ex.getError(), is(AccessTokenErrorType.UNAUTHORIZED_CLIENT));
        assertThat(ex.getMessage(), equalTo("Unable to find client with ID: clientId"));
      }
    }

    @Test
    public void failsWithNonExistAuthorizationTicket() {
      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientById(anyString());
      doReturn(null).when(database).findAuthorizationTicketByCodeAndClientId(anyString(), anyString());

      AccessTokenRequest request = new AccessTokenRequest();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);
      request.setClientId("clientId");
      request.setCode("code");

      AuthorizationServiceImpl service = new AuthorizationServiceImpl(database, null);

      try {
        service.issueAccessToken(request);
        fail("Issuing access token should fail with non-exist authorization ticket");
      } catch (AuthCodeAccessTokenException ex) {
        assertThat(ex.getError(), is(AccessTokenErrorType.INVALID_REQUEST));
        assertThat(ex.getMessage(), equalTo("Unable to find authorization code: code"));
      }
    }

    @Test
    public void failsWithMismatchingRedirectUris() {
      AuthorizationTicket authorizationTicket = new AuthorizationTicket();
      authorizationTicket.setRedirectUri("http://1.2.3.4");
      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientById(anyString());
      doReturn(authorizationTicket).when(database).findAuthorizationTicketByCodeAndClientId(anyString(), anyString());

      AccessTokenRequest request = new AccessTokenRequest();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);
      request.setClientId("clientId");
      request.setCode("code");
      request.setRedirectUri("http://5.6.7.8");

      AuthorizationServiceImpl service = new AuthorizationServiceImpl(database, null);

      try {
        service.issueAccessToken(request);
        fail("Issuing access token should fail with mis-matching redirect URIs");
      } catch (AuthCodeAccessTokenException ex) {
        assertThat(ex.getError(), is(AccessTokenErrorType.INVALID_REQUEST));
        assertThat(ex.getMessage(), equalTo("Mismatch of redirect URI: http://5.6.7.8"));
      }
    }

    @Test
    public void failsWithMismatchClients() {
      Client client = new Client();
      client.setId("clientId2");
      AuthorizationTicket authorizationTicket = new AuthorizationTicket();
      authorizationTicket.setRedirectUri("http://1.2.3.4");
      authorizationTicket.setClientId("clientId1");
      Database database = mock(Database.class);
      doReturn(client).when(database).findClientById(anyString());
      doReturn(authorizationTicket).when(database).findAuthorizationTicketByCodeAndClientId(anyString(), anyString());

      AccessTokenRequest request = new AccessTokenRequest();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);
      request.setClientId("clientId2");
      request.setCode("code");
      request.setRedirectUri("http://1.2.3.4");

      AuthorizationServiceImpl service = new AuthorizationServiceImpl(database, null);

      try {
        service.issueAccessToken(request);
        fail("Issuing access token should fail with mis-matching client identifiers");
      } catch (AuthCodeAccessTokenException ex) {
        assertThat(ex.getError(), is(AccessTokenErrorType.INVALID_CLIENT));
        assertThat(ex.getMessage(), equalTo("Invalid client ID: clientId2"));
      }
    }

    @Test
    public void succeedsToIssueAccessToken() throws Throwable {
      Client client = new Client();
      client.setId("clientId");
      AuthorizationTicket authorizationTicket = new AuthorizationTicket();
      authorizationTicket.setRedirectUri("http://1.2.3.4");
      authorizationTicket.setClientId("clientId");
      Database database = mock(Database.class);
      doReturn(client).when(database).findClientById(anyString());
      doReturn(authorizationTicket).when(database).findAuthorizationTicketByCodeAndClientId(anyString(), anyString());

      AuthValueGenerator authValueGenerator = mock(AuthValueGenerator.class);
      doReturn("accessToken").when(authValueGenerator).generateAccessToken();

      AccessTokenRequest request = new AccessTokenRequest();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);
      request.setClientId("clientId");
      request.setCode("code");
      request.setRedirectUri("http://1.2.3.4");

      AuthorizationServiceImpl service = new AuthorizationServiceImpl(database, authValueGenerator);

      AccessTokenResponse response = service.issueAccessToken(request);
      assertThat(response.getAccessToken(), equalTo("accessToken"));
      assertThat(response.getTokenType(), is(AccessTokenType.BEARER));
      assertThat(response.getExpiresIn(), equalTo((long) Integer.MAX_VALUE));
    }
  }
}
