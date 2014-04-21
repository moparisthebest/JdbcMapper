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
package org.apache.beehive.controls.spi.svc;

import java.lang.reflect.Method;

import org.apache.beehive.controls.api.bean.ControlBean;

/**
 * The controls implementation architecture has a interceptor model for
 * adding annotation-based features.  This model provides the ability to
 * associate a JavaBeans service interface with an annotation to define
 * its runtime feature behaviour.  Such interfaces must extend this
 * Interceptor interface, which defines the contract that the controls runtime
 * has with interceptors.
 *
 * The controls runtime will automatically instantiate and execute
 * implementations of interceptors at the appropriate execution points
 * (pre/post invocation of a control operation, etc).
 *
 * The control runtime will continue the normal flow of control (ie, subsequent
 * interceptors and operation/event execution) unless an interceptor throws a
 * {@link InterceptorPivotException}.  When this type of execption is encountered,
 * the runtime will "pivot" out.
 */
public interface Interceptor
{
    /** Called before a control operation is invoked */
    public void preInvoke( ControlBean cb, Method m, Object [] args)
    	throws InterceptorPivotException;

    /** Called after a control operation is invoked */
    public void postInvoke( ControlBean cb, Method m, Object [] args, Object retval, Throwable t );

    /** Called before a control event is fired (through a client proxy) */
    public void preEvent( ControlBean cb, Class eventSet, Method m, Object [] args )
    	throws InterceptorPivotException;

    /** Called after a control event is fired (through a client proxy) */
    public void postEvent( ControlBean cb, Class eventSet, Method m, Object [] args, Object retval, Throwable t );
}
