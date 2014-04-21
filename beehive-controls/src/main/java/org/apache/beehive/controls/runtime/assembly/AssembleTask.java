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
package org.apache.beehive.controls.runtime.assembly;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.FileSet;
import org.apache.beehive.controls.runtime.generator.apt.ControlClientManifest;
import org.apache.beehive.controls.api.assembly.ControlAssemblyException;

/**
 * AssembleTask defines a custom ant task to perform control assembly.
 * <p>
 * The core assembly algorithm is documented and implemented in {@link Assembler}.
 * <p>
 * Required attributes:<br>
 * <b>moduleDir</b>: path to the root of J2EE module on which to perform assembly.<br>
 * <b>srcOutputDir</b>: path to the dir where control assemblers may output source files.
 * It may be necessary to run additional build steps in order to process such files (for example,
 * if an assembler outputs Java source code, that code may need to be compiled).<br>
 * <b>contextFactoryClassname</b>: fully qualified classname of a factory class that implements
 * {@link org.apache.beehive.controls.api.assembly.ControlAssemblyContext.Factory}.  Typically this
 * would depend on the type of module on which assembly is being run (EJB, webapp, etc).  Different
 * contexts will expose different APIs to control assemblers (making different descriptors available,
 * etc).
 * <p>
 * Supported nested elements:<br>
 * <b>classpath</b>: specifies the classpath that will be searched for control interfaces/implementations,
 * control clients and control assemblers.<br>
 * <b>fileset</b>: specifies the control client manifests that should be processed by this assembly call.<br>
 * <p>
 * An example usage of the AssembleTask in an ant build script (build.xml):
 * <p>
<xmp>
 <taskdef name="assemble" classname="org.apache.beehive.controls.runtime.assembly.AssembleTask"
             classpathref="controls.dependency.path" onerror="report" />  	     

 <assemble moduleDir="${build.beans}"
           srcOutputDir="${build.beansrc}"
           contextFactoryClassname="org.apache.beehive.controls.runtime.assembly.EJBAssemblyContext$Factory">
     <classpath>
        <path refid="test.classpath"/>
        <pathelement location="${build.beans}"/>
     </classpath>
     <fileset dir="${build.beans}">
         <include name="**\*.controls.properties"/>
     </fileset>
 </assemble>
</xmp>
 */
public class AssembleTask extends Task
{
    public AssembleTask()
    {
        // do nothing
    }

    public void setContextFactoryClassName(String contextFactoryClassName)
    {
        _contextFactoryClassName = contextFactoryClassName;
    }

    public void setModuleDir( File moduleDir )
    {
        _moduleDir = moduleDir;
    }

    public void setModuleName( String moduleName )
    {
        _moduleName = moduleName;
    }

    public void setSrcOutputDir( File srcOutputDir )
    {
        _srcOutputDir = srcOutputDir;
    }

    public void setBindingFile(File bindingFile)
    {
        _bindingFile = bindingFile;
    }

    public FileSet createFileset()
    {
        _clientManifestFileSet = new FileSet();
        return _clientManifestFileSet;
    }

    // used to set classpath as an attribute
    public void setClasspath(Path classpath)
    {
        _classPath = new Path(getProject());
        _classPath.append(classpath);
    }

    // used to set classpath as a nested element
    public Path createClasspath()
    {
        _classPath = new Path(getProject());
        return _classPath;
    }

    public void execute()
    {
        validateAttributeSettings();

        if (_clientManifestFileSet == null)
        {
            log("No input fileset specified, nothing to do.");
            return;
        }

        // get list of input files as list of ControlRefs files
        File filesetDir = _clientManifestFileSet.getDir(getProject());
        String[] clientManifests = _clientManifestFileSet.
            getDirectoryScanner(getProject()).getIncludedFiles();

        if (clientManifests.length == 0)
        {
            log("Input fileset contained no files, nothing to do.");
            return;
        }

        List<File> manifestFiles = new ArrayList<File>();
        for ( String mf : clientManifests )
        {
            File f = new File(filesetDir, mf );
            if (!f.exists())
            {
                log("File " + f.getAbsolutePath() +
                        " in input fileset does not exist.");
                continue;
            }

            manifestFiles.add(f);
        }

        // REVIEW: nested control usage is handled poorly right now.
        // Need to refine how we pick up control client manifests, especially
        // for manifests inside control jars (instead of blindly scanning and
        // including all manifests inside all jars, should base it on actual nested
        // control usage as analyzed by starting at non-control clients).
        try
        {
            // Build map of control types to assemble by scanning supplied manifests

            Map<String,String> controlTypesToImpls = new HashMap<String,String>();
            Map<String,Set<String>> controlTypesToClients =
                new HashMap<String, Set<String>>();

            for ( File mf : manifestFiles )
            {
                ControlClientManifest ccmf = new ControlClientManifest( mf );
                String controlClient = ccmf.getControlClient();
                List<String> controlTypes = ccmf.getControlTypes();
                for ( String ct : controlTypes )
                {
                    controlTypesToImpls.put( ct, ccmf.getDefaultImpl( ct ) );
                    Set<String> clients = controlTypesToClients.get( ct );
                    if (clients == null)
                    {
                        clients = new TreeSet<String>();
                        controlTypesToClients.put( ct, clients );
                    }
                    clients.add( controlClient );
                }
            }

            // Build classloader to do loading
            //
            // TODO: The module dir should probably be in the classpath, since it seems reasonable
            // for assemblers to want access to the classes in the module.

            String[] classpaths = _classPath == null ? new String[0] : _classPath.list();
            ClassLoader cl = buildClassLoader( classpaths, Assembler.class.getClassLoader() );

            Assembler.assemble( _moduleDir, _moduleName, _srcOutputDir, _contextFactoryClassName,
                controlTypesToImpls, controlTypesToClients, cl );
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BuildException("Assembly failed.", e);
        }
    }

    private void validateAttributeSettings() throws BuildException
    {
        if (_contextFactoryClassName == null)
            throw new BuildException("The contextFactoryClassName attribute must be set");

        if (_moduleDir == null)
            throw new BuildException("The moduleDir attribute must be set");

        if (_srcOutputDir == null)
            throw new BuildException("The srcOutputDir attribute must be set");
    }

    private ClassLoader buildClassLoader( String[] paths, ClassLoader parentCL)
        throws ControlAssemblyException
    {
        List list = new ArrayList();
        for (int i=0; i<paths.length; i++)
        {
            try
            {
                File file = new File(paths[i]);
                String filePath = file.getCanonicalPath();
                // ending slash is important for URLs that represent directories
                if (!filePath.toLowerCase().endsWith(".jar") &&
                    !filePath.endsWith("/") )
                {
                    filePath += "/";
                }
                URL url = new URL("file:" + filePath);
                list.add(url);
            }
            catch (IOException e)
            {
                throw new ControlAssemblyException("Unable to include path " +
                    paths[i] + " in classpath. Caught " +
                    e.getClass().getName() + " trying to form this path as a URL.", e);
            }
        }

        URL[] urlArray = new URL[list.size()];
        urlArray = (URL[])list.toArray(urlArray);

        return new URLClassLoader(urlArray, parentCL);
    }

    // ant parameter values
    protected String  _contextFactoryClassName;
    protected File    _moduleDir;
    protected String  _moduleName;
    protected File    _srcOutputDir;
    protected File    _bindingFile;
    protected Path    _classPath;
    protected FileSet _clientManifestFileSet;
}
