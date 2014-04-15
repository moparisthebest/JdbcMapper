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

import org.apache.beehive.netui.tools.testrecorder.shared.RequestData;
import org.apache.beehive.netui.tools.testrecorder.shared.ResponseData;
import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.RecordSessionBean;
import org.apache.beehive.netui.tools.testrecorder.shared.util.DateHelper;
import org.apache.beehive.netui.tools.testrecorder.shared.xmlbeans.XMLHelper;


class RecordSessionImpl extends SessionImpl implements RecordSession {

    private static final Logger log = Logger.getInstance( RecordSessionImpl.class );

    private File recordFile;
    private RecordSessionBean sessionBean;

    RecordSessionImpl( String sessionName, File recordFile, boolean overwrite,
            String testUser, String description ) throws SessionFailedException {
        super( new RecordSessionBean( sessionName ) );
        sessionBean = (RecordSessionBean) getSessionBean();
        setTestUser( testUser );
        setDescription( description );
        this.recordFile = recordFile;
        if ( getSessionName() == null || getSessionName().trim().length() <= 0 ) {
            String msg = "the session name supplied( " +
                    getSessionName() + " ) is invalid, it must be a non-null string of non-zero length";
            error( msg );
        }
        if ( recordFile == null ) {
            String msg = "the record file may not be null";
            error( msg );
        }
        if ( recordFile.exists() ) {
            if ( overwrite == false ) {
                String msg = "ERROR: failed to create recording session, file( " +
                        recordFile.getAbsolutePath() + " ) exists and overwrite is false";
                error( msg );
            }
            else if ( !recordFile.canWrite() ) {
                // overwrite is true ... file is not writable.
                String msg = "ERROR: failed to create recording session, file( " +
                        recordFile.getAbsolutePath() + " ) exists but is not writable";
                error( msg );
            }
            else {
                // overwrite is true and file is writable.
                try {
                    recordFile.delete();
                }
                catch ( Exception ex ) {
                    String msg = "unable to delete file( " + recordFile.getAbsolutePath() + " )";
                    error( msg, ex );
                }
            }
        }
        // file doesn't exist
        try {
            if ( !recordFile.createNewFile() ) {
                String msg = "unable to create new record file( " + recordFile.getAbsolutePath() +
                        " ) for session( " + getSessionName() + " )";
                error( msg );
            }
        }
        catch ( Exception ex ) {
            String msg = "unable to create new record file( " +
                    recordFile.getAbsolutePath() + " ) for session( " + getSessionName() + " )";
            error( msg, ex );
        }
        setSessionState( BEGIN );
    }

    public synchronized boolean sessionStart() throws SessionFailedException {
        boolean rtnVal = false;
        if ( getSessionState() != BEGIN ) {
            rtnVal = false;
        }
        else {
            setStartDate( DateHelper.getCalendarInstance() );
            setSessionState( RECORD );
            rtnVal = true;
        }
        return rtnVal;
    }

    public synchronized boolean startTest() throws SessionFailedException {
        boolean rtnVal = false;
        if ( getSessionState() == RECORD ) {
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

    // returns the current test count
    public synchronized int endTest( RequestData request, ResponseData response ) throws SessionFailedException {
        if ( getSessionState() >= ERROR ) {
            String msg = "Unable to end test, Session has encountered a previous unrecoverable error";
            error( msg, null, true );
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
        if ( request == null ) {
            // unrecoverable, test data cannot be persisted
            String msg = "request data may not be null, test count(" + ( testCount() + 1 ) + ")";
            error( msg, null, true );
        }
        else if ( response == null ) {
            // unrecoverable, test data cannot be persisted
            String msg = "response data may not be null, test count(" + ( testCount() + 1 ) + ")";
            error( msg, null, true );
        }
        decrementInProgress();
        sessionBean.addRequestResponseData( request, response );
        // cleans up the session if getSessionState() == STOP and inProgressCnt() == 0
        checkSessionComplete();
        if ( log.isDebugEnabled() ) {
            log.debug( "testCount( " + testCount() + " )" );
        }
        return testCount();
    }

    protected synchronized void closeSessionInternal() throws SessionFailedException {
        try {
            setEndDate( DateHelper.getCalendarInstance() );
            XMLHelper.createRecordFile( getRecordFile(), sessionBean );
        }
        catch ( Exception e ) {
            setSessionState( ERROR_END );
            String msg = "ERROR: failed to create record XML file( " + getRecordFile().getAbsolutePath() +
                    " ), exception( " + e.getMessage() + " )";
            log.error( msg, e );
            throw new SessionFailedException( msg, e );
        }
    }

    public File getRecordFile() {
        return recordFile;
    }

    public int testCount() {
        return sessionBean.getTestCount();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 128 );
        sb.append( "[ " );
        sb.append( super.toString() );
        sb.append( ", recordFile( " + recordFile + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
