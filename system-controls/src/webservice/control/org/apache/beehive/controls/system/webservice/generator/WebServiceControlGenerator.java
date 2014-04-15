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
 */
package org.apache.beehive.controls.system.webservice.generator;

import org.apache.beehive.controls.system.webservice.ServiceControl;
import org.apache.beehive.controls.system.webservice.jaxrpc.ClientBindingLookupFactory;
import org.apache.beehive.controls.system.webservice.wsdl.Wsdl;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlOpParameter;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlOpReturnType;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlOperation;
import org.apache.beehive.webservice.utils.JavaClassUtils;
import org.apache.beehive.webservice.utils.databinding.BindingLookupStrategy;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Generate a service control from a WSDL using Velocity.
 */
public final class WebServiceControlGenerator {

    private static final FileFilter FILE_FILTER_WSDL = new WSDLFilter();
    private static final String WSDL_FILE_EXTENSION = "wsdl";
    private static final String SERVICE_CONTROL_FILE_EXTENSION = ".java";
    private static final String SERVICE_CONTROL_FILE_ENCODING = "UTF-8";
    private static final String VELOCITY_TEMPLATE = "org/apache/beehive/controls/system/webservice/generator/servicecontrol.vm";

    private static VelocityEngine _velocityEngine;
    private static Template _velocityTemplate;

    /**
     * Ant task / general entry point. Generate a service control for each WSDL found
     * in the wsdlDirectory.
     *
     * @param wsdlDirectory   Directory of WSDLs to process.
     * @param pkgName         Package name of generated controls. If null, package name will be generated
     *                        from the WSDL's target namespace.
     * @param wsdlRuntimePath WSDL path annotation value for generated controls.
     * @param copyWsdl        Copy the wsdl to the web service control destination directory.
     * @throws Exception
     */
    static void generateJCXs(File wsdlDirectory, File outputDir, String pkgName,
                             String wsdlRuntimePath, boolean copyWsdl)
            throws Exception {
        assert wsdlDirectory.isDirectory() : "wsdlLocation must be a directory!";
        for (File wsdlFile : wsdlDirectory.listFiles(FILE_FILTER_WSDL)) {
            generateJCX(wsdlFile, outputDir, pkgName, wsdlRuntimePath, copyWsdl, null, null);
        }
    }

    /**
     * Ant task / general entry point. Generate a service control for the specified WSDL file.
     *
     * @param wsdlFile        A WSDL file.
     * @param pkgName         Package name of generated controls. If null, package name will be generated
     *                        from the WSDL's target namespace.
     * @param wsdlRuntimePath WSDL path annotation value for generated control.
     * @param copyWsdl        Copy the wsdl to the web service control destination directory.
     * @param serviceQName    Name of the service to generate the control.
     * @param servicePortName Name of the service port used to generate the control.
     * @throws Exception
     */
    static void generateJCX(File wsdlFile, File outputDir, String pkgName,
                            String wsdlRuntimePath, boolean copyWsdl,
                            QName serviceQName, String servicePortName) throws Exception {

        assert wsdlFile.isFile() : "wsdlFile must be a file!";
        genWsc(wsdlFile.toURI(), wsdlFile.getName(), outputDir, pkgName,
               wsdlRuntimePath, copyWsdl, serviceQName, servicePortName);
    }

    /**
     * Ant task / general entry point. Generate a service control for the specified WSDL file.
     *
     * @param wsdlURL         A URL to a WSDL.
     * @param pkgName         Package name of generated controls. If null, package name will be generated
     *                        from the WSDL's target namespace.
     * @param wsdlRuntimePath WSDL path annotation value for generated control.
     * @param copyWsdl        Copy the wsdl to the web service control destination directory.
     * @param serviceQName    Name of the service to generate the control.
     * @param servicePortName Name of the service port used to generate the control.
     * @throws Exception
     */
    static void generateWSC(URL wsdlURL, File outputDir, String pkgName,
                            String wsdlRuntimePath, boolean copyWsdl,
                            QName serviceQName, String servicePortName) throws Exception {

        String wsdlFileName = GeneratorUtils.genWsdlFileNameFromURL(wsdlURL);
        System.out.println(wsdlFileName);
        genWsc(wsdlURL.toURI(), wsdlFileName, outputDir, pkgName,
               wsdlRuntimePath, copyWsdl, serviceQName, servicePortName);
    }


