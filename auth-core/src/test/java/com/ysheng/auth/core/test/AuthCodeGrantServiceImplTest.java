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
import com.ysheng.auth.common.core.exception.AuthCodeAccessTokenException;
import com.ysheng.auth.common.core.exception.AuthCodeAuthorizationException;
import com.ysheng.auth.common.core.generator.AuthValueGenerator;
import com.ysheng.auth.core.AuthCodeGrantServiceImpl;
import com.ysheng.auth.model.AccessTokenType;
import com.ysheng.auth.model.GrantType;
import com.ysheng.auth.model.ResponseType;
import com.ysheng.auth.model.authcode.AccessTokenErrorType;
import com.ysheng.auth.model.authcode.AccessTokenRequest;
import com.ysheng.auth.model.authcode.AccessTokenResponse;
import com.ysheng.auth.model.authcode.AuthorizationErrorType;
import com.ysheng.auth.model.authcode.AuthorizationRequest;
import com.ysheng.auth.model.authcode.AuthorizationResponse;
import com.ysheng.auth.backend.model.AuthorizationTicket;
import com.ysheng.auth.backend.model.Client;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.fail;

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
      AuthorizationRequest request = new AuthorizationRequest();
      request.setResponseType(ResponseType.TOKEN);

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(null, null);

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

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

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

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, authValueGenerator);

      AuthorizationResponse response = service.authorize(request);
      assertThat(response.getCode(), equalTo("authCode"));
      assertThat(response.getState(), equalTo("state"));
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.AuthCodeGrantServiceImpl#issueAccessToken}.
   */
  public static class IssueAccessTokenTest {

    @Test
    public void failsWithUnsupportedGrantType() {
      AccessTokenRequest request = new AccessTokenRequest();
      request.setGrantType(GrantType.IMPLICIT);

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(null, null);

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

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

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

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

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

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

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

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, null);

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

      AuthCodeGrantServiceImpl service = new AuthCodeGrantServiceImpl(database, authValueGenerator);

      AccessTokenResponse response = service.issueAccessToken(request);
      assertThat(response.getAccessToken(), equalTo("accessToken"));
      assertThat(response.getTokenType(), is(AccessTokenType.BEARER));
      assertThat(response.getExpiresIn(), equalTo((long) Integer.MAX_VALUE));
    }
  }
}
