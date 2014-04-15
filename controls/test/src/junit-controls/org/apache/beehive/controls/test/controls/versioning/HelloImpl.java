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

package org.apache.beehive.controls.test.controls.versioning;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Extensible;
import org.apache.beehive.controls.api.versioning.VersionSupported;

import java.lang.reflect.Method;

@ControlImplementation
@VersionSupported(major = 2)
public class HelloImpl implements Hello, Extensible, java.io.Serializable {
    public String _lastVisitor = "<none>";
    int _visitorCount = 0;

    public String hello(String name) {
        _lastVisitor = name;
        _visitorCount++;
        return "Hello, " + name;
    }

    public String lastVisitor() {
        return _lastVisitor;
    }

    public int visitorCount() {
        return _visitorCount;
    }

    /**
     * Implements the Extensible.invoke interface when a JCX-declared method is called
     */
    public Object invoke(Method method, Object [] args) {
        //
        // To Be Implemented
        //
        return null;
    }
} 
