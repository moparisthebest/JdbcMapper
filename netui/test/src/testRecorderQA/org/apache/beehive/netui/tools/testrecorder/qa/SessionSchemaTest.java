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
import java.util.List;

import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.SessionXMLException;
import org.apache.beehive.netui.tools.testrecorder.shared.RecordSessionBean;
import org.apache.beehive.netui.tools.testrecorder.shared.Constants;
import org.apache.beehive.netui.tools.testrecorder.shared.config.Config;
import org.apache.beehive.netui.tools.testrecorder.shared.config.ConfigException;
import org.apache.beehive.netui.tools.testrecorder.shared.config.Webapps;
import org.apache.beehive.netui.tools.testrecorder.shared.config.WebappConfig;
import org.apache.beehive.netui.tools.testrecorder.shared.xmlbeans.XMLHelper;

/**
 * User: ozzy
 * Date: Apr 8, 2004
 * Time: 3:44:42 PM
 */
public class SessionSchemaTest extends TRTestCase {

    private static final Logger log = Logger.getInstance( SessionSchemaTest.class );

    public SessionSchemaTest( String name ) {
        super( name );
    }

    public static Test suite() {
        log.info( "" );
        log.info( "" );
        log.info( SessionSchemaTest.class.getName() );
        return new TestSuite( SessionSchemaTest.class );
    }

    public void testSession1() {
        log.info( "\ntestSession1()" );
        File file = new File( getDataDir(), "session1.xml" );
        getRecordSessionBean( file );
    }

    public void testExisting() {
        log.info( "testExisting" );
        Config config = null;
        try {
            config = XMLHelper.getConfig( new File( getDataDir(), Constants.CONFIG_FILE ) );
        }
        catch ( ConfigException e ) {
            assertTrue( e );
        }
        catch ( IOException e ) {
            assertTrue( e );
        }
        Webapps webapps = null;
        try {
            webapps = XMLHelper.getWebapps( new File( getDataDir(), Constants.WEBAPPS_FILE ), config );
        }
        catch ( Exception e ) {
            assertTrue( e );
        }

        List webappList = webapps.getWebappList();
        for ( int i = 0; i < webappList.size(); i++ ) {
            WebappConfig webappConfig = (WebappConfig) webappList.get( i );
            File[] testFiles = new File( webappConfig.getTestDirectory() ).listFiles();
            for ( int j = 0; j < testFiles.length; j++ ) {
                File testFile = testFiles[j];
                if ( testFile.getName().endsWith( ".xml" ) ) {
                    log.info( "processing file( " + testFile.getAbsolutePath() + " )" );
                    getRecordSessionBean( testFile );
                }
                else {
                    log.warn( "skipping file( " + testFile.getAbsolutePath() + " )" );
                }
            }
        }
    }

    public RecordSessionBean getRecordSessionBean( File file ) {
        RecordSessionBean session = null;
        try {
            session = XMLHelper.getRecordSessionBean( file );
        }
        catch ( SessionXMLException e ) {
            e.printStackTrace();
            assertTrue( e );
        }
        catch ( IOException e ) {
            e.printStackTrace();
            assertTrue( e );
        }
        catch ( Exception e ) {
            e.printStackTrace();
            assertTrue( e );
        }
        log.info( "record session bean( " + session + " )" );
        return session;
    }

    public static void main( String[] args ) throws Exception {
        log.info( "\n\nTestRecorder Session Schema Test" );
    }

}
