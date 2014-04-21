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

import org.apache.beehive.controls.runtime.servlet.ServletBeanContext;
import org.apache.beehive.netui.pageflow.PageFlowController;

/**
 * Specialization of the base ServletBeanContext that adds a Page Flow service provider to offer
 * initialization for PageFlowController and SharedFlowControler members in a Control.
 */
public class PageFlowBeanContext
        extends ServletBeanContext
        implements PageFlowServiceProvider.HasServletRequest
{
    /**
      * Called by BeanContextSupport superclass during construction and deserialization to
      * initialize subclass transient state
      */
     public void initialize()
     {
         // todo: this should move to a single point _outside_ of the initialize method 
         super.initialize();
         addService( PageFlowController.class, PageFlowServiceProvider.getProvider() );
     }
}
