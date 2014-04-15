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

package org.apache.beehive.netui.tools.testrecorder.server;

import org.apache.beehive.netui.tools.testrecorder.shared.DiffFailedException;
import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.TestResults;

import java.io.LineNumberReader;
import java.io.StringReader;

/**
 * User: ozzy
 * Date: Jul 9, 2004
 * Time: 10:07:32 AM
 */
public class LineDiffEngine extends AbstractDiffEngine {

    private static final Logger log = Logger.getInstance( LineDiffEngine.class );

    private static final String REGEX_MARKER = "REGEX:";

    public TestResults responseBodyDiff( String record, String playback, TestResults results )
            throws DiffFailedException {
        try {
            LineNumberReader recReader = new LineNumberReader( new StringReader( record ) );
            LineNumberReader playReader = new LineNumberReader( new StringReader( playback ) );
            String recLine = null;
            String playLine = null;
            boolean match;
            while ( ( recLine = recReader.readLine() ) != null ) {
                playLine = playReader.readLine();
                if ( playLine != null ) {
                    playLine = playLine.trim();
                    recLine = recLine.trim();
                    if ( recLine.startsWith( REGEX_MARKER ) ) {
                        match = playLine.matches( recLine.substring( REGEX_MARKER.length() ).trim() );
                    }
                    else {
                        match = recLine.equals( playLine );
                    }
                }
                else {
                    match = false;
                }

                if ( !match ) {
                    results.addDiffResult( "Line (" + recReader.getLineNumber() +
                            ") does not match. \n" +
                            "R: " + recLine + "\n" +
                            "P: " + playLine + "\n" );
                }
            }
            if ( log.isDebugEnabled() ) {
                log.debug( "**** record reader has no more lines ****" );
            }
            while ( ( playLine = playReader.readLine() ) != null ) {
                match = false;
                results.addDiffResult( "Line (" + playReader.getLineNumber() +
                        ") does not match.\n" +
                        "R: " + recLine + "\n" +
                        "P: " + playLine + "\n" );
            }
        }
        catch ( Exception ex ) {
            String msg = "diff failed, exception( " + ex.getMessage() + " )";
            log.error( msg, ex );
            throw new DiffFailedException( msg, ex );
        }
        return results;
    }

}
