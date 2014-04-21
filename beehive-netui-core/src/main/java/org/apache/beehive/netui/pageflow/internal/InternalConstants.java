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

public interface InternalConstants
{
    /**
     * The page flow compiler generates this message key when the message is actually an expression for us to evaluate
     * at runtime.
     */ 
    public static final String MESSAGE_IS_EXPRESSION_PREFIX = "NETUI-EXPRESSION:";
    public static final int MESSAGE_IS_EXPRESSION_PREFIX_LENGTH = MESSAGE_IS_EXPRESSION_PREFIX.length();
    
    public static final String ATTR_PREFIX = "_netui:";
    
    public static final String FACES_BACKING_ATTR = ATTR_PREFIX + "facesBacking";
    public static final String BACKING_CLASS_IMPLICIT_OBJECT = "backing";

    public static final String SHARED_FLOW_MODULE_PREFIX = "/-";
    public static final int    SHARED_FLOW_MODULE_PREFIX_LEN = SHARED_FLOW_MODULE_PREFIX.length();
    public static final String SHARED_FLOW_ROOT_MODULE = "/-webappRoot";
    
    public static final String FACES_EXTENSION = "faces";
    public static final String JSF_EXTENSION = "jsf";
    public static final String FACES_EXTENSION_DOT = '.' + FACES_EXTENSION;
    public static final String JSF_EXTENSION_DOT = '.' + JSF_EXTENSION;
    
    public static final String INTERNAL_VAR_PREFIX = "_netui:";
    public static final int    ACTION_EXTENSION_LEN = PageFlowConstants.ACTION_EXTENSION.length();
    public static final String GLOBALAPP_MODULE_CONTEXT_PATH = "/-global";
    public static final String GLOBALAPP_CLASSNAME = "global.Global";
    public static final String GLOBALAPP_MEMBER_NAME = "globalApp";
    public static final String WEBINF_DIR = "/WEB-INF";
    public static final String BEGIN_ACTION_PATH = "/" + PageFlowConstants.BEGIN_ACTION_NAME;
    public static final String RETURNING_FORM_ATTR = ATTR_PREFIX + "returningForm";
    public static final String RETURNING_FROM_NESTING_ATTR = ATTR_PREFIX + "nestedReturning";
    public static final String CURRENT_JPF_ATTR = ATTR_PREFIX + "curPageFlow";
    public static final String CURRENT_LONGLIVED_ATTR = ATTR_PREFIX + "curLongLivedModule";
    public static final String SHARED_FLOW_ATTR_PREFIX = ATTR_PREFIX + "sharedFlow:";
    public static final String SAVED_PREVIOUS_PAGE_INFO_ATTR = ATTR_PREFIX + "savedPrevPageInfo";

    public static final String NETUI_CONFIG_PATH = "/WEB-INF/beehive-netui-config.xml";
    
    public static final String RETURN_ACTION_VIEW_RENDERER_PARAM = ATTR_PREFIX + "returnActionViewRenderer";
    
    /**
     * The prefix for a button or imageButton that has an action.
     */
    public static final String ACTION_OVERRIDE_PREFIX = "actionOverride:";
    
    public static final String FACES_BACKING_EXTENSION = ".jsfb";
    public static final String SHARED_FLOW_EXTENSION = ".jpfs";
    
    public static final String ANNOTATION_QUALIFIER = "org.apache.beehive.netui.pageflow.annotations.Jpf.";
}
