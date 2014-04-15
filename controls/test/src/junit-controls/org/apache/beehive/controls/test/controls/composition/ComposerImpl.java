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

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.test.controls.property.Props;
import org.apache.beehive.controls.test.controls.property.PropsExtension;

import java.lang.annotation.Annotation;
import java.util.LinkedList;

@ControlImplementation
public class ComposerImpl implements Composer, java.io.Serializable {
    static final long serialVersionUID = 1L;

    @Control
    @Props.SimpleProps(simpleString="A field annotation value")
    Props propControl;

    @Control
    @PropsExtension.SimpleProps(simpleString="A different field annotation value")
    private PropsExtension propExtControl;

    @Nested.Index(1)
    private @Control Nested nested1;

    @Control
    @Nested.Index(2)
    /* package */ Nested nested2;

    @Control
    @Nested.Index(3)
    protected Nested nested3;

    @Control
    @Nested.Index(4)
    public Nested nested4;

    private LinkedList<String> _events = new LinkedList<String>();

    /**
     * Provides a simple test API to externally query the PropertySet values on a
     * nested control.
     */
    public Annotation getControlPropertySet(Class propertySet) {
        return propControl.getControlPropertySet(propertySet);
    }

    public Annotation getExtensionControlPropertySet(Class propertySet) {
        return propExtControl.getControlPropertySet(propertySet);
    }

    public void invokeNestedControls() {
        nested1.fireEvent( "Return", "returnVoid" );
        nested1.fireEvent( "Return", "returnInt" );
        nested1.fireEvent( "Return", "returnString" );
    }

    //
    // Define various event handlers for the nested controls
    //
    @EventHandler(field="nested1", eventSet=Nested.Return.class, eventName="returnVoid")
    public void nested1ReturnVoid() {
        _events.add( "nested1ReturnVoid" );
    }

    @EventHandler(field="nested1", eventSet=Nested.Return.class, eventName="returnString")
    public String nested1ReturnString() {
        _events.add( "nested1ReturnString" );
        return "Hello";
    }

    @EventHandler(field="nested1", eventSet=Nested.Return.class, eventName="returnInt")
    public int nested1ReturnInt() {
        _events.add( "nested1ReturnInt" );
        return 21;
    }

    @EventHandler(field="nested2", eventSet=Nested.Args.class, eventName="argsInt")
    public int nested2ArgsInt(int value) {
        _events.add( "nested2ArgsInt" );
        return value;
    }

    @EventHandler(field="nested2", eventSet=Nested.Args.class, eventName="argsString")
    public String nested2ArgsString(String value) {
        _events.add( "nested2ArgsString" );
        return value;
    }

    @EventHandler(field="nested2", eventSet=Nested.Args.class, eventName="argsMultiple")
    public Object [] nested2ArgsMultiple(int val1, String val2) {
        _events.add( "nested2ArgsMultiple" );
        return new Object[] {val1,val2};
    }

    @EventHandler(field="nested3", eventSet=Nested.Except.class, eventName="exceptIO")
    public void nested3ExceptIO()
        throws java.io.IOException {
        _events.add( "nested3ExceptIO" );
        throw new java.io.IOException("Ouch");
    }

    @EventHandler(field="nested3", eventSet=Nested.Except.class, eventName="exceptRuntime")
    public void nested3ExceptRuntime()
        throws RuntimeException {
        _events.add( "nested3ExceptRuntime" );
        throw new RuntimeException("Crash");
    }

    @EventHandler(field="nested3", eventSet=Nested.Except.class, eventName="exceptLocal")
    public void nested3ExceptLocal()
        throws Nested.LocalException {
        _events.add( "nested3ExceptLocal" );
        throw new Nested.LocalException("Bang");
    }

    @EventHandler(field="nested3", eventSet=Nested.Except.class, eventName="exceptMultiple")
    public void nested3ExceptMultiple()
        throws java.io.IOException, RuntimeException {
        _events.add( "nested3ExceptMultiple" );
        throw new java.io.IOException("Play nice!");
    }

    @EventHandler(field="nested4", eventSet=Nested.Return.class, eventName="returnInt")
    public int nested4ReturnInt() {
        _events.add( "nested4ReturnInt" );
        return 99;
    }

    @EventHandler(field="nested4", eventSet=Nested.Args.class, eventName="argsString")
    public String nested4ArgsString(String value) {
        _events.add( "nested4ArgsString" );
        return value;
    }

    @EventHandler(field="nested4", eventSet=Nested.Except.class, eventName="exceptLocal")
    public void nested4ExceptLocal()
        throws Nested.LocalException {
        _events.add( "nested4ExceptLocal" );
        throw new Nested.LocalException("Bang");
    }

    public String[] getEventLog() {
        String[] ret = new String[1];
        return _events.toArray(ret);
    }
}
