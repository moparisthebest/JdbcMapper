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

import javax.activation.DataHandler;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Test DataHandler attachment support for doc/lit/wrapped services.
 */
public class DataHandlerDocLitWrappedTest
        extends ControlTestCase {

    @Control
    public schematypestest.DataHandlerDocLitWrappedService client;

    /**
     * Test for a datahandler which contains plain text.
     * @throws Exception
     */
    public void testPlainText() throws Exception {
        DataHandler dataHandler = client.getPlainText();
        assertNotNull(dataHandler);
        assertEquals("text/plain", dataHandler.getContentType());

        String content = (String) dataHandler.getContent();
        assertEquals("Some plain text.", content);
    }

    /**
     * Test for a datahandler which contains plain text.
     * @throws Exception
     */
    public void testEchoPlainText() throws Exception {
        DataHandler orig = new DataHandler("Some plain text.", "text/plain");
        DataHandler dataHandler = client.echoPlainText(orig);
        assertNotNull(dataHandler);
        assertEquals("text/plain", dataHandler.getContentType());

        String content = (String) dataHandler.getContent();
        assertEquals("Some plain text.", content);
    }

    /**
     * Test for a datahandler which contains xml text.
     * @throws Exception
     */
    public void testXmlText() throws Exception {
        DataHandler dataHandler = client.getXmlText();
        assertNotNull(dataHandler);
        assertEquals("text/xml", dataHandler.getContentType());

        String content = (String) dataHandler.getContent();
        assertEquals("<user><name>Fred</name></user>", content);
    }

    /**
     * Test for a datahandler which contains an jpeg image.
     * @throws Exception
     */
    public void testJpgImage() throws Exception {
        DataHandler dataHandler = client.getJpgImage();
        assertNotNull(dataHandler);
        assertEquals("image/jpeg", dataHandler.getContentType());

        InputStream is = (InputStream) dataHandler.getContent();

        byte[] buffer = new byte[1024];
        byte[] content;
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        int count;
        while ((count = is.read(buffer, 0, buffer.length)) > 0) {
            os.write(buffer, 0, count);
        }
        content = os.toByteArray();
        is.close();
        os.close();

        MediaTracker tracker = new MediaTracker(new Component() {
        });
        Image img = Toolkit.getDefaultToolkit().createImage(content);
        tracker.addImage(img, 0);
        try {
            tracker.waitForAll();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull(img);
        assertEquals(125, img.getHeight(null));
        assertEquals(125, img.getWidth(null));
    }

    /**
     * Test for a datahandler which contains a GIF image.
     * @throws Exception
     */
    public void testGifImage() throws Exception {
        DataHandler dataHandler = client.getGifImage();
        assertNotNull(dataHandler);
        assertEquals("image/gif", dataHandler.getContentType());

        InputStream is = (InputStream) dataHandler.getContent();

        byte[] buffer = new byte[1024];
        byte[] content;
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        int count;
        while ((count = is.read(buffer, 0, buffer.length)) > 0) {
            os.write(buffer, 0, count);
        }
        content = os.toByteArray();
        is.close();
        os.close();

        MediaTracker tracker = new MediaTracker(new Component() {
        });
        Image img = Toolkit.getDefaultToolkit().createImage(content);
        tracker.addImage(img, 0);
        try {
            tracker.waitForAll();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull(img);
        assertEquals(51, img.getHeight(null));
        assertEquals(324, img.getWidth(null));
    }

    public static Test suite() {
        return new TestSuite(DataHandlerDocLitWrappedTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}