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

import com.ysheng.auth.backend.redis.adapter.ImplicitAccessTokenAdapter;
import com.ysheng.auth.model.api.AccessTokenType;
import com.ysheng.auth.model.api.implicit.AccessToken;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Tests for {@link com.ysheng.auth.backend.redis.adapter.ImplicitAccessTokenAdapter}.
 */
public class ImplicitAccessTokenAdapterTest {

  @Test(dataProvider = "AccessTokenKeyMetadata")
  public void succeedsToGetKey(
      String clientId,
      String code,
      String expectedKey) {
    String key = ImplicitAccessTokenAdapter.getKey(clientId, code);

    assertThat(key, equalTo(expectedKey));
  }

  @DataProvider(name = "AccessTokenKeyMetadata")
  public Object[][] provideAccessTokenKeyMetadata() {
    return new Object[][] {
        { "clientId", "accessToken", "auth-implicit-access-token:clientId:accessToken" },
        { "clientId", null, "auth-implicit-access-token:clientId:*" },
        { null, "accessToken", "auth-implicit-access-token:*:accessToken" }
    };
  }

  @Test
  public void succeedsToConvertToHash() {
    AccessToken accessToken = new AccessToken();
    accessToken.setClientId("clientId");
    accessToken.setAccessToken("accessToken");
    accessToken.setTokenType(AccessTokenType.BEARER);
    accessToken.setExpiresIn(1000L);
    accessToken.setScope("scope");
    accessToken.setState("state");

    String hash = ImplicitAccessTokenAdapter.toHash(accessToken);

    assertThat(hash, equalTo(
        "{\"clientId\":\"clientId\",\"accessToken\":\"accessToken\",\"tokenType\":\"BEARER\",\"expiresIn\":1000," +
        "\"scope\":\"scope\",\"state\":\"state\"}"));
  }

  @Test
  public void succeedsToConvertFromHash() {
    String hash =
        "{\"clientId\":\"clientId\",\"accessToken\":\"accessToken\",\"tokenType\":\"BEARER\",\"expiresIn\":1000," +
        "\"scope\":\"scope\",\"state\":\"state\"}";

    AccessToken accessToken = ImplicitAccessTokenAdapter.fromHash(hash);
    assertThat(accessToken, notNullValue());

    assertThat(accessToken.getClientId(), equalTo("clientId"));
    assertThat(accessToken.getAccessToken(), equalTo("accessToken"));
    assertThat(accessToken.getTokenType(), is(AccessTokenType.BEARER));
    assertThat(accessToken.getExpiresIn(), equalTo(1000L));
    assertThat(accessToken.getState(), equalTo("state"));
    assertThat(accessToken.getScope(), equalTo("scope"));
  }


}
