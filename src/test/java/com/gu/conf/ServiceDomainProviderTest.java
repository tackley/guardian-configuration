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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceDomainProviderTest {

    private static final String INSTALLATION_PROPERTIES_LOCATION = "file:///etc/gu/install_vars";

    private ServiceDomainProvider provider;
    @Mock
    private FileAndResourceLoader loader;

    @Before
    public void setUp() throws Exception {
        provider = new ServiceDomainProvider(loader);
    }

    @Test
    public void shouldReturnTheValueOfTheIntServiceDomainSystemPropertyIfSet() throws Exception {
        try {
            System.setProperty("int.service.domain", "myintservicedomain");
            assertThat(provider.getServiceDomain(), is("myintservicedomain"));
        } finally {
            System.clearProperty("int.service.domain");
        }
    }

    @Test
    public void shouldReadValueFromInstallationPropertiesFile() throws Exception {
        assertThat(System.getProperty("int.service.domain"), is(nullValue()));

        Properties properties = new PropertiesBuilder()
                .property("INT_SERVICE_DOMAIN", "myservicedomain")
                .toProperties();

        when(loader.getPropertiesFrom(INSTALLATION_PROPERTIES_LOCATION)).thenReturn(properties);
        
        assertThat(provider.getServiceDomain(), is("myservicedomain"));
    }

    @Test
    public void shouldDefaultWhenInstallationPropertiesFileExistsButDoesNotContainValue() throws Exception {
        Properties properties = new PropertiesBuilder()
                .toProperties();

        when(loader.getPropertiesFrom(INSTALLATION_PROPERTIES_LOCATION)).thenReturn(properties);

        assertThat(provider.getServiceDomain(), is("default"));
    }

}
