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
package org.apache.beehive.controls.runtime.packaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Manifest;
import org.apache.tools.ant.taskdefs.ManifestException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipOutputStream;

/**
 * The ControlTask class extends the standard ant Jar task to perform
 * additional processing for JAR files that contain Beehive Controls.
 */
public class ControlJarTask extends Jar
{
    private static FileUtils fileUtils = FileUtils.newFileUtils();

    /**
     * Step #1: Wrap the implementation of Zip.grabResources.  This method identifies the
     * set of resources to be stored in the JAR file.   For each added/updated resource,
     * the overrided method will look for an associated .manifest file that any
     * JAR manifest data to add/update in the JAR manifest.
     */
    protected Resource[][] grabResources(FileSet[] filesets) 
    {
        //
        // Get the list of resources being added/updated by calling Zip.grabResources
        //
        Resource [][] resources = super.grabResources(filesets);

        //
        // Iterate through the resources for each input FileSet, looking for an associated
        // manifest file.
        //
        for (int i = 0; i < filesets.length; i++)
        {
            if (resources[i].length == 0)
                continue;

            File base = filesets[i].getDir(getProject());
            Resource [] setResources = resources[i];

            for (int j = 0; j < setResources.length; j++)
            {
                File manifestFile = 
                    fileUtils.resolveFile(base, setResources[j].getName() + ".manifest");
                if (manifestFile.exists())
                    manifestFiles.add(manifestFile);
            }
        }

        return resources;
    }

    /**
     * Step #2: Override Jar.initZipOutputStream to inject manifest sections.  This is done
     * by treating them as if they were 'inline' entries, from a <jar> task perspective.
     */
    protected void initZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException 
    {
        if (manifestFiles.size() != 0)
        {
            //
            // Create a default (empty) manifest
            //
            Manifest mergeManifest = Manifest.getDefaultManifest();

            //
            // Iterate through each found manifest file, merging its contents into the
            // merge manifest
            //
            for (File manifestFile : manifestFiles)
            {
                FileInputStream fis = null;
                try
                {
                    fis = new FileInputStream(manifestFile);
                    Manifest resourceManifest = new Manifest(new InputStreamReader(fis));
                    mergeManifest.merge(resourceManifest);
                }
                catch (IOException ioe)
                {
                    throw new BuildException("Unable to read manifest file:" + manifestFile, ioe);
                }
                catch (ManifestException me)
                {
                    throw new BuildException("Unable to process manifest file: "+ manifestFile, me);
                }
                finally 
                {
                    if (fis != null) fis.close();
                }
            }

            //
            // Set the merge manifest as the 'configured' manifest.   This will treat its
            // contents as if they had been included inline with the <jar> task, with
            // similar precedence rules.
            //
            try
            {
                addConfiguredManifest(mergeManifest);
            }
            catch (ManifestException me)
            {
                throw new BuildException("Unable to add new manifest entries:" + me);
            }
        }

        super.initZipOutputStream(zOut);
    }

    protected void addToManifest(Manifest jarManifest, List<File> mergeList)
    {
    }

    /**
     * Reset the manifest file list to be empty
     */
    protected void cleanUp()
    {
        manifestFiles = new Vector<File>();
    }

    /**
     * Contains the set of manifest entries to merge into the JAR manifest
     */
    private List<File> manifestFiles = new Vector<File>();
}
