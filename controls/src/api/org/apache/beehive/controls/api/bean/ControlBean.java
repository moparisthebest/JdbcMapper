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
package org.apache.beehive.controls.api.bean;

import java.beans.beancontext.BeanContext;
import java.beans.beancontext.BeanContextProxy;

import org.apache.beehive.controls.api.context.ControlBeanContext;

/**
 * The ControlBean interface defines a base set of methods that are implemented by all
 * <code>JavaBeans</code> that host Java Controls.
 * <p>
 * A ControlBean will implement the <code>java.beans.beancontext.BeanContextProxy</code>
 * interface to provide a way to get the <code>BeanContext</code> directly associated
 * with the Java Control.   The <code>getBeanContext()</code> API on the interface will
 * return the parent (containing) context.
 *
 * @see java.beans.beancontext.BeanContextProxy
 */
public interface ControlBean extends BeanContextProxy, java.io.Serializable
{
    /**
     * The IDSeparator character is used to separated individual control IDs in nesting
     * scenarios whether the identifier is actually a composite path that represents
     * a nesting relationship.
     */
    public static final char IDSeparator = '/';

    /**
     * Returns the <code>java.beans.beancontext.BeanContext</code> that provides the parent
     * context for the Java Control.
     * @return the containing <code>BeanContext</code> for the Java ControlBean.
     *
     * @see java.beans.beancontext.BeanContext
     */
    BeanContext getBeanContext();

    /**
     * Returns the <code>org.apache.beehive.controls.api.context.ControlBeanContext</code> instance
     * that provides the local context for this control bean. <b>This is not the parent
     * context for the control.</b>  It is the context that would be the parent context for
     * any nested controls hosted by this control.
     */
    ControlBeanContext getControlBeanContext();

    /**
     * Returns the unique control ID associated with the Java ControlBean.  This control ID
     * is guaranteed to be unique within the containing <code>BeanContext</code>
     * @return the control ID
     */
    String getControlID();

    /**
     * Returns the Java Control public interface for the ControlBean.  This interface defines
     * the operations and events exposed by the Java Control to its clients.
     * @return the control public interface
     */
    Class getControlInterface();
}
