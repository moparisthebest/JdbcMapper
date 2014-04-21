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
package org.apache.beehive.netui.pageflow;

import java.util.LinkedList;
import java.util.Enumeration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContext;

import org.apache.beehive.netui.pageflow.internal.PageFlowInitialization;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * Initialize the NetUI framework at {@link javax.servlet.ServletContext} init time.
 */ 
public class PageFlowContextListener
    implements ServletContextListener
{
    private static final Logger LOG = Logger.getInstance( PageFlowContextListener.class );
    
    public void contextInitialized( ServletContextEvent event )
    {
        LOG.info("In " +
            getClass().getName() +
            "; web application " +
            event.getServletContext().getServletContextName() +
            " is being initialized");

        PageFlowInitialization.performInitializations(event.getServletContext(), null);
    }

    public void contextDestroyed( ServletContextEvent event )
    {
        LOG.info("In " +
            getClass().getName() +
            "; web application " +
            event.getServletContext().getServletContextName() +
            " is being destroyed");

        ServletContext servletContext = event.getServletContext();
        removeAttributesNetUI(servletContext);
        removeAttributesStruts(servletContext);
    }

    /**
     * Remove the NetUI related attributes from the {@link ServletContext}.
     *
     * @param servletContext the servelt context
     */
    private void removeAttributesNetUI(ServletContext servletContext) {
        try {
            LinkedList list = new LinkedList();
            Enumeration enumeration = servletContext.getAttributeNames();
            while(enumeration.hasMoreElements()) {
                String string = (String)enumeration.nextElement();
                if(string.startsWith(InternalConstants.ATTR_PREFIX))
                    list.add(string);
            }

            for(int i = 0; i < list.size(); i++) {
                Object key = list.get(i);
                assert key != null;
                assert key instanceof String;
                LOG.trace("Removing ServletContext attribute named \"" + key + "\"");
                servletContext.removeAttribute((String)key);
            }
        }
        catch(Exception e) {
            LOG.error("Caught error removing NetUI attribute from ServletContext.  Cause: " + e, e);
        }
    }

    /**
     * Remove the Struts related attributes from the {@link ServletContext}.
     *
     * @param servletContext the servlet context
     */
    private void removeAttributesStruts(ServletContext servletContext) {
        try {
            LinkedList list = new LinkedList();
            Enumeration enumeration = servletContext.getAttributeNames();
            while(enumeration.hasMoreElements()) {
                String string = (String)enumeration.nextElement();
                if(string.startsWith("org.apache.struts"))
                    list.add(string);
            }

            for(int i = 0; i < list.size(); i++) {
                Object key = list.get(i);
                assert key != null;
                assert key instanceof String;
                LOG.trace("Removing ServletContext attribute named \"" + key + "\"");
                servletContext.removeAttribute((String)key);
            }
        }
        catch(Exception e) {
            LOG.error("Caught error removing Struts attribute from ServletContext.  Cause: " + e, e);
        }
    }
}