    /* ------------------------------------------------------------------------------------------- */
    /*                               Private Methods                                               */
    /* ------------------------------------------------------------------------------------------- */

    /**
     * Do the work to gen the wsc.
     *
     * @param wsdlURI URI of the wsdl file.
     * @param wsdlFileName The name of the Wsdl file (if copy wsdl flag set)
     * @param outputDir Directory to create the wsc in.
     * @param pkgName Java package name for the WSC.
     * @param wsdlRuntimePath Runtime path for wsdl.
     * @param copyWsdl Copy wsdl to wsc generated location?
     * @param serviceQName Service QName.
     * @param servicePortName Service port name.
     * @throws Exception On error.
     */
    private static void genWsc(URI wsdlURI, String wsdlFileName, File outputDir, String pkgName,
                               String wsdlRuntimePath, boolean copyWsdl,
                               QName serviceQName, String servicePortName) throws Exception {

        // load and parse the WSDL file
        Wsdl wsdl = new Wsdl(wsdlURI, serviceQName, servicePortName);
        if (pkgName == null) {
            pkgName = GeneratorUtils.generatePackageName(wsdl.getTargetNamespace());
        }

        //
        // determinig the wsdl runtime path:
        // 1) if the wsdlRuntimePath is not null, use its value
        // 2) if copyWsdl has been set to true, set the wsdl path to the same as the generated control
        // 3) default -- set the wsdl path to the URL of the WSDL file.
        //
        String wsdlPath;
        if (wsdlRuntimePath != null) {
            wsdlRuntimePath = wsdlRuntimePath.replace('\\', '/');
            if (wsdlRuntimePath.endsWith("/")) {
                wsdlPath = wsdlRuntimePath + wsdlFileName;
            }
            else {
                wsdlPath = wsdlRuntimePath + '/' + wsdlFileName;
            }
        }
        else if (copyWsdl) {
            String dirName = GeneratorUtils.packageNameToDirectoryName(pkgName);
            dirName = dirName.replace('\\', '/');
            if (dirName.endsWith("/")) {
                wsdlPath = dirName + wsdlFileName;
            }
            else {
                wsdlPath = dirName + '/' + wsdlFileName;
            }
        }
        else {
            wsdlPath = wsdlURI.toString();
        }

        List<MethodInfo> methodList = buildMethodList(wsdl);
        if (_velocityEngine == null) {
            initializeVelocity();
        }

        String wscClassName = GeneratorUtils.serviceNameToClassName(wsdl.getServiceName());
        VelocityContext context = new VelocityContext();
        context.put("controlPackageName", pkgName);
        context.put("serviceName", wsdl.getServiceName());
        context.put("serviceTns", wsdl.getTargetNamespace());
        context.put("wsdlPath", wsdlPath);
        context.put("methodList", methodList);
        context.put("portName", wsdl.getPortName());
        context.put("wscClassName", wscClassName);

        Writer jcxWriter = getJcxWriter(outputDir, pkgName, wscClassName);

        try {
            _velocityTemplate.merge(context, jcxWriter);
        }
        catch (Exception e) {
            throw(e);
        } finally {
            jcxWriter.close();
        }

        if (copyWsdl) {
            // adjust the wsdlPath value if it was previously set an explicit location.
            if (wsdlRuntimePath != null) {
                wsdlPath = GeneratorUtils.packageNameToDirectoryName(pkgName);
                wsdlPath = wsdlPath.replace('\\', '/');
                if (!wsdlPath.endsWith("/")) {
                    wsdlPath += '/';
                }
                wsdlPath += wsdlFileName;
            }
            File wsdlCopy = new File(outputDir, wsdlPath);
            GeneratorUtils.copyFile(wsdlURI.toURL(), wsdlCopy);
        }
    }

    /**
     * Initialize the velocity engine.
     *
     * @throws Exception
     * @throws ResourceNotFoundException
     * @throws ParseErrorException
     * @throws MethodInvocationException
     */
    static private void initializeVelocity()
            throws Exception, ResourceNotFoundException,
            ParseErrorException, MethodInvocationException {
        Properties p = new Properties();
        p.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
        p.setProperty("class." + VelocityEngine.RESOURCE_LOADER + ".class", ClasspathResourceLoader.class.getName());

        _velocityEngine = new VelocityEngine();
        _velocityEngine.init(p);
        _velocityTemplate = _velocityEngine.getTemplate(VELOCITY_TEMPLATE);
    }

