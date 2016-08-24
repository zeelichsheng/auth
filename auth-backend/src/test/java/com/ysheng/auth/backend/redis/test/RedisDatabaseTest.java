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
import com.ysheng.auth.model.api.ClientType;
import com.ysheng.auth.model.api.authcode.AuthorizationTicket;
import com.ysheng.auth.model.api.client.Client;
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
}
