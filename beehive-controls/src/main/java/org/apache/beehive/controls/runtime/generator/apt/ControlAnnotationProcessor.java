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
package org.apache.beehive.controls.runtime.generator.apt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.properties.PropertySet;

import org.apache.beehive.controls.runtime.generator.*;

public class ControlAnnotationProcessor extends TwoPhaseAnnotationProcessor
{
    public ControlAnnotationProcessor(Set<AnnotationTypeDeclaration> atds,
                                      AnnotationProcessorEnvironment env)
    {
        super(atds, env);
    }

    @Override
    public void check(Declaration decl)
    {
        AnnotationProcessorEnvironment env = getAnnotationProcessorEnvironment();
        Generator genClass = null;
        if (decl.getAnnotation(ControlInterface.class) != null)
        {
            genClass = new AptControlInterface(decl, this);
        }
        else if (decl.getAnnotation(ControlExtension.class) != null)
        {
            genClass = new AptControlInterface(decl, this);

            // When a control extension is declared, values may be assigned to 
            // the properties of the parent controls.  The property constraint
            // validator is called here to ensure all values assigned satisfy any 
            // constraints declared in the properties.
            try
            {
                AnnotationConstraintAptValidator.validate(decl);
            }
            catch (IllegalArgumentException iae)
            {
                printError(decl, "propertyset.illegal.argument.error", iae.getMessage());
            }
            
        }
        else if (decl.getAnnotation(ControlImplementation.class) != null)
        {
            genClass = new AptControlImplementation(decl, this);
        }
        else if (decl.getAnnotation(PropertySet.class) != null)
        {
            new AptPropertySet(null, decl, this);
        }

        if ( genClass != null && !hasErrors() )
        {
            try
            {
                List<GeneratorOutput> genList = genClass.getCheckOutput(env.getFiler());
                if (genList == null || genList.size() == 0)
                    return;

                for (GeneratorOutput genOut : genList)
                {
                    getGenerator().generate(genOut);
                }
            }
            catch (IOException ioe)
            {
                throw new CodeGenerationException("Code generation failure: ", ioe);
            }
        }
    }

    @Override 
    public void generate(Declaration decl)
    {
        AnnotationProcessorEnvironment env = getAnnotationProcessorEnvironment();
        Generator genClass = null;
        if (decl.getAnnotation(ControlInterface.class) != null)
        {
            genClass = new AptControlInterface(decl, this);
        }
        if (decl.getAnnotation(ControlExtension.class) != null)
        {
            genClass = new AptControlInterface(decl, this);
        }
        else if (decl.getAnnotation(ControlImplementation.class) != null)
        {
            genClass = new AptControlImplementation(decl, this);
        }
        
        if ( genClass != null )
        {
            try
            {
                List<GeneratorOutput> genList = genClass.getGenerateOutput(env.getFiler());
                if (genList == null || genList.size() == 0)
                    return;

                for (GeneratorOutput genOut : genList)
                {
                    getGenerator().generate(genOut);
                }
            }
            catch (IOException ioe)
            {
                throw new CodeGenerationException("Code generation failure: ", ioe);
            }
        }
    }

    /**
     * Returns the CodeGenerator instance supporting this processor, instantiating a new
     * generator instance if necessary.
     */
    protected CodeGenerator getGenerator()
    {
        if (_generator == null)
        {
            //
            // Locate the class that wraps the Velocity code generation process
            //
            AnnotationProcessorEnvironment env = getAnnotationProcessorEnvironment();

            try 
            {
                _generator = new VelocityGenerator(env);
            }
            catch (Exception e)
            {
                throw new CodeGenerationException("Unable to create code generator", e); 
            }
        }
        return _generator;
    }

    HashMap<Declaration, Generator> _typeMap = new HashMap<Declaration,Generator>();
    CodeGenerator _generator;
}
