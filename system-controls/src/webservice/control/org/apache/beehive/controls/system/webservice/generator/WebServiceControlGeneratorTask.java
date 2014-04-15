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
package org.apache.beehive.controls.system.webservice.generator;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import javax.xml.namespace.QName;
import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * The web service control generator Ant task generates a web service control from a WSDL.
 * The following parameters are defined for this task:
 * <ul>
 *   <li>copyWsdl, optional, defaults to false, if true WSDL file is copied to the same location as the generated control.</li>
 *   <li>destDir, required, Location of the generated control.</li>
 *   <li>destPackageName, optional, Java package name of the generated control.</li>
 *   <li>serviceName, optional, If not set the first service in the WSDL will be used to generate the control.</li>
 *   <li>serviceNamespace, optional, Used in conjunction with the serviceName parameter to specify a service in the WSDL.</li>
 *   <li>servicePort, optional, If not set the first port in the service will be used for control generation.</li>
 *   <li>wsdlRuntimePath, optional, Specifies the location of the WSDL for the control at runtime.</li>
 *   <li>wsdlSrc, optional, Specifies the location of the WSDL(s) for control generation, must be either a URL, file or directory.</li>
 * </ul>
 *
 * <xmp>
 * <taskdef name="webservice-control-gen"
 * classname="org.apache.beehive.controls.system.webservice.generate.ServiceControlGenerationTask"
 * classpathref="wscgen.dependency.path"/>
 * <p/>
 * <p/>
 * <webservice-control-gen wsdlSrc="${build.wsdls}" destDir="${build.jcxgen}" destPackageName="${pkg.name}"/>
 * </xmp>
 */
