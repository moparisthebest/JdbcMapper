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
package org.apache.beehive.netui.pageflow.internal;

import org.apache.beehive.netui.pageflow.PageFlowConstants;
import org.apache.beehive.netui.pageflow.PageFlowController;

import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.servlet.ServletContext;


/**
 * Information that is cached per pageflow class.
 */
public class CachedPageFlowInfo
        extends CachedSharedFlowRefInfo
{
    /**
     * A cached copy of the module path for this PageFlowController (the path starting at the webapp root).
     */
    private String _modulePath;

    /**
     * A cached copy of the webapp-relative URI for this PageFlowController.
     */
    private String _URI;


    public CachedPageFlowInfo(Class pageFlowClass, ServletContext servletContext) {
        AnnotationReader annReader = AnnotationReader.getAnnotationReader( pageFlowClass, servletContext );
        initSharedFlowFields( annReader, pageFlowClass );

        // URI
        String className = pageFlowClass.getName();
        _URI = '/' + className.replace( '.', '/' ) + PageFlowConstants.PAGEFLOW_EXTENSION;

        // module path
        _modulePath = InternalUtils.inferModulePathFromClassName( className );
    }

    public String getModulePath() {
        return _modulePath;
    }

    public void setModulePath(String modulePath) {
        _modulePath = modulePath;
    }

    public String getURI() {
        return _URI;
    }

    public void setURI(String URI) {
        _URI = URI;
    }
}
