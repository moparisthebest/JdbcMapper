/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at
   
       http://www.apache.org/licenses/LICENSE-2.0
   
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
  
   $Header:$
*/
package org.apache.beehive.controls.test.junit;

import java.util.List;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.File;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.test.controls.serialization.ControlSerialization;
import org.apache.beehive.controls.test.controls.serialization.ControlSerializationBean;
import org.apache.beehive.controls.test.container.ControlTestContainerContext;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test serialization of a simple control.
 */
public class SimpleSerializationTest
    extends ControlTestCase {

    @Control
    private ControlSerialization _serializationControl;

    public void setUp() {
        /* intentionally, this does not call super */
    }

    public void tearDown() {
        /* intentionally, this does not call super */
    }

    public void testSimpleSerialization() throws Exception {

        //
        // start the context
        //
        getControlContainerContextManager().beginContext();

        initializeControls();
        assertNotNull(_serializationControl);

        _serializationControl.setControlState(6);
        _serializationControl.clearLifecycleEvents();

        ControlContainerContext ccc = getControlContainerContext();

        //
        // end the context
        //
        getControlContainerContextManager().endContext();

        //
        // serialize the object
        //
        File serFile = File.createTempFile("controls", "ser");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serFile));
        ControlTestContainerContext ctcc = (ControlTestContainerContext)ccc;
        oos.writeObject(ctcc);
        oos.close();

        //
        // deserialize the ctcc contents
        //
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
        ctcc = (ControlTestContainerContext)ois.readObject();
        ois.close();
        serFile.delete();

        //
        // find the deserialized instance in the ctcc
        //
        Object[] ctrls = ctcc.toArray();
        ControlSerialization deserializedControl = null;
        for (Object c : ctrls) {
            if (c instanceof ControlSerializationBean) {
                deserializedControl = (ControlSerialization)c;
                break;
            }
        }

        //
        // start the context
        //
        getControlContainerContextManager().beginContext();

        assertNotNull(deserializedControl);
        assertFalse(deserializedControl == _serializationControl);
        assertEquals(6, deserializedControl.getControlState());
        List<String> events = deserializedControl.getLifecycleEvents();

        // onRelease should have been invoked before the context was first ended
        assertEquals("onRelease", events.get(0));
        assertEquals("onAcquire", events.get(1));

        getControlContainerContextManager().endContext();
    }

    public static Test suite() {
        return new TestSuite(SimpleSerializationTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
