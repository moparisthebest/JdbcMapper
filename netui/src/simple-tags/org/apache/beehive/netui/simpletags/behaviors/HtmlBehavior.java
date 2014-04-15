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
package org.apache.beehive.netui.simpletags.behaviors;

import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.core.IDocumentTypeProducer;
import org.apache.beehive.netui.simpletags.core.TagContext;
import org.apache.beehive.netui.simpletags.core.services.ErrorReporter;
import org.apache.beehive.netui.simpletags.core.services.ScriptReporter;
import org.apache.beehive.netui.simpletags.html.HtmlConstants;
import org.apache.beehive.netui.simpletags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.simpletags.rendering.HtmlTag;
import org.apache.beehive.netui.simpletags.rendering.TagRenderingBase;
import org.apache.beehive.netui.simpletags.util.ContextUtils;
import org.apache.beehive.netui.simpletags.util.PageFlowAdaptor;
import org.apache.beehive.netui.util.Bundle;

import java.util.Locale;


public class HtmlBehavior extends ScriptContainerBehavior
        implements HtmlConstants, IDocumentTypeProducer
{
    /**
     * Constant representing the document type html 4.01
     */
    public static final String HTML_401 = "html4-loose";

    /**
     * Constant representing the document type html 4.01
     */
    public static final String HTML_401_QUIRKS = "html4-loose-quirks";

    /**
     * Constant representing the document type XHTML 1.0 Transitional.
     */
    public static final String XHTML_10 = "xhtml1-transitional";

    private HtmlTag.State _state = new HtmlTag.State();
    private TagRenderingBase _br;
    private boolean _useLocale = false;     // include xml:lang=defaultLocale.getLanguage()
    private String _docType;                // The document type

    // This is set with the rendering type of the document
    private int _rendering = TagRenderingBase.UNKNOWN_RENDERING;

    /**
     * Returns the name of the Tag.
     */
    public String getTagName()
    {
        return "Html";
    }

    /**
     * This method will return the TagRenderBase enum value for the document type.  The default
     * value is HTML 4.01.
     * @return int
     */
    public int getTargetDocumentType()
    {
        // have we set the rendering type?
        if (_rendering != TagRenderingBase.UNKNOWN_RENDERING)
            return _rendering;

        // check the _docType for the rendering type set on this behavior
        _rendering = TagRenderingBase.getDefaultDocType();
        if (_docType != null) {
            if (_docType.equals(HTML_401))
                _rendering = TagRenderingBase.HTML_RENDERING;
            else if (_docType.equals(HTML_401_QUIRKS))
                _rendering = TagRenderingBase.HTML_RENDERING_QUIRKS;
            else if (_docType.equals(XHTML_10))
                _rendering = TagRenderingBase.XHTML_RENDERING;
        }
        return _rendering;
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
        _state.dir = dir;
    }

    /**
     * Gets whether the default locale's language should be used.
     * @return true or false
     */
    public boolean isUseLocale()
    {
        return _useLocale;
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
        _useLocale = locale;
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
        _docType = docType;
    }

    /**
     * Set an attribute that will be displayed on the generated HTML tag.
     * @param name The name of the attribute to set.
     * @param value The value of the attribute.
     * @param facet Facet is not supported and will report an error.
     */
    public void setAttribute(String name, String value, String facet)
    {
        boolean error = false;

        if (facet != null) {
            String s = Bundle.getString("Tags_AttributeFacetNotSupported", new Object[]{facet});
            registerTagError(s, null);
        }
        // validate the name attribute, in the case of an error simply return.
        if (name == null || name.length() <= 0) {
            String s = Bundle.getString("Tags_AttributeNameNotSet");
            registerTagError(s, null);
            error = true;
        }
        if (error)
            return;

        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, name, value);
    }

    //******************* Lifecycle Methods ************************************


    public void preRender()
    {
        // The super prerender will push the scopeId onto the stack
        super.preRender();

        TagContext tagCtxt = ContextUtils.getTagContext();
        tagCtxt.setDocTypeProducer(this);

        // set the locale
        Locale currentLocale = PageFlowAdaptor.currentLocale(_useLocale);
        if (currentLocale == null) {
            currentLocale = Locale.getDefault();
        }
        _state.lang = currentLocale.getLanguage();

        // if there is an Id scope set then set the attribute to be output
        String idScope = getIdScope();
        if (idScope != null) {
            _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, "netui:idScope", idScope);
        }
    }

    public void renderStart(Appender appender)
    {
        _br = TagRenderingBase.Factory.getRendering(TagRenderingBase.HTML_TAG);
        _br.doStartTag(appender, _state);
        appender.append("\n");
    }

    public void renderEnd(Appender appender)
    {
        TagContext tagCtxt = ContextUtils.getTagContext();

        // if there are errors then we should output the error table
        ErrorReporter er = tagCtxt.getErrorReporter();
        er.reportCollectedErrors(appender);

        // the script can be written out by another tag,  typically this would be the <body> tag.
        ScriptReporter sr = tagCtxt.getScriptReporter();
        sr.writeScript(appender);

        // close the html tag
        _br.doEndTag(appender);
    }
}
