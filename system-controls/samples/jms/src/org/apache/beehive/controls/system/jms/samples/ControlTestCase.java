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
package org.apache.beehive.controls.system.jms.samples;


import java.beans.Beans;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.context.ControlContainerContext;


/**
 * An test case that handles establishing a bean context
 * if there isn't one and providing methods instantiating controls by name
 * via proper channels (@see Beans.instantiate) and adding/removing
 * them.
 */
public abstract class ControlTestCase extends TestCase
{
    /* Public Constructor(s) */
    /**
     * Contstruct a test-case with the given name.
     * @param name the name of the test case.
     */
    public ControlTestCase(String name) throws Exception
    {
        super(name);
    }
    /* Public Method(s) */
    /**
     * @see TestCase#setUp
     */
    public void setUp() throws Exception
    {
        _embeddedContext = ControlThreadContext.getContext();
        if(_embeddedContext == null)
        {
            _embeddedContext = _cntxt = new DefaultContainerContext();
	        _cntxt.beginContext();
        }
        initializeAsClient(this);
    }
    /**
     * @see TestCase#tearDown
     */
    public void tearDown() throws Exception
    {
        if(_cntxt != null)
        {
            _cntxt.endContext();
        }
    }
    /* Protected Method(s) */
    /**
     * Instantiate the control with the given name and add
     * it to the current bean-context.
     * @param name the fully-qualified name of the control.
     */
    protected ControlBean instantiateControl(String name) throws Exception
    {
        ControlBean bean = (ControlBean)Beans.instantiate(Thread.currentThread().getContextClassLoader(),
                name,_embeddedContext);
        initializeAsClient(bean);
        return bean;
    }
    /**
     * Remove the given control from the current bean-context.
     * @param bean
     */
    protected void freeControl(ControlBean bean)
    {
        if(bean != null)
        {
            _embeddedContext.remove(bean);
        }
    }
    /**
     * Initialize this junit as a control client.
     */
    protected void initializeAsClient(Object obj)
    {
        if(_cntxt == null)
        {
            return;
        }
        Class cls = obj.getClass();
        while(cls != null && cls != ControlTestCase.class && cls != Object.class)
        {
	        String init = cls.getName()+CLASS_ClientInitializer;
	        try
	        {
	            Class clazz = Thread.currentThread().getContextClassLoader().loadClass(init);
	            Object initializer = cls.newInstance();
	            Method m = clazz.getMethod(METHOD_Initialize,new Class[] {ControlBeanContext.class,cls });
	            m.invoke(initializer,new Object[] { _cntxt ,this } );
	            
	        }
	        catch(Exception e)
	        {
	            
	        }
	        cls = cls.getSuperclass();
        }
    }
    /* Private Constant(s) */
    final private static String CLASS_ClientInitializer 	= "ClientInitializer";
    final private static String METHOD_Initialize 			= "initialize";
    
    /* Private Field(s) */
    /* The current context. */
    private ControlContainerContext _embeddedContext;
    /* The default context if one didn't exist */
    private ControlContainerContext _cntxt;
}
