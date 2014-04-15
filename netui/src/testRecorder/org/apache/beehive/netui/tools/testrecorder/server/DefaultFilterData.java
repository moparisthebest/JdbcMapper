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

import org.apache.beehive.netui.tools.testrecorder.shared.Constants;
import org.apache.beehive.netui.tools.testrecorder.shared.RequestData;
import org.apache.beehive.netui.tools.testrecorder.shared.ResponseData;
import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.server.state.PlaybackSession;
import org.apache.beehive.netui.tools.testrecorder.server.state.SessionFailedException;
import org.apache.beehive.netui.tools.testrecorder.server.state.RecordSession;
import org.apache.beehive.netui.tools.testrecorder.server.serverAdapter.ServerAdapter;

// J2EE dependencies.
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.ServletRequest;
import javax.servlet.ServletException;

import java.rmi.server.UID;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * User: ozzy
 * Date: Jul 9, 2004
 * Time: 1:19:14 PM
 */
public class DefaultFilterData implements FilterData {

    private static final Logger log = Logger.getInstance( DefaultFilterData.class );

    private ServletRequest request;
    private ServletResponse response;
    private ServerAdapter serverAdapter;
    protected String reqURI;
    // wrapper
    private ServletResponse newResponse;
    protected boolean skipFilter = false;
    protected boolean newRequest = false;
    protected boolean testException = false;
    // playback variables
    protected String testId;
    private PlaybackSession playbackSession;
    // is this method call handling a record transaction?
    // this indicates that we are in record mode.
    private RecordSession recordSession;
    private RequestData reqData;
    private ResponseData respData;
    private List testExceptions;
    private List sessionExceptions;

    public DefaultFilterData( ServletRequest request, ServletResponse response, ServerAdapter serverAdapter ) {
        this.request = request;
        this.response = response;
        this.serverAdapter = serverAdapter;
        testExceptions = new ArrayList();
        sessionExceptions = new ArrayList();
    }

    public void init() throws SessionFailedException {
        processRequest();
        processResponse();
    }

    protected void processRequest() throws SessionFailedException {
        reqURI = ( (HttpServletRequest) request ).getRequestURI();

        ServletRequest original = getOriginalRequest();
        testNewRequest( original );
        testSkipFilter( original );
        testForTestId( original );
        testForTestException();

        // capture request data, this may not be necessary if we aren't recording or playing back.
        reqData = RequestData.populate( (HttpServletRequest) request, new RequestData() );
        if ( log.isDebugEnabled() ) {
            log.debug( "request data( " + reqData + " )" );
        }
    }

    protected void testForTestId( ServletRequest request ) {
        testId = ( (HttpServletRequest) request ).getHeader( Constants.TEST_ID_HEADER );
    }

    protected void testForTestException() {
        Boolean val = (Boolean) getRequest().getAttribute( Constants.REQUEST_EXCEPTION_MARKER_ATTRIBUTE );
        if ( val != null ) {
            testException = val.booleanValue();
        }
        if ( testException ) {
            val = (Boolean) getRequest().getAttribute( Constants.REQUEST_NEW_EXCEPTION_MARKER_ATTRIBUTE );
            if ( val != null ) {
                newRequest = val.booleanValue();
                if ( newRequest ) {
                    getRequest().setAttribute( Constants.REQUEST_NEW_EXCEPTION_MARKER_ATTRIBUTE,
                            Boolean.valueOf( false ) );
                }
            }
        }
    }

    protected void testSkipFilter( ServletRequest request ) {
        skipFilter = Boolean.valueOf( request.getParameter( Constants.FILTER_SKIP_PARAM ) ).booleanValue();
        // check to see if the skip request attribute is set
        if ( skipFilter == false ) {
            // or the skip may be set as an attribute when using a forward
            Object obj = request.getAttribute( Constants.FILTER_SKIP_PARAM );
            if ( obj != null ) {
                if ( obj instanceof Boolean ) {
                    Boolean bool = (Boolean) obj;
                    skipFilter = bool.booleanValue();
                }
            }
            if ( skipFilter == false ) {
                skipFilter = Boolean.valueOf( ( (HttpServletRequest) request ).getHeader(
                        Constants.FILTER_SKIP_PARAM ) ).booleanValue();
            }
        }
    }

