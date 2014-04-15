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

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;

import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.RecordSessionBean;
import org.apache.beehive.netui.tools.testrecorder.shared.Constants;
import org.apache.beehive.netui.tools.testrecorder.shared.RequestData;
import org.apache.beehive.netui.tools.testrecorder.shared.NVPair;
import org.apache.beehive.netui.tools.testrecorder.shared.ResponseData;
import org.apache.beehive.netui.tools.testrecorder.shared.xmlbeans.XMLHelper;
import org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinition;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.HttpRecoverableException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 */
public class PlaybackExecutor {

    private static final Logger LOGGER = Logger.getInstance( PlaybackExecutor.class );

    private static HttpClient controlClient = new HttpClient();
    private static HttpClient testClient = new HttpClient();

    private static final DecimalFormat format = (DecimalFormat)
            NumberFormat.getInstance( Locale.US );

    static {
        // we just want the integer portion
        format.setDecimalSeparatorAlwaysShown( false );
        format.setGroupingSize( 0 );
    }

    private TestDefinition test;
    private String description;
    private String testUser;
    private File recordFile;
    private File resultsFile;
    private File diffFile;

    private NameValuePair[] startParams;
    private NameValuePair[] stopParams;

    private String testId;
    private RecordSessionBean session;

    public PlaybackExecutor( TestDefinition test,
                             String description,
                             String testUser ) {
        this.test = test;
        assert test != null : "the test definition name may not be null";
        this.description = description;
        if ( description == null )
            description = "No Description.";

        this.testUser = testUser;
        setControlParams();
    }

    protected void setControlParams() {
        startParams = new NameValuePair[6];
        stopParams = new NameValuePair[4];

        startParams[0] = new NameValuePair( Constants.MODE, Constants.PLAYBACK );
        startParams[1] = new NameValuePair( Constants.CMD, Constants.START );
        startParams[2] = new NameValuePair( Constants.FILTER_SKIP_PARAM, Boolean.TRUE.toString() );
        startParams[3] = new NameValuePair( Constants.TEST_NAME, getTest().getName() );
        startParams[4] = new NameValuePair( Constants.DESCRIPTION, getDescription() );
        startParams[5] = new NameValuePair( Constants.TEST_USER, getTestUser() );

        stopParams[0] = new NameValuePair( Constants.MODE, Constants.PLAYBACK );
        stopParams[1] = new NameValuePair( Constants.CMD, Constants.STOP );
        stopParams[2] = new NameValuePair( Constants.FILTER_SKIP_PARAM, Boolean.TRUE.toString() );
    }

    public boolean run() throws PlaybackException {
        boolean rtnVal = false;
        // start playback
        Throwable exception = null;
        try {
            startPlayback();
            executeRequests();
        }
        catch ( Throwable e ) {
            e.printStackTrace();
            LOGGER.error( "start failure", e );
            exception = e;
        }
        finally {
            if ( getTestId() != null ) {
                // we successfully started, so stop
                try {
                    rtnVal = stopPlayback();
                }
                catch ( Throwable e ) {
                    if ( exception == null ) {
                        exception = e;
                    }
                    else {
                        LOGGER.error( "ERROR: possible spurious failure, failed to stop playback, exception( " + e.getMessage() + " )", e );
                    }
                }
            }

            if ( exception != null ) {
                String msg = "ERROR: playback failed, exception( " + exception.getMessage() + " )";
                LOGGER.error( msg, exception );
                throw new PlaybackException(msg, exception);
            }
        }
        return rtnVal;
    }

    protected void startPlayback()
        throws PlaybackException {

        HttpMethod method = new GetMethod(
            RequestData.genUri( "http",
                                getTest().getWebapp().getServer().getHostname(),
                                getTest().getWebapp().getServer().getPort(),
                                getTest().getWebapp().getServletURI() )
        );

        method.setQueryString( getStartParams() );
        ResponseData response = null;

        try {
            response = execute( getControlClient(), method );
        }
        catch ( Exception e ) {
            String msg = "Failed to execute start playback request, exception( " + e.getMessage() + " )";
            LOGGER.error( msg, e );
            throw new PlaybackException(msg, e);
        }

        String outcome = response.getHeader( Constants.OUTCOME_HEADER );
        String testId = response.getHeader( Constants.TEST_ID_HEADER );
        String testFileName = response.getHeader( Constants.RECORD_FILE_HEADER );
        String startMsg = response.getHeader( Constants.MSG_ATTRIBUTE );

        /* config the playback stop params */
        setStopParam( 3, new NameValuePair( Constants.TEST_ID_HEADER, testId ) );
        setTestId( testId );
        if ( LOGGER.isInfoEnabled() ) {
            LOGGER.info( "outcome( " + outcome + " )" );
            LOGGER.info( "testId( " + getTestId() + " )" );
            LOGGER.info( "testFileName( " + testFileName + " )" );
        }

        /* failed */
        if ( outcome == null || !outcome.equals( Constants.PASS ) )
            throw new PlaybackException("Failed to start playback, server error message( " + startMsg + " )" );

        this.recordFile = new File( testFileName );
        if ( !getRecordFile().canRead() ) {
            String msg = "ERROR: failed to start playback, lacking permissions to read record file( " + getRecordFile() + " )";
            System.out.println( msg );
            LOGGER.error( msg );
            throw new PlaybackException( msg );
        }

        /* parsexml */
        RecordSessionBean session = createSession();

        setSession( session );

        if ( LOGGER.isInfoEnabled() )
            LOGGER.info( "playback started for test( " + getTest().getName() + " )" );
    }

