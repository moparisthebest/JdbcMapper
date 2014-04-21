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

import javax.servlet.ServletContext;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


/**
 * Information that is cached per pageflow class.
 */ 
public class CachedFacesBackingInfo
        extends CachedSharedFlowRefInfo
{
    /**
     * The PageFlowController-initialized member field -- may or may not be present.
     */ 
    private Field _pageFlowMemberField;
    
    
    public CachedFacesBackingInfo( Class facesBackingClass, ServletContext servletContext )
    {
        Field[] fields = facesBackingClass.getDeclaredFields();
        AnnotationReader annReader = AnnotationReader.getAnnotationReader( facesBackingClass, servletContext );
        
        for ( int i = 0; i < fields.length; i++ )
        {
            Field field = fields[i];
            
            if ( annReader.getJpfAnnotation( field, "PageFlowField" ) != null )
            {
                _pageFlowMemberField = field;
                if ( ! Modifier.isPublic( _pageFlowMemberField.getModifiers() ) )
                {
                    _pageFlowMemberField.setAccessible( true );
                }
            }
        }
        
        initSharedFlowFields( annReader, facesBackingClass );
    }
    
    public Field getPageFlowMemberField()
    {
        return _pageFlowMemberField;
    }
}

