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

import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.tags.ByRef;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.InputFileTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Renders an input tag with type="file", with the given attributes.  Usage of this tag requires the parent
 * Form tag to have enctype="multipart/form-data".
 *
 *
 *
 * <p><b>Notes</b></p>
 * <ul>
 * <li>The dataSource for the FileUpload tag is write-only. This means that the value of the FileUpload
 * cannot get read back into the FileUpload on postback.</li>
 * <li>The page flow or form bean property pointed to with the FileUpload's dataSource
 * should be of type <code>org.apache.struts.upload.FormFile</code>.</li>
 * </ul>
 * @jsptagref.tagdescription Renders an HTML input tag with which users can browse, select, and upload files
 * from their local machine.
 *
 * <p>To successfully upload files, the following two conditions must be met:
 *
 * <blockquote>
 * <ul>
 * <li>The &lt;netui:fileUpload> tag must have a parent
 * {@link Form} tag with the attribute <code>enctype="multipart/form-data"</code>
 *
 * <pre>     &lt;netui:form action="uploadFile" <b>enctype="multipart/form-data"</b>>
 *     ...
 *         &lt;netui:fileUpload tagId="inputbox" dataSource="actionForm.theFile" />
 *     ...
 *     &lt;/netui:form></pre>
 *
 * </li>
 * <li>The <code>dataSource</code>
 * attribute must point to a Form Bean field of
 * type <code>org.apache.struts.upload.FormFile</code>.
 *
 * <pre>    public static class UploadFileForm extends FormData
 *    {
 *        private <b>org.apache.struts.upload.FormFile</b> theFile;
 *
 *        public void setTheFile(org.apache.struts.upload.FormFile theFile)
 *        {
 *            this.theFile = theFile;
 *        }
 *
 *        public org.apache.struts.upload.FormFile getTheFile()
 *        {
 *            return this.theFile;
 *        }
 *    }</pre>
 * </li>
 * </ul>
 * </blockquote>
 *
 * The <code>dataSource</code> attribute for the &lt;netui:fileUpload> tag is write-only.
 * This means that the value of the &lt;netui:fileUpload> tag
 * cannot be read back into the tag on postback.
 * @example In the following example, note the <code>enctype</code> attribute of the
 * enclosing &lt;netui:form> tag.
 * <pre>    &lt;netui:form action="uploadFile" enctype="multipart/form-data">
 *        &lt;netui:fileUpload tagId="inputbox" dataSource="actionForm.theFile" />
 *        &lt;netui:button value="Upload File" />
 *    &lt;/netui:form></pre>
 *
 * Note that the &lt;netui:fileUpload> tag submits the file to Form Bean property <code>theFile</code>.  This
 * property must be of type <code>org.apache.struts.upload.FormFile</code>:
 *
 * <pre>    public static class UploadFileForm extends FormData
 *    {
 *        private <b>org.apache.struts.upload.FormFile</b> theFile;
 *
 *        public void setTheFile(<b>org.apache.struts.upload.FormFile</b> theFile)
 *        {
 *            this.theFile = theFile;
 *        }
 *
 *        public <b>org.apache.struts.upload.FormFile</b> getTheFile()
 *        {
 *            return this.theFile;
 *        }
 *    }</pre>
 * @netui:tag name="fileUpload" description="Upload a file from the client to the server."
 */
public class FileUpload extends HtmlDataSourceTag
{
    private InputFileTag.State _state = new InputFileTag.State();

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "FileUpload";
    }

    /**
     * Base support for the attribute tag.  This is overridden to prevent setting the <code>type</code>
     * attribute.
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.  This may contain an expression.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     * @throws JspException A JspException may be thrown if there is an error setting the attribute.
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException
    {
        if (name != null) {
            if (name.equals(TYPE)) {
                String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
                registerTagError(s, null);
            }
            else if (name.equals(READONLY)) {
                _state.readonly = Boolean.valueOf(value).booleanValue();
                return;
            }
        }
        super.setAttribute(name, value, facet);
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
     * Set the mime-types accepted for the FileUpload.
     * @param accept the accepted mime-types
     * @jsptagref.attributedescription The set of MIME types accepted for file upload.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_acceptTheseMIMETypes</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The set of MIME types accepted for file upload."
     */
    public void setAccept(String accept)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ACCEPT, accept);
    }

    /**
     * Set if this FileUpload is read-only.
     * @param readonly the read-only state
     * @jsptagref.attributedescription Boolean. Determines whether or not the file upload field is read-only.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_readOnly</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Determines whether or not the file upload field is read-only."
     */
    public void setReadonly(boolean readonly)
    {
        _state.readonly = readonly;
    }

    /**
     * Set the size (in characters) of the FileUpload.
     * @param size the size
     * @jsptagref.attributedescription Integer. The number of characters visible in the file upload field.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>integer_fieldSize</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The number of characters visible in the file upload field."
     */
    public void setSize(String size)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, SIZE, size);
    }

    /**
     * Sets the tabIndex of the rendered html tag.
     * @param tabindex the tab index.
     * @jsptagref.attributedescription The tabIndex of the rendered HTML tag.  This attribute determines the position
     * of the rendered HTML tag in the sequence of tags that the user may advance through by pressing the TAB key.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_tabIndex</i>
     * @netui:attribute required="false"  rtexprvalue="true" type="int"
     * description="The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * rendered HTML tag in the sequence of tags that the user may advance through by pressing the TAB key."
     */
    public void setTabindex(int tabindex)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TABINDEX, Integer.toString(tabindex));
    }

    /**
     * Render the fileUpLoad tag.  Cause the body to be buffered to look
     * for contained tags.
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Render the FileUpload.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException
    {
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();

        // if we are not handling multipart requests then we can't output the file upload tag and we will
        // report an error
        if (!InternalUtils.isMultipartHandlingEnabled(req)) {
            String s = Bundle.getString("Tags_FileMultiOff", null);
            registerTagError(s, null);
            return EVAL_PAGE;
        }

        // Create the state for the input tag.
        ByRef ref = new ByRef();
        nameHtmlControl(_state, ref);

        if (hasErrors())
            return reportAndExit(EVAL_PAGE);

        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_FILE_TAG, req);
        br.doStartTag(writer, _state);

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
    }
}
