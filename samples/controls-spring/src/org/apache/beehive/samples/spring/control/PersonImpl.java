package org.apache.beehive.samples.spring.control;

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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.events.EventHandler;

@ControlImplementation
abstract public class PersonImpl implements Person, java.io.Serializable
{
    @Context ControlBeanContext context;

    @EventHandler(field="context", 
                  eventSet=ControlBeanContext.LifeCycle.class, eventName="onCreate")
    public void onCreate()
    {
        Attributes attributes = context.getControlPropertySet(Person.Attributes.class);
        if (!attributes.gender().equals(Person.MALE) && !attributes.gender().equals(Person.FEMALE))
            throw new RuntimeException("Invalid gender:" + attributes.gender());
    }

    public String getFullName()
    {
        Name name = context.getControlPropertySet(Person.Name.class); 
        return name.firstName() + " " + name.lastName();
    }
}
