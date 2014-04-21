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
package org.apache.beehive.controls.system.ejb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.beehive.controls.api.assembly.ControlAssembler;
import org.apache.beehive.controls.api.assembly.ControlAssemblyContext;
import org.apache.beehive.controls.api.assembly.ControlAssemblyException;
import org.apache.beehive.controls.system.ejb.internal.EJBJarDescriptorHandler;
import org.apache.beehive.controls.system.ejb.internal.WebDescriptorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.xml.sax.SAXException;

/**
 * The EJBControl needs to inject EJB reference entries into the
 * DD of its containing module for cases where ejb-link is used.
 */
public class EJBControlAssembler implements ControlAssembler
{
    public void assemble(ControlAssemblyContext cac)
        throws ControlAssemblyException {
        cac.getMessager().printNotice("EJBControlAssembler.assemble() called");

        Class controlInterface = cac.getControlType();
        EJBInfo ei = new EJBInfo( controlInterface );

        EJBControl.EJBHome ea = cac.getControlAnnotation(EJBControl.EJBHome.class);
        if ( ea == null ) {
            cac.getMessager().printError("Missing EJBHome annotation on control?!");
            return;
        }

        String ejbLinkValue = ea.ejbLink();
        if ( ejbLinkValue == null || ejbLinkValue.length() == 0 )
            // Not using ejb-link, so no ejb-ref injection needed
            return;

        if (cac instanceof ControlAssemblyContext.EJBModule)
            // insert any required <ejb-ref> entries into the deployment descriptor
        	updateEJBJar((ControlAssemblyContext.EJBModule)cac, ei, ejbLinkValue );
        else if (cac instanceof ControlAssemblyContext.WebAppModule)
        	updateWebApp((ControlAssemblyContext.WebAppModule)cac, ei, ejbLinkValue );
        else cac.getMessager().printNotice("EJBControlAssembler - no work to do, assembly context is not EJB.");
    }

    protected void updateEJBJar(ControlAssemblyContext.EJBModule ejbAssemblyContext,
                                EJBInfo ejbInfo,
                                String ejbLinkValue)
        throws ControlAssemblyException {
        ControlAssemblyContext cac = (ControlAssemblyContext)ejbAssemblyContext;

        cac.getMessager().printNotice("EJBControlAssembler.updateEJBJar() called");
        cac.getMessager().printNotice(" ejbInfo=" + ejbInfo );
        cac.getMessager().printNotice(" ejbLinkValue=" + ejbLinkValue );

        File ejbJarFile = ejbAssemblyContext.getEjbJarXml();
        FileInputStream ejbJarStream;
        try {
            ejbJarStream = new FileInputStream( ejbJarFile );
        }
        catch (FileNotFoundException fnfe) {
            String msg = "EJBControlAssembler aborted: " +
                "caught FileNotFoundException attempting to read file " +
                ejbJarFile.getAbsolutePath() +
                ". Message: " +
                fnfe.getMessage();
            cac.getMessager().printWarning(msg);
            return;
        }

        try
        {
            // get the existing <ejb-jar> XBean from the stream
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document ejbDoc = db.parse(ejbJarStream);

            ejbJarStream.close();
            ejbJarStream = null;

            EJBJarDescriptorHandler ejbHandler = EJBJarDescriptorHandler.getInstance();
            ejbHandler.assemble(ejbDoc, ejbInfo, ejbLinkValue);

            // overwrite existing ejb-jar.xml file with new document
            writeXML(cac, ejbDoc, ejbJarFile);
        }
        catch(IOException ioe) {
            String msg = "EJBControlAssembler: caught IOException " +
                "attempting to write to file " +
                ejbJarFile.getAbsolutePath() +
                ". Message: " +
                ioe.getMessage();
            cac.getMessager().printError(msg);
        }
        catch (ParserConfigurationException e) {
            String msg = "EJBControlAssembler: caught ParserConfigurationException " +
                "attempting to read to file " +
                ejbJarFile.getAbsolutePath() +
                ". Message: " +
                e.getMessage();
            cac.getMessager().printError(msg);
        }
        catch (SAXException e) {
            String msg = "EJBControlAssembler: caught SAXException " +
                "attempting to read to file " +
                ejbJarFile.getAbsolutePath() +
                ". Message: " +
                e.getMessage();
            cac.getMessager().printError(msg);
        }
        finally {
            try {
                if (ejbJarStream != null)
                    ejbJarStream.close();
            }
            catch(IOException e) { /* ignore */ }
        }
    }

