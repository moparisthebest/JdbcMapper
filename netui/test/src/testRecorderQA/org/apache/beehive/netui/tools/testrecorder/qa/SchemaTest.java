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

package org.apache.beehive.netui.tools.testrecorder.qa;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.io.IOException;

import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.Constants;
import org.apache.beehive.netui.tools.testrecorder.shared.xmlbeans.XMLHelper;
import org.apache.beehive.netui.tools.testrecorder.shared.config.Config;
import org.apache.beehive.netui.tools.testrecorder.shared.config.Webapps;
import org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinitions;
import org.apache.beehive.netui.tools.testrecorder.shared.config.ConfigException;
import org.apache.beehive.netui.tools.testrecorder.shared.config.WebappConfig;
import org.apache.beehive.netui.tools.testrecorder.shared.config.Category;
import org.apache.beehive.netui.tools.testrecorder.shared.config.ServerDefinition;

/**
 * User: ozzy
 * Date: Apr 8, 2004
 * Time: 3:44:42 PM
 */
public class SchemaTest extends TRTestCase {

    private static final Logger log = Logger.getInstance( SchemaTest.class );

    public SchemaTest( String name ) {
        super( name );
    }

    public static Test suite() {
        log.info( "" );
        log.info( "" );
        log.info( SchemaTest.class.getName() );
        return new TestSuite( SchemaTest.class );
    }

    public void testServerDef() {
        log.info( "\ntestServerDef()" );
        File file = new File( getDataDir(), Constants.SERVER_FILE );
        log.info( "file( " + file + " )" );
        ServerDefinition server = null;
        try {
            server = XMLHelper.getServerDefinition( file );
        }
        catch ( ConfigException e ) {
            assertTrue( e );
        }
        catch ( IOException ioe ) {
            assertTrue( ioe );
        }
        log.debug( "server( " + server + " )" );
    }

    public void testTestDefinitions() {
        log.info( "\ntestTestDefinitions()" );
        File file = new File( getDataDir(), "config1.xml" );
        Config config = getConfig( file );

        file = new File( getDataDir(), "webapp1.xml" );

        Webapps webapps = getWebapps( file, config );
        log.debug( "webapps( " + webapps + " )" );

        file = new File( getDataDir(), "test1.xml" );
        log.info( "file( " + file.getAbsolutePath() + " )" );
        TestDefinitions tests = null;
        try {
            tests = XMLHelper.getTestDefinitionsInstance( file, webapps,
                            config.getBaseDirectory().getAbsolutePath() );
        }
        catch ( ConfigException e ) {
            assertTrue( e );
        }
        catch ( IOException ioe ) {
            assertTrue( ioe );
        }
        log.debug( "tests( " + tests + " )" );
    }

    public void testCategories() {
        log.info( "\ntestCategories()" );
        File file = new File( getDataDir(), "config1.xml" );
        Config config = getConfig( file );

        file = new File( getDataDir(), "webapp1.xml" );

        Webapps webapps = getWebapps( file, config );
        log.debug( "webapps( " + webapps + " )" );

        file = new File( getDataDir(), "test1.xml" );
        log.info( "file( " + file.getAbsolutePath() + " )" );
        TestDefinitions tests = null;
        try {
            tests = XMLHelper.getTestDefinitionsInstance( file, webapps,
                            config.getBaseDirectory().getAbsolutePath() );
        }
        catch ( ConfigException e ) {
            assertTrue( e );
        }
        catch ( IOException ioe ) {
            assertTrue( ioe );
        }
        Category category = null;
        for( int i = 0; i < tests.getCategories().getCategories().length; i++ ) {
            category = tests.getCategories().getCategories()[i];
            log.debug( "\n" );
            log.debug( "category( " + category.getName() + " )" );
            log.debug( "reportDir( " + category.getReportDirPath() + " )" );
        }
    }

