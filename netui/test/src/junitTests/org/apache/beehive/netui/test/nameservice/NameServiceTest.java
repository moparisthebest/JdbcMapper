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
package org.apache.beehive.netui.test.nameservice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.beehive.netui.pageflow.requeststate.INameable;
import org.apache.beehive.netui.pageflow.requeststate.NameService;

public class NameServiceTest  extends TestCase
{
    public NameServiceTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(NameServiceTest.class);
    }

    public void testNameServiceNaming()
    {
        NameService ns = NameService.staticInstance();
        assertNotNull("The NameService was returned as null", ns);

        NameableObject no1 = new NameableObject();
        ns.nameObject("NameObject",no1);
        assertNotNull("The Name was returned as null", no1.getObjectName());


        NameableObject no2 = new NameableObject();
        ns.nameObject("NameObject",no2);
        assertNotNull("The Name was returned as null", no2.getObjectName());

        assertFalse("The name of the named objects cannot be the same",
                no1.getObjectName().equals(no2.getObjectName()));
    }

    public void testNameServicePutAndGet()
    {
        NameService ns = NameService.staticInstance();
        assertNotNull("The NameService was returned as null", ns);

        // create a nameable object and the name it.
        NameableObject no1 = new NameableObject();
        ns.nameObject("Nameable",no1);
        ns.put(no1);

        // get the object back and verify that it was the same object
        INameable nameable = ns.get(no1.getObjectName());
        assertEquals(no1,nameable);
    }

    public void testNameServiceErrors()
    {
        NameService ns = NameService.staticInstance();
        assertNotNull("The NameService was returned as null", ns);

        boolean error = true;
        // you cannot add an object without it having a name
        try {
            NameableObject no1 = new NameableObject();
            ns.put(no1);
        }
        catch (IllegalStateException e) {
            error = false;
        }
        assertFalse("Able to add INameable without naming it",error);

        // you cannot put an object without providing the object
        error = true;
        try {
            ns.put(null);
        }
        catch (IllegalStateException e) {
            error = false;
        }
        assertFalse("Didn't fail the put when we passed a null object",error);


        // you cannot get an object without providing a name
        error = true;
        try {
            ns.get(null);
        }
        catch (IllegalStateException e) {
            error = false;
        }
        assertFalse("Didn't fail the get when we passed a null string",error);

    }

    public void testNameServiceSerialization()
    {
        NameService ns = NameService.staticInstance();
        assertNotNull("The NameService was returned as null", ns);

        // create a nameable object and the name it.
        NameableObject no1 = new NameableObject();
        ns.nameObject("Nameable", no1);
        ns.put(no1);

        // get the object back and verify that it was the same object
        INameable nameable = ns.get(no1.getObjectName());
        assertEquals(no1, nameable);

        //System.out.println("Try to serialize NS");
        byte[] ser = serializeIt(ns);
        assertNotNull("The NameService was not serialized correctly", ser);
        //System.out.println("Try to deserialize NS");
        NameService rehydratedNS = null;
        try {
            rehydratedNS = (NameService) deserializeIt(ser);
            assertNotNull("The NameService was not deserialized correctly",
                          rehydratedNS);
        }
        catch (ClassNotFoundException cnfe) {
            fail("Test failed with a ClassNotFoundException: "
                 + cnfe.getMessage());
        }
        INameable nameable2 = rehydratedNS.get(no1.getObjectName());
        assertTrue(nameable2 == null);

        rehydratedNS.put(no1);
        nameable2 = rehydratedNS.get(no1.getObjectName());
        assertEquals(no1, nameable2);
    }

    private static byte[] serializeIt(Object obj)
    {
        byte[] rval = null;
        ObjectOutputStream oos = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            rval = baos.toByteArray();
        }
        catch (IOException e) {
            fail("Test failed to serialize the object: " + e.getMessage());
            //System.out.println(e);
        }
        finally {
            try { oos.close(); } catch (IOException e) { };
        }

        return rval;
    }

    private static Object deserializeIt(byte[] arr)
        throws ClassNotFoundException
    {
        Object rval = null;
        ObjectInputStream is = null;

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(arr);
            is = new ObjectInputStream(bais);
            rval = is.readObject();
        }
        catch (IOException e) {
            fail("Test failed to de-serialize the object: " + e.getMessage());
            //System.out.println(e);
        }
        finally {
            try { is.close(); } catch (IOException e) { };
        }

        return rval;
    }
}
