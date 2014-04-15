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
package org.apache.beehive.controls.runtime.generator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * The AptTask class defines a custom ANT task for invoking APT-based code generation. It
 * derives from the <javac> built-in task, so all of the attributes and nested elements of that
 * task are supported, for source list selection, classpath selection, compiler arguments,
 * etc.   Each of these options will be passed onto APT for processing.
 * <p>
 * AptTask also adds some new attributes:
 * <ul>
 * <li>gendir - specifies the directory where temporary source files that are produced during
 * generation will be kept.
 * <li>srcExtensions - provides a comma-separated list of source file extensions that are 
 * considered valid input to APT.  The default value is "*.java".
 * <li>
 */
public class AptTask extends Javac
{
    /**
     * The srcExtensions attribute can be set to a comma-separated list of source filename
     * extensions that are considered to be valid inputs to APT processing.  
     * The default value is "*.java".
     */
    public void setSrcExtensions(String srcExts)
    {
        StringTokenizer tok = new StringTokenizer(srcExts, ",");
        while (tok.hasMoreTokens())
            _srcExts.add(tok.nextToken());
    }
    
    /**
     * The srcExtensions attribute can be set to a comma-separated list of processor options
     * (of the form <i>option</i> or <i>option</i><code>=</code><i>value</i>) to be passed to
     * APT.
     */
    public void setProcessorOptions(String processorOptions)
    {
        StringTokenizer tok = new StringTokenizer(processorOptions, ",");
        while (tok.hasMoreTokens())
            _processorOptions.add(tok.nextToken());
    }

    /**
     * The gendir attribute specifies the name of the output directory for any files generated
     * as a result of calling APT.
     */
    public void setGendir(File genDir)
    {
        _genDir = genDir;
    }

    /**
     * The nocompile attribute disables compilation of the input source file list and any
     * generated sources that are derived from them.  The default value is 'false'.
     */
    public void setNocompile(boolean nocompile)
    {
        _nocompile = nocompile;
    }

    /**
     * The compileByExtension attribute causes each input source extension to be compiled
     * independently (and sequentially).  This is useful when one type of extensio can
     * possibly depend upon the generation output from another. The default value 'false'.
     */
    public void setCompileByExtension(boolean compileByExt)
    {
        _compileByExt = compileByExt;
    }

    /**
     * Override the implementation of scanDir, to look for additional files based upon any
     * specified source extensions
     */
    protected void scanDir(File srcDir, File destDir, String[] files, String ext) 
    {
        // If no source path was specified, we effectively created one by adding the generation
        // path.   Because of this, we need to be sure and add all source dirs to the path too.
        if (!_hasSourcepath)
        {
            Path srcPath = new Path(getProject()); 
            srcPath.setLocation(srcDir);
            Path sp = getSourcepath();
            sp.append(srcPath);
            setSourcepath(sp);
        }

        GlobPatternMapper m = new GlobPatternMapper();
        m.setFrom(ext);
        m.setTo("*.class");
        SourceFileScanner sfs = new SourceFileScanner(this);
        if (ext.equals("*.java"))
        {
            File[] newFiles = sfs.restrictAsFiles(files, srcDir, destDir, m);
            if (newFiles.length > 0) 
            {
                File[] newCompileList = new File[compileList.length + newFiles.length];
                System.arraycopy(compileList, 0, newCompileList, 0, compileList.length);
                System.arraycopy(newFiles, 0, newCompileList, compileList.length, 
                                 newFiles.length);
                compileList = newCompileList;
            }
        }
        else
        {
            String [] newSources = sfs.restrict(files, srcDir, destDir, m);
            int extLen = ext.length() - 1;  // strip wildcard
            if (newSources.length > 0) 
            {
                File[] newCompileList = new File[compileList.length + newSources.length];
                System.arraycopy(compileList, 0, newCompileList, 0, compileList.length);
                try
                {
                    FileUtils fileUtils = FileUtils.newFileUtils();
                    for (int j = 0; j < newSources.length; j++)
                    {
                        String toName = 
                            newSources[j].substring(0, newSources[j].length() - extLen) + 
                            ".java";
                                                               
                        File srcFile = new File(srcDir, newSources[j]);
                        File dstFile = new File(_genDir, toName);
                        fileUtils.copyFile(srcFile, dstFile, null, true, true);
                        newCompileList[compileList.length + j] = dstFile;
                    }
                }
                catch (IOException ioe)
                {
                    throw new BuildException("Unable to copy " + ext + " file", ioe, 
                                             getLocation());
                }
                compileList = newCompileList;
            }
        }
    }

