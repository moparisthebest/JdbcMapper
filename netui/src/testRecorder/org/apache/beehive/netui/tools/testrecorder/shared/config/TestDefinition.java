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
import org.apache.beehive.netui.tools.testrecorder.shared.Constants;
import org.apache.beehive.netui.tools.testrecorder.shared.Logger;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.IOException;

/**
 * User: ozzy
 * Date: Apr 9, 2004
 * Time: 2:36:17 PM
 */
public class TestDefinition implements Comparable {

    private static final Logger log = Logger.getInstance( TestDefinition.class );

    private String name;
    private String description;
    private WebappConfig webapp;
    private List categories;

    public TestDefinition( String name, String description, WebappConfig webapp, List categories ) {
        this.name = name;
        assert name != null : "ERROR: test name may not be null";
        if ( description == null ) {
            this.description = "";
        }
        else {
            this.description = description;
        }
        assert webapp != null : "ERROR: webapp may not be null, test name( " + name + " )";
        this.webapp = webapp;
        this.categories = categories;
        if ( this.categories == null ) {
            this.categories = new ArrayList();
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public WebappConfig getWebapp() {
        return webapp;
    }

    private List getCategoriesInternal() {
        return categories;
    }

    public List getCategories() {
        return Collections.unmodifiableList( categories );
    }

    public void addCategory( Category category ) {
        getCategoriesInternal().add( category );
    }

    public String getTestFilePath() {
        return getWebapp().getTestDirectory() + "/" + getName() + Constants.XML;
    }

    public String getResultFilePath() {
        return getWebapp().getResultsDirectory() + "/" + getName() + Constants.XML;
    }

    public String getResultDiffFilePath() {
        return getWebapp().getResultsDirectory() + "/" + getName() + Constants.DIFF + Constants.XML;
    }

    public void createRecordFile() throws IOException {
        createRecordFile( false );
    }

    public void createRecordFile( boolean overwrite ) throws IOException {
        if ( ! getWebapp().createTestDirectory()) {
            String msg = "ERROR: unable to create test directory( " + getWebapp().getTestDirectory() + " )";
            log.error( msg );
            throw new IOException( msg );
        }
        File file = new File( getTestFilePath() );
        if ( overwrite ) {
            if ( file.exists() && ! file.delete() ) {
                String msg = "ERROR: unable to delete existing test file( " + file  + " )";
                log.error( msg );
                throw new IOException( msg );
            }
        }
        else {
            if ( file.exists() ) {
                String msg = "ERROR: test file exists( " + file + " )";
                log.error( msg );
                throw new IOException( msg );
            }
        }
        if ( ! file.createNewFile() ) {
            String msg = "ERROR: unable to create new test file( " + file + " )";
            log.error( msg );
            throw new IOException( msg );
        }
    }

    public void createPlaybackFile( ) throws IOException {
        if ( !getWebapp().createResultsDirectory() ) {
            String msg = "ERROR: unable to create results directory( " + getWebapp().getResultsDirectory() + " )";
            log.error( msg );
            throw new IOException( msg );
        }
        File file = new File( getResultFilePath() );
        if ( file.exists() && !file.delete() ) {
            String msg = "ERROR: unable to delete existing playback file( " + file + " )";
            log.error( msg );
            throw new IOException( msg );
        }
        if ( !file.createNewFile() ) {
            String msg = "ERROR: unable to create new test playback file( " + file + " )";
            log.error( msg );
            throw new IOException( msg );
        }
    }

    public void createPlaybackDiffFile() throws IOException {
        if ( !getWebapp().createResultsDirectory() ) {
            String msg = "ERROR: unable to create results directory( " + getWebapp().getResultsDirectory() + " )";
            log.error( msg );
            throw new IOException( msg );
        }
        File file = new File( getResultDiffFilePath() );
        if ( file.exists() && !file.delete() ) {
            String msg = "ERROR: unable to delete existing playback diff file( " + file + " )";
            log.error( msg );
            throw new IOException( msg );
        }
        if ( !file.createNewFile() ) {
            String msg = "ERROR: unable to create new playback diff file( " + file + " )";
            log.error( msg );
            throw new IOException( msg );
        }
    }

    public int compareTo( Object o ) {
        if ( o instanceof TestDefinition ) {
            TestDefinition other = (TestDefinition)o;
            return getName().compareToIgnoreCase( other.getName() );
        }
        return 1;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 64 );
        sb.append( "[ " );
        sb.append( "name( " + name + " )" );
        sb.append( ", description( " + description + " )" );
        sb.append( ", webapp( " + getWebapp().getName() + " )" );
        sb.append( ",\n\t categories( " + StringHelper.toString( categories.toArray(), "\n", "\n\t" ) + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

    public static String testListToString( List list ) {
        StringBuffer sb = new StringBuffer( 16 * list.size() );
        TestDefinition test = null;
        for ( int i = 0; i < list.size(); i++ ) {
            test = (TestDefinition) list.get( i );
            if ( i != 0 ) {
                sb.append( ", " );
            }
            sb.append( test.getName() );
        }
        return sb.toString();
    }

}
