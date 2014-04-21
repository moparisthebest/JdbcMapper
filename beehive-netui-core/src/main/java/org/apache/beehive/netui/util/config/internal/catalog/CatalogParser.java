/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */
package org.apache.beehive.netui.util.config.internal.catalog;

import java.io.InputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.beehive.netui.util.config.bean.CatalogConfig;
import org.apache.beehive.netui.util.config.bean.CommandConfig;
import org.apache.beehive.netui.util.config.bean.ChainConfig;
import org.apache.beehive.netui.util.config.bean.CustomPropertyConfig;
import org.apache.beehive.netui.util.xml.DomUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

/**
 *
 */
public class CatalogParser {

    private static final Log LOG = LogFactory.getLog(CatalogParser.class);
    private static final String CONFIG_SCHEMA = "org/apache/beehive/netui/util/config/internal/catalog/catalog-config.xsd";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    public static CatalogParser getInstance() {
        return new CatalogParser();
    }

    private CatalogParser() {
    }

    public CatalogConfig parse(Element catalogElement) {
        return parseCatalog(catalogElement);
    }

    public CatalogConfig parse(final String resourcePath, final InputStream inputStream) {
        CatalogConfig catalogConfig = null;
        InputStream xmlInputStream = null;
        InputStream xsdInputStream = null;
        try {
            xmlInputStream = inputStream;
            xsdInputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_SCHEMA);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf.setNamespaceAware(true);
            dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            dbf.setAttribute(JAXP_SCHEMA_SOURCE, xsdInputStream);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException exception) {
                    LOG.info("Validation warning validating config file \"" + resourcePath +
                            "\" against XML Schema \"" + CONFIG_SCHEMA);
                }

                public void error(SAXParseException exception) {
                        throw new RuntimeException("Validation errors occurred parsing the config file \"" +
                            resourcePath + "\".  Cause: " + exception, exception);
                }

                public void fatalError(SAXParseException exception) {
                    throw new RuntimeException("Validation errors occurred parsing the config file \"" +
                        resourcePath + "\".  Cause: " + exception, exception);
                }
            });

            db.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId) {
                    if(systemId.endsWith("/catalog-config.xsd")) {
                        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_SCHEMA);
                        return new InputSource(inputStream);
                    }
                    else return null;
                }
            });

            Document document = db.parse(xmlInputStream);
            Element catalogElement = document.getDocumentElement();
            catalogConfig = parse(catalogElement);

        }
        catch(ParserConfigurationException e) {
            throw new RuntimeException("Error occurred parsing the config file \"" + resourcePath + "\"", e);
        }
        catch(IOException e) {
            throw new RuntimeException("Error occurred parsing the config file \"" + resourcePath + "\"", e);
        }
        catch(SAXException e) {
            throw new RuntimeException("Error occurred parsing the config file \"" + resourcePath + "\"", e);
        }
        finally {
            try{if(xsdInputStream != null) xsdInputStream.close();}
            catch(IOException e) {}
        }

        return catalogConfig;
    }

    private static CatalogConfig parseCatalog(Element catalogElement) {
        CatalogConfig catalogConfig = null;
        if(catalogElement != null && catalogElement.hasChildNodes()) {
            catalogConfig = new CatalogConfig();
            NodeList nodeList = catalogElement.getChildNodes();
            if(nodeList != null) {
                for(int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if(!(node.getNodeType() == Node.ELEMENT_NODE))
                        continue;

                    if(node.getNodeName().equals("chain")) {
                        Element element = (Element)node;
                        String name = element.getAttribute("name");
                        ChainConfig chainConfig = new ChainConfig();
                        chainConfig.setName(name);

                        NodeList commandList = element.getElementsByTagName("command");
                        if(commandList != null) {
                            for(int j = 0; j < commandList.getLength(); j++) {
                                Element commandElement = (Element)commandList.item(j);
                                CommandConfig commandConfig = parseCommand(commandElement);
                                chainConfig.addCommand(commandConfig);
                            }
                        }
                        catalogConfig.addCommand(chainConfig);
                    }
                    else if(node.getNodeName().equals("command")) {
                        Element element = (Element)node;
                        CommandConfig commandConfig = parseCommand(element);
                        catalogConfig.addCommand(commandConfig);
                    }
                }
            }
        }

        return catalogConfig;
    }

    private static CommandConfig parseCommand(Element element) {
        assert element != null;
        assert element.getNodeName().equals("command");

        CommandConfig commandConfig = new CommandConfig();
        String id = DomUtils.getChildElementText(element, "id");
        String classname = DomUtils.getChildElementText(element, "command-class");
        commandConfig.setId(id);
        commandConfig.setClassname(classname);

        NodeList propertyList = element.getElementsByTagName("custom-property");
        if(propertyList != null) {
            for(int k = 0; k < propertyList.getLength(); k++) {
                Element propertyElement = (Element)propertyList.item(k);
                String propName = DomUtils.getChildElementText(propertyElement, "name");
                String propValue = DomUtils.getChildElementText(propertyElement, "value");
                CustomPropertyConfig propertyConfig = new CustomPropertyConfig(propName, propValue);
                commandConfig.addParameter(propertyConfig);
            }
        }

        return commandConfig;
    }
}