    public void execute() throws BuildException
    {
        // Ensure that the gendir attribute was specified
        if (_genDir == null)
            throw new BuildException("Missing genDir attribute: must be set to codegen output directory", getLocation());


        // If no source extension specified, then just process .java files
        if (_srcExts.size() == 0)
            _srcExts.add("*.java");

        // Save whether a user sourcepath was provided, and if so, the paths
        String[] userSourcepaths = null;
        _hasSourcepath = getSourcepath() != null;
        if ( _hasSourcepath )
            userSourcepaths = getSourcepath().list();

        // The generation dir is always added to the source path for compilation
        Path genPath = new Path(getProject()); 
        genPath.setLocation(_genDir);
        setSourcepath(genPath);
        
        // If the user sourcepath specifies subdirs underneath the srcdir, then we need to add
        // the corresponding subdirs under the gendir to the source path for compilation.
        // For example, if the user sourcepath is "<webapp-root>;<webapp-root>\WEB-INF\src",
        // then the sourcepath for compilation should include "<gen-dir>;<gen-dir>\WEB-INF\src".
        if ( _hasSourcepath )
        {
            String srcDirPath = (getSrcdir().list())[0]; // TODO: handle multiple srcdirs
            for ( String p: userSourcepaths )
            {
                if ( p.startsWith( srcDirPath ) && p.length() > srcDirPath.length() )
                {
                    File genDirElem = new File( _genDir, p.substring( srcDirPath.length()+1 ));
                    Path gp = new Path(getProject());
                    gp.setLocation( genDirElem );
                    setSourcepath(gp);
                }
            }
        }

        //
        // Select the executable (apt) and set fork = true
        //
        setExecutable("apt");
        setFork(true);

        //
        // Specify the code generation output directory to APT
        // 
        Commandline.Argument arg = createCompilerArg();
        arg.setValue("-s");
        arg = createCompilerArg();
        arg.setFile(_genDir);

        //add the -nocompile flag if set to true
        if(_nocompile)
        {
            Commandline.Argument ncarg = createCompilerArg();
            ncarg.setValue("-nocompile");
        }
        
        //
        // Add processor options.
        //
        for (Object i : _processorOptions)
        {
            Commandline.Argument optionArg = createCompilerArg();
            optionArg.setValue("-A" + i);
        }

        checkParameters();
        resetFileLists();

        // Iterate through the list of input extensions, matching/dependency checking based
        // upon the input list.
        for (int j = 0; j < _srcExts.size(); j++)
        {
            String ext = (String)_srcExts.get(j);
            Vector<File> inputFiles = new Vector<File>();

            // scan source directories and dest directory to build up
            // compile lists
            String[] list = getSrcdir().list();
            File destDir = getDestdir();
            for (int i = 0; i < list.length; i++) 
            {
                File srcFile = getProject().resolveFile(list[i]);
                if (!srcFile.exists()) {
                    throw new BuildException("srcdir \""
                                         + srcFile.getPath()
                                         + "\" does not exist!", getLocation());
                }

                //
                // The base <javac> algorithm is tweaked here, to allow <src> elements
                // to contain a list of files _or_ a list of directories to scan.
                //
                if (srcFile.isDirectory())
                {
                    DirectoryScanner ds = this.getDirectoryScanner(srcFile);
                    String[] files = ds.getIncludedFiles();
                    scanDir(srcFile, destDir != null ? destDir : srcFile, files, ext);
                }
                else
                {
                    //
                    // BUGBUG: Because these bypass scanning, they also bypass dependency chks :(
                    //
                    if (srcFile.getPath().endsWith(ext.substring(1)))
                        inputFiles.add(srcFile);
                }
            }

            if (inputFiles.size() != 0)
            {
                File[] newCompileList = new File[compileList.length + inputFiles.size()];
                inputFiles.toArray(newCompileList);
                System.arraycopy(compileList, 0, newCompileList, inputFiles.size(), 
                             compileList.length);
                compileList = newCompileList;
            }

            //
            // If processing/compiling on a per-extension basis, then handle the current list,
            // then reset the list fo files to compile before moving to the next extension
            //
            if (_compileByExt)
            {
                compile();
                resetFileLists();
            }
        }

        //
        // If not processing on a per-extension basis, then compile the entire aggregated list
        //
        if (!_compileByExt)
            compile();
    }

    protected boolean _nocompile = false;
    protected boolean _compileByExt = false;
    protected boolean _hasSourcepath;
    protected File _genDir;
    protected Vector/*<String>*/ _srcExts = new Vector/*<String>*/();
    protected Vector/*<String>*/ _processorOptions = new Vector/*<String>*/();
}
