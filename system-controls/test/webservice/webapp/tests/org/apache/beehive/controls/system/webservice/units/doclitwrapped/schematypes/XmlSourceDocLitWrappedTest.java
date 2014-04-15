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
package org.apache.beehive.controls.system.webservice.units.doclitwrapped.schematypes;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

/**
 * Tests for Xml Source attachments for doc/lit/wrapped services.
 */
public class XmlSourceDocLitWrappedTest
        extends ControlTestCase {

    @Control
    public schematypestest.XmlSourceDocLitWrappedService client;

    /**
     * Test echoing a xml Source.
     *
     * @throws Exception
     */
    public void testEchoXmlSource() throws Exception {

        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<xmltag>Hello World</xmltag>";

        Source s = new StreamSource(new ByteArrayInputStream(data.getBytes("utf-8")));

        Source echodSource = client.echoXml(s);
        assertNotNull(echodSource);

        Transformer t = TransformerFactory.newInstance().newTransformer();
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        t.transform(echodSource, result);

        assertEquals(data, sw.toString());
    }

    /**
     * Test echoing an array of xml sources.
     * @throws Exception
     */
    public void testEchoXmlSourceArray() throws Exception {
        String data0 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<xmltag>page1</xmltag>";
        String data1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<xmltag>page2</xmltag>";

        StreamSource[] sources = new StreamSource[2];
        sources[0] = new StreamSource(new ByteArrayInputStream(data0.getBytes("utf-8")));
        sources[1] = new StreamSource(new ByteArrayInputStream(data1.getBytes("utf-8")));

        Source[] echodSourceArray = client.echoXmlArray(sources);
        assertNotNull(echodSourceArray);

        Transformer t = TransformerFactory.newInstance().newTransformer();
        StringWriter sw0 = new StringWriter();
        StreamResult result0 = new StreamResult(sw0);
        t.transform(echodSourceArray[0], result0);

        StringWriter sw1 = new StringWriter();
        StreamResult result1 = new StreamResult(sw1);
        t.transform(echodSourceArray[1], result1);

        assertEquals(data0, sw0.toString());
        assertEquals(data1, sw1.toString());
    }
    
    public static Test suite() {
        return new TestSuite(XmlSourceDocLitWrappedTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}