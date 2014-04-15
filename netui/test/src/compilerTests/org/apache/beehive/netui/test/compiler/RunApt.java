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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class RunApt {
    private static Log LOGGER = LogFactory.getLog(RunApt.class);
    private boolean _doCompile  = false;

    /**
     * Run Page Flow apt on the given file/directory.
     * 
     * @param webappRootPath the path to the root of the target webapp.
     * @param doCompile whether or not perform javac compile after apt
     * @param doPageFlow the switch to turn on/off of the JPF compiler
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void runPageFlowApt(String webappRootPath, boolean doCompile, boolean doPageFlow)
        throws FileNotFoundException, IOException, PageFlowCompilerTestException {
        if (doPageFlow){
            _doCompile = doCompile;
            File srcDir = new File(webappRootPath);
            File tempDir = new File(srcDir.getAbsolutePath() + File.separatorChar +
                TestPropertyMgr.DIRNAME_WEB_INF + File.separatorChar +
                TestPropertyMgr.TMPBEANSRC_DIR_NAME);
            tempDir.mkdirs();
            runApt(webappRootPath, tempDir, srcDir);
        }
    }

    /**
     * Run Controls apt on the given file/directory.
     * 
     * @param webappRootPath the path to the root of the target webapp.
     * @param doCompile
     * @param doControls switch to turn on/off the controls compiler
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void runControlsApt(String webappRootPath, boolean doCompile, boolean doControls)
        throws FileNotFoundException, IOException, PageFlowCompilerTestException {

        if(doControls){
            _doCompile = doCompile;
            File srcDir = new File(webappRootPath + File.separatorChar + TestPropertyMgr.DIRNAME_WEB_INF
                               + File.separatorChar + TestPropertyMgr.SRC_DIR_NAME);
            // no files need to be compiled
            if (!srcDir.exists()) {
            }
            else {
                File tempDir = new File(srcDir.getAbsolutePath() + File.separatorChar +
                    TestPropertyMgr.DIRNAME_WEB_INF + File.separatorChar +
                    TestPropertyMgr.TMPBEANSRC_DIR_NAME);
                tempDir.mkdirs();
                runApt(webappRootPath, tempDir, srcDir);

            }
        }
    }

    /**
     * Run apt on the given files/directories.
     * 
     * @param webappRoot
     * @param tempDir
     * @param srcDir the path to the root of the target webapp.
     */
    public void runApt(String webappRoot, File tempDir, File srcDir)
        throws FileNotFoundException, IOException, PageFlowCompilerTestException {

        ArrayList<String> aptArgs = new ArrayList<String>();

        // add apt call
        aptArgs.add("apt");

        // -nocompile
        if (!_doCompile)
            aptArgs.add("-nocompile");

        // target directory for apt-generated files (unused for us)
        aptArgs.add("-s");
        aptArgs.add(webappRoot + File.separatorChar + TestPropertyMgr.DIRNAME_WEB_INF + File.separatorChar
                    + TestPropertyMgr.TMPBEANSRC_DIR_NAME);

        // target directory for processor and javac generated class files
        aptArgs.add("-d");
        aptArgs.add(webappRoot + File.separatorChar + TestPropertyMgr.DIRNAME_WEB_INF + File.separatorChar
                    + TestPropertyMgr.JAVAC_OUTPUT_DIR_NAME);

        // Build the classpath
        String classpath = TestPropertyMgr.getClassPath();
        if (classpath == null){
            LOGGER.error("No class path defined!", (new Exception("No class path defined!")));
        }

        aptArgs.add("-classpath");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Classpath: " + classpath);

        aptArgs.add(classpath);

        aptArgs.add("-sourcepath");
        aptArgs.add(tempDir.getAbsolutePath());

        aptArgs.add("-Aweb.content.root=" + webappRoot);

        // Find all the source files by the specified extentions
        Set srcExtentions = TestPropertyMgr.getSrcExts();
        String[] files = TestUtil.findSrcFiles(srcDir, srcExtentions);

        // Copy all the source file into the test area and rename them into .java
        TestUtil.processTestSrcFiles(srcDir, tempDir, files);
        for (int i = 0; i < files.length; i++)
            aptArgs.add(files[i]);

        String[] aptArgsArray = aptArgs.toArray(new String[aptArgs.size()]);

        // Invoke apt using a new process
        String ls_str;
        File SystemErr = new File(webappRoot + File.separatorChar + TestPropertyMgr.PREFIX_WARNINGS_OR_ERRORS
                                  + TestPropertyMgr.EXT_ACTUAL);
        FileWriter errorsFw = new FileWriter(SystemErr);
        BufferedReader aptErrBufferedReader = null;

        try {
            Process ls_proc = Runtime.getRuntime().exec(aptArgsArray);
            aptErrBufferedReader = new BufferedReader(new InputStreamReader(ls_proc.getErrorStream()));

            try {
                while ((ls_str = aptErrBufferedReader.readLine()) != null) {
                    errorsFw.write(ls_str);
                    errorsFw.write('\n');
                }
            }
            catch (IOException eWhile) {
                eWhile.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (errorsFw != null)
                errorsFw.close();
            if (aptErrBufferedReader != null)
                aptErrBufferedReader.close();
        }
    }
}
