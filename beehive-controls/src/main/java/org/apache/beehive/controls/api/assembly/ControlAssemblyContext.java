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
package org.apache.beehive.controls.api.assembly;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import com.sun.mirror.apt.Messager;

/**
 * Control assemblers are passed a ControlAssemblyContext at the time they are
 * invoked; the context allows the assemblers to interact with their external
 * environment (checking files, side-effecting deployment descriptors, emitting
 * code parameterized by the specifics of the control extension, etc).
 *
 * Beehive provides ControlAssemblyContext implementations that expose the
 * standard environments of J2EE applications and modules.  Vendor-specific
 * implementations may provide access to their specific environment information,
 * such as vendor-specific descriptors, via definition and implementation
 * of additional interfaces.  ControlAssemblers should use reflection to
 * determine if the ControlAssemblyContext implementation they are passed
 * supports a particular set of environment features.
 */
public interface ControlAssemblyContext
{
    /**
     * Providers of ControlAssemblyContext implementations MUST implement
     * Factory and newInstance to return their implementation.
     */
    interface Factory
    {
        /**
         * Creates a new instance of a ControlAssemblyContext implementation.
         *
         * @param controlIntfOrExt public interface/extension of the control
         *                         type being assembled
         * @param bindings map of control implementation bindings, null
         *                 means use defaults.
         * @param clients set of clients that use this control type.
         * @param moduleRoot file root of the J2EE module containing the
         *                   control clients to be assembled
         * @param moduleName name of the J2EE module containing the
         *                   control clients to be assembled
         * @param srcOutputRoot file root of a location where assemblers
         *                      should output any sources they create that
         *                      may need further processing before use.
         * @return a new instance of a ControlAssemblyContext implementation
         */
        ControlAssemblyContext newInstance( Class controlIntfOrExt,
                                            Map<String,String> bindings,
                                            Set<String> clients,
                                            File moduleRoot,
                                            String moduleName,
                                            File srcOutputRoot )
            throws ControlAssemblyException;
    }

    /**
     * Providers of ControlAssemblyContext implementations may implement
     * EJBModule to provide access to an EJB module environment.
     */
    interface EJBModule
    {
        // TODO: Provide more abstract helpers for common tasks.
        // E.g. addResourceRef().

        File getEjbJarXml();
    }

    /**
     * Providers of ControlAssemblyContext implementations may implement
     * WebAppModule to provide access to a webapp module environment.
     */
    interface WebAppModule
    {
        File getWebXml();
    }

    /**
     * Providers of ControlAssemblyContext implementations may implement
     * EntAppModule to provide access to an enterprise application module
     * environment.
     */
    interface EntAppModule
    {
        File getApplicationXml();
    }

    /**
     * @return the interface type of the control being assembled (annotated
     * w/ ControlExtension or ControlInterface)
     */
    Class getControlType();

    /**
     * @return the most derived interface of the control being assembled that
     * is annotated with ControlInterface (may return the same as
     * getControlType() if the control type is non-extended)
     */
    Class getMostDerivedControlInterface();

    /**
     * @return an annotation on the interface returned by
     * getControlType()
     */
    <T extends Annotation> T
        getControlAnnotation(Class<T> annotationClass);

    /**
     * @return an annotation on a method on the interface
     * returned by getControlType()
     */
    <T extends Annotation> T
        getControlMethodAnnotation(Class<T> annotationClass, Method m)
            throws NoSuchMethodException;

    /**
     * @return the defaultBinding member of the ControlInterface
     */
    String getDefaultImplClassName();

    /**
     * @return the output directory into which "compilable" source should be output.
     */
    File getSrcOutputDir();

    /**
     * @return the root of the module for which assembly is taking place.
     */
    File getModuleDir();

    /**
     * @return the name of the module for which assembly is taking place.
     */
    String getModuleName();

    /**
     * @return the set of clients (by class name) which use the control type
     */
    Set<String> getClients();

    /**
     * @return a Messager implementation that can be used to emit diagnostics during the
     *         assembly process.
     */
    Messager getMessager();

    /**
     * @return true if the assembly process reported errors via the Messager
     */
    boolean hasErrors();
}
