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
package org.apache.beehive.netui.tools.testrecorder.shared;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 */
public class PlaybackSessionBean
    extends RecordSessionBean {

    private static final Logger LOGGER = Logger.getInstance(PlaybackSessionBean.class);

    private int _recordedTestCount = -1;
    private int _passedCount = 0;
    private int _failedCount = 0;
    private List _testResults;

    public PlaybackSessionBean(String sessionName) {
        this(sessionName, false);
    }

    public PlaybackSessionBean(String sessionName, boolean error) {
        super(sessionName);
        setError(error);
        _testResults = new ArrayList();
    }

    public int getRecordedTestCount() {
        return _recordedTestCount;
    }

    public void setRecordedTestCount(int recordedTestCount) {
        _recordedTestCount = recordedTestCount;
    }

    public int getTestCount() {
        return _testResults.size();
    }

    public TestResults getTestResults(int index) {
        return (TestResults) _testResults.get(index);
    }

    public List getTestResults() {
        return Collections.unmodifiableList(_testResults);
    }

    public void addTestResults(TestResults results) {
        if (results.isTestPassed()) {
            incrementPassedCount();
        }
        else {
            incrementFailedCount();
        }
        _testResults.add(results);
    }

    public boolean isError() {
        return _error ? _error : isErrorInternal();
    }

    private boolean isErrorInternal() {
        boolean rtnVal = false;
        if (getRecordedTestCount() < 1) {
            LOGGER.debug("Invalid recorded test count(" + getRecordedTestCount() + ")");
            rtnVal = true;
        }
        else if ((getTestCount()) != getRecordedTestCount()) {
            LOGGER.debug("executed count(" + getTestCount() + ") does not equal recorded test count(" + getRecordedTestCount() + ")");
            rtnVal = true;
        }
        return rtnVal;
    }

    public String getStatus() {
        if (isError()) {
            return Constants.ERROR;
        }
        if (isSessionPassed()) {
            return Constants.PASS;
        }
        return Constants.FAIL;
    }

    public boolean isSessionPassed() {
        if (isError())
            return false;

        return _passedCount == getRecordedTestCount();
    }

    public int incrementPassedCount() {
        return ++_passedCount;
    }

    public int getPassedCount() {
        return _passedCount;
    }

    public int incrementFailedCount() {
        return ++_failedCount;
    }

    public int getFailedCount() {
        return _failedCount;
    }

}
