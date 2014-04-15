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

import java.net.URISyntaxException;
import javax.servlet.jsp.JspContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.AnchorTag;
import org.apache.beehive.netui.tags.rendering.ImageTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.databinding.datagrid.api.exceptions.CellDecoratorException;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellDecorator;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.ImageAnchorCellModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.Bundle;

/**
 */
public final class ImageAnchorCellDecorator
    extends CellDecorator {

    private static final Logger LOGGER = Logger.getInstance(ImageAnchorCellDecorator.class);

    public void decorate(JspContext jspContext, AbstractRenderAppender appender, CellModel cellModel)
            throws CellDecoratorException {
        HttpServletRequest request = JspUtil.getRequest(jspContext);

        assert cellModel instanceof ImageAnchorCellModel;
        ImageAnchorCellModel imageAnchorCellModel = (ImageAnchorCellModel)cellModel;

        AnchorTag.State anchorState = imageAnchorCellModel.getAnchorState();
        ImageTag.State imageState = imageAnchorCellModel.getImageState();

        DataGridTagModel dgm = cellModel.getDataGridTagModel();
        assert dgm != null;

        String url = null;
        try {
            url = JspUtil.createURL(imageAnchorCellModel.getHref(),
                    imageAnchorCellModel.getAction(),
                    null,
                    imageAnchorCellModel.getScopeId(),
                    imageAnchorCellModel.getParams(),
                    jspContext);
        }
        catch(URISyntaxException use) {
            String message = Bundle.getErrorString("Rendering_URLException", new Object[]{imageAnchorCellModel.getHref(), imageAnchorCellModel.getAction()});
            LOGGER.error(message, use);
            throw new CellDecoratorException(message, use);
        }

        anchorState.href = url;

        TagRenderingBase imageTag = TagRenderingBase.Factory.getRendering(TagRenderingBase.IMAGE_TAG, request);
        TagRenderingBase anchorTag = TagRenderingBase.Factory.getRendering(TagRenderingBase.ANCHOR_TAG, request);

        anchorTag.doStartTag(appender, anchorState);
        imageTag.doStartTag(appender, imageState);
        imageTag.doEndTag(appender);
        anchorTag.doEndTag(appender);

        String script = imageAnchorCellModel.getJavascript();
        if(script != null)
            appender.append(script);
    }
}
