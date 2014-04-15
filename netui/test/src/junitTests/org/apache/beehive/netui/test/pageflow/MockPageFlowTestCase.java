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
package org.apache.beehive.netui.test.pageflow;

import org.apache.beehive.netui.pageflow.PageFlowActionServlet;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.internal.PageFlowInitialization;
import org.apache.beehive.netui.util.xml.XmlInputStreamResolver;
import servletunit.struts.MockStrutsTestCase;
import servletunit.HttpServletRequestSimulator;
import junit.framework.AssertionFailedError;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Base class for Page Flow test cases running under StrutsTestCase/JUnit.
 */
public abstract class MockPageFlowTestCase
        extends MockStrutsTestCase
{
    protected void setUp()
        throws Exception
    {
        super.setUp();
        PageFlowInitialization.performInitializations(config.getServletContext(), getOverrideConfigResolver());
        setActionServlet(new PageFlowActionServlet());
        getActionServlet();     // causes it to be initialized

        // The page flow class must be on classpath; if not, assert.
        try
        {
            compilePageFlow();
            Class.forName( getPageFlowClassName() );
        }
        catch ( ClassNotFoundException e )
        {
            throw new AssertionFailedError( "Page flow class " + getPageFlowClassName() + " not found." );
        }
    }

    /**
     * This allows overriding of resolution of beehive-netui-config.xml.
     * @return an {@link XmlInputStreamResolver} that resolves the location of beehive-netui-config.xml.
     */
    protected XmlInputStreamResolver getOverrideConfigResolver() {
        return null;
    }
    
    /**
     * This is support for compiling page flows from within the test, for use within an IDE.  Set the system
     * property "mockpageflowtestcase-compile-ide-beehive-root"Z
     * @throws Exception
     */
    protected void compilePageFlow()
            throws Exception
    {
        String beehiveRoot = System.getProperty("mockpageflowtestcase-compile-ide-beehive-root");
        
        if (beehiveRoot != null) {
            // Compile the page flow using apt.
            String pageFlowClassName = getPageFlowClassName();
            String junitSourceRoot = beehiveRoot + "/netui/test/src/junitTests";
            String junitBuildRoot = beehiveRoot + "/netui/build/test-classes/junitTests";
            String sourceFile = junitSourceRoot + '/' + pageFlowClassName.replace('.', '/') + ".java";
            Class aptMainClass = Class.forName("com.sun.tools.apt.Main");
            Method mainMethod = aptMainClass.getMethod("process", new Class[]{ String[].class });
            ArrayList args = new ArrayList();
            args.add("-nocompile");
            args.add("-classpath");
            args.add(System.getProperty("java.class.path"));
            args.add("-d");
            args.add(junitBuildRoot);
            args.add("-factory");
            args.add("org.apache.beehive.netui.compiler.apt.PageFlowAnnotationProcessorFactory");
            args.add("-sourcepath");
            args.add(junitSourceRoot);
            args.add("-Aweb.content.root=" + junitSourceRoot);
            args.add(sourceFile);
            String[] argsArray = (String[]) args.toArray(new String[args.size()]);
            System.out.print(MockPageFlowTestCase.class.getName());
            System.out.print(": invoking apt with command line:");
            for (int i = 0; i < argsArray.length; i++) {
                System.out.print(' ');
                System.out.print(argsArray[i]);
            }
            System.out.println();
            mainMethod.invoke(aptMainClass, new Object[]{ argsArray });
            
            
        }
    }

    protected void tearDown()
            throws Exception
    {
        super.tearDown();
    }

    /**
     * Get the fully-qualified class name for the page flow to be tested.
     */
    protected abstract String getPageFlowClassName();

    /**
     * Get the Struts module path for the page flow.
     * @return the module path, which is the parent directory path; or "" if the page flow is at the root of the webapp.
     */
    protected String getModulePath()
    {
        String modulePath = '/' + getPageFlowClassName().replace( '.', '/' );
        int lastSlash = modulePath.lastIndexOf( '/' );
        return modulePath.substring( 0, lastSlash );
    }
    
    /**
     * Run the given action on the page flow determined by {@link #getPageFlowClassName}.
     */
    protected void runAction(String actionName)
    {
        String modulePath = getModulePath();
        String actionPath = '/' + actionName + ".do";
        HttpServletRequestSimulator mockRequest = getMockRequest();

        // Set up the request path-info, servlet-path, URI, etc..
        setRequestPathInfo(modulePath, actionPath);
        mockRequest.setServletPath(modulePath + actionPath);
        mockRequest.setRequestURI(getRequestContextPath() + mockRequest.getServletPath());
        mockRequest.setContextPath(getRequestContextPath());

        actionPerform();
    }

    public void verifyForwardPath(String forwardPath) throws AssertionFailedError
    {
        // If the forward path is local, prepend the page flow's directory path.
        if (forwardPath.charAt(0) != '/') {
            forwardPath = getModulePath() + '/' + forwardPath;
        }
        super.verifyForwardPath(forwardPath);
    }

    /**
     * Verify that a particular action output was added by the action.
     * 
     * @param actionOutputName the name of the expected action output.
     * @param desiredValue the desired value of the expected action output.
     * @throws AssertionFailedError if the action output was not added, or if it had the wrong value.
     */
    public void verifyActionOutput(String actionOutputName, Object desiredValue) throws AssertionFailedError
    {
        Object value = PageFlowUtils.getActionOutput(actionOutputName, getMockRequest());
        
        if (value == null && desiredValue != null) {
            throw new AssertionFailedError("Did not find expected action output \"" + actionOutputName
                                           + "\" (request=" + getMockRequest().getServletPath() + ").");
        }
        
        if ((value != null || desiredValue != null) && ! value.equals(desiredValue)) {
            throw new AssertionFailedError("Wrong value for action output \"" + actionOutputName + "\"; expected "
                                           + desiredValue + ", got " + value + " (request=" +
                                           getMockRequest().getServletPath() + ").");
        }
    }
    
    /**
     * Get the context path for all requests.  Defaults to "/mockwebapp".  It is rarely necessary to specify this; it
     * defaults to "mockwebapp".
     */
    protected String getRequestContextPath()
    {
        return "/mockwebapp";
    }
    
    /**
     * Add an update expression and the value to be set.
     * @param expression the update expression, e.g., "actionForm.foo".
     * @param value the value to be set.
     */
    protected void addUpdateExpression( String expression, String value )
    {
        addRequestParameter( '{' + expression + '}', new String[]{ value } );
    }

    protected MockPageFlowTestCase( String name )
    {
        super( name );
    }
}
