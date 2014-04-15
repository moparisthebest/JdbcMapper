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

/**
 *
 */
public final class Constants {

    /* files in the classpath obtained via the classloader */
    public static String CONFIG_FILE = "testRecorder-config.xml";
    public static String TESTS_FILE = "testRecorder-tests.xml";
    public static String WEBAPPS_FILE = "testRecorder-webapp.xml";
    public static String SERVER_FILE = "testRecorder-server.xml";

    public static String SCHEMA_LOCATION = "org/apache/beehive/netui/tools/testrecorder/shared/schemas";

    public static String CONFIG_SCHEMA_NAME = "testRecorderDefaultConfig.xsd";
    public static String DIFF_SESSION_SCHEMA_NAME = "testRecorderDiffSession.xsd";
    public static String SERVER_SCHEMA_NAME = "testRecorderServer.xsd";
    public static String SESSION_SCHEMA_NAME = "testRecorderSession.xsd";
    public static String TESTS_SCHEMA_NAME = "testRecorderTests.xsd";
    public static String WEBAPPS_SCHEMA_NAME = "testRecorderWebapps.xsd";

    public static String FILE_TYPE_CONFIG = "config";
    public static String FILE_TYPE_WEBAPP = "webapp";
    public static String FILE_TYPE_TESTS = "tests";

    // playback test number
    public static final String TEST_NUMBER_HEADER = "testRecorder.playback.testNumber";
    // playback session id.
    public static final String TEST_ID_HEADER = "testRecorder.playback.testID";
    // describes the absolute path to the playback file to be used for the session.
    // returned as a response header on playback start.
    public static final String RECORD_FILE_HEADER = "testRecorder.playback.recordFile";
    // describes the absolute path to the playback results file
    public static final String RESULTS_FILE_HEADER = "testRecorder.playback.resultsFile";
    // describes the absolute path to the diff file
    // NOTE: This may not be set if the file does not exist
    public static final String RESULTS_DIFF_HEADER = "testRecorder.playback.resultsDiffFile";
    // request header sent by PlaybackExecutor indicating whether to continue on
    // failure with subsequent tests or to stop processing tests.
    public static final String FAIL_MODE_HEADER = "testRecorder.playback.failMode";
    // response header to indicate the outcome of a playback request.
    public static final String OUTCOME_HEADER = "testRecorder.playback.outcome";
    // skip processing through the filter, used as both a parameter and a request header
    public static final String FILTER_SKIP_PARAM = "testRecorder.filter.skip";
    // request attribute to mark requests seen by the filter, so forwards are still
    // considered a single request from a capture stand point
    public static final String REQUEST_MARKER_ATTRIBUTE = "testRecorder.request.marked";
    public static final String REQUEST_EXCEPTION_MARKER_ATTRIBUTE = "testRecorder.request.marked.exception";
    public static final String REQUEST_NEW_EXCEPTION_MARKER_ATTRIBUTE = "testRecorder.request.marked.exception.new";
    public static final String STATE_STORAGE_KEY = "testRecorder.state.";
    public static final String BODY_END = "</body>";
    public static final String BODY_END_CAPS = "</BODY>";
    public static final String NL = "\n";
    public static final String EMPTY_STRING = "";
    // 'cmd' values
    public static final String START = "Start";
    public static final String STOP = "Stop";
    public static final String RED = "red";
    public static final String GREEN = "green";
    public static final String RECORD_FILE_PATH = "/testRecorder/tests";
    public static final String RESULTS_FILE_PATH = "/testRecorder/testResults";
    public static final String ERROR_PAGE = "/testRecorder/error.jsp";
    public static final String RECORD_PAGE = "/testRecorder/record.jsp";
    public static final String PLAYBACK_PAGE = "/testRecorder/playback.jsp";
    public static final String STATUS_PAGE = "/testRecorder/status.jsp";
    public static final String TEST_PAGE = "/testRecorder/test.jsp";
    public static final String XML = ".xml";
    public static final String DIFF = ".diff";
    // request attributes
    public static final String MSG_ATTRIBUTE = "testRecorder.msg";
    public static final String RECORD_SESSION_ATTRIBUTE = "testRecorder.record.session";
    public static final String PLAYBACK_SESSION_ATTRIBUTE = "testRecorder.playback.session";
    // request param
    public static final String TEST_USER = "testUser";
    public static final String TEST_NAME = "testName";
    public static final String DESCRIPTION = "description";
    public static final String OVERWRITE = "overwrite";
    public static final String MODE = "mode";
    public static final String CMD = "cmd";
    public static final String FILE = "file";
    // 'mode' values
    public static final String RECORD = "record";
    public static final String PLAYBACK = "playback";
    public static final String XML_MODE = "xml";
    public static final String ADMIN = "admin";
    public static final String CLEAN = "clean";
    public static final String SET_TEST_MODE_FALSE = "setTestModeFalse";
    public static final String SET_TEST_MODE_TRUE = "setTestModeTrue";
    public static final String DPY_DIFF = "diff";
    public static final String DPY_DETAILS = "details";
    public static final String DPY_LINK = "displayLink";
    public static final String MODE_TEST = "test";
    public static final String DISPLAY_REPORT = "displayReport";
    public static final String DISPLAY_RECORD = "displayRecordFile";
    public static final String DISPLAY_PLAYBACK = "displayPlaybackFile";

    // outcomes
    public static final String PASS = "pass";
    public static final String FAIL = "fail";
    public static final String ERROR= "ERROR";
}
