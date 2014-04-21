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

import org.apache.beehive.netui.pageflow.PageFlowConstants;
import org.apache.beehive.netui.pageflow.internal.annotationreader.AnnotationAttribute;
import org.apache.beehive.netui.pageflow.internal.annotationreader.ProcessedAnnotation;
import org.apache.beehive.netui.pageflow.internal.annotationreader.ProcessedAnnotations;
import org.apache.beehive.netui.pageflow.internal.annotationreader.ProcessedAnnotationParser;
import org.apache.beehive.netui.util.internal.concurrent.InternalConcurrentHashMap;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Member;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility for reading XML files that describe annotations in classes.  These files are generated during
 * Page Flow build.
 */
public class AnnotationReader
        implements Serializable
{
    private static final Logger _log = Logger.getInstance( AnnotationReader.class );
    private static final String CACHE_ATTR = InternalConstants.ATTR_PREFIX + "annCache";

    private ProcessedAnnotations _annotations;

    public static AnnotationReader getAnnotationReader( Class type, ServletContext servletContext )
    {
        InternalConcurrentHashMap cache = ( InternalConcurrentHashMap ) servletContext.getAttribute( CACHE_ATTR );

        if ( cache == null )
        {
            cache = new InternalConcurrentHashMap();
            servletContext.setAttribute( CACHE_ATTR, cache );
        }

        AnnotationReader reader = ( AnnotationReader ) cache.get( type );

        if ( reader == null )
        {
            reader = new AnnotationReader( type, servletContext );
            cache.put( type, reader );
        }

        return reader;
    }

    private AnnotationReader( Class type, ServletContext servletContext )
    {
        String annotationsXml =
                PageFlowConstants.PAGEFLOW_MODULE_CONFIG_GEN_DIR + "/annotations-"
                + type.getName().replace( '.', '-' ) + ".xml";
        InputStream in = servletContext.getResourceAsStream( annotationsXml );
        if ( in == null )
        {
            assert annotationsXml.startsWith( "/" ) : annotationsXml;
            annotationsXml = annotationsXml.substring( 1 );
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream( annotationsXml );
        }


        if ( in != null )
        {
            _annotations = ProcessedAnnotationParser.parse( annotationsXml, in );
            try
            {
                in.close();
            }
            catch ( IOException e )
            {
                _log.error( "Could not close input stream for " + annotationsXml, e );
            }
        }
    }

    public ProcessedAnnotation getAnnotation( String declarationName, String annotationTypeName )
    {
        if ( _annotations == null ) return null;

        Map elements = _annotations.getAnnotatedElements();

        for ( Iterator i = elements.keySet().iterator(); i.hasNext(); )
        {
            String name = ( String ) i.next();
            if ( name.equals( declarationName ) )
            {
                // For now, we can be sure that there's only one element in this array.
                ProcessedAnnotation[] annotations = ( ProcessedAnnotation[] ) elements.get( name );
                assert annotations.length == 1 : annotations.length;
                ProcessedAnnotation pa = annotations[ 0 ];
                return pa.getAnnotationName().equals( annotationTypeName ) ? pa : null;
            }
        }

        return null;
    }

    public ProcessedAnnotation getJpfAnnotation( Member member, String annotationTypeName )
    {
        return getAnnotation( member.getName(), InternalConstants.ANNOTATION_QUALIFIER + annotationTypeName );
    }

    public ProcessedAnnotation getJpfAnnotation( Class type, String annotationTypeName )
    {
        return getAnnotation( type.getName(), InternalConstants.ANNOTATION_QUALIFIER + annotationTypeName );
    }

    public static String getStringAttribute( ProcessedAnnotation ann, String attrName )
    {
        AnnotationAttribute[] attrs = ann.getAnnotationAttributes();

        for ( int i = 0; i < attrs.length; i++ )
        {
            AnnotationAttribute attr = attrs[i];

            if ( attr.getAttributeName().equals( attrName ) )
            {
                String value = attr.getStringValue();
                assert value != null : "attribute " + attrName + " did not have a String value";
                return value;
            }
        }

        return null;
    }

    public static ProcessedAnnotation[] getAnnotationArrayAttribute( ProcessedAnnotation ann, String attrName )
    {
        AnnotationAttribute[] attrs = ann.getAnnotationAttributes();

        for ( int i = 0; i < attrs.length; i++ )
        {
            AnnotationAttribute attr = attrs[i];

            if ( attr.getAttributeName().equals( attrName ) )
            {
                ProcessedAnnotation[] array = attr.getAnnotationValues();
                assert array != null : "attribute " + attrName + " did not have an array of annotations.";
                return array;
            }
        }

        return null;
    }
}
