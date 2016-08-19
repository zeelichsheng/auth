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
import com.ysheng.auth.model.ClientType;
import com.ysheng.auth.model.database.AuthorizationTicket;
import com.ysheng.auth.model.database.Client;
import org.testng.annotations.Test;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Tests for {@link com.ysheng.auth.backend.redis.RedisDatabase}.
 */
public class RedisDatabaseTest {

  @Test
  public void succeedsToStoreClient() {
    RedisClient redisClient = mock(RedisClient.class);
    doNothing().when(redisClient).hmset(anyString(), anyMap());

    Client client = new Client();
    client.setId("clientId");
    client.setSecret("clientSecret");
    client.setType(ClientType.CONFIDENTIAL);
    client.setRedirectUri("http://1.2.3.4");

    RedisDatabase database = new RedisDatabase(redisClient);
    database.storeClient(client);

    verify(redisClient).hmset(anyString(), anyMap());
  }

  @Test
  public void succeedsToFindClientById() {
    RedisClient redisClient = mock(RedisClient.class);
    Set<String> keys = new HashSet<>();
    keys.add("key");
    Map<String, String> hash = new HashMap<>();
    hash.put("id", "clientId");
    hash.put("secret", "clientSecret");
    hash.put("type", "CONFIDENTIAL");
    hash.put("redirectUri", "http://1.2.3.4");

    doReturn(keys).when(redisClient).keys(anyString());
    doReturn(hash).when(redisClient).hgetAll(anyString());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.findClientById("clientId");

    verify(redisClient).hgetAll(anyString());
  }

  @Test
  public void succeedsToFindClientByRedirectUri() {
    RedisClient redisClient = mock(RedisClient.class);
    Set<String> keys = new HashSet<>();
    keys.add("key");
    Map<String, String> hash = new HashMap<>();
    hash.put("id", "clientId");
    hash.put("secret", "clientSecret");
    hash.put("type", "CONFIDENTIAL");
    hash.put("redirectUri", "http://1.2.3.4");

    doReturn(keys).when(redisClient).keys(anyString());
    doReturn(hash).when(redisClient).hgetAll(anyString());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.findClientByRedirectUri("http://1.2.3.4");

    verify(redisClient).hgetAll(anyString());
  }

  @Test
  public void succeedsToStoreAuthorizationTicket() {
    RedisClient redisClient = mock(RedisClient.class);
    doNothing().when(redisClient).hmset(anyString(), anyMap());

    AuthorizationTicket authorizationTicket = new AuthorizationTicket();
    authorizationTicket.setCode("code");
    authorizationTicket.setClientId("clientId");
    authorizationTicket.setRedirectUri("http://1.2.3.4");
    authorizationTicket.setScope("scope");
    authorizationTicket.setState("state");

    RedisDatabase database = new RedisDatabase(redisClient);
    database.storeAuthorizationTicket(authorizationTicket);

    verify(redisClient).hmset(anyString(), anyMap());
  }

  @Test
  public void succeedsToFindAuthorizationTicketByCodeAndClientId() {
    RedisClient redisClient = mock(RedisClient.class);
    Map<String, String> hash = new HashMap<>();
    hash.put("clientId", "clientId");
    hash.put("code", "code");
    hash.put("scope", "scope");
    hash.put("redirectUri", "http://1.2.3.4");
    hash.put("state", "state");

    doReturn(hash).when(redisClient).hgetAll(anyString());

    RedisDatabase database = new RedisDatabase(redisClient);
    database.findAuthorizationTicketByCodeAndClientId("code", "clientId");

    verify(redisClient).hgetAll(anyString());
  }
}
