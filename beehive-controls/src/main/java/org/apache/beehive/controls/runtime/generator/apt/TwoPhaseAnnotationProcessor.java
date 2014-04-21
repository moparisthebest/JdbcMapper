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

import java.util.*;
import java.text.MessageFormat;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;

import org.apache.beehive.controls.runtime.generator.CodeGenerationException;

/**
 * The TwoPhaseAnnotationProcessor class is an abstract class that implements the APT
 * AnnotationProcessor interface.   It breaks the work of the process() method of the
 * AnnotationProcessor down into two distinct phases, represented as abstract method
 * of TwoPhaseAnnotationProcessor that are to be implemented by concrete subclasses.
 * <p>
 * The two phases of processing are:
 * <ul>
 * <li>The <b>check</b> phase is used to validate input Declarations that have been
 * annotated with annotations claimed by the processor to ensure that it 
 * is semantically valid.  If the presence of the input Declaration implies the need
 * to add new files, and those files need to be visible during the check phase for
 * other Declarations, then the AnnotationProcessorEnvironment's Filer API should be
 * used to add those files in this phase.  The adding of such files at this point
 * should typically not result in their emission to persistent storage (i.e. disk),
 * but rather be kept in memory to be referenced by the check phase of other
 * Declarations.
 * <li>The <b>generate</b> phase will actually emit any source, binary, or class files
 * that are derived from the input Declaration, including files added via the Filer
 * API during the check phase.  The Filer API may also be used in this phase to add
 * new files, however, such additions will not be visible during the check phase of
 * any Declarations.
 * </ul>
 * <p>
 * The benefits of breaking process() down into check() and generate() phases are:
 * <ol>
 * <li>Makes it possible to perform the semantic validation of Declarations without
 * necessarily resulting in code generation.
 * <li>Provides a clearer association between input Declarations and generator output.
 * </ol>
 * TwoPhaseAnnotationProcessor is intended provide a uniform mechanism for writing
 * AnnotationProcessor implementations that can be used in tooling environments more
 * sophisticated than command-line tools (that may not do all their work on source
 * in a single pass).  Such environments will typically also provide implementations
 * of the AnnotationProcessorEnvironment and associated interfaces (Messager,
 * Filer etc).
 */
abstract public class TwoPhaseAnnotationProcessor
        extends Diagnostics
        implements AnnotationProcessor
{
    public TwoPhaseAnnotationProcessor(Set<AnnotationTypeDeclaration> atds,
                                       AnnotationProcessorEnvironment env)
    {
        super( env );
        _atds = atds;
        _locale = Locale.getDefault();
    }

    /**
     * Implements AnnotationProcessor.process() as two phases, "check" and "generate".
     * "generate" will not be called if "check" emitted any errors (via printError()).
     */
    public void process() 
    { 
        check();

        // Do not call generate if check resulted in errors.
        if ( !hasErrors() )
            generate();
    }

    /**
     * Performs semantic validation of input Declarations that are annotated with
     * annotations claimed by this AnnotationProcessor.
     */
    public void check()
    {
        for (AnnotationTypeDeclaration atd : _atds)
        {
            Collection<Declaration> decls = getAnnotationProcessorEnvironment().getDeclarationsAnnotatedWith(atd);
            for (Declaration decl : decls)
            {
                check(decl);
            }
        }
    }

    /**
     * Emits additional artifacts for input Declarations that are annotated with
     * annotations claimed by this AnnotationProcessor.
     */
    public void generate() throws CodeGenerationException
    {
        for (AnnotationTypeDeclaration atd : _atds)
        {
            Collection<Declaration> decls = getAnnotationProcessorEnvironment().getDeclarationsAnnotatedWith(atd);
            for (Declaration decl : decls)
            {
                generate(decl);
            }
        }
    }
    
    /**
     * The check method is responsible for all semantic validation of the input Declaration.
     * <p>
     * All semantic errors/warnings associated with the input Declaration should
     * be output during check via the {@link #printError} and
     * {@link #printWarning} methods.  If an implementation
     * bypasses printError, it must override {@link #hasErrors()} to ensure correct behaviour.
     * <p>
     * If the presence of the input Declaration implies the need to add new files,
     * and those files need to be visible during the check phase for
     * other Declarations, then the AnnotationProcessorEnvironment's Filer API should be
     * used to add those files in this phase.  The adding of such files at this point
     * should typically not result in their emission to persistent storage (i.e. disk),
     * but rather be kept in memory to be referenced by the check phase of other
     * Declarations.
     */
    abstract public void check(Declaration decl);

    /**
     * The generate method is responsible for the generation of any additional artifacts
     * (source, class, or binary) that are derived from the input Declaration.
     */
    abstract public void generate(Declaration decl);

    //
    // Helper functions for handling diagnostics
    //

    /**
     * Report an error detected during the "check" phase.  The presence of errors
     * will suppress execution of the "generate" phase.
     */
    public void printError( Declaration d, String id, Object... args )
    {
        addError( d, id, args );
    }

    /**
     * Report a warning detected during the "check" phase.  The presence of warnings
     * will not affect execution of the "generate" phase.
     */
    public void printWarning( Declaration d, String id, Object... args )
    {
        addWarning( d, id, args );
    }

    protected String getResourceString( String id, Object... args )
    {
        ResourceBundle rb = ResourceBundle.getBundle(
            this.getClass().getPackage().getName() + ".strings", _locale );
	    String pattern = rb.getString(id);
	    return MessageFormat.format(pattern, args);
    }

    Set<AnnotationTypeDeclaration> _atds;
    Locale _locale;
}
