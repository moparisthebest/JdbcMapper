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

import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.beehive.netui.tools.testrecorder.shared.xmlbeans.XMLHelper;
import org.apache.beehive.netui.tools.testrecorder.server.TestRecorderServlet;

public class Reporter {

    public static String genDetails( RecordSessionBean bean ) {
        return formatDetails( bean );
    }

    public static String genDiffDetails( File diffFile )
        throws IOException, SessionXMLException {
        List results = XMLHelper.getDiffResults( diffFile );
        StringBuffer sb = new StringBuffer( results.size() * 128 );
        TestResults testResults = null;
        List diffList = null;
        for ( int i = 0; i < results.size(); i++ ) {
            testResults = (TestResults) results.get( i );
            diffList = testResults.getDiffResults();
            String diff = null;
            if ( i > 0 ) {
                sb.append( getLineBreak() );
            }
            sb.append( "Uri( " + testResults.getUri() + " )" + getLineBreak() );
            sb.append( "Test Number( " + testResults.getTestNumber() + " )" + getLineBreak() );
            if ( testResults.isError() ) {
                sb.append( "Test Execution Error Encountered!" );
            }
            if ( diffList.size() == 0 ) {
                sb.append( "*** no diff info present for this test failure ***" );
            }
            else if ( diffList.size() == 1 ) {
                diff = (String) diffList.get( 0 );
                sb.append( formatDiff( diff ) );
            }
            else {
                sb.append( "*** ERROR: unexpected diff results ***" );
            }
        }
        return sb.toString();
    }

    public static String escape( String in ) {
        char[] c = in.toCharArray();
        return escape( c, in.length() );
    }
    public static String escape( char[] c, int size ) {
        StringBuffer sb = new StringBuffer();
        for ( int i = 0; i < size; i++ ) {
            if ( c[i] == '<' ) {
                sb.append( "&lt;" );
                continue;
            }
            sb.append( c[i] );
        }
        return sb.toString();
    }

    private static String formatDetails( RecordSessionBean bean ) {
        StringBuffer sb = new StringBuffer();
        // the header information
        sb.append( "<table>" );
        sb.append( "<tr><td><b>Test</b></td><td>" );
        sb.append( bean.getSessionName() );
        sb.append( "</td></tr>" );
        sb.append( "<tr><td><b>Description</b></td><td>" );
        sb.append( bean.getDescription() );
        sb.append( "</td></tr>" );
        sb.append( "<tr><td><b>Tester</b></td><td>" );
        sb.append( bean.getTester() );
        sb.append( "</td></tr>" );
        sb.append( "</table>" );

        int cnt = bean.getTestCount();
        for ( int i = 0; i < cnt; i++ ) {
            formatRequestDetails( bean.getRequestData( i ), sb );
        }
        return sb.toString();
    }

    private static void formatRequestDetails( RequestData request, StringBuffer sb ) {
        sb.append( "<hr /><h4>Request " );
        sb.append( request.getPath() );
        String buttonAction = null;
        String params = formatParameterDetails( request, buttonAction );
        if ( buttonAction != null ) {
            sb.append( "<br />Button Request" );
            sb.append( buttonAction );
        }
        sb.append( "</h4>" );
        sb.append( "<h5>Parameters</h5>" );
        sb.append( params );
    }

    private static String formatParameterDetails( RequestData request, String buttonAction ) {
        StringBuffer sb = new StringBuffer( ( 16 * request.getParamCount() ) + 5 );
        buttonAction = null;
        if ( request.getParamCount() > 0 ) {
            sb.append( "<table border=\"1\" cellspacing=\"0\"><tr><th>Name</th><th>Value</th></tr>" );
            int cnt = request.getParamCount();
            for ( int i = 0; i < cnt; i++ ) {
                if ( request.getParamName( i ).startsWith( "actionOverride:" ) ) {
                    buttonAction = request.getParamName( i );
                }
                sb.append( "<tr><td>" );
                sb.append( request.getParamName( i ) );
                sb.append( "</td><td>" );
                sb.append( request.getParamValue( i ) );
                sb.append( "</td></tr>" );
            }
            sb.append( "</table>" );
        }
        else {
            sb.append( "None" );
        }
        return sb.toString();
    }

    public static String formatDiff( String diff ) {
        StringBuffer sb = new StringBuffer( diff.length() );
        StringTokenizer st = new StringTokenizer( diff, "\n\r\f" );
        String line = null;
        while ( st.hasMoreTokens() ) {
            line = st.nextToken();
            if ( line.startsWith( "R:" ) ) {
                if ( line.length() == 2 ) {
                    line = getRecordMarker() + getLineBreak();
                }
                else {
                    line = new String( getRecordMarker() + escape( line.substring( 2 ) ) + getLineBreak() );
                }
            }
            else if ( line.startsWith( "P:" ) ) {
                if ( line.length() == 2 ) {
                    line = getPlaybackMarker() + getLineBreak();
                }
                else {
                    line = new String( getPlaybackMarker() + escape( line.substring( 2 ) ) + getLineBreak() );
                }
            }
            else {
                line = escape( line ) + getLineBreak();
            }
            sb.append( line );
        }
        return sb.toString();
    }

    private static String getLineBreak() {
        return "\n";
    }

    private static String getPlaybackMarker() {
        return "<b>P:</b>";
    }

    private static String getRecordMarker() {
        return "<b>R:</b>";
    }
}
