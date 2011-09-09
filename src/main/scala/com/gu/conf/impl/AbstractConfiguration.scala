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
package com.gu.conf.impl

import com.gu.conf._
import com.gu.conf.fixtures.PropertiesBuilder
import java.util.Properties
import scala.collection.mutable.StringBuilder

private[conf] trait AbstractConfiguration extends Configuration {

  def hasProperty(propertyName: String): Boolean = {
    getPropertyNames.contains(propertyName)
  }

  def getStringProperty(propertyName: String, default: String): String = {
    getStringProperty(propertyName) getOrElse default
  }

  def getIntegerProperty(propertyName: String): Option[Int] = {
    getStringProperty(propertyName) map { _.toInt }
  }

  def getIntegerProperty(propertyName: String, defaultValue: Int): Int = {
    val option = try {
      getStringProperty(propertyName) map { _.toInt }
    } catch {
      case _ => None
    }

    option getOrElse defaultValue
  }

  def getStringPropertiesSplitByComma(propertyName: String): List[String] = {
    getStringProperty(propertyName) match {
      case Some(property) => (property split ",").toList
      case None => Nil
    }
  }

  def toProperties: Properties = {
    val builder = new PropertiesBuilder
    getPropertyNames foreach { name =>
      builder.property(name, getStringProperty(name).get)
    }

    builder.toProperties
  }

  /**
   * Get the source of a property
   *
   * @param propertyName name of the property
   * @return the source of the property or none if the property is unknown
   */
  def getPropertySource(propertyName: String): Option[AbstractConfiguration]

  /**
   * Return a count of the properties in this configuration
   *
   * @return size of this configuration
   */
  def size(): Int = getPropertyNames.size

  /**
   * Return a projection of this configuration to the given set of properties
   *
   * @param properties the names of the properties to retain in the projection
   * @return this configuration with only the named properties
   */
  def project(properties: Set[String]): AbstractConfiguration = {
    new ProjectedConfiguration(this, properties)
  }

  /**
   * Return a projection of this configuration to the given set of properties
   *
   * @param properties a configuration containing the names of the properties to retain in the projection
   * @return this configuration with only the given properties
   */
  def project(properties: AbstractConfiguration): AbstractConfiguration = {
    project(properties.getPropertyNames)
  }

  /**
   * Return a copy of this configuration with the the given set of properties removed
   *
   * @param properties the names of the properties to remove
   * @return this configuration with the named properties removed
   */
  def minus(properties: Set[String]): AbstractConfiguration = {
    new ProjectedConfiguration(this, getPropertyNames -- properties)
  }

  /**
   * Return a copy of this configuration with the the given set of properties removed
   *
   * @param properties a configuration containing the names of the properties to remove
   * @return this configuration with the named properties removed
   */
  def minus(properties: AbstractConfiguration): AbstractConfiguration = {
    minus(properties.getPropertyNames)
  }

  /**
   * Return a configuration with the same property names as this configuration but with
   * property values overriden with values from the given configuration.
   *
   * @param overrides a configuration containing the property override values. Properties
   *                  not in the base configuration are ignore.
   * @return this configuration with the given overrides.
   */
  def overrideWith(overrides: AbstractConfiguration): AbstractConfiguration = {
    new CompositeConfiguration(overrides.project(this), this)
  }

  override def toString(): String = {
    val builder = new StringBuilder

    builder append "# Properties from "
    builder append getIdentifier
    builder appendNewline

    getPropertyNames.toList.sorted foreach { name =>
      builder append PrinterUtil.propertyString(name, getStringProperty(name).get)
    }

    builder appendNewline

    builder.toString()
  }
}