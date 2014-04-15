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

import org.apache.beehive.netui.tools.testrecorder.server.state.PlaybackSession;
import org.apache.beehive.netui.tools.testrecorder.server.state.SessionFailedException;
import org.apache.beehive.netui.tools.testrecorder.server.state.RecordSession;
import org.apache.beehive.netui.tools.testrecorder.shared.RequestData;
import org.apache.beehive.netui.tools.testrecorder.shared.ResponseData;

import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * User: ozzy
 * Date: Jul 8, 2004
 * Time: 3:28:25 PM
 */
public interface FilterData {

    void init() throws SessionFailedException;

    ServletRequest getRequest();

    ServletResponse getResponse();

    ServletResponse getNewResponse();

    String getReqURI();

    boolean isNewRequest();

    boolean isSkipFilter();

    public boolean isTestException();

    String getTestId();

    boolean isPlayback();

    PlaybackSession getPlaybackSession();

    void setPlaybackSession( PlaybackSession playbackSession );

    boolean isRecording();

    boolean isNewRecording();

    RecordSession getRecordingSession();

    void setRecordingSession( RecordSession recordSession );

    void clearRecording();

    RequestData getReqData();

    ResponseData getRespData();

    void setRespData( boolean replaceSessionId ) throws SessionFailedException;

    void addTestException( Throwable e );

    int getTestExceptionCount();

    /**
     * return the first test exception if one exists.
     *
     * @return the first test exception or null if no exceptions exist
     */
    Throwable getTestException();

    Throwable getTestException( int i );

    void addSessionException( Throwable e );

    int getSessionExceptionCount();

    Throwable getSessionException( int i );

    /**
     * rethrow the first test exception if it exists.
     *
     * @throws IOException
     * @throws ServletException
     */
    void throwTestException() throws IOException, ServletException;

}
