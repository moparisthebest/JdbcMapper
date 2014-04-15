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
import org.apache.beehive.netui.simpletags.behaviors.BaseBehavior;
import org.apache.beehive.netui.simpletags.html.HtmlConstants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import java.io.IOException;

/**
 * Provides the base for every URL on this page.
 * @jsptagref.tagdescription Provides the base for every URL on the page.
 * @example In this sample, the Base tag is simply dropped in and then automatically determines
 * the base for each URL on this page.
 * <pre>
 * &lt;head>
 *      &lt;netui:base />
 * &lt;/head>
 * </pre>
 * @netui:tag name="base" body-content="scriptless" dynamic-attributes="true" description="Provides the base for every URL on this page."
 */
public class Base extends AbstractSimpleTag
        implements HtmlConstants, DynamicAttributes
{
    public Base() {
        _behavior = new BaseBehavior();
    }

    /**
     * Set the default window target.
     * @param target the window target.
     * @jsptagref.attributedescription The default window target.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_windowTarget</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The default window target."
     */
    public void setTarget(String target)
    {
        ((BaseBehavior) _behavior).setTarget(setNonEmptyValueAttribute(target));
    }

    /**
     * Render the hyperlink.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException, IOException
    {
        _behavior.start();
        // evaluate the body, this is called basically so any attributes my be applied.
        getBufferBody(false);
        
        Appender appender = new ResponseAppender(getPageContext().getResponse());
        _behavior.preRender();
        _behavior.renderStart(appender);
        _behavior.renderEnd(appender);
        _behavior.postRender();
        _behavior.terminate();
    }

    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException
    {
        ((BaseBehavior) _behavior).setAttribute(localName, value.toString(), uri);
    }
}



