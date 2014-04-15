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

package org.apache.beehive.test.controls.system.test.util;

import org.apache.cactus.ServletTestCase;
import org.apache.beehive.controls.test.util.ControlContainerContextManager;
import org.apache.beehive.controls.test.util.ControlContainerContextManagerFactory;
import org.apache.beehive.controls.test.container.ControlTestContainerContext;
import org.apache.beehive.controls.test.ControlTestException;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.bean.ControlBean;

/**
 * Base control test case.
 */
public abstract class CactusControlTestCase
    extends ServletTestCase {

    /* todo: push strings into a .properties file */

    private ControlContainerContextManager _controlContainerContextManager = null;

    public void setUp()
        throws Exception {

        super.setUp();

        beginContext();
        initializeControls();
    }

    public void tearDown()
        throws Exception {

        super.tearDown();

        endContext();
    }

    protected ControlContainerContext initializeControlContainerContext() {
        return new ControlTestContainerContext();
    }

    protected ControlContainerContext getControlContainerContext() {
        return getControlContainerContextManager().getControlContainerContext();
    }

    protected void initializeControls() {
        getControlContainerContextManager().instantiateControls(this);
    }

    protected void beginContext() {
        getControlContainerContextManager().beginContext();
    }

    protected void endContext() {
        getControlContainerContextManager().endContext();
    }

    protected ControlContainerContextManager getControlContainerContextManager() {
        if(_controlContainerContextManager == null) {
            ControlContainerContext ccc = initializeControlContainerContext();

            if(ccc == null)
                throw new ControlTestException("Could not instantiate a ControlContainerContextManager as the control container context was null");

            _controlContainerContextManager = ControlContainerContextManagerFactory.getInstance(ccc);
        }

        return _controlContainerContextManager;
    }

    protected ControlBean instantiateControl(String className) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Object controlBean = java.beans.Beans.instantiate(classLoader, className, ControlThreadContext.getContext());
            assert controlBean instanceof ControlBean;
            return (ControlBean)controlBean;
        }
        catch(Exception e) {
            throw new ControlTestException("Could not instantiate control with class \"" + className +
                "\".  Cause: " + e.getMessage(), e);
        }
    }
}
