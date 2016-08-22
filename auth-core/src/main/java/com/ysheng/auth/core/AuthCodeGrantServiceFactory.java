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

package com.ysheng.auth.core;

import com.ysheng.auth.backend.Database;
import com.ysheng.auth.core.generator.AuthValueGeneratorFactory;
import com.ysheng.auth.model.configuration.core.CoreConfiguration;

/**
 * Defines the service factory that produces auth code grant service.
 */
public class AuthCodeGrantServiceFactory {

  /**
   * Produces an auth code grant service object.
   *
   * @param database The database object.
   * @param configuration The core configuration.
   * @return An auth code grant service object.
   */
  public static AuthCodeGrantService produce(
      Database database,
      CoreConfiguration configuration) {
    return new AuthCodeGrantServiceImpl(
        database,
        AuthValueGeneratorFactory.produce(configuration.getAuthValueGeneratorConfiguration()));
  }
}
