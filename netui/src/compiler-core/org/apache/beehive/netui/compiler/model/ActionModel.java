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

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Iterator;

import org.apache.beehive.netui.compiler.JpfLanguageConstants;
import org.w3c.dom.Element;


/**
 * Represents an ActionMapping in a Struts based web application or
 * sub application.
 */
public class ActionModel
        extends AbstractForwardContainer
        implements ForwardContainer, ExceptionContainer, JpfLanguageConstants
{
    public static final String DEFAULT_FORM_SCOPE = "request";

    private static final String PAGEFLOW_ACTION_MAPPING_CLASSNAME = PAGEFLOW_PACKAGE + ".config.PageFlowActionMapping";


    // Struts attributes.
    private ArrayList _exceptionCatches = new ArrayList();
    private String _attribute;
    private String _forward;
    private String _include;
    private String _input;
    private String _formBeanName;
    private String _parameter;
    private String _path;  // required to be set
    private String _prefix;
    private String _scope = DEFAULT_FORM_SCOPE;
    private String _suffix;
    private String _type;
    private boolean _unknown;
    private String _roles;
    private boolean _validate;

    // Non-struts attributes.
    private String _unqualifiedActionPath;
    private Boolean _loginRequired;
    private boolean _isOverloaded;
    private Boolean _readonly;
    private boolean _isSimpleAction = false;
    private boolean _preventDoubleSubmit = false;
    private String _formMember;     // pageflow-scoped form
    private String _formClass;      // applicable for non-ActionForm-derived types
    private LinkedHashMap _conditionalForwards;
    private String _formBeanMessageResourcesKey;
    private String _defaultForwardName;     // for Simple Actions


    public ActionModel( String path, String formName, StrutsApp parent )
    {
        super( parent );
        this._path = path;
        this._formBeanName = formName;
    }

    protected ActionModel( StrutsApp parent )
    {
        this( null, null, parent );
    }

    public void setFormBeanName( String formBeanName )
    {
        _formBeanName = formBeanName;
    }

    protected void writeToElement(XmlModelWriter xw, Element element)
    {
        element.setAttribute("path", _path);
        setElementAttribute(element, "name", _formBeanName);
        setElementAttribute(element, "className", getClassName());
        setElementAttribute(element, "type", _type);
        setElementAttribute(element, "attribute", _attribute);
        setElementAttribute(element, "input", _input);
        setElementAttribute(element, "parameter", _parameter);
        setElementAttribute(element, "prefix", _prefix);
        setElementAttribute(element, "suffix", _suffix);
        setElementAttribute(element, "scope", _scope != null ? _scope : "request");
        setElementAttribute(element, "roles", _roles);
        setElementAttribute(element, "forward", _forward);
        setElementAttribute(element, "include", _include);
        setElementAttribute(element, "validate", Boolean.toString(_validate));  // always set the value, even if false
        addSetProperty( xw, element, "unqualifiedActionPath", _unqualifiedActionPath );
        addSetProperty( xw, element, "formMember", _formMember );
        addSetProperty( xw, element, "formClass", _formClass );
        addSetProperty( xw, element, "loginRequired", _loginRequired );
        addSetProperty( xw, element, "preventDoubleSubmit", _preventDoubleSubmit );
        addSetProperty( xw, element, "overloaded", _isOverloaded );
        addSetProperty( xw, element, "readonly", _readonly );
        addSetProperty( xw, element, "simpleAction", _isSimpleAction );
        addSetProperty( xw, element, "defaultForward", _defaultForwardName );
        addSetProperty( xw, element, "formBeanMessageResourcesKey", _formBeanMessageResourcesKey );

        if ( _conditionalForwards != null )
        {
            addSetProperty( xw, element, "conditionalForwards", getMapString( _conditionalForwards ) );
        }

        if ( _exceptionCatches != null && ! _exceptionCatches.isEmpty() )
        {
            for ( int i = 0; i < _exceptionCatches.size(); ++i )
            {
                ExceptionModel ec = ( ExceptionModel ) _exceptionCatches.get( i );
                Element exceptionToEdit = findChildElement(xw, element, "exception", "type", ec.getType(), true, null);
                ec.writeXML( xw, exceptionToEdit );
            }
        }

        // forwards
        writeForwards( xw, element );
        
        writeAdditionalSetProperties(xw, element);
    }

    private void addSetProperty( XmlModelWriter xw, Element element, String propertyName, boolean propertyValue )
    {
        if (propertyValue) addSetProperty( xw, element, propertyName, Boolean.toString( propertyValue ) );
    }

    private void addSetProperty( XmlModelWriter xw, Element element, String propertyName, Boolean propertyValue )
    {
        if (propertyValue != null) addSetProperty( xw, element, propertyName, propertyValue.toString() );
    }

    protected void addSetProperty( XmlModelWriter xw, Element element, String propertyName, String propertyValue )
    {
        setCustomProperty(xw, element, propertyName, propertyValue, PAGEFLOW_ACTION_MAPPING_CLASSNAME);
    }

    /**
     * Implemented for {@link ExceptionContainer}.
     */
    public void addException( ExceptionModel em )
    {
        _exceptionCatches.add( em );
    }

    public String getAttribute()
    {
        return _attribute;
    }

    public void setAttribute( String attribute )
    {
        this._attribute = attribute;
    }

    public String getForward()
    {
        return _forward;
    }

    public void setForward( String forward )
    {
        this._forward = forward;
    }

    public String getInclude()
    {
        return _include;
    }

    public void setInclude( String include )
    {
        this._include = include;
    }

    public String getInput()
    {
        return _input;
    }

    public void setInput( String input )
    {
        this._input = input;
    }

    public String getName()
    {
        return _formBeanName;
    }

    public String getFormBeanName()
    {
        return _formBeanName;
    }

    public void setName( String name )
    {
        this._formBeanName = name;
    }

    public String getParameter()
    {
        return _parameter;
    }

    public void setParameter( String parameter )
    {
        this._parameter = parameter;
    }

    public boolean isValidate()
    {
        return _validate;
    }

    public void setValidate( boolean validate )
    {
        _validate = validate;
    }

    public String getPath()
    {
        return _path;
    }

    public String getPath( boolean useUnqualifiedPath )
    {
        if ( useUnqualifiedPath && _unqualifiedActionPath != null )
        {
            return _unqualifiedActionPath;
        }
        else
        {
            return _path;
        }
    }

    public void setPath( String path )
    {
        this._path = path;
    }

    public String getPrefix()
    {
        return _prefix;
    }

    public void setPrefix( String prefix )
    {
        this._prefix = prefix;
    }

    public String getScope()
    {
        return _scope == null ? DEFAULT_FORM_SCOPE : _scope;
    }

    public void setScope( String scope )
    {
        this._scope = scope;
    }

    public String getSuffix()
    {
        return _suffix;
    }

    public void setSuffix( String suffix )
    {
        this._suffix = suffix;
    }

    public String getType()
    {
        return _type;
    }

    public void setType( String type )
    {
        this._type = type;
    }

    public boolean isUnknown()
    {
        return _unknown;
    }

    public void setUnknown( boolean unknown )
    {
        this._unknown = unknown;
    }

    public String getUnqualifiedActionPath()
    {
        return _unqualifiedActionPath;
    }

    public void setUnqualifiedActionPath( String unqualifiedActionPath )
    {
        this._unqualifiedActionPath = unqualifiedActionPath;
    }

    public String getDefaultForwardName()
    {
        return _defaultForwardName;
    }

    public void setDefaultForwardName( String defaultForwardName )
    {
        _defaultForwardName = defaultForwardName;
    }

    public String getRoles()
    {
        return _roles;
    }

    public void setRoles( String roles )
    {
        _roles = roles;
    }

    /**
     * Set the value to use for the login required <set-property> of the <action>.
     * If the value is null, then this <set-property> will not be included in the
     * <action>.
     * @param loginRequired if <code>true</code>, login is required for this action.
     *                      If <code>false</code>, no login is required. Otherwise,
     *                      a <set-property> will not be written, implying
     *                      login is not required.
     */
    public void setLoginRequired( Boolean loginRequired )
    {
        _loginRequired = loginRequired;
    }

    public void setPreventDoubleSubmit( boolean preventDoubleSubmit )
    {
        _preventDoubleSubmit = preventDoubleSubmit;
    }

    public boolean isSimpleAction()
    {
        return _isSimpleAction;
    }

    public void setSimpleAction( boolean simpleAction )
    {
        _isSimpleAction = simpleAction;
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

    public Boolean isReadonly()
    {
        return _readonly;
    }

    /**
     * Set the value to use for the read only <set-property> of the <action>.
     * If the value is null, then this <set-property> will not be included in
     * the <action>. If set to <code>true</code>, then by default the action
     * "promises" that it will not modify member data.
     * @param readonly if true, this action is read only .If false, it is not
     *                 read only (the default). Otherwise, a <set-property>
     *                 will not be written, implying it is not read only. 
     */
    public void setReadonly( Boolean readonly )
    {
        _readonly = readonly;
    }

    public void addConditionalForward( String expression, String forwardName )
    {
        if ( _conditionalForwards == null ) _conditionalForwards = new LinkedHashMap();
        _conditionalForwards.put( expression, forwardName );
    }

    private static String getMapString( Map map )
    {
        StringBuffer retVal = new StringBuffer();

        for ( Iterator i = map.entrySet().iterator(); i.hasNext(); )
        {
            Map.Entry entry = ( Map.Entry ) i.next();
            retVal.append( entry.getValue() ).append( ':' ).append( entry.getKey() ).append( ';' );
        }

        return retVal.toString();
    }

    public void setFormBeanMessageResourcesKey( String formBeanMessageResourcesKey )
    {
        _formBeanMessageResourcesKey = formBeanMessageResourcesKey;
    }

    public void setActionName(String actionName)
    {
        setPath('/' + actionName);
    }

    public String getActionPath()
    {
        return getPath();
    }
}
