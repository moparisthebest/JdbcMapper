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
package qaUtil;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.System;

/**
 *
 */
public class PageLogger implements Serializable
    {
    private String _testNa;
    private int _pgCnter = 0;
    private static File _directory = new File("");  // The default is an empty string.

    /**
     *
     */
    public PageLogger(String name)
        {
        _testNa = name;
        return;
        }

    /**
     *
     */
    public void logPage(String page)
        {
        FileOutputStream logStream = null;
        try
            {
            ++_pgCnter;
            File logFile = new File(_directory, (_testNa + "_" + _pgCnter + ".log"));
//            System.out.println("Log file: " + logFile.getAbsoluteFile());
            logStream = new FileOutputStream(logFile);
            logStream.write(page.getBytes());
            }
        catch (Throwable e)
            {
            System.err.println("Encountered exception while logging to file.");
            e.printStackTrace();
            }
        finally
            {
            if (logStream != null)
                {
                try
                    {
                    logStream.close();
                    }
                catch (IOException e)
                    {
                    System.err.println("Encountered exception while closing log file.");
                    e.printStackTrace();
                    ; // We tried.
                    }
                }
            }
        return;
        }

    /**
     *
     */
    public void setDir(String directroy)
        {
        File tmpDir = new File(directroy);
        if (tmpDir.isDirectory() == false)
            {
                //System.out.println("The \"directroy\" parameter must point to an existing directory!");
            }
        _directory = tmpDir;
        return;
        }

    /**
     *
     */
    public String getDir()
        {
        return _directory.getAbsolutePath();
        }

    /**
     * LogForm
     */
    public static class LogForm implements Serializable
        {
        private String _pageText;

        public void setPageText(String thePage)
            { _pageText = thePage; }

        public String getPageText()
            { return _pageText; }
        }
    }
