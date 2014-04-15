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

import junit.framework.TestCase;

import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;

import org.apache.beehive.netui.tools.testrecorder.shared.Logger;

/**
 * User: ozzy
 * Date: Jun 16, 2004
 * Time: 11:16:26 AM
 */
public class TRTestCase extends TestCase {

    private static final Logger log = Logger.getInstance( TRTestCase.class );

    private File dataDir;
    private File appDir;
    private File webuiHomeDir;

    public TRTestCase( String name ) {
        super( name );
    }

    protected void setUp() {
        setDataDir( determineDataDir() );
        setAppDir( determineAppDir() );
        setWebuiHomeDir( determineWebuiHomeDir() );
    }

    protected void tearDown() {
    }

    protected File determineDataDir() {
        String val = System.getProperty( "data.dir" );
        assertNotNull( "'data.dir' property must be specified", val );
        return new File( val );
    }

    protected File determineAppDir() {
        String val = System.getProperty( "app.dir" );
        assertNotNull( "'app.dir' property must be specified", val );
        return new File( val );
    }

    protected File determineWebuiHomeDir() {
        String val = System.getProperty( "webui.home" );
        assertNotNull( "'webui.home' property must be specified", val );
        return new File( val );
    }

    public File getDataDir() {
        return dataDir;
    }

    public void setDataDir( File dataDir ) {
        this.dataDir = dataDir;
    }

    public File getAppDir() {
        return appDir;
    }

    public void setAppDir( File appDir ) {
        this.appDir = appDir;
    }

    public File getWebuiHomeDir() {
        return webuiHomeDir;
    }

    public void setWebuiHomeDir( File webuiHomeDir ) {
        this.webuiHomeDir = webuiHomeDir;
    }

    public void assertTrue( Throwable e ) {
        log.info( getStackTrace( e ) );
        assertTrue( e.getMessage(), false );
    }

    public void assertTrue( Throwable e, boolean condition ) {
        log.info( getStackTrace( e ) );
        assertTrue( e.getMessage(), condition );
    }

    public static String getStackTrace( Throwable e ) {
        StringWriter sw = new StringWriter( 256 );
        PrintWriter pw = new PrintWriter( sw );
        e.printStackTrace( pw );
        pw.flush();
        pw.close();
        return sw.toString();
    }

}
