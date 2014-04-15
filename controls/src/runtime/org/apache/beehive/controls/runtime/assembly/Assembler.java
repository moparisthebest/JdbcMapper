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

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.assembly.ControlAssemblyContext;
import org.apache.beehive.controls.api.assembly.ControlAssemblyException;
import org.apache.beehive.controls.api.assembly.ControlAssembler;
import org.apache.beehive.controls.api.assembly.DefaultControlAssembler;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Helper class to execute assembly logic.
 */
public class Assembler
{
    /**
     * Executes basic assembly algorithm.  For each control type & impl specified, query each impl for the presence
     * of an assembler -- for each assembler present, build the specified ControlAssemblyContext implementation,
     * create an instance of the assembler and execute it.
     *
     * @param moduleRoot dir root of the module
     * @param moduleName name of the module
     * @param srcOutputRoot dir where assemblers can output source files
     * @param factoryName name of the ControlAssemblyContext factory to use
     * @param controlTypeToImpl map of control type name to control impl for all control types to be assembled in this module
     * @param controlTypeToClients map of control type name to a set of control clients (in this module) that use this type
     * @param cl classloader used to load factories and assemblers
     * @throws ControlAssemblyException
     * @throws IOException
     */
    public static void assemble( File moduleRoot,
                                 String moduleName,
                                 File srcOutputRoot,
                                 String factoryName,
                                 Map<String,String> controlTypeToImpl,
                                 Map<String,Set<String>> controlTypeToClients,
                                 ClassLoader cl )
        throws ControlAssemblyException, IOException
    {
        if ( !moduleRoot.exists() || !srcOutputRoot.exists() )
            throw new IOException( "Directories " + moduleRoot + " or " + srcOutputRoot + " don't exist!");

        if ( factoryName == null )
            throw new ControlAssemblyException( "Missing context factory names" );

        if ( cl == null )
            throw new ControlAssemblyException( "Must specify a classloader" );

        ClassLoader origCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader( cl );

        try
        {
            // Create the requested ControlAssemblyContext.Factory
            Class factoryClass = cl.loadClass( factoryName );
            ControlAssemblyContext.Factory factory = (ControlAssemblyContext.Factory)factoryClass.newInstance();

            // Iterate over control types
            Set<String> controlTypes = controlTypeToImpl.keySet();
            for ( String ct : controlTypes )
            {
                // Search for applicable ControlAssemblers as specified on the control impls
                String cImpl = controlTypeToImpl.get( ct );
                Class cImplClass = cl.loadClass( cImpl );

                ControlImplementation a = (ControlImplementation)cImplClass.getAnnotation(ControlImplementation.class);
                if ( a == null )
                    throw new ControlAssemblyException( "Control implementation class=" + cImpl + " missing ControlImplementation annotation" );

                // For each non-default ControlAssembler, create one and call it.
                Class<? extends ControlAssembler> assemblerClass = a.assembler();
                if ( !assemblerClass.equals(DefaultControlAssembler.class) )
                {
                    ControlAssembler assembler = assemblerClass.newInstance();
                    Set<String> clients = controlTypeToClients.get( ct );
                    ControlAssemblyContext cac = factory.newInstance(
                        cl.loadClass(ct), null, clients, moduleRoot, moduleName, srcOutputRoot );
                    assembler.assemble( cac );
                }
            }
        }
        catch ( ControlAssemblyException cae )
        {
            // just rethrow ControlAssemblyExceptions, which will typically come from user-provided assemblers.
            throw cae;
        }
        catch ( Throwable t )
        {
            // Not expecting any throwables other than ControlAssemblyExceptions, so consider them as
            // unexpected infrastructure issues and wrap them in a CAE.
            throw new ControlAssemblyException( "Assembly infrastructure exception",  t);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader( origCL );
        }
    }
}
