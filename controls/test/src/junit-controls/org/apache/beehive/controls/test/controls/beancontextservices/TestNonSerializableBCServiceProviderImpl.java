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

import java.beans.beancontext.BeanContextServiceProvider;
import java.beans.beancontext.BeanContextServices;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Service provider for TestService, should be registered with TestService as the serviceClass.
 */
public class TestNonSerializableBCServiceProviderImpl implements BeanContextServiceProvider {

    private LinkedList<String> _actions;
    private int _refCount;
    private List<String> _serviceSelectors;

    public TestNonSerializableBCServiceProviderImpl() {
        _actions = new LinkedList<String>();
        _refCount = 0;
        _serviceSelectors = new ArrayList<String>();
        _serviceSelectors.add("ONE");
        _serviceSelectors.add("TWO");
        _serviceSelectors.add("THREE");
    }

    public Object getService(BeanContextServices bcs, Object requestor, Class serviceClass, Object serviceSelector) {
        _actions.add("GET");
        _refCount++;
        return new TestServiceImpl();
    }

    public void releaseService(BeanContextServices bcs, Object requestor, Object service) {
        _actions.add("RELEASE");
        _refCount--;
        assert _refCount > 0;
    }

    public Iterator getCurrentServiceSelectors(BeanContextServices bcs, Class serviceClass) {
        return _serviceSelectors.iterator();
    }

    public String[] getRecordedActions() {
        String[] s = new String[1];
        return _actions.toArray(s);
    }

    public int getRefCount() {
        return _refCount;
    }

    public void resetState() {
        _actions = new LinkedList<String>();
        _refCount = 0;
    }
}
