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
package org.apache.beehive.controls.runtime.generator;

import java.io.Writer;
import java.util.HashMap;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;

/**
 * The VelocityGenerator class is an implementation of CodeGenerator that uses standard
 * Apache Velocity classes from the system classpath.
 */
public class VelocityGenerator extends CodeGenerator
{
    public VelocityGenerator(AnnotationProcessorEnvironment env) throws Exception
    {
        super();

        // Create a Velocity engine instance to support codgen
        _ve = new VelocityEngine();
        _ve.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
        _ve.setProperty("class." + VelocityEngine.RESOURCE_LOADER + ".class",
            ClasspathResourceLoader.class.getName());
        _ve.setProperty("velocimacro.library", 
            "org/apache/beehive/controls/runtime/generator/ControlMacros.vm");

        // Use the VelocityAptLogSystem to bridge Velocity warnings and errors back to APT
        VelocityAptLogSystem logger = new VelocityAptLogSystem(env.getMessager());
        _ve.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, logger);

        _ve.init();
    }

    /**
     * Implementation of the CodeGenerator.generate() method, using standard Velocity
     * package naming conventions and the system class loader
     */
    public void generate(GeneratorOutput genOut) throws CodeGenerationException
    {
        //
        // Create a new VelocityContext
        //
        VelocityContext vc = new VelocityContext();

        //
        // Transfer any code generation properties excepted by the templates into the context
        // 
        HashMap<String,Object> genContext = genOut.getContext();
        for(String key : genContext.keySet())
            vc.put(key, genContext.get(key));

        try
        {
            Writer genWriter = genOut.getWriter();
            Template template = getTemplate(genOut.getTemplateName());
            template.merge(vc, genWriter);    
            genWriter.close();
        }
        // never wrap RuntimeException
        catch (RuntimeException re) {
            throw re;
        }
        catch (Exception e) {
            throw new CodeGenerationException(e);
        }
    }

    //
    // Returns the requested template, and caches the result for subsequent requests using the
    // same template.
    //
    public Template getTemplate(String templateName) throws Exception
    {
        if (_templateMap.containsKey(templateName))
            return _templateMap.get(templateName);

        Template t = _ve.getTemplate(templateName);
        _templateMap.put(templateName, t);
        return t;
    }

    private HashMap<String, Template> _templateMap = new HashMap<String, Template>();
    private VelocityEngine _ve;
}
