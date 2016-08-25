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
import com.ysheng.auth.core.AuthCodeGrantServiceImpl;
import com.ysheng.auth.core.generator.AuthValueGenerator;
import com.ysheng.auth.model.api.AccessTokenType;
import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.GrantType;
import com.ysheng.auth.model.api.ResponseType;
import com.ysheng.auth.model.api.authcode.AccessToken;
import com.ysheng.auth.model.api.authcode.AccessTokenError;
import com.ysheng.auth.model.api.authcode.AccessTokenErrorType;
import com.ysheng.auth.model.api.authcode.AccessTokenSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationError;
import com.ysheng.auth.model.api.authcode.AuthorizationErrorType;
import com.ysheng.auth.model.api.authcode.AuthorizationSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationTicket;
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.model.api.client.ClientNotFoundError;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Tests {@link com.ysheng.auth.core.AuthCodeGrantServiceImpl}.
 */
public class AuthCodeGrantServiceImplTest {

  @Test(enabled = false)
  public void dummy() {
  }

  /**
   * Tests for {@link com.ysheng.auth.core.AuthCodeGrantServiceImpl#authorize}.
   */
  public static class AuthorizeTest {

    @Test
    public void failsWithUnsupportedResponseType() {
      AuthorizationSpec request = new AuthorizationSpec();
      request.setResponseType(ResponseType.TOKEN);

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(null, null);

      try {
        service.authorize(request);
        fail("Authorization should fail with unsupported response type");
      } catch (AuthorizationError ex) {
        assertThat(ex.getError(), is(AuthorizationErrorType.UNSUPPORTED_RESPONSE_TYPE));
        assertThat(ex.getMessage(), equalTo("Unsupported response type in request: TOKEN"));
      }
    }

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      AuthorizationSpec request = new AuthorizationSpec();
      request.setResponseType(ResponseType.CODE);
      request.setClientId("clientId");

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.authorize(request);
        fail("Authorization should fail with non-exist client");
      } catch (AuthorizationError ex) {
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

      AuthorizationSpec request = new AuthorizationSpec();
      request.setResponseType(ResponseType.CODE);
      request.setClientId("clientId");

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, authValueGenerator);

      AuthorizationTicket response = service.authorize(request);
      assertThat(response.getCode(), equalTo("authCode"));
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.AuthCodeGrantServiceImpl#listAuthorizationTicket}.
   */
  public static class ListAuthorizationTicketTest {

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.listAuthorizationTicket(Optional.of("clientId"));
        fail("Listing authorization tickets should fail with non-exist client");
      } catch (ClientNotFoundError ex) {
        assertThat(ex.getMessage(), containsString("clientId"));
      }
    }

    @Test(dataProvider = "ClientForListAuthorizationTickets")
    public void succeedsToListAuthorizationTickets(
        Optional<String> clientId,
        Client client) throws Throwable {
      AuthorizationTicket ticket1 = new AuthorizationTicket();
      ticket1.setCode("code1");
      AuthorizationTicket ticket2 = new AuthorizationTicket();
      ticket1.setCode("code2");
      List<AuthorizationTicket> tickets = Arrays.asList(ticket1, ticket2);

      Database database = mock(Database.class);
      doReturn(client).when(database).findClientById(anyString());
      doReturn(tickets).when(database).listAuthorizationTickets(clientId.isPresent() ? clientId.get() : null);

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      ApiList<AuthorizationTicket> ticketApiList = service.listAuthorizationTicket(clientId);

      assertThat(ticketApiList.getItems().size(), is(2));
      assertThat(ticketApiList.getItems().get(0).getCode(), equalTo(ticket1.getCode()));
      assertThat(ticketApiList.getItems().get(1).getCode(), equalTo(ticket2.getCode()));
    }

    @DataProvider(name = "ClientForListAuthorizationTickets")
    public Object[][] provideClientForListAuthorizationTickets() {
      return new Object[][] {
          { Optional.empty(), null },
          { Optional.of("clientId"), new Client() }
      };
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.AuthCodeGrantServiceImpl#issueAccessToken}.
   */
  public static class IssueAccessTokenTest {

    @Test
    public void failsWithUnsupportedGrantType() {
      AccessTokenSpec request = new AccessTokenSpec();
      request.setGrantType(GrantType.IMPLICIT);

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(null, null);

      try {
        service.issueAccessToken(request);
        fail("Issuing access token should fail with unsupported grant type");
      } catch (AccessTokenError ex) {
        assertThat(ex.getError(), is(AccessTokenErrorType.UNSUPPORTED_GRANT_TYPE));
        assertThat(ex.getMessage(), equalTo("Unsupported grant type in request: IMPLICIT"));
      }
    }

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      AccessTokenSpec request = new AccessTokenSpec();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);
      request.setClientId("clientId");

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.issueAccessToken(request);
        fail("Issuing access token should fail with non-exist client");
      } catch (AccessTokenError ex) {
        assertThat(ex.getError(), is(AccessTokenErrorType.UNAUTHORIZED_CLIENT));
        assertThat(ex.getMessage(), equalTo("Unable to find client with ID: clientId"));
      }
    }

    @Test
    public void failsWithNonExistAuthorizationTicket() {
      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientById(anyString());
      doReturn(null).when(database).findAuthorizationTicketByCodeAndClientId(anyString(), anyString());

      AccessTokenSpec request = new AccessTokenSpec();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);
      request.setClientId("clientId");
      request.setCode("code");

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.issueAccessToken(request);
        fail("Issuing access token should fail with non-exist authorization ticket");
      } catch (AccessTokenError ex) {
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

      AccessTokenSpec request = new AccessTokenSpec();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);
      request.setClientId("clientId");
      request.setCode("code");
      request.setRedirectUri("http://5.6.7.8");

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.issueAccessToken(request);
        fail("Issuing access token should fail with mis-matching redirect URIs");
      } catch (AccessTokenError ex) {
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

      AccessTokenSpec request = new AccessTokenSpec();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);
      request.setClientId("clientId2");
      request.setCode("code");
      request.setRedirectUri("http://1.2.3.4");

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.issueAccessToken(request);
        fail("Issuing access token should fail with mis-matching client identifiers");
      } catch (AccessTokenError ex) {
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

      AccessTokenSpec request = new AccessTokenSpec();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);
      request.setClientId("clientId");
      request.setCode("code");
      request.setRedirectUri("http://1.2.3.4");

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, authValueGenerator);

      AccessToken response = service.issueAccessToken(request);
      assertThat(response.getAccessToken(), equalTo("accessToken"));
      assertThat(response.getTokenType(), is(AccessTokenType.BEARER));
      assertThat(response.getExpiresIn(), equalTo((long) Integer.MAX_VALUE));
    }
  }
}
