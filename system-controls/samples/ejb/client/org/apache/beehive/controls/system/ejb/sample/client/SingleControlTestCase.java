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
package org.apache.beehive.controls.system.ejb.sample.client;


import java.beans.Beans;

import junit.framework.TestCase;

import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.context.ControlContainerContext;


/**
 * A test case that handles establishing a bean context
 * if there isn't one and instantiating a single control.
 */
public abstract class SingleControlTestCase extends ControlTestCase
{
    /* Public Constructor(s) */
    /**
     * Contstruct a test-case with the given name for a single control.
     * @param name the name of the test case.
     */
    public SingleControlTestCase(String testName,String controlName) throws Exception
    {
        super(testName);
        _controlName = controlName;
    }
    /* Public Method(s) */
    /**
     * @see TestCase#setUp
     */
    public void setUp() throws Exception
    {
        super.setUp();
        _bean = instantiateControl(_controlName);
    }
      
    /**
     * @see TestCase#tearDown
     */
    public void tearDown() throws Exception
    {
        //freeControl(_bean);
        super.tearDown();
    }
    /* Protected Method(s) */
    /**
     * Get the single control bean.
     * @return a control-bean.
     */
    protected ControlBean getControl() throws Exception
    {
        return _bean;
    }
    /* Private Field(s) */
    /** The name of the control */
    private String _controlName;
    /** A cache of the bean. */
    private ControlBean _bean;
}
