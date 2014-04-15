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

import java.util.Calendar;
import java.util.Date;

import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.SessionBean;


/**
 * User: ozzy
 * Date: Mar 10, 2003
 * Time: 10:33:11 AM
 */
abstract class SessionImpl implements Session {

    private static final Logger log = Logger.getInstance( SessionImpl.class );

    // milliseconds
    public static final long LOOP_TIMEOUT = 250;
    // milliseconds ... 1 minute to you and me
    public static final int HARD_TIMEOUT = 60 * 1 * 1000;

    public static final int BEGIN = 0;
    public static final int RECORD = 1;
    public static final int PLAYBACK = 2;
    public static final int STOP = 3;
    public static final int END = 4;
    public static final int ERROR = 5;
    // ended with an error
    public static final int ERROR_END = 6;

    // static
    private SessionBean sessionBean;

    // dynamic
    private int inProgress;
    private int sessionState;
    private boolean sessionClosed;

    SessionImpl( SessionBean sessionBean ) {
        this.sessionBean = sessionBean;
        inProgress = 0;
        sessionState = BEGIN;
    }

    // session has been started, new tests may or may not be startable
    public boolean isSessionStarted() {
        boolean rtnVal = false;
        if ( getSessionState() > BEGIN || getSessionState() < END ) {
            rtnVal = true;
        }
        return rtnVal;
    }

    // all tests have been persisted, no new tests may be started.
    public boolean isSessionFinished() {
        boolean rtnVal = false;
        if ( getSessionState() >= END ) {
            rtnVal = true;
        }
        return rtnVal;
    }

    // returns true if the session is successfully started, false otherwise.
    public abstract boolean sessionStart() throws SessionFailedException;

    public synchronized boolean sessionEnd() throws SessionFailedException {
        return sessionEnd( LOOP_TIMEOUT, HARD_TIMEOUT );
    }

    // TODO get rid of boolean return value
    public synchronized boolean sessionEnd( long loopTimeout, long hardTimeout ) throws SessionFailedException {
        if ( getSessionState() < ERROR ) {
            setSessionState( STOP );
        }
        int cnt = 0;
        long sTime = new Date().getTime();
        long eTime = sTime;
        while ( !isSessionFinished() ) {
            if ( log.isDebugEnabled() ) {
                log.debug( "sessionEnd(): cnt(" + cnt + "), starting loop" );
            }
            // we can only wait so long ...
            if ( ( eTime - sTime ) > hardTimeout ) {
                float milliseconds = ( eTime - sTime );
                String msg = "ERROR: session end timed out, elapsedTime( " + milliseconds + "" +
                        "milliseconds ), inProgress( " + inProgressCnt() + " )";
                log.error( msg );
                throw new SessionFailedException( msg );
            }
            cnt++;
            if ( cnt != 1 ) {
                try {
                    if ( log.isDebugEnabled() ) {
                        log.debug( "sessionEnd(): cnt(" + cnt + "), waiting ..." );
                    }
                    wait( loopTimeout );
                }
                catch ( InterruptedException ex ) {
                    // do nothing.
                }
            }
            // cleans up the session if getSessionState() == STOP and inProgressCnt() == 0
            if ( getSessionState() == STOP && inProgressCnt() == 0 || getSessionState() >= ERROR ) {
                if ( getSessionState() == ERROR ) {
                    setSessionState( ERROR_END );
                }
                else {
                    setSessionState( END );
                }
                closeSession();
            }
            eTime = new Date().getTime();
        }
        return true;
    }

    /**
     * indicates the number of tests executed by this session.
     *
     * @return
     */
    public abstract int testCount();

    public String getStartDateString() {
        return sessionBean.getStartDateString();
    }

    public String getEndDateString() {
        return sessionBean.getEndDateString();
    }

    public String getSessionName() {
        return sessionBean.getSessionName();
    }

    public String getTestUser() {
        return sessionBean.getTester();
    }

    public String getDescription() {
        return sessionBean.getDescription();
    }

    protected int getSessionState() {
        return sessionState;
    }

    protected int inProgressCnt() {
        return inProgress;
    }

    // called by synchronized methods
    protected synchronized void incrementInProgress() {
        inProgress++;
        if ( log.isDebugEnabled() ) {
            log.debug( "inProgress( " + inProgress + " )" );
        }
    }

    // called by synchronized methods
    protected synchronized void decrementInProgress() {
        inProgress--;
        if ( log.isDebugEnabled() ) {
            log.debug( "inProgress( " + inProgress + " )" );
        }
    }

    protected SessionBean getSessionBean() {
        return sessionBean;
    }

    protected synchronized void closeSession() throws SessionFailedException {
        if ( log.isDebugEnabled() ) {
            log.debug( "closing session( " + getSessionName() + " ), testCount( " + testCount() + " )" );
        }
        if ( sessionClosed == true ) {
            return;
        }
        sessionClosed = true;
        closeSessionInternal();
    }

    protected abstract void closeSessionInternal() throws SessionFailedException;

    // called by synchronized methods
    protected synchronized void checkSessionComplete() throws SessionFailedException {
        if ( log.isDebugEnabled() ) {
            log.debug( "checkSessionComplete(): inProgressCnt(" + inProgressCnt() + "), sessionState(" +
                    getSessionState() + ")" );
        }
        if ( ( getSessionState() == STOP && inProgressCnt() == 0 ) || getSessionState() >= ERROR ) {
            if ( getSessionState() == ERROR ) {
                setSessionState( ERROR_END );
            }
            else {
                setSessionState( END );
            }
            // a thread may be waiting for the session to complete
            notifyAll();
            // clean up the session
            // may throw IOException
            try {
                closeSession();
            }
            catch ( Exception e ) {
                String msg = "Failed to close session( " + getSessionName() + " ), exception( " +
                        e.getMessage() + " )";
                log.error( msg, e );
                throw new SessionFailedException( msg, e );
            }
        }
    }

    protected void setStartDate( Calendar startDate ) {
        sessionBean.setStartDate( startDate );
    }

    protected void setEndDate( Calendar endDate ) {
        sessionBean.setEndDate( endDate );
    }

    protected void setTestUser( String testUser ) {
        sessionBean.setTester( testUser );
    }

    protected void setDescription( String description ) {
        sessionBean.setDescription( description );
    }

    protected void setSessionState( int sessionState ) {
        this.sessionState = sessionState;
    }

    protected void error( String msg ) throws SessionFailedException {
        // recoverable error
        error( msg, null );
    }

    protected void error( String msg, Exception e ) throws SessionFailedException {
        // recoverable error
        error( msg, e, false );
    }

    protected void error( String msg, Exception e, boolean unrecoverable ) throws SessionFailedException {
        if ( e == null ) {
            log.error( msg );
        }
        else {
            log.error( msg, e );
        }
        if ( unrecoverable ) {
            setSessionState( ERROR_END );
            closeSession();
        }
        if ( e == null ) {
            throw new SessionFailedException( msg );
        }
        throw new SessionFailedException( msg, e );
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 128 );
        sb.append( "[ " );
        sb.append( "sessionName( " + getSessionName() + " )" );
        sb.append( ", startDate( " + sessionBean.getStartDateString() + " )" );
        sb.append( ", endDate( " + sessionBean.getEndDateString() + " )" );
        sb.append( ", testUser( " + getTestUser() + " )" );
        sb.append( ", description( " + getDescription() + " )" );
        sb.append( ", state( " + getSessionState() + " )" );
        sb.append( ", testCount( " + testCount() + " )" );
        sb.append( ", inProgress( " + inProgressCnt() + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