    protected boolean stopPlayback() throws PlaybackException {
        boolean rtnVal = false;
        HttpMethod method = new GetMethod( RequestData.genUri( "http",
                getTest().getWebapp().getServer().getHostname(), getTest().getWebapp().getServer().getPort(),
                getTest().getWebapp().getServletURI() ) );
        method.setQueryString( getStopParams() );
        ResponseData response = null;
        try {
            response = execute( getControlClient(), method );
        }
        catch ( Exception e ) {
            String msg = "Failed to execute stop playback request, exception( " + e.getMessage() + " )";
            LOGGER.error( msg, e );
            if ( e instanceof PlaybackException ) {
                throw (PlaybackException) e;
            }
            throw new PlaybackException( msg, e );
        }
        String outcome = response.getHeader( Constants.OUTCOME_HEADER );
        String resultsFileName = response.getHeader( Constants.RESULTS_FILE_HEADER );
        String diffFileName = response.getHeader( Constants.RESULTS_DIFF_HEADER );
        String stopMsg = response.getHeader( Constants.MSG_ATTRIBUTE );
        if ( LOGGER.isDebugEnabled() ) {
            LOGGER.debug( "stop outcome( " + outcome + " )" );
            LOGGER.debug( "stopMsg( " + stopMsg + " )" );
        }
        if ( outcome != null && !outcome.equals( Constants.ERROR ) ) {
            // requests successfully executed
            if ( LOGGER.isInfoEnabled() ) {
                LOGGER.info( "Results are stored in file( " + resultsFileName + " )" );
            }
            setResultsFile( new File( resultsFileName ) );
            // if there were errors then report them...
            if ( outcome.equals( Constants.FAIL ) ) {
                if ( LOGGER.isInfoEnabled() ) {
                    LOGGER.info( "Diff results are stored in file( " + diffFileName + " )" );
                }
                setDiffFile( new File( diffFileName ) );
                rtnVal = false;
            }
            else {
                rtnVal = true;
            }
        }
        else {
            String msg = "ERROR: error encountered executing test( " + getTest().getName() + " ), server msg( " +
                    stopMsg + " )";
            LOGGER.error( msg );
            throw new PlaybackException( msg );
        }
        return rtnVal;
    }

    private RecordSessionBean createSession()
        throws PlaybackException {
        RecordSessionBean session = null;
        try {
            session = XMLHelper.getRecordSessionBean( getRecordFile() );
            if(LOGGER.isDebugEnabled())
                LOGGER.debug("test(" + getTest().getName() + "), testCount(" + session.getTestCount() + ")");
        }
        catch(Exception e) {
            String msg = "ERROR: failed to process session file(" + getRecordFile().getAbsolutePath() + ")";
            throw new PlaybackException(msg, e);
        }
        return session;
    }

    private void executeRequests()
        throws PlaybackException {

        RequestData request = null;
        int testNumber = 0;
        try {
            List requestList = getSession().getRequestData();
            if ( LOGGER.isDebugEnabled() )
                LOGGER.debug( "beginning test execution" );

            for ( testNumber = 0; testNumber < requestList.size(); testNumber++ ) {
                request = (RequestData) requestList.get( testNumber );

                if ( LOGGER.isDebugEnabled() )
                    LOGGER.debug( "beginning execution for test number( " + ( testNumber + 1 ) + " )" );

                HttpMethod method = createPlaybackMethod( request, getTest() );
                if ( LOGGER.isDebugEnabled() )
                    LOGGER.debug( "playback URI( " + method.getURI() + " )" );

                ResponseData response = execute( getTestClient(), method );
                if ( LOGGER.isDebugEnabled() ) {
                    LOGGER.debug( "playback response, status code( " + response.getStatusCode() + " )" );
                    LOGGER.debug( "playback response, body(\n" + response.getBody() + " )" );
                }
                String outcome = response.getHeader( Constants.OUTCOME_HEADER );
                if ( LOGGER.isDebugEnabled() )
                    LOGGER.debug( "outcome( " + outcome + " )" );

                if ( response.getStatusCode() >= 400 ) {
                    String msg = "WARNING: unable to access URI( " + method.getURI() + "), status code( " + response.getStatusCode() + " ) returned";
                    System.out.println( msg );

                    if ( LOGGER.isWarnEnabled() )
                        LOGGER.warn( msg );
                }
            }
        }
        catch ( Exception e ) {
            String msg = "ERROR: failed executing request for test( " + getTest().getName() +
                    " ), testNumber( " + testNumber + " ), path( " + request.getPath() + " ), exception( "
                    + e.getMessage() + " )";
            LOGGER.error( msg, e );
            throw new PlaybackException(msg, e);
        }
    }

