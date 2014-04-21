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
package org.apache.beehive.controls.system.ejb.internal;

import java.util.List;

import org.apache.beehive.controls.system.ejb.EJBInfo;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 */
public class WebDescriptorHandler {

    public static WebDescriptorHandler getInstance() {
        return new WebDescriptorHandler();
    }

    private WebDescriptorHandler() {}

    public void assemble(Document document, EJBInfo ejbInfo, String ejbLinkValue) {
        Element webAppType = document.getDocumentElement();

        if (ejbInfo.isLocal())
            insertEJBLocalRefInWebApp(webAppType, ejbInfo, ejbLinkValue, document);
        else
            insertEJBRefInWebApp(webAppType, ejbInfo, ejbLinkValue, document);
    }

    private void insertEJBRefInWebApp(Element webAppType, EJBInfo ei, String ejbLinkValue, Document webAppDoc) {
        List ejbRefArray = DomUtils.getChildElementsByName(webAppType, "ejb-ref");

        String insertedEjbRefName = ei.getRefName();
        Node nextSibling = null;
        for (int j = ejbRefArray.size() - 1; j >= 0; j--) {
            Element ejbRef = (Element) ejbRefArray.get(j);
            String ejbRefName = DomUtils.getChildElementText(ejbRef, "ejb-ref-name");
            if (insertedEjbRefName.equals(ejbRefName)) {
                nextSibling = ejbRef.getNextSibling();
                webAppType.removeChild(ejbRef);
                break;
            }
        }

        // insert a new <ejb-ref> entry and fill in the values
        Element insertedEjbRef = webAppDoc.createElement("ejb-ref");
        if (nextSibling != null)
            webAppType.insertBefore(insertedEjbRef, nextSibling);
        else webAppType.insertBefore(insertedEjbRef, findEjbRefInsertPoint(webAppType));

        Element ejbRefName = webAppDoc.createElement("ejb-ref-name");
        ejbRefName.setTextContent(insertedEjbRefName);
        insertedEjbRef.appendChild(ejbRefName);

        Element ejbRefType = webAppDoc.createElement("ejb-ref-type");
        ejbRefType.setTextContent(ei.getBeanType());
        insertedEjbRef.appendChild(ejbRefType);

        Element homeType = webAppDoc.createElement("home");
        homeType.setTextContent(ei.getBeanInterface().getName());
        insertedEjbRef.appendChild(homeType);

        Element remoteType = webAppDoc.createElement("remote");
        remoteType.setTextContent(ei.getBeanInterface().getName());
        insertedEjbRef.appendChild(remoteType);

        Element ejbLink = webAppDoc.createElement("ejb-link");
        ejbLink.setTextContent(ejbLinkValue);
        insertedEjbRef.appendChild(ejbLink);
    }

    private void insertEJBLocalRefInWebApp(Element webAppType, EJBInfo ei, String ejbLinkValue, Document webAppDoc)
    {
        List ejbLocalRefArray = DomUtils.getChildElementsByName(webAppType, "ejb-local-ref");
        String insertedEjbRefName = ei.getRefName();

        Node nextSibling = null;
        for (int j = ejbLocalRefArray.size() - 1; j >= 0; j--) {
            Element ejbLocalRef = (Element) ejbLocalRefArray.get(j);
            String ejbRefName = DomUtils.getChildElementText(ejbLocalRef, "ejb-ref-name");
            if (insertedEjbRefName.equals(ejbRefName)) {
                nextSibling = ejbLocalRef.getNextSibling();
                webAppType.removeChild(ejbLocalRef);
                break;
            }
        }

        // insert a new <ejb-local-ref> entry and fill in the values
        Element insertedEjbLocalRef = webAppDoc.createElement("ejb-local-ref");
        if (nextSibling != null)
            webAppType.insertBefore(insertedEjbLocalRef, nextSibling);
        else webAppType.insertBefore(insertedEjbLocalRef, findEjbLocalRefInsertPoint(webAppType));

        Element ejbRefName = webAppDoc.createElement("ejb-ref-name");
        ejbRefName.setTextContent(insertedEjbRefName);
        insertedEjbLocalRef.appendChild(ejbRefName);

        Element ejbRefType = webAppDoc.createElement("ejb-ref-type");
        ejbRefType.setTextContent(ei.getBeanType());
        insertedEjbLocalRef.appendChild(ejbRefType);

        Element homeType = webAppDoc.createElement("local-home");
        homeType.setTextContent(ei.getHomeInterface().getName());
        insertedEjbLocalRef.appendChild(homeType);

        Element localType = webAppDoc.createElement("local");
        localType.setTextContent(ei.getBeanInterface().getName());
        insertedEjbLocalRef.appendChild(localType);

        Element ejbLink = webAppDoc.createElement("ejb-link");
        ejbLink.setTextContent(ejbLinkValue);
        insertedEjbLocalRef.appendChild(ejbLink);
    }

    private Node findEjbRefInsertPoint(Element parent) {
        Element e =  DomUtils.getChildElementByName(parent, "ejb-local-ref");
        if (e != null)
           return e;

        return findEjbLocalRefInsertPoint(parent);
    }

    private Node findEjbLocalRefInsertPoint(Element parent) {

        Element e = DomUtils.getChildElementByName(parent, "service-ref");
        if (e != null)
            return e;

        e = DomUtils.getChildElementByName(parent, "resource-ref");
        if (e != null)
            return e;

        e = DomUtils.getChildElementByName(parent, "resource-env-ref");
        if (e != null)
            return e;

        e = DomUtils.getChildElementByName(parent, "message-destination-ref");
        if(e != null)
            return e;

        e = DomUtils.getChildElementByName(parent, "message-destination");
        if (e != null)
            return e;

        e = DomUtils.getChildElementByName(parent, "locale-encoding-mapping-list");
        if (e != null)
            return e;

        return null;
    }
}
