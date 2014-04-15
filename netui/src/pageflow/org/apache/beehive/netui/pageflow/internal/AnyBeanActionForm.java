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

import org.apache.beehive.netui.pageflow.handler.ReloadableClassHandler;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.pageflow.config.PageFlowActionMapping;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;


public class AnyBeanActionForm extends BaseActionForm
{
    private static final Logger _log = Logger.getInstance( AnyBeanActionForm.class );
    
    private Object _bean;

    
    public AnyBeanActionForm()
    {
    }
    
    public AnyBeanActionForm( Object bean )
    {
        _bean = bean;
    }
    
    public Object getBean()
    {
        return _bean;
    }

    public void setBean( Object bean )
    {
        _bean = bean;
    }
    
    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        if ( _bean == null )
        {
            assert mapping instanceof PageFlowActionMapping : mapping.getClass().getName();
            
            String formClass = ( ( PageFlowActionMapping ) mapping ).getFormClass();
            assert formClass != null;
            
            try
            {
                ReloadableClassHandler reloadableHandler =
                        Handlers.get( getServlet().getServletContext() ).getReloadableClassHandler();
                _bean = reloadableHandler.newInstance( formClass );
            }
            catch ( Exception e )
            {
                // Can be any exception -- not just the reflection-related exceptions...
                // because the exception could be thrown from the bean's constructor.
                if ( _log.isErrorEnabled() )
                {
                    _log.error( "Error while creating form-bean object of type " + formClass, e );
                }
            }
        }
    } 

    public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
    {
        assert _bean != null;
        String beanName = mapping.getAttribute();
        return validateBean( _bean, beanName, mapping, request );
    }    
    
    public String toString()
    {
        return "[AnyBeanActionForm wrapper for " + _bean + ']';
    }
}