public final class WebServiceControlGeneratorTask
        extends Task {

    private File _destdir;
    private String _wsdlSrc;
    private String _destPackageName;
    private String _wsdlRuntimePath;
    private String _serviceName;
    private String _serviceNamespace;
    private String _servicePortName;
    private boolean _copyWsdl = false;

    private File _wsdlSrcFile;
    private URL _wsdlSrcURL;

    //@todo: add option to compile generated service control

    /**
     * Create a new WebServiceControlGeneratorTask.
     */
    public WebServiceControlGeneratorTask() {
        // do nothing
    }

    /**
     * Should the WSDL used to generate the web service control be copied to the location of the generated control?
     * Optional. Defaults to false.
     * @param copyWsdl true if the WSDL used to generate the web service control should be copied to the location of
     * the control.
     */
    public void setCopyWsdl(boolean copyWsdl) {
        _copyWsdl = copyWsdl;
    }

    /**
     * Set the destination directory for the generated service controls.
     * The directory specified will be the base generation dir, if destPackageName is specified
     * the appropriate sub-directories will be created in it.
     *
     * @param destdir Location to create service control(s).
     */
    public void setDestdir(File destdir) {
        _destdir = destdir;
    }

    /**
     * The java package name for generated service controls.  If not set the generated controls
     * the package name will be generated from the target namespace of the WSDL.
     *
     * @param destPackageName Java package name. Package names may be of arbitrary depth, ex. xxx.yyy.zzz
     */
    public void setDestPackageName(String destPackageName) {
        _destPackageName = ("".equals(destPackageName)) ? null : destPackageName;
    }

    /**
     * A WSDL file may contain multiple services, this value specifies the service name of the service
     * which should be used to generate the web service control.  This is an optional value, if not specified
     * the first service found in the WSDL file will be used to generate the control. This value may only be used
     * when the input WSDL has been specified using the srcFile task attribute.
     *
     * @param serviceName Name of service to generate web service control from.
     */
    public void setServiceName(String serviceName) {
        _serviceName = ("".equals(serviceName)) ? null : serviceName;
    }

    /**
     * This value can be used in conjunction with the serviceName value to define a qualified QName value
     * for the name of the service. If the serviceName attribute has NOT been set the value of serviceNamespace
     * will be ignored.
     *
     * @param serviceNamespace Namespace of the service.
     */
    public void setServiceNamespace(String serviceNamespace) {
        _serviceNamespace = ("".equals(serviceNamespace)) ? null : serviceNamespace;
    }

    /**
     * This value can be used to specify the name of the port of a specific service to use.  This is an
     * optional value if not specified the first port defined in the service will be used.
     *
     * @param servicePortName Name of the service port to use in control generation.
     */
    public void setServicePort(String servicePortName) {
        _servicePortName = ("".equals(servicePortName)) ? null : servicePortName;
    }

    /**
     * Set the wsdlSrc attribute value.  Value may be in a form of a URL, file or diectory.
     * @param wsdlSrc wsdlSrc attribute value.
     */
    public void setWsdlSrc(String wsdlSrc) {
        if (!isUrl(wsdlSrc) && !isFile(wsdlSrc)) {
            throw new BuildException("Invalid wsdlsrc attribute value, wsdlsrc must be a URL, file or directory.");
        }
        _wsdlSrc = wsdlSrc;
    }

    /**
     * Is the parameter a URL string?
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
     * @param src Parameter to check.
     * @return true if file or directory.
     */
    private boolean isFile(String src) {
        _wsdlSrcFile = new File(src);
        return (_wsdlSrcFile.isFile() || _wsdlSrcFile.isDirectory());
    }

    /**
     * A web service control needs to be able to locate the WSDL it was generated from at runtime. This value may be
     * in the form of a URL or fully qualified location within the classpath of the application at runtime.  It is
     * not necessary to include the name of the WSDL, just its path information.
     *
     * @param wsdlRuntimePath The value for the WSDL path annotation.
     */
    public void setWsdlRuntimePath(String wsdlRuntimePath) {
        _wsdlRuntimePath = ("".equals(wsdlRuntimePath)) ? null : wsdlRuntimePath;
    }

    /**
     * Execute this task.
     */
    public void execute() {
        validateAttributeSettings();

        // xmlbeans does not use the AntClassLoader if defined when parsing
        // an xml instance - it uses the Thread's loader.  Since a WSDL is parsed
        // by this task the thread's classloader must be replaced with the ant classloader
        // in order for the parse to succeeed.
        ClassLoader cl = this.getClass().getClassLoader();
        if (cl instanceof AntClassLoader) {
            ((AntClassLoader) cl).setThreadContextLoader();
        }

        QName serviceQName = null;
        if (_serviceName != null) {
            serviceQName = new QName(_serviceNamespace, _serviceName);
        }

        try {
            if (_wsdlSrcURL != null) {
                WebServiceControlGenerator.generateWSC(_wsdlSrcURL, _destdir, _destPackageName, _wsdlRuntimePath,
                                                       _copyWsdl, serviceQName, _servicePortName);
            }
            else if (_wsdlSrcFile.isFile()) {
                WebServiceControlGenerator.generateJCX(_wsdlSrcFile, _destdir, _destPackageName, _wsdlRuntimePath,
                                                       _copyWsdl, serviceQName, _servicePortName);
            }
            else {
                WebServiceControlGenerator.generateJCXs(_wsdlSrcFile, _destdir, _destPackageName, _wsdlRuntimePath,
                                                        _copyWsdl);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new BuildException("Service control generation failed.", e);
        } finally {
            if (cl instanceof AntClassLoader) {
                ((AntClassLoader) cl).resetThreadContextLoader();
            }
        }
    }

    /**
     * Validate the attribute settings.
     */
    private void validateAttributeSettings() {

        if (_wsdlSrc == null) {
            throw new BuildException("The srcFile, srcDir or srcUrl attribute must be set");
        }

        if ((_wsdlSrcFile != null && _wsdlSrcFile.isDirectory())
                && (_serviceName != null && !"".equals(_serviceName))) {
            throw new BuildException("The serviceName attribute can only be used with the srcFile or srcUrl attributes");
        }

        if (_destdir == null) {
            throw new BuildException("The destDir attribute must be set");
        }

        if (!_destdir.isDirectory()) {
            throw new BuildException("destDir must be a directory");
        }

        if ((_wsdlRuntimePath != null && !"".equals(_wsdlRuntimePath)) && _copyWsdl) {
            log("Warning: 'wsdlRuntimePath' and 'copyWsdl' attributes have both been set.  The generated web service control will use the value of 'wsdlRuntimePath' to locate the WSDL file at runtime.");
        }
    }
}
