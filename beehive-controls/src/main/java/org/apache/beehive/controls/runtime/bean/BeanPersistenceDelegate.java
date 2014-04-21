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
package org.apache.beehive.controls.runtime.bean;

import java.beans.BeanInfo;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.EventSetDescriptor;
import java.beans.Expression;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PersistenceDelegate;
import java.beans.PropertyDescriptor;
import java.beans.Statement;
import java.beans.XMLEncoder;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.properties.AnnotatedElementMap;
import org.apache.beehive.controls.api.properties.BeanPropertyMap;
import org.apache.beehive.controls.api.properties.PropertyKey;
import org.apache.beehive.controls.api.properties.PropertyMap;

/**
 * The BeanPersistenceDelegate class supports the XML persistence of Control JavaBeans by
 * implementing the <code>java.beans.PersistenceDelegate</b> API, and overriding the default
 * persistence algorithm based upon the runtime structure for Controls.  It selectively registers
 * other PersistenceDelegate instances for other nested entities, as required, to ensure that
 * runtime-defined state and object relationships are properly maintained.
 * <p>
 * For the BeanInfo of all generated ControlJavaBeans, a BeanPersistenceDelegate instance will
 * be registered as the "persistenceDelegate" attribute in the BeanDescriptor.  The standard
 * <code>java.beans.Encoder</code> persistence delegate lookup mechanism recognizes this attribute
 * and will use the instance to persist an ControlBeans written to the encoding stream.
 * <p>
 * The BeanPersistence class implements optimized property persistence based upon the
 * fact that the ControlBean already has a map containing all non-default property state.  Rather
 * than using the standard (and slower) algorithm of comparing the encoding instance against a
 * 'clean' instance, the delegate can simply retrieve the map and persist the values contained
 * within it.
 *
 * @see java.beans.XMLEncoder
 * @see java.beans.PersistenceDelegate 
 */
public class BeanPersistenceDelegate extends DefaultPersistenceDelegate
{
    /**
     * The FieldPersistencersistence is an XMLEncoder PersistenceDelegate for the
     * <code>java.lang.reflect.Field</code> claass.  It is similar to the one that comes
     * bundled with the JDK with one key exception:  it works for non-public fields as
     * well.
     */
    class FieldPersistenceDelegate extends PersistenceDelegate 
    {
        protected Expression instantiate(Object oldInstance, Encoder out) 
        {
            Field f = (Field)oldInstance;
            return new Expression(oldInstance, f.getDeclaringClass(), "getDeclaredField",
                                    new Object[]{f.getName()});
        }
    }

    /**
     * PersistenceDelegate.instantiate()
     */
    protected Expression instantiate(Object oldInstance, Encoder out)
    {
        XMLEncoder xmlOut = (XMLEncoder)out;
        ControlBean control = (ControlBean)oldInstance;

        //
        // If processing a nested control, then use the parent bean's context as the
        // constructor context
        //
        ControlBeanContext cbc = null;
        if (xmlOut.getOwner() != null)
            cbc = ((ControlBean)xmlOut.getOwner()).getControlBeanContext();

        //
        // See if the ControlBean has any associated PropertyMap in its delegation chain
        // that was derived from an AnnotatedElement so this relationship (and any associated 
        // external config delegates) will be restored as part of the decoding process.
        //
        // BUGBUG: What about a user-created PropertyMap that was passed into the constructor?
        //
        AnnotatedElementMap aem = null;
        PropertyMap pMap = control.getPropertyMap();
        while (pMap != null)
        {
            if (pMap instanceof AnnotatedElementMap)
            {
                aem = (AnnotatedElementMap)pMap;

                //
                // Ignore a class-valued AnnotationElementMap.. this just refers to the
                // Control type, and will be automatically reassociated at construction
                // time
                //
                if (aem.getAnnotatedElement() instanceof Class)
                    aem = null;

                xmlOut.setPersistenceDelegate(AnnotatedElementMap.class,
                                              new AnnotatedElementMapPersistenceDelegate());

                break;
            }

            pMap = pMap.getDelegateMap();
        }


        //
        // Create a constructor that that uses the following form:
        //      new <BeanClass>(ControlBeanContext cbc, String id, PropertyMap map)
        // The context is set to null, so the current active container context will be
        // used, the id will be the ID of the original control and the map will be
        // any AnnotatedElementMap that was passed into the original constructor.
        //
        return new Expression(control, control.getClass(), "new", 
                              new Object [] {cbc, control.getLocalID(), aem});
    }

