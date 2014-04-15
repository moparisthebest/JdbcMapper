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

import org.apache.struts.action.ActionMapping;

import java.util.Map;
import java.util.LinkedHashMap;


/**
 * Bean class to handle our extensions to the Struts &lt;action&gt; element.
 */
public class PageFlowActionMapping extends ActionMapping
{
    private String _unqualifiedActionPath;
    private boolean _loginRequired = false;
    private boolean _preventDoubleSubmit = false;
    private boolean _simpleAction = false;
    private boolean _isOverloaded = false;
    private String _formMember;
    private String _formClass;  // applicable for non-ActionForm-derived form types
    private boolean _readonly = false;
    private Map/*< String, String >*/ _conditionalForwards = new LinkedHashMap/*< String, String >*/();
    private String _formBeanMessageResourcesKey;
    private String _defaultForward;
    

    public String getUnqualifiedActionPath()
    {
        return _unqualifiedActionPath;
    }

    public final void setUnqualifiedActionPath( String unqualifiedActionPath )
    {
        _unqualifiedActionPath = unqualifiedActionPath;
    }
    
    public String getUnqualifiedActionName()
    {
        if ( _unqualifiedActionPath != null && _unqualifiedActionPath.startsWith( "/" ) )
        {
            return _unqualifiedActionPath.substring( 1 );
        }
        else
        {
            return _unqualifiedActionPath;
        }
    }
    
    public boolean isLoginRequired()
    {
        return _loginRequired;
    }
    
    public void setLoginRequired( boolean loginRequired )
    {
        _loginRequired = loginRequired;
    }

    public boolean isPreventDoubleSubmit()
    {
        return _preventDoubleSubmit;
    }

    public void setPreventDoubleSubmit( boolean preventDoubleSubmit )
    {
        _preventDoubleSubmit = preventDoubleSubmit;
    }

    public boolean isSimpleAction()
    {
        return _simpleAction;
    }

    public void setSimpleAction( boolean simpleAction )
    {
        _simpleAction = simpleAction;
    }

    public boolean isOverloaded()
    {
        return _isOverloaded;
    }

    public void setOverloaded( boolean overloaded )
    {
        _isOverloaded = overloaded;
    }
    
    public String getFormMember()
    {
        return _formMember;
    }

    public void setFormMember( String formMember )
    {
        _formMember = formMember;
    }

    public String getFormClass()
    {
        return _formClass;
    }

    public void setFormClass( String formClass )
    {
        _formClass = formClass;
    }

    public boolean isReadonly()
    {
        return _readonly;
    }

    public void setReadonly( boolean readonly )
    {
        _readonly = readonly;
    }
    
    public void setConditionalForwards( String conditionalForwards )
    {
        String[] pairs = conditionalForwards.split( ";" );
        
        for ( int i = 0; i < pairs.length; i++ )
        {
            String pair = pairs[i];
            int delim = pair.indexOf( ':' );
            assert delim != -1 : pair;
            String forwardName = pair.substring( 0, delim );
            String expression = pair.substring( delim + 1 );
            _conditionalForwards.put( expression, forwardName );
        }
    }
    
    /**
     * Get a map of expression -> forward-name.  If the expression evaluates to <code>true</code> the forward is used.
     */ 
    public Map/*< String, String >*/ getConditionalForwardsMap()
    {
        return _conditionalForwards;
    }

    public String getFormBeanMessageResourcesKey()
    {
        return _formBeanMessageResourcesKey;
    }

    public void setFormBeanMessageResourcesKey( String formBeanMessageResourcesKey )
    {
        _formBeanMessageResourcesKey = formBeanMessageResourcesKey;
    }

    public String getDefaultForward()
    {
        return _defaultForward;
    }

    public void setDefaultForward( String defaultForward )
    {
        _defaultForward = defaultForward;
    }

    /**
     * Get a prefix directory path that all Forward local paths should be relative to.  By default this is
     * <code>null</code>, which means that there is no forced prefix path.
     */
    public String getLocalPathsRelativeTo()
    {
        return null;
    }
}
