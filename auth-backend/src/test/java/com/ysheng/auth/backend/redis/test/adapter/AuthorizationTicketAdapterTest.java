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
import com.ysheng.auth.backend.model.AuthorizationTicket;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

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

    String hash = AuthorizationTicketAdapter.toHash(authorizationTicket);

    assertThat(hash, equalTo(
        "{\"code\":\"code\",\"clientId\":\"clientId\",\"redirectUri\":\"http://1.2.3.4\"," +
        "\"scope\":\"scope\",\"state\":\"state\"}"
    ));
  }

  @Test
  public void succeedsToConvertFromHash() {
    String hash =
        "{\"code\":\"code\",\"clientId\":\"clientId\",\"redirectUri\":\"http://1.2.3.4\"," +
            "\"scope\":\"scope\",\"state\":\"state\"}";

    AuthorizationTicket authorizationTicket = AuthorizationTicketAdapter.fromHash(hash);
    assertThat(authorizationTicket, notNullValue());

    assertThat(authorizationTicket.getCode(), equalTo("code"));
    assertThat(authorizationTicket.getClientId(), equalTo("clientId"));
    assertThat(authorizationTicket.getRedirectUri(), equalTo("http://1.2.3.4"));
    assertThat(authorizationTicket.getScope(), equalTo("scope"));
    assertThat(authorizationTicket.getState(), equalTo("state"));
  }
}
