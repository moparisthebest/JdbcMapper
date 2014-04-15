/**
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 $Header:$
 */
package org.apache.beehive.netui.test.util.config;

import java.io.InputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.apache.beehive.netui.util.xml.XmlInputStreamResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;

/**
 */
public class SchemaValidationTest
    extends TestCase {

    public void testValidationSuccess()
        throws Exception {

        XmlInputStreamResolver xsdInputStreamResolver =
            new TestXmlInputStreamResolver("org/apache/beehive/netui/util/config/schema/beehive-netui-config.xsd");

        XmlInputStreamResolver xmlInputStreamResolver =
            new TestXmlInputStreamResolver("org/apache/beehive/netui/test/util/config/xmls/beehive-netui-config-default-1.0.xml");

        InputStream xsdIs = xsdInputStreamResolver.getInputStream();
        InputStream xmlIs = xmlInputStreamResolver.getInputStream();
        try {
            validate(xsdInputStreamResolver, xmlInputStreamResolver, "/beehive-netui-config.xsd");
        }
        catch(SchemaValidationException e) {
            assertTrue("Received an unexpected schema validation error", false);
        }
        finally {
            try {if(xsdIs != null) xsdIs.close();}catch(IOException io) {}
            try {if(xmlIs != null) xmlIs.close();}catch(IOException io) {}
        }
    }

    public void testNetUI1xConfigValidationSuccess()
        throws Exception {

        XmlInputStreamResolver xsdInputStreamResolver =
            new TestXmlInputStreamResolver("org/apache/beehive/netui/util/config/schema/beehive-netui-config.xsd");

        XmlInputStreamResolver xmlInputStreamResolver =
            new TestXmlInputStreamResolver("org/apache/beehive/netui/test/util/config/xmls/beehive-netui-config-default-1.x.xml");

        InputStream xsdIs = xsdInputStreamResolver.getInputStream();
        InputStream xmlIs = xmlInputStreamResolver.getInputStream();
        try {
            validate(xsdInputStreamResolver, xmlInputStreamResolver, "/beehive-netui-config.xsd");
        }
        catch(SchemaValidationException e) {
            assertTrue("Received an unexpected schema validation error", false);
        }
        finally {
            try {if(xsdIs != null) xsdIs.close();}catch(IOException io) {}
            try {if(xmlIs != null) xmlIs.close();}catch(IOException io) {}
        }
    }

    public void testValidationFailure()
        throws Exception {

        XmlInputStreamResolver xsdInputStreamResolver =
            new TestXmlInputStreamResolver("org/apache/beehive/netui/util/config/schema/beehive-netui-config.xsd");

        XmlInputStreamResolver xmlInputStreamResolver =
            new TestXmlInputStreamResolver("org/apache/beehive/netui/test/util/config/xmls/invalid-beehive-netui-config-default.xml");

        InputStream xsdIs = xsdInputStreamResolver.getInputStream();
        InputStream xmlIs = xmlInputStreamResolver.getInputStream();
        try {
            validate(xsdInputStreamResolver, xmlInputStreamResolver, "/beehive-netui-config.xsd");
        }
        catch(SchemaValidationException e) {
            return;
        }
        finally {
            try {if(xsdIs != null) xsdIs.close();}catch(IOException io) {}
            try {if(xmlIs != null) xmlIs.close();}catch(IOException io) {}
        }

        assertTrue("Expected a validation failure but did not receive one", false);
    }

    private void validate(final XmlInputStreamResolver xsdResolver, final XmlInputStreamResolver xmlResolver, String systemId)
        throws Exception {
        InputStream xmlIs = null;
        InputStream xsdIs = null;
        try {
            final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
            final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
            final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

            xmlIs = xmlResolver.getInputStream();
            xsdIs = xsdResolver.getInputStream();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf.setNamespaceAware(true);
            dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            dbf.setAttribute(JAXP_SCHEMA_SOURCE, xsdIs);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException exception) {
                    System.out.println("Validation warning validating config file \"" + xmlResolver.getResourcePath() +
                            "\" against XML Schema \"" + xsdResolver.getResourcePath());
                }

                public void error(SAXParseException exception) {
                        throw new SchemaValidationException("Validation errors occurred parsing the config file \"" +
                            xmlResolver.getResourcePath() + "\".  Cause: " + exception, exception);
                }

                public void fatalError(SAXParseException exception) {
                    throw new SchemaValidationException("Validation errors occurred parsing the config file \"" +
                        xmlResolver.getResourcePath() + "\".  Cause: " + exception, exception);
                }
            });

            db.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId) {
                    if(systemId.endsWith(systemId)) {
                        return new InputSource(xsdResolver.getInputStream());
                    }
                    else return null;
                }
            });

            Document document = db.parse(xmlIs);
        }
        catch(ParserConfigurationException ignore) {}
        finally {
            try {if(xsdIs != null) xsdIs.close();}catch(IOException io) {}
            try {if(xmlIs != null) xmlIs.close();}catch(IOException io) {}
        }
    }
}

class SchemaValidationException
    extends RuntimeException {

    SchemaValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}