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
import com.ysheng.auth.model.api.authcode.AccessTokenIssueSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationRevocationSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationGrantSpec;
import com.ysheng.auth.model.api.authcode.AuthorizationTicket;
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.model.api.exception.AuthorizationTicketNotFoundError;
import com.ysheng.auth.model.api.exception.ClientNotFoundException;
import com.ysheng.auth.model.api.exception.ClientUnauthorizedException;
import com.ysheng.auth.model.api.exception.GrantTypeUnsupportedException;
import com.ysheng.auth.model.api.exception.InternalException;
import com.ysheng.auth.model.api.exception.InvalidClientException;
import com.ysheng.auth.model.api.exception.InvalidRequestException;
import com.ysheng.auth.model.api.exception.ResponseTypeUnsupportedException;
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
      AuthorizationGrantSpec request = new AuthorizationGrantSpec();
      request.setResponseType(ResponseType.TOKEN);

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(null, null);

      try {
        service.authorize("clientId", request);
        fail("Authorization should fail with unsupported response type");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(ResponseTypeUnsupportedException.class));
        assertThat(ex.getErrorDescription(), equalTo("Response type not supported: TOKEN"));
      }
    }

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      AuthorizationGrantSpec request = new AuthorizationGrantSpec();
      request.setResponseType(ResponseType.CODE);

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.authorize("clientId", request);
        fail("Authorization should fail with non-exist client");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(ClientNotFoundException.class));
        assertThat(ex.getErrorDescription(), equalTo("Client not found with ID: clientId"));
      }
    }

    @Test
    public void succeedsToAuthorize() throws Throwable {
      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientById(anyString());

      AuthValueGenerator authValueGenerator = mock(AuthValueGenerator.class);
      doReturn("authCode").when(authValueGenerator).generateAuthCode();

      AuthorizationGrantSpec request = new AuthorizationGrantSpec();
      request.setResponseType(ResponseType.CODE);

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, authValueGenerator);

      AuthorizationTicket response = service.authorize("clientId", request);
      assertThat(response.getCode(), equalTo("authCode"));
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.AuthCodeGrantServiceImpl#revokeAuthorization}.
   */
  public static class RevokeAuthorizationTest {

    @Test
    public void failsWithNonExistClient() {
      AuthorizationRevocationSpec request = new AuthorizationRevocationSpec();
      request.setClientSecret("clientSecret");

      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.revokeAuthorization("clientId", "code", request);
        fail("Revoking authorization tickets should fail with non-exist client");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(ClientNotFoundException.class));
        assertThat(ex.getErrorDescription(), equalTo("Client not found with ID: clientId"));
      }
    }

    @Test
    public void failsWithUnauthorizedClient() {
      AuthorizationRevocationSpec request = new AuthorizationRevocationSpec();
      request.setClientSecret("clientSecret1");

      Client client = new Client();
      client.setSecret("clientSecret2");

      Database database = mock(Database.class);
      doReturn(client).when(database).findClientById(anyString());

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.revokeAuthorization("clientId", "code", request);
        fail("Revoking authorization tickets should fail with unauthorized client");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(ClientUnauthorizedException.class));
        assertThat(ex.getErrorDescription(), equalTo("Client not authorized: clientId"));
      }
    }

    @Test
    public void failsWithNonExistAuthorizationTicket() {
      AuthorizationRevocationSpec request = new AuthorizationRevocationSpec();
      request.setClientSecret("clientSecret");

      Client client = new Client();
      client.setSecret("clientSecret");

      Database database = mock(Database.class);
      doReturn(client).when(database).findClientById(anyString());
      doReturn(null).when(database).findAuthorizationTicketByCodeAndClientId(anyString(), anyString());

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.revokeAuthorization("clientId", "code", request);
        fail("Revoking authorization tickets should fail with non-exist authorization ticket");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(AuthorizationTicketNotFoundError.class));
        assertThat(ex.getErrorDescription(), equalTo("Authorization ticket not found with client ID: clientId" +
          " and code: code"));
      }
    }

    @Test
    public void succeedsToRevokeAuthorization() throws Throwable {
      AuthorizationRevocationSpec request = new AuthorizationRevocationSpec();
      request.setClientSecret("clientSecret");

      Client client = new Client();
      client.setSecret("clientSecret");

      Database database = mock(Database.class);
      doReturn(client).when(database).findClientById(anyString());
      doReturn(new AuthorizationTicket()).when(database)
          .findAuthorizationTicketByCodeAndClientId(anyString(), anyString());

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      service.revokeAuthorization("clientId", "code", request);
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.AuthCodeGrantServiceImpl#listAuthorizationTickets}.
   */
  public static class ListAuthorizationTicketsTest {

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.listAuthorizationTickets("clientId");
        fail("Listing authorization tickets should fail with non-exist client");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(ClientNotFoundException.class));
        assertThat(ex.getErrorDescription(), equalTo("Client not found with ID: clientId"));
      }
    }

    @Test
    public void succeedsToListAuthorizationTickets() throws Throwable {
      AuthorizationTicket ticket1 = new AuthorizationTicket();
      ticket1.setCode("code1");
      AuthorizationTicket ticket2 = new AuthorizationTicket();
      ticket1.setCode("code2");
      List<AuthorizationTicket> tickets = Arrays.asList(ticket1, ticket2);

      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientById(anyString());
      doReturn(tickets).when(database).listAuthorizationTickets(anyString());

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      ApiList<AuthorizationTicket> ticketApiList = service.listAuthorizationTickets("clientId");

      assertThat(ticketApiList.getItems().size(), is(2));
      assertThat(ticketApiList.getItems().get(0).getCode(), equalTo(ticket1.getCode()));
      assertThat(ticketApiList.getItems().get(1).getCode(), equalTo(ticket2.getCode()));
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.AuthCodeGrantServiceImpl#getAuthorizationTicket}.
   */
  public static class GetAuthorizationTicketTest {

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.getAuthorizationTicket("clientId", "code");
        fail("Getting authorization ticket should fail with non-exist client");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(ClientNotFoundException.class));
        assertThat(ex.getErrorDescription(), equalTo("Client not found with ID: clientId"));
      }
    }

    @Test
    public void failsWithNonExistTicket() {
      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientById(anyString());
      doReturn(null).when(database).findAuthorizationTicketByCodeAndClientId(anyString(), anyString());

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.getAuthorizationTicket("clientId", "code");
        fail("Getting authorization ticket should fail with non-exist ticket");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(AuthorizationTicketNotFoundError.class));
        assertThat(ex.getErrorDescription(), equalTo("Authorization ticket not found with client ID: clientId" +
          " and code: code"));
      }
    }

    @Test
    public void succeedsToGet() throws Throwable {
      AuthorizationTicket ticket = new AuthorizationTicket();
      ticket.setClientId("clientId");
      ticket.setCode("code");
      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientById(anyString());
      doReturn(ticket).when(database).findAuthorizationTicketByCodeAndClientId(anyString(), anyString());

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      AuthorizationTicket actualTicket = service.getAuthorizationTicket("clientId", "code");
      assertThat(actualTicket.getClientId(), equalTo("clientId"));
      assertThat(actualTicket.getCode(), equalTo("code"));
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.AuthCodeGrantServiceImpl#issueAccessToken}.
   */
  public static class IssueAccessTokenTest {

    @Test
    public void failsWithUnsupportedGrantType() {
      AccessTokenIssueSpec request = new AccessTokenIssueSpec();
      request.setGrantType(GrantType.IMPLICIT);

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(null, null);

      try {
        service.issueAccessToken("clientId", "code", request);
        fail("Issuing access token should fail with unsupported grant type");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(GrantTypeUnsupportedException.class));
        assertThat(ex.getErrorDescription(), equalTo("Grant type not supported: IMPLICIT"));
      }
    }

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      AccessTokenIssueSpec request = new AccessTokenIssueSpec();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.issueAccessToken("clientId", "code", request);
        fail("Issuing access token should fail with non-exist client");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(ClientNotFoundException.class));
        assertThat(ex.getErrorDescription(), equalTo("Client not found with ID: clientId"));
      }
    }

    @Test
    public void failsWithNonExistAuthorizationTicket() {
      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientById(anyString());
      doReturn(null).when(database).findAuthorizationTicketByCodeAndClientId(anyString(), anyString());

      AccessTokenIssueSpec request = new AccessTokenIssueSpec();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.issueAccessToken("clientId", "code", request);
        fail("Issuing access token should fail with non-exist authorization ticket");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(AuthorizationTicketNotFoundError.class));
        assertThat(ex.getErrorDescription(), equalTo("Authorization ticket not found with client ID: clientId" +
            " and code: code"));
      }
    }

    @Test
    public void failsWithMismatchingRedirectUris() {
      AuthorizationTicket authorizationTicket = new AuthorizationTicket();
      authorizationTicket.setRedirectUri("http://1.2.3.4");
      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientById(anyString());
      doReturn(authorizationTicket).when(database).findAuthorizationTicketByCodeAndClientId(anyString(), anyString());

      AccessTokenIssueSpec request = new AccessTokenIssueSpec();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);
      request.setRedirectUri("http://5.6.7.8");

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.issueAccessToken("clientId", "code", request);
        fail("Issuing access token should fail with mis-matching redirect URIs");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(InvalidRequestException.class));
        assertThat(ex.getErrorDescription(), equalTo("Mismatch of redirect URI: http://5.6.7.8"));
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

      AccessTokenIssueSpec request = new AccessTokenIssueSpec();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);
      request.setRedirectUri("http://1.2.3.4");

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.issueAccessToken("clientId2", "code", request);
        fail("Issuing access token should fail with mis-matching client identifiers");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(InvalidClientException.class));
        assertThat(ex.getErrorDescription(), equalTo("Invalid client: clientId2"));
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
      doNothing().when(database).storeAccessToken(any(AccessToken.class));
      doNothing().when(database).removeAuthorizationTicket(anyString(), anyString());

      AuthValueGenerator authValueGenerator = mock(AuthValueGenerator.class);
      doReturn("accessToken").when(authValueGenerator).generateAccessToken();

      AccessTokenIssueSpec request = new AccessTokenIssueSpec();
      request.setGrantType(GrantType.AUTHORIZATION_CODE);
      request.setRedirectUri("http://1.2.3.4");

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, authValueGenerator);

      AccessToken response = service.issueAccessToken("clientId", "code", request);
      assertThat(response.getAccessToken(), equalTo("accessToken"));
      assertThat(response.getTokenType(), is(AccessTokenType.BEARER));
      assertThat(response.getExpiresIn(), equalTo((long) Integer.MAX_VALUE));
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.AuthCodeGrantServiceImpl#listAccessTokens}.
   */
  public static class ListAccessTokensTest {

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      try {
        service.listAccessTokens("clientId");
        fail("Listing access tokens should fail with non-exist client");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(ClientNotFoundException.class));
        assertThat(ex.getErrorDescription(), equalTo("Client not found with ID: clientId"));
      }
    }

    @Test
    public void succeedsToListAccessTokens() throws Throwable {
      AccessToken token1 = new AccessToken();
      token1.setAccessToken("accessToken1");
      AccessToken token2 = new AccessToken();
      token2.setAccessToken("accessToken1");
      List<AccessToken> tickets = Arrays.asList(token1, token2);

      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientById(anyString());
      doReturn(tickets).when(database).listAccessTokens(anyString());

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

      ApiList<AccessToken> ticketApiList = service.listAccessTokens("clientId");

      assertThat(ticketApiList.getItems().size(), is(2));
      assertThat(ticketApiList.getItems().get(0).getAccessToken(), equalTo(token1.getAccessToken()));
      assertThat(ticketApiList.getItems().get(1).getAccessToken(), equalTo(token2.getAccessToken()));
    }
  }
}
