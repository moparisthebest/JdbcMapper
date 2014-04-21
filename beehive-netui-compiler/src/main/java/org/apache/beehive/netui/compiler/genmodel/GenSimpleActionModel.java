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

import org.apache.beehive.netui.compiler.JpfLanguageConstants;
import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.model.ForwardModel;
import org.apache.beehive.netui.compiler.typesystem.declaration.Declaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.FieldDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.type.TypeInstance;

import java.util.List;
import java.util.Iterator;


public class GenSimpleActionModel
        extends GenActionModel
        implements JpfLanguageConstants
{
    public GenSimpleActionModel( AnnotationInstance annotation, GenStrutsApp parentApp, ClassDeclaration jclass )
    {
        super( CompilerUtils.getString( annotation, NAME_ATTR, true ), annotation, parentApp, jclass );
    }

    protected void init( String actionName, AnnotationInstance annotation, GenStrutsApp parentApp, ClassDeclaration jclass )
    {
        super.init( actionName, annotation, parentApp, jclass );

        setSimpleAction( true );
        addForwards( annotation, parentApp, jclass );
        
        String formMember = getFormMember();
        if ( formMember != null )
        {
            FieldDeclaration field = CompilerUtils.findField( jclass, formMember );
            assert field != null;  // checker should prevent this
            setFormBeanName( addFormBean( field.getType(), parentApp ) );
        }
        else
        {
            setReadonly( Boolean.valueOf( true ) );     // can't modify member state; mark as read-only
            
            TypeInstance formBeanType = CompilerUtils.getTypeInstance( annotation, USE_FORM_BEAN_TYPE_ATTR, true );
            
            if ( formBeanType != null )
            {
                setFormBeanName( addFormBean( formBeanType, parentApp ) );
            }
        }
    }

    protected String getFormBean( Declaration sourceElement, GenStrutsApp parentApp )
    {
        return null;
    }

    protected void getForwards( AnnotationInstance annotation, ClassDeclaration jclass, GenStrutsApp parentApp )
    {
    }

    private void addForwards( AnnotationInstance annotation, GenStrutsApp parentApp, ClassDeclaration jclass )
    {
        //
        // First add the default forward -- the one that is parsed from the simple action annotation itself.
        // But, if the "forwardRef" attribute was given, simply use the one referenced.
        //
        String forwardRef = CompilerUtils.getString( annotation, FORWARD_REF_ATTR, true );
        
        if ( forwardRef == null )
        {
            forwardRef = DEFAULT_SIMPLE_ACTION_FORWARD_NAME;
            ForwardModel fwd = new SimpleActionForward( forwardRef, parentApp, annotation, jclass );
            
            if ( fwd.getPath() != null || fwd.isReturnToAction() || fwd.isReturnToPage() || fwd.isNestedReturn() )
            {
                addForward( fwd );
            }
        }
        
        setDefaultForwardName( forwardRef );
        
        List conditionalFwdAnnotations =
                CompilerUtils.getAnnotationArray( annotation, CONDITIONAL_FORWARDS_ATTR, true );
        
        if ( conditionalFwdAnnotations != null )
        {
            int anonCount = 0;
            
            for ( Iterator ii = conditionalFwdAnnotations.iterator(); ii.hasNext(); )  
            {
                AnnotationInstance conditionalFwdAnnotation = ( AnnotationInstance ) ii.next();
                ForwardModel conditionalFwd = new SimpleActionForward( parentApp, conditionalFwdAnnotation, jclass );
                String expression = CompilerUtils.getString( conditionalFwdAnnotation, CONDITION_ATTR, true );
                assert expression != null;
                
                if ( conditionalFwd.getName() == null ) conditionalFwd.setName( "_anon" + ++anonCount );
                addForward( conditionalFwd );
                addConditionalForward( expression, conditionalFwd.getName() );
            }
        }
    }
    
    private static class SimpleActionForward extends GenForwardModel
    {
        public SimpleActionForward( GenStrutsApp parent, AnnotationInstance annotation, ClassDeclaration jclass )
        {
            super( parent, annotation, jclass, null );
        }
        
        public SimpleActionForward( String name, GenStrutsApp parent, AnnotationInstance annotation, ClassDeclaration jclass )
        {
            super( parent, annotation, jclass, null );
            setName( name );
        }

        protected void addActionOutputs( AnnotationInstance annotation, ClassDeclaration jclass )
        {
            // do nothing -- there are no action outputs on simple actions
        }
    }
}

