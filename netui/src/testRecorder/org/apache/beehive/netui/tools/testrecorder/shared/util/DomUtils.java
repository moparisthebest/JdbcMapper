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
package org.apache.beehive.netui.tools.testrecorder.shared.util;

import java.util.List;
import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * <p>This class exists simply because DOM is so inconvenient to use.</p>
 */
public final class DomUtils {

    /* do not construct */
    private DomUtils() {}

    /**
     * <p>Returns the first child element with the given localName. Returns
     * <code>null</code> if not found.</p>
     *
     * @param parent parent element
     * @param localName localName of the child element
     * @return child element
     */
    public static Element getChildElementByName(Element parent, String localName) {
        return getChildElementByName(parent, null, localName);
    }

    public static Element getChildElementByName(Element parent, String namespace, String localName) {
        NodeList children = null;
        children = (namespace != null ? parent.getElementsByTagNameNS(namespace, localName) : parent.getElementsByTagName(localName));

        for(int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE)
                return (Element)node;
        }

        return null;
    }

    /**
     * <p>Returns a list of child elements with the given
     * name. Returns an empty list if there are no such child
     * elements.</p>
     *
     * @param parent parent element
     * @param localName name of the child element
     * @return child elements
     */
    public static List getChildElementsByName(Element parent, String localName) {
        return getChildElementsByName(parent, null, localName);
    }

    public static List getChildElementsByName(Element parent, String namespace, String localName) {
        NodeList children =
            (namespace != null ? parent.getElementsByTagNameNS(namespace, localName) : parent.getElementsByTagName(localName));

        ArrayList elements = new ArrayList(children.getLength());
        for(int i = 0; i < children.getLength(); i++)
            if(children.item(i).getNodeType() == Node.ELEMENT_NODE)
                elements.add((Element)children.item(i));

        return elements;
    }

    /**
     * <p>Returns the text value of a child element. Returns
     * <code>null</code> if there is no child element found.</p>
     *
     * @param parent parent element
     * @param localName name of the child element
     * @return text value
     */
    public static String getChildElementText(Element parent, String localName) {
        return getChildElementText(parent, null, localName);
    }

    public static String getChildElementText(Element parent, String namespace, String localName) {
        List list = DomUtils.getChildElementsByName(parent, namespace, localName);
        if(list.size() == 1) {
            Element child = (Element) list.get(0);
            StringBuffer buf = new StringBuffer();
            NodeList children = child.getChildNodes();
            for(int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if(node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE) {
                    buf.append(((Text)node).getData());
                }
            }
            return buf.toString();
        }
        else return null;
    }

    /**
     * <p>Returns the text value of a child element. Returns
     * <code>null</code> if there is no child element found.</p>
     *
     * @param element element
     * @return text value
     */
    public static String getElementText(Element element) {
        StringBuffer buf = new StringBuffer();

        NodeList children = element.getChildNodes();
        for(int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if(node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE) {
                buf.append(((Text)node).getData());
            }
        }

        return buf.toString();
    }
}