    protected ResponseData execute( HttpClient client, HttpMethod method ) throws PlaybackException, IOException {
        ResponseData response = new ResponseData( method.getHostConfiguration().getHost(),
                method.getHostConfiguration().getPort() );
        int statusCode = -1;
        // retry up to 3 times.
        for ( int attempt = 0; statusCode == -1 && attempt < 3; attempt++ ) {
            try {
                statusCode = client.executeMethod( method );
            }
            catch ( HttpRecoverableException e ) {
                if ( LOGGER.isWarnEnabled() ) {
                    String msg = "A recoverable exception occurred calling URI( " + method.getURI() +
                            " ), retrying. exception( " + e.getMessage() + " )";
                    LOGGER.error( msg, e );
                }
            }
            catch ( IOException e ) {
                String msg = "Failed executing request( " + method.getURI() + " ), exception( " + e.getMessage() +
                        " )";
                LOGGER.error( msg, e );
                throw e;
            }
        }
        // Retries failed
        if ( statusCode == -1 ) {
            String msg = "Failed to execute request( " + method.getURI() + " )";
            LOGGER.error( msg );
            throw new PlaybackException( msg );
        }
        response.setStatusCode( statusCode );
        byte[] responseBody = method.getResponseBody();
        response.setBody( new String( responseBody ) );
        if ( LOGGER.isDebugEnabled() ) {
            LOGGER.debug( "statusCode( " + statusCode + " )" );
//            LOGGER.debug( "response body:\n" + response.getBody() + "\n" );
        }
        response.setHeaders( convertHeaders( method.getResponseHeaders() ) );
        method.releaseConnection();
        return response;
    }

    protected HttpMethod createPlaybackMethod( RequestData request, TestDefinition test ) {
        HttpMethod method = null;
        if ( request.getMethod().equalsIgnoreCase( "POST" ) ) {
            method = new PostMethod( request.getUri( getTest().getWebapp().getServer().getHostname(),
                    getTest().getWebapp().getServer().getPort() ) );
            populatePlaybackPostMethod( (PostMethod) method, request );
        }
        else if ( request.getMethod().equalsIgnoreCase( "GET" ) ) {
            method = new GetMethod( request.getUri( getTest().getWebapp().getServer().getHostname(),
                    getTest().getWebapp().getServer().getPort() ) );
            populatePlaybackGetMethod( (GetMethod) method, request );
        }
        else {
            throw new RuntimeException( "ERROR: unhandled HTTP method( " + request.getMethod() + " )" );
        }
        return method;
    }

    protected HttpMethod populatePlaybackPostMethod( final PostMethod method, final RequestData request ) {
        LOGGER.debug( "calling setRequestBody()" );
        method.setRequestBody( convertNVPairs( request.getParameters() ) );
        populatePlaybackMethod( method, request );
        debugNameValuePairs( method.getParameters() );
        return method;
    }

    protected HttpMethod populatePlaybackGetMethod( final GetMethod method, final RequestData request ) {
        populatePlaybackMethod( method, request );
        NameValuePair[] nvPairs = convertNVPairs(request.getParameters());
        method.setQueryString( nvPairs != null ? nvPairs : new NameValuePair[0] );
        return method;
    }

    protected HttpMethod populatePlaybackMethod( final HttpMethod method, RequestData request ) {
        addRequestHeaders( method, request );
        addTestIdHeader( method );
        return method;
    }

    protected HttpMethod addRequestHeaders( HttpMethod method, RequestData request ) {
        for ( int i = 0; i < request.getHeaderCount(); i++ ) {
            if ( skipPlaybackHeader( request.getHeaderName( i ) ) ) {
                continue;
            }
            method.addRequestHeader( request.getHeaderName( i ), request.getHeaderValue( i ) );
        }
        return method;
    }

