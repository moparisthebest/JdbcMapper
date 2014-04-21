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
package org.apache.beehive.netui.compiler.model;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.Text;

import java.util.ArrayList;

public abstract class XmlElementSupport
{
    private String _comment;
    
    public void setComment( String comment )
    {
        _comment = comment;
    }

    public final void writeXML(XmlModelWriter xw, Element element) {
        if (_comment != null) {
            xw.addComment( element, ' ' + _comment + ' ' );
        }
        writeToElement(xw, element);
    }
    
    protected abstract void writeToElement(XmlModelWriter xw, Element element);
    
    protected final void setElementAttributeMayBeEmpty(Element element, String attrName, String value)
    {
        if (value != null) {
            String existingAttr = getElementAttribute(element, attrName);
            if (existingAttr == null || existingAttr.length() == 0) {
                element.setAttribute(attrName, value);
            }
        }
    }

    protected final void setElementAttribute(Element element, String attrName, String value)
    {
        if (value != null && value.length() > 0 ) {
            String existingAttr = getElementAttribute(element, attrName);
            if (existingAttr == null || existingAttr.length() == 0) {
                element.setAttribute(attrName, value);
            }
        }
    }

    protected final void setElementAttribute(Element element, String attrName, Boolean value)
    {
        if (value != null) {
            String existingAttr = getElementAttribute(element, attrName);
            if (existingAttr == null || existingAttr.length() == 0) {
                element.setAttribute(attrName, value.toString());
            }
        }
    }

    /**
     * Gets the attribute value, or <code>null</code> (unlike <code>Element.getAttribute</code>).
     */
    protected String getElementAttribute(Element element, String attrName)
    {
        Attr attr = element.getAttributeNode(attrName);
        return attr != null ? attr.getValue() : null;
    }

    protected final void setElementAttribute(Element element, String attrName, boolean value)
    {
        if (value) {
            String existingAttr = getElementAttribute(element, attrName);
            if (existingAttr == null) {
                element.setAttribute(attrName, Boolean.toString(value));
            }
        }
    }
    
    protected final Element findChildElement(XmlModelWriter xw, Element parent, String childName)
    {
        return findChildElement(xw, parent, childName, null, null, false, null);
    }

    protected final Element findChildElement(XmlModelWriter xw, Element parent, String childName,
                                             boolean createIfNotPresent, String[] createOrder)
    {
        return findChildElement(xw, parent, childName, null, null, createIfNotPresent, createOrder);
    }

    protected final Element findChildElementWithChildText(XmlModelWriter xw, Element parent, String childName,
                                                          String childSubElementName, String textValue,
                                                          boolean createIfNotPresent, String[] createOrder)
    {
        Element[] matchingChildren = getChildElements(parent, childName);

        for (int i = 0; i < matchingChildren.length; i++) {
            Element childSubElement = findChildElement(xw, matchingChildren[i], childSubElementName, false, createOrder);
            if (childSubElement != null) {
                String text = getTextContent(childSubElement);
                if (textValue.equals(text)) {
                    return childSubElement;
                }
            }
        }
        
        if (createIfNotPresent) {
            Element newChild = xw.addElement(parent, childName);
            xw.addElementWithText(newChild, childSubElementName, textValue);
            return newChild;
        }
        
        return null;
    }

    protected final Element findChildElement(XmlModelWriter xw, Element parent, String childName,
                                             String keyAttributeName, String keyAttributeValue)
    {
        return findChildElement(xw, parent, childName, keyAttributeName, keyAttributeValue, false, null);
    }
    
    /**
     * Find a child element by name, and optionally add it if it isn't present.
     * 
     * @param xw the XmlModelWriter
     * @param parent the parent element
     * @param childName the name of the desired child element
     * @param keyAttributeName the name of a key attribute in the child element by which to restrict the search.
     *            May be <code>null</code>, in which case there is no restriction.
     * @param keyAttributeValue the value of a key attribute in the child element by which to restrict the search.
     *            Only used if <code>keyAttributeName</code> is not <code>null</code>.  This value may be
     *            <code>null</code>, which means that the target node must have a null value.
     * @param createIfNotPresent if <code>true</code>, the node will be created if it's not present.
     * @param createOrder an array of Strings that describes the order of insertion.  May be <code>null</code>, in
     *            which case a newly-created node is appended to the parent.
     * @return the node.
     */
    protected final Element findChildElement(XmlModelWriter xw, Element parent, String childName,
                                             String keyAttributeName, String keyAttributeValue,
                                             boolean createIfNotPresent, String[] createOrder)
    {
        NodeList childNodes = parent.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); ++i)
        {
            Node node = childNodes.item(i);

            if (node instanceof Element)
            {
                Element childElement = (Element) node;

                if (childName.equals(childElement.getTagName()))
                {
                    // If there's no target key attribute to match, just return the element.
                    if (keyAttributeName == null) {
                        return childElement;
                    }
                    
                    // Return the element if the key attribute values match (or if both are null).
                    String childElementAttributeValue = getElementAttribute(childElement, keyAttributeName);
                    if ((keyAttributeValue == null && childElementAttributeValue == null)
                            || (keyAttributeValue != null && keyAttributeValue.equals(childElementAttributeValue)))
                    {
                        return childElement;
                    }
                }
            }
        }

        if (createIfNotPresent)
        {
            Element newChild = xw.getDocument().createElement(childName);
            Node insertBefore = null;
            
            if (createOrder != null) {
                for (int i = 0; i < createOrder.length; i++) {
                    String nodeName = createOrder[i];
                    if (nodeName.equals(childName)) {
                        while (++i < createOrder.length) {
                            insertBefore = findChildElement(xw, parent, createOrder[i], false, null);
                            if (insertBefore != null) {
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            
            if (insertBefore != null) {
                parent.insertBefore(newChild, insertBefore);
            } else {
                parent.appendChild(newChild);
            }
            
            
            if (keyAttributeName != null && keyAttributeValue != null) {
                newChild.setAttribute(keyAttributeName, keyAttributeValue);
            }
            return newChild;
        }

        return null;
    }

    protected Element[] getChildElements(Element element, String nameFilter)
    {
        NodeList children = element.getChildNodes();
        ArrayList list = new ArrayList(children.getLength());
        for (int i = 0; i < children.getLength(); ++i)
        {
            Node node = children.item(i);
            if (node instanceof Element)
            {
                if (nameFilter == null || nameFilter.equals(((Element) node).getTagName()))
                {
                    list.add(node);
                }
            }
        }

        return (Element[]) list.toArray(new Element[list.size()]);
    }

    public static boolean isWhiteSpace(String s)
    {
        for (int j = 0; j < s.length(); ++j) {
            if (! Character.isWhitespace(s.charAt(j))) {
                return false;
            }
        }

        return true;
    }

    public static String getTextContent(Element element)   // TODO: move to a utils class, so XmlModelWriter is independentf
    {
        NodeList children = element.getChildNodes();
        String retVal = null;

        for (int i = 0, len = children.getLength(); i < len; ++i) {
            Node child = children.item(i);
            if (! (child instanceof Text)) {
                return null;
            }
            String text = child.getNodeValue();
            if (! isWhiteSpace(text)) {
                if (retVal != null) {
                    return null;
                }
                retVal = text;
            }
        }

        return retVal;
    }
}