    protected void testNewRequest( ServletRequest request ) {
        UID uid = (UID) request.getAttribute( Constants.REQUEST_MARKER_ATTRIBUTE );
        // the request should not have been wrapped either if it goes here
        if ( uid == null ) {
            // this is the first time this request has been seen in the filter.
            // set the marker attribute
            request.setAttribute( Constants.REQUEST_MARKER_ATTRIBUTE, new UID() );
            newRequest = true;
        }
        else {
            newRequest = false;
        }
    }

    protected void processResponse() throws SessionFailedException {
        // create a response wrapper for capturing response data.
        ServletResponse original = getOriginalResponse();
        if ( original instanceof ResponseWrapper ) {
            if ( log.isDebugEnabled() ) {
                log.debug( "instance of ResponseWrapper" );
            }
            if ( isNewRequest() ) {
                // sanity check, new requests should not have an existing response wrapper
                throw new SessionFailedException(
                        "test recorder failure, requests deemed 'new' should not have previous response wrapper" );
            }
            setNewResponse( response );
        }
        else {
            if ( log.isDebugEnabled() ) {
                log.debug( "NOT instance of ResponseWrapper" );
            }
            if ( !isNewRequest() ) {
                // sanity check, only new requests should need to have the response wrapped
                throw new SessionFailedException( "test recorder failure, only new requests should be wrapped" );
            }
            setNewResponse( new ResponseWrapper( (HttpServletResponse) response ) );
        }
    }

