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
import com.ysheng.auth.core.ImplicitGrantServiceImpl;
import com.ysheng.auth.core.generator.AuthValueGenerator;
import com.ysheng.auth.model.api.AccessTokenType;
import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.ResponseType;
import com.ysheng.auth.model.api.client.Client;
import com.ysheng.auth.model.api.exception.AccessTokenNotFoundException;
import com.ysheng.auth.model.api.exception.ClientNotFoundException;
import com.ysheng.auth.model.api.exception.ClientUnauthorizedException;
import com.ysheng.auth.model.api.exception.InternalException;
import com.ysheng.auth.model.api.exception.ResponseTypeUnsupportedException;
import com.ysheng.auth.model.api.implicit.AccessToken;
import com.ysheng.auth.model.api.implicit.AccessTokenRevokeSpec;
import com.ysheng.auth.model.api.implicit.AuthorizationGrantSpec;
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
 * Tests {@link com.ysheng.auth.core.ImplicitGrantServiceImpl}.
 */
public class ImplicitGrantServiceImplTest {

  @Test(enabled = false)
  public void dummy() {
  }

  /**
   * Tests for {@link com.ysheng.auth.core.ImplicitGrantServiceImpl#issueAccessToken}.
   */
  public static class IssueAccessTokenTest {

    @Test
    public void failsWithUnsupportedResponseType() {
      AuthorizationGrantSpec request = new AuthorizationGrantSpec();
      request.setResponseType(ResponseType.CODE);

      ImplicitGrantServiceImpl service = new ImplicitGrantServiceImpl(null, null);

      try {
        service.issueAccessToken("clientId", request);
        fail("Issuing implicit access token should fail with unsupported response type");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(ResponseTypeUnsupportedException.class));
        assertThat(ex.getErrorDescription(), equalTo("Response type not supported: CODE"));
      }
    }

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      AuthorizationGrantSpec request = new AuthorizationGrantSpec();
      request.setResponseType(ResponseType.TOKEN);

      ImplicitGrantServiceImpl service = new ImplicitGrantServiceImpl(database, null);