    /**
     * Create a Writer for the JCX control.
     *
     * @param outputDir   The output directory.
     * @param packageName Package name for the service control.
     * @param serviceName The web service name.
     * @return java.io.Writer
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    static private Writer getJcxWriter(File outputDir, String packageName, String serviceName)
            throws FileNotFoundException, UnsupportedEncodingException {

        if (serviceName != null && packageName != null) {

            File subDir = new File(outputDir, GeneratorUtils.packageNameToDirectoryName(packageName));
            subDir.mkdirs();

            assert subDir.isDirectory() : "genJcxWriter: invalid output location: " + subDir.getPath();

            if (subDir.isDirectory()) {
                File jcx = new File(subDir, serviceName + SERVICE_CONTROL_FILE_EXTENSION);
                return new PrintWriter(jcx, SERVICE_CONTROL_FILE_ENCODING);
            }
            else {
                throw new RuntimeException(subDir.getPath() + " is not a directory");
            }
        }
        else {
            throw new RuntimeException("Service and package name must not be null");
        }
    }

    /**
     * Builds a list of methods from the beehive type metadata.
     *
     * @param wsdlDefinition A WSDL definition.
     * @return An array list containing the methods of the service control.
     */
    static private ArrayList<MethodInfo> buildMethodList(Wsdl wsdlDefinition) {
        ArrayList<MethodInfo> methodList = new ArrayList<MethodInfo>();

        for (WsdlOperation operation : wsdlDefinition.getOperations()) {
            WsdlOpReturnType returnType = operation.getReturnType();
            Class javaReturnType;

            if (returnType != null)
                javaReturnType = getClass(returnType.getXmlType(),
                                          returnType.getItemXmlType(),
                                          returnType.isArray());
            else javaReturnType = getClass(null, null, false);

            MethodInfo m = new MethodInfo();
            m.setMethodName(operation.getOperationQName().getLocalPart());
            m.setOperationName(operation.getOperationQName().getLocalPart());
            m.setReturnTypeName(JavaClassUtils.convertToReadableName(javaReturnType.getCanonicalName()));

            for (WsdlOpParameter param : operation.getParameters()) {
                Class paramType = getClass(param.getXmlType(), param.getItemXmlType(), param.isArray());
                if (param.getMode() == WsdlOpParameter.ParameterMode.IN) {
                    m.addParameter(param.getName().getLocalPart(),
                                   JavaClassUtils.convertToReadableName(paramType.getCanonicalName()));
                }
                else {
                    m.addOutParameter(param.getName().getLocalPart(),
                                      JavaClassUtils.convertToReadableName(paramType.getCanonicalName()));
                }
            }
            methodList.add(m);
        }
        return methodList;
    }

    /**
     * Find the class for the xmlType.
     *
     * @param xmlType     XmlType to map to java class.
     * @param itemXmlType If xmlType is an array type, itemXmlType specifies the component type of the array.
     * @param isArray     is xmlType an array type?
     * @return The class which the xmlType maps to. void.class if xmlType is null.
     */
    static private Class getClass(QName xmlType, QName itemXmlType, boolean isArray) {

        if (xmlType == null) {
            return void.class;
        }

        // todo: if more jaxrpc client types are supported this the client type should become a parameter of the ant task
        BindingLookupStrategy bindingLookupStrategy =
                (new ClientBindingLookupFactory()).getInstance(ServiceControl.ServiceFactoryProviderType.APACHE_AXIS);
        if (!isArray) {
            return bindingLookupStrategy.qname2class(xmlType);
        }

        //
        // this is an array type, if the itemXmlType is not null, lookup the java class mapping
        // using it.  If its not defined use the xmlType. Typically not defined for doc/lit/wrap.
        //
        Class javaReturnType;
        if (itemXmlType != null) {
            javaReturnType = bindingLookupStrategy.qname2class(itemXmlType);
        }
        else {
            javaReturnType = bindingLookupStrategy.qname2class(xmlType);
        }

        Object o = Array.newInstance(javaReturnType, 1);
        return o.getClass();
    }

    /**
     * FileFilter for WSDL files.
     */
    private static final class WSDLFilter implements FileFilter {
        public final boolean accept(File f) {
            return f.isFile() && (f.getName().toLowerCase().endsWith(WSDL_FILE_EXTENSION));
        }
    }
}
