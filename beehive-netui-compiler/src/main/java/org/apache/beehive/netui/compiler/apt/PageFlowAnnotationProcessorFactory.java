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

import org.apache.beehive.netui.compiler.JpfLanguageConstants;
import org.apache.beehive.netui.compiler.processor.PageFlowCoreAnnotationProcessor;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessor;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;


public class PageFlowAnnotationProcessorFactory
        extends BaseAnnotationProcessorFactory
        implements JpfLanguageConstants
{
    private static final HashSet PAGEFLOW_ANNOTATIONS = new HashSet();
    private static final HashSet FACES_BACKING_ANNOTATIONS = new HashSet();
    
    private static final ArrayList SUPPORTED_ANNOTATION_TYPES = new ArrayList();
    
    static
    {
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + ACTION_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + ACTION_OUTPUT_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + CATCH_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + CONDITIONAL_FORWARD_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + CONTROLLER_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + EXCEPTION_HANDLER_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + FORM_BEAN_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + FORWARD_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + MESSAGE_ARG_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + MESSAGE_BUNDLE_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + SIMPLE_ACTION_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATABLE_BEAN_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATABLE_PROPERTY_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATE_CREDIT_CARD_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATE_CUSTOM_RULE_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATE_CUSTOM_VARIABLE_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATE_DATE_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATE_EMAIL_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATE_MASK_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATE_MAX_LENGTH_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATE_MIN_LENGTH_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATE_RANGE_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATE_REQUIRED_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATE_TYPE_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATE_VALID_WHEN_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATE_URL_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VALIDATION_LOCALE_RULES_TAG_NAME );
        PAGEFLOW_ANNOTATIONS.add( ANNOTATION_QUALIFIER + VIEW_PROPERTIES_TAG_NAME );

        FACES_BACKING_ANNOTATIONS.add( ANNOTATION_QUALIFIER + COMMAND_HANDLER_TAG_NAME );
        FACES_BACKING_ANNOTATIONS.add( ANNOTATION_QUALIFIER + FACES_BACKING_TAG_NAME );
        FACES_BACKING_ANNOTATIONS.add( ANNOTATION_QUALIFIER + PAGE_FLOW_FIELD_TAG_NAME );
        FACES_BACKING_ANNOTATIONS.add( ANNOTATION_QUALIFIER + RAISE_ACTION_TAG_NAME );
        
        SUPPORTED_ANNOTATION_TYPES.addAll( PAGEFLOW_ANNOTATIONS );
        SUPPORTED_ANNOTATION_TYPES.addAll( FACES_BACKING_ANNOTATIONS );
        SUPPORTED_ANNOTATION_TYPES.add( ANNOTATION_QUALIFIER + SHARED_FLOW_FIELD_TAG_NAME );
        SUPPORTED_ANNOTATION_TYPES.add( ANNOTATION_QUALIFIER + SHARED_FLOW_REF_TAG_NAME );
    }
    
    public Collection supportedAnnotationTypes()
    {
        return SUPPORTED_ANNOTATION_TYPES;
    }

    public Collection supportedOptions()
    {
        return Collections.EMPTY_LIST;
    }

    /**
     * Get the core annotation processor which is appropriate for the given annotations.  Note that this is "our"
     * annotation processor, not a Sun apt annotation processor.  See
     * {@link BaseAnnotationProcessorFactory#getProcessorFor} for the place where a Sun annotation processor is returned.
     */
    public CoreAnnotationProcessor getCoreProcessorFor( AnnotationTypeDeclaration[] atds, CoreAnnotationProcessorEnv env )
    {
        return new PageFlowCoreAnnotationProcessor( atds, env );
    }
}
