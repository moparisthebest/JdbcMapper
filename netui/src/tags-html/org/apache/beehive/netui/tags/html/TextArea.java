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
package org.apache.beehive.netui.tags.html;

import org.apache.beehive.netui.tags.ByRef;
import org.apache.beehive.netui.tags.HtmlUtils;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.TextAreaTag;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import java.util.ArrayList;

/**
 * Renders a databound TextArea with the given attributes.
 * @jsptagref.tagdescription Renders an HTML &lt;textarea> tag.
 *
 * <pre>     &lt;textarea/></pre>
 * @example In this sample a text area reads from and writes to the Form Bean's
 * description property.  If the submitted value is NULL the default value is specified
 * by the Page Flow's
 * defaultDescription property.
 * <pre>     &lt;netui:textArea dataSource="actionForm.description"
 *           defaultValue="${pageFlow.defaultDescription}"
 *           cols="25" rows="3" /></pre>
 * @netui:tag name="textArea" description="Renders a databound TextArea with the given attributes."
 */
public class TextArea extends HtmlDefaultableDataSourceTag
        implements IFormattable
{
    private TextAreaTag.State _state = new TextAreaTag.State();

    private ArrayList _formatters = null;
    private boolean _formatErrors = false;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "TextArea";
    }

    /**
     * This method will return the state associated with the tag.  This is used by this
     * base class to access the individual state objects created by the tags.
     * @return a subclass of the <code>AbstractHtmlState</code> class.
     */
    protected AbstractHtmlState getState()
    {
        return _state;
    }

    /**
     * Base support for the attribute tag.
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.  This may contain an expression.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     * @throws JspException A JspException may be thrown if there is an error setting the attribute.
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException
    {
        if (name != null) {
            if (name.equals(DISABLED)) {
                setDisabled(Boolean.parseBoolean(value));
                return;
            }
            else if (name.equals(READONLY)) {
                _state.readonly = new Boolean(value).booleanValue();
                return;
            }
            else if (name.equals(COLS)) {
                _state.cols = Integer.parseInt(value);
                return;
            }
            else if (name.equals(ROWS)) {
                _state.rows = Integer.parseInt(value);
                return;
            }
        }

        super.setAttribute(name, value, facet);
    }

    /**
     * Sets the number of columns in the TextArea.
     * @param cols the number of columns
     * @jsptagref.attributedescription Integer. The number of columns in the &lt;netui:textArea>.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>integer_columns</i>
     * @netui:attribute required="false" rtexprvalue="true" type="int"
     * description="The number of columns in the <netui:textArea>."
     */
    public void setCols(int cols)
    {
        _state.cols = cols;
    }

    /**
     * Set if this TextArea is read-only.
     * @param readonly the read-only state
     * @jsptagref.attributedescription Boolean. Determines whether text can be entered in the &lt;netui:textArea> or not.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_disabled</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Determines whether text can be entered in the <netui:textArea> or not."
     */
    public void setReadonly(boolean readonly)
    {
        _state.readonly = readonly;
    }

    /**
     * Sets the number of rows in the TextArea.
     * @param rows the number of rows
     * @jsptagref.attributedescription The number of rows in the &lt;netui:textArea>
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>integer_rows</i>
     * @netui:attribute required="false" rtexprvalue="true" type="int"
     * description="The number of rows in the <netui:textArea>"
     */
    public void setRows(int rows)
    {
        _state.rows = rows;
    }

    /**
     * Prepare the TextArea's formatters.
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Render the TextArea.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException
    {
        ServletRequest req = pageContext.getRequest();
        Object textObject = null;

        // Get the value of the data source.  The object will not be null.
        Object val = evaluateDataSource();
        textObject = (val != null) ? val : "";
        assert(textObject != null);

        // setup the rest of the state.
        ByRef ref = new ByRef();
        nameHtmlControl(_state, ref);

        _state.disabled = isDisabled();

        // if there were expression errors report them
        if (hasErrors())
            return reportAndExit(EVAL_PAGE);

        // if there were format errors then report them
        if (_formatErrors) {
            if (bodyContent != null) {
                String value = bodyContent.getString().trim();
                bodyContent.clearBody();
                write(value);
            }
        }

        // create the input tag.
        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.TEXT_AREA_TAG, req);
        br.doStartTag(writer, _state);

        // create the text value which will be found inside the textarea.
        if ((textObject == null) || (textObject.toString().length() == 0)) {
            textObject = _defaultValue;
        }

        String text = formatText(textObject);
        if (text != null) {
            // make sure leading blank line in the text value is not interpreted
            // just as formatting... force a new line for formatting
            if (text.startsWith("\n") || text.startsWith("\r")) {
                writer.append("\n");
            }
            HtmlUtils.filter(text, writer);
        }

        //results.append(text);
        br.doEndTag(writer);

        // if there are errors report them...
        if (hasErrors())
            return reportAndExit(EVAL_PAGE);

        // report any script
        if (!ref.isNull())
            write((String) ref.getRef());

        localRelease();
        return EVAL_PAGE;
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();

        _state.clear();
        _formatters = null;
        _formatErrors = false;
    }

    /**
     * Adds a FormatTag.Formatter to the TextArea's set of formatters
     * @param formatter a FormatTag.Formatter added by a child FormatTag.
     */
    public void addFormatter(FormatTag.Formatter formatter)
    {
        if (_formatters == null)
            _formatters = new ArrayList();

        _formatters.add(formatter);
    }

    /**
     * Indicate that a formatter has reported an error so the formatter should output it's
     * body text.
     */
    public void formatterHasError()
    {
        _formatErrors = true;
    }

    /**
     */
    private String formatText(Object text)
            throws JspException
    {
        if (text == null)
            return null;
        if (_formatters == null)
            return text.toString();

        for (int i = 0; i < _formatters.size(); i++) {
            FormatTag.Formatter currentFormatter = (FormatTag.Formatter) _formatters.get(i);
            try {
                text = currentFormatter.format(text);
            }
            catch (JspException e) {
                registerTagError(e.getMessage(), null);
            }
        }
        return text.toString();
    }

    /* ==================================================================
     *
     * This tag's publically exposed HTML, CSS, and JavaScript attributes
     *
     * ==================================================================
     */

    /**
     * Sets the accessKey attribute value.  This should key value of the
     * keyboard navigation key.  It is recommended not to use the following
     * values because there are often used by browsers <code>A, C, E, F, G,
     * H, V, left arrow, and right arrow</code>.
     * @param accessKey the accessKey value.
     * @jsptagref.attributedescription The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: <code>A, C, E, F, G,
     * H, V, left arrow, and right arrow</code>
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_accessKey</i>
     * @netui:attribute required="false" rtexprvalue="true" type="char"
     * description="The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: A, C, E, F, G,
     * H, V, left arrow, and right arrow"
     */
    public void setAccessKey(char accessKey)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, "accesskey", Character.toString(accessKey));
    }

    /**
     * Sets the tabIndex of the rendered html tag.
     * @param tabindex the tab index.
     * @jsptagref.attributedescription The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * tag in the sequence of page elements that the user may advance through by pressing the TAB key.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_tabIndex</i>
     * @netui:attribute required="false" rtexprvalue="true" type="int"
     * description="The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * tag in the sequence of page elements that the user may advance through by pressing the TAB key."
     */
    public void setTabindex(int tabindex)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TABINDEX, Integer.toString(tabindex));
    }
}
