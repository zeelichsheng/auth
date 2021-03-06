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

package com.ysheng.auth.backend.redis.test;

import com.ysheng.auth.backend.redis.RedisClient;
import com.ysheng.auth.backend.redis.RedisDatabase;
import com.ysheng.auth.model.api.AccessTokenType;
import com.ysheng.auth.model.api.ClientType;
import com.ysheng.auth.model.api.authcode.AuthorizationTicket;
import com.ysheng.auth.model.api.client.Client;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tests for {@link com.ysheng.auth.backend.redis.RedisDatabase}.
 */
public class RedisDatabaseTest {

  ///
  /// Client related tests.
  ///

  @Test
  public void succeedsToStoreClient() {
    RedisClient redisClient = mock(RedisClient.class);
    doNothing().when(redisClient).set(anyString(), anyString());

    Client client = new Client();
    client.setId("clientId");
    client.setSecret("clientSecret");
    client.setType(ClientType.CONFIDENTIAL);
    client.setRedirectUri("http://1.2.3.4");

    RedisDatabase database = new RedisDatabase(redisClient);
    database.storeClient(client);

    verify(redisClient).set(anyString(), anyString());
  }

  @Test
  public void succeedsToRemoveClient() {
    RedisClient redisClient = mock(RedisClient.class);
    doNothing().when(redisClient).remove(anyString());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.removeClient("clientId");

    verify(redisClient).remove(anyString());
  }

  @Test
  public void succeedsToFindClientById() {
    RedisClient redisClient = mock(RedisClient.class);
    String hash =
        "{\"type\":\"CONFIDENTIAL\",\"id\":\"clientId\",\"secret\":\"clientSecret\"," +
            "\"redirectUri\":\"http://1.2.3.4\"}";

    doReturn(hash).when(redisClient).get(anyString());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.findClientById("clientId");

    verify(redisClient).get(anyString());
  }

  @Test
  public void succeedsToListClients() {
    RedisClient redisClient = mock(RedisClient.class);
    Set<String> keys = new HashSet<>();
    keys.add("key1");
    keys.add("key2");
    String hash1 =
        "{\"type\":\"CONFIDENTIAL\",\"id\":\"clientId1\",\"secret\":\"clientSecret1\"," +
            "\"redirectUri\":\"http://1.2.3.4\"}";
    String hash2 =
        "{\"type\":\"CONFIDENTIAL\",\"id\":\"clientId2\",\"secret\":\"clientSecret2\"," +
        "\"redirectUri\":\"http://5.6.7.8\"}";
    List<String> hashes = Arrays.asList(hash1, hash2);

    doReturn(keys).when(redisClient).keys(anyString());
    doReturn(hashes).when(redisClient).mget(anySet());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.listClients();

    verify(redisClient).keys(anyString());
    verify(redisClient).mget(anySet());
  }

  ///
  /// Auth Code Grant related tests.
  ///

  @Test
  public void succeedsToStoreAuthorizationTicket() {
    RedisClient redisClient = mock(RedisClient.class);
    doNothing().when(redisClient).set(anyString(), anyString());

    AuthorizationTicket authorizationTicket = new AuthorizationTicket();
    authorizationTicket.setCode("code");
    authorizationTicket.setClientId("clientId");
    authorizationTicket.setRedirectUri("http://1.2.3.4");
    authorizationTicket.setScope("scope");
    authorizationTicket.setState("state");

    RedisDatabase database = new RedisDatabase(redisClient);
    database.storeAuthorizationTicket(authorizationTicket);

    verify(redisClient).set(anyString(), anyString());
  }

