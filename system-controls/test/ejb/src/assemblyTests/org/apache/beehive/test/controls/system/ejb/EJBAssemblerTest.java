/**
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
package org.apache.beehive.test.controls.system.ejb;

import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;

import junit.framework.TestCase;
import org.apache.beehive.controls.system.ejb.EJBInfo;
import org.apache.beehive.controls.system.ejb.internal.EJBJarDescriptorHandler;
import org.apache.beehive.controls.system.ejb.internal.WebDescriptorHandler;
import org.apache.beehive.test.controls.system.ejb.remote.RemoteSessionEjbControl;
import org.apache.beehive.test.controls.system.ejb.local.LocalSessionEjbControl;
import org.apache.beehive.test.controls.system.ejb.entity.RemoteEntityEjbControl;
import org.apache.beehive.test.controls.system.ejb.entity.LocalEntityEjbControl;
import org.w3c.dom.Document;

/**
 * Test the EJBControl assembler. These tests run the assembler to verify that
 * valid XML is generated but do not do anything with the generated deployment
 * descriptors.
 */
public class EJBAssemblerTest
    extends TestCase {

    private static final String RESOURCE_ROOT = "org/apache/beehive/test/controls/system/ejb/cases";
    private static final int EJB = 0;
    private static final int WEB = 1;

    public void testWebLocal()
        throws Exception {
        assemble(LocalSessionEjbControl.class, "web.xml", "local-web.xml", WEB);
    }

    public void testWebLocalAfterElements()
        throws Exception {
        assemble(LocalSessionEjbControl.class, "web-afterelements.xml", "local-web-afterelements.xml", WEB);
    }

    public void testWebRemote()
        throws Exception {
        assemble(RemoteSessionEjbControl.class, "web.xml", "remote-web.xml", WEB);
    }

    public void testWebRemoteAfterElememnts()
        throws Exception {
        assemble(RemoteSessionEjbControl.class, "web-afterelements.xml", "remote-web-afterelements.xml", WEB);
    }

    public void testSimpleRef()
        throws Exception {
        assemble(RemoteSessionEjbControl.class, "ejb-jar.xml", "remote-ejb-jar.xml", EJB);
    }

    public void testAfterElements()
        throws Exception {
        assemble(RemoteSessionEjbControl.class, "ejb-jar-afterelements.xml", "remote-ejb-jar-afterelements.xml", EJB);
    }

    public void testLocalSimpleRef()
        throws Exception {
        assemble(LocalSessionEjbControl.class, "ejb-jar.xml", "local-ejb-jar.xml", EJB);
    }

    public void testLocalAfterElements()
        throws Exception {
        assemble(LocalSessionEjbControl.class, "ejb-jar-afterelements.xml", "local-ejb-jar-afterelements.xml", EJB);
    }

    public void testEntitySimpleRef() throws Exception {
        assemble(RemoteEntityEjbControl.class, "ejb-jar.xml", "remote-entity-ejb-jar.xml", EJB);
    }

    public void testEntityAfterElements() throws Exception {
        assemble(RemoteEntityEjbControl.class, "ejb-jar.xml", "remote-entity-ejb-jar-afterelements.xml", EJB);
    }

    public void testLocalEntitySimpleRef() throws Exception {
        assemble(LocalEntityEjbControl.class, "ejb-jar.xml", "local-entity-ejb-jar.xml", EJB);
    }

    public void testLocalEntityAfterElements() throws Exception {
        assemble(LocalEntityEjbControl.class, "ejb-jar.xml", "local-entity-ejb-jar-afterelements.xml", EJB);
    }

    private void assemble(Class ejbControl, String fileName, String outputFileName, int type)
        throws Exception {

        InputStream is = null;
        Document doc = null;
        try {
            String resourcePath = RESOURCE_ROOT + "/" + fileName;
            is = this.getClass().getClassLoader().getResourceAsStream(resourcePath);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(is);

            /* create EJBInfo */
            String ejbLinkValue = "mockEjbLink";
            EJBInfo ejbInfo = new EJBInfo(ejbControl);

            /* edit document */
            if(type == EJB) {
                EJBJarDescriptorHandler ddHandler = EJBJarDescriptorHandler.getInstance();
                ddHandler.assemble(doc, ejbInfo, ejbLinkValue);
            }
            else {
                WebDescriptorHandler ddHandler = WebDescriptorHandler.getInstance();
                ddHandler.assemble(doc, ejbInfo, ejbLinkValue);
            }
        }
        finally {
            if(is != null) try{is.close();}catch(IOException e) {}
        }

        /* write document */
        writeXML(doc, new File(System.getProperty("build.dir") + File.separator + outputFileName));
    }

    private void writeXML(Document doc, File outputFile)
        throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        try {
            transformerFactory.setAttribute("indent-number", 2);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        Transformer transformer;
        FileOutputStream fos = null;
        try {
            transformer= transformerFactory.newTransformer();

            try {
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                if(doc.getDoctype() != null) {
                    transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doc.getDoctype().getPublicId());
                    transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doc.getDoctype().getSystemId());
                }
            } catch (IllegalArgumentException e) {
                throw e;
            }

            DOMSource source = new DOMSource(doc);
            fos = new FileOutputStream(outputFile);
            StreamResult stream = new StreamResult(fos);
            transformer.transform(source, stream);

        } catch (Exception e) {
            throw e;
        }
        finally {
            if (fos != null) try {fos.close();} catch (IOException ignore) {}
        }
    }

}
