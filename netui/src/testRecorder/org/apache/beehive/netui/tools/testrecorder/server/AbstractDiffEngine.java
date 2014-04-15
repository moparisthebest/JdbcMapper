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

import org.apache.beehive.netui.tools.testrecorder.shared.RequestData;
import org.apache.beehive.netui.tools.testrecorder.shared.DiffFailedException;
import org.apache.beehive.netui.tools.testrecorder.shared.NVPair;
import org.apache.beehive.netui.tools.testrecorder.shared.ResponseData;
import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.TestResults;

/**
 * User: ozzy
 * Date: Jul 9, 2004
 * Time: 10:10:18 AM
 */
public abstract class AbstractDiffEngine implements DiffEngine {

    private static final Logger log = Logger.getInstance( AbstractDiffEngine.class );

    public TestResults diff( RequestData recordRequest, ResponseData recordResponse, RequestData playbackRequest,
            ResponseData playbackResponse, TestResults results ) throws DiffFailedException {
        results = diff( recordRequest, playbackRequest, results );
        results = diff( recordResponse, playbackResponse, results );
        return results;
    }

    public abstract TestResults responseBodyDiff( String record, String playback, TestResults results )
            throws DiffFailedException;

    public TestResults diff( RequestData record, RequestData playback,
            TestResults results ) throws DiffFailedException {
        if ( !record.getProtocol().equals( playback.getProtocol() ) ) {
            results.addDiffResult( "record request protocol( " + record.getProtocol() +
                    " ) does not match playback request protocol( " +
                    playback.getProtocol() + " )" );
        }
        
        // Note that we do NOT compare the recorded request path with the playback request path, because the Servlet
        // spec doesn't say that they must be equal.  Specifically, this can break when the container itself interprets
        // a welcome-page request.
        
        if ( !record.getMethod().equals( playback.getMethod() ) ) {
            results.addDiffResult( "record request method( " + record.getMethod() +
                    " ) does not match playback request method( " +
                    playback.getMethod() + " )" );
        }
        results = diffParams( record.getParameters(), playback.getParameters(), results );
        return results;
    }

    public TestResults diffParams( NVPair[] record, NVPair[] playback,
            TestResults results ) throws DiffFailedException {
        // assumption: params are sorted.
        NVPair recPair = null;
        NVPair playPair = null;
        int i = 0;
        boolean match = false;
        if ( record != null ) {
            for ( i = 0; i < record.length; i++, match = false ) {
                recPair = record[i];
                if ( playback != null && i < playback.length ) {
                    playPair = playback[i];
                    if ( recPair.equals( playPair ) ) {
                        match = true;
                    }
                }
                else {
                    // match stays false
                    playPair = null;
                }
                if ( !match ) {
                    results.addDiffResult( "record request parameter( " + recPair +
                            " ) does not match playback request parameter( " +
                            playPair + " )" );
                }
            }
        }
        if ( playback != null ) {
            // see if more playback params exist
            recPair = null;
            for ( ; i < playback.length; i++ ) {
                playPair = playback[i];
                results.addDiffResult( "record request parameter( " + recPair +
                        " ) does not match playback request parameter( " +
                        playPair + " )" );
            }
        }
        return results;
    }

    public TestResults diff( ResponseData record, ResponseData playback, TestResults results )
            throws DiffFailedException {
        if ( record.getStatusCode() != playback.getStatusCode() ) {
            results.addDiffResult( "record response status code( " + record.getStatusCode() +
                    " ) does not match playback response status code( " +
                    playback.getStatusCode() + " )" );
        }
        results = responseBodyDiff( record.getNormalizedBody(), playback.getNormalizedBody(), results );
        return results;
    }

}
