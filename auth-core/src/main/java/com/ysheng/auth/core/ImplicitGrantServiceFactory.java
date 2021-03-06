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
 * Defines the service factory that produces implicit grant service.
 */
public class ImplicitGrantServiceFactory {

  /**
   * Produces a implicit grant service object.
   *
   * @param database The database object.
   * @param configuration The core configuration.
   * @return A client service object.
   */
  public ImplicitGrantService produce(
      Database database,
      CoreConfiguration configuration) {
    return new ImplicitGrantServiceImpl(
        database,
        new AuthValueGeneratorFactory().produce(configuration.getAuthValueGeneratorConfiguration()));
  }
}
