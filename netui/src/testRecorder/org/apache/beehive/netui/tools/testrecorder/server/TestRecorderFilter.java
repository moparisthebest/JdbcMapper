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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;

import org.apache.beehive.netui.tools.testrecorder.shared.Constants;
import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.xmlbeans.XMLHelper;
import org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinitions;
import org.apache.beehive.netui.tools.testrecorder.shared.config.WebappConfig;
import org.apache.beehive.netui.tools.testrecorder.server.state.State;
import org.apache.beehive.netui.tools.testrecorder.server.state.SessionFailedException;
import org.apache.beehive.netui.tools.testrecorder.server.state.PlaybackSession;
import org.apache.beehive.netui.tools.testrecorder.server.serverAdapter.ServerAdapter;
import org.apache.beehive.netui.tools.testrecorder.server.serverAdapter.ServerAdapterUtils;


public class TestRecorderFilter implements Filter {

    private static final Logger log = Logger.getInstance( TestRecorderFilter.class );
    private static final String ADMIN_LINK =
        "<div id=\"netuiTestRecorder\" style=\"background-color:yellow;color: blue;margin:5;padding:10;border: 1pt solid;\">\n" +
        "<hr>\n" +
        "<span style=\"font-weight:bold\">Test Recorder:</span>&nbsp;\n" +
        "<a href=\"{0}\">\n" +
        "<span style=\"color:{1};font-weight:bold;\">{2}</span> Recording</a>\n" +
        "<a href=\"{3}\">Admin</a>\n" +
        "</div>";
    /*
    private static final String ADMIN_LINK =
            "<hr><table style=\"background-color: yellow; color: blue\" width=\"100%\">" +
            "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<strong>Test Recorder:&nbsp;</strong>" +
            "&nbsp;&nbsp;&nbsp;<a href=\"{0}\">" +
            "<b style=\"color: {1}\">{2}</b><strong> Recording</strong></a>" +
            "&nbsp;&nbsp;&nbsp;<a href=\"{3}\"><strong>Admin</strong></a>" +
            "<td><tr></table></span>";
    */
    private static final MessageFormat link = new MessageFormat( ADMIN_LINK );
    // set in init, returned to callers via a public static method
    private static TestRecorderFilter instance = null;
    boolean initFlag = false;
    // set once on first request
    private static String contextPath;
    // these two are dependent on contextPath and are set on first request/
    private static Object[] startLink;
    private static Object[] stopLink;

    private TestDefinitions testDefinitions;
    private WebappConfig webapp;
    private FilterConfig filterConfig = null;
    private State state;


    public TestRecorderFilter() {
    }