    public void testWebappConfig() {
        log.info( "\ntestWebappConfig()" );
        File file = new File( getDataDir(), "config1.xml" );
        Config config = getConfig( file );

        file = new File( getDataDir(), "webapp1.xml" );

        Webapps webapps = getWebapps( file, config );
        log.debug( "webapps( " + webapps + " )" );

        assertNotNull( "webapps returned null", webapps );
        WebappConfig webapp = null;
        for ( int i = 0; i < webapps.getWebappList().size(); i++ ) {
            webapp = (WebappConfig) webapps.getWebappList().get( i );
            log.debug( "\n" );
            log.debug( "webapp(" + ( i + 1 ) + ")( " + webapp.getName() + " )" );
            log.debug( "config baseDirectory( " + config.getBaseDirectory() + " )" );
            log.debug( "webapp config baseDirectory( " + webapp.getConfig().getBaseDirectory() + " )" );
            log.debug( "resultsDirectory( " + webapp.getResultsDirectory() + " )" );
        }
    }

    public void testInvalidTestDefinitions() {
        log.info( "\ntestInvalidTestDefinitions()" );
        File file = new File( getDataDir(), "config1.xml" );
        Config config = getConfig( file );

        file = new File( getDataDir(), "webapp1.xml" );
        Webapps webapps = getWebapps( file, config );
        log.debug( "webapps( " + webapps + " )" );

        file = new File( getDataDir(), "test2.xml" );
        log.info( "file( " + file.getAbsolutePath() + " )" );
        try {
            XMLHelper.getTestDefinitionsInstance( file, webapps, config.getBaseDirectory().getAbsolutePath() );
        }
        catch ( ConfigException e ) {
            log.info( getStackTrace( e ) );
            // success
            return;
        }
        catch ( IOException ioe ) {
            assertTrue( ioe );
        }
        assertTrue( false );
    }

    public void testConfigXML() {
        log.info( "\ntestConfigXML()" );
        File file = new File( getDataDir(), "config1.xml" );
        log.info( "file( " + file.getAbsolutePath() + " )" );
        Config config = null;
        try {
            config = XMLHelper.getConfig( file );
        }
        catch ( ConfigException e ) {
            assertTrue( e );
        }
        catch ( IOException e ) {
            assertTrue( e );
        }
        log.debug( "config( " + config + " )" );
    }

    public void testConfigBaseDir() {
        log.info( "\ntestConfigBaseDir()" );
        File file = new File( getDataDir(), "config1.xml" );
        log.info( "file( " + file.getAbsolutePath() + " )" );
        Config config = null;
        try {
            config = XMLHelper.getConfig( file );
        }
        catch ( ConfigException e ) {
            assertTrue( e );
        }
        catch ( IOException e ) {
            assertTrue( e );
        }
        if ( config.getBaseDirectory() == null ) {
            assertNull( "server config base directory is null", config.getBaseDirectory() );
        }
        log.debug( "base directory( " + config.getBaseDirectory() + " )" );
        log.debug( "config( " + config + " )" );
    }

    public void testInvalidConfigXML() {
        log.info( "\ntestInvalidConfigXML()" );
        File file = new File( getDataDir(), "config2.xml" );
        invalidConfigTest( file );
        file = new File( getDataDir(), "config3.xml" );
        invalidConfigTest( file );
    }

    private Webapps getWebapps( File file, Config config ) {
        log.info( "file( " + file.getAbsolutePath() + " )" );
        Webapps webapps = null;
        try {
            webapps = XMLHelper.getWebapps( file, config );
        }
        catch ( ConfigException e ) {
            assertTrue( e );
        }
        catch ( IOException e ) {
            assertTrue( e );
        }
        return webapps;
    }

    private Config getConfig( File configFile ) {
        log.info( "configFile( " + configFile.getAbsolutePath() + " )" );
        Config config = null;
        try {
            config = XMLHelper.getConfig( configFile );
        }
        catch ( Exception e ) {
            assertTrue( e );
        }
        return config;
    }

    private void invalidConfigTest( File file ) {
        log.info( "file( " + file.getAbsolutePath() + " )" );
        try {
            XMLHelper.getConfig( file );
        }
        catch ( ConfigException ce ) {
            // success
            return;
        }
        catch ( IOException e ) {
            assertTrue( e );
        }
    }

    public static void main( String[] args ) throws Exception {
        log.info( "\n\nTestRecorder Schema Test" );
    }
}
