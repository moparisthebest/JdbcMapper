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
package org.apache.beehive.netui.compiler.grammar;

import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.FlowControllerInfo;
import org.apache.beehive.netui.compiler.AnnotationGrammar;
import org.apache.beehive.netui.compiler.FatalCompileTimeException;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationValue;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.MemberDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeElementDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.MethodDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;
import org.apache.beehive.netui.compiler.typesystem.type.ClassType;

import java.util.Collection;
import java.util.Iterator;

public class WebappPathOrActionType
        extends WebappPathType
{
    public WebappPathOrActionType( boolean pathMustBeRelative, String requiredRuntimeVersion,
                                   AnnotationGrammar parentGrammar, FlowControllerInfo fcInfo )
    {
        super( pathMustBeRelative, requiredRuntimeVersion, parentGrammar, fcInfo );
    }

    
    public Object onCheck( AnnotationTypeElementDeclaration valueDecl, AnnotationValue value,
                           AnnotationInstance[] parentAnnotations, MemberDeclaration classMember,
                           int annotationArrayIndex )
            throws FatalCompileTimeException
    {
        String stringValue = ( String ) value.getValue();
        checkAction( stringValue, value, classMember );
        return super.onCheck( valueDecl, value, parentAnnotations, classMember, annotationArrayIndex );
    }
    
    protected void checkAction( String stringValue, AnnotationValue annValue, MemberDeclaration classMember )
    {
        if ( stringValue.endsWith( ACTION_EXTENSION_DOT ) && stringValue.indexOf( '/' ) == -1 )
        {
            TypeDeclaration outerType = CompilerUtils.getOuterClass( classMember );

            if ( outerType != null )    // null in some error conditions
            {
                int extensionPos = stringValue.lastIndexOf( ACTION_EXTENSION_DOT );
                String actionMethodName = stringValue.substring( 0, extensionPos );
                FlowControllerInfo fcInfo = getFlowControllerInfo();
                boolean foundIt = actionExists( actionMethodName, outerType, null, getEnv(), fcInfo, true );

                if ( ! foundIt && actionMethodName.length() > 0 )
                {
                    //
                    // Check for a Shared Flow action reference of the form <shared-flow-name>..
                    //
                    int dot = actionMethodName.indexOf( '.' );
                    
                    if ( dot != -1 && dot < actionMethodName.length() - 1 )
                    {
                        String sharedFlowName = actionMethodName.substring( 0, dot );
                        TypeDeclaration sfTypeDecl = ( TypeDeclaration ) getFlowControllerInfo().getSharedFlowTypes().get( sharedFlowName );
                        
                        if ( sfTypeDecl != null )
                        {
                            actionMethodName = actionMethodName.substring( dot + 1 );
                            foundIt = actionExists( actionMethodName, sfTypeDecl, null, getEnv(), fcInfo, true );
                        }
                    }
                }
                
                //
                // Look in (legacy) Global.app, unless the class being checked is a shared flow (shared flows are
                // the successor to Global.app -- they can't raise Global.app actions.
                //
                if ( ! foundIt &&
                     ! CompilerUtils.isAssignableFrom( SHARED_FLOW_BASE_CLASS,
                                                       CompilerUtils.getOutermostClass( classMember ), getEnv() ) )
                {
                    TypeDeclaration globalAppDecl = getEnv().getTypeDeclaration( GLOBALAPP_FULL_CLASSNAME );
                    if ( globalAppDecl != null )
                    {
                        foundIt = actionExists( actionMethodName, globalAppDecl, null, getEnv(), fcInfo, false );
                    }
                }
                
                if ( ! foundIt )
                {
                    if ( doFatalError() )
                    {
                        addError( annValue, "error.action-not-found", actionMethodName );
                    }
                    else
                    {
                        addWarning( annValue, "warning.action-not-found", actionMethodName );
                    }
                }
            }        
        }
    }
    
    public static boolean actionExists( String actionName, TypeDeclaration type, AnnotationInstance annotationToIgnore,
                                        CoreAnnotationProcessorEnv env, FlowControllerInfo fcInfo,
                                        boolean checkInheritedActions )
    {
        if ( ! ( type instanceof ClassDeclaration ) )
        {
            return false;
        }
        
        ClassDeclaration classDecl = ( ClassDeclaration ) type;
        
        do
        {
            //
            // First look through the action methods.
            //
            MethodDeclaration[] methods = classDecl.getMethods();
            
            for ( int i = 0; i < methods.length; i++ )
            {
                MethodDeclaration method = methods[i];
                if ( method.getSimpleName().equals( actionName )
                     && CompilerUtils.getAnnotation( method, ACTION_TAG_NAME ) != null )
                {
                    return true;
                }
            }
            
            //
            // Next, look through the simple actions (annotations).
            //
            Collection simpleActionAnnotations = 
                CompilerUtils.getAnnotationArrayValue( classDecl, CONTROLLER_TAG_NAME, SIMPLE_ACTIONS_ATTR, true );
            
            if ( simpleActionAnnotations != null )
            {
                for ( Iterator i = simpleActionAnnotations.iterator(); i.hasNext(); )  
                {
                    AnnotationInstance ann = ( AnnotationInstance ) i.next();
                    String name = CompilerUtils.getString( ann, NAME_ATTR, false );
                
                    if ( actionName.equals( name )
                            && ! CompilerUtils.annotationsAreEqual( ann, annotationToIgnore, false, env ) )
                    {
                        return true;
                    }
                }
            }
            
            ClassType superType = classDecl.getSuperclass();
            classDecl = superType != null ? superType.getClassTypeDeclaration() : null;
        } while ( checkInheritedActions && classDecl != null );
        
        
        return false;
    }
}
