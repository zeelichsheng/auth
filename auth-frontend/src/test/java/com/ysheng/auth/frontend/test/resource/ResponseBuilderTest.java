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

package com.ysheng.auth.frontend.test.resource;

import com.ysheng.auth.frontend.resource.ResponseBuilder;
import com.ysheng.auth.model.api.ApiList;
import com.ysheng.auth.model.api.client.Client;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Tests for {@link com.ysheng.auth.frontend.resource.ResponseBuilder}.
 */
public class ResponseBuilderTest {

  @Test(dataProvider = "ResponseEntity")
  public void succeedsToBuild(Object entity) {
    Response response = ResponseBuilder.build(
        Response.Status.CREATED,
        entity);

    assertThat(response.getStatus(), equalTo(Response.Status.CREATED.getStatusCode()));
    assertThat(response.getEntity(), equalTo(entity));

    if (entity != null) {
      assertThat(response.getMediaType(), equalTo(MediaType.APPLICATION_JSON_TYPE));
    }
  }

  @DataProvider(name = "ResponseEntity")
  public Object[][] provideResponseEntity() {
    return new Object[][] {
        { null },
        { new Client() },
        { new ApiList<Client>() }
    };
  }
}