    /**
     * PersistenceDelegate.initialize()
     */
    protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out)
    {
        //
        // Get the bean and associated beanInfo for the source instance
        //
        ControlBean control = (ControlBean)oldInstance;
        BeanInfo beanInfo;
        try
        {
            beanInfo = Introspector.getBeanInfo(control.getClass());
        }
        catch (IntrospectionException ie)
        {
            throw new ControlException("Unable to locate BeanInfo", ie);
        }

        //
        // Cast the encoding stream to an XMLEncoder (only encoding supported) and then set
        // the stream owner to the bean being persisted
        //
        XMLEncoder xmlOut = (XMLEncoder)out;
        Object owner = xmlOut.getOwner();
        xmlOut.setOwner(control);
        try
        {

            //
            // The default implementation of property persistence will use BeanInfo to 
            // incrementally compare oldInstance property values to newInstance property values.   
            // Because the bean instance PropertyMap holds only the values that have been 
            // modified, this process can be optimized by directly writing out only the properties 
            // found in the map.
            //
            BeanPropertyMap beanMap = control.getPropertyMap();
            PropertyDescriptor [] propDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyKey pk : beanMap.getPropertyKeys())
            {
                //
                // Locate the PropertyDescriptor for the modified property, and use it to write
                // the property value to the encoder stream
                //
                String propName = pk.getPropertyName();
                boolean found = false;
                for (int i = 0; i < propDescriptors.length; i++)
                {
                    if (propName.equals(propDescriptors[i].getName()))
                    {
                        found = true;

                        // Only write the property if it is not flagged as transient
                        Object transientVal = propDescriptors[i].getValue("transient");
                        if (transientVal == null || transientVal.equals(Boolean.FALSE))
                        {
                            xmlOut.writeStatement(
                                new Statement(oldInstance, 
                                      propDescriptors[i].getWriteMethod().getName(),
                                      new Object [] {beanMap.getProperty(pk)}));
                        }
                    }
                }
                if (found == false)
                {
                    throw new ControlException("Unknown property in bean PropertyMap: " + pk);
                }
            }

            //
            // Get the bean context associated with the bean, and persist any nested controls
            //
            ControlBeanContext cbc = control.getControlBeanContext();
            if (cbc.size() != 0)
            {
                xmlOut.setPersistenceDelegate(ControlBeanContext.class, 
                                              new ContextPersistenceDelegate());

                Iterator nestedIter = cbc.iterator();
                while (nestedIter.hasNext())
                {
                    Object bean = nestedIter.next();
                    if (bean instanceof ControlBean)
                    {
                        xmlOut.writeStatement(
                            new Statement(cbc, "add", new Object [] { bean } ));
                    }
                }
            }

            //
            // Restore any listeners associated with the control
            //
            EventSetDescriptor [] eventSetDescriptors = beanInfo.getEventSetDescriptors();
            for (int i = 0; i < eventSetDescriptors.length; i++)
            {
                EventSetDescriptor esd = eventSetDescriptors[i];
                Method listenersMethod = esd.getGetListenerMethod();
                String addListenerName = esd.getAddListenerMethod().getName();
                if (listenersMethod != null)
                {
                    //
                    // Get the list of listeners, and then add statements to incrementally
                    // add them in the same order
                    //
                    try
                    {
                        Object [] lstnrs = (Object [])listenersMethod.invoke(control, 
                                                                             new Object []{});
                        for (int j = 0; j < lstnrs.length; j++)
                        {
                            //
                            // If this is a generated EventAdaptor class, then set the delegate
                            // explicitly
                            //
                            if (lstnrs[j] instanceof EventAdaptor)
                                xmlOut.setPersistenceDelegate(lstnrs[j].getClass(), 
                                                              new AdaptorPersistenceDelegate());
                            xmlOut.writeStatement(
                                new Statement(control, addListenerName, new Object [] {lstnrs[j]}));
                        }
                    }
                    catch (Exception iae)
                    {
                        throw new ControlException("Unable to initialize listeners", iae);
                    } 
                }
            }

            //
            // See if the control holds an implementation instance, if so, we need to include
            // it (and any nested controls or state) in the encoding stream
            //
            Object impl = control.getImplementation();
            if (impl != null)
            {

                //
                // Set the persistence delegate for the impl class to the Impl delegate,
                // set the current stream owner to the bean, and then write the implementation
                //
                Class implClass = impl.getClass(); 
                if (xmlOut.getPersistenceDelegate(implClass) instanceof DefaultPersistenceDelegate) 
                    xmlOut.setPersistenceDelegate(implClass, new ImplPersistenceDelegate());

                //
                // HACK: This bit of hackery pushes the impl into the persistence stream
                // w/out actually requiring it be used as an argument elsewhere, since there
                // is no public API on the bean that takes an impl instance as an argument.
                //
                xmlOut.writeStatement(
                    new Statement(impl, "toString", null));
            }
        }
        finally
        {
            // Restore the previous encoding stream owner
            xmlOut.setOwner(owner);
        }
    }

    /**
     * PersistenceDelegate.writeObject()
     */
    public void writeObject(Object oldInstance, Encoder out)
    {
        // Override the default FieldPersistence algorithm for the encoder, so private fields
        // can also be encoded
        out.setPersistenceDelegate(Field.class, new FieldPersistenceDelegate());
        super.writeObject(oldInstance, out);
    }
}
