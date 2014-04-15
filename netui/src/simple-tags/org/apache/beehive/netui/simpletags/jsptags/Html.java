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
import org.apache.beehive.netui.simpletags.behaviors.HtmlBehavior;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.ServletResponse;

/**
 * Generates the html element and performs error handling within its body.
 * @jsptagref.tagdescription <p>
 * Renders an &lt;html> tag.
 * </p>
 * @example In this sample, the &lt;netui:html> tag uses the default locale and the direction of the HTML is
 * left-to-right (LTR).
 * <pre>&lt;netui:html dir="LTR" useLocale="true"  /></pre>
 *
 * @netui:tag name="html" body-content="scriptless" dynamic-attributes="true" description="Generates the html element and performs error handling within its body."
 */
public class Html extends ScriptContainer implements DynamicAttributes
{

    public Html() {
        _behavior = new HtmlBehavior();
    }

    /////////////////////////// Attributes ////////////////////////////

    /**
     * Sets the dir value of the html.
     * @param dir the direction of text, "LTR" or "RTL"
     * @jsptagref.attributedescription Specifies the direction of text. (<code>LTR | RTL</code>)
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the dir value (ltr or rtl) of the html."
     */
    public void setDir(String dir)
    {
        ((HtmlBehavior) _behavior).setDir(dir);
    }

    /**
     * Gets whether the default locale's language should be used.
     * @return true or false
     */
    public boolean isUseLocale()
    {
        return ((HtmlBehavior) _behavior).isUseLocale();
    }

    /**
     * Sets whether the default locale's language should be used.
     * @param locale true or false
     * @jsptagref.attributedescription Sets whether the default locale's language should be used.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_locale</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Sets whether the default locale's language should be used."
     */
    public void setUseLocale(boolean locale)
    {
        ((HtmlBehavior) _behavior).setUseLocale(locale);
    }

    /**
     * Set the document type (html4-loose or xhtml1-transitional) of the document.
     * @jsptagref.attributedescription Set the document type (html4-loose or xhtml1-transitional) of the document.
     * The default is html4-loose-quirks.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_doctype</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the document type (html4-loose or xhtml1-transitional) of the document."
     */
    public void setDocumentType(String docType)
    {
        ((HtmlBehavior) _behavior).setDocumentType(docType);
    }

    public void doTag()
            throws JspException, java.io.IOException
    {
        _behavior.start();

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

    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException
    {
        ((HtmlBehavior) _behavior).setAttribute(localName, value.toString(), uri);
    }
}
