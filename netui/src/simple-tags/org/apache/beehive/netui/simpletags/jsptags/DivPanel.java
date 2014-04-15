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
import org.apache.beehive.netui.simpletags.behaviors.DivPanelBehavior;

import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import java.io.IOException;

/**
 * A DivPanelBehavior creates an HTML &lt;div> tag that may contain additional &lt;div> tags.  There will only
 * be a single div that is visible at a time.
 * @jsptagref.tagdescription Creates an HTML &lt;div> tag that may contain additional &lt;div> tags.  Only a single section will be visible at a time.
 * @netui:tag name="divPanel"
 * description="A divPanel is an placeholder which may contain multiple sections.  Only a single section will be visible at a time."
 */
public class DivPanel extends AbstractSimpleTag
{
    public DivPanel() {
        _behavior = new DivPanelBehavior();
    }
    /**
     * Sets an expression which indentifies the DivPanelState which will store the state of the
     * DivPanelBehavior between posts to the server.
     * @param dataSource the tree attribute name
     * @jsptagref.attributedescription An expression which identifies which DivPanelState object will store state between posts to the server.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>expression</i>
     * @netui:attribute description="Sets an expression which indentifies the DivPanelState storing the state of the
     * DivPanelBehavior between posts."
     */
    public void setDataSource(String dataSource)
    {
        ((DivPanelBehavior) _behavior).setDataSource(dataSource);
    }

    /**
     * Set the ID of the tag.
     * @param tagId the tagId.
     * @jsptagref.attributedescription Set the ID of the &lt;div> tag
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="Set the ID of the tag."
     */
    public void setTagId(String tagId)
    {
        ((DivPanelBehavior) _behavior).setTagId(tagId);
    }

    /**
     * Set the name of the first page to display.
     * @param firstPage the name of the first page.
     * @jsptagref.attributedescription Set the name of the first page to display.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute rtexprvalue="true"
     * description="Set the name of the first page to display."
     */
    public void setFirstPage(String firstPage)
    {
        ((DivPanelBehavior) _behavior).setFirstPage(firstPage);
    }

    /**
     * Causes the content of the section to be rendered into a buffer.
     * @throws javax.servlet.jsp.JspException if there are errors.
     */
    public void doTag()
            throws JspException, IOException
    {
        _behavior.start();

        // it is always required to set the variable resolver on a datasource.
        ((DivPanelBehavior) _behavior).setVariableResolver(getPageContext().getVariableResolver());

        ServletResponse response = getPageContext().getResponse();
        Appender appender = new ResponseAppender(response);
        _behavior.preRender();
        _behavior.renderStart(appender);

        JspFragment frag = getJspBody();
        if (frag != null) {
            frag.invoke(response.getWriter());
        }

        _behavior.renderEnd(appender);
        _behavior.postRender();
        _behavior.terminate();
    }
}
