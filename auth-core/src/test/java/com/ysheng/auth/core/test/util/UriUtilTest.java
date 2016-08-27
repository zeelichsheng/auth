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

package com.ysheng.auth.core.test.util;

import com.ysheng.auth.core.util.UriUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link com.ysheng.auth.core.util.UriUtil}.
 */
public class UriUtilTest {

  @Test(dataProvider = "ValidUris")
  public void succeedsUriValidation(String validUri) {
    boolean isValid = UriUtil.isValidUri(validUri);
    assertThat(isValid, is(true));
  }

  @DataProvider(name = "ValidUris")
  public Object[][] provideValidUris() {
    return new Object[][] {
        { "http://" },
        { "http://1.2.3.4" },
        { "http://1.2.3.4/foo/bar" }
    };
  }

  @Test(dataProvider = "InvalidUris")
  public void failsUriValidation(String invalidUri) {
    boolean isValid = UriUtil.isValidUri(invalidUri);
    assertThat(isValid, is(false));
  }

  @DataProvider(name = "InvalidUris")
  public Object[][] provideInvalidUris() {
    return new Object[][] {
        { "1.2.3.4" },
        { "foobar" },
        { " foo bar " },
        { "" },
        { null }
    };
  }
}
