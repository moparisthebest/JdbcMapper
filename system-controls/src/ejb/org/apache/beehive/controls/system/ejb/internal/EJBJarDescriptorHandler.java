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

import org.apache.beehive.controls.system.ejb.EJBInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

/**
 * This class is used by the EJBControlAssembler to modify an ejb-jar.xml file's ejb-ref descriptors.
 * The assembler will only invoke this class if the EJBControl's @EJBHome.ejbLink() annotation attribute
 * has been set.
 * <p/>
 * The EjbJar assembler walks the ejb-jar.xml DOM tree looking for any session / entity beans
 * which contain ejb-ref or ejb-local-ref elements.  If an ejb-ref is found which matches the
 * ejbRefName from the EJBInfo helper class it is removed and then re-created using the proper values.
 */
public class EJBJarDescriptorHandler {

    /**
     * Get an instance of this class.
     * @return EJBJarDescriptorHandler instance.
     */
    public static EJBJarDescriptorHandler getInstance() {
        return new EJBJarDescriptorHandler();
    }

    /**
     * Private constructor, use getInstance() to get an instance of this class.
     */
    private EJBJarDescriptorHandler() {
    }

    /**
     * Assemble entry point.  Based on the type of EJBControl build a list of
     * DOM elements of the property type (either session or entity) and process
     * each entry in the list.
     *
     * @param document     DOM tree of an ejb-jar.xml file.
     * @param ejbInfo      Contains information about the EJB control.
     * @param ejbLinkValue The ejb-link value for the EJBs.
     */
    public void assemble(Document document, EJBInfo ejbInfo, String ejbLinkValue) {
        Element ejbJarType = document.getDocumentElement();
        Element entBeansType = DomUtils.getChildElementByName(ejbJarType, "enterprise-beans");

        String beanType = ejbInfo.getBeanType();
        if (!("Session".equals(beanType) || "Entity".equals(beanType)))
        {
             throw new UnsupportedOperationException("Unknown EJBControl type: "
                    + ejbInfo.getBeanType()
                    + beanType
                    + " Expected 'Session' or 'Entity'");
        }

        /* TODO: Currently, we update all session and entity bean entries with a ejb ref, even
           if the control is not used from the ejb.  We should probably only add an ejb ref
           to a given session or entity bean if the control is referenced by it. */
        List sessionEjbList = DomUtils.getChildElementsByName(entBeansType, "session");
        insertEJBRefsInEJBJar(document, ejbInfo, ejbLinkValue, sessionEjbList);

        List entityEjbList = DomUtils.getChildElementsByName(entBeansType, "entity");
        insertEJBRefsInEJBJar(document, ejbInfo, ejbLinkValue, entityEjbList);
    }

    /**
     * Insert EJB references in all of the descriptors of the EJB's in the supplied list
     *
     * @param document      DOM tree of an ejb-jar.xml file.
     * @param ejbInfo       Contains information about the EJB control.
     * @param ejbLinkValue  The ejb-link value for the EJBs.
     * @param ejbList       The list of EJB's
     */
    private void insertEJBRefsInEJBJar(Document document, EJBInfo ejbInfo, String ejbLinkValue, List ejbList) {
        for (Object ejb : ejbList) {
            if (ejbInfo.isLocal())
                insertEJBLocalRefInEJBJar((Element)ejb, ejbInfo, ejbLinkValue, document);
            else
                insertEJBRefInEJBJar((Element)ejb, ejbInfo, ejbLinkValue, document);
        }
    }

    /**
     * Insert a remote ejb-ref into the specified EJB's descriptor, if an ejb-ref already
     * exists with the same name, remove it before adding a new ref.
     *
     * @param ejb          Root DOM element of the EJB descriptor.
     * @param ei           EJBInfo helper.
     * @param ejbLinkValue New ejb-link value.
     * @param ejbDoc       The ejb-jar DOM root.
     */
    private void insertEJBRefInEJBJar(Element ejb, EJBInfo ei, String ejbLinkValue, Document ejbDoc) {

        List ejbRefArray = DomUtils.getChildElementsByName(ejb, "ejb-ref");
        String insertedEjbRefName = ei.getRefName();

        Node nextSibling = null;
        for (int j = ejbRefArray.size() - 1; j >= 0; j--) {
            Element ejbRef = (Element) ejbRefArray.get(j);
            String ejbRefName = DomUtils.getChildElementText(ejbRef, "ejb-ref-name");
            if (insertedEjbRefName.equals(ejbRefName)) {
                nextSibling = ejbRef.getNextSibling();
                ejb.removeChild(ejbRef);
                break;
            }
        }

        // insert a new <ejb-ref> entry and fill in the values
        Element insertedEjbRef = ejbDoc.createElement("ejb-ref");
        if (nextSibling != null) {
            ejb.insertBefore(insertedEjbRef, nextSibling);
        }
        else {
            ejb.insertBefore(insertedEjbRef, findEjbRefInsertPoint(ejb));
        }

        Element ejbRefName = ejbDoc.createElement("ejb-ref-name");
        ejbRefName.setTextContent(insertedEjbRefName);
        insertedEjbRef.appendChild(ejbRefName);

        Element ejbRefType = ejbDoc.createElement("ejb-ref-type");
        ejbRefType.setTextContent(ei.getBeanType());
        insertedEjbRef.appendChild(ejbRefType);

        Element homeType = ejbDoc.createElement("home");
        homeType.setTextContent(ei.getHomeInterface().getName());
        insertedEjbRef.appendChild(homeType);

        Element remoteType = ejbDoc.createElement("remote");
        remoteType.setTextContent(ei.getBeanInterface().getName());
        insertedEjbRef.appendChild(remoteType);

        Element ejbLink = ejbDoc.createElement("ejb-link");
        ejbLink.setTextContent(ejbLinkValue);
        insertedEjbRef.appendChild(ejbLink);
    }

