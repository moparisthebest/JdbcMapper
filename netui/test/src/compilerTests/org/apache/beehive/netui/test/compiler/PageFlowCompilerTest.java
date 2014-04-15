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
package org.apache.beehive.netui.test.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class PageFlowCompilerTest
    extends TestCase {

    /**
     * Test Log.
     */
    private static final Log LOGGER = LogFactory.getLog(PageFlowCompilerTest.class);

    /**
     * The parent directory of the filesToCompile.
     */
    private File _testDir;

    /**
     * The directory to which the compiler will write the actual file.
     */
    private File _outputDir;

    /**
     * The handler to process the compiler outputs and compare with the expected
     */
    private OutputHandler _outputHandler = new OutputHandler();

    protected void setUp()
        throws Exception { 
        super.setUp();
        System.out.println("Compiling: " + this.getName());
        // Set up test resources into the _outputDir
        TestUtil.createCompilerResources(_testDir, _outputDir);
    }

    /**
     * Constructor for PageFlowCompilerTest. A PageFlowCompilerTest is created
     * by a PageflowCompilerTestSuite.
     *
     * @param testName Take the test directory (pageflow) name as the test name
     * @param outputDir the directory for the compiler output
     * @param testDir the directory that contains the tests
     */
    public PageFlowCompilerTest(String testName, File outputDir, File testDir) {
        super(testName);
        _outputDir = outputDir;
        _testDir = testDir;
    }

    /**
     * Run a JUnit TestCase which will attempt to compile pageflow controller
     * files and any controls files in the test directory and
     * analyze the compiler output results.
     * <p/>
     * This is intended to be called by the runner.
     *
     * @throws PageFlowCompilerTestException Each compilation of a page flow is considered a junit test, and each test
     *                                       uses the same runTest() method.
     *                                       <p/>
     *                                       The runTest() method follows the below algorithm:
     *                                       <p/>
     *                                       1. Setup: Copy the page flow and its expected results to a build
     *                                       directory. 2. Compile: Try to compile the page flow and its controls. Any
     *                                       warning or error messages are piped to a warningorerrors.actual file. A
     *                                       struts-config file MAY have been created. 3. Compare: The compiler
     *                                       may have produced certain files. These files need to be compared to files
     *                                       that are KNOWN to be correct. The correctness is determined by the test
     *                                       author.
     *                                       <p/>
     *                                       The supported file comparisons are: A struts-config-*.xml file and A
     *                                       warningsorerrors.actual file.
     *                                       <p/>
     *                                       If the test has a .expected file, then compiler MUST produce a
     *                                       duplicate .actual file. If the files differ, the test will fail. If you
     *                                       have a .expected file, without a corresponding .actual file, the test
     *                                       will fail. File comparisons, are generally, just
     *                                       line by line diffs.
     */
    protected void runTest()
        throws Throwable, FileNotFoundException {

        String webappRoot = _outputDir.getAbsolutePath();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("webappRoot = " + webappRoot);

        String outputFailure = "";
        String strutsOutputFailure = "";
        assertNotNull("outputDir not exist", _outputDir);

        RunApt runner = new RunApt();
        runner.runControlsApt(webappRoot, true, TestPropertyMgr.getControlsCompilerSwitch());
        runner.runPageFlowApt(webappRoot, true, TestPropertyMgr.getJPFCompilerSwitch());

        outputFailure = _outputHandler.handleWarningsOrErrors(_outputDir, TestPropertyMgr.EXPECTED_DIR_NAME);
        strutsOutputFailure = _outputHandler.handleGeneratedStrutsConfigFileComparisons(_outputDir, TestPropertyMgr.EXPECTED_DIR_NAME);

        assertNull(outputFailure, outputFailure);
        assertNull(strutsOutputFailure, strutsOutputFailure);
    }

    /**
     * The suite method will generate a list of TestCases to run, and run them.
     * TODO will need to use a test config file to parse for test locations and
     * what tests to run and what not to run etc.
     */
    public static Test suite()
        throws PageFlowCompilerTestException {
        File suiteTestDir;
        File suiteOutputDir;

        TestSuite suite = new TestSuite();
        suite.setName("PageflowCompilerTestSuite");

        /* Get the test suite location. */
        String testSuitePath = TestPropertyMgr.getTestSuiteDir();

        /* Verify the existance of the directory. */
        suiteTestDir = new File(testSuitePath);
        assertTrue(TestPropertyMgr.PROPERTY_TESTSUITE_TESTS_DIR + " [" + testSuitePath + "]: did not exist.",
            suiteTestDir.exists());
        assertTrue(TestPropertyMgr.PROPERTY_TESTSUITE_TESTS_DIR + " [" + testSuitePath + "]: is not a directory.",
            suiteTestDir.isDirectory());

        /* Get test output location. */
        String outputPath = TestPropertyMgr.getOutputDir();

        /* Verify and create the output directory for the suite. */
        suiteOutputDir = new File(outputPath);
        /* Create the output directory. */
        if (!suiteOutputDir.exists())
            suiteOutputDir.mkdirs();

        /* Get included and excluded test lists */
        Set includedTests = TestPropertyMgr.getPropertySet(TestPropertyMgr.PROPERTY_TESTSUITE_INCLUDE_LIST);
        Set excludedTests = TestPropertyMgr.getPropertySet(TestPropertyMgr.PROPERTY_TESTSUITE_EXCLUDE_LIST);

        /* Each direct child directory of the suiteTestDir is a test. */
        File[] directories = suiteTestDir.listFiles();
        // Sort the directories to keep the test close to consistent on
        // different platforms
        Arrays.sort(directories);
        for (int i = 0; i < directories.length; i++) {
            File test = directories[i];
            String name = test.getName().trim();
            if (test.isDirectory() && !name.startsWith(".")) {
                /* Determine if it is included or excluded. */
                File myoutputdir = new File(suiteOutputDir, name);
                if (includedTests.size() > 0
                    && !includedTests.contains("${" + TestPropertyMgr.PROPERTY_TESTSUITE_INCLUDE_LIST + "}")
                    && !includedTests.contains("ALL")) {
                    if (includedTests.contains(name) && !excludedTests.contains(name)) {
                        /* Add the JUnit TestCase. */
                        suite.addTest(new PageFlowCompilerTest(name, myoutputdir, test));
                    }
                } else {
                    if (!excludedTests.contains(name)) {
                        /* Add the JUnit TestCase. */
                        suite.addTest(new PageFlowCompilerTest(name, myoutputdir, test));
                    }
                }
            } // end IF isDirectory
        }// end FOR directories

        System.out.println("This PageFlowCompilerTest has " + suite.countTestCases() + " test cases.");

        return suite;
    }
}
