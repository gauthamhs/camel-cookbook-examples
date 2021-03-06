/*
 * Copyright (C) Scott Cranton and Jakub Korab
 * https://github.com/CamelCookbook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camelcookbook.security.encryptedproperties;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates the use of encrypted properties in Camel routes, where the master password is set in
 * a system property.
 */
public class EncryptedPropertiesPasswordInSystemPropertySpringTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        // normally this would be set along the lines of -DjasyptMasterPassword=encryptionPassword
        // in a place appropriate to the runtime
        System.setProperty("jasyptMasterPassword", "encryptionPassword");
        return new ClassPathXmlApplicationContext("META-INF/spring/encryptedProperties-PasswordInSystemProperty-context.xml");
    }

    @Test
    public void testPropertiesLoaded() throws InterruptedException {
        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(1);
        mockOut.message(0).header("dbPassword").isEqualTo("myDatabasePassword");

        template.sendBody("direct:in", "Foo");

        assertMockEndpointsSatisfied();
    }
}
