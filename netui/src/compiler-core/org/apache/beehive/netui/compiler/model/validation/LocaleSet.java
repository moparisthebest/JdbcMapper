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
package org.apache.beehive.netui.compiler.model.validation;

import org.apache.beehive.netui.compiler.model.XmlModelWriter;
import org.apache.beehive.netui.compiler.model.XmlElementSupport;
import org.w3c.dom.Element;

import java.util.Locale;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class LocaleSet
    extends XmlElementSupport
{
    private Locale _locale;
    private Map _entities = new HashMap();
    
    
    public LocaleSet()
    {
        _locale = null;     // default locale;
    }
    
    public LocaleSet( Locale locale )
    {
        _locale = locale;
    }
    
    public Locale getLocale()
    {
        return _locale;
    }
    
    public ValidatableEntity getEntity( String entityName )
    {
        return ( ValidatableEntity ) _entities.get( entityName );
    }
    
    public void addValidatableEntity( ValidatableEntity entity )
    {
        _entities.put( entity.getEntityName(), entity );
    }
    
    public void writeToElement(XmlModelWriter xw, Element element)
    {
        if ( _locale != null ) {
            setElementAttribute(element, "language", _locale.getLanguage());
            setElementAttribute(element, "country", _locale.getCountry());
            setElementAttribute(element, "variant", _locale.getVariant());
        }
        
        for ( Iterator i = _entities.values().iterator(); i.hasNext(); )
        {
            ValidatableEntity entity = ( ValidatableEntity ) i.next();
            String entityName = entity.getEntityName();
            Element formElementToUse = findChildElement(xw, element, "form", "name", entityName, true, null);
            entity.writeXML(xw, formElementToUse);
        }
    }
}
