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

import org.apache.beehive.netui.pageflow.config.PageFlowActionMapping;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class XmlBeanActionForm extends AnyBeanActionForm
{
    private static final Logger _log = Logger.getInstance( XmlBeanActionForm.class );
    
    private String _formClassName;
    
    public XmlBeanActionForm()
    {
    }
    
    public XmlBeanActionForm( Object xml )
    {
        setBean( xml );
    }
    
    public String getXmlString()
    {
        Object xmlBean = getBean();
        
        if ( xmlBean == null) return null;
        
        try
        {
            return ( String ) xmlBean.getClass().getMethod( "xmlText", null ).invoke( xmlBean, null );
        }
        catch ( InvocationTargetException e )
        {
            _log.error( "Error while getting XML String", e.getCause() );
        }
        catch ( Exception e )
        {
            assert e instanceof NoSuchMethodException || e instanceof IllegalAccessException : e.getClass().getName();
            _log.error( "Error while getting XML String", e );
        }
        
        return null;
    }
    
    public void setXmlString( String xml )
    {
        assert _formClassName != null;
        setBean( invokeFactoryMethod( "parse", new Class[]{ String.class }, new Object[]{ xml } ) );
    }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        if ( _formClassName == null )
        {
            assert mapping instanceof PageFlowActionMapping : mapping.getClass().getName();    
            _formClassName = ( ( PageFlowActionMapping ) mapping ).getFormClass();
            assert _formClassName != null;
        }
        
        if ( getBean() == null )
        {
            setBean( invokeFactoryMethod( "newInstance", new Class[0], new Object[0] ) );
        }
    }
    
    private Object invokeFactoryMethod( String methodName, Class[] argTypes, Object[] args )
    {
        String factoryClassName = _formClassName + "$Factory";

        try
        {
            Class factoryClass = Class.forName( factoryClassName );
            Method newInstanceMethod = factoryClass.getMethod( methodName, argTypes );
            return newInstanceMethod.invoke( factoryClass, args );
        }
        catch ( Exception e )
        {
            // Can be any exception -- not just the reflection-related exceptions...
            // because the exception could be thrown while creating the XML bean.
            if ( _log.isErrorEnabled() )
            {
                _log.error( "Error while creating XML object of type " + _formClassName, e );
            }
            
            return null;
        }
    }
}