  @Test(dataProvider = "ClientIdForListAuthorizationTickets")
  public void succeedsToListAuthorizationTickets(String clientId) {
    RedisClient redisClient = mock(RedisClient.class);
    Set<String> keys = new HashSet<>();
    keys.add("key1");
    keys.add("key2");
    String hash1 = "{\"code\":\"code1\",\"clientId\":\"clientId1\",\"redirectUri\":\"http://1.2.3.4\"," +
        "\"scope\":\"scope\",\"state\":\"state\"}";
    String hash2 = "{\"code\":\"code2\",\"clientId\":\"clientId2\",\"redirectUri\":\"http://1.2.3.4\"," +
        "\"scope\":\"scope\",\"state\":\"state\"}";
    List<String> hashes = Arrays.asList(hash1, hash2);

    doReturn(keys).when(redisClient).keys(anyString());
    doReturn(hashes).when(redisClient).mget(anySet());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.listAuthorizationTickets(clientId);

    verify(redisClient).keys(anyString());
    verify(redisClient).mget(anySet());
  }

  @DataProvider(name = "ClientIdForListAuthorizationTickets")
  public Object[][] provideClientIdForListAuthorizationTickets() {
    return new Object[][] {
        { null },
        { "clientId1" }
    };
  }

  @Test
  public void succeedsToRemoveAuthorizationTicket() {
    RedisClient redisClient = mock(RedisClient.class);
    doNothing().when(redisClient).remove(anyString());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.removeAuthorizationTicket("clientId", "code");

    verify(redisClient).remove(anyString());
  }

  @Test
  public void succeedsToFindAuthorizationTicketByCodeAndClientId() {
    RedisClient redisClient = mock(RedisClient.class);
    String hash = "{\"code\":\"code\",\"clientId\":\"clientId\",\"redirectUri\":\"http://1.2.3.4\"," +
        "\"scope\":\"scope\",\"state\":\"state\"}";

    doReturn(hash).when(redisClient).get(anyString());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.findAuthorizationTicketByCodeAndClientId("code", "clientId");

    verify(redisClient).get(anyString());
  }

  @Test
  public void succeedsToStoreAccessToken() {
    RedisClient redisClient = mock(RedisClient.class);
    doNothing().when(redisClient).set(anyString(), anyString());

    com.ysheng.auth.model.api.authcode.AccessToken accessToken = new com.ysheng.auth.model.api.authcode.AccessToken();
    accessToken.setClientId("clientId");
    accessToken.setAccessToken("accessToken");
    accessToken.setTokenType(AccessTokenType.BEARER);
    accessToken.setExpiresIn(1000L);
    accessToken.setRefreshToken("refreshToken");
    accessToken.setScope("scope");

    RedisDatabase database = new RedisDatabase(redisClient);
    database.storeAccessToken(accessToken);

    verify(redisClient).set(anyString(), anyString());
  }

  @Test(dataProvider = "ClientIdForListAccessTokens")
  public void succeedsToListAccessTokens(String clientId) {
    RedisClient redisClient = mock(RedisClient.class);
    Set<String> keys = new HashSet<>();
    keys.add("key1");
    keys.add("key2");
    String hash1 = "{\"clientId\":\"clientId1\",\"accessToken\":\"accessToken1\",\"tokenType\":\"BEARER\"," +
        "\"expiresIn\":1000,\"refreshToken\":\"refreshToken\",\"scope\":\"scope\"}";
    String hash2 = "{\"clientId\":\"clientId2\",\"accessToken\":\"accessToken2\",\"tokenType\":\"BEARER\"," +
        "\"expiresIn\":1000,\"refreshToken\":\"refreshToken\",\"scope\":\"scope\"}";
    List<String> hashes = Arrays.asList(hash1, hash2);

    doReturn(keys).when(redisClient).keys(anyString());
    doReturn(hashes).when(redisClient).mget(anySet());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.listAccessTokens(clientId);

    verify(redisClient).keys(anyString());
    verify(redisClient).mget(anySet());
  }

  @DataProvider(name = "ClientIdForListAccessTokens")
  public Object[][] provideClientIdForListAccessTokens() {
    return new Object[][] {
        { null },
        { "clientId1" }
    };
  }

