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

import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.ResponseData;
import org.apache.beehive.netui.tools.testrecorder.server.state.SessionFailedException;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;


public class ResponseWrapper extends HttpServletResponseWrapper {

    private static final Logger log = Logger.getInstance( ResponseWrapper.class );

    private ByteArrayOutputStream output;
    private PrintWriter writer;
    private ServletOutputStream servletStream;
    private int statusCode = SC_OK;
    private String reason = "";
    private String outputString = null;

    public ResponseWrapper( HttpServletResponse response ) {
        super( response );
        output = new ByteArrayOutputStream( 2048 );
    }

    public PrintWriter getWriter() throws IOException {
        if ( log.isDebugEnabled() ) {
            log.debug( "getWriter()" );
        }
        if ( writer == null ) {
            String encoding = getCharacterEncoding();
            writer = new PrintWriter( new BufferedWriter(
                new OutputStreamWriter( getOutputStream(), encoding ) ) );
        }
        return writer;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if ( log.isDebugEnabled() ) {
//            log.debug( "getOutputStream()" );
//            Exception e = new Exception();
//            e.printStackTrace();
        }
        if ( servletStream == null ) {
            servletStream = new InternalServletStream( output );
        }
        // create an internal output
        return servletStream;
    }

    public void sendError( int statusCode ) throws IOException {
        if ( log.isDebugEnabled() ) {
            log.debug( "sendError(): statusCode( " + statusCode + " )" );
        }
        this.statusCode = statusCode;
        super.sendError( statusCode );
    }

    public void sendError( int statusCode, String reason ) throws IOException {
        if ( log.isDebugEnabled() ) {
            log.debug( "sendError(): statusCode( " + statusCode + " )" );
            log.debug( "reason( " + reason + " )" );
        }
        this.statusCode = statusCode;
        this.reason = reason;
        super.sendError( statusCode, reason );
    }

    public void setStatus( int statusCode ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "setStatus(): statusCode( " + statusCode + " )" );
        }
        this.statusCode = statusCode;
        super.setStatus( statusCode );
        if ( log.isDebugEnabled() ) {
            log.debug( "setStatus() done" );
        }
    }

    public void reset() {
        if ( log.isDebugEnabled() ) {
            log.debug( "reset()" );
        }
        if ( isCommitted() ) {
            throw new IllegalStateException( "response is already commited, reset not allowed" );
        }
        output.reset();
        statusCode = SC_OK;
        super.reset();
    }

    public void resetBuffer() {
        if ( log.isDebugEnabled() ) {
            log.debug( "resetBuffer()" );
        }
        if ( isCommitted() ) {
            throw new IllegalStateException( "response is already commited, reset buffer not allowed" );
        }
        output.reset();
        super.resetBuffer();
    }

    // package scoped
    int getStatusCode() {
        return statusCode;
    }

    // package scoped
    String getReason() {
        return reason;
    }

    // package scoped
    String getOutputString() throws IOException {
        if ( outputString == null ) {
            if ( writer != null ) {
                writer.flush();
                writer.close();
            }
            output.flush();
            output.close();
            String encoding = getCharacterEncoding();
            outputString = output.toString( encoding );
        }
        return outputString;
    }

    /**
     * Internal class that implements a ServletOutputStream.  This is used to
     * return our output stream to the JSP world.
     */
    static class InternalServletStream extends ServletOutputStream {

        private OutputStream stream;

        public InternalServletStream( OutputStream stream ) {
            this.stream = stream;
        }

        public void write( int b )
                throws IOException {
            stream.write( b );
        }
    }

    public static ResponseData populate( HttpServletResponse response,
            ResponseData respData ) throws SessionFailedException {
        String data = null;
        try {
            // may throw ClassCastException
            ResponseWrapper wrapper = (ResponseWrapper) response;
            respData.setReason( wrapper.getReason() );
            if ( log.isDebugEnabled() ) {
                log.debug( "populate status code( " + wrapper.getStatusCode() + " )" );
            }
            respData.setStatusCode( wrapper.getStatusCode() );
            data = wrapper.getOutputString();
        }
        catch ( Exception e ) {
            throw new SessionFailedException( "ERROR: failed to get output stream from wrapper" );
        }
        if ( log.isDebugEnabled() ) {
//            log.debug( "data( " + data + " )" );
        }
        // data may be null
        respData.setBody( data );
        return respData;
    }

}
