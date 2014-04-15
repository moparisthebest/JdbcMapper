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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

final class OutputHandler {

    private static Log LOGGER = LogFactory.getLog(OutputHandler.class);

    /**
     * Validate the generated struts config files, if any.
     *
     * @param outputDir       compiler test output directory
     * @param expectedDirName the page flow direcotry name under test
     * @return String - file mismatch messages
     */
    protected String handleGeneratedStrutsConfigFileComparisons(File outputDir, String expectedDirName)
        throws PageFlowCompilerTestException {

        boolean localize = false;
        boolean trimLines = true;
        boolean removeXMLComment = true;
        boolean removeJpfLineNumbers = true;
        StringBuffer failMsg = new StringBuffer();

        Map actual = getActualGeneratedStrutsConfigFiles(outputDir);
        Map expected = getExpectedGeneratedStrutsConfigFiles(outputDir, expectedDirName);

        if ((actual == null && expected.size() == 0) || (actual.size() == 0 && expected.size() == 0)) {
            // no comparision needed
            return null;
        }

        if ((actual == null && expected.size() > 0) || (actual.size() == 0 && expected.size() > 0)) {
            // no comparision needed
            failMsg.append(" no config files were generated but " + expected.size() + " were expected! \n");
            return failMsg.toString();
        }

        if (actual.size() > 0 && expected.size() == 0) {
            // no comparision needed
            failMsg.append(" no config files expected " + actual.size() + " were generated! \n");
            return failMsg.toString();
        }

        if (actual.size() != expected.size()) {
            failMsg.append(actual.size() + " files were found but " + expected.size() + " were expected! \n");
        }

        Iterator it = actual.keySet().iterator();
        String key = null;
        while (it.hasNext()) {
            key = (String) it.next();
            String[] tokens = key.split(TestPropertyMgr.EXT_XML);
            String returnMsg;

            if (!expected.containsKey(tokens[0] + TestPropertyMgr.EXT_EXPECTED)) {
                returnMsg = ((File) actual.get(key)).getName() + " is not expected!";

            } else {
                returnMsg = TestUtil.fileCompare(((File) expected.get(tokens[0] + TestPropertyMgr.EXT_EXPECTED)),
                    ((File) actual.get(key)), localize, trimLines, removeXMLComment,
                    removeJpfLineNumbers);
            }
            if (returnMsg != null) {
                failMsg.append(returnMsg);
            }

        }

        if (failMsg.length() == 0)
            return null;
        else
            return failMsg.toString();
    }

    /**
     * Validate any warnings or errors generated (or not generated) by the
     * compiler.
     *
     * @param outputDir compiler test output directory
     * @param expectedDirName the pageflow direcotry name under test
     */
    protected String handleWarningsOrErrors(File outputDir, String expectedDirName)
        throws PageFlowCompilerTestException {

        boolean localize = true;
        boolean trimLines = true;
        boolean removeXMLComment = false;
        boolean removeJpfLineNumbers = false;
        StringBuffer failMsg = new StringBuffer();

        File actual = getActualOutputFile(outputDir);
        File expected = getExpectedWarningsOrErrorsFile(outputDir, expectedDirName);
        try {
            if (!actual.exists() && expected.exists()) {
                FileInputStream expectedStream = null;
                try {
                    expectedStream = new FileInputStream(expected);

                    if (expectedStream.available() > 0)
                        failMsg.append(" [warningorerrors.actual] is expected! actual: " + actual.getPath() + " expected: "
                            + expected.getPath() + "\n");
                }
                finally {
                    try{if(expectedStream != null)expectedStream.close();}catch(IOException ignore){}
                }
            }
            else if (actual.exists() && !expected.exists()) {
                FileInputStream actualStream = null;
                try {
                    actualStream = new FileInputStream(actual);

                    if (actualStream.available() > 0)
                        failMsg.append("No [warningorerrors.actual] is expected!  actual: " + actual.getPath() + "\n");
                }
                finally {
                    try{if(actualStream != null)actualStream.close();}catch(IOException ignore){}
                }
            }
            else if (!actual.exists() && !expected.exists()) { }
            // Try the new compare logic to accormodate the messages occuring in different order
            else {
                Set<String> actualOutput = TestUtil.processWarningOrErrorContent(actual, localize, trimLines,
                    removeXMLComment, removeJpfLineNumbers);
                Set<String> expectedOutput = TestUtil.processWarningOrErrorContent(expected, localize, trimLines,
                    removeXMLComment,
                    removeJpfLineNumbers);
                if (!actualOutput.equals(expectedOutput)) {
                    failMsg.append("[warningorerrors] \nACTUAL: ");

                    String[] actualInArray = actualOutput.toArray(new String[actualOutput.size()]);
                    String[] expectedInArray = expectedOutput.toArray(new String[expectedOutput.size()]);
                    for (int i = 0; i < actualInArray.length; i++) {

                        failMsg.append(actualInArray[i]);
                    }

                    failMsg.append("\nEXPECTED: ");
                    for (int i = 0; i < expectedInArray.length; i++) {

                        failMsg.append(expectedInArray[i]);
                    }
                }
            }

        }
        catch (IOException ioe) {
            throw new PageFlowCompilerTestException("Can not read " + expected.getName());
        }

        if (failMsg.length() == 0) {
            return null;
        } else {
            return (failMsg.toString() + "\n");
        }
    }

