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

package org.apache.beehive.controls.test.controls.inherit;

/**
 * This is the root interface for the inheritance test control hierarchy.  It is <b>not</b> a
 * ControlInterface, and exists to verify that it is possible to subclass a ControlInterface/
 * ControlImpl from an existing interface/impl class pair.
 */
public interface Root {
    public void rootOperation1();

    public void rootOperation2();

    public interface RootEvents {
        public void rootEvent1();

        public void rootEvent2();
    }
}