    /**
     * if this method returns true the header specified by the name parameter should not be sent to the
     * client during playback
     *
     * @param name
     */
    protected boolean skipPlaybackHeader( String name ) {
        // fail mode header is for legacy record files
        if ( name.equalsIgnoreCase( "host" ) ||
                name.equalsIgnoreCase( "referer" ) ||
                name.equalsIgnoreCase( "Content-length" ) ||
                name.equalsIgnoreCase( "Host" ) ||
                name.equals( Constants.TEST_ID_HEADER ) ||
                name.equals( Constants.FAIL_MODE_HEADER ) ||
                name.equals( Constants.TEST_NUMBER_HEADER ) ) {
            // skip
            return true;
        }
        return false;
    }

    public NameValuePair[] getStartParams() {
        return startParams;
    }

    protected void setStartParams( NameValuePair[] startParams ) {
        this.startParams = startParams;
    }

    protected void setStartParam( int index, NameValuePair param ) {
        if ( index >= startParams.length ) {
            throw new IndexOutOfBoundsException( "Invalid start parameter index( " + index +
                    " ), start parameter size is (" + startParams.length + ")" );
        }
        stopParams[index] = param;
    }

    public NameValuePair[] getStopParams() {
        return stopParams;
    }

    protected void setStopParams( NameValuePair[] stopParams ) {
        this.stopParams = stopParams;
    }

    protected void setStopParam( int index, NameValuePair param ) {
        if ( index >= stopParams.length ) {
            throw new IndexOutOfBoundsException( "Invalid stop parameter index( " + index +
                    " ), stop parameter size is (" + stopParams.length + ")" );
        }
        stopParams[index] = param;
    }

    protected HttpMethod addTestIdHeader( HttpMethod method ) {
        if ( getTestId() != null ) {
            method.setRequestHeader( Constants.TEST_ID_HEADER, getTestId() );
        }
        return method;
    }

    public RecordSessionBean getSession() {
        return session;
    }

    protected void setSession( RecordSessionBean session ) {
        this.session = session;
    }

    protected HttpClient getTestClient() {
        return testClient;
    }

    protected HttpClient getControlClient() {
        return controlClient;
    }

    public TestDefinition getTest() {
        return test;
    }

    public String getDescription() {
        return description;
    }

    public String getTestUser() {
        return testUser;
    }

    public String getTestId() {
        return testId;
    }

    protected void setTestId( String testId ) {
        this.testId = testId;
    }

    public File getRecordFile() {
        return recordFile;
    }

    public File getResultsFile() {
        return resultsFile;
    }

    protected void setResultsFile( File resultsFile ) {
        this.resultsFile = resultsFile;
    }

    public File getDiffFile() {
        return diffFile;
    }

    protected void setDiffFile( File diffFile ) {
        this.diffFile = diffFile;
    }

    private static NVPair[] convertHeaders( Header[] headers ) {
        if ( headers == null ) {
            return null;
        }
        NVPair[] newPairs = new NVPair[headers.length];
        NVPair newPair = null;
        for ( int i = 0; i < headers.length; i++ ) {
            newPair = convertHeader( headers[i] );
            newPairs[i] = newPair;
        }
        return newPairs;
    }

    private static NVPair convertHeader( Header header ) {
        if ( LOGGER.isDebugEnabled() ) {
            LOGGER.debug( "header, name( " + header.getName() + " ), value( " + header.getValue() + " )" );
        }
        return new NVPair( header.getName(), header.getValue() );
    }

    private static NameValuePair[] convertNVPairs( NVPair[] pairs ) {
        if ( pairs == null )
            return null;

        NameValuePair[] newPairs = new NameValuePair[pairs.length];
        NameValuePair newPair = null;
        for ( int i = 0; i < pairs.length; i++ ) {
            newPair = convertNVPair( pairs[i] );
            newPairs[i] = newPair;
        }
        return newPairs;
    }

    private static NameValuePair convertNVPair( NVPair pair ) {
        if ( LOGGER.isDebugEnabled() ) {
            LOGGER.debug( "pair, name( " + pair.getName() + " ), value( " + pair.getValue() + " )" );
        }
        return new NameValuePair( pair.getName(), pair.getValue() );
    }

    private static void debugNameValuePairs( NameValuePair[] pairs ) {
        if ( pairs == null ) {
            LOGGER.debug( "pairs( " + pairs + " )" );
            return;
        }
        for ( int i = 0; i < pairs.length; i++ ) {
            LOGGER.debug(
                    "pair(" + i + ")[ name( " + pairs[i].getName() + " ), value( " + pairs[i].getValue() + " )" );
        }
        return;
    }

}
