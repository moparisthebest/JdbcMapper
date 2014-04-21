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
package org.apache.beehive.netui.compiler.model;

import java.util.List;
import java.util.ArrayList;

import org.apache.beehive.netui.compiler.JpfLanguageConstants;
import org.w3c.dom.Element;

/**
 * Represents an action forward in a Struts application.
 */
public class ForwardModel
        extends StrutsElementSupport
        implements JpfLanguageConstants
{
    private static final String JPF_ACTION_FWD_CLASSNAME = PAGEFLOW_PACKAGE + ".config.PageFlowActionForward";

    private boolean _isNestedReturn = false;
    private boolean _contextRelative = false;
    private String _name;  // required to be set
    private String _path;  // required to be set
    private boolean _redirect = false;
    private boolean _externalRedirect = false;
    private boolean _returnToPage = false;
    private boolean _returnToAction = false;
    private String _outputFormBeanType;
    private String _outputFormBeanMember;
    private boolean _hasExplicitRedirectValue = false;
    private List _actionOutputs = null;
    private boolean _restoreQueryString = false;
    private String _relativeTo;


    protected ForwardModel( StrutsApp parent )
    {
        super( parent );
    }

    public ForwardModel( String name, String path, StrutsApp parent )
    {
        super( parent );
        _name = name;
        _path = path;
    }

    protected void writeToElement(XmlModelWriter xw, Element element)
    {
        assert _name != null;
        element.setAttribute("name", _name);
        setElementAttributeMayBeEmpty(element, "path", _path == null ? "" : _path);
        setElementAttribute(element, "contextRelative", _contextRelative);
        setElementAttribute(element, "redirect", _redirect);

        //
        // "externalRedirect" is set using set-property, to indicate that the redirect
        // is to another app.
        //
        if ( _externalRedirect ) addSetProperty( xw, element, "externalRedirect", "true" );

        //
        // "returnToPage" is set using set-property, which requires us to override the
        // ActionForward class.
        //
        if ( _returnToPage ) addSetProperty( xw, element, "returnToPage", "true" );

        //
        // "returnToAction" is set using set-property, which requires us to override the
        // ActionForward class.
        //
        if ( _returnToAction ) addSetProperty( xw, element, "returnToAction", "true" );

        if ( _hasExplicitRedirectValue ) addSetProperty( xw, element, "hasExplicitRedirectValue", "true" );

        if ( _restoreQueryString ) addSetProperty( xw, element, "restoreQueryString", "true" );

        if ( _actionOutputs != null && _actionOutputs.size() > 0 )
        {
            int n = _actionOutputs.size();
            addSetProperty(xw, element, "actionOutputCount", Integer.toString( n ) );

            for ( int i = 0; i < n; ++i )
            {
                ActionOutputModel pi = ( ActionOutputModel ) _actionOutputs.get( i );
                String value = pi.getType() + '|' + pi.getNullable() + '|' + pi.getName();
                addSetProperty(xw, element, "actionOutput" + i, value);
            }
        }

        //
        // "nestedReturn" is set using set-property, which requires us to override the
        // ActionForward class.
        //
        if ( _isNestedReturn ) addSetProperty( xw, element, "nestedReturn", "true" );
        if ( _outputFormBeanType != null ) addSetProperty( xw, element, "returnFormType", _outputFormBeanType );
        if ( _outputFormBeanMember != null ) addSetProperty( xw, element, "returnFormMember", _outputFormBeanMember );
        if ( _relativeTo != null ) addSetProperty( xw, element, "relativeTo", _relativeTo );
    }

    public boolean isReturnToPage()
    {
        return _returnToPage;
    }

    public void setReturnToPage( boolean returnToPage )
    {
        _returnToPage = returnToPage;
    }

    public boolean isReturnToAction()
    {
        return _returnToAction;
    }

    public void setReturnToAction( boolean returnToAction )
    {
        _returnToAction = returnToAction;
    }

    public void setIsNestedReturn( boolean nestedReturn )
    {
        _isNestedReturn = nestedReturn;
    }

    public String getOutputFormBeanType()
    {
        return _outputFormBeanType;
    }

    public void setOutputFormBeanType( String outputFormBeanType )
    {
        _outputFormBeanType = outputFormBeanType;
    }

    public String getOutputFormBeanMember()
    {
        return _outputFormBeanMember;
    }

    public void setOutputFormBeanMember( String outputFormBeanMember )
    {
        _outputFormBeanMember = outputFormBeanMember;
    }

    public boolean getContextRelative()
    {
        return _contextRelative;
    }

    public void setContextRelative( boolean contextRelative )
    {
        _contextRelative = contextRelative;
    }

    public String getName()
    {
        return _name;
    }

    public void setName( String name )
    {
        _name = name;
    }

    public String getPath()
    {
        return _path;
    }

    public void setPath( String path )
    {
        _path = path;
    }

    public boolean isRedirect()
    {
        return _redirect;
    }

    public void setRedirect( boolean redirect )
    {
        _redirect = redirect;
        _hasExplicitRedirectValue = redirect;
    }

    public boolean isExternalRedirect()
    {
        return _externalRedirect;
    }

    public void setExternalRedirect( boolean externalRedirect )
    {
        _externalRedirect = externalRedirect;
        if ( externalRedirect  ) setRedirect( externalRedirect );
    }

    public boolean isRestoreQueryString()
    {
        return _restoreQueryString;
    }

    public void setRestoreQueryString( boolean restore )
    {
        _restoreQueryString = restore;
    }

    /**
     * @deprecated
     * @see #forwardsToPage
     */
    public final boolean isPageForward()
    {
        return forwardsToPage();
    }

    public boolean forwardsToPage()
    {
        return ! _path.endsWith( ".do" ) && ! _path.endsWith( ".jpf" );  // NOI18N
    }

    public boolean forwardsToAction()
    {
        return _path.endsWith( ACTION_EXTENSION_DOT );  // NOI18N
    }

    public final boolean forwardsToPageFlow()
    {
        return _path.endsWith( JPF_FILE_EXTENSION_DOT );  // NOI18N
    }

    public String getPageName()
    {
        assert forwardsToPage() : "getPageName() called for non-page " + _path;  // NOI18N

        int slash = _path.lastIndexOf( '/' );  // NOI18N
        return slash != -1 ? _path.substring( slash + 1 ) : _path;
    }

    public String getActionName()
    {
        assert forwardsToAction() : "getActionName() called for non-action" + _path;  // NOI18N

        int index = _path.indexOf( ACTION_EXTENSION_DOT );  // NOI18N
        assert index != -1;
        return _path.substring( 0, index );
    }

    public void addActionOutput( ActionOutputModel actionOutput )
    {
        if ( _actionOutputs == null ) _actionOutputs = new ArrayList();
        _actionOutputs.add( actionOutput );
    }

    public boolean isNestedReturn()
    {
        return _isNestedReturn;
    }

    protected void addSetProperty( XmlModelWriter xw, Element element, String propertyName, String propertyValue )
    {
        setCustomProperty(xw, element, propertyName, propertyValue, JPF_ACTION_FWD_CLASSNAME);
    }

    /**
     * Set the path for the struts module that this forward is relative to.
     * @param relativeTo The struts module path that this forward is relative to.
     */
    public void setRelativeTo( String relativeTo )
    {
        _relativeTo = relativeTo;
    }
}
