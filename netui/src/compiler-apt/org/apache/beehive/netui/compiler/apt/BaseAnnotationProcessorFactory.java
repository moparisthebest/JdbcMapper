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
package org.apache.beehive.netui.compiler.apt;

import org.apache.beehive.netui.compiler.typesystem.impl.env.CoreAnnotationProcessorEnvImpl;
import org.apache.beehive.netui.compiler.typesystem.impl.DelegatingImpl;
import org.apache.beehive.netui.compiler.typesystem.impl.WrapperFactory;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessor;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;

import java.util.Set;
import java.util.Iterator;

import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;

/**
 * Slim Sun apt annotation processor factory that delegates to our "core" (apt-independent) layer.
 */
public abstract class BaseAnnotationProcessorFactory
        implements AnnotationProcessorFactory
{
    /**
     * Get a Sun annotation processor for the given annotation type declarations.  This just wraps the type declarations
     * in our wrapper types, and returns an annotation processor that delegates to our "core" (apt-independent)
     * processor to do the real work.
     */
    public final AnnotationProcessor getProcessorFor(Set annotationTypeDeclarations, AnnotationProcessorEnvironment env)
    {

        CoreAnnotationProcessorEnv coreEnv = CoreAnnotationProcessorEnvImpl.get( env );
        AnnotationTypeDeclaration[] atds = new AnnotationTypeDeclaration[ annotationTypeDeclarations.size() ];
        int j = 0;
        for ( Iterator i = annotationTypeDeclarations.iterator(); i.hasNext(); )
        {
            // Wrap each Sun/Mirror annotation type declaration with our core AnnotationTypeDeclaration.
            com.sun.mirror.declaration.AnnotationTypeDeclaration decl =
                    ( com.sun.mirror.declaration.AnnotationTypeDeclaration ) i.next();
            atds[ j++ ] = WrapperFactory.get().getAnnotationTypeDeclaration( decl );
        }

        CoreAnnotationProcessor ap = getCoreProcessorFor( atds, coreEnv );
        return ap != null ? new DelegatingAnnotationProcessor( ap ) : null;
    }

    private static class DelegatingAnnotationProcessor
            extends DelegatingImpl
            implements AnnotationProcessor
    {
        public DelegatingAnnotationProcessor( CoreAnnotationProcessor delegate )
        {
            super( delegate );
        }

        public void process()
        {
            ( ( CoreAnnotationProcessor ) getDelegate() ).process();
        }
    }

    /**
     * Get the core annotation processor which is appropriate for the given annotations.  Note that this is "our"
     * annotation processor, not a Sun apt annotation processor.  See
     * {@link BaseAnnotationProcessorFactory#getProcessorFor} for the place where a Sun annotation processor is returned.
     */
    protected abstract CoreAnnotationProcessor getCoreProcessorFor( AnnotationTypeDeclaration[] annotationTypeDecls,
                                                                    CoreAnnotationProcessorEnv env );
}
