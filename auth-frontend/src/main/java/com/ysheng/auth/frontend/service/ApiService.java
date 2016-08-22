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

package com.ysheng.auth.frontend.service;

import com.ysheng.auth.backend.Database;
import com.ysheng.auth.core.AuthCodeGrantService;
import com.ysheng.auth.core.ClientService;
import com.ysheng.auth.frontend.configuration.ApiConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Defines the API service.
 */
public class ApiService extends Application<ApiConfiguration> {

  // The API configuration.
  private ApiConfiguration configuration;

  // The backend database object.
  private Database database;

  // The client service that provides client related operations.
  private ClientService clientService;

  // The authorization code grant service that provides auth code
  // related operations.
  private AuthCodeGrantService authCodeGrantService;

  /**
   * Constructs an ApiService object.
   *
   * @param configuration The API configuration.
   */
  public ApiService(
      ApiConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Initializes the API service.
   *
   * @param bootstrap The bootstrap object.
   */
  @Override
  public void initialize(Bootstrap<ApiConfiguration> bootstrap) {
  }

  /**
   * Runs the API service.
   *
   * @param configuration The configuration object.
   * @param environment The environment object.
   */
  @Override
  public void run(
      ApiConfiguration configuration,
      Environment environment) {
  }
}