    public void doFilter( ServletRequest request, ServletResponse response,
            FilterChain chain ) throws IOException, ServletException {
        if ( initFlag == false ) {
            filterInit( request );
        }
        String reqURI = ( (HttpServletRequest) request ).getRequestURI();
        if ( log.isDebugEnabled() ) {
            log.debug( "\n\t\t**********************************************\n" );
        }
        if ( !getState().isTestMode() || !shouldHandleRequest( reqURI ) || isControlRequest( reqURI ) ) {
            // not in test mode just or not a handled suffix, do the normal stuff ...
            if ( log.isDebugEnabled() ) {
                log.debug( "ignoring request, reqURI( " + reqURI + " ), testMode( " +
                        getState().isTestMode() + " )" );
            }
            chain.doFilter( request, response );
            return;
        }
        if ( log.isInfoEnabled() ) {
            log.info( "reqURI( " + reqURI + " )" );
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "state( " + getState() + " )" );
            //reportCookies( ((HttpServletRequest) request).getCookies() );
            //reportHeaders( RequestData.getHeaders( (HttpServletRequest) request ) );
        }
        ServerAdapter serverAdapter = ServerAdapterUtils.getServerAdapter();
        FilterData data = serverAdapter.genFilterDataInstance( request, response, state );
        try {
            data.init();
        }
        catch ( SessionFailedException e ) {
            data.addSessionException( e );
        }
        if ( data.isSkipFilter() == true ) {
            // skip filter work and send the request on
            if ( log.isDebugEnabled() ) {
                log.debug( "skiping filter" );
            }
            chain.doFilter( data.getRequest(), data.getResponse() );
            return;
        }
        try {
            // no container work, session work only
            preFilter( data, chain );
        }
        catch ( SessionFailedException e ) {
            data.addSessionException( e );
            log.error( "Pre filter work failed", e );
        }
        // filter work
        if ( log.isDebugEnabled() ) {
            log.debug( "calling doFilter()" );
        }
        // store any exceptions ... after cleanup rethrow exceptions
        // as necessary.
        try {
            chain.doFilter( data.getRequest(), data.getNewResponse() );
        }
        catch ( Exception ex ) {
            data.addTestException( ex );
            if ( log.isDebugEnabled() ) {
                log.debug( "caught filter chain exception( " + ex + " )", ex );
            }
        }
        finally {
            postFilter( data );
            if ( log.isDebugEnabled() && data.isNewRequest() ) {
                log.debug( "\nreturning, state( " + state + " )" );
            }
            else if ( log.isDebugEnabled() ) {
                log.debug( "data.isNewRequest()( " + data.isNewRequest() + " )" );
            }
        }
        return;
    }

    protected void postFilter( FilterData data ) throws IOException, ServletException {
        if ( log.isDebugEnabled() ) {
            log.debug( "postFilter START" );
        }
        try {
            if ( data.isPlayback() ) {
                postFilterPlayback( data );
            }
            else {
                String body = null;
                if ( data.isNewRequest() || data.isNewRecording() ) {
                    // the request has been wrapped, get the body
                    try {
                        if ( data.getTestExceptionCount() > 0 ) {
                            ( (HttpServletResponse) data.getNewResponse() ).setStatus( 500 );
                        }
                        data.setRespData( false );
                        body = data.getRespData().getBody();
                    }
                    catch ( Exception e ) {
                        data.addSessionException( e );
                        body = "Failed getting output string from wrapper, exception( " +
                                e.getMessage() + " )";
                        log.error( body, e );
                        body = "<br/><br/><b>" + body + "</b><br/><br/></html>";
                    }
                }
                if ( data.isNewRecording() ) {
                    // recording and outer or new request
                    if ( log.isDebugEnabled() ) {
                        log.debug( "filter data( " + data + " )" );
                    }
                    postFilterRecord( data, body );
                }
                else if ( data.isNewRequest() ) {
                    // not recording or playback but we have still captured the response,
                    // write out the response with links if in test mode
                    writeResponse( (ResponseWrapper) data.getNewResponse(), body, data, getState().isTestMode() );
                }
                else {
                    // forward or include
                    if ( log.isDebugEnabled() ) {
                        log.debug( "forward or include, filter data( " + data + " )" );
                    }
                }
            }
        }
        finally {
            data.throwTestException();
            if ( log.isDebugEnabled() ) {
                log.debug( "postFilter END" );
            }
        }
    }

    protected void postFilterRecord( FilterData data, String body ) throws IOException, ServletException {
        // capture record response data
        if ( log.isDebugEnabled() ) {
            log.debug( "data.isNewRecording()( " + data.isNewRecording() + " )" );
        }
        // write the response to the client, let IOExceptions bubble up as part of normal servlet behavior
        try {
            writeResponse( (ResponseWrapper) data.getNewResponse(), body, data, true );
        }
        finally {
            int testNum = 0;
            try {
                if ( data.isTestException() ) {
                    if ( log.isDebugEnabled() ) {
                        log.debug( "skipping record end, isTestException(" + data.isTestException() + "), body( " +
                                body + " )");
                    }
                }
                else {
                    String resp = data.getRespData().getBody();
                    // replace the CDATA in the body. We escape the CDATA so we can nest this.
                    int pos = resp.indexOf("<![CDATA[");
                    if (pos != -1) {
                        resp = resp.replaceAll("\\Q<![CDATA[\\E","&lt;![CDATA[");
                        resp = resp.replaceAll("\\Q]]>","]]&gt;");
                        data.getRespData().setBody(resp);
                    }
                    testNum = data.getRecordingSession().endTest( data.getReqData(), data.getRespData() );
                    if ( log.isDebugEnabled() ) {
                        log.debug( "testNum( " + testNum + " )" );
                    }
                }
            }
            catch ( SessionFailedException e ) {
                // do not rethrow exception
                data.addSessionException( e );
                log.error( "Failed to end test(" + testNum + ") for session( " +
                        data.getRecordingSession().getSessionName() + " )" );
            }
            data.clearRecording();
        }
    }

    protected void postFilterPlayback( FilterData data ) {
        // capture playback response data
        if ( log.isDebugEnabled() ) {
            log.debug( "postFilterPlayback START" );
        }
        if ( !data.isNewRequest() ) {
            // forward or include ... do nothing
            if ( log.isDebugEnabled() ) {
                log.debug( "data.isNewRequest()( " + data.isNewRequest() + " )" );
                log.debug( "postFilterPlayback END" );
            }
            return;
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "filter data( " + data + " )" );
        }
        // new or outer request obtain response data and diff
        int testNum = -2;
        try {
            if ( data.getTestExceptionCount() > 0 ) {
                ( (HttpServletResponse) data.getNewResponse() ).setStatus( 500 );
            }
            data.setRespData( true );
            if ( log.isDebugEnabled() ) {
                log.debug( "response body( " + data.getRespData().getBody() + " )" );
            }
            if ( data.isTestException() ) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "skipping playback end, isTestException(" + data.isTestException() + ")" );
                }
            }
            else {
                testNum = data.getPlaybackSession().endTest( data );
            }

            if ( log.isDebugEnabled() ) {
                log.debug( "testNum( " + testNum + " )" );
            }
        }
                // Session exception
        catch ( Exception ex ) {
            data.addSessionException( ex );
            if ( log.isDebugEnabled() ) {
                log.debug( "Failed ending playback, exception( " + ex.getMessage() + " )", ex );
            }
            // do not rethrow.
        }
        finally {
            if ( testNum >= 0 ) {
                setResponseOutcome( data.getResponse(), Constants.PASS );
            }
            else {
                log.error( "Failed ending test(" + testNum + ")" );
                setResponseOutcome( data.getResponse(), Constants.FAIL );
            }
            // write the output to client
            writeResponse( (ResponseWrapper) data.getNewResponse(), data.getRespData().getBody(), data, false );
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "postFilterPlayback END" );
        }
    }

    /**
     * @param data
     * @param chain
     * @throws SessionFailedException
     */
    protected void preFilter( FilterData data, FilterChain chain ) throws SessionFailedException {
        if ( log.isDebugEnabled() ) {
            log.debug( "preFilter START" );
        }
        if ( data.getTestId() != null ) {
            // playback ...
            if ( log.isDebugEnabled() ) {
                log.debug( "filter data( " + data + " )" );
            }
            if ( data.isNewRequest() ) {
                PlaybackSession session = getState().getPlaybackSession( data.getTestId() );
                if ( session == null ) {
                    String msg = "ERROR: no playback session found for test id( " + data.getTestId() + " )";
                    log.error( msg );
                    throw new SessionFailedException( msg );
                }
                data.setPlaybackSession( session );
                if ( data.isTestException() ) {
                    if ( log.isDebugEnabled() ) {
                        log.debug( "skipping playback start, isTestException(" + data.isTestException() + ")" );
                    }
                }
                else {
                    boolean started = session.startTest();
                    // may throw SessionFailedException
                    if ( !started ) {
                        throw new SessionFailedException( "Playback session failed to start, session( " +
                                session.getSessionName() + " )" );
                    }
                }
            }
            else {
                // this is a forward ... do nothing
            }
            if ( log.isDebugEnabled() ) {
                log.debug( "filter data( " + data + " )" );
            }
        }
        else {
            // not playback ... maybe recording ...
            // we want to check this value once and leave it on for the life of the filter data object
            // (this method call) so we check and set it in a synchronized block
            synchronized ( getState() ) {
                if ( getState().isRecording() == true ) {
                    data.setRecordingSession( getState().getRecordingSession() );
                }
            }
            if ( data.isNewRecording() ) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "filter data.isNewRecording()( " + data.isNewRecording() + " )" );
                }
                // only start recording the request the first time it is seen,
                // not on forwards back through the container or test exceptions forwarded back to filter
                if ( data.isTestException() ) {
                    if ( log.isDebugEnabled() ) {
                        log.debug( "skipping record start, isTestException(" + data.isTestException() + ")" );
                    }
                }
                else {
                    data.getRecordingSession().startTest();
                }
            }
            else {
                if ( log.isDebugEnabled() ) {
                    log.debug( "forward, include or not recording filter data( " + data + " )" );
                }
            }
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "preFilter END" );
        }
    }

    public void init( FilterConfig filterConfig ) throws ServletException {
        this.filterConfig = filterConfig;
        instance = this;
        String webappName = filterConfig.getInitParameter( "webapp" );
        System.out.println( "initializing test recorder enabled webapp( " + webappName + " )" );
        if ( log.isInfoEnabled() ) {
            log.info( "webapp name( " + webappName + " )" );
        }
        testDefinitions = XMLHelper.getTestDefinitionsInstance( Thread.currentThread().getContextClassLoader() );
        webapp = testDefinitions.getWebapps().getWebapp( webappName );
        if ( webapp == null ) {
            throw new ServletException(
                    "ERROR: unable to find the webapp config for webapp name( " + webappName + " )" );
        }
        if ( log.isInfoEnabled() ) {
            log.debug( "webapp( " + webapp + " )" );
        }
        state = new State();
        state.setTestMode( webapp.isTestMode() );
    }

    public void destroy() {
        if ( log.isInfoEnabled() ) {
            log.info( "destroying test recorder filter for webapp( " + getWebapp().getName() + " )" );
        }
        instance = null;
        this.filterConfig = null;
    }

    public State getState() {
        return state;
    }

    public static TestRecorderFilter instance() {
        return instance;
    }

    protected FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public WebappConfig getWebapp() {
        return webapp;
    }

    public TestDefinitions getTestDefinitions() {
        return testDefinitions;
    }

    private void writeResponse( ResponseWrapper wrapper, String body, FilterData data, boolean addLink ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "data.getReqURI()( " + data.getReqURI() + " )" );
        }
        try {
            ServletResponse response = wrapper.getResponse();
            Writer writer = getWriter( response );
            // add a link to start or stop recording.
            if ( addLink ) {
                int index = body.lastIndexOf( Constants.BODY_END );
                if ( index == -1 ) {
                    index = body.lastIndexOf( Constants.BODY_END_CAPS );
                }
                if ( index == -1 ) {
                    if ( log.isDebugEnabled() ) {
                        log.debug( "</body> was not found:\nbody(" + body + ")" );
                    }
                    // just print what is there
                    writer.write( body );
                }
                else if ( data.isNewRequest() ) {
                    writer.write( body.substring( 0, index ) );
                    // write link
                    Object[] objs = null;
                    if ( getState().isRecording() ) {
                        objs = stopLink;
                    }
                    else {
                        objs = startLink;
                    }
                    writer.write( Constants.NL + link.format( objs ) + Constants.NL );
                    writer.write( body.substring( index ) );
                }
                else {
                    writer.write( body );
                }
            }
            else {
                writer.write( body );
            }
        }
        catch ( IOException e ) {
            data.addTestException( e );
            if ( log.isWarnEnabled() ) {
                log.warn( "Failed writing response, exception( " + e.getMessage() + " )" );
            }
        }
    }

    private Writer getWriter( ServletResponse resp ) throws IOException {
        Writer writer = null;
        try {
            writer = resp.getWriter();
        }
        catch ( IllegalStateException e ) {
            // when we get an IllegalStateException then someone accessed the
            // output stream so we need to create the writer from the output stream
            if ( log.isDebugEnabled() ) {
                log.debug( "Creating a Writer for the response" );
            }
            writer = new OutputStreamWriter( resp.getOutputStream() );
        }
        return writer;
    }

    private void filterInit( ServletRequest request ) {
        // prefix for requests to this webapp
        contextPath = ( (HttpServletRequest) request ).getContextPath();
        genStartStopLinks();
        initFlag = true;
    }

    protected static void genStartStopLinks() {
        startLink = new Object[4];
        stopLink = new Object[4];

        startLink[0] = contextPath + "/testRecorder/startRecord.jsp?" +
                Constants.FILTER_SKIP_PARAM + "=true";
        startLink[1] = Constants.GREEN;
        startLink[2] = Constants.START;
        startLink[3] = contextPath + "/testRecorder/index.jsp?" +
                Constants.FILTER_SKIP_PARAM + "=true";

        stopLink[0] = contextPath + "/testRecorder?" +
                Constants.MODE + "=" + Constants.RECORD + "&" +
                Constants.CMD + "=" + Constants.STOP + "&" +
                Constants.FILTER_SKIP_PARAM + "=true";
        stopLink[1] = Constants.RED;
        stopLink[2] = Constants.STOP;
        stopLink[3] = contextPath + "/testRecorder/index.jsp?" +
                Constants.FILTER_SKIP_PARAM + "=true";
    }

    // determine if this url should be handled by the filter.
    private boolean shouldHandleRequest( String reqURI ) {
        boolean rtnVal = false;
        // is the request URI to a servlet, no suffix
        int slash = reqURI.lastIndexOf( "/" );
        int period = reqURI.lastIndexOf( "." );
        String suffix = null;
        if ( slash > period ) {
            // no period in the last segment of the path, no path suffix.
            suffix = Constants.EMPTY_STRING;
        }
        else {
            suffix = reqURI.substring( period + 1 );
        }
        rtnVal = getWebapp().handleSuffix( suffix );
        return rtnVal;
    }

    private boolean isControlRequest( String reqURI ) {
        if ( reqURI.startsWith( getWebapp().getServletURI() ) ) {
            return true;
        }
        return false;
    }

    static void setResponseOutcome( ServletResponse response, String value ) {
        ( (HttpServletResponse) response ).setHeader( Constants.OUTCOME_HEADER, value );
    }

// debugging method
    protected static void reportCookies( Cookie[] cookies ) {
        if ( cookies != null ) {
            for ( int i = 0; i < cookies.length; i++ ) {
                log.debug( "cookies[" + i + "]( " + cookies[i] + " )" );
            }
        }
        else {
            log.debug( "cookies( " + cookies + " )" );
        }
    }

}
