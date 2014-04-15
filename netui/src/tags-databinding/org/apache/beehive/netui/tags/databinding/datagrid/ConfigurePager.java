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
package org.apache.beehive.netui.tags.databinding.datagrid;

import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.databinding.datagrid.api.exceptions.DataGridExtensionException;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.PagerRenderer;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.pager.FirstPreviousNextLastPagerRenderer;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.pager.PreviousNextPagerRenderer;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.ExtensionUtil;
import org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel;
import org.apache.beehive.netui.tags.AbstractSimpleTag;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * The configurePager tag is used to parameterize the data used to render a data grid's pager.  This
 * tag should be used inside of a &lt;netui-data:dataGrid&gt; tag.  The tag supports adding a custom
 * {@link PagerRenderer} via the {@link #setPagerRendererClass(String)}, overriding the default page
 * size for a data grid via the {@link #setDefaultPageSize(Integer)} attribute, and setting
 * the current data grid's page size via the {@link #setPageSize(int)} attribute.  This tag produces
 * no output and does not evaluate its body.
 * </p>
 * @jsptagref.tagdescription
 * <p>
 * The configurePager tag is used to parameterize the data used to render a data grid's pager.  This
 * tag should be used inside of a &lt;netui-data:dataGrid&gt; tag.  The tag supports adding a custom
 * {@link PagerRenderer} via the {@link #setPagerRendererClass(String)}, overriding the default page
 * size for a data grid via the {@link #setDefaultPageSize(Integer)} attribute, and setting
 * the current data grid's page size via the {@link #setPageSize(int)} attribute.  This tag produces
 * no output and does not evaluate its body.
 * </p>
 * @netui:tag name="configurePager" body-content="empty"
 * description="Pager tag for the configuring and rendering the pager rendered in the NetUI data grid"
 */
public class ConfigurePager
        extends AbstractSimpleTag {

    private static final Logger LOGGER = Logger.getInstance(ConfigurePager.class);
    private static final String PAGER_FORMAT_FIRST_LAST_PREV_NEXT = "firstPrevNextLast";
    private static final String PAGER_FORMAT_PREV_NEXT = "prevNext";

    private Boolean _disableDefaultPager = null;
    private Integer _pageSize = null;
    private Integer _defaultPageSize = null;
    private String _pagerFormat = null;
    private String _pageHref = null;
    private String _pageAction = null;
    private String _pagerRendererClass = null;

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public String getTagName() {
        return "ConfigurePager";
    }

    /**
     * Set the page size for the current data grid.  This value is the maximum number of data rows
     * that will be rendered by a data grid.  When this value is set, it overides the
     * {@link #setDefaultPageSize(Integer)} attribute.  If this value is not set, the
     * {@link #setDefaultPageSize(Integer)} is used to determine the maximum size of a data grid's
     * page.  Use this value when a data grid allows a user to vary the number of rows in a grid.
     * @jsptagref.attributedescription
     * Set the page size for the current data grid.  This value is the maximum number of data rows
     * that will be rendered by a data grid.  When this value is set, it overides the
     * {@link #setDefaultPageSize(Integer)} attribute.  If this value is not set, the
     * {@link #setDefaultPageSize(Integer)} is used to determine the maximum size of a data grid's
     * page.  Use this value when a data grid allows a user to vary the number of rows in a grid.
     * @jsptagref.attributesyntaxvalue <i>int_pageSize</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="Set the maximum number of rows to render for a data grid."
     */
    public void setPageSize(int pageSize) {
        _pageSize = new Integer(pageSize);
    }

    /**
     * Set the appearance of the pager rendered by the data grid.  This attribute allows the page author to
     * choose from a set of pre-defined pager renderers which can be used to render paging UI
     * in a data grid.  The values include:
     * <b>prevNext</b> which renders a pager as:<br/>
     * <pre>
     *   Page # of # Previous Next
     * </pre>
     * where Previous and Next are anchors that can navigate to the previous and next pages.  When
     * there is no previous or next page, Previous or Next are displayed as literal text.
     * <br/>
     * <br/>
     * <b>firstPrevNextLast</b> which renders a pager as:<br/>
     * <pre>
     *   Page # of # First Previous Next Last
     * </pre>
     * where First, Previous, Next, and Last are anchors that can navigate to the first, previous, next, and last
     * pages respectively.  When the anchors would reference invalid pages, First, Previous, Nest, and Last
     * are displayed as literal text.
     * @jsptagref.attributedescription
     * Set the style of the pager rendered by the data grid.  This attribute allows the page author to
     * choose from a set of pre-defined pager renderers which can be used to render paging UI
     * in a data grid.  The values include:
     * <b>prevNext</b> which renders a pager as:<br/>
     * <pre>
     *   Page # of # Previous Next
     * </pre>
     * where Previous and Next are anchors that can navigate to the previous and next pages.  When
     * there is no previous or next page, Previous or Next are displayed as literal text.
     * <br/>
     * <br/>
     * <b>firstPrevNextLast</b> which renders a pager as:<br/>
     * <pre>
     *   Page # of # First Previous Next Last
     * </pre>
     * where First, Previous, Next, and Last are anchors that can navigate to the first, previous, next, and last
     * pages respectively.  When the anchors would reference invalid pages, First, Previous, Nest, and Last
     * are displayed as literal text.
     * @jsptagref.attributesyntaxvalue <i>String_pagerStyle</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="Set the appearance of the pager rendered by the data grid.
     */
    public void setPagerFormat(String pagerFormat) {
        _pagerFormat = pagerFormat;
    }

    /**
     * Set the HREF used to perform paging.  When a pager renders anchors for navigating the data set
     * inside of the data grid, this HREF when set is used as the request URI to perform the paging.
     * Only one of this and the {@link #setPageAction(String)} may be set.
     * @jsptagref.attributedescription
     * Set the HREF used to perform paging.  When a pager renders anchors for navigating the data set
     * inside of the data grid, this HREF when set is used as the request URI to perform the paging.
     * Only one of this and the {@link #setPageAction(String)} may be set.
     * @jsptagref.attributesyntaxvalue <i>String_pageUri</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The HREF used when building HTML
     * anchors that perform paging."
     */
    public void setPageHref(String pageHref) {
        _pageHref = pageHref;
    }

    /**
     * Set the action used to perform paging.  When a pager renders anchors for navigating the
     * data set in a data grid, this action is used as the request URI to perform the paging.
     * Only one of this and the {@link #setPageHref(String)} may be set.
     * @jsptagref.attributedescription
     * Set the action used to perform paging.  When a pager renders anchors for navigating the
     * data set in a data grid, this action is used as the request URI to perform the paging.
     * Only one of this and the {@link #setPageHref(String)} may be set.
     * @jsptagref.attributesyntaxvalue <i>String_pageAction</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The action used when building HTML anchors that perrform paging.
     */
    public void setPageAction(String pageAction) {
        _pageAction = pageAction;
    }

    /**
     * Sets the value of an attribute that enables or disables rendering the data grid's default pager.
     * When this value is set to <code>false</code>, the data grid will not render its pager and rendering
     * is left to the page author.  When rendering is disabled, a pager can be rendered using the
     * {@link RenderPager} tag or manually in a page.
     * @jsptagref.attributedescription
     * Sets the value of an attribute that enables or disables rendering the data grid's default pager.
     * When this value is set to <code>false</code>, the data grid will not render its pager and rendering
     * is left to the page author.  When rendering is disabled, a pager can be rendered using the
     * {@link RenderPager} tag or manually in a page.
     * @jsptagref.attributesyntaxvalue <i>boolean</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="Boolean that enables / disables a data grid rendering the pager in its default location"
     */
    public void setDisableDefaultPager(boolean disableDefaultPager) {
        _disableDefaultPager = Boolean.valueOf(disableDefaultPager);
    }

    /**
     * Set a Java class name to create a {@link PagerRenderer} used to render a data grid's pager.  The
     * class referenced by this name must extend the {@link PagerRenderer} base class.
     * @jsptagref.attributedescription
     * Set a Java class name to create a {@link PagerRenderer} used to render a data grid's pager.  The
     * class referenced by this name must extend the {@link PagerRenderer} base class.
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The data grid pager's custom pager render classname"
     */
    public void setPagerRendererClass(String pagerRendererClass) {
        _pagerRendererClass = pagerRendererClass;
    }

    /**
     * Set the default page size for the data grid.  The grid has a default page size that is used
     * when no other page size is specified; this value is {@link PagerModel#DEFAULT_PAGE_SIZE}.
     * This value overrides that default so that the normal rendering of such a data grid is
     * change in the absence of an override provided by the {@link #setPageSize(int)} attribute.
     *
     * @jsptagref.attributedescription
     * Set the default page size for the data grid.  The grid has a default page size that is used
     * when no other page size is specified; this value is {@link PagerModel#DEFAULT_PAGE_SIZE}.
     * This value overrides that default so that the normal rendering of such a data grid is
     * change in the absence of an override provided by the {@link #setPageSize(int)} attribute.
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The data grid pager's default page size"
     */
    public void setDefaultPageSize(Integer defaultPageSize) {
        _defaultPageSize = defaultPageSize;
    }

    /**
     * Execute the ConfigurePager tag.  When the ConfigurePager tag runs, it applies its tag attributes
     * onto a {@link DataGridTagModel} state object which is used when the data grid renders its pager.
     * @throws JspException when errors occur when processing this tag's attribute values
     */
    public void doTag()
        throws JspException {

        DataGridTagModel dgm = DataGridUtil.getDataGridTagModel(getJspContext());
        if(dgm == null)
            throw new JspException(Bundle.getErrorString("DataGridTags_MissingDataGridModel"));

        PagerModel pm = dgm.getState().getPagerModel();
        assert pm != null;

        if(_disableDefaultPager != null)
            dgm.setDisableDefaultPagerRendering(_disableDefaultPager.booleanValue());

        if(_pageSize != null)
            pm.setPageSize(_pageSize.intValue());

        if(_defaultPageSize != null)
            pm.setDefaultPageSize(_defaultPageSize.intValue());

        PagerRenderer pagerRenderer = null;
        if(_pagerRendererClass != null) {
            try {
                pagerRenderer = (PagerRenderer)ExtensionUtil.instantiateClass(_pagerRendererClass, PagerRenderer.class);
                assert pagerRenderer != null : "Expected a non-null pager renderer of type \"" + _pagerRendererClass + "\"";
            }
            catch(DataGridExtensionException e) {
                String msg = Bundle.getErrorString("ConfigurePager_CantCreateCustomPagerRenderer", new Object[]{e});
                JspException jsp = new JspException(msg, e);

                // todo: future cleanup
                // The 2.5 Servlet api will set the initCause in the Throwable superclass during construction,
                // this will cause an IllegalStateException on the following call.
                if (jsp.getCause() == null) {
                    jsp.initCause(e);
                }
                throw jsp;
            }
        }
        else if(_pagerFormat != null) {
            if(_pagerFormat.equals(PAGER_FORMAT_FIRST_LAST_PREV_NEXT))
                pagerRenderer = new FirstPreviousNextLastPagerRenderer();
            else if(_pagerFormat.equals(PAGER_FORMAT_PREV_NEXT))
                pagerRenderer = new PreviousNextPagerRenderer();
            else
                throw new JspException(Bundle.getErrorString("ConfigurePager_UnsupportedPagerFormat", new Object[]{_pagerFormat}));
        }

        if(pagerRenderer != null)
            dgm.setPagerRenderer(pagerRenderer);

        if(_pageHref != null && _pageAction != null)
            throw new JspException(Bundle.getErrorString("ConfigurePager_CantSetHrefAndAction"));

        if(_pageHref == null && _pageAction == null) {
            LOGGER.info("The configurePager tag has no page action or HREF specified; using the current request URI instead.");
            _pageHref = JspUtil.getRequest(getJspContext()).getRequestURI();
        }

        if(_pageHref != null)
            pm.setPageHref(_pageHref);

        if(_pageAction != null)
            pm.setPageAction(_pageAction);
    }
}

