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

package com.gu.conf;

import java.util.HashSet;
import java.util.Set;

public class CompositeConfiguration extends ConfigurationAdaptor {

   private Configuration primary;
   private Configuration secondary;

   public CompositeConfiguration(Configuration primary, Configuration secondary) {
      this.primary = primary;
      this.secondary = secondary;
   }

   /**
    * Get the source of a named property.
    *
    * @param propertyName name of the property
    * @return the source of the property in the format "conf(1|2):[source]",
    *         or null if the property is unknown
    */
   public String getPropertySource(String propertyName) {
      String source = primary.getPropertySource(propertyName);
      if (source != null) {
         source = "primary:" + source;
      }

      if (source == null) {
         source = secondary.getPropertySource(propertyName);
         if (source != null) {
            source = "secondary:" + source;
         }
      }

      return source;
   }

   /**
    * Return the value of property, or default if property is not set
    *
    * @param propertyName name of the property
    * @param defaultValue value to return if property not set
    * @return value of the property or defaultValue if property not set
    */
   public String getStringProperty(String propertyName, String defaultValue) {
      String value = primary.getStringProperty(propertyName, null);
      if (value == null) {
         value = secondary.getStringProperty(propertyName, defaultValue);
      }

      return value;
   }

   @Override
   public Set<String> getPropertyNames() {
      Set<String> propertyNames = new HashSet<String>();
      propertyNames.addAll(primary.getPropertyNames());
      propertyNames.addAll(secondary.getPropertyNames());

      return propertyNames;
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();

      stringBuilder.append("primary\n");
      stringBuilder.append("-------\n");
      stringBuilder.append(primary.toString());

      stringBuilder.append("\n");

      stringBuilder.append("secondary\n");
      stringBuilder.append("---------\n");
      stringBuilder.append(secondary.toString());

      return stringBuilder.toString();
   }
}