    /**
     * Get all generated struts config files from the test output area
     *
     * @param outputDir --
     *                  File, the test output directory
     * @return Map <File.getName(),File>
     */
    protected Map getActualGeneratedStrutsConfigFiles(File outputDir) {
        File classesDir = new File(outputDir.getAbsolutePath() + File.separatorChar + TestPropertyMgr.DIRNAME_WEB_INF + File.separatorChar + TestPropertyMgr.JAVAC_OUTPUT_DIR_NAME);
        File generatedDir = new File(classesDir, TestPropertyMgr.DIRNAME_PAGEFLOW_STRUTS_GENERATED);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTUAL_GENERATDSTRUTSCONFIG_PARENT:" + generatedDir.getPath());
        }

        if (!generatedDir.exists()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(generatedDir.getAbsolutePath() + " did not exist.");
            }

            return null;
        }
        return getGeneratedStrutsConfigFiles(generatedDir, TestPropertyMgr.EXT_XML);
    }

    /**
     * Find the Files which is the file we expect to be created by the compiler.
     *
     * @param outputDir -
     *                  File, the test output directory
     * @param testName  -
     *                  the page flow directory name under test
     * @return Map <File.getName(),File>
     */
    protected Map getExpectedGeneratedStrutsConfigFiles(File outputDir, String expectedDirName) {
        File parent = new File(outputDir, expectedDirName);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("EXPECTED_GENERATEDSTRUTSCONFIG_PARENT:" + parent.getPath());
        }

        return getGeneratedStrutsConfigFiles(parent, TestPropertyMgr.EXT_EXPECTED);
    }

    /**
     * Get the actual compiler test errororwarning output file
     *
     * @param outputDir The test output directory
     * @return File
     */
    protected File getActualOutputFile(File outputDir) {
        File file = new File(outputDir, TestPropertyMgr.FILENAME_ACTUAL_OUTPUT);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTUAL_OUTPUT_FILE:" + file.getPath());
        }
        return file;
    }

    /**
     * Get the expected warnings or errors file.
     *
     * @param outputdir
     * @param testName
     */
    protected File getExpectedWarningsOrErrorsFile(File outputDir, String expectedDirName) {
        String filename = TestPropertyMgr.PREFIX_WARNINGS_OR_ERRORS + TestPropertyMgr.EXT_EXPECTED;
        File file = new File(outputDir, expectedDirName + File.separatorChar + filename);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("EXPECTED_OUTPUT_FILE:" + file.getPath());
        }
        return file;
    }

    /**
     * Get the struts config files in a HashMap. The files must be direct
     * children of the parent directory
     *
     * @param parent the directory where the actual or expected config files reside
     * @param extension the struts config file extention, .xml for the actual, .expected for the expected
     * @return Map <File.getName(),File>
     */
    protected Map getGeneratedStrutsConfigFiles(File parent, String extension) {
        Map map = new HashMap();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("?IS_THIS_A_STRUTS_CONFIG_FILE with prefix and extension:"
                + TestPropertyMgr.PREFIX_CONFIG_FILE + " and " + extension);
        }

        if (parent == null) {
            LOGGER.error(parent.getAbsolutePath() + " was null.");
            return map;
        }
        if (!parent.isDirectory()) {
            LOGGER.error(parent.getAbsolutePath() + " was not a directory.");
            return map;
        }
        if (!parent.exists()) {
            LOGGER.error(parent.getAbsolutePath() + " did not exist.");
            return map;
        }
        File[] files = parent.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* Is this the file we want to compile? */
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("?IS_THIS_A_STRUTS_CONFIG_FILE:" + files[i].getPath());
            }
            if (!files[i].isDirectory() && files[i].getName().endsWith(extension)
                && files[i].getName().startsWith(TestPropertyMgr.PREFIX_CONFIG_FILE)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("ADDING_A_STRUTS_CONFIG_FILE:" + files[i].getPath());
                }
                map.put(files[i].getName(), files[i]);
            }
        }
        return map;
    }

}
