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
import java.util.Collection;
import java.util.List;

import org.apache.beehive.netui.compiler.JpfLanguageConstants;
import org.w3c.dom.Element;


/**
 * Represents a form bean in a Struts application.
 */
public class FormBeanModel
        extends StrutsElementSupport
        implements JpfLanguageConstants
{
    public static class Property
    {
        private String _name;
        private String _type;
        private boolean _required;
        private boolean _multiValue;

        public Property( String name, String type, boolean required, boolean multival )
        {
            _name = name;
            _type = type;
            _required = required;
            _multiValue = multival;
        }

        public String getName()
        {
            return _name;
        }

        public void setName( String name )
        {
            _name = name;
        }

        public String getType()
        {
            return _type;
        }

        public void setType( String type )
        {
            _type = type;
        }

        public boolean isRequired()
        {
            return _required;
        }

        public void setRequired( boolean required )
        {
            _required = required;
        }

        public boolean isMultiValue ()
        {
            return _multiValue;
        }

        public void setMultiValue ( boolean multi )
        {
            _multiValue = multi;
        }
    }


    private static final String CUSTOM_ACTION_FORM_BEAN_CLASSNAME = PAGEFLOW_PACKAGE + ".config.PageFlowActionFormBean";

    private String _id = "";  // NOI18N
    private boolean _dynamic = false;
    private String _name = null;  // required to be set
    private String _type = null;  // required to be set
    private String _formBeanMessageResourcesKey = null;

    /** This is a NetUI-specific property. */
    private String _actualType = null;

    /** This is a NetUI-specific property. */
    private boolean _pageFlowScoped;  // required to be set

    private ArrayList _properties = new ArrayList();

    public FormBeanModel( String name, String type, String actualType, boolean pageFlowScoped,
                          String formBeanMessageResorucesKey, StrutsApp parent )
    {
        super( parent );
        _name = name;
        _type = type;
        _actualType = actualType;
        _pageFlowScoped = pageFlowScoped;
        _formBeanMessageResourcesKey = formBeanMessageResorucesKey;
    }

    protected void writeToElement(XmlModelWriter xw, Element element)
    {
        element.setAttribute("name", _name);
        
        setElementAttribute(element, "type", _type);
        setElementAttribute(element, "id", _id);
        setElementAttribute(element, "className", getClassName());
        setElementAttribute(element, "dynamic", _dynamic);
        setElementAttribute(element, "className", CUSTOM_ACTION_FORM_BEAN_CLASSNAME);
        
        if ( _actualType != null && ! _actualType.equals(element.getAttribute("type")) )
        {
            setCustomProperty(xw, element, "actualType", _actualType, CUSTOM_ACTION_FORM_BEAN_CLASSNAME);
        }
    }

    public String getId()
    {
        return _id;
    }

    public void setId( String id )
    {
        _id = id;
    }

    public void setClassName( String className )
    {
        if ( className != null )
        {
            if ("org.apache.struts.action.DynaActionForm".equals( className ) )  // NOI18N
                _dynamic = true;

            setClassName(className);
        }
    }

    public boolean isDynamic()
    {
        return _dynamic;
    }

    public void setDynamic( boolean dynamic )
    {
        _dynamic = dynamic;
    }

    public String getName()
    {
        return _name;
    }

    public void setName( String name )
    {
        _name = name;
    }

    public String getType()
    {
        return _type;
    }

    public void setType( String type )
    {
        _type = type;
    }

    public String getActualType()
    {
        return _actualType;
    }

    public void setActualType(String actualType)
    {
        _actualType = actualType;
    }

    public String getFormBeanMessageResourcesKey() {
        return _formBeanMessageResourcesKey;
    }

    public void addProperty( String name, String type, boolean required, boolean multival )
    {
        _properties.add( new Property( name, type, required, multival ) );
    }

    /**
     * Sets the collection of properties for a form bean to a new collection.
     */
    public void updateProperties( Collection newProps )
    {
        _properties = new ArrayList();

        if ( newProps != null )
        {
            _properties.addAll( newProps );
        }
    }

    public Property[] getProperties()
    {
        return ( Property[] ) _properties.toArray( new Property[]{} );
    }

    public void deleteProperty( String name )
    {
        for ( int i = 0; i < _properties.size(); ++i )
        {
            Property prop = ( Property ) _properties.get( i );

            if ( prop.getName().equals( name ) )
            {
                _properties.remove( i-- );
            }
        }
    }

    public void deleteProperty( Property prop )
    {
        _properties.remove( prop );
    }

    public Property findProperty( String name )
    {
        int index = findPropertyIndex( name );
        return index != -1 ? ( Property ) _properties.get( index ) : null;
    }

    protected int findPropertyIndex( String name )
    {
        for ( int i = 0; i < _properties.size(); ++i )
        {
            Property prop = ( Property ) _properties.get( i );

            if ( prop.getName().equals( name ) )
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns a clone (shallow copy) of the internal properties list.
     */
    protected final List getPropertyList()
    {
        return ( List ) _properties.clone();
    }

    public boolean isPageFlowScoped()
    {
        return _pageFlowScoped;
    }
    
    protected void addSetProperty( XmlModelWriter xw, Element element, String propertyName, String propertyValue )
    {
        throw new UnsupportedOperationException("not implemented for " + getClass().getName());
    }
}
