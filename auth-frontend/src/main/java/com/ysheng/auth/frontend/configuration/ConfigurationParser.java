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

package com.ysheng.auth.frontend.configuration;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.DefaultConfigurationFactoryFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.validation.valuehandling.OptionalValidatedValueUnwrapper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Defines a parser helper that parses API configuration file.
 */
public class ConfigurationParser {

  /**
   * Parses the given configuration file and returns a configuration object.
   *
   * @param configurationFileName The name of the configuration file.
   * @return A configuration object.
   * @throws IOException The IO error that contains detail information.
   * @throws ConfigurationException The configuration error that contains detail information.
   */
  public static ApiConfiguration parse(String configurationFileName)
    throws IOException, ConfigurationException {
    if (StringUtils.isBlank(configurationFileName)) {
      throw new IllegalArgumentException("Configuration file cannot be blank");
    }

    ObjectMapper objectMapper = null;
    if (configurationFileName.endsWith("yml") || configurationFileName.endsWith("yaml")) {
      objectMapper = Jackson.newObjectMapper(new YAMLFactory());
    } else if (configurationFileName.endsWith("json")) {
      objectMapper = Jackson.newObjectMapper(new JsonFactory());
    } else {
      throw new IllegalArgumentException("Unrecognized configuration file type");
    }

    ValidatorFactory validatorFactory = Validation
        .byProvider(HibernateValidator.class)
        .configure()
        .addValidatedValueHandler(new OptionalValidatedValueUnwrapper())
        .buildValidatorFactory();

    final ConfigurationFactory<ApiConfiguration> configurationFactory =
        new DefaultConfigurationFactoryFactory<ApiConfiguration>()
          .create(ApiConfiguration.class, validatorFactory.getValidator(), objectMapper, "dw");

    final File file = new File(configurationFileName);
    if (!file.exists()) {
      throw new FileNotFoundException("Configuration file " + configurationFileName + " not found");
    }

    return configurationFactory.build(file);
  }
}