    protected void updateWebApp(ControlAssemblyContext.WebAppModule webAssemblyContext,
                                EJBInfo ejbInfo,
                                String ejbLinkValue)
        throws ControlAssemblyException
    {
        ControlAssemblyContext cac = (ControlAssemblyContext)webAssemblyContext;

        System.err.println("EJBControlAssembler.updateWebApp() called");
        System.err.println("ejbInfo =" + ejbInfo);
        System.err.println("ejbLinkValue =" + ejbLinkValue );
        File webXmlFile = webAssemblyContext.getWebXml();
        FileInputStream webXmlStream;
        try {
                webXmlStream = new FileInputStream( webXmlFile );
        }
        catch (FileNotFoundException fnfe) {
            String msg = "EJBControlAssembler: " +
                        "caught FileNotFoundException attempting to read file " +
                        webXmlFile.getAbsolutePath() + ". Message: " +
                        fnfe.getMessage();
            cac.getMessager().printError(msg);
                return;
            }

            try
            {
                // parse the web.xml file
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document webAppDoc = db.parse(webXmlStream);

                webXmlStream.close();
                webXmlStream = null;

                WebDescriptorHandler webHandler = WebDescriptorHandler.getInstance();
                webHandler.assemble(webAppDoc, ejbInfo, ejbLinkValue);

                // overwrite existing web.xml file with new document
                writeXML(cac, webAppDoc, webXmlFile);
            }
            catch(IOException ioe) {
                String msg = "EJBControlAssembler: caught IOException " +
                    "attempting to write to file " +
                    webXmlFile.getAbsolutePath() +
                    ". Message: " +
                    ioe.getMessage();
                cac.getMessager().printError(msg);
            }
            catch (ParserConfigurationException e) {
                String msg = "EJBControlAssembler: caught ParserConfigurationException " +
                    "attempting to read to file " +
                    webXmlFile.getAbsolutePath() +
                    ". Message: " +
                    e.getMessage();
                cac.getMessager().printError(msg);
            }
            catch (SAXException e) {
                String msg = "EJBControlAssembler: caught SAXException " +
                    "attempting to read to file " +
                    webXmlFile.getAbsolutePath() +
                    ". Message: " +
                    e.getMessage();
                cac.getMessager().printError(msg);
            }
            finally {
                try {
                    if (webXmlStream != null)
                        webXmlStream.close();
                }
                catch(IOException e) { /* ignore */ }
            }

        }

    private void writeXML(ControlAssemblyContext cac, Document doc, File outputFile) {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            transformerFactory.setAttribute("indent-number", 2);
        } catch (IllegalArgumentException e) {
            // not a fatal error, just means underlying parser implementation does not support indent-number.
            String msg = "EJBControlAssembler: Warning -- Caught IllegalArgumentException " +
                "attempting to set transformer factory attribute: 'indent-number'.  Message: " +
                e.getMessage();

            cac.getMessager().printNotice(msg);
        }

        Transformer transformer;
        FileOutputStream fos = null;
        try {
            transformer= transformerFactory.newTransformer();
            try {
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DocumentType docType = doc.getDoctype();
                if (docType != null) {
                    transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, docType.getPublicId());
                    transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType.getSystemId());
                }
            } catch (IllegalArgumentException e) {
                // just keep going not a fatal error, just means underlying parser implementation
                // does not support one of these options.
                String msg = "EJBControlAssembler: Warning -- Caught IllegalArgumentException " +
                    "attempting to set transformer option.  Message: " +
                    e.getMessage();

                cac.getMessager().printNotice(msg);
            }

            DOMSource source = new DOMSource(doc);
            fos = new FileOutputStream(outputFile);
            StreamResult stream = new StreamResult(fos);
            transformer.transform(source, stream);

        }
        catch (TransformerConfigurationException e) {
            String msg = "EJBControlAssembler: caught TransformerConfigurationException " +
                "attempting to write to file " + outputFile.getAbsolutePath() +
                ". Message: " + e.getMessage();
            cac.getMessager().printError(msg);
        }
        catch (FileNotFoundException e) {
            String msg = "EJBControlAssembler aborted: " +
                "caught FileNotFoundException attempting to write file " +
                outputFile.getAbsolutePath() +
                ". Message: " +
                e.getMessage();
            cac.getMessager().printError(msg);
        }
        catch (TransformerException e) {
            String msg = "EJBControlAssembler: caught TransformerException " +
                "attempting to write to file " +
                outputFile.getAbsolutePath() +
                ". Message: " +
                e.getMessage();
            cac.getMessager().printError(msg);
        }
        finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) { /* ignore */ }
            }
        }
    }
}
