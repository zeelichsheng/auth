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

package com.ysheng.auth.portal.common.bastion.api;

import com.ysheng.auth.common.restful.BaseClient;
import com.ysheng.auth.common.restful.RestClient;
import com.ysheng.auth.portal.common.bastion.model.BrowserPluginSpec;
import org.apache.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Defines the initialization API class.
 */
public class InitializationApi extends BaseClient {

  private static final String BRWOSER_PLUGIN_PATH = "/client_config";

  /**
   * Constructs an InitializationApi object.
   *
   * @param restClient The client that performs RESTful operations.
   */
  public InitializationApi(RestClient restClient) {
    super(restClient);
  }

  /**
   * Gets the download URL of the Bastion Networks browser plugin.
   *
   * @param spec The browser plugin spec.
   * @return The object contains information of Bastion Networks browser plugin.
   * @throws Exception The error that contains detail information.
   */
  public Map<String, String> getBrowserPluginDownloadUrl(BrowserPluginSpec spec) throws Exception {
    LinkedHashMap<String, String> result = post(
        BRWOSER_PLUGIN_PATH,
        spec,
        HttpStatus.SC_OK);
    return result;
  }
}
