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

package org.apache.beehive.controls.test.controls.property;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.packaging.PropertyInfo;
import org.apache.beehive.controls.api.properties.PropertySet;

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A simple control that can be used for property testing of bound and constrained
 * property event behavior.
 */
@ControlInterface
public interface PropEvents {
    //
    // Declare a set of bound properties.  These should deliver PropertyChange events
    // if modified.
    //
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    public @interface BoundProps {
        @PropertyInfo(bound = true)
        public int boundInt() default 0;
    }

    //
    // Declare a set of bound properties.  These should deliver PropertyChange and
    // VetoableChange events if modified.
    //
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    public @interface ConstrainedProps {
        @PropertyInfo(constrained = true)
        public int constrainedInt() default 0;

    }

    //
    // Declared unbound and unconstrained events.  These should deliver no events if
    // modified.
    //
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    public @interface BasicProps {
        public int basicInt() default 0;
    }

    //
    // These EventSets are used as an external test point for events received by the
    // implementation class.  The implementation will simply echo the ControlBeanContext
    // lifecycle events it receives as events on these event sets.  This enables events
    // received by the Impl to be matched against those received by an externally registered
    // listener.  Except in veto scenarios, where someone later on the veto chain may not
    // receive an event, they should be equivalent.
    //
    @EventSet
    public interface ImplPropertyChange extends PropertyChangeListener {
    }

    @EventSet
    public interface ImplVetoableChange extends VetoableChangeListener {
    }
}
