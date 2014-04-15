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
package org.apache.beehive.netui.tools.testrecorder.shared.xmlbeans;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.Writer;
import java.io.FileWriter;
import java.text.ParseException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.servlet.http.Cookie;

import org.apache.beehive.netui.tools.testrecorder.shared.util.DomUtils;
import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.Constants;
import org.apache.beehive.netui.tools.testrecorder.shared.RecordSessionBean;
import org.apache.beehive.netui.tools.testrecorder.shared.SessionXMLException;
import org.apache.beehive.netui.tools.testrecorder.shared.RequestData;
import org.apache.beehive.netui.tools.testrecorder.shared.ResponseData;
import org.apache.beehive.netui.tools.testrecorder.shared.TestResults;
import org.apache.beehive.netui.tools.testrecorder.shared.PlaybackSessionBean;
import org.apache.beehive.netui.tools.testrecorder.shared.NVPair;
import org.apache.beehive.netui.tools.testrecorder.shared.config.ServerDefinition;
import org.apache.beehive.netui.tools.testrecorder.shared.config.WebappDefinition;
import org.apache.beehive.netui.tools.testrecorder.shared.config.ConfigException;
import org.apache.beehive.netui.tools.testrecorder.shared.config.RuntimeConfigException;
import org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinitions;
import org.apache.beehive.netui.tools.testrecorder.shared.config.Config;
import org.apache.beehive.netui.tools.testrecorder.shared.config.Webapps;
import org.apache.beehive.netui.tools.testrecorder.shared.config.ServerConfig;
import org.apache.beehive.netui.tools.testrecorder.shared.config.WebappConfig;
import org.apache.beehive.netui.tools.testrecorder.shared.config.Categories;
import org.apache.beehive.netui.tools.testrecorder.shared.config.Category;
import org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinition;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.EntityResolver;

/**
 */
public final class XMLHelper {

    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private static final Logger LOGGER = Logger.getInstance(XMLHelper.class);

    public static ServerDefinition getServerDefinition(final InputStream xmlInputStream, final String xmlResourcePath) {

        Document document = null;
        final String xsdResourcePath = Constants.SCHEMA_LOCATION + "/" + Constants.SERVER_SCHEMA_NAME;
        InputStream xsdInputStream = XMLHelper.class.getClassLoader().getResourceAsStream(xsdResourcePath);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf.setNamespaceAware(true);
            dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            dbf.setAttribute(JAXP_SCHEMA_SOURCE, xsdInputStream);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException exception) {
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("Validation warning validating config file \"" + xmlResourcePath +
                            "\" against XML Schema \"" + xsdResourcePath);
                }

                public void error(SAXParseException exception) {
                    throw new IllegalStateException("Validation errors occurred parsing the config file \"" +
                        xmlResourcePath + "\".  Cause: " + exception, exception);
                }

