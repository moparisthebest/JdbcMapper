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

package org.apache.beehive.controls.test.controls.generic;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.properties.PropertySet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ControlInterface
public interface ListControl<E, T extends List<? extends E>> {
    //
    // Note: JSR-175 annotation types cannot have formal type parameters
    //
    @PropertySet
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface ListProps {
        /**
         * This property can be used to set the concrete List implementation
         * to use for containing elements
         */
        Class<? extends List> listClass() default ArrayList.class;
    }

    public void add(E val);

    public <X extends E> boolean contains(X val);

    public E get(int index);

    public T copy(Collection<? extends E> src);

    public <X extends E> E getLast(Collection<X> coll);

    //
    // Test an EventSet that uses formal type paramters defined in the outer control interface.
    //
    @EventSet(unicast = true)
    public interface ListEvents<E> {
        public E onAdd(E newElement);
    }
}