  @Test
  public void succeedsToRemoveAccessToken() {
    RedisClient redisClient = mock(RedisClient.class);
    doNothing().when(redisClient).remove(anyString());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.removeAccessToken("clientId", "token");

    verify(redisClient).remove(anyString());
  }

  @Test
  public void succeedsToGetAccessToken() {
    RedisClient redisClient = mock(RedisClient.class);
    String hash = "{\"clientId\":\"clientId\",\"accessToken\":\"accessToken\",\"tokenType\":\"BEARER\"," +
        "\"expiresIn\":1000,\"refreshToken\":\"refreshToken\",\"scope\":\"scope\"}";

    doReturn(hash).when(redisClient).get(anyString());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.findAccessTokenByClientIdAndToken("clientId", "token");

    verify(redisClient).get(anyString());
  }

  ///
  /// Implicit Grant related tests.
  ///

  @Test
  public void succeedsToStoreImplictAccessToken() {
    RedisClient redisClient = mock(RedisClient.class);
    doNothing().when(redisClient).set(anyString(), anyString());

    com.ysheng.auth.model.api.implicit.AccessToken accessToken = new com.ysheng.auth.model.api.implicit.AccessToken();
    accessToken.setClientId("clientId");
    accessToken.setAccessToken("accessToken");
    accessToken.setTokenType(AccessTokenType.BEARER);
    accessToken.setExpiresIn(1000L);
    accessToken.setScope("scope");
    accessToken.setState("state");

    RedisDatabase database = new RedisDatabase(redisClient);
    database.storeImplictAccessToken(accessToken);

    verify(redisClient).set(anyString(), anyString());
  }

  @Test(dataProvider = "ClientIdForListImplicitAccessTokens")
  public void succeedsToListImplicitAccessTokens(String clientId) {
    RedisClient redisClient = mock(RedisClient.class);
    Set<String> keys = new HashSet<>();
    keys.add("key1");
    keys.add("key2");
    String hash1 =
        "{\"clientId\":\"clientId1\",\"accessToken\":\"accessToken1\",\"tokenType\":\"BEARER\",\"expiresIn\":1000," +
            "\"scope\":\"scope\",\"state\":\"state\"}";
    String hash2 =
        "{\"clientId\":\"clientId1\",\"accessToken\":\"accessToken2\",\"tokenType\":\"BEARER\",\"expiresIn\":1000," +
            "\"scope\":\"scope\",\"state\":\"state\"}";
    List<String> hashes = Arrays.asList(hash1, hash2);

    doReturn(keys).when(redisClient).keys(anyString());
    doReturn(hashes).when(redisClient).mget(anySet());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.listImplicitAccessTokens(clientId);

    verify(redisClient).keys(anyString());
    verify(redisClient).mget(anySet());
  }

  @DataProvider(name = "ClientIdForListImplicitAccessTokens")
  public Object[][] provideClientIdForListImplicitAccessTokens() {
    return new Object[][] {
        { null },
        { "clientId1" }
    };
  }

  @Test
  public void succeedsToRemoveImplicitAccessToken() {
    RedisClient redisClient = mock(RedisClient.class);
    doNothing().when(redisClient).remove(anyString());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.removeImplictAccessToken("clientId", "token");

    verify(redisClient).remove(anyString());
  }

  @Test
  public void succeedsToGetImplicitAccessToken() {
    RedisClient redisClient = mock(RedisClient.class);
    String hash =
        "{\"clientId\":\"clientId\",\"accessToken\":\"accessToken\",\"tokenType\":\"BEARER\",\"expiresIn\":1000," +
            "\"scope\":\"scope\",\"state\":\"state\"}";

    doReturn(hash).when(redisClient).get(anyString());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.findImplicitAccessTokenByClientIdAndToken("clientId", "token");

    verify(redisClient).get(anyString());
  }
}