                public void fatalError(SAXParseException exception) {
                    throw new IllegalStateException("Validation errors occurred parsing the config file \"" +
                        xmlResourcePath + "\".  Cause: " + exception, exception);
                }
            });

            db.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId) {
                    if (systemId.endsWith(Constants.SERVER_SCHEMA_NAME)) {
                        InputStream inputStream = XMLHelper.class.getClassLoader().getResourceAsStream(xsdResourcePath);
                        return new InputSource(inputStream);
                    }
                    else
                        return null;
                }
            });

            document = db.parse(xmlInputStream);
        }
        catch (Exception e) {
            assert e instanceof ParserConfigurationException;
            throw new IllegalStateException("Exception occurred parsing document.  Cause: " + e, e);
        }

        String name = DomUtils.getChildElementText(document.getDocumentElement(), "name");
        String hostname = DomUtils.getChildElementText(document.getDocumentElement(), "hostname");
        String portString = DomUtils.getChildElementText(document.getDocumentElement(), "port");
        int port = Integer.parseInt(portString);

        ServerDefinition server = new ServerDefinition(name, hostname, port);

        Element elem = DomUtils.getChildElementByName(document.getDocumentElement(), "webapps");
        NodeList webappList = elem.getElementsByTagName("webapp");
        for (int i = 0; i < webappList.getLength(); i++) {
            Element webappElem = (Element) webappList.item(i);
            WebappDefinition webapp = new WebappDefinition(
                DomUtils.getChildElementText(webappElem, "name"),
                DomUtils.getChildElementText(webappElem, "description"),
                DomUtils.getChildElementText(webappElem, "contextRoot"),
                DomUtils.getChildElementText(webappElem, "servletURI")
            );
            server.addWebapp(webapp);
        }

        return server;
    }

    public static List getDiffResults(File file)
        throws IOException {

        if (LOGGER.isInfoEnabled())
            LOGGER.info("file( " + file.getAbsolutePath() + " )");

        final String xmlResourcePath = file.getAbsolutePath();
        final InputStream xmlInputStream = new FileInputStream(file);

        Document document = null;
        final String xsdResourcePath = Constants.SCHEMA_LOCATION + "/" + Constants.DIFF_SESSION_SCHEMA_NAME;
        InputStream xsdInputStream = XMLHelper.class.getClassLoader().getResourceAsStream(xsdResourcePath);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf.setNamespaceAware(true);
            dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            dbf.setAttribute(JAXP_SCHEMA_SOURCE, xsdInputStream);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException exception) {
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("Validation warning validating config file \"" + xmlResourcePath +
                            "\" against XML Schema \"" + xsdResourcePath);
                }

                public void error(SAXParseException exception) {
                    throw new IllegalStateException("Validation errors occurred parsing the config file \"" +
                        xmlResourcePath + "\".  Cause: " + exception, exception);
                }

                public void fatalError(SAXParseException exception) {
                    throw new IllegalStateException("Validation errors occurred parsing the config file \"" +
                        xmlResourcePath + "\".  Cause: " + exception, exception);
                }
            });

            db.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId) {
                    if (systemId.endsWith(Constants.DIFF_SESSION_SCHEMA_NAME)) {
                        InputStream inputStream = XMLHelper.class.getClassLoader().getResourceAsStream(xsdResourcePath);
                        return new InputSource(inputStream);
                    }
                    else
                        return null;
                }
            });

            document = db.parse(xmlInputStream);
        }
        catch (Exception e) {
            throw new IllegalStateException("Exception occurred parsing document.  Cause: " + e, e);
        }

        LinkedList requestList = new LinkedList();
        Element rootElement = document.getDocumentElement();

        NodeList requestNodeList = rootElement.getElementsByTagName("request");
        for (int i = 0; i < requestNodeList.getLength(); i++) {
            Element requestElement = (Element) requestNodeList.item(i);
            String testNumberStr = DomUtils.getChildElementText(requestElement, "testNumber");
            int testNumber = Integer.parseInt(testNumberStr);
            String uri = DomUtils.getChildElementText(requestElement, "uri");
            String diffResults = DomUtils.getChildElementText(requestElement, "diffResults");

            TestResults results = new TestResults(testNumber, uri, false, false);
            results.addDiffResult(diffResults);
            requestList.add(results);
        }

        return requestList;
    }

    public static void createDiffFile(File diffFile, PlaybackSessionBean bean) {
        /*
        note, I'd love to use the javax.xml.* infrastructure to write out the XML document here, but
        due to configuration differences between Tomcat 5.0 and Java 5.0, this isn't possible without
        taking additional dependencies on JARs like Xalan...

        <sigh/>

        So, we're just going to print XML by hand.
        */

        StringBuffer buf = new StringBuffer();
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        buf.append("<recorderDiff xmlns=\"http://beehive.apache.org/netui/tools/testrecorder/2004/diffsession\">\n");
        for (int i = 0; i < bean.getTestCount(); i++) {
            TestResults results = bean.getTestResults(i);
            if (results.isTestPassed())
                continue;

            /* otherwise, the test failed, start building the XML */
            RequestData request = bean.getRequestData(i);

            buf.append("<request>\n");
            buf.append("<testNumber>\n");
            buf.append(results.getTestNumber());
            buf.append("\n");
            buf.append("</testNumber>\n");

            buf.append("<uri>\n");
            buf.append(results.getUri());
            buf.append("\n");
            buf.append("</uri>\n");

            List list = results.getDiffResults();
            StringBuffer sb = new StringBuffer(64 * list.size());
            sb.append("\n");
            for (int j = 0; j < list.size(); j++)
                sb.append(list.get(j)).append("\n");

            String diffString = sb.toString();

            buf.append("<diffResults>\n");
            buf.append("<![CDATA[");
            buf.append(diffString);
            buf.append("]]>\n");
            buf.append("</diffResults>\n");

            buf.append("</request>\n");
        }

        buf.append("</recorderDiff>\n");

        write(diffFile, buf.toString());
    }

    public static RecordSessionBean getRecordSessionBean(File file)
        throws IOException {

        if (LOGGER.isInfoEnabled())
            LOGGER.info("file( " + file.getAbsolutePath() + " )");

        final String xmlResourcePath = file.getAbsolutePath();
        final InputStream xmlInputStream = new FileInputStream(file);

        Document document = null;
        final String xsdResourcePath = Constants.SCHEMA_LOCATION + "/" + Constants.SESSION_SCHEMA_NAME;
        InputStream xsdInputStream = XMLHelper.class.getClassLoader().getResourceAsStream(xsdResourcePath);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf.setNamespaceAware(true);
            dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            dbf.setAttribute(JAXP_SCHEMA_SOURCE, xsdInputStream);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException exception) {
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("Validation warning validating config file \"" + xmlResourcePath +
                            "\" against XML Schema \"" + xsdResourcePath);
                }

                public void error(SAXParseException exception) {
                    throw new IllegalStateException("Validation errors occurred parsing the config file \"" +
                        xmlResourcePath + "\".  Cause: " + exception, exception);
                }

                public void fatalError(SAXParseException exception) {
		    exception.printStackTrace();
                    throw new IllegalStateException("Validation errors occurred parsing the config file \"" +
                        xmlResourcePath + "\".  Cause: " + exception, exception);
                }
            });

            db.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId) {
                    if (systemId.endsWith(Constants.SESSION_SCHEMA_NAME)) {
                        InputStream inputStream = XMLHelper.class.getClassLoader().getResourceAsStream(xsdResourcePath);
                        return new InputSource(inputStream);
                    }
                    else
                        return null;
                }
            });

            document = db.parse(xmlInputStream);
        }
        catch (Exception e) {
	    e.printStackTrace();
            throw new IllegalStateException("Exception occurred parsing document.  Cause: " + e, e);
        }

        Element root = document.getDocumentElement();
        String namespaceURI = root.getNamespaceURI();

        String sessionName = DomUtils.getChildElementText(root, namespaceURI, "sessionName");
        String testOwner = DomUtils.getChildElementText(root, namespaceURI, "tester");
        String description = DomUtils.getChildElementText(root, namespaceURI, "description");
        String startDate = DomUtils.getChildElementText(root, namespaceURI, "startDate");
        String endDate = DomUtils.getChildElementText(root, namespaceURI, "endDate");

        RecordSessionBean bean = new RecordSessionBean(sessionName);
        bean.setTester(testOwner);
        bean.setDescription(description);

        try {
            if (startDate != null)
                bean.setStartDate(startDate);
        }
        catch (ParseException pe) {
            throw new SessionXMLException("Failed to parse start date \"" + startDate + "\".  Cause: " + pe, pe);
        }

        try {
            if (endDate != null)
                bean.setEndDate(endDate);
        }
        catch (ParseException pe) {
            throw new SessionXMLException("Failed to parse end date \"" + endDate + "\".  Cause: " + pe, pe);
        }

        Element testsElement = DomUtils.getChildElementByName(root, namespaceURI, "tests");
        if (testsElement != null) {
            NodeList testsList =
                namespaceURI != null ? testsElement.getElementsByTagNameNS(namespaceURI, "test") : testsElement.getElementsByTagName("test");
            for (int i = 0; i < testsList.getLength(); i++) {
                Element test = (Element) testsList.item(i);

                //String testNumberStr = DomUtils.getChildElementText(test,  namespaceURI, "testNumber");
                // int testNumber = Integer.parseInt(testNumberStr);

                Element requestElement = DomUtils.getChildElementByName(test, namespaceURI, "request");
                String protocol = DomUtils.getChildElementText(requestElement, namespaceURI, "protocol");
                String protocolVersion = DomUtils.getChildElementText(requestElement, namespaceURI, "protocolVersion");
                String host = DomUtils.getChildElementText(requestElement, namespaceURI, "host");
                String portStr = DomUtils.getChildElementText(requestElement, namespaceURI, "port");
                int port = Integer.parseInt(portStr);
                String uri = DomUtils.getChildElementText(requestElement, namespaceURI, "uri");
                String method = DomUtils.getChildElementText(requestElement, namespaceURI, "method");

                RequestData request = new RequestData();
                request.setProtocol(protocol);
                request.setProtocolVersion(protocolVersion);
                request.setHost(host);
                request.setPort(port);
                request.setPath(uri);
                request.setMethod(method);

                /* parameters */
                Element element = DomUtils.getChildElementByName(test, namespaceURI, "parameters");
                if (element != null) {
                    NodeList parameterList =
                        namespaceURI != null ? element.getElementsByTagNameNS(namespaceURI, "parameter") :
                            element.getElementsByTagName("parameter");
                    NVPair[] pairs = getNVPairs(namespaceURI, parameterList);
                    request.setParameters(pairs);
                }

                /* cookies */
                element = DomUtils.getChildElementByName(test, namespaceURI, "cookies");
                if (element != null) {
                    NodeList parameterList =
                        namespaceURI != null ? element.getElementsByTagNameNS(namespaceURI, "cookie") :
                            element.getElementsByTagName("cookie");

                    Cookie[] cookies = new Cookie[parameterList.getLength()];
                    for (int j = 0; j < cookies.length; j++) {
                        Element cookie = (Element)parameterList.item(j);

                        String name = DomUtils.getChildElementText(cookie, namespaceURI, "name");
                        String value = DomUtils.getChildElementText(cookie, namespaceURI, "value");

                        cookies[j] = new Cookie(name, value);
                    }
                    request.setCookies(cookies);
                }

                /* headers */
                element = DomUtils.getChildElementByName(test, namespaceURI, "headers");
                if (element != null) {
                    NodeList parameterList =
                        namespaceURI != null ? element.getElementsByTagNameNS(namespaceURI, "header") :
                            element.getElementsByTagName("header");
                    NVPair[] pairs = getNVPairs(namespaceURI, parameterList);
                    request.setHeaders(pairs);
                }

                Element responseElement = DomUtils.getChildElementByName(test, namespaceURI, "response");

                String statusCodeStr = DomUtils.getChildElementText(responseElement, namespaceURI, "statusCode");
                int statusCode = Integer.parseInt(statusCodeStr);
                String reason = DomUtils.getChildElementText(responseElement, namespaceURI, "reason");
                String responseBody = DomUtils.getChildElementText(responseElement, namespaceURI, "responseBody");

                ResponseData response = new ResponseData(host, port);
                response.setStatusCode(statusCode);
                response.setReason(reason);
                response.setBody(responseBody);

                bean.addRequestResponseData(request, response);
            }
        }

        return bean;
    }

    public static void createRecordFile(File recordFile, RecordSessionBean bean) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            buf.append("<recorderSession xmlns=\"http://beehive.apache.org/netui/tools/testrecorder/2004/session\">\n");

            writeElement(buf, "sessionName", bean.getSessionName());
            writeElement(buf, "tester", bean.getTester());
            writeElement(buf, "startDate", bean.getStartDateString());
            writeElement(buf, "description", bean.getDescription());

            /* tests */
            buf.append("<tests>\n");
            for (int i = 0; i < bean.getTestCount(); i++) {

                buf.append("<test>\n");
                writeElement(buf, "testNumber", new Integer(i + 1));

                RequestData requestData = bean.getRequestData(i);
                buf.append("<request>\n");
                writeElement(buf, "protocol", requestData.getProtocol());
                writeElement(buf, "protocolVersion", requestData.getProtocolVersion());
                writeElement(buf, "host", requestData.getHost());
                writeElement(buf, "port", new Integer(requestData.getPort()));
                writeElement(buf, "uri", requestData.getPath());
                writeElement(buf, "method", requestData.getMethod());

                /* parameters */
                NVPair[] pairs = requestData.getParameters();
                buf.append("<parameters>\n");
                if (pairs != null) {
                    for (int j = 0; j < pairs.length; j++) {
                        buf.append("<parameter>\n");
                        writeElement(buf, "name", pairs[j].getName());
                        writeElement(buf, "value", pairs[j].getValue());
                        buf.append("</parameter>\n");
                    }
                }
                buf.append("</parameters>\n");

                /* cookies */
                Cookie[] cookies = requestData.getCookies();
                if (cookies != null) {
                    buf.append("<cookies>\n");
                    for (int j = 0; j < cookies.length; j++) {
                        buf.append("<cookie>\n");
                        writeElement(buf, "name", cookies[j].getName());
                        writeElement(buf, "value", cookies[j].getValue());
                        buf.append("</cookie>\n");
                    }
                    buf.append("</cookies>\n");
                }

                /* headers */
                pairs = requestData.getHeaders();
                if (pairs != null) {
                    buf.append("<headers>\n");
                    for (int j = 0; j < pairs.length; j++) {
                        buf.append("<header>\n");
                        writeElement(buf, "name", pairs[j].getName());
                        writeElement(buf, "value", pairs[j].getValue());
                        buf.append("</header>\n");
                    }
                    buf.append("</headers>\n");
                }

                buf.append("</request>\n");

                ResponseData responseData = bean.getResponseData(i);
                buf.append("<response>\n");
                writeElement(buf, "statusCode", new Integer(responseData.getStatusCode()));
                writeElement(buf, "reason", responseData.getReason());
                buf.append("<responseBody>\n");
                buf.append("<![CDATA[");
                buf.append(responseData.getBody());
                buf.append("]]>\n");
                buf.append("</responseBody>\n");
                buf.append("</response>\n");

                buf.append("</test>\n");
            }
            buf.append("</tests>\n");

            writeElement(buf, "endDate", bean.getEndDateString());
            writeElement(buf, "testCount", new Integer(bean.getTestCount()));

            buf.append("</recorderSession>\n");

            write(recordFile, buf.toString());
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void createPlaybackFile(File playbackFile, PlaybackSessionBean bean) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            buf.append("<recorderSession xmlns=\"http://beehive.apache.org/netui/tools/testrecorder/2004/session\">\n");

            writeElement(buf, "sessionName", bean.getSessionName());
            writeElement(buf, "tester", bean.getTester());
            writeElement(buf, "startDate", bean.getStartDateString());
            writeElement(buf, "description", bean.getDescription());

            buf.append("<tests>\n");
            /* tests */
            for (int i = 0; i < bean.getTestCount(); i++) {

                buf.append("<test>\n");
                writeElement(buf, "testNumber", new Integer(i + 1));

                RequestData requestData = bean.getRequestData(i);
                buf.append("<request>\n");
                writeElement(buf, "protocol", requestData.getProtocol());
                writeElement(buf, "protocolVersion", requestData.getProtocolVersion());
                writeElement(buf, "host", requestData.getHost());
                writeElement(buf, "port", new Integer(requestData.getPort()));
                writeElement(buf, "uri", requestData.getPath());
                writeElement(buf, "method", requestData.getMethod());

                /* parameters */
                NVPair[] pairs = requestData.getParameters();
                buf.append("<parameters>\n");
                if (pairs != null) {
                    for (int j = 0; j < pairs.length; j++) {
                        buf.append("<parameter>\n");
                        writeElement(buf, "name", pairs[j].getName());
                        writeElement(buf, "value", pairs[j].getValue());
                        buf.append("</parameter>\n");
                    }
                }
                buf.append("</parameters>\n");

                /* cookies */
                Cookie[] cookies = requestData.getCookies();
                if (cookies != null) {
                    buf.append("<cookies>\n");
                    for (int j = 0; j < cookies.length; j++) {
                        buf.append("<cookie>\n");
                        writeElement(buf, "name", cookies[j].getName());
                        writeElement(buf, "value", cookies[j].getValue());
                        buf.append("</cookie>\n");
                    }
                    buf.append("</cookies>\n");
                }

                /* headers */
                pairs = requestData.getHeaders();
                if (pairs != null) {
                    buf.append("<headers>\n");
                    for (int j = 0; j < pairs.length; j++) {
                        buf.append("<header>\n");
                        writeElement(buf, "name", pairs[j].getName());
                        writeElement(buf, "value", pairs[j].getValue());
                        buf.append("</header>\n");
                    }
                    buf.append("</headers>\n");
                }

                buf.append("</request>\n");

                ResponseData responseData = bean.getResponseData(i);
                buf.append("<response>\n");
                writeElement(buf, "statusCode", new Integer(responseData.getStatusCode()));
                writeElement(buf, "reason", responseData.getReason());
                buf.append("<responseBody>\n");
                buf.append("<![CDATA[");
                buf.append(responseData.getBody());
                buf.append("]]>\n");
                buf.append("</responseBody>\n");
                buf.append("</response>\n");

		buf.append("<testResults>");
                writeElement(buf, "testStatus", bean.getTestResults(i).getStatus().toLowerCase());

		buf.append("</testResults>");
                buf.append("</test>\n");
            }
            buf.append("</tests>\n");

            writeElement(buf, "endDate", bean.getEndDateString());
            writeElement(buf, "sessionStatus", bean.getStatus().toLowerCase());
            writeElement(buf, "testCount", new Integer(bean.getTestCount()));
            writeElement(buf, "passedCount", new Integer(bean.getPassedCount()));
            writeElement(buf, "failedCount", new Integer(bean.getFailedCount()));

            buf.append("</recorderSession>\n");
            write(playbackFile, buf.toString());
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static TestDefinitions getTestDefinitionsInstance(ClassLoader loader) {
        InputStream is = loader.getResourceAsStream(Constants.CONFIG_FILE);
        if (is == null)
            throw new RuntimeConfigException("ERROR: unable to obtain the resource stream for resource(" + Constants.CONFIG_FILE + ")");

        Config config = null;
        try {
            config = getConfig(is, Constants.CONFIG_FILE);
        }
        finally {
            try {
                is.close();
            }
            catch (IOException ignore) {
            }
        }

        LOGGER.info("config( " + config + " )");

        is = loader.getResourceAsStream(Constants.WEBAPPS_FILE);
        if (is == null)
            throw new RuntimeConfigException("ERROR: unable to obtain the resource stream for resource( " + Constants.WEBAPPS_FILE + " )");

        Webapps webapps = null;
        try {
            webapps = getWebapps(is, Constants.WEBAPPS_FILE, config);
        }
        finally {
            try {if (is != null) is.close();} catch (IOException ignore) {}
        }

        if (LOGGER.isInfoEnabled())
            LOGGER.info("webapps( " + webapps + " )");

        is = loader.getResourceAsStream(Constants.TESTS_FILE);
        if (is == null)
            throw new RuntimeConfigException("Unable to obtain the resource stream for resource(" + Constants.TESTS_FILE + ")");

        TestDefinitions testDefinitions = null;
        try {
            testDefinitions = getTestDefinitionsInstance(is,
                                                         Constants.TESTS_FILE,
                                                         webapps,
                                                         config.getBaseDirectory().getAbsolutePath());
        }
        catch (ConfigException e) {
            throw new RuntimeConfigException(e.getMessage(), e);
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
            }
        }
        return testDefinitions;
    }

    public static Config getConfig(final InputStream xmlInputStream, final String xmlResourcePath) {

        Document document = null;
        final String xsdResourcePath = Constants.SCHEMA_LOCATION + "/" + Constants.CONFIG_SCHEMA_NAME;
        InputStream xsdInputStream = XMLHelper.class.getClassLoader().getResourceAsStream(xsdResourcePath);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf.setNamespaceAware(true);
            dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            dbf.setAttribute(JAXP_SCHEMA_SOURCE, xsdInputStream);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException exception) {
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("Validation warning validating config file \"" + xmlResourcePath +
                            "\" against XML Schema \"" + xsdResourcePath);
                }

                public void error(SAXParseException exception) {
                    throw new IllegalStateException("Validation errors occurred parsing the config file \"" +
                        xmlResourcePath + "\".  Cause: " + exception, exception);
                }

                public void fatalError(SAXParseException exception) {
                    throw new IllegalStateException("Validation errors occurred parsing the config file \"" +
                        xmlResourcePath + "\".  Cause: " + exception, exception);
                }
            });

            db.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId) {
                    if (systemId.endsWith(Constants.CONFIG_SCHEMA_NAME)) {
                        InputStream inputStream = XMLHelper.class.getClassLoader().getResourceAsStream(xsdResourcePath);
                        return new InputSource(inputStream);
                    }
                    else
                        return null;
                }
            });

            document = db.parse(xmlInputStream);
        }
        catch (Exception e) {
            assert e instanceof ParserConfigurationException;
            throw new IllegalStateException("Exception occurred parsing document.  Cause: " + e, e);
        }

        String servletUri = DomUtils.getChildElementText(document.getDocumentElement(), "servletURI");
        String baseDirectory = DomUtils.getChildElementText(document.getDocumentElement(), "baseDirectory");
        Element elem = DomUtils.getChildElementByName(document.getDocumentElement(), "suffixList");

        NodeList suffixList = elem.getElementsByTagName("suffix");
        LinkedList list = new LinkedList();
        for (int i = 0; i < suffixList.getLength(); i++) {
            list.add(DomUtils.getElementText((Element) suffixList.item(i)));
        }

        Config config = new Config(list, servletUri, baseDirectory);
        return config;
    }

    public static Webapps getWebapps(final InputStream xmlInputStream, final String xmlResourcePath, Config config) {

        Document document = null;
        final String xsdResourcePath = Constants.SCHEMA_LOCATION + "/" + Constants.WEBAPPS_SCHEMA_NAME;
        InputStream xsdInputStream = XMLHelper.class.getClassLoader().getResourceAsStream(xsdResourcePath);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf.setNamespaceAware(true);
            dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            dbf.setAttribute(JAXP_SCHEMA_SOURCE, xsdInputStream);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException exception) {
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("Validation warning validating config file \"" + xmlResourcePath +
                            "\" against XML Schema \"" + xsdResourcePath);
                }

                public void error(SAXParseException exception) {
                    throw new IllegalStateException("Validation errors occurred parsing the config file \"" +
                        xmlResourcePath + "\".  Cause: " + exception, exception);
                }

                public void fatalError(SAXParseException exception) {
                    throw new IllegalStateException("Validation errors occurred parsing the config file \"" +
                        xmlResourcePath + "\".  Cause: " + exception, exception);
                }
            });

            db.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId) {
                    if (systemId.endsWith(Constants.SERVER_SCHEMA_NAME)) {
                        InputStream inputStream = XMLHelper.class.getClassLoader().getResourceAsStream(xsdResourcePath);
                        return new InputSource(inputStream);
                    }
                    else
                        return null;
                }
            });

            document = db.parse(xmlInputStream);
        }
        catch (Exception e) {
            assert e instanceof ParserConfigurationException;
            throw new IllegalStateException("Exception occurred parsing document.  Cause: " + e, e);
        }

        Element elem = document.getDocumentElement();
        String name = DomUtils.getChildElementText(document.getDocumentElement(), "name");
        String hostname = DomUtils.getChildElementText(document.getDocumentElement(), "hostname");
        String portString = DomUtils.getChildElementText(document.getDocumentElement(), "port");
        int port = Integer.parseInt(portString);

        ServerConfig server = new ServerConfig(name, hostname, port);

        ArrayList webappList = new ArrayList();
        Config defaultConfig = config;
        elem = DomUtils.getChildElementByName(elem, "webapps");
        NodeList webappElements = elem.getElementsByTagName("webapp");
        for (int i = 0; i < webappElements.getLength(); i++) {
            Element webappElem = (Element) webappElements.item(i);

            Config webappConfig = null;
            Element overrideDefaultConfig = DomUtils.getChildElementByName(webappElem, "overrideDefaultConfig");
            if (overrideDefaultConfig == null)
                webappConfig = defaultConfig;
            else {
                String[] suffixes = null;
                String servletURI = null;
                Element suffixListElem = DomUtils.getChildElementByName(overrideDefaultConfig, "suffixListElem");

                if (suffixListElem != null && suffixListElem.getElementsByTagName("suffix").getLength() > 0) {
                    NodeList suffixList = suffixListElem.getElementsByTagName("suffix");
                    suffixes = new String[suffixList.getLength()];
                    for (int j = 0; j < suffixes.length; j++) {
                        suffixes[j] = DomUtils.getElementText((Element) suffixList.item(i));
                    }
                    servletURI = DomUtils.getChildElementText(overrideDefaultConfig, "servletURI");
                }

                webappConfig = new Config(suffixes, servletURI, defaultConfig.getBaseDirectory().getAbsolutePath());
            }

            String testModeString = DomUtils.getChildElementText(webappElem, "testMode");
            boolean testMode = Boolean.parseBoolean(testModeString);

            WebappConfig webapp = new WebappConfig(
                DomUtils.getChildElementText(webappElem, "name"),
                DomUtils.getChildElementText(webappElem, "description"),
                server,
                testMode,
                DomUtils.getChildElementText(webappElem, "contextRoot"),
                DomUtils.getChildElementText(webappElem, "testDefinitionsDirectory"),
                webappConfig);

            webappList.add(webapp);
        }

        return new Webapps(webappList);
    }

    public static TestDefinitions getTestDefinitionsInstance(final InputStream xmlInputStream,
                                                             final String xmlResourcePath,
                                                             Webapps webapps,
                                                             String baseDirPath)
        throws ConfigException {

        Document document = null;
        final String xsdResourcePath = Constants.SCHEMA_LOCATION + "/" + Constants.TESTS_SCHEMA_NAME;
        InputStream xsdInputStream = XMLHelper.class.getClassLoader().getResourceAsStream(xsdResourcePath);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf.setNamespaceAware(true);
            dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            dbf.setAttribute(JAXP_SCHEMA_SOURCE, xsdInputStream);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException exception) {
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("Validation warning validating config file \"" + xmlResourcePath +
                            "\" against XML Schema \"" + xsdResourcePath);
                }

                public void error(SAXParseException exception) {
                    throw new IllegalStateException("Validation errors occurred parsing the config file \"" +
                        xmlResourcePath + "\".  Cause: " + exception, exception);
                }

                public void fatalError(SAXParseException exception) {
                    throw new IllegalStateException("Validation errors occurred parsing the config file \"" +
                        xmlResourcePath + "\".  Cause: " + exception, exception);
                }
            });

            db.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId) {
                    if (systemId.endsWith(Constants.TESTS_SCHEMA_NAME)) {
                        InputStream inputStream = XMLHelper.class.getClassLoader().getResourceAsStream(xsdResourcePath);
                        return new InputSource(inputStream);
                    }
                    else
                        return null;
                }
            });

            document = db.parse(xmlInputStream);
        }
        catch (Exception e) {
            assert e instanceof ParserConfigurationException;
            throw new IllegalStateException("Exception occurred parsing document.  Cause: " + e, e);
        }

        Element rootElement = document.getDocumentElement();
        Element categoriesElement = DomUtils.getChildElementByName(rootElement, "categories");
        NodeList categoryNodes = categoriesElement.getElementsByTagName("category");

        LinkedList testDefinitions = new LinkedList();
        Category[] categoryArray = new Category[categoryNodes.getLength()];
        for (int i = 0; i < categoryNodes.getLength(); i++) {
            Element category = (Element) categoryNodes.item(i);

            String name = DomUtils.getChildElementText(category, "name");
            String description = DomUtils.getChildElementText(category, "description");

            categoryArray[i] = new Category(name, description, baseDirPath);
        }
        Categories categories = new Categories(categoryArray);

        Element testsElement = DomUtils.getChildElementByName(rootElement, "tests");
        NodeList testNodes = testsElement.getElementsByTagName("test");
        for (int j = 0; j < testNodes.getLength(); j++) {
            Element testElement = (Element) testNodes.item(j);

            String testName = DomUtils.getChildElementText(testElement, "name");
            String testWebapp = DomUtils.getChildElementText(testElement, "webapp");
            String testDescription = DomUtils.getChildElementText(testElement, "description");

            LinkedList testCategories = new LinkedList();
            Element testCategoriesElement = DomUtils.getChildElementByName(testElement, "categories");
            if (testCategoriesElement != null) {
                NodeList categoriesNodes = testCategoriesElement.getElementsByTagName("category");
                for (int i = 0; i < categoriesNodes.getLength(); i++) {
                    Element category = (Element) categoriesNodes.item(i);

                    String categoryName = DomUtils.getElementText(category);
                    Category testCategory = categories.getCategory(categoryName);

                    if (testCategory == null)
                        throw new ConfigException("Invalid test category name \"" + categoryName + "\" for test named \"" + testName + "\"");

                    testCategories.add(testCategory);
                }
            }

            WebappConfig webapp = webapps.getWebapp(testWebapp);
            if (webapp == null)
                throw new ConfigException("Invalid test webapp name \"" + testWebapp + "\" referenced for test \"" + testName + "\"");

            TestDefinition testDef = new TestDefinition(testName, testDescription, webapp, testCategories);
            categories.addTest(testDef);
            testDefinitions.add(testDef);
        }
        return new TestDefinitions(testDefinitions, categories, webapps);
    }

    private static NVPair[] getNVPairs(String namespaceURI, NodeList nodeList) {
        NVPair[] pairs = new NVPair[nodeList.getLength()];

        for (int j = 0; j < nodeList.getLength(); j++) {
            Element parameter = (Element) nodeList.item(j);
            String name = DomUtils.getChildElementText(parameter, namespaceURI, "name");
            String value = DomUtils.getChildElementText(parameter, namespaceURI, "value");
            pairs[j] = new NVPair(name, value);
        }

        return pairs;
    }

    private static void writeElement(StringBuffer buffer, String elementName, Object elementValue) {
        buffer.append("<");
        buffer.append(elementName);
        buffer.append(">");
        buffer.append(elementValue);
        buffer.append("</");
        buffer.append(elementName);
        buffer.append(">\n");
    }

    private static void write(File file, String contents) {
        Writer writer = null;
        try {
            writer = new FileWriter(file);
            writer.append(contents);
            writer.flush();
        }
        catch (Exception e) {
            throw new IllegalStateException("Exception occurred writing XML document \"" + file.getAbsolutePath() + "\"");
        }
        finally {
            try {if (writer != null) writer.close();} catch (Exception ignore) {}
        }
    }
}
