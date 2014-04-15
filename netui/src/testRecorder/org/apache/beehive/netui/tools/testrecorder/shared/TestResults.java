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
package org.apache.beehive.netui.tools.testrecorder.shared;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class TestResults {

    private String uri;
    private int testNumber;
    // if true indicates an error has occured playing back the test
    private boolean error;
    // if no error has occured and the the test passed
    private boolean testPassed = true;
    // List of entrues
    private List diffResults;

    public TestResults( int testNumber, final String uri ) {
        this( testNumber, uri, false, true );
    }

    public TestResults( int testNumber, String uri, boolean error, boolean testPassed ) {
        this.uri = uri;
        this.testNumber = testNumber;
        this.error = error;
        this.testPassed = testPassed;
    }

    public String getUri() {
        return uri;
    }

    public int getTestNumber() {
        return testNumber;
    }

    public String getStatus() {
        if ( error ) {
            return Constants.ERROR;
        }
        if ( testPassed ) {
            return Constants.PASS;
        }
        return Constants.FAIL;
    }

    public boolean isError() {
        return error;
    }

    public boolean isTestPassed() {
        if ( isError() ) {
            return false;
        }
        return testPassed;
    }

    public void addDiffResult( String result ) {
        addDiffResult( result, false );
    }

    public void addDiffResult( String result, final boolean error ) {
        if ( error )
            this.error = true;

        testPassed = false;
        if ( diffResults == null )
            diffResults = new ArrayList();
        
        diffResults.add( result );
    }

    public List getDiffResults() {
        if ( diffResults == null )
            return null;
        else return Collections.unmodifiableList( diffResults );
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 16 );
        sb.append( "[ " );
        sb.append( ", status( " + getStatus() + " )" );
        sb.append( ", diffResults( " +
                ( ( diffResults == null ) ? "null" :
                Util.toString( diffResults.iterator(), diffResults.size() ) ) + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
