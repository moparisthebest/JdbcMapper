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

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.tags.internal.ReturnActionViewRenderer;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;


/**
 * Causes a value to be retrieved when a popup window closes.
 * @jsptagref.tagdescription Causes a value to be retrieved when a popup window closes.
 * @example <pre>
 *     &lt;netui:anchor action="getCityZipFromNestedPageFlow" popup="true">
 *         Get a city and zip code
 *         &lt;netui:configurePopup resizable="false" width="400" height="200">
 *             &lt;netui:retrievePopupOutput tagIdRef="zipCodeField" dataSource="outputFormBean.zipCode" /&gt;
 *             &lt;netui:retrievePopupOutput tagIdRef="cityField" dataSource="outputFormBean.city" /&gt;
 *         &lt;/netui:configurePopup>
 *     &lt;/netui:anchor></pre>
 * @netui:tag name="retrievePopupOutput" description="Causes a value to be retrieved when a popup window closes."
 */
public class RetrievePopupOutput
        extends AbstractClassicTag
{
    private String _tagIdRef = null;
    private String _dataSource = null;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "RetrievePopupOutput";
    }

    /**
     * Sets the ID of the form field to populate with a popup output.
     * @param tagIdRef the ID of the form field to populate with a popup output.
     * @jsptagref.attributedescription The ID of the form field to populate with a popup output.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_tagidRef</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="The ID of the form field to populate with a popup output."
     */
    public void setTagIdRef(String tagIdRef)
    {
        _tagIdRef = tagIdRef;
    }

    /**
     * Sets an expression to be evaluated and retrieved from the popup window.
     * @param dataSource an expression to be evaluated and retrieved from the popup window.
     * @jsptagref.attributedescription An expression to be evaluated and retrieved from the popup window.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_dataSource</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="An expression to be evaluated and retrieved from the popup window."
     */
    public void setDataSource(String dataSource)
    {
        _dataSource = dataSource;
    }

    /**
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        JspTag parentTag = SimpleTagSupport.findAncestorWithClass(this, ConfigurePopup.class);
        if (parentTag == null) {
            String msg = Bundle.getString("Tags_InvalidRetrievePopupOutputParent");
            registerTagError(msg, null);
            reportErrors();
        }
        else {
            JspTag parentParent = SimpleTagSupport.findAncestorWithClass(this, IUrlParams.class);
            if (parentTag != null)  // there will already be an error on ConfigurePopup if there is no URLParams parent.
            {
                IUrlParams urlParams = (IUrlParams) parentParent;
                urlParams.addParameter(ReturnActionViewRenderer.getMapItemParamName(),
                        _dataSource + ReturnActionViewRenderer.getDelim() + getIdForTagId(_tagIdRef),
                        null);
            }
        }
        localRelease();
        return SKIP_BODY;
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();

        _dataSource = null;
        _tagIdRef = null;
    }
}
