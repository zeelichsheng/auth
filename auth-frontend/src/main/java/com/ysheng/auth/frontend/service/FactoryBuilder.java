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

import com.ysheng.auth.backend.DatabaseFactory;
import com.ysheng.auth.core.AuthCodeGrantServiceFactory;
import com.ysheng.auth.core.ClientServiceFactory;

/**
 * Defines a builder class that builds factories.
 */
public class FactoryBuilder {;

  // The database factory.
  private DatabaseFactory databaseFactory;

  // The client service factory.
  private ClientServiceFactory clientServiceFactory;

  // The auth code grant service factory.
  private AuthCodeGrantServiceFactory authCodeGrantServiceFactory;

  /**
   * Constructs a FactoryBuilder object.
   */
  public FactoryBuilder() {
    this.databaseFactory = new DatabaseFactory();
    this.clientServiceFactory = new ClientServiceFactory();
    this.authCodeGrantServiceFactory = new AuthCodeGrantServiceFactory();
  }

  ///
  /// Getters.
  ///

  public DatabaseFactory getDatabaseFactory() {
    return databaseFactory;
  }

  public ClientServiceFactory getClientServiceFactory() {
    return clientServiceFactory;
  }

  public AuthCodeGrantServiceFactory getAuthCodeGrantServiceFactory() {
    return authCodeGrantServiceFactory;
  }
}
