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
package org.apache.beehive.netui.databinding.datagrid.runtime.rendering.cell;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import java.util.Map;
import java.net.URISyntaxException;
import javax.servlet.jsp.JspContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.databinding.datagrid.api.exceptions.CellDecoratorException;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortDirection;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellDecorator;
import org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.HeaderCellModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.StringBuilderRenderAppender;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.AnchorTag;
import org.apache.beehive.netui.tags.rendering.ImageTag;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.Bundle;

/**
 *
 */
public final class SortedCellDecorator
    extends CellDecorator {

    private static final Logger LOGGER = Logger.getInstance(SortedCellDecorator.class);

    public SortedCellDecorator(CellDecorator decorator) {
        super(decorator);
    }

    public void decorate(JspContext jspContext, AbstractRenderAppender appender, CellModel cellModel)
            throws CellDecoratorException {

        assert cellModel instanceof HeaderCellModel;
        HeaderCellModel headerCellModel = (HeaderCellModel)cellModel;

        assert getNestedDecorator() != null : "SortedCellDecorator did not find a nested decorator and requires one";

        getNestedDecorator().decorate(jspContext, appender, cellModel);

        if(headerCellModel.isSortable()) {
            String sortLink = buildSortLink(jspContext, headerCellModel);
            appender.append("&nbsp;");
            appender.append(sortLink);
        }
    }

    protected String buildSortLink(JspContext jspContext, HeaderCellModel cellModel) {
        HttpServletRequest request = JspUtil.getRequest(jspContext);
        DataGridTagModel dgm = cellModel.getDataGridTagModel();
        assert dgm != null;

        SortModel sortModel = dgm.getState().getSortModel();

        InternalStringBuilder builder = new InternalStringBuilder();
        AbstractRenderAppender appender = new StringBuilderRenderAppender(builder);

        TagRenderingBase imageRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.IMAGE_TAG, request);
        TagRenderingBase anchorRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.ANCHOR_TAG, request);

        AnchorTag.State aTag = new AnchorTag.State();
        ImageTag.State imgTag = new ImageTag.State();

        SortDirection sortDirection = sortModel.getSortDirection(cellModel.getSortExpression());

        /* build icon for existing sort */
        if(sortModel.isSorted(cellModel.getSortExpression()))
            imgTag.src = dgm.getResourcePath() + dgm.getSortImagePath(sortDirection);
        else
            imgTag.src = dgm.getResourcePath() + dgm.getDefaultSortImagePath();

        imgTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.BORDER, "false");

        Map queryParams = dgm.getUrlBuilder().buildSortQueryParamsMap(cellModel.getSortExpression());

        String href = null;
        try {
            href = JspUtil.createURL(cellModel.getSortHref(),
                    cellModel.getSortAction(),
                    null,
                    cellModel.getScopeId(),
                    queryParams,
                    dgm.getJspContext());
        }
        catch(URISyntaxException use) {
            String message = Bundle.getErrorString("Rendering_URLException", new Object[]{cellModel.getSortHref(), cellModel.getSortAction()});
            if(LOGGER.isErrorEnabled())
                LOGGER.error(message, use);
            throw new CellDecoratorException(message, use);
        }

        aTag.href = href;

        anchorRenderer.doStartTag(appender, aTag);
        imageRenderer.doStartTag(appender, imgTag);
        imageRenderer.doEndTag(appender);
        anchorRenderer.doEndTag(appender);

        return builder.toString();
    }
}
