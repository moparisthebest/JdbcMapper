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
package org.apache.beehive.netui.tools.testrecorder.server.state;

import java.io.File;
import java.rmi.server.UID;
import java.lang.StringBuffer;

import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.RecordSessionBean;
import org.apache.beehive.netui.tools.testrecorder.shared.PlaybackSessionBean;
import org.apache.beehive.netui.tools.testrecorder.shared.xmlbeans.XMLHelper;
import org.apache.beehive.netui.tools.testrecorder.shared.TestResults;
import org.apache.beehive.netui.tools.testrecorder.shared.util.DateHelper;
import org.apache.beehive.netui.tools.testrecorder.server.DiffEngineFactory;
import org.apache.beehive.netui.tools.testrecorder.server.FilterData;
import org.apache.beehive.netui.tools.testrecorder.server.DiffEngine;

class PlaybackSessionImpl
    extends SessionImpl
    implements PlaybackSession {

    private static final Logger LOGGER = Logger.getInstance( PlaybackSessionImpl.class );

    private UID uid;
    private String stringUid;
    private File recordFile;
    private File playbackFile;
    private File diffFile;

    /* record bean is read only */
    private RecordSessionBean recordSessionBean;
    private PlaybackSessionBean playbackSessionBean;

    PlaybackSessionImpl(String sessionName,
                        File playbackFile,
                        File recordFile,
                        File diffFile)
        throws SessionFailedException {

        super( new PlaybackSessionBean( sessionName ) );
        playbackSessionBean = (PlaybackSessionBean) getSessionBean();
        if ( sessionName == null || sessionName.trim().length() <= 0 ) {
            error( "the session name supplied( " + sessionName +
                    " ) is invalid, it must be a non-null string of non-zero length" );
        }
        if ( playbackFile == null || diffFile == null ) {
            error( "the playback and diff files may not be null" );
        }
        uid = new UID();
        stringUid = uid.toString();
        this.playbackFile = playbackFile;
        this.diffFile = diffFile;
        this.recordFile = recordFile;
        if ( !recordFile.exists() )
            error( "record file( " + recordFile.getAbsolutePath() + " ) does NOT exist" );

        if ( !recordFile.canRead() )
            error( "record file( " + recordFile.getAbsolutePath() + " ) is not readable" );

        recordSessionBean = parseRecordFile( recordFile );

        playbackSessionBean.setRecordedTestCount( recordSessionBean.getTestCount() );
        if ( playbackSessionBean.getTester() == null || playbackSessionBean.getTester().length() == 0 ) {
            if ( recordSessionBean.getTester() == null || recordSessionBean.getTester().length() == 0 ) {
                playbackSessionBean.setTester( "unknown" );
            }
            playbackSessionBean.setTester( recordSessionBean.getTester() );
        }
        if ( playbackSessionBean.getDescription() == null || playbackSessionBean.getDescription().length() == 0 ) {
            if ( recordSessionBean.getDescription() == null ||
                    recordSessionBean.getDescription().length() == 0 ) {
                playbackSessionBean.setDescription( "unknown" );
            }
            playbackSessionBean.setDescription( recordSessionBean.getTester() );
        }
        // if a version of this file exists, kill it
        if ( LOGGER.isDebugEnabled() ) {
            LOGGER.debug( "playbackFile( " + playbackFile.getAbsolutePath() + " )" );
        }
        if ( playbackFile.exists() ) {
            boolean rtnVal = playbackFile.delete();
            if ( rtnVal ) {
                if ( LOGGER.isDebugEnabled() ) {
                    LOGGER.debug( "playback file exists, deleted(" + rtnVal + ")" );
                }
            }
            else {
                error( "unable to delete playback file( " + playbackFile.getAbsolutePath() + " )" );
            }
        }
        if ( diffFile.exists() ) {
            boolean rtnVal = diffFile.delete();
            if ( rtnVal ) {
                if ( LOGGER.isDebugEnabled() ) {
                    LOGGER.debug( "diff file exists, deleted(" + rtnVal + ")" );
                }
            }
            else {
                error( "unable to delete diff file( " + diffFile.getAbsolutePath() + " )" );
            }
        }
        // file doesn't exist
        try {
            if ( !playbackFile.createNewFile() ) {
                error( "unable to create new playback file( " + playbackFile.getAbsolutePath() + " )" );
            }
        }
        catch ( Exception ex ) {
            error( "unable to create new playback file( " + playbackFile.getAbsolutePath() + " )", ex );
        }
        setSessionState( BEGIN );
    }

    public String getStatus() {
        return playbackSessionBean.getStatus();
    }

    public synchronized boolean startTest() throws SessionFailedException {
        boolean rtnVal = false;
        if ( getSessionState() == PLAYBACK ) {
            incrementInProgress();
            rtnVal = true;
        }
        else if ( getSessionState() == BEGIN ) {
            String msg = "Unable to start test, session must be started before starting a test";
            error( msg );
        }
        else if ( getSessionState() == END ) {
            String msg = "Unable to start test, session is finished, no new tests may be started";
            error( msg );
        }
        else if ( getSessionState() >= ERROR ) {
            String msg = "Unable to start test, Session has encountered a previous unrecoverable error";
            error( msg );
        }
        else {
            // getSessionState() == STOP ... return false, no new tests
        }
        return rtnVal;
    }

    // returns the current test count or -1 if endTest() fails
    public synchronized int endTest( FilterData filterData ) throws SessionFailedException {
        if ( getSessionState() >= ERROR ) {
            String msg = "Unable to end test, Session has encountered a previous unrecoverable error";
            error( msg );
        }
        if ( getSessionState() == BEGIN ) {
            String msg = "Session must be started before using test operations";
            error( msg );
        }
        else if ( getSessionState() == END ) {
            String msg = "Session is finished, no test operations may be performed";
            error( msg );
        }
        if ( inProgressCnt() == 0 ) {
            // no tests in progress to end.
            String msg = "No session in progress to end";
            error( msg );
        }
        else if ( inProgressCnt() != 1 ) {
            // programming error, unrecoverable
            String msg = "inProgress( " + inProgressCnt() + " ) is invalid";
            error( msg, null, true );
        }
        // write to record file.
        if ( filterData.getReqData() == null ) {
            // unrecoverable, test data cannot be persisted
            String msg = "request data may not be null, test count(" + ( testCount() + 1 ) + ")";
            error( msg, null, true );
        }
        else if ( filterData.getRespData() == null ) {
            // unrecoverable, test data cannot be persisted
            String msg = "response data may not be null, test count(" + ( testCount() + 1 ) + ")";
            error( msg, null, true );
        }
        decrementInProgress();
        playbackSessionBean.addRequestResponseData( filterData.getReqData(), filterData.getRespData() );
        // do diff work ... get TestResults
        TestResults results = new TestResults( testCount() + 1, filterData.getReqData().getPath() );
        // add test and session exceptions to diff results.
        Throwable throwable = null;
        for ( int i = 0; i < filterData.getTestExceptionCount(); i++ ) {
            throwable = filterData.getTestException( i );
            results.addDiffResult( "encountered test exception(" + i + ")( " +
                    Logger.format( throwable, throwable ) + " )" );
        }
        for ( int i = 0; i < filterData.getSessionExceptionCount(); i++ ) {
            throwable = filterData.getSessionException( i );
            results.addDiffResult( "encountered test recorder session exception(" + i + ")( " +
                    Logger.format( throwable, throwable ) + " )" );
        }
        try {
            DiffEngine engine = DiffEngineFactory.getInstance( filterData );

            String resp = filterData.getRespData().getBody();
            String body = null;
            // replace the CDATA in the body. We escape the CDATA so we can nest this.
            int pos = resp.indexOf("<![CDATA[");
            if (pos != -1) {
                body = resp;
                resp = resp.replaceAll("\\Q<![CDATA[\\E","&lt;![CDATA[");
                resp = resp.replaceAll("\\Q]]>","]]&gt;");
                filterData.getRespData().setBody(resp);
            }
            results = engine.diff( recordSessionBean.getRequestData( testCount() ),
                    recordSessionBean.getResponseData( testCount() ),
                    filterData.getReqData(), filterData.getRespData(), results );
            if (body != null) {
                filterData.getRespData().setBody(body);
            }
        }
        catch ( Exception e ) {
            String msg = "Failed diffing results for session( " + getSessionName() +
                    " ), test count(" + testCount() + "), exception( " + e.getMessage() + " )";
            results.addDiffResult( "Test Recorder ERROR: " + msg, true );
            results.addDiffResult( Logger.format( "EXCEPTION: ", e ) );
            error( msg, e, true );
        }
        finally {
            playbackSessionBean.addTestResults( results );
            // cleans up the session if getSessionState() == STOP and inProgressCnt() == 0
            checkSessionComplete();
        }
        if ( LOGGER.isDebugEnabled() ) {
            LOGGER.debug( "testCount( " + testCount() + " )" );
        }
        return testCount();
    }

    // returns true if the session is successfully started, false otherwise.
    public synchronized boolean sessionStart() throws SessionFailedException {
        boolean rtnVal = false;
        if ( getSessionState() != BEGIN ) {
            rtnVal = false;
        }
        else {
            setStartDate( DateHelper.getCalendarInstance() );
            setSessionState( PLAYBACK );
            rtnVal = true;
        }
        return rtnVal;
    }

    protected synchronized void closeSessionInternal() throws SessionFailedException {
        if ( LOGGER.isDebugEnabled() ) {
            LOGGER.debug( "closing session file ( " + getPlaybackFile() + " ), session status( " +
                    playbackSessionBean.getStatus() +
                    " )" );
        }
        SessionFailedException caught = null;
        try {
            setEndDate( DateHelper.getCalendarInstance() );
            XMLHelper.createPlaybackFile( getPlaybackFile(), playbackSessionBean );
        }
        catch ( Exception e ) {
            setSessionState( ERROR_END );
            String msg = "ERROR: failed to create playback XML file( " + getPlaybackFile().getAbsolutePath() +
                    " ), exception( " + e.getMessage() + " )";
            LOGGER.error( msg, e );
            caught = new SessionFailedException( msg, e );
        }
        if ( playbackSessionBean.getFailedCount() > 0 || playbackSessionBean.isError() ) {
            // test failed, write diff data
            try {
                if ( !diffFile.createNewFile() ) {
                    error( "unable to create new diff file( " + diffFile.getAbsolutePath() + " )" );
                }
                XMLHelper.createDiffFile( getDiffFile(), playbackSessionBean );
            }
            catch ( Exception e ) {
                setSessionState( ERROR_END );
                String msg = "ERROR: failed to create diff XML file( " + getDiffFile().getAbsolutePath() +
                        " ), exception( " + e.getMessage() + " )";
                LOGGER.error( msg, e );
                // don't hide previous error
                if ( caught == null ) {
                    caught = new SessionFailedException( msg, e );
                }
            }
        }
        if ( caught != null ) {
            throw caught;
        }
    }

    public String getStringUID() {
        return stringUid;
    }

    public File getPlaybackFile() {
        return playbackFile;
    }

    public File getDiffFile() {
        return diffFile;
    }

    public File getRecordFile() {
        return recordFile;
    }

    public int passCount() {
        return playbackSessionBean.getPassedCount();
    }

    public int failCount() {
        return playbackSessionBean.getFailedCount();
    }

    public int testCount() {
        return playbackSessionBean.getTestCount();
    }

    private RecordSessionBean parseRecordFile(File recordFile)
        throws SessionFailedException {

        RecordSessionBean sessionBean = null;
        try {
            sessionBean = XMLHelper.getRecordSessionBean( recordFile );
        }
        catch ( Exception e ) {
            String msg = "Exception parsing record file(" + recordFile + "), exception(" + e.getMessage() +")";
            error( msg, e );
        }
        return sessionBean;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 128 );
        sb.append( "[ " );
        sb.append( "sessionName( " + getSessionName() + " )" );
//        sb.append( ", status( " + getStatus() + " )" );
        sb.append( ", uid( " + uid + " )" );
        sb.append( ", startDate( " + getStartDateString() + " )" );
        sb.append( ", endDate( " + getEndDateString() + " )" );
        sb.append( ", playbackFile( " + playbackFile + " )" );
        sb.append( ", recordFile( " + recordFile + " )" );
        sb.append( ", testUser( " + getTestUser() + " )" );
        sb.append( ", recordDesc( " + recordSessionBean.getDescription() + " )" );
        sb.append( ", state( " + getSessionState() + " )" );
        sb.append( ", testCount( " + testCount() + " )" );
        sb.append( ", recorded test count( " + playbackSessionBean.getRecordedTestCount() + " )" );
        sb.append( ", passed( " + playbackSessionBean.getPassedCount() + " )" );
        sb.append( ", failed( " + playbackSessionBean.getFailedCount() + " )" );
        sb.append( ", inProgress( " + inProgressCnt() + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
