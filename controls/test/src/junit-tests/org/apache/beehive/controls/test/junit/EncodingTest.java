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

package org.apache.beehive.controls.test.junit;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;

import org.apache.beehive.controls.test.controls.property.NestPropsBean;
import org.apache.beehive.controls.test.controls.property.Props;
import org.apache.beehive.controls.test.controls.property.PropsBean;

/**
 * A TestCase that tests encoding/decoding control state to/from XML.
 */
public class EncodingTest extends TestCase {

    /**
     * An Exception listener class that will catch encoding/decoding exceptions, dump the
     * failure stack, and then flag a test failure.
     */
    private class ExceptionDump
        implements java.beans.ExceptionListener {
        public void exceptionThrown(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     *  Helper method that will take a given object, encode it using XMLEncoder, then decode
     *  it using XmLMDecoder.
     */
    private <T> T encodeDecode(T obj)
        throws Exception {
        //
        // Validate decode ability by running through a decoder
        //
        ByteArrayOutputStream baos = null;
        XMLEncoder encoder = null;
        try {
            baos = new ByteArrayOutputStream();
            encoder = new XMLEncoder(baos);
            encoder.setExceptionListener(new org.apache.beehive.controls.test.junit.EncodingTest.ExceptionDump());
            encoder.writeObject(obj);
            baos.flush();
        }
        finally {
            if(encoder != null)
                encoder.close();
            if(baos != null)
                baos.close();
        }

        ByteArrayInputStream bais = null;
        XMLDecoder decoder = null;
        T returnObj = null;
        try {
            bais = new ByteArrayInputStream(baos.toByteArray());
            decoder = new XMLDecoder(bais);
            decoder.setExceptionListener(new org.apache.beehive.controls.test.junit.EncodingTest.ExceptionDump());
            returnObj = (T)decoder.readObject();
        }
        finally {
            if(decoder != null)
                decoder.close();
            if(bais != null)
                bais.close();
        }

        bais.close();
        return returnObj;
    }

    /**
     * Tests encoding a NestPropsBean instance for validating of property state, nesting,
     * and listener registration.
     */
    public void testNestPropsBean() throws Exception
    {
        NestPropsBean propsBean = new NestPropsBean(null, "myID", null);
        propsBean.setSimpleInt(3);
        propsBean.getControlPropertySet(Props.SimpleProps.class);

        PropsBean nestedBean = new NestPropsBean(null, "nested", null);
        propsBean.getControlBeanContext().add(nestedBean);
        nestedBean.setSimpleInt(4);

        // Run the bean through the encoder/decoder, then test equivalence
        NestPropsBean newPropsBean = encodeDecode(propsBean);
        assertEquals(newPropsBean.getSimpleInt(),propsBean.getSimpleInt());

        PropsBean newNestedBean = (NestPropsBean)(newPropsBean.getControlBeanContext().getBean("nested"));
        assertNotNull(newNestedBean);
        assertEquals(newNestedBean.getSimpleInt(), nestedBean.getSimpleInt());
    }
}
