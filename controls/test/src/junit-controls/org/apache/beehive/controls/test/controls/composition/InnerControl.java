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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.properties.PropertySet;

/**
 * A control interface designed to test control composition
 */
@ControlInterface
public interface InnerControl
{
	static final String DEFAULT_NAME="Bob";
	static final String DEFAULT_JOB="cleaner";

    @PropertySet
    @Target( {ElementType.TYPE, ElementType.FIELD} )
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Identity {
        public String name() default DEFAULT_NAME;
        //does not have a default value assigned
		public String job();
        public int rank() default 0;
    }

    @EventSet(unicast=true)
    public interface Activity {
        void wakeup();
        int readMessage(String message);
        String report();
    }

    @EventSet(unicast=true)
    public interface Action {
        public Object[] shopping (double credit);
        public void doStuff(String value);
    }

	public void fireAllEvents();
    public void fireEvent(String eventSet, String eventName);

	/*Gets property value from context*/
    public String getNameFromContext();

    /*Gets property value from context*/
    public String getJobFromContext();
}
