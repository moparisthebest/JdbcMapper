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

package org.apache.beehive.controls.test.controls.composition;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.properties.PropertySet;
import org.apache.beehive.controls.api.events.EventSet;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ControlInterface
public interface Nested {

    public void fireEvent(String eventSet, String eventName);

    @PropertySet
    @Target( {ElementType.TYPE, ElementType.FIELD} )
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Index {
        int value() default -1;
    }

    @EventSet(unicast=true)
    public interface Return {
        void returnVoid();
        String returnString();
        int returnInt();
    }

    @EventSet(unicast=true)
    public interface Args {
        public int argsInt(int value);
        public String argsString(String value);
        public Object [] argsMultiple(int val1, String val2);
    }

    @EventSet
    public interface Except {
        void exceptIO() throws java.io.IOException;
        void exceptRuntime() throws RuntimeException;
        void exceptLocal() throws LocalException;
        void exceptMultiple() throws java.io.IOException, RuntimeException;
    }

    public class LocalException
        extends Exception
    {
        LocalException(String msg) {
            super(msg);
        }

        LocalException(String msg, Throwable t) {
            super(msg, t);
        }
    }
}
