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
package org.apache.beehive.netui.compiler.genmodel;

import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.JpfLanguageConstants;
import org.apache.beehive.netui.compiler.model.ActionModel;
import org.apache.beehive.netui.compiler.model.ForwardModel;
import org.apache.beehive.netui.compiler.model.FormBeanModel;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.Declaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.MethodDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.ParameterDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.type.DeclaredType;
import org.apache.beehive.netui.compiler.typesystem.type.TypeInstance;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class GenActionModel
        extends ActionModel
        implements JpfLanguageConstants
{
    public GenActionModel( Declaration sourceElement, GenStrutsApp parentApp, ClassDeclaration jclass )
    {
        super( parentApp );
        
        init( getActionName( sourceElement ), getActionAnnotation( sourceElement ), parentApp, jclass );
        
        // Get the form class from the method argument.
        setFormBeanName( getFormBean( sourceElement, parentApp ) );
    }
    
    protected GenActionModel( String actionName, AnnotationInstance ann, GenStrutsApp parentApp, ClassDeclaration jclass )
    {
        super( parentApp );
        init( actionName, ann, parentApp, jclass );
    }

    protected void init( String actionName, AnnotationInstance annotation, GenStrutsApp parentApp, ClassDeclaration jclass )
    {
        setActionName(actionName);
        
        //
        // loginRequired
        //
        Boolean loginRequired = CompilerUtils.getBoolean( annotation, LOGIN_REQUIRED_ATTR, true );
        if ( loginRequired == null )
        {
            loginRequired = parentApp.getFlowControllerInfo().getMergedControllerAnnotation().isLoginRequired();
        }
        setLoginRequired( loginRequired );

        //
        // prevent-double-submit
        //
        Boolean preventDoubleSubmit = CompilerUtils.getBoolean( annotation, PREVENT_DOUBLE_SUBMIT_ATTR, false );
        setPreventDoubleSubmit( preventDoubleSubmit.booleanValue() );
        
        //
        // readOnly
        //
        Boolean readOnly = CompilerUtils.getBoolean( annotation, READONLY_ATTR, true );
        if ( readOnly == null )
        {
            readOnly = parentApp.getFlowControllerInfo().getMergedControllerAnnotation().isReadOnly();
        }
        setReadonly( readOnly );

        //
        // rolesAllowed -- avoid setting this if loginRequired is explicitly false.
        //
        if ( loginRequired == null || loginRequired.booleanValue() ) setRolesAllowed( annotation, jclass, parentApp );
        
        //
        // type (delegating Action class, with the FlowController as parameter)
        //
        setType( FLOW_CONTROLLER_ACTION_CLASS );
        setParameter( jclass.getQualifiedName() );
        
        //
        // form bean member -- the page-flow-scoped form referenced by the action (a member variable)
        //
        setFormMember( CompilerUtils.getString( annotation, USE_FORM_BEAN_ATTR, true ) );
        
        //
        // forwards
        //
        getForwards( annotation, jclass, parentApp );
        
        //
        // validationErrorForward -- the forward used when validation fails
        //
        AnnotationInstance validateErrFwd = CompilerUtils.getAnnotation( annotation, VALIDATION_ERROR_FORWARD_ATTR, true );
        boolean doValidation = false;
        if ( validateErrFwd != null )
        {
            ForwardModel fwd = new GenForwardModel( parentApp, validateErrFwd, jclass, " (validationErrorForward)" );
            addForward( fwd );
            setInput( fwd.getName() );
            doValidation = true;
        }
        
        //
        // validate
        //
        Boolean explicitDoValidation = CompilerUtils.getBoolean( annotation, DO_VALIDATION_ATTR, true );
        setValidate( explicitDoValidation != null ? explicitDoValidation.booleanValue() : doValidation );

        //
        // exception-catches
        //
        List catches = CompilerUtils.getAnnotationArray( annotation, CATCHES_ATTR, true );
        GenExceptionModel.addCatches( catches, this, jclass, parentApp, this );
    }

    protected void setRolesAllowed( AnnotationInstance annotation, ClassDeclaration jclass, GenStrutsApp parentApp )
    {
        List rolesAllowed = CompilerUtils.getStringArray( annotation, ROLES_ALLOWED_ATTR, true );
        List classLevelRA = parentApp.getFlowControllerInfo().getMergedControllerAnnotation().getRolesAllowed();
        Iterator it = null;
        
        if ( rolesAllowed != null && classLevelRA != null )
        {
            HashSet merged = new HashSet();
            for ( Iterator ii = rolesAllowed.iterator(); ii.hasNext(); )  
            {
                String role = ( String ) ii.next();
                merged.add( role );
            }
            for ( Iterator ii = classLevelRA.iterator(); ii.hasNext(); )  
            {
                String classLevelRole = ( String ) ii.next();
                merged.add( classLevelRole );
            }
            it = merged.iterator();
        }
        else if ( rolesAllowed != null )
        {
            it = rolesAllowed.iterator();
        }
        else if ( classLevelRA != null )
        {
            it = classLevelRA.iterator();
        }
        
        if ( it != null && it.hasNext() )
        {
            StringBuffer rolesAllowedStr = new StringBuffer( ( String ) it.next() );
            
            while ( it.hasNext() )
            {
                rolesAllowedStr.append( ',' ).append( ( ( String ) it.next() ).trim() );
            }
            
            setRoles( rolesAllowedStr.toString() );
        }
    }
    
    protected static String getActionName( Declaration sourceElement )
    {
        return sourceElement.getSimpleName();
    }
    
    /**
     * @return the Struts name of the form bean.
     */ 
    protected String getFormBean( Declaration sourceElement, GenStrutsApp parentApp )
    {
        assert sourceElement instanceof MethodDeclaration : sourceElement.getClass().getName();
        ParameterDeclaration[] params = ( ( MethodDeclaration ) sourceElement ).getParameters();
        String formBeanName = null;
        
        if ( params.length > 0 )
        {
            assert params.length == 1 : params.length;  // checker should catch this
            TypeInstance paramType = CompilerUtils.getGenericBoundsType( params[0].getType() );
            formBeanName = addFormBean( paramType, parentApp );
        }
        
        return formBeanName;
    }
    
    protected String addFormBean( TypeInstance paramType, GenStrutsApp parentApp )
    {
        paramType = CompilerUtils.getGenericBoundsType( paramType );
        assert paramType instanceof DeclaredType : paramType.getClass().getName();  // checker should enforce this
        TypeDeclaration decl = CompilerUtils.getDeclaration( ( DeclaredType ) paramType );
        List formBeans = parentApp.getMatchingFormBeans(decl, Boolean.valueOf(getFormMember() != null));
        assert formBeans.size() > 0;
        FormBeanModel formBeanModel = (FormBeanModel) formBeans.get(0);
        setFormBeanMessageResourcesKey(formBeanModel.getFormBeanMessageResourcesKey());
        
        //
        // If this isn't an ActionForm-derived argument, keep track of the classname for the runtime.
        //
        if ( ! CompilerUtils.isAssignableFrom( PAGEFLOW_FORM_CLASS_NAME, decl, parentApp.getEnv() ) )
        {
            setFormClass( CompilerUtils.getLoadableName( decl ) );
        }
        
        return formBeanModel.getName();
    }
    
    protected AnnotationInstance getActionAnnotation( Declaration sourceElement )
    {
        assert sourceElement instanceof MethodDeclaration : sourceElement.getClass().getName();
        return CompilerUtils.getAnnotation( sourceElement, ACTION_TAG_NAME );
    }
    
    protected void getForwards( AnnotationInstance annotation, ClassDeclaration jclass, GenStrutsApp parentApp )
    {
        GenForwardModel.addForwards( annotation, this, jclass, parentApp, null );
    }
}
