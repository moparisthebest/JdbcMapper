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

import org.apache.beehive.controls.api.assembly.ControlAssemblyContext;
import org.apache.beehive.controls.api.assembly.ControlAssemblyException;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * A ControlAssemblyContext implementation supporting standard EJB modules
 */
public class EJBAssemblyContext
    extends BaseAssemblyContext
    implements ControlAssemblyContext.EJBModule
{
    public static class Factory implements ControlAssemblyContext.Factory
    {
        public EJBAssemblyContext newInstance( Class controlIntfOrExt,
                                               Map<String,String> bindings,
                                               Set<String> clients,
                                               File moduleRoot,
                                               String moduleName,
                                               File srcOutputRoot )
            throws ControlAssemblyException
        {
            return new EJBAssemblyContext( controlIntfOrExt, bindings, clients,
                moduleRoot, moduleName, srcOutputRoot );
        }
    }

    protected EJBAssemblyContext( Class controlIntfOrExt, Map<String,String> bindings,
                                  Set<String> clients, File moduleRoot,
                                  String moduleName, File srcOutputRoot )
        throws ControlAssemblyException
    {
        super( controlIntfOrExt, bindings, clients, moduleRoot, moduleName, srcOutputRoot );
    }

    public File getEjbJarXml()
    {
        return new File( getModuleDir(), "META-INF" + File.separator + "ejb-jar.xml");
    }
}
