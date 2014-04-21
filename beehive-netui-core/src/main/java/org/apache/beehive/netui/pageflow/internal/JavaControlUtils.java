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

import java.beans.beancontext.BeanContext;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import org.apache.beehive.controls.api.bean.Controls;
import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.properties.AnnotatedElementMap;
import org.apache.beehive.netui.pageflow.ControlFieldInitializationException;
import org.apache.beehive.netui.pageflow.PageFlowManagedObject;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.PageFlowControlContainerFactory;
import org.apache.beehive.netui.pageflow.PageFlowControlContainer;
import org.apache.beehive.netui.util.internal.concurrent.InternalConcurrentHashMap;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * This class provides utilities for integrating Page Flow and Beehive's Controls technology.  In order
 * to use the two together, a Page Flow must act as a <em>Control container</em> and implement the
 * Control lifecycle contract.
 */
public class JavaControlUtils
{
    private static final Logger LOG = Logger.getInstance( JavaControlUtils.class );
    private static final String CONTROL_ANNOTATION_CLASSNAME = Control.class.getName();

    /** Map of control-container-class (e.g., PageFlowController) to Map of fields/control-properties. */
    private static InternalConcurrentHashMap/*< String, Map< Field, PropertyMap > >*/ _controlFieldCache =
            new InternalConcurrentHashMap/*< String, Map< Field, PropertyMap > >*/();

    /**
     * Initialize all null member variables that are Java Controls.
     *
     * @param request the current HttpServletRequest
     * @param response the current HttpServletRequest
     * @param servletContext the ServletContext
     * @param controlClient the class with possible annotated Control fields
     * @throws org.apache.beehive.netui.pageflow.ControlFieldInitializationException
     */
    public static void initJavaControls( HttpServletRequest request,
                                         HttpServletResponse response,
                                         ServletContext servletContext,
                                         PageFlowManagedObject controlClient )
        throws ControlFieldInitializationException
    {
        Class controlClientClass = controlClient.getClass();

        //
        // First, just return if there are no annotated Control fields.  This saves us from having to catch a
        // (wrapped) ClassNotFoundException for the control client initializer if we were to simply call
        // Controls.initializeClient().
        //
        Map controlFields = getAccessibleControlFieldAnnotations( controlClientClass, servletContext );
        if ( controlFields.isEmpty() ) {
            if(LOG.isTraceEnabled())
                LOG.trace("No control field annotations were found for " + controlClient);
            PageFlowControlContainer pfcc = PageFlowControlContainerFactory.getControlContainer(request,servletContext);
            pfcc.beginContextOnPageFlow(controlClient,request,response,servletContext);
            return;
        }

        request = PageFlowUtils.unwrapMultipart( request );

        PageFlowControlContainer pfcc = PageFlowControlContainerFactory.getControlContainer(request,servletContext);
        pfcc.createAndBeginControlBeanContext(controlClient,request,response,servletContext);
        ControlBeanContext beanContext = pfcc.getControlContainerContext(controlClient);
        assert beanContext != null : "ControlBeanContext was not initialized by PageFlowRequestProcessor";

        try
        {
            if(LOG.isDebugEnabled())
                LOG.debug("Initializing control client " + controlClient);
            Controls.initializeClient(null, controlClient, beanContext);
        }
        catch ( Exception e )
        {
            LOG.error( "Exception occurred while initializing controls", e);
            throw new ControlFieldInitializationException( controlClientClass.getName(), controlClient, e );
        }
    }

    /**
     * Clean up all member variables that are Java Controls.
     */
    public static void uninitJavaControls( ServletContext servletContext, PageFlowManagedObject controlClient )
    {
        Map controlFields = getAccessibleControlFieldAnnotations( controlClient.getClass(), servletContext );

        for ( Iterator i = controlFields.keySet().iterator(); i.hasNext(); )
        {
            Field controlField = ( Field ) i.next();

            try
            {
                Object fieldValue = controlField.get( controlClient );

                if ( fieldValue != null )
                {
                    controlField.set( controlClient, null );
                    destroyControl( fieldValue );
                }
            }
            catch ( IllegalAccessException e )
            {
                LOG.error( "Exception while uninitializing Java Control " + controlField.getName(), e );
            }
        }
    }

    /**
     * Destroy a Beehive Control.
     *
     * @param controlInstance the Control instance
     */
    private static void destroyControl( Object controlInstance )
    {
        assert controlInstance instanceof ControlBean : controlInstance.getClass().getName();
        BeanContext beanContext = ( ( ControlBean ) controlInstance ).getBeanContext();
        if ( beanContext != null ) {
            if(LOG.isTraceEnabled())
                LOG.trace("Removing control " + controlInstance + " from ControlBeanContext " + beanContext);
            beanContext.remove( controlInstance );
        }
    }

    /**
     * @return a map of Field (accessible) -> AnnotationMap
     */
    private static Map getAccessibleControlFieldAnnotations( Class controlClientClass, ServletContext servletContext )
    {
        String className = controlClientClass.getName();

        //
        // Reading the annotations is expensive.  See if there's a cached copy of the map.
        //
        Map/*< Field, PropertyMap >*/ cached = ( Map ) _controlFieldCache.get( className );

        if ( cached != null )
            return cached;

        HashMap/*< Field, PropertyMap >*/ ret = new HashMap/*< Field, PropertyMap >*/();

        // Note that the annnotation reader doesn't change per-class.  Inherited annotated elements are all read.
        AnnotationReader annReader = AnnotationReader.getAnnotationReader( controlClientClass, servletContext );

        //
        // Go through this class and all superclasses, looking for control fields.  Make sure that a superclass control
        // field never replaces a subclass control field (this is what the 'fieldNames' HashSet is for).
        //
        HashSet fieldNames = new HashSet();
        do {
            Field[] fields = controlClientClass.getDeclaredFields();

            for ( int i = 0; i < fields.length; i++ )
            {
                Field field = fields[i];
                String fieldName = field.getName();
                int modifiers = field.getModifiers();

                if ( ! fieldNames.contains( fieldName ) && ! Modifier.isStatic( modifiers )
                     && annReader.getAnnotation( field.getName(), CONTROL_ANNOTATION_CLASSNAME ) != null )
                {
                    if ( ! Modifier.isPublic( field.getModifiers() ) ) field.setAccessible( true );
                    ret.put( field, new AnnotatedElementMap( field ) );
                    fieldNames.add( fieldName );
                    if(LOG.isDebugEnabled())
                        LOG.debug("Found control field " + fieldName + " in control client class "
                                  + controlClientClass.getName());
                }
            }

            controlClientClass = controlClientClass.getSuperclass();
        } while ( controlClientClass != null );

        _controlFieldCache.put( className, ret );
        return ret;
    }
}
