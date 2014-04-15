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
package org.apache.beehive.netui.simpletags.rendering;

import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.core.IDocumentTypeProducer;
import org.apache.beehive.netui.simpletags.core.TagContext;
import org.apache.beehive.netui.simpletags.util.ContextUtils;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.config.ConfigUtil;
import org.apache.beehive.netui.util.config.bean.DocType;
import org.apache.beehive.netui.util.config.bean.JspTagConfig;
import org.apache.beehive.netui.util.logging.Logger;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 */
public abstract class TagRenderingBase
{
    private static final Logger logger = Logger.getInstance(TagRenderingBase.class);

    /**
     * Unknown Rendering
     */
    public static final int UNKNOWN_RENDERING = 0;

    /**
     * Identifier for HTML 4.01 Rendering
     */
    public static final int HTML_RENDERING = 1;

    /**
     * Identifier for XHTML Transitional Rendering
     */
    public static final int XHTML_RENDERING = 2;

    /**
     * Identifier for HTML 4.01 Rendering in Quirks mode
     */
    public static final int HTML_RENDERING_QUIRKS = 3;

    //////////////////////////////////// Supported Rendering Constants  ////////////////////////////

    /**
     * The static initializer will read the netui config file and set the doc type of there is
     * one specified.
     */

    private static int _defaultDocType;

    static
    {
        _defaultDocType = HTML_RENDERING_QUIRKS;
        JspTagConfig tagConfig = ConfigUtil.getConfig().getJspTagConfig();
        if (tagConfig != null) {
            DocType docType = tagConfig.getDocType();
            setDefaultDocType(docType);
        }
    }

    public static int getDefaultDocType()
    {
        return _defaultDocType;
    }

    public static void setDefaultDocType(DocType docType)
    {
        if (docType != null) {
            if (docType == DocType.HTML4_LOOSE)
                _defaultDocType = TagRenderingBase.HTML_RENDERING;
            else if (docType == DocType.HTML4_LOOSE_QUIRKS)
                _defaultDocType = TagRenderingBase.HTML_RENDERING_QUIRKS;
            else if (docType== DocType.XHTML1_TRANSITIONAL)
                _defaultDocType = TagRenderingBase.XHTML_RENDERING;
        }
    }

    /**
     * Token identifying the Anchor Renderer &lt;a>
     */
    public static final Object ANCHOR_TAG = new Object();
    public static final Object AREA_TAG = new Object();
    public static final Object BASE_TAG = new Object();
    public static final Object BODY_TAG = new Object();
    public static final Object BR_TAG = new Object();
    public static final Object CAPTION_TAG = new Object();
    public static final Object FORM_TAG = new Object();
    public static final Object IMAGE_TAG = new Object();
    public static final Object INPUT_BOOLEAN_TAG = new Object();
    public static final Object INPUT_FILE_TAG = new Object();
    public static final Object INPUT_HIDDEN_TAG = new Object();
    public static final Object INPUT_IMAGE_TAG = new Object();
    public static final Object INPUT_SUBMIT_TAG = new Object();
    public static final Object INPUT_TEXT_TAG = new Object();
    public static final Object HTML_TAG = new Object();
    public static final Object LABEL_TAG = new Object();
    public static final Object OPTION_TAG = new Object();
    public static final Object SELECT_TAG = new Object();
    public static final Object SPAN_TAG = new Object();
    public static final Object DIV_TAG = new Object();
    public static final Object TABLE_TAG = new Object();
    public static final Object TBODY_TAG = new Object();
    public static final Object TD_TAG = new Object();
    public static final Object TEXT_AREA_TAG = new Object();
    public static final Object TH_TAG = new Object();
    public static final Object THEAD_TAG = new Object();
    public static final Object TFOOT_TAG = new Object();
    public static final Object TR_TAG = new Object();
    public static final Object SCRIPT_TAG = new Object();

    //////////////////////////////////// Abstract Methods  ////////////////////////////

    /**
     * Render the start tag for an element.  The element will render the tag and all of it's
     * attributes into a InternalStringBuilder.
     * @param sb          A InternalStringBuilder where the element start tag is appended.
     * @param renderState The state assocated with the element.
     */
    abstract public void doStartTag(Appender sb, AbstractTagState renderState);

    /**
     * Render the end tag for an element. The end tag will be rendered if the tag requires an end tag.
     * @param sb A InternalStringBuilder where the element end tag may be appended.
     */
    abstract public void doEndTag(Appender sb);

