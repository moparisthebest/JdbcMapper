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

import org.apache.beehive.netui.compiler.BaseChecker;
import org.apache.beehive.netui.compiler.BaseGenerator;
import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.Diagnostics;
import org.apache.beehive.netui.compiler.JpfLanguageConstants;
import org.apache.beehive.netui.compiler.SourceFileInfo;
import org.apache.beehive.netui.compiler.FatalCompileTimeException;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.Declaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.Modifier;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;
import org.apache.beehive.netui.compiler.typesystem.type.ClassType;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.ResourceBundle;

public abstract class BaseCoreAnnotationProcessor
        extends TwoPhaseCoreAnnotationProcessor
        implements JpfLanguageConstants
{
    private HashMap _sourceFileInfo;
    private SourceFileInfo _singleSourceFileInfo = null;
    private ResourceBundle _messages;
    
    
    protected BaseCoreAnnotationProcessor( AnnotationTypeDeclaration[] annotationTypeDecls,
                                           CoreAnnotationProcessorEnv env )
    {
        super( annotationTypeDecls, env );
        _messages = ResourceBundle.getBundle( "org.apache.beehive.netui.compiler.diagnostics" );
    }

    public void check( Declaration decl )
            throws FatalCompileTimeException
    {
        assert _sourceFileInfo != null;     // process() should guarantee this.
        
        if ( decl instanceof ClassDeclaration )
        {
            ClassDeclaration classDecl = ( ClassDeclaration ) decl;
            BaseChecker checker = getChecker( classDecl, this );
            
            if ( checker != null )
            {
                checker.check( classDecl );
                
                //
                // Also do a silent check on all base classes.  We don't want to generate if there were errors
                // in the base class.
                //
                SilentDiagnostics silentDiagnostics = new SilentDiagnostics();
                
                for ( ClassType i = classDecl.getSuperclass(); i != null; i = i.getSuperclass() )
                {
                    ClassDeclaration baseDecl = i.getClassTypeDeclaration();
                    
                    if ( CompilerUtils.getSourceFile( baseDecl, false ) != null )
                    {
                        BaseChecker silentChecker = getChecker( baseDecl, silentDiagnostics );
                        if ( silentChecker != null ) silentChecker.check( baseDecl );
                    }
                }
                
                if ( silentDiagnostics.hasErrors() ) setHasErrors( true );
            }
        }
    }

    public void generate( Declaration decl )
            throws FatalCompileTimeException
    {
        assert _sourceFileInfo != null;     // process() should guarantee this.
        
        if ( decl instanceof ClassDeclaration )
        {
            ClassDeclaration classDecl = ( ClassDeclaration ) decl;
            BaseGenerator generator = getGenerator( classDecl, this );
            if ( generator != null ) generator.generate( classDecl );
        }
    }

    public void process()
    {
        _sourceFileInfo = new HashMap();
        super.process();
        _sourceFileInfo = null;
    }

    protected abstract BaseChecker getChecker( ClassDeclaration decl, Diagnostics diagnostics );
    
    protected abstract BaseGenerator getGenerator( ClassDeclaration decl, Diagnostics diagnostics );
    
    protected SourceFileInfo getSourceFileInfo( ClassDeclaration decl )
    {
        assert _sourceFileInfo != null || _singleSourceFileInfo != null;
        assert _sourceFileInfo == null || _singleSourceFileInfo == null;
        return _singleSourceFileInfo != null ? _singleSourceFileInfo : ( SourceFileInfo ) _sourceFileInfo.get( decl.getQualifiedName() );
    }

    protected void setSourceFileInfo( ClassDeclaration decl, SourceFileInfo sourceFileInfo )
    {
        assert _sourceFileInfo != null || _singleSourceFileInfo == null;
        
        if ( _sourceFileInfo != null )
        {
            _sourceFileInfo.put( decl.getQualifiedName(), sourceFileInfo );
        }
        else
        {
            _singleSourceFileInfo = sourceFileInfo;
        }
    }
    
    protected static boolean expectAnnotation( ClassDeclaration classDecl, String annotationBaseName,
                                               String fileExtensionRequiresAnnotation, String baseClass,
                                               Diagnostics diagnostics )
    {
        if ( CompilerUtils.getAnnotation( classDecl, annotationBaseName ) != null ) return true;
        
        String fileName = classDecl.getPosition().file().getName();
        
        if ( fileExtensionRequiresAnnotation != null && fileName.endsWith( fileExtensionRequiresAnnotation ) )
        {
            diagnostics.addError( classDecl, "error.annotation-required", 
                                  fileExtensionRequiresAnnotation, ANNOTATIONS_CLASSNAME + '.' + annotationBaseName );
        }
        else if ( ! classDecl.hasModifier( Modifier.ABSTRACT ) )
        {
            diagnostics.addWarning( classDecl, "warning.missing-annotation", baseClass,
                                    ANNOTATIONS_CLASSNAME + '.' + annotationBaseName );
        }
        
        return false;
    }
    
    protected String getResourceString( String key, Object[] args )
    {
        String message = _messages.getString( key );
        return MessageFormat.format( message, args );
    }
}
