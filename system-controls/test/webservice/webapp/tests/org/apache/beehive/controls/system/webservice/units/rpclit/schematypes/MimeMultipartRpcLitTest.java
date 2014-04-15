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
package org.apache.beehive.controls.system.webservice.units.rpclit.schematypes;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Tests for MimeMultipart attachments for doc/lit/wrapped services.
 */
public class MimeMultipartRpcLitTest
        extends ControlTestCase {

    @Control
    public schematypestest.MimeMultipartRpcLitService client;

    /**
     * Test echoing a multipart mime message of type text/plain.
     *
     * @throws Exception
     */
    public void testEchoPlainText() throws Exception {

        MimeMultipart mmp = new MimeMultipart();
        MimeBodyPart mbp = new MimeBodyPart();

        try {
            mbp.setContent("echo'd plain text", "text/plain");
            mbp.setHeader("Content-Type", "text/plain");
            mmp.addBodyPart(mbp);

            mbp = new MimeBodyPart();
            mbp.setContent("more echo'd plain text", "text/plain");
            mbp.setHeader("Content-Type", "text/plain");
            mmp.addBodyPart(mbp);
        }
        catch (MessagingException e) {
            e.printStackTrace();
            fail("exception");
        }

        MimeMultipart response = client.echoPlainText(mmp);

        assertNotNull(response);

        BodyPart part = mmp.getBodyPart(0);
        String content = (String) part.getContent();
        assertEquals("echo'd plain text", content);

        part = mmp.getBodyPart(1);
        content = (String) part.getContent();
        assertEquals("more echo'd plain text", content);
    }

    /**
     * Test echoing a multipart mime message of type text/xml.
     *
     * @throws Exception
     */
    public void testEchoXmlText() throws Exception {

        MimeMultipart mmp = new MimeMultipart();
        MimeBodyPart mbp = new MimeBodyPart();

        try {
            mbp.setContent("<user><name>MM_Fred</name></user>", "text/xml");
            mbp.setHeader("Content-Type", "text/xml");
            mmp.addBodyPart(mbp);

            mbp = new MimeBodyPart();
            mbp.setContent("<xmltag>Some text</xmltag>", "text/xml");
            mbp.setHeader("Content-Type", "text/xml");
            mmp.addBodyPart(mbp);
        }
        catch (MessagingException e) {
            e.printStackTrace();
            fail("exception");
        }

        MimeMultipart response = client.echoXmlText(mmp);

        assertNotNull(response);

        BodyPart part = mmp.getBodyPart(0);
        String content = (String) part.getContent();
        assertEquals("<user><name>MM_Fred</name></user>", content);

        part = mmp.getBodyPart(1);
        content = (String) part.getContent();
        assertEquals("<xmltag>Some text</xmltag>", content);
    }

    /**
     * Test a mimemultipart response which contains two images, a jpeg and gif.
     *
     * @throws Exception
     */
    public void testImages() throws Exception {

        MimeMultipart response = client.getImages();

        assertNotNull(response);
        BodyPart part = response.getBodyPart(0);
        assertEquals("image/jpeg", part.getContentType());

        InputStream is = (InputStream) part.getContent();
        byte[] buffer = new byte[1024];
        byte[] content = null;
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

        part = response.getBodyPart(1);
        assertEquals("image/gif", part.getContentType());

        is = (InputStream) part.getContent();

        buffer = new byte[1024];
        os = new ByteArrayOutputStream();

        while ((count = is.read(buffer, 0, buffer.length)) > 0) {
            os.write(buffer, 0, count);
        }
        content = os.toByteArray();
        is.close();
        os.close();

        tracker = new MediaTracker(new Component() {
        });
        img = Toolkit.getDefaultToolkit().createImage(content);
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
        return new TestSuite(MimeMultipartRpcLitTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}