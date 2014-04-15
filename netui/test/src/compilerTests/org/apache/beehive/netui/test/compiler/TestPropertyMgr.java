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
import java.util.Set;
import java.util.TreeSet;

final class TestPropertyMgr {

    // Defaults
    public static final String TESTS_DIR_NAME = "testsuite";

    public static final String OUTPUT_DIR_NAME = "compiler";

    public static final String EXPECTED_DIR_NAME = "expectedOutput";

    public static final String TMPBEANSRC_DIR_NAME = ".tmpbeansrc";

    public static final String SRC_DIR_NAME = "src";

    public static final String JAVAC_OUTPUT_DIR_NAME = "classes";

    public static final String LIST_DELIM = ",";

    public static final String DEFAULT_SRC_EXT = "java,jpf,jsfb,jpfs,jcx,jcs,app";

    public static final String UNSET = "UNSET";

    // test properties
    public static final String PROPERTY_JPF_COMPILER_SWITCH = "jpfCompilerSwitch";

    public static final String PROPERTY_CONTROLS_COMPILER_SWITCH = "controlsCompilerSwitch";

    public static final String PROPERTY_CLASS_PATH = "compiler-tests.classpath";

    public static final String PROPERTY_TESTSUITE_TESTS_DIR = "testsuite.dir";

    public static final String PROPERTY_TESTSUITE_OUTPUT_DIR = "test.output.dir";

    public static final String PROPERTY_TESTSUITE_INCLUDE_LIST = "included.test.list";

    public static final String PROPERTY_TESTSUITE_EXCLUDE_LIST = "excluded.test.list";

    public static final String PROPERTY_TEST_HOME_DIR = "compiler.test.home";

    public static final String PROPERTY_TEST_OUTPUT_DIR = "test.output.dir";

    // extentions
    public static final String EXT_XML = ".xml";

    public static final String EXT_EXPECTED = ".expected";

    public static final String EXT_ACTUAL = ".actual";

    // directory names
    public static final String DIRNAME_WEB_INF = "WEB-INF";

    public static final String DIRNAME_PAGEFLOW_STRUTS_GENERATED = "_pageflow";

    // prefixes
    public static final String PREFIX_CONFIG_FILE = "struts-config-";

    public static final String PREFIX_WARNINGS_OR_ERRORS = "warningsorerrors";

    // output file name
    public static final String FILENAME_ACTUAL_OUTPUT = PREFIX_WARNINGS_OR_ERRORS + EXT_ACTUAL;

    // local path representation
    public static final String LOCAL_PATH_STRING = "[LOCAL_PATH]";

    // exception messages
    public static final String EXCEPTION_FILE_COMPARISON = "The file comparison encountered an IOException.";

    public static final String EXCEPTION_TEST_HOME_DIR_UNSET = PROPERTY_TEST_HOME_DIR + " " + UNSET;

    public static final String EXCEPTION_TESTSUITE_TESTS_DIR_UNSET = PROPERTY_TESTSUITE_TESTS_DIR + " " + UNSET;

    public static final String EXCEPTION_TEST_OUTPUT_DIR_UNSET =
        PROPERTY_TEST_OUTPUT_DIR + " OR " + PROPERTY_TEST_HOME_DIR + " " + UNSET;

    /**
     * Property getters
     */

    public static String getTestHomeDir() throws PageFlowCompilerTestException {
        String testHomeDir = (System.getProperty(PROPERTY_TEST_HOME_DIR, UNSET)).trim();

        if (testHomeDir.equals(TestPropertyMgr.UNSET) ||
            testHomeDir.contains("${" + PROPERTY_TEST_HOME_DIR + "}") ||
            testHomeDir.length() == 0)
            throw new PageFlowCompilerTestException(EXCEPTION_TEST_HOME_DIR_UNSET);
        else return testHomeDir;
    }

    public static String getTestSuiteDir()
        throws PageFlowCompilerTestException {

        String suiteDir = (System.getProperty(PROPERTY_TESTSUITE_TESTS_DIR, UNSET)).trim();

        if (suiteDir.contains("${" + PROPERTY_TESTSUITE_TESTS_DIR + "}") ||
            suiteDir.equals(TestPropertyMgr.UNSET) ||
            suiteDir.length() == 0) {
            // default here
            String testHome;
            try {
                testHome = getTestHomeDir();
            }
            catch (Exception re) {
                throw new PageFlowCompilerTestException(EXCEPTION_TEST_OUTPUT_DIR_UNSET);
            }
            suiteDir = testHome + File.separatorChar + TESTS_DIR_NAME;
        }

        return suiteDir;
    }

    public static String getOutputDir() throws PageFlowCompilerTestException {

        String outputDir = System.getProperty(PROPERTY_TEST_OUTPUT_DIR, UNSET);

        if (UNSET.equals(outputDir) || outputDir.length() == 0)
            throw new PageFlowCompilerTestException(EXCEPTION_TEST_OUTPUT_DIR_UNSET);

        return outputDir + File.separatorChar + OUTPUT_DIR_NAME;
    }

    public static Set getPropertySet(String property) {
        String propertyString = System.getProperty(property, UNSET);
        Set propertySet = new TreeSet();
        if (!propertyString.equals(UNSET) || propertyString.length() != 0)
            propertySet = TestUtil.parseList(propertyString, LIST_DELIM);

        return propertySet;
    }

    public static Set getSrcExts() {
        return TestUtil.parseList(DEFAULT_SRC_EXT, LIST_DELIM);
    }

    public static String getClassPath() {
        String classPath = System.getProperty(PROPERTY_CLASS_PATH, UNSET);
        if (classPath.equals(UNSET) || classPath.length() == 0 || classPath.contains("${" + PROPERTY_CLASS_PATH + "}"))
            return null;
        // the string replacement of "<DEFAULT>" below is simply there to help run this test in an IDE
        else return classPath.replace("<DEFAULT>", System.getProperty("java.class.path"));
    }

    public static Boolean getJPFCompilerSwitch() {
        String flag = System.getProperty(PROPERTY_JPF_COMPILER_SWITCH, UNSET);
        return !(flag.toUpperCase()).equals("OFF");
    }

    public static Boolean getControlsCompilerSwitch() {
        String flag = System.getProperty(PROPERTY_CONTROLS_COMPILER_SWITCH, UNSET);
        return !(flag.toUpperCase()).equals("OFF");
    }
}
