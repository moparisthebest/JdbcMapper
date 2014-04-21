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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.beehive.netui.pageflow.FlowController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple Struts action that delegates to a page flow controller.
 */
public class FlowControllerAction
        extends Action
{
    private FlowController _flowController;

    
    public FlowControllerAction( FlowController flowController )
    {
        _flowController = flowController;
    }

    public FlowController getFlowController()
    {
        return _flowController;
    }

    public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, 
                                  HttpServletResponse response ) throws Exception
    {
        return _flowController.execute( mapping, form, request, response );
    }
}