    /**
     * Insert a local ejb-ref into the specified EJB's descriptor, if an ejb-ref already
     * exists with the same name, remove it before adding a new ref.
     *
     * @param ejb          Root DOM element of the EJB descriptor.
     * @param ei           EJBInfo helper.
     * @param ejbLinkValue New ejb-link value.
     * @param ejbDoc       The ejb-jar DOM root.
     */
    private void insertEJBLocalRefInEJBJar(Element ejb, EJBInfo ei, String ejbLinkValue, Document ejbDoc) {

        List ejbLocalRefArray = DomUtils.getChildElementsByName(ejb, "ejb-local-ref");
        String insertedEjbRefName = ei.getRefName();

        Node nextSibling = null;
        for (int j = ejbLocalRefArray.size() - 1; j >= 0; j--) {
            Element ejbLocalRef = (Element) ejbLocalRefArray.get(j);
            String ejbRefName = DomUtils.getChildElementText(ejbLocalRef, "ejb-ref-name");
            if (insertedEjbRefName.equals(ejbRefName)) {
                nextSibling = ejbLocalRef.getNextSibling();
                ejb.removeChild(ejbLocalRef);
                break;
            }
        }

        // insert a new <ejb-local-ref> entry and fill in the values
        Element insertedEjbLocalRef = ejbDoc.createElement("ejb-local-ref");
        if (nextSibling != null) {
            ejb.insertBefore(insertedEjbLocalRef, nextSibling);
        }
        else {
            ejb.insertBefore(insertedEjbLocalRef, findEjbLocalRefInsertPoint(ejb));
        }

        Element ejbRefName = ejbDoc.createElement("ejb-ref-name");
        ejbRefName.setTextContent(insertedEjbRefName);
        insertedEjbLocalRef.appendChild(ejbRefName);

        Element ejbRefType = ejbDoc.createElement("ejb-ref-type");
        ejbRefType.setTextContent(ei.getBeanType());
        insertedEjbLocalRef.appendChild(ejbRefType);

        Element homeType = ejbDoc.createElement("local-home");
        homeType.setTextContent(ei.getHomeInterface().getName());
        insertedEjbLocalRef.appendChild(homeType);

        Element localType = ejbDoc.createElement("local");
        localType.setTextContent(ei.getBeanInterface().getName());
        insertedEjbLocalRef.appendChild(localType);

        Element ejbLink = ejbDoc.createElement("ejb-link");
        ejbLink.setTextContent(ejbLinkValue);
        insertedEjbLocalRef.appendChild(ejbLink);
    }

    /**
     * ejb-refs must be inserted before any ejb-local-ref tags.
     *
     * @param parent The 'session' or 'entity' XML element.
     * @return A suitable node to insert the ejb-ref BEFORE.
     */
    private Node findEjbRefInsertPoint(Element parent) {

        Element e = DomUtils.getChildElementByName(parent, "ejb-local-ref");
        if (e != null)
            return e;

        return findEjbLocalRefInsertPoint(parent);
    }

    /**
     * The local ref must be inserted into the ejb-jar file at the proper
     * location based on the ejb-jar schema definition. Check for elements which
     * can immediatly FOLLOW an ejb-ref from the closest to farthest as
     * defined by the schema.
     *
     * @param parent The 'session' or 'entity' XML element.
     * @return A suitable node to insert the ejb-local-ref BEFORE.
     */
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
        if (e != null)
            return e;

        e = DomUtils.getChildElementByName(parent, "security-role-ref");
        if (e != null)
            return e;

        e = DomUtils.getChildElementByName(parent, "security-identity");
        if (e != null)
            return e;

        // only applies to entity beans
        e = DomUtils.getChildElementByName(parent, "query");
        if (e != null)
            return e;

        return null;
    }
}
