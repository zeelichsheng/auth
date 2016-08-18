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

import com.ysheng.auth.backend.redis.adapter.AuthorizationTicketAdapter;
import com.ysheng.auth.model.database.AuthorizationTicket;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link com.ysheng.auth.backend.redis.adapter.AuthorizationTicketAdapter}.
 */
public class AuthorizationTicketAdapterTest {

  @Test(dataProvider = "AuthorizationTicketKeyMetadata")
  public void succeedsToGetKey(
      String clientId,
      String code,
      String expectedKey) {
    String key = AuthorizationTicketAdapter.getKey(clientId, code);

    assertThat(key, equalTo(expectedKey));
  }

  @DataProvider(name = "AuthorizationTicketKeyMetadata")
  public Object[][] provideAuthorizationTicketKeyMetadata() {
    return new Object[][] {
        { "clientId", "code", "auth-authorization-ticket:clientId:code" },
        { "clientId", null, "auth-authorization-ticket:clientId:*" },
        { null, "code", "auth-authorization-ticket:*:code" }
    };
  }

  @Test
  public void succeedsToConvertToHash() {
    AuthorizationTicket authorizationTicket = new AuthorizationTicket();
    authorizationTicket.setCode("code");
    authorizationTicket.setClientId("clientId");
    authorizationTicket.setRedirectUri("http://1.2.3.4");
    authorizationTicket.setScope("scope");
    authorizationTicket.setState("state");

    Map<String, String> hash = AuthorizationTicketAdapter.toHash(authorizationTicket);

    assertThat(hash.size(), is(5));
    assertThat(hash.containsKey("code"), is(true));
    assertThat(hash.get("code"), equalTo("code"));
    assertThat(hash.containsKey("clientId"), is(true));
    assertThat(hash.get("clientId"), equalTo("clientId"));
    assertThat(hash.containsKey("redirectUri"), is(true));
    assertThat(hash.get("redirectUri"), equalTo("http://1.2.3.4"));
    assertThat(hash.containsKey("scope"), is(true));
    assertThat(hash.get("scope"), equalTo("scope"));
    assertThat(hash.containsKey("state"), is(true));
    assertThat(hash.get("state"), equalTo("state"));
  }

  @Test
  public void succeedsToConvertFromHash() {
    Map<String, String> hash = new HashMap<>();
    hash.put("code", "code");
    hash.put("clientId", "clientId");
    hash.put("redirectUri", "http://1.2.3.4");
    hash.put("scope", "scope");
    hash.put("state", "state");

    AuthorizationTicket authorizationTicket = AuthorizationTicketAdapter.fromHash(hash);
    assertThat(authorizationTicket, notNullValue());

    assertThat(authorizationTicket.getCode(), equalTo("code"));
    assertThat(authorizationTicket.getClientId(), equalTo("clientId"));
    assertThat(authorizationTicket.getRedirectUri(), equalTo("http://1.2.3.4"));
    assertThat(authorizationTicket.getScope(), equalTo("scope"));
    assertThat(authorizationTicket.getState(), equalTo("state"));
  }
}