    /**
     * return the original request.  if necessary work back through request wrappers to obtain original.
     *
     * @return
     */
    protected ServletRequest getOriginalRequest() {
        ServletRequest original = request;
        int i = 0;
        while ( original instanceof HttpServletRequestWrapper ) {
            original = ( (HttpServletRequestWrapper) original ).getRequest();
            i++;
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "number of servlet request wrappers( " + i + " )" );
        }
        return original;
    }

    /**
     * Return the original response or the test recorder wrapper around the original if it exists.
     *
     * @return
     */
    protected ServletResponse getOriginalResponse() {
        ServletResponse original = response;
        ServletResponse temp = response;
        if ( log.isDebugEnabled() ) {
            log.debug( "response.getClass().getName()( " + original.getClass().getName() + " )" );
        }
        while ( temp != null &&
                ( original instanceof javax.servlet.ServletResponseWrapper ) &&
                !( original instanceof ResponseWrapper ) ) {
            temp = ( (ServletResponseWrapper) original ).getResponse();
            if ( temp != null ) {
                original = temp;
            }
            if ( log.isDebugEnabled() ) {
                log.debug( "response.getClass().getName()( " + original.getClass().getName() + " )" );
            }
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "final: (" + System.identityHashCode( original ) + ") response.getClass().getName()( " +
                    original.getClass().getName() + " )" );
        }
        return original;
    }

    public void addTestException( Throwable e ) {
        testExceptions.add( e );
    }

    public int getTestExceptionCount() {
        return testExceptions.size();
    }

    /**
     * return the first test exception if one exists.
     *
     * @return the first test exception or null
     */
    public Throwable getTestException() {
        if ( getTestExceptionCount() == 0 ) {
            return null;
        }
        return (Throwable) testExceptions.get( 0 );
    }

    public Throwable getTestException( int i ) {
        return (Throwable) testExceptions.get( i );
    }

    public void addSessionException( Throwable e ) {
        sessionExceptions.add( e );
    }

    public int getSessionExceptionCount() {
        return sessionExceptions.size();
    }

    public Throwable getSessionException( int i ) {
        return (Throwable) sessionExceptions.get( i );
    }

    /**
     * rethrow the first test exception if it exists.
     *
     * @throws IOException
     * @throws ServletException
     */
    public void throwTestException() throws IOException, ServletException {
        Throwable ex = getTestException();
        if ( ex != null ) {
            if ( log.isWarnEnabled() ) {
                log.warn( "rethrowing container exception( " + ex + " )", ex );
            }
            markTestException();
            if ( ex instanceof IOException ) {
                throw (IOException) ex;
            }
            if ( ex instanceof ServletException ) {
                throw (ServletException) ex;
            }
            if ( ex instanceof RuntimeException ) {
                throw (RuntimeException) ex;
            }
            // this shouldn't happen but this protects us from just returning from the method
            // in the event of a progamming error.
            // log and bail out the container interface has changed.
            String msg = "Unexpected exception type( " + ex.getClass().getName() + " )";
            log.error( msg );
            assert false;
            throw new RuntimeException( msg, ex );
        }
    }

    protected void markTestException() {
        getRequest().setAttribute( Constants.REQUEST_EXCEPTION_MARKER_ATTRIBUTE, Boolean.valueOf( true ) );
        getRequest().setAttribute( Constants.REQUEST_NEW_EXCEPTION_MARKER_ATTRIBUTE, Boolean.valueOf( true ) );
    }

    public String getReqURI() {
        return reqURI;
    }

    public ServletResponse getNewResponse() {
        return newResponse;
    }

    protected void setNewResponse( ServletResponse newResponse ) {
        this.newResponse = newResponse;
    }

    public boolean isSkipFilter() {
        return skipFilter;
    }

    public boolean isNewRequest() {
        return newRequest;
    }

    public boolean isTestException() {
        return testException;
    }

    public String getTestId() {
        return testId;
    }

    public boolean isPlayback() {
        if ( getPlaybackSession() != null ) {
            return true;
        }
        return false;
    }

    public PlaybackSession getPlaybackSession() {
        return playbackSession;
    }

    public void setPlaybackSession( PlaybackSession playbackSession ) {
        this.playbackSession = playbackSession;
    }

    public RecordSession getRecordingSession() {
        return recordSession;
    }

    public void setRecordingSession( RecordSession recordSession ) {
        this.recordSession = recordSession;
    }

    public void clearRecording() {
        this.recordSession = null;
    }


    /**
     * Indicates that the filter is in record mode and this is considered a new request.
     *
     * @return true if this is a new record request, false otherwise.
     */
    public boolean isNewRecording() {
        if ( isRecording() && isNewRequest() ) {
            return true;
        }
        return false;
    }

    /**
     * Indicates that the filter is in record mode for this request.
     *
     * @return true if this is a record request, false otherwise.
     */
    public boolean isRecording() {
        if ( recordSession != null ) {
            return true;
        }
        return false;
    }

    public RequestData getReqData() {
        return reqData;
    }

    public ResponseData getRespData() {
        return respData;
    }

    public void setRespData( boolean replaceSessionId ) throws SessionFailedException {
        respData = ResponseWrapper.populate( (HttpServletResponse) newResponse,
                new ResponseData( request.getServerName(), request.getServerPort() ) );       
        if ( replaceSessionId &&  respData.getBody() != null ) {
            respData.setBody( serverAdapter.replaceSessionID(  respData.getBody() ) );
        }
    }

    public ServletRequest getRequest() {
        return request;
    }

    public ServletResponse getResponse() {
        return response;
    }

    public ServerAdapter getServerAdapter() {
        return serverAdapter;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 256 );
        sb.append( "[ " );
        sb.append( "reqURI( " + getReqURI() + " )" );
        sb.append( ", skipFilter( " + isSkipFilter() + " )" );
        sb.append( ", newRequest( " + isNewRequest() + " )" );
        sb.append( ", isTestException( " + isTestException() + " )" );
        sb.append( ", recording( " + isRecording() + " )" );
        sb.append( ", testId( " + getTestId() + " )" );
        sb.append( ", playbackSession( " + getPlaybackSession() + " )" );
        sb.append( ", exception( " + getTestException() + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
