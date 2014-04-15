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
package org.apache.beehive.netui.tags.databinding.xml;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.jsp.JspException;

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * <p>
 * A tag that serializes an XMLBean into the output of a JSP page in order to move data to the browser for data binding.
 * </p>
 * @jsptagref.tagdescription
 * A tag that serializes an XMLBean into the output of a JSP page in order to move data to the browser for data binding.
 * @netui:tag name="serializeXML"
 *            description="A tag that serializes an XMLBean into the output of a JSP page in order to move data to the browser for data binding."
 */
public class SerializeXML
    extends AbstractClassicTag {

    private static final Logger LOGGER = Logger.getInstance(SerializeXML.class);
    private static final Object[] EMPTY_ARRAY = new Object[0];
    private static final Class[] EMPTY_CLASS_ARRAY = new Class[0];

    private Object _source;
    private String _divName;

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public String getTagName() {
        return "SerializeXML";
    }

    /**
     * Set the source document to be serialized.  The object referenced here should be an instance of
     * {@link org.apache.xmlbeans.XmlObject}.
     * @param source the source
     * @jsptagref.attributedescription
     * Set the source document to be serialized.  The object referenced here should be an instance of
     * {@link org.apache.xmlbeans.XmlObject}.
     * @jsptagref.attributesyntaxvalue <i>object_source</i>
     * @netui:attribute required="true" rtexprvalue="true"
     */
    public void setSource(Object source) {
        _source = source;
    }

    /**
     * Set the name of the div into which this XML will be rendered.
     * @param divName the div name
     * @jsptagref.attributedescription
     * Set the name of the div into which this XML will be rendered.
     * @jsptagref.attributesyntaxvalue <i>string_divName</i>
     * @netui:attribute required="true" rtexprvalue="true"
     */
    public void setDivName(String divName) {
        _divName = divName;
    }

    public int doStartTag() {
        return SKIP_BODY;
    }

    public int doEndTag()
        throws JspException {

        Object xmlText = null;
        if(_source != null) {
            Class xmlObject = null;
            try {
                xmlObject = Class.forName("org.apache.xmlbeans.XmlObject");

                if(xmlObject.isAssignableFrom(_source.getClass())) {
                    Method method = xmlObject.getMethod("xmlText", EMPTY_CLASS_ARRAY);
                    xmlText = method.invoke(_source, EMPTY_ARRAY);
                }
            }
            catch(ClassNotFoundException e) {
                String msg = "Unable to serialize object; Apache XMLBeans is not available.  To fix this problem, add XMLBeans to your project";
                LOGGER.error(msg);
                registerTagError(msg, e);
                reportErrors();
                localRelease();
                return EVAL_PAGE;
            }
            catch(NoSuchMethodException e) {
                String msg = "Unexpected exception occurred serializing supposed XMLBean.  Caues: " + e;
                assert false : msg;
                LOGGER.error(msg);
                localRelease();
                return EVAL_PAGE;
            }
            catch(InvocationTargetException e) {
                String msg = "Unexpected exception occurred serializing supposed XMLBean.  Caues: " + e;
                assert false : msg;
                LOGGER.error(msg);
                localRelease();
                return EVAL_PAGE;
            }
            catch(IllegalAccessException e) {
                String msg = "Unexpected exception occurred serializing supposed XMLBean.  Caues: " + e;
                assert false : msg;
                LOGGER.error(msg);
                localRelease();
                return EVAL_PAGE;
            }

            InternalStringBuilder buf = new InternalStringBuilder();
            buf.append("<div");
            buf.append(" id=\"");
            buf.append(_divName);
            buf.append("\">\n<!--\n");
            buf.append(xmlText);
            buf.append("\n-->\n</div>");

            write(buf.toString());
        }
        else LOGGER.info("The object to serialize was not an XMLBean");

        localRelease();

        return EVAL_PAGE;
    }

    public void localRelease() {
        super.localRelease();

        _source = null;
        _divName = null;
    }
} 
