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
package org.apache.beehive.netui.tags.template;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.tags.javascript.IScriptReporter;
import org.apache.beehive.netui.tags.javascript.ScriptRequestState;
import org.apache.beehive.netui.tags.divpanel.DivPanel;
import org.apache.beehive.netui.tags.databinding.repeater.Repeater;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.Tag;
import java.util.HashMap;

import org.apache.struts.util.ResponseUtils;

/**
 * Used within a content page to provide content for a placeholder
 * section defined within a template.  The body content of the tag
 * is passed to the <code>IncludeSection</code> tag in the template
 * providing the content for that section.
 * If the <code>name</code> attribute matches a
 * <code>name</code> attribute on a
 * <code>IncludeSection</code> tag in the template, the body
 * content of this tag will be rendered.

 * @jsptagref.tagdescription
 * Sets HTML content inside placeholders defined by a 
 * {@link  IncludeSection} tag.
 * 
 * <p>The &lt;netui-template:section> tag must have a parent 
 * {@link Template} tag. 
 * 
 * <p>The &lt;netui-template:section> tag appears in content pages, which adopt a template page, 
 * set properties on the template's placeholders 
 * (using this &lt;netui-template:section> tag), 
 * and render the completed HTML in the browser.
 * 
 * <p>For content to be placed in the placeholder, the &lt;netui-template:section> and 
 * &lt;netui-template:includeSection> tags must have matching <code>name</code> attributes.
 * 
 * <p>For example, assume a template page defines the following content placeholder.
 * 
 * <p><b>In the template JSP page...</b>
 * 
 * <pre>      &lt;table>
 *          &lt;tr>
 *              &lt;td colspan="3">
 *                  &lt;netui-template:includeSection name="tableHeader"/>
 *              &lt;/td>
 *          &lt;/tr></pre>
 * 
 * <p>Then a content page can set HTML content in the placeholder using the &lt;netui-template:section> tag.
 * 
 * <p><b>In a content JSP page...</b>
 * 
 * <pre>    &lt;netui-template:section name="tableHeader">
 *        &lt;h1>HEADER TEXT&lt;/h1>
 *    &lt;/netui-template:section></pre>
 * 
 * <p>The HTML rendered in the browser will appear as follows.
 * 
 * <pre>      &lt;table>
 *          &lt;tr>
 *              &lt;td colspan="3">
 *                  &lt;h1>HEADER TEXT&lt;/h1>
 *              &lt;/td>
 *          &lt;/tr></pre>
 * 
 * @example 
 * Assume a &lt;netui-template:includeSection> tag defines a content placeholder inside a 
 * table row 
 *
 * <pre>    &lt;tr>
 *        &lt;netui-template:includeSection name="rowPlaceholder"/>
 *    &lt;/tr></pre>
 *    
 * <p>A content page can set content into the placeholder using the &lt;netui-template:section> 
 * tag as follows.
 * 
 * <pre>  &lt;netui-template:section name="rowPlaceHolder">
 *      &lt;td>&lt;p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, 
 *      sed diam nonummy nibh euismod tincidunt ut laoreet dolore 
 *      magna aliquam erat volutpat. Ut wisi enim ad minim veniam, 
 *      quis nostrud exerci tation ullamcorper suscipit lobortis nisl 
 *      ut aliquip ex ea commodo consequat.&lt;/p>&lt;/td>
 *  &lt;/netui-template:section></pre>
 * 
 * The HTML rendered in the browser will appear as follows.
 * 
 * <pre>    &lt;tr>
 *      &lt;td>&lt;p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, 
 *      sed diam nonummy nibh euismod tincidunt ut laoreet dolore 
 *      magna aliquam erat volutpat. Ut wisi enim ad minim veniam, 
 *      quis nostrud exerci tation ullamcorper suscipit lobortis nisl 
 *      ut aliquip ex ea commodo consequat.&lt;/p>&lt;/td>
 *    &lt;/tr></pre>
 *    
 * @netui:tag name="section"
 *          description="Use this tag to mark out content to replace a netui-template:includeSection within a template file."
 */