    /**
     * @param buf
     * @param name
     */
    protected final void renderTag(Appender buf, String name)
    {
        assert (buf != null) : "Parameter 'buf' must not be null.";
        assert (name != null) : "Parameter 'name' must not be null";

        buf.append("<");
        buf.append(name);
    }

    /**
     * @param buf
     * @param name
     */
    protected final void renderEndTag(Appender buf, String name)
    {
        buf.append("</");
        buf.append(name);
        buf.append(">");
    }

    /**
     * This method will append an attribute value to a InternalStringBuilder.
     * The method assumes that the attr is not <code>null</code>.  If the
     * <code>value</code> attribute is <code>null</code> the attribute will not be appended to the
     * <code>InternalStringBuilder</code>.
     * @param buf   The InternalStringBuilder to append the attribute into.
     * @param name  The name of the attribute
     * @param value The value of teh attribute.  If this is <code>null</code> the attribute will not be written.
     */
    protected final void renderAttribute(Appender buf, String name, String value)
    {
        assert (buf != null) : "Parameter 'buf' must not be null.";
        assert (name != null) : "Parameter 'name' must not be null";

        // without a value lets skip writting this out
        if (value == null)
            return;

        buf.append(" ");
        buf.append(name);
        buf.append("=\"");
        buf.append(value);
        buf.append("\"");
    }

    /**
     * @param buf
     * @param name
     * @param value
     */
    protected final void renderAttributeSingleQuotes(Appender buf, String name, String value)
    {
        assert (buf != null) : "Parameter 'buf' must not be null.";
        assert (name != null) : "Parameter 'name' must not be null";

        // without a value lets skip writting this out
        if (value == null)
            return;

        buf.append(" ");
        buf.append(name);
        buf.append("='");
        buf.append(value);
        buf.append("'");
    }

    /**
     * Render all of the attributes defined in a map and return the string value.  The attributes
     * are rendered with in a name="value" style supported by XML.
     * @param type an integer key indentifying the map
     */
    protected void renderAttributes(int type, Appender sb, AbstractAttributeState state, boolean doubleQuote)
    {
        HashMap map = null;
        switch (type) {
            case AbstractAttributeState.ATTR_GENERAL:
                map = state.getGeneralAttributeMap();
                break;
            default:
                String s = Bundle.getString("Tags_ParameterRenderError",
                        new Object[]{new Integer(type)});
                logger.error(s);
                throw new IllegalStateException(s);
        }
        renderGeneral(map, sb, doubleQuote);
    }

    final protected void renderAttributes(int type, Appender sb, AbstractAttributeState state)
    {
        renderAttributes(type, sb, state, true);
    }

    /**
     * This method will render all of the general attributes.
     */
    protected void renderGeneral(HashMap map, Appender sb, boolean doubleQuote)
    {
        if (map == null)
            return;

        Iterator iterator = map.keySet().iterator();
        for (; iterator.hasNext();) {
            String key = (String) iterator.next();
            if (key == null)
                continue;

            String value = (String) map.get(key);
            if (doubleQuote)
                renderAttribute(sb, key, value);
            else
                renderAttributeSingleQuotes(sb, key, value);
        }
    }

    /**
     * This is the factory for obtaining a Tag Rendering object.  The factory supports to types
     * of renderings HTML 4.01 and XHTML.  The factory is responsible for creating the rendering objects and
     * passing them out.  The target encoding may be specified on a page by page basis within a WebApp.  The
     * <code>getRendering</code> method will return a <code>TagRenderingBase</code> object.  This object is always
     * a stateless object.  The state needed to render the tag will be passed into the tag.
     */
    public static class Factory
    {
        private static HashMap _xhtml;      // The XHTML TagRenderingBase objects
        private static HashMap _html;       // The HTML TagRenderingBase objects
        private static HashMap _htmlQuirks; // THe HTML Quirks TagRenderingBase object

        private static ConstantRendering _xhtmlConstants;
        private static ConstantRendering _htmlConstants;

