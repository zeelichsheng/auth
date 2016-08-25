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

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.ysheng.auth.frontend.mapper.InternalExceptionMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.DropwizardResourceConfig;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.inmemory.InMemoryTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import java.util.HashSet;
import java.util.Set;

/**
 * Defines the base class for resource tests.
 */
public class ResourceTestHelper {

  // A set of singleton objects.
  private final Set<Object> singletons;

  // The Jersey test object.
  private JerseyTest test;

  /**
   * Constructs a ResourceTestHelper class.
   */
  public ResourceTestHelper() {
    singletons = new HashSet<>();
  }

  public void addResource(Object resource) {
    singletons.add(resource);
  }

  public Client getClient() {
    return test.client();
  }

  public void setup() throws Exception {
    singletons.add(new InternalExceptionMapper());

    test = new JerseyTest() {
      @Override
      protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
        return new InMemoryTestContainerFactory();
      }

      @Override
      protected DeploymentContext configureDeployment() {
        final DropwizardResourceConfig resourceConfig = new DropwizardResourceConfig();

        for (Object singleton : singletons) {
          resourceConfig.register(singleton);
        }

        ServletDeploymentContext deploymentContext = ServletDeploymentContext.builder(resourceConfig)
            .initParam(ServletProperties.JAXRS_APPLICATION_CLASS, DropwizardResourceConfig.class.getName())
            .build();

        return deploymentContext;

      }

      @Override
      protected void configureClient(ClientConfig config) {
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
        jsonProvider.setMapper(Jackson.newObjectMapper());
        config.register(jsonProvider);
      }
    };

    test.setUp();
  }

  public void destroy() throws Exception {
    if (test != null) {
      test.tearDown();
    }
  }

  public <R, T> T post(
      String path,
      R request,
      Class<T> responseType) {
    return getClient()
        .target(path)
        .request()
        .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE))
        .readEntity(responseType);
  }

  public <R> void post(
      String path,
      R request) {
    getClient()
        .target(path)
        .request()
        .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
  }

  public <T> T get(
      String path,
      Class<T> responseType) {
    return getClient()
        .target(path)
        .request()
        .get()
        .readEntity(responseType);
  }

  public <T> T get(
      String path,
      GenericType<T> responseType) {
    return getClient()
        .target(path)
        .request()
        .get()
        .readEntity(responseType);
  }
}