public class Section extends AbstractClassicTag
        implements TemplateConstants
{
    /**
     * The name of the section.
     */
    private String _name;

    /**
     * Is the section visible
     */
    private boolean _visible = true;

    /**
     * Returns the name of the Tag.  This is used to
     * identify the type of tag reporting errors.
     * @return a constant string representing the name of the tag.
     */
    public String getTagName() {
        return "Section";
    }

    /**
     * Sets the name of the placeholder section defined in
     * the template that this tag is providing content for.
     * This name is matched against the <code>IncludeSection</code>
     * name.  If the names match, the content of this tag will be
     * rendered within the template's section.
     * @param name The name of an <code>IncludeSection<code> the
     * this tag is providing content for.
     *
     * @jsptagref.attributedescription
     * The name of the content to fill the placeholder. 
     * This name is matched against the &lt;netui-template:includeSection>
     * name.  If the names match, the content of this tag will be
     * rendered within the template's placeholder.
     * 
     * @jsptagref.databindable false
     * 
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     *
     * @netui:attribute required="true" rtexprvalue="true"
     * description="The name of the content to fill the placeholder."
     */
    public void setName(String name) {
        _name = name;
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
        _visible = visible;
    }

    /**
     * Causes the content of the section to be rendered into a buffer.
     * @return SKIP_BODY if the visible state is <code>false</code>,
     *	otherwise EVAL_BODY_BUFFERED to cause the body content to be buffered.
     * @throws JspException if there are errors.
     */
    public int doStartTag()
            throws JspException {

        if (!_visible)
            return SKIP_BODY;

        // If the parent is a DivPanel then this section will be inlined.
        Tag parentTag = getParent();
        // If the parent tag is a repeater then we must insure that the parent of that is a DivPanel
        if (parentTag instanceof Repeater) {
            parentTag = parentTag.getParent();
            if (!(parentTag instanceof DivPanel)) {
                String s = Bundle.getString("Temp_SectionInRepeater");
                registerTagError(s,null);
                reportErrors();
                localRelease();
                return EVAL_PAGE;
             }
        }
        if (parentTag instanceof DivPanel) {
            InternalStringBuilder results = new InternalStringBuilder(128);
            results.append("<div ");
            renderTagId(results);
            if (hasErrors()) {
                reportErrors();
                localRelease();
                return EVAL_PAGE;
            }
            results.append(">");
            ResponseUtils.write(pageContext,results.toString());
            return EVAL_BODY_BUFFERED;
        }

        return EVAL_BODY_BUFFERED;
    }

    /**
     * Stores the buffered body content into the <code>TEMPLATE_SECTIONS
     * HashMap</code>.  The buffered body is
     * accessed by the template page to obtain
     * the content for <code>IncludeSection</code> tags.
     * @return EVAL_PAGE to continue evaluating the page.
     * @throws JspException on error.
     */
    public int doEndTag()
            throws JspException {

        // If the parent is a DivPanel then this section will be inlined.
        Tag parentTag = getParent();
        if (parentTag instanceof Repeater) {
             parentTag = parentTag.getParent();
        }
         if (parentTag instanceof DivPanel) {
            return processDivPanel();
        }
        assert(parentTag instanceof Template);
        return processTemplate();
    }

    /**
     * Resets all of the fields of the tag.
     */
    protected void localRelease() {
        super.localRelease();
        _name = null;
        _visible = true;
    }

    /**
     * This method will process the section in the context of a DivPanel
     * @return returns the integer code doEndTag() will return.
     */
    private int processDivPanel()
            throws JspException {

        if (!_visible)
            return EVAL_PAGE;

        if (hasErrors()) {
            reportErrors();
            localRelease();
            return EVAL_PAGE;
        }

        BodyContent bc = getBodyContent();
        String content = (bc != null) ? bc.getString() : "";
        ResponseUtils.write(pageContext,content);
        ResponseUtils.write(pageContext,"</div>");
        localRelease();
        return EVAL_PAGE;
    }

    /**
     * This method will process the section in the context of the Template
     * @return returns the integer code doEndTag() will return.
     */
    private int processTemplate()
    {
        ServletRequest req = pageContext.getRequest();
        Template.TemplateContext tc = (Template.TemplateContext)
                req.getAttribute(TEMPLATE_SECTIONS);

        if (tc.secs == null) {
            tc.secs = new HashMap();
        }

        assert (tc.secs != null);

        if (!_visible) {
            // set the section so that it doesn't contain anything
            tc.secs.put(_name,"");
            localRelease();
            return EVAL_PAGE;
        }
        if (hasErrors()) {
            String s = getErrorsReport();
            tc.secs.put(_name,s);
            localRelease();
            return EVAL_PAGE;
        }

        BodyContent bc = getBodyContent();
        String content = (bc != null) ? bc.getString() : "";
        tc.secs.put(_name,content);
        localRelease();
        return EVAL_PAGE;
    }

    // @TODO: this code is duplicated between section and divPanel
    /**
     * This method will handle creating the tagId attribute.  The tagId attribute indentifies the
     * tag in the generated HTML.  There is a lookup table created in JavaScript mapping the <coe>tagId</code>
     * to the actual name.  The tagId is also run through the naming service so it can be scoped.  Some tags will
     * write that <code>tagid</code> out as the <code>id</code> attribute of the HTML tag being generated.
     * @param buffer
     * @return String
     */
    private final String renderTagId(InternalStringBuilder buffer)
        throws JspException {
        assert(_name != null);

        // @todo: this is busted.  It should be writing out inline.
        String realName = rewriteName(_name);
        String idScript = mapLegacyTagId(_name, realName);

        // some tags will output the id attribute themselves so they don't write this out
        renderAttribute(buffer, "id", realName);
        return idScript;
    }

    /**
     * This method will write append an attribute value to a InternalStringBuilder.
     * The method assumes that the attr is not <code>null</code>.  If the
     * value is <code>null</code> the attribute will not be appended to the
     * <code>InternalStringBuilder</code>.
     */
    private void renderAttribute(InternalStringBuilder buf, String name, String value)
    {
        assert (name != null);
        if (value == null)
            return;

        buf.append(" ");
        buf.append(name);
        buf.append("=\"");
        buf.append(value);
        buf.append("\"");
    }

    private String mapLegacyTagId(String tagId, String value)
    {
        // @todo: this is sort of broken, it needs to be updated
        IScriptReporter scriptReporter = getScriptReporter();
        ScriptRequestState srs = ScriptRequestState.getScriptRequestState((HttpServletRequest) pageContext.getRequest());
        String scriptId = srs.mapLegacyTagId(scriptReporter,tagId, value);
        return scriptId;
    }
}
