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

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Defines general support for elements that
 */
public abstract class StrutsElementSupport extends XmlElementSupport
{
    private StrutsApp _parentApp;
    private String _className;
    private LinkedHashMap _additionalSetProperties = null;

    public StrutsElementSupport( StrutsApp parentApp )
    {
        _parentApp = parentApp;
    }

    protected StrutsApp getParentApp()
    {
        return _parentApp;
    }

    public String getClassName()
    {
        return _className;
    }

    public void setClassName( String className )
    {
        _className = className;
    }

    protected void setParentApp( StrutsApp parentApp )
    {
        _parentApp = parentApp;
    }

    protected final void setCustomProperty(XmlModelWriter xw, Element element, String propertyName, String value,
                                           String changeElementClassName)
    {
        if (value != null) {
            Element setPropertyElement = findChildElement(xw, element, "set-property", "property", propertyName, true, null);
            setPropertyElement.setAttribute("property", propertyName);
            setElementAttributeMayBeEmpty(setPropertyElement, "value", value);
            setElementAttribute(element, "className", changeElementClassName);
        }
    }


    protected void addSetProperty(String name, String value)
    {
        if (_additionalSetProperties == null) _additionalSetProperties = new LinkedHashMap();
        _additionalSetProperties.put(name, value);
    }

    protected void writeAdditionalSetProperties(XmlModelWriter xw, Element element)
    {
        // Add additional set-property elements.
        if (_additionalSetProperties != null) {
            for (Iterator i = _additionalSetProperties.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                addSetProperty(xw, element, (String) entry.getKey(), (String) entry.getValue());
            }
        }
    }
    
    protected abstract void addSetProperty(XmlModelWriter xw, Element element, String propertyName, String propertyValue);
}
