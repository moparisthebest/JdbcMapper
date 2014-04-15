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


/**
 * User: ozzy
 * Date: Mar 10, 2003
 * Time: 10:10:37 AM
 */
public interface Session {

    /**
     * returns true if session started, false if the session is already started.  An excepion is thrown
     * if an error occurs
     *
     * @return
     * @throws SessionFailedException
     */
    boolean sessionStart() throws SessionFailedException;

    /**
     * Returns true if the session was ended or throws an exception if an error occured or the session
     * failed to end.
     * @return
     * @throws SessionFailedException
     */
    boolean sessionEnd() throws SessionFailedException;

    /**
     * Returns true if the session was ended or throws an exception if an error occured or the session
     * failed to end.
     *
     * @param loopTimeout wait time before checking to see if any running tests have completed.
     * @param hardTimeout wait time before giving up on any running tests.
     * @return
     * @throws SessionFailedException
     */
    boolean sessionEnd( long loopTimeout, long hardTimeout ) throws SessionFailedException;

    String getSessionName();

    String getTestUser();

    int testCount();

    // session has been started, new tests may or may not be startable
    boolean isSessionStarted();

    // all tests have been persisted, no new tests may be started.
    boolean isSessionFinished();

}
