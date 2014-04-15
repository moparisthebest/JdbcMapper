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
import org.apache.beehive.netui.compiler.model.StrutsApp;
import org.apache.beehive.netui.compiler.model.XmlModelWriter;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.FieldDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.type.TypeInstance;
import org.w3c.dom.Element;

/**
 * Generates to a Struts ActionMapping object that delegates to an ActionMapping
 * in a different module. These are used for support of inheritance for simple
 * actions.
 * 
 * <p>Note that there are some Controller attributes that are for actions such as
 * loginRequired and readOnly. This implementation extends GenSimpleActionModel
 * and overrides init() so that local Controller properties can be included in
 * the Struts module config we generate. Then the runtime can use these values to
 * override the values of the delegate.</p>
 */
public class DelegatingSimpleActionModel extends GenSimpleActionModel
{
    public DelegatingSimpleActionModel(AnnotationInstance annotation, TypeDeclaration containingType,
                                       GenStrutsApp parent, ClassDeclaration jclass) {
        super(annotation, parent, jclass);

        String modulePath = CompilerUtils.inferModulePathFromType(containingType);
        if (parent instanceof GenSharedFlowStrutsApp) {
            // modify the module config path for a shared flow
            modulePath = "/" + StrutsApp.STRUTS_CONFIG_SEPARATOR + modulePath.substring(1);
        }
        addSetProperty("delegateModulePath", modulePath);

        if (parent.getFlowControllerInfo().getMergedControllerAnnotation().isInheritLocalPaths()) {
            addSetProperty("inheritLocalPaths", "true");
        }
    }

    protected void init(String actionName, AnnotationInstance annotation,
                        GenStrutsApp parent, ClassDeclaration jclass) {
        setActionName(actionName);
        setClassName(DelegatingActionModel.ACTION_MAPPING_CLASSNAME);
        setParameter(parent.getFlowControllerClass().getQualifiedName());

        // loginRequired - Set this to override the delegating action property
        // only if loginRequired is set on the Controller annotation.
        AnnotationInstance controllerAnnotation = CompilerUtils.getAnnotation( jclass, CONTROLLER_TAG_NAME );
        Boolean loginRequired = CompilerUtils.getBoolean( controllerAnnotation, LOGIN_REQUIRED_ATTR, true );
        if (loginRequired != null) {
            setLoginRequired(loginRequired);
        }

        // check for a form bean member in the delegate -- 
        // the page-flow-scoped form referenced by the action (a member variable)
        String formMember = CompilerUtils.getString(annotation, USE_FORM_BEAN_ATTR, true);
        if (formMember != null) {
            setFormMember(formMember);
            FieldDeclaration field = CompilerUtils.findField(jclass, formMember);
            assert field != null;  // checker should prevent this
            setFormBeanName(addFormBean(field.getType(), parent));
        }
        else {
            // can't modify member state; mark as read-only
            setReadonly(Boolean.valueOf(true));

            TypeInstance formBeanType = CompilerUtils.getTypeInstance(annotation, USE_FORM_BEAN_TYPE_ATTR, true);
            if (formBeanType != null) {
                setFormBeanName(addFormBean(formBeanType, parent));
            }
        }

        // rolesAllowed -- avoid setting this if loginRequired is explicitly false.
        // If it's not set on this controller, check the action annotation, then the
        // parent controller.
        if (loginRequired == null) {
            loginRequired = CompilerUtils.getBoolean( annotation, LOGIN_REQUIRED_ATTR, true );
            if (loginRequired == null) {
                loginRequired = parent.getFlowControllerInfo().getMergedControllerAnnotation().isLoginRequired();
            }
        }
        if (loginRequired == null || loginRequired.booleanValue()) {
            setRolesAllowed(annotation, jclass, parent);
        }
    }

    protected void addSetProperty(XmlModelWriter xw, Element element, String propertyName, String propertyValue) {
        setCustomProperty(xw, element, propertyName, propertyValue, DelegatingActionModel.ACTION_MAPPING_CLASSNAME);
    }
}
