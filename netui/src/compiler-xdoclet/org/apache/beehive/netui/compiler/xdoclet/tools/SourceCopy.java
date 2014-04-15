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
package org.apache.beehive.netui.compiler.xdoclet.tools;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Copy;

import java.util.Enumeration;
import java.io.*;

/**
 * Extend the default ant copy task to do some verification on java and pageflow source files.
 */ 
public class SourceCopy extends Copy
{
    protected static final String PACKAGE = "package";

    /**
     * Override doFileOperations to verify things that a source file's
     * package name is the same as directory name.
     * 
     * Note: we are not checking that anything named .jpf extends PageFlowController because
     * that would require more advanced source parsing; the netui doclet task handles this by
     * making sure that if we find a class that extends PageFlowController, there is a corresponding
     * jpf file.
     */
    protected void doFileOperations()
    {
        if (fileCopyMap.size() > 0)
        {
            log("Verifying " + fileCopyMap.size()
                    + " source file" + (fileCopyMap.size() == 1 ? "" : "s")
                    + " before copy");

           
            Enumeration e = fileCopyMap.keys();
            while (e.hasMoreElements())
            {
                String fromFile = (String) e.nextElement();
                
                if ( !fromFile.endsWith( ".java" ) && !fromFile.endsWith( ".jpf" ) &&
                        !fromFile.endsWith( ".app" ) )
                    continue;

                try
                {
                    File sourceFile = new File( fromFile );
                    if ( sourceFile.exists() && sourceFile.isFile() )
                    {
                        String packageName = getPackage( sourceFile );
                        if ( packageName != null && packageName.length() > 0 )
                        {
                            String path = sourceFile.getParentFile().getPath();
                            path = path.replace( File.separatorChar, '.' );
                            if ( !path.endsWith( packageName ) )
                            {
                                throw new BuildException( "File " + fromFile + " failed verification because its package (" +
                                        packageName + ") differs from its directory location. This will cause errors with the pageflow compiler." );
                            }
                        }                        
                    }
                }
                catch (Exception ioe)
                {
                    String msg = "Failed to verify " + fromFile
                            + " due to " + ioe.getMessage();
                    throw new BuildException(msg, ioe, getLocation());
                }
            }
        }

        super.doFileOperations();
    }

    /**
     * Get the package name of a java source file. This just does some really basic parsing to find
     * the first line that starts with "package", and then returns the rest of that line before the
     * first semicolon. It won't catch any possible way that a package could be specified (after a comment,
     * with line breaks, etc) but it's a good-faith effort to determine the package name. If no package
     * name is found, the file will be skipped during the verification process.
     * @param sourceFile
     * @return
     * @throws IOException
     */ 
    protected String getPackage(File sourceFile)
            throws IOException
    {
        BufferedReader in = null;
        String packageName = null;
        String encoding = getEncoding();

        try
        {
            if (encoding == null)
            {
                in = new BufferedReader(new FileReader(sourceFile));
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(
                                new FileInputStream(sourceFile),
                                encoding));
            }

            String line = in.readLine();
            while (line != null)
            {
                if (line.length() != 0)
                {
                    line = line.trim();
                    if (line.startsWith(PACKAGE))
                    {
                        int semi = line.indexOf(";");
                        if ( semi != -1 )
                        {
                            packageName = line.substring(PACKAGE.length() + 1, semi);
                            packageName = packageName.trim();
                        }
                        break;
                    }
                }
                line = in.readLine();
            }
        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
        }

        return packageName;
    }
}
