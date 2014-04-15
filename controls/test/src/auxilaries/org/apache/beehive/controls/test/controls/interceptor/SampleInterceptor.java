package org.apache.beehive.controls.test.controls.interceptor;
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

import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.spi.svc.Interceptor;
import org.apache.beehive.controls.spi.svc.InterceptorPivotException;
import java.lang.reflect.Method;

public class SampleInterceptor implements Interceptor
{
    /** Called before a control operation is invoked */
    public void preInvoke( ControlBean cb, Method m, Object [] args)
    	throws InterceptorPivotException
    {
        System.out.println( "SampleInterceptor.preInvoke() called: bean=" + cb.getControlID() + " m=" + m.getName() );
    }

    /** Called after a control operation is invoked */
    public void postInvoke( ControlBean cb, Method m, Object [] args, Object retval, Throwable t )
    {
        System.out.println( "SampleInterceptor.postInvoke() called: bean=" + cb.getControlID() + " m=" + m.getName() );
    }

    /** Called before a control event is fired (through a client proxy) */
    public void preEvent( ControlBean cb, Class eventSet, Method m, Object [] args )
    	throws InterceptorPivotException
    {
        System.out.println( "SampleInterceptor.preEvent() called: bean=" + cb.getControlID() + " es=" + eventSet.getName() + " m=" + m.getName() );
    }

    /** Called after a control event is fired (through a client proxy) */
    public void postEvent( ControlBean cb, Class eventSet, Method m, Object [] args, Object retval, Throwable t )
    {
        System.out.println( "SampleInterceptor.postEvent() called: bean=" + cb.getControlID() + " es=" + eventSet.getName() + " m=" + m.getName() );
    }
}
