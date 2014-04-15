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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.beehive.controls.api.context.ControlHandle;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.events.EventRef;
import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.api.context.ControlContainerContext;

/**
 * A control-handle used for simple invokation/event support.
 */
public class DefaultControlHandle implements ControlHandle, Serializable
{
    /** Public Constructor(s) */
    /**
     * Default constructor (used by serialization).
     */
    public DefaultControlHandle()
    {

    }

    /**
     * Construct a simple-control handle.
     * 
     * @param context
     *            The control-container-context.
     * @param bean
     *            the control bean.
     */
    public DefaultControlHandle(ControlContainerContext context,
            ControlBean bean)
    {
        _context = context;
        _controlId = bean.getControlID();
        _controlInterface = bean.getControlInterface().getName();
        _controlBean = bean.getClass().getName();
    }

    /** Public Method(s) */
    /**
     * @see org.apache.beehive.controls.api.context.ControlHandle#getControlID()
     */
    public String getControlID()
    {
        return _controlId;
    }

    /**
     * @see org.apache.beehive.controls.api.context.ControlHandle#sendEvent(org.apache.beehive.controls.api.events.EventRef,
     *      java.lang.Object[])
     */
    public Object sendEvent(EventRef event, Object[] args)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        if(_context == null)
        {
            _context = ControlThreadContext.getContext();
        }
        if(_context == null)
        {
            throw new IllegalArgumentException("No container context to send event to");
        }
        try
        {
            //
            // Push the test context, to simulate re-entering the container
            //
            _context.beginContext();

            return _context.dispatchEvent(this, event, args);
        } finally
        {
            _context.endContext();
        }
    }

    private static final long serialVersionUID = 1928410200080675302L;

    /* Private Field(s) */
    /** The control-id of the control. */
    private String _controlId;

    /** The interface class of the control. */
    private String _controlInterface;

    /** The bean class of the control. */
    private String _controlBean;

    /** The control container context */
    private transient ControlContainerContext _context;

}
