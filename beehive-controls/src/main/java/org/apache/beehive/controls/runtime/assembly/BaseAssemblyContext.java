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
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.LinkedList;
import java.util.Queue;

import com.sun.mirror.apt.Messager;
import com.sun.mirror.util.SourcePosition;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.assembly.ControlAssemblyContext;
import org.apache.beehive.controls.api.assembly.ControlAssemblyException;
import org.apache.beehive.controls.runtime.bean.ControlUtils;

/**
 * Abstract ControlAssemblyContext implementation.  Provides a basic implementation of most non-module-specific
 * APIs, meant to be extended by module-specific types.
 */
public abstract class BaseAssemblyContext implements ControlAssemblyContext
{
    protected BaseAssemblyContext( Class controlIntfOrExt, Map<String,String> bindings,
                                   Set<String> clients, File moduleRoot,
                                   String moduleName, File srcOutputRoot )
        throws ControlAssemblyException
    {
        _controlIntfOrExt = controlIntfOrExt;
        _bindings = bindings;
        _clients = clients;
        _moduleRoot = moduleRoot;
        _moduleName = moduleName;
        _srcOutputRoot = srcOutputRoot;
        _messager = new DefaultAssemblyMessager();

        // Compute and cache "most derived ControlInterface"
        Queue<Class> q = new LinkedList<Class>();
        Class ci = controlIntfOrExt;

        while ( ci != null )
        {
            if ( ci.isAnnotationPresent(ControlInterface.class) )
            {
                _controlMostDerivedIntf = ci;
                break;
            }

            Class[] supers = ci.getInterfaces();
            for ( Class s : supers )
                q.offer( s );

            ci = q.poll();
        }

        if ( _controlMostDerivedIntf == null )
            throw new ControlAssemblyException( "Invalid control type: " + controlIntfOrExt.getName() );
    }

    public Class getControlType()
    {
        return _controlIntfOrExt;
    }

    public Class getMostDerivedControlInterface()
    {
        return _controlMostDerivedIntf;
    }

    // TODO - if we want to override class annotations on instance then here is where we will do it
    public <T extends Annotation> T
        getControlAnnotation(Class<T> annotationClass)
    {
        Class controlInterface = getControlType();
        return (T)controlInterface.getAnnotation(annotationClass);
    }

    public <T extends Annotation> T
        getControlMethodAnnotation(Class<T> annotationClass, Method m)
            throws NoSuchMethodException
    {
        Class controlInterface = getControlType();
        Method controlMethod = controlInterface.getMethod(
                m.getName(), m.getParameterTypes());

        return (T)controlMethod.getAnnotation(annotationClass);
    }

    public String getDefaultImplClassName()
    {
        Class ci = getMostDerivedControlInterface();
        ControlInterface a = (ControlInterface)
            ci.getAnnotation(ControlInterface.class);

        return ControlUtils.resolveDefaultBinding( a.defaultBinding(), ci.getName() );
    }

    public File getSrcOutputDir()
    {
        return _srcOutputRoot;
    }

    public File getModuleDir()
    {
        return _moduleRoot;
    }

    public String getModuleName()
    {
        return _moduleName;
    }

    public Set<String> getClients()
    {
        return _clients;
    }

    public Messager getMessager()
    {
        return _messager;
    }

    public boolean hasErrors()
    {
        return _nErrors > 0;
    }

    private class DefaultAssemblyMessager implements Messager
    {
        public void printError( SourcePosition pos, String msg )
        {
            printDiagnostic( "Error", pos, msg );
            _nErrors++;
        }
        public void printError( String msg )
        {
            printError( null, msg );
        }

        public void printNotice( SourcePosition pos, String msg )
        {
            printDiagnostic( "Notice", pos, msg );
        }
        public void printNotice( String msg )
        {
            printNotice( null, msg );
        }

        public void printWarning( SourcePosition pos, String msg )
        {
            printDiagnostic( "Warning", pos, msg );
        }
        public void printWarning( String msg )
        {
            printWarning( null, msg );
        }

        protected void printDiagnostic( String type, SourcePosition pos, String msg )
        {
            String fn = "<not available>";
            int line = 0;
            int column = 0;

            if ( pos != null )
            {
                fn = pos.file().getName();
                line = pos.line();
                column = pos.column();
            }

            System.out.println( type + ": (" + fn + ":" + line + ":" + column + ") " + msg );
        }
    }

    private File _moduleRoot;
    private String _moduleName;
    private File _srcOutputRoot;
    private Class _controlIntfOrExt;
    private Map<String,String> _bindings;
    private Set<String> _clients;
    private Messager _messager;
    private int _nErrors = 0;

    private Class _controlMostDerivedIntf;
}
