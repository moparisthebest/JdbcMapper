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

import org.apache.beehive.netui.compiler.model.ExceptionContainer;
import org.apache.beehive.netui.compiler.model.ExceptionModel;
import org.apache.beehive.netui.compiler.model.StrutsApp;
import org.apache.beehive.netui.compiler.model.XmlModelWriter;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.CompilerUtils;
import org.w3c.dom.Element;

/**
 * Generates to a Struts ExceptionConfig object that delegates to an ExceptionConfig in a different module.
 */
public class DelegatingExceptionModel extends ExceptionModel
{
    private static final String EXCEPTION_CONFIG_CLASSNAME = PAGEFLOW_PACKAGE + ".config.DelegatingExceptionConfig";

    protected DelegatingExceptionModel(ExceptionContainer container, GenStrutsApp parentApp, AnnotationInstance ann,
                                       TypeDeclaration containingType) {
        super(parentApp);
        setType(CompilerUtils.getLoadableName(CompilerUtils.getDeclaredType(ann, TYPE_ATTR, true)));
        setClassName(EXCEPTION_CONFIG_CLASSNAME);

        String modulePath = CompilerUtils.inferModulePathFromType(containingType);
        if (parentApp instanceof GenSharedFlowStrutsApp) {
            // modify the module config path for a shared flow
            modulePath = "/" + StrutsApp.STRUTS_CONFIG_SEPARATOR + modulePath.substring(1);
        }
        addSetProperty("delegateModulePath", modulePath);
        addSetProperty("delegateActionPath", container.getActionPath());

        if (parentApp.getFlowControllerInfo().getMergedControllerAnnotation().isInheritLocalPaths()) {
            addSetProperty("inheritLocalPaths", "true");
        }
    }

    protected void addSetProperty(XmlModelWriter xw, Element element, String propertyName, String propertyValue) {
        setCustomProperty(xw, element, propertyName, propertyValue, EXCEPTION_CONFIG_CLASSNAME);
    }
}
