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
package org.apache.beehive.controls.system.webservice.units.rpcenc.schematypes;

import java.awt.Image;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Image attachement tests for rpc/enc services.
 */
public class ImageRpcEncTest
    extends ControlTestCase {

    @Control
    public schematypestest.ImagesRpcEncService client;

// TODO: broken suspect bug in WSM needs further investigation
//    public void testJpgImage() throws Exception {
//        Image img = client.getJpgImage();
//        assertNotNull(img);
//        assertEquals(125, img.getHeight(null));
//        assertEquals(125, img.getWidth(null));
//    }

// TODO: broken suspect bug in WSM needs further investigation
//    public void testGifImage() throws Exception {
//        Image img = client.getGifImage();
//        assertNotNull(img);
//        assertEquals(51, img.getHeight(null));
//        assertEquals(324, img.getWidth(null));
//    }

    /**
     * Test an array of image attachments.
     * @throws Exception
     */
    public void testImageArray() throws Exception {
        Image[] images = client.getImages();
        assertNotNull(images);
        assertEquals(125, images[0].getHeight(null));
        assertEquals(125, images[0].getWidth(null));
        assertEquals(51, images[1].getHeight(null));
        assertEquals(324, images[1].getWidth(null));
    }

    public static Test suite() { return new TestSuite(ImageRpcEncTest.class); }

    public static void main(String[] args) { junit.textui.TestRunner.run(suite()); }
}