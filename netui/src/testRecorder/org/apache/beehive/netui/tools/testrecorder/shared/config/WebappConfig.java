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

package org.apache.beehive.netui.tools.testrecorder.shared.config;

import org.apache.beehive.netui.tools.testrecorder.shared.util.StringHelper;
import org.apache.beehive.netui.tools.testrecorder.shared.Logger;

import java.io.File;

/**
 * User: ozzy
 * Date: Apr 12, 2004
 * Time: 5:35:51 PM
 */
public class WebappConfig {

    private static final Logger log = Logger.getInstance( WebappConfig.class );

    private String name;
    private String description;
    private boolean testMode;
    private ServerConfig server;
    // includes leading slash
    private String contextRoot;
    private File testDirectory;
    private File resultsDirectory;
    private Config config;

    public WebappConfig( String name, String description, ServerConfig server, boolean testMode,
            String contextRoot, String testDirectory, Config config ) {
        this.name = name;
        this.description = description;
        this.testMode = testMode;
        this.server = server;
        this.contextRoot = contextRoot;
        this.testDirectory = new File( testDirectory );
        this.resultsDirectory = new File( config.getBaseDirectory(), name + "-playback" );
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ServerConfig getServer() {
        return server;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public String getServletURI() {
        return getContextRoot() + "/" + getConfig().getServletURI();
    }

    public String getTestDirectory() {
        return testDirectory.getAbsolutePath();
    }

    /**
     * @return true if the directory exists or was created successfully.
     */
    public boolean createTestDirectory() {
        if ( testDirectory.exists() ) {
            return true;
        }
        return testDirectory.mkdirs();
    }

    public String getResultsDirectory() {
        return resultsDirectory.getAbsolutePath();
    }

    /**
     * returns true only if all files could be deleted from the results directory, false otherwise
     *
     * @return
     */
    public boolean deleteResults() {
        return deleteResults( false );
    }

    /**
     * return value is true only if all files could be deleted from the results directory,
     * false otherwise.
     *
     * @return
     */
    public boolean deleteResults( boolean removeDir ) {
        boolean rtnVal = true;
        if ( resultsDirectory.exists() ) {
            File[] files = resultsDirectory.listFiles();
            File file = null;
            for ( int i = 0; i < files.length; i++ ) {
                file = files[i];
                if ( !file.delete() ) {
                    if ( log.isWarnEnabled() ) {
                        log.warn( "unable to delete results file( " + file.getAbsolutePath() + " )" );
                    }
                    rtnVal = false;
                }
            }
            if ( rtnVal && removeDir ) {
                if ( !resultsDirectory.delete() ) {
                    if ( log.isWarnEnabled() ) {
                        log.warn( "unable to delete( " + resultsDirectory.getAbsolutePath() + " )" );
                    }
                    rtnVal = false;
                }
            }
        }
        return rtnVal;
    }

    /**
     * @return true if the results directory exists immediately prior to returning.  therefore,
     *         if the directory already exists this method will return true.
     */
    public boolean createResultsDirectory() {
        boolean rtnVal = false;
        resultsDirectory.mkdirs();
        if ( resultsDirectory.exists() ) {
            rtnVal = true;
        }
        return rtnVal;
    }

    public boolean handleSuffix( String suffix ) {
        return getConfig().handleSuffix( suffix );
    }

    public Config getConfig() {
        return config;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 256 );
        sb.append( "[ " );
        sb.append( "name( " + getName() + " )" );
        sb.append( ", description( " + getDescription() + " )" );
        sb.append( ", testMode( " + isTestMode() + " )" );
        sb.append( ", server( " + getServer() + " )" );
        sb.append( ", contextRoot( " + getContextRoot() + " )" );
        sb.append( ", servletURI( " + getServletURI() + " )" );
        sb.append( ", testDir( " + getTestDirectory() + " )" );
        sb.append( ", resultsDir( " + getResultsDirectory() + " )" );
        sb.append( ", suffixList( " + StringHelper.toString( getConfig().getSuffixes().iterator(), "\n", "\n\t" ) + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
