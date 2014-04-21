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

import org.apache.beehive.netui.compiler.JpfLanguageConstants;
import org.w3c.dom.Element;

public class ExceptionModel
        extends StrutsElementSupport
        implements JpfLanguageConstants
{
    private String _type;
    private String _path;
    private String _handlerMethod;
    private String _message;
    private String _messageKey;
    private String _handlerClass;
    private boolean _readonly = false;

    private static final String EXCEPTION_CONFIG_CLASSNAME = PAGEFLOW_PACKAGE + ".config.PageFlowExceptionConfig";


    protected ExceptionModel( StrutsApp parentApp )
    {
        super( parentApp );
    }

    public ExceptionModel( String type, String path, String handlerMethod, String message,
                           String messageKey, StrutsApp parentApp )
    {
        super( parentApp );

        _type = type;
        _path = path;
        _handlerMethod = handlerMethod;
        _message = message;
        _messageKey = messageKey;
    }

    public String getType()
    {
        return _type;
    }

    public void setType( String type )
    {
        _type = type;
    }

    public String getPath()
    {
        return _path;
    }

    public void setPath( String path )
    {
        _path = path;
    }

    public String getHandlerMethod()
    {
        return _handlerMethod;
    }

    public void setHandlerMethod( String handlerMethod )
    {
        _handlerMethod = handlerMethod;
    }

    public String getMessage()
    {
        return _message;
    }

    public void setMessage( String message )
    {
        _message = message;
    }

    public String getMessageKey()
    {
        return _messageKey;
    }

    public void setMessageKey( String messageKey )
    {
        _messageKey = messageKey;
    }

    public String getHandlerClass()
    {
        return _handlerClass;
    }

    public void setHandlerClass( String handlerClass )
    {
        _handlerClass = handlerClass;
    }

    protected void writeToElement(XmlModelWriter xw, Element element)
    {
        element.setAttribute("type", _type);

        if ( _path != null )
        {
            boolean relativeToModule = ! _path.startsWith( "/" );

            if ( relativeToModule )
            {
                // struts wants "/" -- assumes this is module-relative path
                setElementAttribute(element, "path", '/' + _path);
            }
            else
            {
                setElementAttributeMayBeEmpty(element, "path", _path);
                addSetProperty(xw, element, "isPathContextRelative", "true" );
            }
        }

        //
        // Set the message key.  If there isn't one, use the typename as the message key.
        setElementAttribute(element, "key", _messageKey);
        if (_messageKey == null) setElementAttribute(element, "key", _type);

        //
        // Struts doesn't support "message" directly -- we'll add this as a custom property.
        //
        addSetProperty(xw, element, "defaultMessage", _message );

        //
        // Note that we're setting the handler *method* as the handler.  This would break Struts.
        //
        if ( _handlerMethod != null && _handlerClass == null ) {
            setElementAttribute(element, "handler", _handlerMethod);
            addSetProperty(xw, element, "isHandlerMethod", "true" );
        }
        else {
            setElementAttribute(element, "handler", _handlerClass);
        }

        if ( _readonly ) addSetProperty(xw, element, "readonly", "true" );
        
        writeAdditionalSetProperties(xw, element);
    }

    protected void addSetProperty( XmlModelWriter xw, Element element, String propertyName, String propertyValue )
    {
        setCustomProperty(xw, element, propertyName, propertyValue, EXCEPTION_CONFIG_CLASSNAME);
    }

    public boolean isReadonly()
    {
        return _readonly;
    }

    public void setReadonly( boolean readonly )
    {
        _readonly = readonly;
    }
}
