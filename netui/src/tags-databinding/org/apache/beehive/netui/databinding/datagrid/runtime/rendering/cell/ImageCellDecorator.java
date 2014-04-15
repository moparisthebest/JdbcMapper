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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;

import org.apache.beehive.netui.databinding.datagrid.api.exceptions.CellDecoratorException;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellDecorator;
import org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.ImageCellModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;

/**
 *
 */
public final class ImageCellDecorator
    extends CellDecorator {

    public void decorate(JspContext jspContext, AbstractRenderAppender appender, CellModel cellModel)
            throws CellDecoratorException {

        assert cellModel instanceof ImageCellModel;

        ImageCellModel imageColumnModel = (ImageCellModel)cellModel;
        HttpServletRequest request = JspUtil.getRequest(jspContext);

        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.IMAGE_TAG, request);
        br.doStartTag(appender, imageColumnModel.getImageState());
        br.doEndTag(appender);

        String javascript = imageColumnModel.getJavascript();
        if(javascript != null)
            appender.append(imageColumnModel.getJavascript());

    }
}