        // create the HashMaps and their static renderings.
        static
        {
            _xhtml = new HashMap(37);
            _html = new HashMap(37);
            _htmlQuirks = new HashMap(37);

            // build the supported renderers.
            AnchorTag.add(_html, _htmlQuirks, _xhtml);
            AreaTag.add(_html, _htmlQuirks, _xhtml);
            BaseTag.add(_html, _htmlQuirks, _xhtml);
            BodyTag.add(_html, _htmlQuirks, _xhtml);
            CaptionTag.add(_html, _htmlQuirks, _xhtml);
            DivTag.add(_html, _htmlQuirks, _xhtml);
            FormTag.add(_html, _htmlQuirks, _xhtml);
            ImageTag.add(_html, _htmlQuirks, _xhtml);
            InputBooleanTag.add(_html, _htmlQuirks, _xhtml);
            InputFileTag.add(_html, _htmlQuirks, _xhtml);
            InputHiddenTag.add(_html, _htmlQuirks, _xhtml);
            InputImageTag.add(_html, _htmlQuirks, _xhtml);
            InputSubmitTag.add(_html, _htmlQuirks, _xhtml);
            InputTextTag.add(_html, _htmlQuirks, _xhtml);
            HtmlTag.add(_html, _htmlQuirks, _xhtml);
            LabelTag.add(_html, _htmlQuirks, _xhtml);
            OptionTag.add(_html, _htmlQuirks, _xhtml);
            SelectTag.add(_html, _htmlQuirks, _xhtml);
            SpanTag.add(_html, _htmlQuirks, _xhtml);
            TableTag.add(_html, _htmlQuirks, _xhtml);
            TBodyTag.add(_html, _htmlQuirks, _xhtml);
            TdTag.add(_html, _htmlQuirks, _xhtml);
            TextAreaTag.add(_html, _htmlQuirks, _xhtml);
            ThTag.add(_html, _htmlQuirks, _xhtml);
            THeadTag.add(_html, _htmlQuirks, _xhtml);
            TFootTag.add(_html, _htmlQuirks, _xhtml);
            TrTag.add(_html, _htmlQuirks, _xhtml);
            ScriptTag.add(_html, _htmlQuirks, _xhtml);
        }

        /**
         * Factory method for getting a TagRenderingBase for a tag.  The default rendering is HTML 4.01.
         * @param token The type of TagRenderingBase to retrieve.
         * @return A <code>TagRenderingBase</code>
         */
        public static TagRenderingBase getRendering(Object token)
        {
            int renderingType = _defaultDocType;
            TagContext tagCtxt = ContextUtils.getTagContext();
            int reqRender = tagCtxt.getTagRenderingType();
            if (reqRender != -1)
                renderingType = reqRender;
            else {
                IDocumentTypeProducer docProducer = tagCtxt.getDocTypeProducer();
                if (docProducer != null) {
                    renderingType = docProducer.getTargetDocumentType();
                }
                tagCtxt.setTagRenderingType(renderingType);
            }

            // pick the map of renderers
            HashMap h = null;
            switch (renderingType) {
                case HTML_RENDERING:
                    h = _html;
                    break;
                case HTML_RENDERING_QUIRKS:
                    h = _htmlQuirks;
                    break;
                case XHTML_RENDERING:
                    h = _xhtml;
                    break;
                default:
                    assert(true) : "Didn't find the map for rendering type:" + renderingType;
            }

            // return the renderer
            Object o = h.get(token);
            assert(o != null) : "Renderer was not found for [" + token + "] rendering:" + renderingType;
            return (TagRenderingBase) o;
        }

        /**
         * Return true if the current document is XHTML
         * @return boolean
         */
        public static boolean isXHTML()
        {
            TagContext tagCtxt = ContextUtils.getTagContext();
            IDocumentTypeProducer html = tagCtxt.getDocTypeProducer();

            // the default is html 4.0
            int renderingType = _defaultDocType;
            if (html != null) {
                renderingType = html.getTargetDocumentType();
            }

            return (renderingType == XHTML_RENDERING);
        }

        /**
         * @return ConstantRendering
         */
        public static ConstantRendering getConstantRendering()
        {
            TagContext tagCtxt = ContextUtils.getTagContext();
            IDocumentTypeProducer docProducer = tagCtxt.getDocTypeProducer();

            if (_htmlConstants == null) {
                _htmlConstants = ConstantRendering.getRendering(HTML_RENDERING);
                _xhtmlConstants = ConstantRendering.getRendering(XHTML_RENDERING);
            }

            // the default is docProducer 4.0
            int renderingType = TagRenderingBase.getDefaultDocType();
            if (docProducer != null) {
                renderingType = docProducer.getTargetDocumentType();
            }
            return (renderingType == XHTML_RENDERING) ? _xhtmlConstants : _htmlConstants;
        }
    }

    /**
     * @return String
     */
    public static String getAmp()
    {
        TagContext tagCtxt = ContextUtils.getTagContext();
        IDocumentTypeProducer docProducer = tagCtxt.getDocTypeProducer();

        // the default is docProducer 4.0
        int renderingType = HTML_RENDERING;
        if (docProducer != null) {
            renderingType = docProducer.getTargetDocumentType();
        }

        // pick the map of renderers
        return (renderingType == XHTML_RENDERING) ? "&amp;" : "&";
    }
}
