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

package com.ysheng.auth.backend.redis.test.adapter;

import com.ysheng.auth.backend.redis.adapter.ClientAdapter;
import com.ysheng.auth.model.ClientType;
import com.ysheng.auth.model.database.Client;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link com.ysheng.auth.backend.redis.adapter.ClientAdapter}.
 */
public class ClientAdapterTest {

  @Test(dataProvider = "ClientKeyMetadata")
  public void succeedsToGetKey(
      String clientId,
      String redirectUri,
      String expectedKey) {
    String key = ClientAdapter.getKey(clientId, redirectUri);

    assertThat(key, equalTo(expectedKey));
  }

  @DataProvider(name = "ClientKeyMetadata")
  public Object[][] provideClientKeyMetadata() {
    return new Object[][] {
        { "clientId", "redirectUri", "auth-client:clientId:redirectUri" },
        { "clientId", null, "auth-client:clientId:*" },
        { null, "redirectUri", "auth-client:*:redirectUri" }
    };
  }

  @Test
  public void succeedsToConvertToHash() {
    Client client = new Client();
    client.setId("clientId");
    client.setSecret("clientSecret");
    client.setType(ClientType.CONFIDENTIAL);
    client.setRedirectUri("http://1.2.3.4");

    Map<String, String> hash = ClientAdapter.toHash(client);

    assertThat(hash.size(), is(4));
    assertThat(hash.containsKey("id"), is(true));
    assertThat(hash.get("id"), equalTo("clientId"));
    assertThat(hash.containsKey("secret"), is(true));
    assertThat(hash.get("secret"), equalTo("clientSecret"));
    assertThat(hash.containsKey("type"), is(true));
    assertThat(hash.get("type"), equalTo("CONFIDENTIAL"));
    assertThat(hash.containsKey("redirectUri"), is(true));
    assertThat(hash.get("redirectUri"), equalTo("http://1.2.3.4"));
  }

  @Test
  public void succeedsToConvertFromHash() {
    Map<String, String> hash = new HashMap<>();
    hash.put("id", "clientId");
    hash.put("secret", "clientSecret");
    hash.put("type", "CONFIDENTIAL");
    hash.put("redirectUri", "http://1.2.3.4");

    Client client = ClientAdapter.fromHash(hash);
    assertThat(client, notNullValue());
    assertThat(client.getId(), equalTo("clientId"));
    assertThat(client.getSecret(), equalTo("clientSecret"));
    assertThat(client.getType(), equalTo(ClientType.CONFIDENTIAL));
    assertThat(client.getRedirectUri(), equalTo("http://1.2.3.4"));
  }
}
