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

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;

/**
 * This class has a dependency on javax.servlet.http
 */
public class RequestData {

    private String protocol;
    private String protocolVersion;
    private String host;
    private int port;
    private String method;
    private String path;
    private NVPair[] headers;
    private NVPair[] parameters;
    private Cookie[] cookies;

    public RequestData() {
    }

    /**
     * returns the original request for this data
     *
     * @return
     */
    public String getUri() {
        return getUri( getHost(), getPort() );
    }

    /**
     * returns the Uri for this data but replaces the host and port with the provided values.
     *
     * @param host
     * @param port
     * @return
     */
    public String getUri( String host, int port ) {
        return genUri( protocol, host, port, getPath() );
    }

    public void setProtocol( String protocol ) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocolVersion( String protocolVersion ) {
        this.protocolVersion = protocolVersion;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setHost( String host ) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setMethod( String method ) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPort( int port ) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setParameters( NVPair[] parameters ) {
        this.parameters = parameters;
    }

    public NVPair[] getParameters() {
        return parameters;
    }

    public int getParamCount() {
        if ( parameters == null ) {
            return 0;
        }
        return parameters.length;
    }

    public String getParamName( int index ) {
        return ( (NVPair) parameters[index] ).getName();
    }

    public String getParamValue( int index ) {
        return ( (NVPair) parameters[index] ).getValue();
    }

    public void setHeaders( NVPair[] headers ) {
        this.headers = headers;
    }

    public NVPair[] getHeaders() {
        return headers;
    }

    public int getHeaderCount() {
        if ( headers == null ) {
            return 0;
        }
        return headers.length;
    }

    public String getHeaderName( int index ) {
        return ( (NVPair) headers[index] ).getName();
    }

    public String getHeaderValue( int index ) {
        return ( (NVPair) headers[index] ).getValue();
    }

    public void setCookies( Cookie[] cookies ) {
        this.cookies = cookies;
    }

    public Cookie[] getCookies() {
        return cookies;
    }

    public int getCookieCount() {
        if ( cookies == null ) {
            return 0;
        }
        return cookies.length;
    }

    public String getCookieName( int index ) {
        return ( (Cookie) cookies[index] ).getName();
    }

    public String getCookieValue( int index ) {
        return ( (Cookie) cookies[index] ).getValue();
    }

    public static String genUri( String protocol, String host, int port, String path ) {
        return protocol + "://" + host + ":" + port + path;
    }

    public static RequestData populate( HttpServletRequest request, RequestData data ) {
        String protocol = request.getProtocol();
        int index = protocol.indexOf( "/" );
        // make sure a slash exists and its not at the end of the string
        if ( index > -1 && index < protocol.length() - 1 ) {
            data.setProtocol( protocol.substring( 0, index ) );
            data.setProtocolVersion( protocol.substring( index + 1 ) );
        }
        data.setHost( request.getServerName() );
        data.setPort( request.getServerPort() );
        data.setMethod( request.getMethod() );
        data.setPath( request.getRequestURI() );
        data.setParameters( getParams( request ) );
        data.setHeaders( getHeaders( request ) );
        data.setCookies( request.getCookies() );
        return data;
    }

    public static NVPair[] getParams( HttpServletRequest request ) {
        Map map = request.getParameterMap();
        Enumeration e = request.getParameterNames();
        List list = new ArrayList();
        String param = null;
        while ( e.hasMoreElements() ) {
            param = (String) e.nextElement();
//            if ( log.isDebugEnabled() ) {
//                log.debug( "param name( " + param + " )" );
//            }
            list.add( param );
        }
        Collections.sort( list );
        List pairs = new ArrayList( list.size() );
        for ( int i = 0; i < list.size(); i++ ) {
            String[] vals = (String[]) map.get( list.get( i ) );
            for ( int j = 0; j < vals.length; j++ ) {
                try {
                    byte[] bytes = vals[j].getBytes( "ISO-8859-1" );

                    //
                    // todo: We should try to use a field defined in the
                    // test admin page, when recording a new test, that
                    // provides the expected character set encoding to use
                    // for the bytes of the values in the query parameters.
                    // The character set encoding used in the test would be
                    // stored in the recorded XML to be used during playback.
                    //
                    String value = new String( bytes, "UTF-8" );

                    pairs.add( new NVPair( (String) list.get( i ), value ) );
                } catch ( UnsupportedEncodingException uee ) {
                    // Should never hit this
                }
            }
        }
        return (NVPair[]) pairs.toArray( (Object[]) new NVPair[pairs.size()] );
    }

    public static NVPair[] getHeaders( HttpServletRequest request ) {
        Enumeration e = request.getHeaderNames();
        List list = new ArrayList();
        String name = null;
        while ( e.hasMoreElements() ) {
            name = (String) e.nextElement();
//            if ( log.isDebugEnabled() ) {
//                log.debug( "header name( " + name + " )" );
//            }
            list.add( name );
        }
        Collections.sort( list );
        NVPair[] pairs = new NVPair[list.size()];
        for ( int i = 0; i < list.size(); i++ ) {
            pairs[i] = new NVPair( (String) list.get( i ),
                    request.getHeader( (String) list.get( i ) ) );
//            if ( log.isDebugEnabled() ) {
//                log.debug( "header: name( " + pairs[i].getName() + " ), value( " + pairs[i].getValue() + " )" );
//            }
        }
        return pairs;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 256 );
        sb.append( "[ " );
        sb.append( "protocol( " + protocol + " )" );
        sb.append( ", protocolVersion( " + protocolVersion + " )" );
        sb.append( ", host( " + host + " )" );
        sb.append( ", port( " + port + " )" );
        sb.append( ", method( " + method + " )" );
        sb.append( ", path( " + path + " )" );
        sb.append( ", headers( " + Util.toString( headers ) + " )" );
        sb.append( ", parameters( " + Util.toString( parameters ) + " )" );
        sb.append( ", cookies( " + Util.toString( cookies ) + " )" );

        sb.append( " ]" );
        return sb.toString();
    }

}
