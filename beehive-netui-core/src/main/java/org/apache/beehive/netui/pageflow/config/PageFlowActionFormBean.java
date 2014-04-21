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
package org.apache.beehive.netui.pageflow.config;

import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.action.ActionFormBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionServlet;
import org.apache.beehive.netui.pageflow.handler.ReloadableClassHandler;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.util.logging.Logger;


/**
 * Class to handle our extensions to the Struts &lt;form-bean&gt; element.
 */
public class PageFlowActionFormBean extends ActionFormBean
{
    private static final Logger _log = Logger.getInstance(PageFlowActionFormBean.class);
    
    private String _actualType;  // applicable for non-ActionForm-derived form types


    public String getActualType()
    {
        return _actualType;
    }

    public void setActualType(String actualType)
    {
        _actualType = actualType;
    }

    /**
     * <p>
     * Create and return an <code>ActionForm</code> instance appropriate
     * to the information in this <code>FormBeanConfig</code>.
     * </p>
     * <p>
     * This is different than the base implementation in that it uses our ReloadableClassHandler
     * to load the form bean class.
     * </p>
     *
     * @param servlet The action servlet
     * @return ActionForm instance
     * @exception IllegalAccessException if the Class or the appropriate
     *  constructor is not accessible
     * @exception InstantiationException if this Class represents an abstract
     *  class, an array class, a primitive type, or void; or if instantiation
     *  fails for some other reason
     */
    public ActionForm createActionForm(ActionServlet servlet)
        throws IllegalAccessException, InstantiationException {


        // Create a new form bean instance
        if (getDynamic()) {
            return super.createActionForm(servlet);
        }

        try {
            ReloadableClassHandler rch = Handlers.get(servlet.getServletContext()).getReloadableClassHandler();
            Object obj = rch.newInstance(getType());
            assert obj instanceof ActionForm : obj.getClass().getName();
            ActionForm form = (ActionForm) obj;
            form.setServlet(servlet);
            return form;
        } catch (ClassNotFoundException e) {
            _log.error("Could not find form bean class " + getType(), e);
            return null;
        }
    }
}
