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
package org.apache.beehive.netui.databinding.datagrid.api.rendering;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import java.util.Map;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.databinding.datagrid.api.exceptions.CellDecoratorException;
import org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.tags.rendering.AnchorTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.StringBuilderRenderAppender;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * This is the base class for data grid pager renderers.  Subclasses can be used by the data grid to
 * render custom pager markup overriding one or both of the {@link #internalRender()} and {@link #noDataRender()}
 * methods.  Additional methods are implemented here as a convenience and may be overriden by
 * subclasses needing to change their behavior.
 * </p>
 */
public abstract class PagerRenderer {

    private static final Logger LOGGER = Logger.getInstance(PagerRenderer.class);
    private static final String EMPTY_STRING = "";

    private PagerModel _pagerModel;
    private DataGridTagModel _gridModel;
    private HttpServletRequest _request = null;
    private TagRenderingBase _anchorTag = null;
    private AnchorTag.State _anchorState = new AnchorTag.State();

    /**
     * <p>
     * Set the {@link DataGridTagModel} with which this pager renderer instance is associated.  Pager renderer
     * instances should not be shared between data grids without setting a {@link DataGridTagModel} for
     * each data grid.
     * @param dataGridTagModel the {@link DataGridTagModel}
     */
    public void setDataGridTagModel(DataGridTagModel dataGridTagModel) {
        assert dataGridTagModel != null;

        _gridModel = dataGridTagModel;
        _pagerModel = _gridModel.getState().getPagerModel();
        _request = JspUtil.getRequest(_gridModel.getJspContext());
        _anchorTag = TagRenderingBase.Factory.getRendering(TagRenderingBase.ANCHOR_TAG, _request);
    }

    /**
     * <p>
     * This method should be called when the pager is to be rendered.  The result will be the String that
     * represents the result of rendering.
     * </p>
     * <p>
     * If there is no data in the data set, the method {@link #noDataRender()} will be invoked.  Otherwise,
     * the {@link #internalRender()} method will be called.
     * </p>
     * @return a string containing markup rendered by the pager
     */
    public String render() {
        if(_gridModel.getDataSet().getSize() == 0)
            return noDataRender();
        else
            return internalRender();
    }

    /**
     * Get the {@link PagerModel} that contains the current pager state.
     * @return the {@link PagerModel}
     */
    protected PagerModel getPagerModel() {
        return _pagerModel;
    }

    /**
     * Get the {@link DataGridTagModel} to which this pager is associated.
     * @return the {@link DataGridTagModel}
     */
    protected DataGridTagModel getDataGridTagModel() {
        return _gridModel;
    }

    /**
     * Render the pager.  This method is invoked when there is data available in the data set.
     * @return a string containing markup rendered by the pager
     */
    protected String internalRender() {
        return EMPTY_STRING;
    }

    /**
     * Render the pager.  This method is invoked when there is no data available in the data set.
     * @return a string containing markup rendered by the pager
     */
    protected String noDataRender() {
        return _gridModel.getMessage(IDataGridMessageKeys.DATAGRID_MSG_NODATA);
    }

    /**
     * Build an HTML anchor that contains URL state for navigating to the first page of a data set.
     * @return the HTML markup for anchor to the first page
     */
    protected String buildLiveFirstLink() {
        InternalStringBuilder builder = new InternalStringBuilder();
        AbstractRenderAppender appender = new StringBuilderRenderAppender(builder);

        buildAnchor(appender, _gridModel.getUrlBuilder().getQueryParamsForFirstPage(), IDataGridMessageKeys.PAGER_MSG_FIRST);

        return builder.toString();
    }

    /**
     * Build literal text for the string displayed when there is no first page for the data set.  This
     * text is generally used when the {@link PagerModel} is already on the first page.  By default, this text is
     * obtained using the {@link IDataGridMessageKeys#PAGER_MSG_FIRST} message key.
     * @return the text for the first page link
     */
    protected String buildDeadFirstLink() {
        return _gridModel.getMessage(IDataGridMessageKeys.PAGER_MSG_FIRST);
    }

    /**
     * Build an HTML anchor that contains URL state for navigating to the previous page of a data set.  The
     * previous page is calculated relative to the current location in the {@link PagerModel}
     * @return the HTML markup for anchor to the previous page
     */
    protected String buildLivePreviousLink() {
        InternalStringBuilder builder = new InternalStringBuilder();
        AbstractRenderAppender appender = new StringBuilderRenderAppender(builder);

        buildAnchor(appender, _gridModel.getUrlBuilder().getQueryParamsForPreviousPage(), IDataGridMessageKeys.PAGER_MSG_PREVIOUS);

        return builder.toString();
    }

    /**
     * Build literal text for the string displayed when there is no previous page for the data set.  This
     * text is generally used when the {@link PagerModel} is on the first page.  By default, this text is
     * obtained using the {@link IDataGridMessageKeys#PAGER_MSG_PREVIOUS} message key.
     * @return the text for the previous page link
     */
    protected String buildDeadPreviousLink() {
        return _gridModel.getMessage(IDataGridMessageKeys.PAGER_MSG_PREVIOUS);
    }

    /**
     * Build an HTML anchor that contains URL state for navigating to the next page of a data set.  The
     * next page is calculated relative to the current location in the {@link PagerModel}
     * @return the HTML markup for anchor to the next page
     */
    protected String buildLiveNextPageLink() {
        InternalStringBuilder builder = new InternalStringBuilder();
        AbstractRenderAppender appender = new StringBuilderRenderAppender(builder);
        buildAnchor(appender, _gridModel.getUrlBuilder().getQueryParamsForNextPage(), IDataGridMessageKeys.PAGER_MSG_NEXT);
        return builder.toString();
    }

    /**
     * Build literal text for the string displayed when there is no next page for the data set.  This
     * text is generally used when the {@link PagerModel} is on the last page.  By default, this text is
     * obtained using the {@link IDataGridMessageKeys#PAGER_MSG_NEXT} message key.
     * @return the text for the next page link
     */
    protected String buildDeadNextLink() {
        return _gridModel.getMessage(IDataGridMessageKeys.PAGER_MSG_NEXT);
    }

    /**
     * Build an HTML anchor that contains URL state for navigating to the last page of a data set.
     * @return the HTML markup for anchor to the last page
     */
    protected String buildLiveLastLink() {
        InternalStringBuilder builder = new InternalStringBuilder();
        AbstractRenderAppender appender = new StringBuilderRenderAppender(builder);
        buildAnchor(appender, _gridModel.getUrlBuilder().getQueryParamsForLastPage(), IDataGridMessageKeys.PAGER_MSG_LAST);
        return builder.toString();
    }

    /**
     * Build literal text for the string displayed when there is no last page for the data set.  This
     * text is generally used when the {@link PagerModel} is on the last page.  By default, this text is
     * obtained using the {@link IDataGridMessageKeys#PAGER_MSG_LAST} message key.
     * @return the text for the last page link
     */
    protected String buildDeadLastLink() {
        return _gridModel.getMessage(IDataGridMessageKeys.PAGER_MSG_LAST);
    }

    /**
     * Build the anchor
     * @param appender
     * @param queryParams
     * @param labelKey
     */
    protected final void buildAnchor(AbstractRenderAppender appender, Map queryParams, String labelKey) {
        assert appender != null;
        assert queryParams != null;
        assert labelKey != null && labelKey.length() > 0;

        _anchorState.href = buildPageUri(queryParams);
        _anchorTag.doStartTag(appender, _anchorState);
        appender.append(_gridModel.getMessage(labelKey));
        _anchorTag.doEndTag(appender);
        _anchorState.clear();
    }

    protected String buildPageUri(Map queryParams) {
        String uri = null;

        /*
           if there isn't a defined page href, set one here.
           note, this defaults to the curernt request URI.  In the absence of
           anything else, it's hard (impossible?) to determine if this is an
           action, so we default to something reasonable which is a request URI.
         */
        String href = _pagerModel.getPageHref() != null ? _pagerModel.getPageHref() : _request.getRequestURI();

        try {
            uri = JspUtil.createURL(href,
                    _pagerModel.getPageAction(),
                    null,
                    null,
                    queryParams,
                    _gridModel.getJspContext());
        }
        catch(URISyntaxException mue) {
            String message = Bundle.getErrorString("Rendering_URLException", new Object[]{_pagerModel.getPageHref(), _pagerModel.getPageAction()});
            LOGGER.error(message, mue);
            throw new CellDecoratorException(message, mue);
        }

        return uri;
    }

}
