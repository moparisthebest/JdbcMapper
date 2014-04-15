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
package org.apache.beehive.netui.tools.testrecorder.client;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.io.InputStream;
import java.io.IOException;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.Constants;
import org.apache.beehive.netui.tools.testrecorder.shared.xmlbeans.XMLHelper;
import org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinitions;
import org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinition;
import org.apache.beehive.netui.tools.testrecorder.shared.config.RuntimeConfigException;
import org.apache.beehive.netui.tools.testrecorder.shared.config.WebappConfig;
import org.apache.beehive.netui.tools.testrecorder.shared.config.ServerDefinition;
import org.apache.beehive.netui.tools.testrecorder.shared.config.WebappDefinition;
import org.apache.beehive.netui.tools.testrecorder.shared.config.ConfigException;
import org.apache.beehive.netui.tools.testrecorder.shared.config.Config;
import org.apache.beehive.netui.tools.testrecorder.shared.config.Webapps;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpRecoverableException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 */
public class MasterTestRecorderJUnitTest
    extends TestCase {

    private static final Logger LOGGER = Logger.getInstance( MasterTestRecorderJUnitTest.class );
    // specified when invoking the test to describe the tests to run
    public static final String TESTS_PROPERTY = "test.recorder.run.tests";
    public static final String CATEGORIES_PROPERTY = "test.recorder.run.categories";
    public static final String WEBAPPS_PROPERTY = "test.recorder.run.webapps";
    public static final String DELETE_RESULTS_PROPERTY = "test.recorder.run.results.delete";
    private static final NameValuePair configParam =
        new NameValuePair( Constants.FILE, Constants.FILE_TYPE_CONFIG );
    private static final NameValuePair webappParam =
        new NameValuePair( Constants.FILE, Constants.FILE_TYPE_WEBAPP );
    private static final NameValuePair testsParam =
        new NameValuePair( Constants.FILE, Constants.FILE_TYPE_TESTS );

    private static ServerDefinition serverDef;
    private static NameValuePair[] queryParams = new NameValuePair[3];

    private static HttpClient CONTROL_CLIENT = new HttpClient();

    static {
        queryParams[0] = new NameValuePair( Constants.MODE, "xml" );
        queryParams[1] = new NameValuePair( Constants.CMD, "xml" );
    }

    public MasterTestRecorderJUnitTest() {
        super( "MasterTestRecorderJUnitTest" );
    }

    public static Test suite() {
        TestSuite suite = null;
        try {
            initialize();
            suite = new TestSuite();
            buildSuite( suite );
        }
        catch ( Throwable e ) {
            String msg = "Failed building test recorder junit suite, exception( " + e.getMessage() + " )";
            LOGGER.error( msg, e );
            if ( e instanceof RuntimeException ) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException( msg, e );
        }
        return suite;
    }

    private static void buildSuite( TestSuite suite ) {
        boolean delete = getDeleteResultsProperty();
        HashMap webappMap = new HashMap();
        TestDefinitions testDefs = null;
        TestDefinitions[] testDefList = serverDef.getTestDefinitions();
        boolean includeWebappInName = ( testDefList.length > 1 ) ? true : false;
        for ( int i = 0; i < testDefList.length; i++ ) {
            testDefs = (TestDefinitions) testDefList[i];
            List list = getTestList( getTestsProperty(), testDefs );
            addList( list, suite, webappMap, includeWebappInName );
            list = getTestsByCategory( getCategoriesProperty(), testDefs );
            addList( list, suite, webappMap, includeWebappInName );
        }
        if ( LOGGER.isInfoEnabled() ) {
            LOGGER.info( "Test recorder test suite consists of (" + suite.countTestCases() + ") tests" );
        }
        if ( suite.countTestCases() == 0 ) {
            String msg = "ERROR: no tests specified, check for warning of skipped webapps, tests and categories";
            System.err.println( msg );
            LOGGER.fatal( msg );
            throw new RuntimeConfigException( msg );
        }
        // Delete results file for all webapps used, create results directory if it doesn't exist
        // TODO move to server to allow for playback from remote server
        Iterator it = webappMap.values().iterator();
        WebappConfig webapp = null;
        while ( it.hasNext() ) {
            webapp = (WebappConfig) it.next();
            // delete all results only if requested
            if ( delete ) {
                LOGGER.info( "Deleting results for webapp( " + webapp.getName() + " )" );
                if ( !webapp.deleteResults() ) {
                    String msg = "WARNING: unable to delete all results files for webapp( " + webapp.getName() +
                            " )";
                    System.out.println( msg );
                    LOGGER.warn( msg );
                }
            }
            if ( !webapp.createResultsDirectory() ) {
                String msg = "ERROR: unable to create results directory( " + webapp.getResultsDirectory() +
                        " ), for webapp( " + webapp.getName() + " )";
                System.err.println( msg );
                LOGGER.error( msg );
                throw new RuntimeConfigException( msg );
            }
        }
    }

    private static void addList( List list, TestSuite suite, HashMap webappMap, boolean includeWebappInName ) {
        TestDefinition def = null;
        for ( int i = 0; i < list.size(); i++ ) {
            def = (TestDefinition) list.get( i );
            LOGGER.debug( "Adding test( " + def.getName() + " )to JUnit suite" );
            if ( includeWebappInName ) {
                suite.addTest( new TestRecorderJUnitTest( def, def.getWebapp().getName() + "-" + def.getName() ) );
            }
            else {
                suite.addTest( new TestRecorderJUnitTest( def ) );
            }
            addWebapp( webappMap, def.getWebapp() );
        }
    }

    private static void initialize()
        throws ConfigException, IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(Constants.SERVER_FILE);

        try {
            serverDef = XMLHelper.getServerDefinition(is, Constants.SERVER_FILE);
        }
        catch ( Throwable e ) {
            String msg = "Failed to obtain test recorder server definition from XML";
            LOGGER.error( msg, e );
            throw new ConfigException( msg, e );
        }
        finally {
            if ( is != null ) {
                try {
                    is.close();
                }
                catch ( IOException e ) {
                    LOGGER.error( "Failed closing stream of server definition XML", e );
                    throw e;
                }
            }
        }
        if ( serverDef == null ) {
            String msg = "Failed to obtain test recorder server definition from XML";
            LOGGER.error( msg );
            throw new ConfigException( msg );
        }
        // the schema insures that at least one webapp is defined
        assert serverDef.getWebappCount() != 0 : "no webapps defined in server definition XML file" ;
        String webappList = getWebappsProperty();
        if ( webappList == null ) {
            String msg = "ERROR: the '" + WEBAPPS_PROPERTY + "' property must be set.";
            LOGGER.error( msg );
            throw new ConfigException( msg );
        }
        webappList = webappList.trim();
        WebappDefinition webapp = null;
        TestDefinitions tests = null;
        if ( webappList.equalsIgnoreCase( "all" ) ) {
            WebappDefinition[] webappDefList = serverDef.getWebapps();
            for ( int i = 0; i < webappDefList.length; i++ ) {
                webapp = webappDefList[i];
                tests = getTestDefinitions( webapp );
                if ( tests == null ) {
                    continue;
                }
                serverDef.addTestDefinitions( webapp, tests );
            }
        }
        else {
            String webappName = null;
            for ( StringTokenizer stringTokenizer = new StringTokenizer( webappList, "," );
                    stringTokenizer.hasMoreTokens(); ) {
                webappName = stringTokenizer.nextToken().trim();
                if ( webappName.length() == 0 ) {
                    continue;
                }
                webapp = serverDef.getWebapp( webappName );
                if ( webapp == null ) {
                    String msg = "WARNING: no webapp found with name( " + webappName + " )";
                    if ( LOGGER.isErrorEnabled() ) {
                        LOGGER.error( msg );
                    }
                    System.err.println( msg );
                    continue;
                }
                if ( LOGGER.isDebugEnabled() ) {
                    LOGGER.debug( "retrieving test definition for webapp at( " + webapp.getContextRoot() + " )" );
                }
                tests = getTestDefinitions( webapp );
                if ( tests == null ) {
                    continue;
                }
                serverDef.addTestDefinitions( webapp, tests );
            }
        }
        if ( serverDef.getTestDefinitionsCount() == 0 ) {
            String msg = "ERROR: no test definitions found";
            LOGGER.error( msg );
            throw new ConfigException( msg );
        }
    }

    private static TestDefinitions getTestDefinitions( WebappDefinition webapp ) {
        TestDefinitions tests = null;
        try {
            tests = retrieveTestDefinitions( serverDef, webapp );
        }
        catch ( ConfigException e ) {
            String msg = "WARNING: failed to obtain test definitions for webapp at( " +
                    webapp.getContextRoot() + " )";
            if ( LOGGER.isErrorEnabled() ) {
                LOGGER.warn( msg, e );
            }
            e.printStackTrace();
            System.err.println( msg );
        }
        return tests;
    }

    private static TestDefinitions retrieveTestDefinitions( ServerDefinition server, WebappDefinition webapp )
            throws ConfigException {
        String uri = genUri( "http", server.getHostname(), server.getPort(), webapp.getServletUri() );
        HttpMethod method = new GetMethod( uri );
        queryParams[2] = configParam;
        method.setQueryString( queryParams );
        InputStream is = executeXMLRequest( method, uri, Constants.CONFIG_FILE );
        Config config = null;
        try {
            config = XMLHelper.getConfig( is, Constants.CONFIG_FILE );
        }
        catch ( Exception e ) {
            String msg = "Encountered exception processing resource( " + Constants.CONFIG_FILE + " )";
            throw new ConfigException(msg, e);
        }
        finally {
            try {if ( is != null ) {is.close();}}catch(IOException ignore) {}
            method.releaseConnection();
        }
        LOGGER.info( "config( " + config + " )" );

        method = new GetMethod( uri );
        queryParams[2] = webappParam;
        method.setQueryString( queryParams );
        is = executeXMLRequest( method, uri, Constants.WEBAPPS_FILE );
        Webapps webapps = null;
        try {
            webapps = XMLHelper.getWebapps( is, Constants.WEBAPPS_FILE, config );
        }
        catch ( Exception e ) {
            String msg = "Encountered exception processing resource( " + Constants.WEBAPPS_FILE + " )";
            throw new ConfigException(msg, e);
        }
        finally {
            try {if ( is != null ) is.close();}catch(IOException io) {}
            method.releaseConnection();
        }
        LOGGER.info( "webapps( " + webapps + " )" );

        method = new GetMethod( uri );
        queryParams[2] = testsParam;
        method.setQueryString( queryParams );
        is = executeXMLRequest( method, uri, Constants.TESTS_FILE );
        TestDefinitions testDefinitions = null;
        try {
            testDefinitions = XMLHelper.getTestDefinitionsInstance(is,
                                                                   Constants.TESTS_FILE,
                                                                   webapps,
                                                                   config.getBaseDirectory().getAbsolutePath());
        }
        catch ( Exception e ) {
            String msg = "Encountered exception processing resource( " + Constants.TESTS_FILE + " )";
            throw new ConfigException(msg, e);
        }
        finally {
            try {if (is != null) is.close();} catch(IOException ignore) {}

            method.releaseConnection();
        }

        return testDefinitions;
    }

    private static InputStream executeXMLRequest( HttpMethod method, String uri, String resourceIdentifier )
            throws ConfigException {
        InputStream is = null;
        try {
            is = executeHttpRequest( CONTROL_CLIENT, method );
        }
        catch ( Exception e ) {
            String msg = "Failed to obtain '" + resourceIdentifier + "' from webapp at uri( " + uri + " )";
            LOGGER.error( msg, e );
            if(is != null) {try {is.close();}catch(IOException ignore) {}}
            method.releaseConnection();
            throw new ConfigException( msg, e );
        }
        return is;
    }

    private static InputStream executeHttpRequest( HttpClient client, HttpMethod method )
        throws ConfigException, IOException {

        int statusCode = -1;

        // retry up to 3 times.
        for ( int attempt = 0; statusCode == -1 && attempt < 3; attempt++ ) {
            try {
                statusCode = client.executeMethod( method );
            }
            catch ( HttpRecoverableException e ) {
                    String msg = "A recoverable exception occurred calling URI( " + method.getURI() + " ), retrying. exception( " + e.getMessage() + " )";
                    LOGGER.error( msg, e );
            }
            catch ( IOException e ) {
                String msg = "Failed executing request( " + method.getURI() + " ), exception( " + e.getMessage() + " )";
                LOGGER.error( msg, e );
                throw e;
            }
        }

        if ( statusCode == -1 ) {
            String msg = "Failed to execute request( " + method.getURI() + " )";
            LOGGER.error( msg );
            throw new ConfigException( msg );
        }
        InputStream is = method.getResponseBodyAsStream();
        return is;
    }

    public static String genUri( String protocol, String host, int port, String path ) {
        return protocol + "://" + host + ":" + port + path;
    }

    /**
     * Returns a List of TestDefinition objects
     */
    private static List getTestList( String testList, TestDefinitions testDefs ) {
        List list = new ArrayList();
        if ( testList == null ) {
            return list;
        }
        String name = null;
        TestDefinition test = null;
        for ( StringTokenizer stringTokenizer = new StringTokenizer( testList, "," );
                stringTokenizer.hasMoreTokens(); ) {
            name = stringTokenizer.nextToken().trim();
            if ( name.length() == 0 ) {
                continue;
            }
            test = testDefs.getTest( name );
            if ( test == null ) {
                String msg = "WARNING: unable to find test with name( " + name +" ) in webapp( " +
                testDefs.getWebapps().toString() + " ), skippping";
                if ( LOGGER.isWarnEnabled() ) {
                    LOGGER.warn( msg );
                }
                System.err.println( msg );
                continue;
            }
            list.add( test );
        }
        return list;
    }

    private static List getTestsByCategory( String categoryList, TestDefinitions testDefs ) {
        String category = null;
        List list = new ArrayList();
        if ( categoryList == null ) {
            return list;
        }
        List temp = null;
        for ( StringTokenizer stringTokenizer = new StringTokenizer( categoryList, "," );
                stringTokenizer.hasMoreTokens(); ) {
            category = stringTokenizer.nextToken().trim();
            if ( category.length() == 0 ) {
                continue;
            }
            // implicit category
            if ( category.equalsIgnoreCase( "all" ) ) {
                temp = testDefs.getTestDefinitions();
            }
            else {
                temp = testDefs.getCategories().getTests( category );
            }
            if ( temp == null ) {
                String msg = "WARNING: unable to find category( " + category + " ) in webapp( " +
                testDefs.getWebapps().toString() + " ), skippping";
                if ( LOGGER.isWarnEnabled() ) {
                    LOGGER.warn( msg );
                }
                System.err.println( msg );
                continue;
            }
            list.addAll( temp );
        }
        return list;
    }

    private static boolean getDeleteResultsProperty() {
        return Boolean.valueOf( getProperty( DELETE_RESULTS_PROPERTY ) ).booleanValue();
    }

    private static String getTestsProperty() {
        return getProperty( TESTS_PROPERTY );
    }

    private static String getCategoriesProperty() {
        return getProperty( CATEGORIES_PROPERTY );
    }

    private static String getWebappsProperty() {
        return getProperty( WEBAPPS_PROPERTY );
    }

    private static String getProperty( String prop ) {
        String value = null;
        try {
            value = System.getProperty( prop );
        }
        catch ( Exception ex ) {
            LOGGER.fatal( "unable to obtain system property( " + prop + " ), ex( " + ex.getMessage() + " )", ex );
            throw new RuntimeException( "unable to obtain system property( " + prop + " ), exception( " +
                    ex.toString() + " )" );
        }
        assert value != null : "system property( " + prop + " ) has value( " + value + " )";
        return value;
    }

    private static void addWebapp( Map map, WebappConfig webapp ) {
        if ( !map.containsKey( webapp.getName() ) ) {
            map.put( webapp.getName(), webapp );
        }
    }
}
