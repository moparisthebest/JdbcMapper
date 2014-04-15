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
package org.apache.beehive.netui.test.servlet;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ServletHandler
    implements InvocationHandler {

    private Map _attributes = null;

    public ServletHandler() {
        _attributes = new HashMap();
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        if(method.getName().endsWith("getAttribute")) {
            String key = (String)args[0];
            return _attributes.get(key);
        } else if(method.getName().endsWith("getAttributeNames")) {
            return Collections.enumeration(_attributes.keySet());
        } else if(method.getName().endsWith("setAttribute")) {
            String key = (String)args[0];
            Object value = args[1];
            _attributes.put(key, value);
            return null;
        } else if(method.getName().endsWith("removeAttribute")) {
            String key = (String)args[0];
            _attributes.remove(key);

            return null;
        } else if(method.getName().equals("toString"))
            return "";
        else if(method.getName().equals("isSecure"))
            return Boolean.FALSE;
        else
            throw new UnsupportedOperationException("Can not invoke method (" + method + "); this proxy does not support the method.");
    }


}
