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
package org.apache.beehive.netui.tags.databinding.pageinput;

import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.tags.AbstractSimpleTag;

/**
 * <p>
 * The DeclarePageInput tag is used to declare variables that are passed as outputs of Page Flow actions to a
 * JSP.  This allows pages to declare a data contract that invoking actions must satisfy in order to
 * successfully render a page; in essence, this is a simple method signature for the JSP.
 * </p>
 * <p>
 * Page inputs are added to a Page Flow's {@link org.apache.beehive.netui.pageflow.Forward} class via the
 * {@link org.apache.beehive.netui.pageflow.Forward#addActionOutput(String, Object)} method.  From the page's
 * perspective, the action outputs are known as <i>page inputs</i> and are available via the JSP EL
 * implicit object <code>pageInput</code> using the name given them on the
 * {@link org.apache.beehive.netui.pageflow.Forward} and set on this tag via {@link #setName(String)}.
 * </p>
 * <p>
 * A page input can be declared to be required; if required, the page input must be available in
 * the map of action outputs passed to the page.
 * </p>
 * <p>
 * For example, to add an actiout output called <code>profile</code> to a {@link org.apache.beehive.netui.pageflow.Forward}
 * an action would contain code like:<br/>
 * <pre>
 *     forward.addActionOutput("profile", yourProfile);
 * </pre>
 * In order to declare this as a page input in a JSP, the page would contain a tag as:
 * <pre>
 *     &lt;netui-data:declarePageInput name="profile" type="org.someprofile.ProfileBean"/>
 * </pre>
 * and the <code>profile</code> object could be referenced in the JSP as:
 * <pre>
 *     ${pageInput.profile}
 * </pre>
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * The DeclarePageInput tag is used to declare variables that are passed as outputs of Page Flow actions to a
 * JSP.  This allows pages to declare a data contract that invoking actions must satisfy in order to
 * successfully render a page; in essence, this is a simple method signature for the JSP.
 * </p>
 * <p>
 * Page inputs are added to a Page Flow's {@link org.apache.beehive.netui.pageflow.Forward} class via the
 * {@link org.apache.beehive.netui.pageflow.Forward#addActionOutput(String, Object)} method.  From the page's
 * perspective, the action outputs are known as <i>page inputs</i> and are available via the JSP EL
 * implicit object <code>pageInput</code> using the name given them on the
 * {@link org.apache.beehive.netui.pageflow.Forward} and set on this tag via {@link #setName(String)}.
 * </p>
 * <p>
 * A page input can be declared to be required; if required, the page input must be available in
 * the map of action outputs passed to the page.
 * </p>
 * <p>
 * For example, to add an actiout output called <code>profile</code> to a {@link org.apache.beehive.netui.pageflow.Forward}
 * an action would contain code like:<br/>
 * <pre>
 *     forward.addActionOutput("profile", yourProfile);
 * </pre>
 * In order to declare this as a page input in a JSP, the page would contain a tag as:
 * <pre>
 *     &lt;netui-data:declarePageInput name="profile" type="org.someprofile.ProfileBean"/>
 * </pre>
 * and the <code>profile</code> object could be referenced in the JSP as:
 * <pre>
 *     ${pageInput.profile}
 * </pre>
 * </p>
 *
 * @netui:tag name="declarePageInput" body-content="scriptless"
 *            description="Use this tag to declare a page input variable that is available in the pageInput databinding context."
 */
public class DeclarePageInput
    extends AbstractSimpleTag {

    private static final Logger LOGGER = Logger.getInstance(DeclarePageInput.class);
    private static final String EMPTY_STRING = "";

    private String _type = null;
    private String _name = null;
    private boolean _required = true;

    /**
     * Get the name of this tag.  This is used to identify the type of this tag
     * for reporting tag errors.
     *
     * @return a constant String representing the name of this tag.
     */
    public String getTagName() {
        return "DeclarePageInput";
    }

    /**
     * Set the name of a variable that can be referecned using the page
     * input data binding context.
     *
     * @param name the name of the variable
     * @jsptagref.attributedescription The name of the variable to reference.
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     * @netui:attribute required="true"
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Set a flag that declares whether this page intput is required or optiona.  If a Page Input is required,
     * the tag will report an error if the page input key does not appear in the set of page inputs for a page.
     *
     * @param required whether to require the page input for the page
     * @jsptagref.attributedescription
     * Set a flag that declares whether this page intput is required or optiona.  If a Page Input is required,
     * the tag will report an error if the page input key does not appear in the set of page inputs for a page.
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     * @netui:attribute required="false"
     */
    public void setRequired(boolean required) {
        _required = required;
    }

    /**
     * Set the String classname variable that represents the expected type of the page input.
     *
     * @param type the type of the variable that is referenced
     * @jsptagref.attributedescription
     * Set the String classname variable that represents the expected type of the page input.
     * @jsptagref.attributesyntaxvalue <i>string_type</i>
     * @netui:attribute required="true"
     */
    public void setType(String type) {
        _type = type;
    }

    /**
     *
     */
    public void doTag()
        throws JspException {

        LOGGER.debug("Added a page input named \"" + _name + "\" of type \"" + _type + "\"");

        if(_name.equals(EMPTY_STRING)) {
            String msg = Bundle.getErrorString("Tags_DeclarePageInput_EmptyName");
            registerTagError(msg, null);
        }

        if(_type.equals(EMPTY_STRING)) {
            String msg = Bundle.getErrorString("Tags_DeclarePageInput_EmptyType");
            registerTagError(msg, null);
        }

        if(_required) {
            assert getJspContext() instanceof PageContext;
            assert ((PageContext)getJspContext()).getRequest() instanceof HttpServletRequest;

            HttpServletRequest request = (HttpServletRequest)((PageContext)getJspContext()).getRequest();
            Map actionOutputMap = InternalUtils.getPageInputMap(request);
            if(actionOutputMap == null || !actionOutputMap.containsKey(_name)) {
                String msg = Bundle.getErrorString("Tags_DeclarePageInput_Required", new Object[]{_name});
                registerTagError(msg, null);
            }
        }

        if(hasErrors())
            reportErrors();
    }
}
