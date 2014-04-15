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

package org.apache.beehive.controls.system.jaxrpc;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Ant task for generating java classes from a WSDL using Axis type generation.
 * Basically just a wrapper around the Axis Wsdl2Java task only performing type generation.
 */
public class AxisTypeGeneratorTask extends Task {

    private static final FileFilter WSDL_FILE_FILTER = new AxisTypeGeneratorTask.WSDLFilter();

    private String _wsdlSrc;
    private File _wsdlSrcFile;
    private URL _wsdlSrcURL;
    private File _outDir;

    /**
     * Set the wsdlSrc attribute value.
     *
     * @param wsdlSrc Location of the wsdl file(s) to process.
     *                Must be either a URL, file or directory.
     */
    public void setWsdlSrc(String wsdlSrc) {
        if (!isUrl(wsdlSrc) && !isFile(wsdlSrc)) {
            throw new BuildException("Invalid wsdlsrc attribute value, wsdlsrc must be a URL, file or directory.");
        }
        _wsdlSrc = wsdlSrc;
    }

    /**
     * Set the directory to output the generated types to.
     *
     * @param outputDir Directory.
     */
    public void setOutputDir(File outputDir) {
        _outDir = outputDir;
    }

    /**
     * Execute the task.
     *
     * @throws BuildException
     */
    public void execute() throws BuildException {

        if (_wsdlSrc == null) {
            throw new BuildException("wsdlsrc is a required attribute for this task.");
        }

        String fileName = null;
        try {
            AxisTypeGenerator atg = new AxisTypeGenerator();

            List<URI> wsdlUris = buildUriList();
            for (URI wsdlUri : wsdlUris) {
                atg.generateTypes(wsdlUri.toString(), _outDir.getPath());

            }
        }
        catch (Exception e) {
            throw new BuildException("An error occurred generating a web service for WSDL '" +
                    fileName + "'.  Cause: " + e.toString(), e);
        }
    }

    /**
     * Builds a list of WSDL URI's to generate java classes for.
     *
     * @return List of URI's
     * @throws URISyntaxException
     */
    private List<URI> buildUriList() throws URISyntaxException {

        List<URI> files = new ArrayList<URI>();
        if (_wsdlSrcURL != null) {
            files.add(_wsdlSrcURL.toURI());
        }
        else if (!_wsdlSrcFile.isDirectory()) {
            files.add(_wsdlSrcFile.toURI());
        }
        else {
            for (File f : _wsdlSrcFile.listFiles(AxisTypeGeneratorTask.WSDL_FILE_FILTER)) {
                files.add(f.toURI());
            }
        }
        return files;
    }

    /**
     * Is the parameter a URL string?
     *
     * @param src Parameter to check.
     * @return true if valid url.
     */
    private boolean isUrl(String src) {
        try {
            _wsdlSrcURL = new URL(src);
        }
        catch (MalformedURLException e) {
            // noop
        }
        return (_wsdlSrcURL != null);
    }

    /**
     * Is the parameter a file or directory?
     *
     * @param src Parameter to check.
     * @return true if file or directory.
     */
    private boolean isFile(String src) {
        _wsdlSrcFile = new File(src);
        return (_wsdlSrcFile.isFile() || _wsdlSrcFile.isDirectory());
    }

    /**
     * Filter for wsdl files.
     */
    private static class WSDLFilter implements FileFilter {
        public boolean accept(File f) {
            return (f.isFile() && (f.getName().endsWith("wsdl") || f.getName().endsWith("WSDL")));
        }
    }
}
