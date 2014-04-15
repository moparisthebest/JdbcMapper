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
package org.apache.beehive.netui.compiler;

import org.apache.beehive.netui.compiler.genmodel.GenStrutsApp;
import org.apache.beehive.netui.compiler.genmodel.GenValidationModel;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.FieldDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;
import org.apache.beehive.netui.compiler.typesystem.type.ClassType;
import org.apache.beehive.netui.compiler.model.XmlModelWriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


abstract class FlowControllerGenerator
        extends BaseGenerator
{
    private static long _compilerJarTimestamp = -1;
    private static final boolean ALWAYS_GENERATE = true;  // TODO: this turns stale checking off.  Do we need it?
    private static final String CONTROL_ANNOTATION = JpfLanguageConstants.BEEHIVE_PACKAGE + ".controls.api.bean.Control";
    
    protected FlowControllerGenerator( CoreAnnotationProcessorEnv env, FlowControllerInfo fcInfo, 
                                       Diagnostics diagnostics )
    {
        super( env, fcInfo, diagnostics );
    }
    
    protected abstract GenStrutsApp createStrutsApp( ClassDeclaration cl )
            throws IOException, FatalCompileTimeException;
    
    public void generate( ClassDeclaration publicClass )
    {
        GenStrutsApp app = null;
        getFCSourceFileInfo().startBuild( getEnv(), publicClass );
        
        try
        {
            // Write the Struts config XML, and the Validator config XML if appropriate.
            app = createStrutsApp( publicClass );
            GenValidationModel validationModel = new GenValidationModel( publicClass, app, getEnv() );
            
            if ( ! validationModel.isEmpty() )
            {
                app.setValidationModel( validationModel );
                validationModel.writeToFile();
            }
            
            generateStrutsConfig( app, publicClass );
            
            // First, write out XML for any fields annotated with @Jpf.SharedFlowField or @Control.
            writeFieldAnnotations( publicClass, app );
        }
        catch ( FatalCompileTimeException e )
        {
            e.printDiagnostic( getDiagnostics() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();    // @TODO log
            assert e instanceof IOException : e.getClass().getName();
            getDiagnostics().addError( publicClass, "error.could-not-generate",
                                       app != null ? app.getStrutsConfigFile() : null, e.getMessage() );
        }
        finally
        {
            getFCSourceFileInfo().endBuild();
        }
    }

    private void writeFieldAnnotations( ClassDeclaration classDecl, GenStrutsApp app )
    {
        try {
            // We always write out the annotations XML file, even if there are no annotated elements.
            // This is to prevent problems in iterative dev -- imagine compiling a file that has
            // annotated elements, then removing those annotated elements, then recompiling.  If
            // we didn't write the (empty) file out, the old one would still be there, with stale
            // data in it.
            AnnotationToXML atx = new AnnotationToXML( classDecl );
            
            includeFieldAnnotations( atx, classDecl, null );
            atx.writeXml( getDiagnostics(), getEnv() );
        } catch (Exception e) {
            getDiagnostics().addError(classDecl, "error.could-not-generate", AnnotationToXML.getFilePath(classDecl),
                                      e.getMessage() );
            e.printStackTrace();  // TODO: log instead
        }
    }
    
    /*
     * Gets all the public, protected, default (package) access,
     * and private fields, including inherited fields, that have
     * desired annotations.
     */
    static boolean includeFieldAnnotations( AnnotationToXML atx, TypeDeclaration typeDecl, String additionalAnnotation )
    {
        boolean hasFieldAnnotations = false;

        if (! (typeDecl instanceof ClassDeclaration)) {
            return hasFieldAnnotations;
        }

        ClassDeclaration jclass = (ClassDeclaration) typeDecl;
        FieldDeclaration[] fields = jclass.getFields();

        for (int i = 0; i < fields.length; i++) {
            AnnotationInstance fieldAnnotation =
                    CompilerUtils.getAnnotation(fields[i], JpfLanguageConstants.SHARED_FLOW_FIELD_TAG_NAME);

            if (fieldAnnotation == null) {
                fieldAnnotation = CompilerUtils.getAnnotationFullyQualified(fields[i], CONTROL_ANNOTATION);
            }

            if (fieldAnnotation == null && additionalAnnotation != null) {
                fieldAnnotation = CompilerUtils.getAnnotation(fields[i], additionalAnnotation);
            }

            if (fieldAnnotation != null) {
                atx.include(fields[i], fieldAnnotation);
                hasFieldAnnotations = true;
            }
        }

        ClassType superclass = jclass.getSuperclass();
        boolean superclassHasFieldAnns = false;
        if (superclass != null) {
            superclassHasFieldAnns = includeFieldAnnotations(atx, CompilerUtils.getDeclaration(superclass), additionalAnnotation);
        }

        return hasFieldAnnotations || superclassHasFieldAnns;
    }

    protected void generateStrutsConfig( GenStrutsApp app, ClassDeclaration publicClass )
    {
        File strutsConfigFile = null;
        
        try
        {
            strutsConfigFile = app.getStrutsConfigFile();

            if ( ALWAYS_GENERATE || app.isStale() )
            {
                // @TODO logger.info( "Writing Struts module: " + _strutsConfig.getStrutsConfigFile() );
                app.writeToFile();
            }
            else if ( _compilerJarTimestamp > strutsConfigFile.lastModified() )
            {
                // @TODO logger.info( _compilerJarName + " has been updated; writing Struts module "
                //          + _strutsConfig.getStrutsConfigFile() );
                app.writeToFile();
            }
            else
            {
                // @TODO logger.info( "Struts module " + _strutsConfig.getStrutsConfigFile() + " is up-to-date." );
            }
        }
        catch ( FatalCompileTimeException e )
        {
            e.printDiagnostic( getDiagnostics() );
        }
        catch ( FileNotFoundException e )
        {
            getDiagnostics().addError( publicClass, "error.could-not-generate",
                    strutsConfigFile != null ? strutsConfigFile.getPath() : null,
                    e.getMessage() );
        }
        catch ( IOException e )
        {
            getDiagnostics().addError( publicClass, "error.could-not-generate",
                    strutsConfigFile != null ? strutsConfigFile.getPath() : null,
                    e.getMessage() );
        }
        catch ( XmlModelWriterException e )
        {
            getDiagnostics().addError( publicClass, "error.could-not-generate",
                                       strutsConfigFile != null ? strutsConfigFile.getPath() : null,
                                       e.getMessage() );
        }
    }

    protected FlowControllerInfo getFCSourceFileInfo()
    {
        return ( FlowControllerInfo ) super.getSourceFileInfo();
    }
}
