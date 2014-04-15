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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ResponseData {

    public static final String NON_UNIQUE_SESSION_ID = "";
    public static final String NON_UNIQUE_HOST = "@NON_UNIQUE_HOST@";
    public static final String NON_UNIQUE_PORT = "@NON_UNIQUE_PORT@";

    private static final String COLON = ":";
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";

    private static final DecimalFormat format = (DecimalFormat)NumberFormat.getInstance(Locale.US);

    static {
        // we just want the integer portion
        format.setDecimalSeparatorAlwaysShown( false );
        format.setGroupingSize( 0 );
    }

    private String host;
    private int port;
    private int statusCode;
    private String reason;
    private NVPair[] headers;
    private Map headerMap;
    // non-normalized
    private String body;

    public ResponseData( String host, int port ) {
        this.host = host;
        this.port = port;
    }

    public void setStatusCode( int statusCode ) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setReason( String reason ) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setHeaders( NVPair[] headers ) {
        this.headers = headers;
    }

    public NVPair[] getHeaders() {
        return headers;
    }

    /**
     * returns null if the header does not exist.
     * @param name
     * @return
     */
    public String getHeader( String name ) {
        initializeHeaderMap();
        String value = (String) headerMap.get( name );
        return value;
    }

    private void initializeHeaderMap() {
        if ( headerMap != null ) {
            return;
        }
        headerMap = new HashMap();
        NVPair header = null;
        for ( int i = 0; i < headers.length; i++ ) {
            header = headers[i];
            // duplicates are over written
            headerMap.put( header.getName(), header.getValue() );
        }
    }

    public void setBody( String body ) {
        this.body = body.trim();
    }

    /**
     * Returns the non-normalized response body, that is, the response body contains the original
     * host and port.
     *
     * @return
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns the normalized response body, with the original port and host replaced with tokens.
     *
     * @return
     */
    public String getNormalizedBody() {
        return replaceHostPort( getBody(), host, port );
    }

    // TODO needs to handle using the default port http://<host>/<path>
    // TODO https needs to handle port and default port
    private static String replaceHostPort( String string, String host, int port ) {
        // replace http://<host>:<port> combination with a non-unique string
        Pattern pattern = Pattern.compile( HTTP + host + COLON + format.format( port ) );
        Matcher matcher = pattern.matcher( string );
        string = matcher.replaceAll( HTTP + NON_UNIQUE_HOST + COLON + NON_UNIQUE_PORT );

        // https
        pattern = Pattern.compile( HTTPS + host + COLON );
        matcher = pattern.matcher( string );
        string = matcher.replaceAll( HTTPS + NON_UNIQUE_HOST + COLON );
        return string;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 256 );
        sb.append( "[ " );
        sb.append( "host( " + host + " )" );
        sb.append( ", port( " + port + " )" );
        sb.append( ", statusCode( " + statusCode + " )" );
        sb.append( ", reason( " + reason + " )" );
        sb.append( ", headers( " + headers + " )" );
        sb.append( ", body( " + body + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
