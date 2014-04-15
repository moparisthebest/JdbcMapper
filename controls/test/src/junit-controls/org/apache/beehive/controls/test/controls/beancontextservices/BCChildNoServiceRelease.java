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

package org.apache.beehive.controls.test.controls.beancontextservices;

import java.beans.beancontext.BeanContextServiceAvailableEvent;
import java.beans.beancontext.BeanContextServiceRevokedEvent;
import java.beans.beancontext.BeanContextServices;
import java.util.TooManyListenersException;

/**
 * Extension of CBCCS, which listens for a service available event and
 * gets a reference to that service. Must be regestered manually as
 * ServicesSupport listener.
 */
public class BCChildNoServiceRelease extends org.apache.beehive.controls.runtime.webcontext.ControlBeanContextChildSupport {

    private int _refCount = 0;
    private Object _service = null;

    public void serviceAvailable(BeanContextServiceAvailableEvent bcsae) {
        if (TestService.class.equals(bcsae.getServiceClass())) {
            try {
                _service = ((BeanContextServices)getBeanContext()).getService(this, this, TestService.class, null, this);
                _refCount++;
            }
            catch (TooManyListenersException tmle) {
                throw new RuntimeException(tmle);
            }
        }
    }

    public void serviceRevoked(BeanContextServiceRevokedEvent bcsre) {
        if (bcsre.isServiceClass(TestService.class) && bcsre.isCurrentServiceInvalidNow()) {
//             spec: don't to this if invalidate now
//             bcsre.getSourceAsBeanContextServices().releaseService(this, this, _service);
            _refCount--;
        }
    }

    public int getRefCount() {
        return _refCount;
    }
}
