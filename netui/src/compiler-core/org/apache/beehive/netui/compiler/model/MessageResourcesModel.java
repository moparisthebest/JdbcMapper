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

import org.w3c.dom.Element;

public class MessageResourcesModel extends StrutsElementSupport
{
    private String _parameter;   // the target resources
    private String _key;         // the ServletContext attribute in which to store the resources
    private Boolean _returnNull; // if false, will return '???keyname???' instead of null for missing resources
    private String _factory;     // configuration bean class -- org.apache.struts.config.MessageResourcesConfig

    public MessageResourcesModel( StrutsApp parent )
    {
        super( parent );
    }

    public String getParameter()
    {
        return _parameter;
    }

    public void setParameter( String parameter )
    {
        _parameter = parameter;
    }

    public String getKey()
    {
        return _key;
    }

    public void setKey( String key )
    {
        _key = key;
    }

    public boolean doesReturnNull()
    {
        return _returnNull == null || _returnNull.booleanValue();
    }

    public void setReturnNull( boolean aNull )
    {
        _returnNull = new Boolean( aNull );
    }

    public String getFactory()
    {
        return _factory;
    }

    public void setFactory( String factory )
    {
        _factory = factory;
    }

    public Boolean getReturnNull()
    {
        return _returnNull;
    }

    public void setReturnNull( Boolean returnNull )
    {
        _returnNull = returnNull;
    }

    protected void writeToElement(XmlModelWriter xw, Element element)
    {
        setElementAttribute(element, "key", _key);
        setElementAttribute(element, "parameter", _parameter);
        setElementAttribute(element, "null", _returnNull);
        setElementAttribute(element, "factory", _factory);
    }
    
    protected void addSetProperty( XmlModelWriter xw, Element element, String propertyName, String propertyValue )
    {
        throw new UnsupportedOperationException("not implemented for " + getClass().getName());
    }
}
