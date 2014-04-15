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

import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.Constants;
import org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinition;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 */
public class TestRecorderJUnitTest extends TestCase {

    private static final Logger log = Logger.getInstance( TestRecorderJUnitTest.class );
    private TestDefinition test;

    public TestRecorderJUnitTest( TestDefinition test ) {
        this( test, test.getName() );
        this.test = test;
    }

    public TestRecorderJUnitTest( TestDefinition test, String name ) {
        super( name );
        this.test = test;
    }

    protected void runTest() throws Throwable {
        boolean outcome = execute();
        if ( !outcome ) {
            String msg = "\nPlayback FAILED for test( " + getTest().getName() + " )\n";
            if ( log.isWarnEnabled() ) {
                log.warn( msg );
            }
            System.out.println( msg );
            Assert.fail( msg );
        }
    }

    private boolean execute() {
        boolean outcome = false;
        String testUser = System.getProperty( "user.name" );
        PlaybackExecutor exec = new PlaybackExecutor( getTest(), testUser, testUser );
        try {
            outcome = exec.run();
            String msg = ( outcome == true ) ? Constants.PASS : Constants.FAIL ;
            System.out.println( "Finished: '" + getTest().getName() + "': " + msg );
            if ( log.isWarnEnabled() ) {
                log.warn( "Finished: '" + getTest().getName() + "': " + msg );
            }
        }
        catch ( Throwable ex ) {
            outcome = false;
            String msg = "ERROR: failed executing playback, exception( " + ex.getMessage() + " )";
            System.out.println( Logger.format( msg, ex ) );
            log.error( msg, ex );
        }
        return outcome;
    }

    public TestDefinition getTest() {
        return test;
    }
}
