/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beehive.controls.system.webservice.units;

import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.test.junit.ControlTestCase;
import org.apache.beehive.controls.test.container.ControlTestContainerContext;

/**
 * Serialization test for the WSC.  Serialize and deserialize a wsc and make sure
 * it still functions.
 */
public class WscSerializationTest
    extends ControlTestCase {

    @Control
    public schematypestest.SoapMarshallingDocLitWrappedService _client;

    public void setUp() {
       // intentially not calling super()
    }

    public void tearDown() {
        // intentially not calling super()
    }

    /**
     * Echo boolean.
     * @throws Exception
     */
    public void testSerialization() throws Exception {

        //
        // start the context
        //
        getControlContainerContextManager().beginContext();

        initializeControls();
        assertNotNull(_client);

        assertEquals(6, _client.echoint(6));

        ControlContainerContext ccc = getControlContainerContext();

        //
        // end the context
        //
        getControlContainerContextManager().endContext();

        //
        // serialize the wsc
        //
        File serFile = File.createTempFile("wsc", "ser");
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
        schematypestest.SoapMarshallingDocLitWrappedService deserializedControl = null;
        for (Object c : ctrls) {
            if (c instanceof schematypestest.SoapMarshallingDocLitWrappedService && !c.equals(_client)) {
                deserializedControl = (schematypestest.SoapMarshallingDocLitWrappedService)c;
                break;
            }
        }

        //
        // start the context
        //
        getControlContainerContextManager().beginContext();

        assertNotNull(deserializedControl);
        assertFalse(deserializedControl == _client);
        assertEquals(7, _client.echoint(7));

        getControlContainerContextManager().endContext();
    }

    public static Test suite() { return new TestSuite(WscSerializationTest.class); }

    public static void main(String[] args) { junit.textui.TestRunner.run(suite()); }
}