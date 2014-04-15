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

import org.apache.beehive.netui.compiler.grammar.ValidatablePropertyGrammar;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeElementDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationValue;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.MemberDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.MethodDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.Modifier;
import org.apache.beehive.netui.compiler.typesystem.declaration.ParameterDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;
import org.apache.beehive.netui.compiler.typesystem.type.DeclaredType;
import org.apache.beehive.netui.compiler.typesystem.type.TypeInstance;

import java.util.Map;


public class FormBeanChecker
        extends BaseChecker
        implements JpfLanguageConstants
{
    public FormBeanChecker( CoreAnnotationProcessorEnv env, Diagnostics diags )
    {
        super( env, null, diags );
    }

    public Map onCheck( ClassDeclaration jclass )
            throws FatalCompileTimeException
    {
        GetterValidatablePropertyGrammar validatablePropertyGrammar = new GetterValidatablePropertyGrammar();
        boolean hasFormBeanAnnotation = CompilerUtils.getAnnotation( jclass, FORM_BEAN_TAG_NAME, true ) != null;
        boolean isFormBeanClass = hasFormBeanAnnotation;

        // Look for ValidationField annotations on the methods; if there are some present, then we consider this
        // a form bean class, even if it's not annotated as such.
        MethodDeclaration[] methods = CompilerUtils.getClassMethods( jclass, null );
        
        for ( int i = 0; i < methods.length; i++ )
        {
            MethodDeclaration method = methods[i];
            isFormBeanClass |=
                checkValidationAnnotation( method, VALIDATABLE_PROPERTY_TAG_NAME, validatablePropertyGrammar );
            // We don't currently support validation rule annotations directly on getter methods.
            /*
            hasOne |= checkValidationAnnotation( method, LOCALE_RULES_ATTR, _validationLocaleRulesGrammar );
            hasOne |= checkValidationAnnotation( method, VALIDATE_REQUIRED_TAG_NAME, _baseValidationRuleGrammar );
            hasOne |= checkValidationAnnotation( method, VALIDATE_RANGE_TAG_NAME, _validateRangeGrammar );
            hasOne |= checkValidationAnnotation( method, VALIDATE_MIN_LENGTH_TAG_NAME, _baseValidationRuleGrammar );
            hasOne |= checkValidationAnnotation( method, VALIDATE_MAX_LENGTH_TAG_NAME, _baseValidationRuleGrammar );
            hasOne |= checkValidationAnnotation( method, VALIDATE_CREDIT_CARD_TAG_NAME, _baseValidationRuleGrammar );
            hasOne |= checkValidationAnnotation( method, VALIDATE_EMAIL_TAG_NAME, _baseValidationRuleGrammar );
            hasOne |= checkValidationAnnotation( method, VALIDATE_MASK_TAG_NAME, _baseValidationRuleGrammar );
            hasOne |= checkValidationAnnotation( method, VALIDATE_DATE_TAG_NAME, _baseValidationRuleGrammar );
            hasOne |= checkValidationAnnotation( method, VALIDATE_TYPE_TAG_NAME, _validateTypeGrammar );
            */
        }
        
        CoreAnnotationProcessorEnv env = getEnv();
        
        // Make sure ActionForm subclasses are public static, and that they have default constructors.
        if ( isFormBeanClass || CompilerUtils.isAssignableFrom( STRUTS_FORM_CLASS_NAME, jclass, env ) )
        {
            if ( jclass.getDeclaringType() != null && ! jclass.hasModifier( Modifier.STATIC ) )
            {
                getDiagnostics().addError( jclass, "error.form-not-static" );
            }
            
            if ( ! jclass.hasModifier( Modifier.PUBLIC ) )
            {
                getDiagnostics().addError( jclass, "error.form-not-public" );
            }
            
            if ( ! CompilerUtils.hasDefaultConstructor( jclass ) )
            {
               getDiagnostics().addError( jclass, "error.form-no-default-constructor" );
            }
        }

        // check that a class with declarative validation  uses a FormBean annotation
        if (isFormBeanClass && !hasFormBeanAnnotation) {
            getDiagnostics().addWarning(jclass, "warning.validatable-formbean-use-formbean",
                                        ANNOTATION_INTERFACE_PREFIX + FORM_BEAN_TAG_NAME);
        }
        
        // Check to see if this class extends the (deprecated) FormData class and overrides its validate() method.
        // If so, then declarative validation annotations won't work unless the override calls super.validate().
        // Print a warning describing this behavior and suggesting implementing Validatable instead.
        methods = jclass.getMethods();
        if (CompilerUtils.isAssignableFrom(PAGEFLOW_FORM_CLASS_NAME, jclass, env)) {
            for (int i = 0; i < methods.length; i++) {
                
                MethodDeclaration method = methods[i];
                if (method.getSimpleName().equals("validate")) {
                    ParameterDeclaration[] params = method.getParameters();
                    
                    if (params.length == 2) {
                        TypeInstance param1Type = params[0].getType();
                        TypeInstance param2Type = params[1].getType();
                        
                        if (param1Type instanceof DeclaredType && param2Type instanceof DeclaredType) {
                            TypeDeclaration param1Decl = ((DeclaredType) param1Type).getDeclaration();
                            TypeDeclaration param2Decl = ((DeclaredType) param2Type).getDeclaration();
                            TypeDeclaration actionMappingDecl = env.getTypeDeclaration(STRUTS_ACTION_MAPPING_CLASS_NAME);
                            TypeDeclaration httpRequestDecl = env.getTypeDeclaration(HTTP_REQUEST_CLASS_NAME);
                            
                            if (param1Decl != null && CompilerUtils.typesAreEqual(param1Decl, actionMappingDecl)
                                && param2Decl != null && CompilerUtils.typesAreEqual(param2Decl, httpRequestDecl)) {
                                    getDiagnostics().addWarning(method, "warning.formdata-override-validate",
                                                                jclass.getQualifiedName(), PAGEFLOW_FORM_CLASS_NAME,
                                                                PAGEFLOW_VALIDATABLE_INTERFACE_NAME);
                            }
                        }
                    }
                }
            }
        }
        
        return null;
    }

    private boolean checkValidationAnnotation( MethodDeclaration method, String annotationTagName,
                                               AnnotationGrammar grammar )
            throws FatalCompileTimeException
    {
        AnnotationInstance annotation = CompilerUtils.getAnnotation( method, annotationTagName );
        
        if ( annotation != null )
        {
            if ( CompilerUtils.getBeanProperty( method ) == null )
            {
                getDiagnostics().addError( annotation, "error.validation-field-on-non-getter" );
            }
            
            grammar.check( annotation, null, method );
            
            return true;
        }
        
        return false;
    }
        
    private class GetterValidatablePropertyGrammar
            extends ValidatablePropertyGrammar
    {
        public GetterValidatablePropertyGrammar()
        {
            super( FormBeanChecker.this.getEnv(), FormBeanChecker.this.getDiagnostics(),
                   FormBeanChecker.this.getRuntimeVersionChecker() );
        }
        
        public String[][] getRequiredAttrs()
        {
            return null;  // This override causes the 'propertyName' attribute *not* to be required
        }
        
        protected void onCheckMember( AnnotationTypeElementDeclaration memberDecl, AnnotationValue member,
                                      AnnotationInstance annotation, AnnotationInstance[] parentAnnotations,
                                      MemberDeclaration classMember )
        {
            if ( memberDecl.getSimpleName().equals( PROPERTY_NAME_ATTR ) )
            {
                addError( member, "error.validatable-field-property-name-not-allowed", PROPERTY_NAME_ATTR );
            }
        }
    }
}
