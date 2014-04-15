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

import org.apache.beehive.netui.tools.testrecorder.shared.RequestData;
import org.apache.beehive.netui.tools.testrecorder.shared.ResponseData;

import java.io.File;


/**
 * User: ozzy
 * Date: Mar 7, 2003
 * Time: 12:06:23 PM
 */
public interface RecordSession extends Session {

    /**
     * @return true if successful starting the test, false if not tests remain, throws
     *         an exception if a failure occurs.
     * @throws SessionFailedException
     */
    boolean startTest() throws SessionFailedException;

    /**
     * @param request
     * @param response
     * @return returns the current test count or throws an exception if it fails.
     * @throws SessionFailedException
     */
    int endTest( RequestData request, ResponseData response )
            throws SessionFailedException;

    File getRecordFile();

}
