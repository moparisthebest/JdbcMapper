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
package org.apache.beehive.netui.compiler.processor;

import java.util.*;
import java.text.MessageFormat;

import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessor;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.Declaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.MethodDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.Diagnostics;
import org.apache.beehive.netui.compiler.FatalCompileTimeException;
import org.apache.beehive.netui.compiler.CompilerUtils;

/**
 * The TwoPhaseCoreAnnotationProcessor class is an abstract class that implements the APT
 * CoreAnnotationProcessor interface.   It breaks the work of the process() method of the
 * CoreAnnotationProcessor down into two distinct phases, represented as abstract method
 * of TwoPhaseCoreAnnotationProcessor that are to be implemented by concrete subclasses.
 * <p/>
 * The two phases of processing are:
 * 
 * The <b>check</b> phase is used to validate input Declarations that have been
 * annotated with annotations claimed by the processor to ensure that it
 * is semantically valid.  If the presence of the input Declaration implies the need
 * to add new files, and those files need to be visible during the check phase for
 * other Declarations, then the CoreAnnotationProcessorEnv's Filer API should be
 * used to add those files in this phase.  The adding of such files at this point
 * should typically not result in their emission to persistent storage (i.e. disk),
 * but rather be kept in memory to be referenced by the check phase of other
 * Declarations.
 * The <b>generate</b> phase will actually emit any source, binary, or class files
 * that are derived from the input Declaration, including files added via the Filer
 * API during the check phase.  The Filer API may also be used in this phase to add
 * new files, however, such additions will not be visible during the check phase of
 * any Declarations.
 * </ul>
 * <p/>
 * The benefits of breaking process() down into check() and generate() phases are:
 * 
 * Makes it possible to perform the semantic validation of Declarations without
 * necessarily resulting in code generation.
 * Provides a clearer association between input Declarations and generator output.
 * </ol>
 * TwoPhaseCoreAnnotationProcessor is intended provide a uniform mechanism for writing
 * CoreAnnotationProcessor implementations that can be used in tooling environments more
 * sophisticated than command-line tools (that may not do all their work on source
 * in a single pass).  Such environments will typically also provide implementations
 * of the CoreAnnotationProcessorEnv and associated interfaces (Messager,
 * Filer etc).
 */
public abstract class TwoPhaseCoreAnnotationProcessor
        extends Diagnostics
        implements CoreAnnotationProcessor
{
    protected TwoPhaseCoreAnnotationProcessor( AnnotationTypeDeclaration[] atds, CoreAnnotationProcessorEnv env )
    {
        super( env );
        _atds = atds;
        _locale = Locale.getDefault();
    }

    /**
     * Implements CoreAnnotationProcessor.process() as two phases, "check" and "generate".
     * "generate" will not be called if "check" emitted any errors (via printError()).
     */
    public void process()
    {
        try
        {
            check();

            boolean isReconcilePhase = false;

            // In the Eclipse IDE's integration between JDT and APT, annotation processors
            // run in two phases -- reconcile and build.  These translate into the
            // check and generate phases for a TwoPhaseAnnotationProcessor.  In order to
            // optimize for hosting in this environment (and other IDE-centric AP environments)
            // the generate phase can be cut out when performing only the check phase.
            // Custom AP environments that wish to control this shoudl set the "phase" flag of the
            // annotation processor to "RECONCILE".
            try {
                String phase = (String)CompilerUtils.isReconcilePhase(getAnnotationProcessorEnvironment());
                isReconcilePhase = "RECONCILE".equals(phase);
            }
            catch(FatalCompileTimeException e) {
                e.printDiagnostic(this);
            }

            // Do not call generate if check resulted in errors of if the AP is running in
            // a phase called "reconcile"
            if (!isReconcilePhase && !hasErrors()) {
                generate();
            }
        }
        catch ( FatalCompileTimeException e )
        {
            e.printDiagnostic( this );
        }
    }

    /**
     * Performs semantic validation of input Declarations that are annotated with
     * annotations claimed by this CoreAnnotationProcessor.
     */
    public void check()
        throws FatalCompileTimeException
    {
        HashSet declsToCheck = new HashSet();

        //
        // First, build up the Set of declarations to check.  We don't want any duplicates.
        //
        for ( int i = 0; i < _atds.length; ++i )
        {
            AnnotationTypeDeclaration atd = _atds[i];
            Declaration[] decls = getAnnotationProcessorEnvironment().getDeclarationsAnnotatedWith( atd );
            for ( int j = 0; j < decls.length; j++ )
            {
                declsToCheck.add( decls[j] );
            }
        }

        // Now, check the declarations.
        check(declsToCheck);
    }

    protected void check(Collection decls)
        throws FatalCompileTimeException
    {
        for (Iterator i = decls.iterator(); i.hasNext();) {
            Declaration decl = (Declaration) i.next();
            check(decl);
        }
    }

    /**
     * Emits additional artifacts for input Declarations that are annotated with
     * annotations claimed by this CoreAnnotationProcessor.
     */
    public void generate()
    {
        try
        {
            HashSet alreadyProcessed = new HashSet();

            for ( int i = 0; i < _atds.length; i++ )
            {
                AnnotationTypeDeclaration atd = _atds[i];
                Declaration[] decls = getAnnotationProcessorEnvironment().getDeclarationsAnnotatedWith( atd );

                for ( int j = 0; j < decls.length; j++ )
                {
                    Declaration decl = decls[j];
                    if ( ! alreadyProcessed.contains( decl ) ) generate( decl );
                    alreadyProcessed.add( decl );
                }
            }
        }
        catch ( FatalCompileTimeException e )
        {
            e.printDiagnostic( this );
        }
    }

    /**
     * The check method is responsible for all semantic validation of the input Declaration.
     * <p/>
     * All semantic errors/warnings associated with the input Declaration should
     * be output during check via methods on {@link Diagnostics}.
     * <p/>
     * If the presence of the input Declaration implies the need to add new files,
     * and those files need to be visible during the check phase for
     * other Declarations, then the CoreAnnotationProcessorEnv's Filer API should be
     * used to add those files in this phase.  The adding of such files at this point
     * should typically not result in their emission to persistent storage (i.e. disk),
     * but rather be kept in memory to be referenced by the check phase of other
     * Declarations.
     */
    public abstract void check( Declaration decl )
        throws FatalCompileTimeException;

    /**
     * The generate method is responsible for the generation of any additional artifacts
     * (source, class, or binary) that are derived from the input Declaration.
     */
    public abstract void generate( Declaration decl )
            throws FatalCompileTimeException;

    //
    // Helper functions for handling diagnostics
    //

    protected String getResourceString( String id, Object[] args )
    {
        ResourceBundle rb = ResourceBundle.getBundle( getClass().getPackage().getName() + ".strings", _locale );
        String pattern = rb.getString( id );
        return MessageFormat.format( pattern, args );
    }

    private AnnotationTypeDeclaration[] _atds;
    private Locale _locale;
}
