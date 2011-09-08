/*
 * Copyright 2010 Guardian News and Media
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.gu.conf.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Provides information on the the nature of the environment that the machine forms a part of
 */
class InstallationConfiguration extends AbstractConfiguration {
   private static final Logger LOG = LoggerFactory.getLogger(InstallationConfiguration.class);
   public static final String INSTALLATION_PROPERTIES_LOCATION = "file:///etc/gu/install_vars";

   private AbstractConfiguration installation;

   InstallationConfiguration() {
      this(new FileAndResourceLoader());
   }

   InstallationConfiguration(FileAndResourceLoader loader) {
      super("Installation");

      LOG.info("Loading installation properties from " + INSTALLATION_PROPERTIES_LOCATION);
      installation = loader.getConfigurationFrom(INSTALLATION_PROPERTIES_LOCATION);
   }

   String getServiceDomain() {
      String property = getStringProperty("int.service.domain", null);
      if (property == null) {
         property = getStringProperty("INT_SERVICE_DOMAIN", null);
      }

      if (property == null) {
         LOG.info("unable to find INT_SERVICE_DOMAIN in " + INSTALLATION_PROPERTIES_LOCATION + " defaulting to \"default\"");
         property = "default";
      }

      return property;
   }

   String getStage() {
      String property = getStringProperty("stage", null);
      if (property == null) {
         property = getStringProperty("STAGE", null);
      }

      if (property == null) {
         LOG.info("unable to find STAGE in " + INSTALLATION_PROPERTIES_LOCATION + " defaulting to \"default\"");
         property = "default";
      }

      return property;
   }

   @Override
   public String toString() {
      return installation.toString();
   }

   @Override
   public AbstractConfiguration getPropertySource(String propertyName) {
      return installation.getPropertySource(propertyName);
   }

   @Override
   public String getStringProperty(String propertyName, String defaultValue) {
      return installation.getStringProperty(propertyName, defaultValue);
   }

   @Override
   public Set<String> getPropertyNames() {
      return installation.getPropertyNames();
   }
}