      try {
        service.issueAccessToken("clientId", request);
        fail("Issuing implicit access token should fail with non-exist client");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(ClientNotFoundException.class));
        assertThat(ex.getErrorDescription(), equalTo("Client not found with ID: clientId"));
      }
    }

    @Test
    public void succeedsToIssueAccessToken() throws Throwable {
      Client client = new Client();
      client.setId("clientId");
      Database database = mock(Database.class);
      doReturn(client).when(database).findClientById(anyString());
      doNothing().when(database).storeImplictAccessToken(any(AccessToken.class));

      AuthValueGenerator authValueGenerator = mock(AuthValueGenerator.class);
      doReturn("accessToken").when(authValueGenerator).generateAccessToken();

      AuthorizationGrantSpec request = new AuthorizationGrantSpec();
      request.setResponseType(ResponseType.TOKEN);

      ImplicitGrantServiceImpl service = new ImplicitGrantServiceImpl(database, authValueGenerator);

      AccessToken response = service.issueAccessToken("clientId", request);
      assertThat(response.getAccessToken(), equalTo("accessToken"));
      assertThat(response.getTokenType(), is(AccessTokenType.BEARER));
      assertThat(response.getExpiresIn(), equalTo((long) Integer.MAX_VALUE));
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.ImplicitGrantServiceImpl#listAccessTokens}.
   */
  public static class ListAccessTokenTest {

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      ImplicitGrantServiceImpl service = new ImplicitGrantServiceImpl(database, null);

      try {
        service.listAccessTokens("clientId");
        fail("Listing implicit access tokens should fail with non-exist client");
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
      doReturn(tickets).when(database).listImplicitAccessTokens(anyString());

      ImplicitGrantServiceImpl service = new ImplicitGrantServiceImpl(database, null);

      ApiList<AccessToken> ticketApiList = service.listAccessTokens("clientId");

      assertThat(ticketApiList.getItems().size(), is(2));
      assertThat(ticketApiList.getItems().get(0).getAccessToken(), equalTo(token1.getAccessToken()));
      assertThat(ticketApiList.getItems().get(1).getAccessToken(), equalTo(token2.getAccessToken()));
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.ImplicitGrantServiceImpl#revokeAccessToken}.
   */
  public static class RevokeAccessTokenTest {

    @Test
    public void failsWithNonExistClient() {
      AccessTokenRevokeSpec request = new AccessTokenRevokeSpec();
      request.setClientSecret("clientSecret");

      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      ImplicitGrantServiceImpl service = new ImplicitGrantServiceImpl(database, null);

      try {
        service.revokeAccessToken("clientId", "accessToken", request);
        fail("Revoking implicit access token should fail with non-exist client");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(ClientNotFoundException.class));
        assertThat(ex.getErrorDescription(), equalTo("Client not found with ID: clientId"));
      }
    }

    @Test
    public void failsWithUnauthorizedClient() {
      AccessTokenRevokeSpec request = new AccessTokenRevokeSpec();
      request.setClientSecret("clientSecret1");

      Client client = new Client();
      client.setSecret("clientSecret2");

      Database database = mock(Database.class);
      doReturn(client).when(database).findClientById(anyString());

      ImplicitGrantServiceImpl service = new ImplicitGrantServiceImpl(database, null);

      try {
        service.revokeAccessToken("clientId", "accessToken", request);
        fail("Revoking implicit access token should fail with unauthorized client");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(ClientUnauthorizedException.class));
        assertThat(ex.getErrorDescription(), equalTo("Client not authorized: clientId"));
      }
    }

    @Test
    public void failsWithNonExistAccessToken() {
      AccessTokenRevokeSpec request = new AccessTokenRevokeSpec();
      request.setClientSecret("clientSecret");

      Client client = new Client();
      client.setSecret("clientSecret");

      Database database = mock(Database.class);
      doReturn(client).when(database).findClientById(anyString());
      doReturn(null).when(database).findImplicitAccessTokenByClientIdAndToken(anyString(), anyString());

      ImplicitGrantServiceImpl service = new ImplicitGrantServiceImpl(database, null);

      try {
        service.revokeAccessToken("clientId", "accessToken", request);
        fail("Revoking implicit  access token should fail with non-exist access token");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(AccessTokenNotFoundException.class));
        assertThat(ex.getErrorDescription(), equalTo("Access token not found with client ID: clientId" +
            " and token: accessToken"));
      }
    }

    @Test
    public void succeedsToRevokeAccessToken() throws Throwable {
      AccessTokenRevokeSpec request = new AccessTokenRevokeSpec();
      request.setClientSecret("clientSecret");

      Client client = new Client();
      client.setSecret("clientSecret");

      Database database = mock(Database.class);
      doReturn(client).when(database).findClientById(anyString());
      doReturn(new AccessToken()).when(database)
          .findImplicitAccessTokenByClientIdAndToken(anyString(), anyString());

      ImplicitGrantServiceImpl service = new ImplicitGrantServiceImpl(database, null);

      service.revokeAccessToken("clientId", "accessToken", request);
    }
  }

  /**
   * Tests for {@link com.ysheng.auth.core.ImplicitGrantServiceImpl#getAccessToken}.
   */
  public static class GetAccessTokenTest {

    @Test
    public void failsWithNonExistClient() {
      Database database = mock(Database.class);
      doReturn(null).when(database).findClientById(anyString());

      ImplicitGrantServiceImpl service = new ImplicitGrantServiceImpl(database, null);

      try {
        service.getAccessToken("clientId", "accessToken");
        fail("Getting implicit access token should fail with non-exist client");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(ClientNotFoundException.class));
        assertThat(ex.getErrorDescription(), equalTo("Client not found with ID: clientId"));
      }
    }

    @Test
    public void failsWithNonExistAccessToken() {
      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientById(anyString());
      doReturn(null).when(database).findImplicitAccessTokenByClientIdAndToken(anyString(), anyString());

      ImplicitGrantServiceImpl service = new ImplicitGrantServiceImpl(database, null);

      try {
        service.getAccessToken("clientId", "accessToken");
        fail("Getting access token should fail with non-exist access token");
      } catch (InternalException ex) {
        assertThat(ex.getClass(), equalTo(AccessTokenNotFoundException.class));
        assertThat(ex.getErrorDescription(), equalTo("Access token not found with client ID: clientId" +
            " and token: accessToken"));
      }
    }

    @Test
    public void succeedsToGetAccessToken() throws Throwable {
      AccessToken token = new AccessToken();
      token.setClientId("clientId");
      token.setAccessToken("accessToken");
      Database database = mock(Database.class);
      doReturn(new Client()).when(database).findClientById(anyString());
      doReturn(token).when(database).findImplicitAccessTokenByClientIdAndToken(anyString(), anyString());

      ImplicitGrantServiceImpl service = new ImplicitGrantServiceImpl(database, null);

      AccessToken actualToken = service.getAccessToken("clientId", "accessToken");
      assertThat(actualToken.getClientId(), equalTo("clientId"));
      assertThat(actualToken.getAccessToken(), equalTo("accessToken"));
    }
  }
}
