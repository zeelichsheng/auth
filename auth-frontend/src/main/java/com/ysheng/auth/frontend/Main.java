/*
 * Copyright 2016 VMware, Inc. All Rights Reserved.
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

package com.ysheng.auth.frontend;

import com.ysheng.auth.frontend.configuration.ApiConfiguration;
import com.ysheng.auth.frontend.configuration.ConfigurationParser;
import com.ysheng.auth.frontend.service.ApiService;
import com.ysheng.auth.frontend.service.FactoryProviderImpl;

/**
 * The main class.
 */
public class Main {

  public static void main(String[] args) throws Exception {
    if (args.length == 2 && "server".equals(args[0])) {
      ApiConfiguration configuration = ConfigurationParser.parse(args[1]);
      new ApiService(configuration, new FactoryProviderImpl()).run(args);
    }
  }
}
