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

import org.apache.beehive.netui.tools.testrecorder.shared.Util;
import org.apache.beehive.netui.tools.testrecorder.shared.Logger;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import java.io.Serializable;
import java.io.File;


/**
 * User: ozzy
 * Date: Mar 6, 2003
 * Time: 3:34:14 PM
 */
public class State implements Serializable {

    private static final Logger log = Logger.getInstance( State.class );

    private boolean testMode;
    // needs to be serialized
    private RecordSession recordSession = null;
    // needs to be serialized
    // map of PlaybackSessions
    private Map playbackMap;


    public State() {
        if ( log.isInfoEnabled() ) {
            log.info( "Constructor" );
        }
        playbackMap = new HashMap();
    }

    public boolean isTestMode() {
        return testMode;
    }

    private void setTestMode() {
        setTestMode( true );
    }

    public void setTestMode( boolean testMode ) {
        this.testMode = testMode;
    }

    public boolean isRecording() {
        boolean rtnVal = false;
        if ( getRecordingSession() != null ) {
            rtnVal = true;
        }
        return rtnVal;
    }

    public RecordSession getRecordingSession() {
        return recordSession;
    }

    // return true if a new session has been started.
    // if already in record mode false will be returned.
    // Use record stop before starting a new session.
    public boolean recordStart( RecordSession session ) throws SessionFailedException {
        boolean rtnVal = false;
        if ( isRecording() == true ) {
            rtnVal = false;
        }
        else if ( session == null ) {
            String msg = "the RecordSession may not be null";
            log.error( msg );
            throw new SessionFailedException( msg );
        }
        else {
            synchronized ( this ) {
                if ( isRecording() == true ) {
                    rtnVal = false;
                }
                else {
                    rtnVal = session.sessionStart();
                    if ( rtnVal ) {
                        setRecordingSession( session );
                        setTestMode();
                    }
                }
            }
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "rtnVal( " + rtnVal + " )" );
            log.debug( "recording( " + isRecording() + " )" );
            log.debug( "recordSession( " + getRecordingSession() + " )" );
        }
        return rtnVal;
    }

    // return the session or null if no recording session was in progress
    public RecordSession recordStop() throws SessionFailedException {
        RecordSession rtnVal = null;
        if ( isRecording() == false ) {
            rtnVal = null;
        }
        else {
            synchronized ( this ) {
                if ( isRecording() == false ) {
                    rtnVal = null;
                }
                else {
                    // may throw a RuntimeException if the session fails to end.
                    // this method will wait for a timeout specified in the RecordSession class.
                    try {
                        rtnVal = getRecordingSession();
                        rtnVal.sessionEnd();
                    }
                    catch ( Exception ex ) {
                        String msg = "ERROR: Failed while stopping recording session( " +
                                getRecordingSession().getSessionName() + " )";
                        log.error( msg, ex );
                        if ( ex instanceof SessionFailedException ) {
                            throw (SessionFailedException) ex;
                        }
                        throw new SessionFailedException( msg, ex );
                    }
                    finally {
                        removeRecordingSession();
                    }
                }
            }
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "recording( " + isRecording() + " )" );
            log.debug( "rtnVal( " + rtnVal + " )" );
        }
        return rtnVal;
    }

    public PlaybackSession getPlaybackSession( String testId ) {
        return (PlaybackSession) getInternalPlaybackMap().get( testId );
    }

    public Map getPlaybackMap() {
        if ( playbackMap.size() == 0 ) {
            return null;
        }
        // playback sessions only modifiable by classes in testrecorder package.
        // !!! don't modify them without understanding the consequences!!!
        return Collections.unmodifiableMap( playbackMap );
    }

    public boolean playbackSessionExists( PlaybackSession session ) {
        return playbackSessionExists( session.getStringUID() );
    }

    public boolean playbackSessionExists( String testId ) {
        if ( getPlaybackMap() == null ) {
            return false;
        }
        return getInternalPlaybackMap().containsKey( testId );
    }

    // return true if a new session has been started.
    // if this session is already in playback mode, return false.
    // Use playback stop before starting a session.
    public boolean playbackStart( PlaybackSession session ) throws SessionFailedException {
        boolean rtnVal = false;
        if ( session == null ) {
            String msg = "the PlaybackSession may not be null";
            log.fatal( msg );
            throw new SessionFailedException( msg );
        }
        else {
            if ( log.isDebugEnabled() ) {
                log.debug( "session( " + session + " )" );
            }
            rtnVal = session.sessionStart();
            if ( rtnVal ) {
                addPlaybackSession( session );
                setTestMode();
            }
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "rtnVal( " + rtnVal + " )" );
        }
        return rtnVal;
    }

    // return the session or null if the playback session was NOT in progress
    public PlaybackSession playbackStop( String testId ) throws SessionFailedException {
        PlaybackSession rtnVal = getPlaybackSession( testId );
        // may throw a RuntimeException if the session fails to end.
        // this method will wait for a timeout specified in the PlaybackSession class.
        if ( rtnVal != null ) {
            try {
                if ( rtnVal.sessionEnd() == false ) {
                    if ( log.isWarnEnabled() ) {
                        log.warn( "session failed to end( " + rtnVal + " )" );
                    }
                }
            }
            finally {
                removePlaybackSession( rtnVal );
            }
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "rtnVal( " + rtnVal + " )" );
        }
        return rtnVal;
    }

    public synchronized void stopAll() {
        if ( log.isDebugEnabled() ) {
            log.debug( "stopAll(): START ..." );
        }
        if ( getRecordingSession() != null ) {
            if ( log.isDebugEnabled() ) {
                log.debug( "stopAll(): stopping record session ..." );
            }
            try {
                recordStop();
            }
            catch ( Exception e ) {
                log.error( e );
            }
            if ( log.isDebugEnabled() ) {
                log.debug( "stopAll(): .... record session stopped" );
            }
        }
        if ( getInternalPlaybackMap() != null ) {
            if ( log.isDebugEnabled() ) {
                log.debug( "stopAll(): stopping playback sessions, session count( " +
                        getInternalPlaybackMap().size() + " )" );
            }
            Iterator it = Util.getListOfKeys( getInternalPlaybackMap() ).iterator();
            String key = null;
            while ( it.hasNext() ) {
                key = (String) it.next();
                if ( log.isDebugEnabled() ) {
                    log.debug( "stopAll(): stopping playback session with key( " + key + " ) ..." );
                }
                try {
                    playbackStop( key );
                }
                catch ( Exception e ) {
                    log.error( e );
                }
                if ( log.isDebugEnabled() ) {
                    log.debug( "stopAll(): ... playback session stopped" );
                }
            }
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "stopAll(): ... END" );
        }
    }

    public static PlaybackSession createPlaybackSession(String sessionName,
                                                        File playbackFile,
                                                        File recordFile,
                                                        File diffFile )
            throws SessionFailedException {
        return new PlaybackSessionImpl( sessionName, playbackFile, recordFile, diffFile );
    }

    public static RecordSession createRecordSession(String sessionName,
                                                    File recordFile,
                                                    boolean overwrite,
                                                    String testUser,
                                                    String description)
        throws SessionFailedException {

        return new RecordSessionImpl(sessionName, recordFile, overwrite, testUser, description);
    }

    boolean addPlaybackSession( PlaybackSession session ) {
        boolean rtnVal = false;
        synchronized ( playbackMap ) {
            if ( playbackSessionExists( session ) ) {
                rtnVal = false;
            }
            else {
                getInternalPlaybackMap().put( session.getStringUID(), session );
                rtnVal = true;
            }
        }
        return rtnVal;
    }

    PlaybackSession removePlaybackSession( PlaybackSession session ) {
        return removePlaybackSession( session.getStringUID() );
    }

    PlaybackSession removePlaybackSession( String testId ) {
        PlaybackSession rtnVal;
        synchronized ( playbackMap ) {
            if ( playbackSessionExists( testId ) ) {
                // returns the value and removes the key.
                rtnVal = (PlaybackSession) playbackMap.remove( testId );
            }
            else {
                rtnVal = null;
            }
        }
        return rtnVal;
    }

    protected RecordSession removeRecordingSession() {
        RecordSession session = getRecordingSession();
        setRecordingSession( null );
        return session;
    }

    protected void setRecordingSession( RecordSession recordSession ) {
        this.recordSession = recordSession;
    }

    protected Map getInternalPlaybackMap() {
        return playbackMap;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 128 );
        sb.append( "[ " );
        sb.append( "recording( " + isRecording() + " )" );
        sb.append( ", recordSession( " + getRecordingSession() + " )" );
        sb.append( ", playbackMap( " + Util.toString( playbackMap ) + " )" );
        sb.append( " ]" );
        return sb.toString();
    }
}
