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
package org.apache.beehive.netui.simpletags.jsptags;

import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.appender.ResponseAppender;
import org.apache.beehive.netui.simpletags.behaviors.AnchorBehavior;
import org.apache.beehive.netui.simpletags.behaviors.DivSectionBehavior;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * @netui:tag name="divSection" body-content="scriptless" dynamic-attributes="true"
 *          description="Use this tag to mark out content to replace a netui-template:includeSection within a template file."
 */
public class DivSection extends AbstractSimpleTag
{
    public DivSection() {
        _behavior = new DivSectionBehavior();
    }

    /**
     * Sets the visible state of the tag.
     * @param visible <code>Boolean</code> value representing the visible state.
     *
     * @jsptagref.attributedescription
     * Boolean. Determines if the section is visible.
     * 
     * @jsptagref.databindable false
     * 
     * @jsptagref.attributesyntaxvalue <i>boolean_literal_visible</i>
     *
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Determines if the section is visible."
     */
    public void setVisible(boolean visible) {
        ((DivSectionBehavior) _behavior).setVisible(visible);
    }

    /**
     * Causes the content of the section to be rendered into a buffer.
     * @throws JspException if there are errors.
     */
    public void doTag() throws JspException, IOException
    {
        _behavior.start();
        String value = getBufferBody(true);
        ((DivSectionBehavior) _behavior).setBodyContent(value);

        Appender appender = new ResponseAppender(getPageContext().getResponse());
        ((AnchorBehavior) _behavior).setText(value);
        _behavior.preRender();
        _behavior.renderStart(appender);
        _behavior.renderEnd(appender);
        _behavior.postRender();
        _behavior.terminate();
    }

}
