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

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;

import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.Constants;
import org.apache.beehive.netui.tools.testrecorder.shared.Reporter;
import org.apache.beehive.netui.tools.testrecorder.shared.RecordSessionBean;
import org.apache.beehive.netui.tools.testrecorder.shared.RequestData;
import org.apache.beehive.netui.tools.testrecorder.shared.SessionXMLException;
import org.apache.beehive.netui.tools.testrecorder.shared.xmlbeans.XMLHelper;
import org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinition;
import org.apache.beehive.netui.tools.testrecorder.shared.config.ConfigException;
import org.apache.beehive.netui.tools.testrecorder.shared.config.Category;
import org.apache.beehive.netui.tools.testrecorder.shared.config.WebappConfig;
import org.apache.beehive.netui.tools.testrecorder.server.state.PlaybackSession;
import org.apache.beehive.netui.tools.testrecorder.server.state.RecordSession;
import org.apache.beehive.netui.tools.testrecorder.server.state.State;
import org.apache.beehive.netui.tools.testrecorder.server.state.SessionFailedException;

public class TestRecorderServlet
    extends HttpServlet {

    private static final Logger LOGGER = Logger.getInstance(TestRecorderServlet.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String mode = null;
        try {
            mode = request.getParameter(Constants.MODE);
            if (mode == null) {
                String msg = "mode( " + mode + " ) may not be null.";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
            }
            else if (mode.equalsIgnoreCase(Constants.RECORD)) {
                doRecord(request, response);
            }
            else if (mode.equalsIgnoreCase(Constants.PLAYBACK)) {
                doPlayback(request, response);
            }
            else if (mode.equalsIgnoreCase(Constants.DPY_DIFF)) {
                doDiff(request, response);
            }
            else if (mode.equalsIgnoreCase(Constants.DPY_DETAILS)) {
                doDetails(request, response);
            }
            else if (mode.equalsIgnoreCase(Constants.DPY_LINK)) {
                doLink(request, response);
            }
            else if (mode.equalsIgnoreCase(Constants.ADMIN)) {
                doAdmin(request, response);
            }
            else if (mode.equalsIgnoreCase(Constants.XML_MODE)) {
                doXml(request, response);
            }
            else {
                String msg = "unknown mode( " + mode + " ).";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
            }
        }
        catch (Exception ex) {
            String msg = "ERROR: encountered exception handling test recorder control request, mode( " + mode +
                ", exception( " + ex.getMessage() + " )";
            forward(request, response, msg, Constants.ERROR_PAGE, true, ex);
        }
    }

    public void doRecord(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, ConfigException {
        String cmd = request.getParameter(Constants.CMD);
        String testName = getTestName(request);
        String testUser = request.getParameter(Constants.TEST_USER);
        String description = request.getParameter(Constants.DESCRIPTION);
        boolean overwrite = Boolean.valueOf(request.getParameter(Constants.OVERWRITE)).booleanValue();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("record cmd( " + cmd + " )");
            LOGGER.info("testName( " + testName + " )");
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("testUser( " + testUser + " )");
            LOGGER.debug("overwrite( " + overwrite + " )");
            LOGGER.debug("description( " + description + " )");
        }

        if (cmd == null) {
            // fail
            String msg = "ERROR: unable to handle record request, '" + Constants.CMD + "' may not be null.";
            forward(request, response, msg, Constants.ERROR_PAGE, true);
            return;
        }

        if (cmd.equalsIgnoreCase(Constants.START))
            doRecordStart(testName, request, response, overwrite, testUser, description);
        else if (cmd.equalsIgnoreCase(Constants.STOP))
            doRecordStop(request, response);
        else {
            String msg = "ERROR: unable to handle record request: '" + Constants.CMD + "'( " + cmd + " ) is not recognized.";
            forward(request, response, msg, Constants.ERROR_PAGE, true);
        }
    }

    private void doRecordStop(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        TestRecorderFilter filter = TestRecorderFilter.instance();
        RecordSession session = filter.getState().getRecordingSession();
        String msg = "ERROR: failed stopping recording session";

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("STOPPING: recording session( " + session + " )");

        try {
            /* this may take some time, this waits for recording threads to finish */
            session = filter.getState().recordStop();
            if (session == null) {
                // fail, not currently recording
                msg = "ERROR: no recording session is currently started";
                if (LOGGER.isWarnEnabled())
                    LOGGER.warn(msg);
            }
            else {
                request.setAttribute(Constants.RECORD_SESSION_ATTRIBUTE, session);
                msg = "Recording session( " + session.getSessionName() + " ) stopped";
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(msg);
            }

            forward(request, response, msg, Constants.RECORD_PAGE, false);
        }
        catch (Exception ex) {
            msg = "ERROR: failed while stopping recording";
            request.setAttribute(Constants.RECORD_SESSION_ATTRIBUTE, session);
            forward(request, response, msg, Constants.RECORD_PAGE, true, ex);
        }

        LOGGER.debug("STOPPED: recording session( " + session + " )");
    }

    private void doRecordStart(String testName,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               boolean overwrite,
                               String testUser,
                               String description)
        throws IOException, ServletException {

        TestDefinition test = getTest(testName);
        if (test == null)
            test = new TestDefinition(testName, description, TestRecorderFilter.instance().getWebapp(), null);

        if (test.getWebapp() != TestRecorderFilter.instance().getWebapp()) {
            String msg = "ERROR: unable to start recording, the webapp of the test( " +
                test.getWebapp().getName() + " ) is not the same as this webapp( " +
                TestRecorderFilter.instance().getWebapp() + " )";
            forward(request, response, msg, Constants.RECORD_PAGE, true);
            return;
        }

        /* may throw IOException or SecurityException */
        RecordSession session = null;
        try {
            session = getRecordSession(test, overwrite, testUser, description);
            if (description != null)
                test.setDescription(description);

            TestRecorderFilter.instance().getTestDefinitions().add(test);
        }
        catch (Exception ex) {
            String msg = "ERROR: unable to start recording, unable to obtain session, exception( " + ex.getMessage() + " )";
            forward(request, response, msg, Constants.RECORD_PAGE, true, ex);
            return;
        }

        LOGGER.debug("attempting to start recording session( " + session + " )");

        TestRecorderFilter filter = TestRecorderFilter.instance();
        boolean start = false;
        try {
            start = filter.getState().recordStart(session);
        }
        catch (Exception ex) {
            String msg = "ERROR: failed to start recording, exception( " + ex.getMessage() + " )";
            forward(request, response, msg, Constants.RECORD_PAGE, true, ex);
            return;
        }

        if (LOGGER.isInfoEnabled())
            LOGGER.info("start( " + start + " )");

        if (start) {
            String msg = "recording session( " + session.getSessionName() + " ) started.";
            request.setAttribute(Constants.RECORD_SESSION_ATTRIBUTE, session);
            if (LOGGER.isInfoEnabled())
                LOGGER.info(msg);

            forward(request, response, msg, Constants.RECORD_PAGE, false);
        }
        else {
            String msg = "ERROR: recording session( " + session.getSessionName() + " ) already started.";
            request.setAttribute(Constants.RECORD_SESSION_ATTRIBUTE, filter.getState().getRecordingSession());
            forward(request, response, msg, Constants.RECORD_PAGE, true);
        }
    }

    public void doPlayback(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, ConfigException {
        String cmd = request.getParameter(Constants.CMD);

        LOGGER.info("playback cmd( " + cmd + " )");

        // any request forwards from here should skip the filter.
        request.setAttribute(Constants.FILTER_SKIP_PARAM, Boolean.TRUE);
        if (cmd != null) {
            if (cmd.equalsIgnoreCase(Constants.START))
                doPlaybackStart(request, response);

            if (cmd.equalsIgnoreCase(Constants.STOP))
                doPlaybackStop(request, response);
        }
        else {
            String msg = "ERROR: playback '" + Constants.CMD + "'( " + cmd + " ) is not recognized.";
            forward(request, response, msg, Constants.ERROR_PAGE, true);
            return;
        }
        forward(request, response, Constants.RECORD_PAGE);
    }

    private void doPlaybackStop(HttpServletRequest request,
                                HttpServletResponse response)
        throws IOException {

        String testId = request.getParameter(Constants.TEST_ID_HEADER);

        LOGGER.info("playback stop testId( " + testId + " )");

        TestRecorderFilter filter = TestRecorderFilter.instance();
        PlaybackSession session = null;
        try {
            // this may take some time, this waits for recording threads to finish
            // may throw IOException
            session = filter.getState().playbackStop(testId);
        }
        catch (Exception ex) {
            String msg = "ERROR: unable to stop playback session for testId( " + testId + " ), exception( " + ex.getMessage() + " )";
            request.setAttribute(Constants.MSG_ATTRIBUTE, msg);
            response.setHeader(Constants.OUTCOME_HEADER, Constants.ERROR);
            LOGGER.error(msg, ex);
            if (ex instanceof IOException)
                throw (IOException) ex;
            else if (ex instanceof RuntimeException)
                throw (RuntimeException) ex;
            else throw new RuntimeException(msg, ex);
        }

        LOGGER.info("STOP: playback session( " + session + " )");

        if (session == null) {
            // fail, not currently doing playback
            String msg = "ERROR: no playback session exists for testId( " + testId + " )";
            request.setAttribute(Constants.MSG_ATTRIBUTE, msg);
            // set response headers for the client.
            response.setHeader(Constants.OUTCOME_HEADER, Constants.ERROR);
            response.setHeader(Constants.MSG_ATTRIBUTE, msg);
            LOGGER.error(msg);
        }
        else {
            // gen playback summary
            // set outcome to stop or fail via resp header
            // return playback summary as response body.
            String outcome = session.getStatus();
            String msg = "playback session( " + session.getSessionName() + " ) stopped with status( " + outcome + " ).";
            request.setAttribute(Constants.MSG_ATTRIBUTE, msg);
            request.setAttribute(Constants.PLAYBACK_SESSION_ATTRIBUTE, session);
            LOGGER.info(msg);

            // set response headers for the client.
            response.setHeader(Constants.OUTCOME_HEADER, outcome);
            response.setHeader(Constants.MSG_ATTRIBUTE, msg);
            response.setHeader(Constants.RESULTS_FILE_HEADER, session.getPlaybackFile().getAbsolutePath());

            File diffFile = session.getDiffFile();
            if (diffFile != null)
                response.setHeader(Constants.RESULTS_DIFF_HEADER, diffFile.getAbsolutePath());
        }
    }

    private void doPlaybackStart(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ConfigException {
        String testName = getTestName(request);
        TestDefinition test = getTest(testName);

        if (test == null)
            throw new ConfigException("ERROR: playback start failed, unable to find a test for test name( " + testName + " )");

        LOGGER.info("playback start test( " + test.getName() + " )");

        PlaybackSession session = null;
        try {
            session = getPlaybackSession(test);
        }
        catch (Exception ex) {
            String msg = "ERROR: unable to start playback, unable to obtain session, exception( " + ex.getMessage() + " )";
            request.setAttribute(Constants.MSG_ATTRIBUTE, msg);
            LOGGER.error(msg, ex);
            System.err.println("\nPLAYBACK ERROR:\n" + msg);
            if (ex instanceof IOException)
                throw (IOException) ex;
            else if (ex instanceof RuntimeException)
                throw (RuntimeException) ex;
            else throw new RuntimeException(msg, ex);
        }
        TestRecorderFilter filter = TestRecorderFilter.instance();
        boolean start = false;
        try {
            // may throw IOException
            start = filter.getState().playbackStart(session);
        }
        catch (Exception ex) {
            String msg = "ERROR: failed to start playback session( " + session + " ), exception( " + ex.getMessage() + " )";
            request.setAttribute(Constants.MSG_ATTRIBUTE, msg);
            LOGGER.error(msg, ex);
            System.err.println("\nPLAYBACK ERROR:\n" + msg);

            if (ex instanceof IOException)
                throw (IOException) ex;
            else if (ex instanceof RuntimeException)
                throw (RuntimeException) ex;
            else throw new RuntimeException(msg, ex);
        }

        LOGGER.info("playback start( " + start + " )");

        if (start) {
            String msg = "playback session( " +
                session.getSessionName() + " ) started.";
            request.setAttribute(Constants.MSG_ATTRIBUTE, msg);
            request.setAttribute(Constants.PLAYBACK_SESSION_ATTRIBUTE, session);
            LOGGER.info(msg);

            // set response headers for the client.
            response.setHeader(Constants.OUTCOME_HEADER, Constants.PASS);
            response.setHeader(Constants.TEST_ID_HEADER, session.getStringUID());
            response.setHeader(Constants.RECORD_FILE_HEADER, session.getRecordFile().getAbsolutePath());
        }
        else {
            // fail
            String msg = "ERROR: failed to start playback session( " +
                session.getSessionName() + " )";
            request.setAttribute(Constants.MSG_ATTRIBUTE, msg);
            request.setAttribute(Constants.PLAYBACK_SESSION_ATTRIBUTE, session);
            LOGGER.error(msg);
            // set response headers for the client.
            response.setHeader(Constants.OUTCOME_HEADER,
                               Constants.FAIL);
            response.setHeader(Constants.MSG_ATTRIBUTE, msg);
        }
    }

    public void doDiff(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException, ConfigException {
        // TODO use test name constant here, similar to the rest of the commands
        String sessionName = request.getParameter(Constants.FILE);
        TestDefinition test = getTest(sessionName);
        if (test == null) {
            String msg = "ERROR: unable to display diff output, no test was found for name( " + sessionName +
                " )";
            forward(request, response, msg, Constants.ERROR_PAGE, true);
            return;
        }
        try {
            Writer wrtr = response.getWriter();
            File diffFile = getDiffFile(test);
            if (!diffFile.exists()) {
                String msg = "ERROR: unable to display diff output, no file was found for test( " +
                    test.getName() + " ), file( " + diffFile.getAbsolutePath() + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }
            // output an html page
            wrtr.write("<html><head><title>Test Diffs of ");
            wrtr.write(test.getName());
            wrtr.write("</title></head>\n");
            wrtr.write("<body>\n");
            wrtr.write("<h4>Test Diffs: ");
            wrtr.write(test.getName());
            wrtr.write("</h4>\n");
            wrtr.write("<pre>");
            wrtr.write(Reporter.genDiffDetails(diffFile));
            wrtr.write("</pre>");
            wrtr.write("</body></html>");
        }
        catch (Exception e) {
            String msg = "ERROR: failed to display diff file, exception( " + e.getMessage() + " ), test( " + test.getName() + " )";
            forward(request, response, msg, Constants.ERROR_PAGE, true);
            return;
        }
    }

    public void doLink(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException, ConfigException {

        String sessionName = request.getParameter(Constants.FILE);
        TestDefinition test = getTest(sessionName);
        WebappConfig config = TestRecorderFilter.instance().getWebapp();
        try {
            if (test == null) {
                String msg = "No test was found for name( " + sessionName + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }

            File recFile = getRecordSessionFile(test);
            if (!recFile.exists()) {
                String msg = "No file was found for test( " + test.getName() + " ), file( " +
                    recFile.getAbsolutePath() + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }

            RecordSessionBean bean = null;
            try {
                bean = XMLHelper.getRecordSessionBean(recFile);
            }
            catch (SessionXMLException e) {
                String msg = "Failed processing file for record session( " + test.getName() + " ), file( " +
                    recFile.getAbsolutePath() + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }

            RequestData rd = bean.getRequestData(0);
            if (rd == null) {
                String msg = "Unable to find the first request in the test ( " + test.getName() + " ), file( " + recFile.getAbsolutePath() + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }

            String host = config.getServer().getHostname();
            if (host.equals("localhost"))
                host = request.getServerName();

            response.sendRedirect(rd.getUri(host, config.getServer().getPort()));
        }
        catch (Exception e) {
            String msg = "ERROR: failed to display test details, exception( " + e.getMessage() + " ), test( " + test.getName() + " )";
            forward(request, response, msg, Constants.ERROR_PAGE, true, e);
        }
    }

    public void doDetails(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException, ConfigException {
        /* todo: use test name constant here, similar to the rest of the commands */
        String sessionName = request.getParameter(Constants.FILE);
        TestDefinition test = getTest(sessionName);
        try {
            if (test == null) {
                String msg = "No test was found for name( " + sessionName + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }
            Writer wrtr = response.getWriter();
            File recFile = getRecordSessionFile(test);
            if (!recFile.exists()) {
                String msg = "No file was found for test( " + test.getName() + " ), file( " +
                    recFile.getAbsolutePath() + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }
            RecordSessionBean bean = null;
            try {
                bean = XMLHelper.getRecordSessionBean(recFile);
            }
            catch (SessionXMLException e) {
                String msg = "Failed processing file for record session( " + test.getName() + " ), file( " +
                    recFile.getAbsolutePath() + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }
            // output an html page
            wrtr.write("<html><head><title>Test Details of ");
            wrtr.write(test.getName());
            wrtr.write("</title></head>\n");
            wrtr.write("<body>\n");
            wrtr.write("<h3>Test Details: ");
            wrtr.write(test.getName());
            wrtr.write("</h3>\n");
            wrtr.write(Reporter.genDetails(bean));
            wrtr.write("</body></html>");
        }
        catch (Exception e) {
            String msg = "ERROR: failed to display test details, exception( " + e.getMessage() + " ), test( " + test.getName() + " )";
            forward(request, response, msg, Constants.ERROR_PAGE, true, e);
        }
    }

    private void forward(HttpServletRequest request,
                         HttpServletResponse response,
                         String msg,
                         String page,
                         boolean error)
        throws ServletException, IOException {
        forward(request, response, msg, page, error, null);
    }

    private void forward(HttpServletRequest request,
                         HttpServletResponse response,
                         String msg,
                         String page,
                         boolean error,
                         Exception ex)
        throws ServletException, IOException {
        if (request.getAttribute(Constants.MSG_ATTRIBUTE) == null)
            request.setAttribute(Constants.MSG_ATTRIBUTE, msg);

        if (error) {
            if (ex != null)
                LOGGER.error(msg, ex);
            else LOGGER.error(msg);
        }
        forward(request, response, page);
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String page)
        throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }

    public void doAdmin(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, ConfigException {
        String cmd = request.getParameter(Constants.CMD);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("admin cmd( " + cmd + " )");

        // any request forwards from here should skip the filter.
        request.setAttribute(Constants.FILTER_SKIP_PARAM, Boolean.TRUE);
        if (cmd != null) {
            if (cmd.equalsIgnoreCase(Constants.CLEAN)) {
                doClean(request, response);
                return;
            }
            else if (cmd.equalsIgnoreCase(Constants.SET_TEST_MODE_FALSE)) {
                doTestMode(request, response, false);
                return;
            }
            else if (cmd.equalsIgnoreCase(Constants.SET_TEST_MODE_TRUE)) {
                doTestMode(request, response, true);
                return;
            }
            else if (cmd.equalsIgnoreCase(Constants.DISPLAY_REPORT)) {
                doDisplayReport(request, response);
                return;
            }
            else if (cmd.equalsIgnoreCase(Constants.DISPLAY_RECORD)) {
                doDisplayRecord(request, response);
                return;
            }
            else if (cmd.equalsIgnoreCase(Constants.DISPLAY_PLAYBACK)) {
                doDisplayPlayback(request, response);
                return;
            }
        }
        String msg = "ERROR: admin '" + Constants.CMD + "'( " + cmd + " ) is not recognized.";
        forward(request, response, msg, Constants.ERROR_PAGE, true);
    }

    private void doClean(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("clean up test recorder sessions");

        TestRecorderFilter filter = TestRecorderFilter.instance();
        filter.getState().stopAll();
        forward(request, response, Constants.STATUS_PAGE);
    }

    private void doTestMode(HttpServletRequest request, HttpServletResponse response, boolean value)
        throws ServletException, IOException {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("setting test mode to ( " + value + " )");

        TestRecorderFilter filter = TestRecorderFilter.instance();
        filter.getState().setTestMode(value);
        forward(request, response, Constants.STATUS_PAGE);
    }

    private void doDisplayRecord(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String testString = request.getParameter("test");
        TestRecorderFilter filter = TestRecorderFilter.instance();
        TestDefinition test = filter.getTestDefinitions().getTest(testString);
        File file = null;
        try {
            if (test == null) {
                String msg = "No test was found for name( " + testString + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }
            file = new File(test.getTestFilePath());
            if (!file.exists()) {
                String msg = "No record file was found for test( " + test.getName() + " ), file( " +
                    file.getAbsolutePath() + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }
            doFileReport(request, response, test, file, "Record File for '" + test.getName() + "' Test");
        }
        catch (Exception e) {
            String msg = "ERROR: failed to display record file, exception( " + e.getMessage() + " ), test( " + test.getName() + " ), file( " + file + " )";
            forward(request, response, msg, Constants.ERROR_PAGE, true, e);
        }
    }

    private void doDisplayPlayback(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String testString = request.getParameter("test");
        TestRecorderFilter filter = TestRecorderFilter.instance();
        TestDefinition test = filter.getTestDefinitions().getTest(testString);
        File file = null;
        try {
            if (test == null) {
                String msg = "No test was found for name( " + testString + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }

            file = new File(test.getResultFilePath());
            if (!file.exists()) {
                String msg = "No playback file was found for test( " + test.getName() + " ), file( " +
                    file.getAbsolutePath() + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }
            doFileReport(request, response, test, file, "Playback File for '" + test.getName() + "' Test");
        }
        catch (Exception e) {
            String msg = "ERROR: failed to display playback file, exception( " + e.getMessage() +
                " ), test( " + test.getName() + " ), file( " + file + " )";
            forward(request, response, msg, Constants.ERROR_PAGE, true, e);
        }
    }

    private void doFileReport(HttpServletRequest request, HttpServletResponse response, TestDefinition test,
                              File file, String title) throws ServletException, IOException {
        FileReader reader = null;
        Writer wrtr = null;
        try {
            wrtr = response.getWriter();
            wrtr.write("<html><title>" + Reporter.escape(title) + "</title><body><b>" + title + "</b><br /><br /><pre>");
            // output an html page
            char[] buf = new char[1024];
            reader = new FileReader(file);
            int size;
            while (true) {
                size = reader.read(buf, 0, 1024);
                if (size == -1)
                    break;
                wrtr.write(Reporter.escape(buf, size));
            }
        }
        catch (Exception e) {
            String msg = "ERROR: failed to display file, exception( " + e.getMessage() + " ), test( " + test.getName() + " ), file( " + file.getAbsolutePath() + " )";
            forward(request, response, msg, Constants.ERROR_PAGE, true, e);
        }
        finally {
            if (wrtr != null)
                wrtr.write("</pre></body></html>");

            if (reader != null)
                reader.close();
        }
    }

    private void doDisplayReport(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String categoryString = request.getParameter("category");
        TestRecorderFilter filter = TestRecorderFilter.instance();
        Category category = filter.getTestDefinitions().getCategories().getCategory(categoryString);
        FileReader reader = null;
        try {
            if (category == null) {
                String msg = "No category was found for name( " + categoryString + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }

            Writer wrtr = response.getWriter();
            File reportFile = new File(category.getReportDirPath(), "/html/junit-noframes.html");
            if (!reportFile.exists()) {
                String msg = "No JUnit report was found for category( " + category.getName() + " ), file( " +
                    reportFile.getAbsolutePath() + " )";
                forward(request, response, msg, Constants.ERROR_PAGE, true);
                return;
            }

            // output an html page
            char[] buf = new char[1024];
            reader = new FileReader(reportFile);
            int size;
            while (true) {
                size = reader.read(buf, 0, 1024);
                if (size == -1)
                    break;
                wrtr.write(buf, 0, size);
            }
        }
        catch (Exception e) {
            String msg = "ERROR: failed to display JUnit report, exception( " + e.getMessage() + " ), category( " + category.getName() + " )";
            forward(request, response, msg, Constants.ERROR_PAGE, true, e);
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private void doXml(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String outputType = request.getParameter(Constants.CMD);
        boolean outputHtml = false;

        if (outputType.equalsIgnoreCase("xml"))
            outputHtml = false;
        else if (outputType.equalsIgnoreCase("html"))
            outputHtml = true;
        else {
            String msg = "ERROR: unable to handle Xml request: '" + Constants.CMD + "' ( " + outputType + " ) is not recognized.";
            forward(request, response, msg, Constants.ERROR_PAGE, true);
            return;
        }

        String fileType = request.getParameter(Constants.FILE);
        String resource;
        if (fileType.equalsIgnoreCase(Constants.FILE_TYPE_CONFIG))
            resource = Constants.CONFIG_FILE;
        else if (fileType.equalsIgnoreCase(Constants.FILE_TYPE_WEBAPP))
            resource = Constants.WEBAPPS_FILE;
        else if (fileType.equalsIgnoreCase(Constants.FILE_TYPE_TESTS))
            resource = Constants.TESTS_FILE;
        else {
            String msg = "ERROR: unable to handle Xml request: '" + Constants.FILE + "' ( " + fileType + " ) is not recognized.";
            forward(request, response, msg, Constants.ERROR_PAGE, true);
            return;
        }

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        if (is == null) {
            String msg = "ERROR: unable to handle Xml request, unable to obtain stream for resource( " + resource + " )";
            forward(request, response, msg, Constants.ERROR_PAGE, true);
            return;
        }

        if (outputHtml)
            response.setContentType("text/html");
        else response.setContentType("text/xml");

        doResourceStream(request, response, is, resource, outputHtml);
    }

    private void doResourceStream(HttpServletRequest request,
                                  HttpServletResponse response,
                                  final InputStream is,
                                  final String resource,
                                  final boolean outputHtml)
        throws ServletException, IOException {

        InputStreamReader reader = null;
        Writer wrtr = null;
        try {
            wrtr = response.getWriter();
            if (outputHtml)
                wrtr.write("<html><title>" + Reporter.escape(resource) + "</title><body><b>" + resource + "</b><br /><br /><pre>");

            // output an html page
            char[] buf = new char[1024];
            reader = new InputStreamReader(is);
            int size;
            while (true) {
                size = reader.read(buf, 0, 1024);
                if (size == -1)
                    break;

                if (outputHtml)
                    wrtr.write(Reporter.escape(buf, size));
                else wrtr.write(buf, 0, size);
            }
        }
        catch (Exception e) {
            String msg = "ERROR: failed to write resource( " + resource + " ) as stream , exception( " + e.getMessage() + " )";
            forward(request, response, msg, Constants.ERROR_PAGE, true, e);
        }
        finally {
            if (outputHtml && wrtr != null)
                wrtr.write("</pre></body></html>");

            if (reader != null)
                reader.close();
        }
    }

    private static String getTestName(HttpServletRequest request) {
        return request.getParameter(Constants.TEST_NAME);
    }

    private static TestDefinition getTest(String name) {
        if (LOGGER.isInfoEnabled())
            LOGGER.debug("test name( " + name + " )");

        if (name == null)
            return null;

        TestDefinition test = TestRecorderFilter.instance().getTestDefinitions().getTest(name);
        return test;
    }

    private static RecordSession getRecordSession(TestDefinition test, boolean overwrite, String testUser, String description)
        throws SessionFailedException {

        File file = getRecordSessionFile(test);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("record file( " + file.getAbsolutePath() + " )");

        /* may throw IOException or SecurityException (runtime) */
        RecordSession session = State.createRecordSession(test.getName(), file, overwrite, testUser, description);
        return session;
    }

    private static PlaybackSession getPlaybackSession(TestDefinition test)
        throws IOException, SessionFailedException {
        File playbackFile = getPlaybackSessionFile(test);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("playback file( " + playbackFile.getAbsolutePath() + " )");

        /* may throw IOException or SecurityException (runtime) */
        File diffFile = getDiffFile(test);
        File recordFile = getTestFile(test);

        /* may throw IOException or SecurityException (runtime) */
        return State.createPlaybackSession(test.getName(), playbackFile, recordFile, diffFile);
    }

    public static File getRecordSessionFile(TestDefinition test) {
        return getTestFile(test);
    }

    public static File getPlaybackSessionFile(TestDefinition test) throws IOException {
        test.createPlaybackFile();
        return getResultFile(test);
    }

    public static File getTestFile(TestDefinition test) {
        return new File(test.getTestFilePath());
    }

    public static File getResultFile(TestDefinition test) {
        return new File(test.getResultFilePath());
    }

    public static File getDiffFile(TestDefinition test) {
        return new File(test.getResultDiffFilePath());
    }

}

