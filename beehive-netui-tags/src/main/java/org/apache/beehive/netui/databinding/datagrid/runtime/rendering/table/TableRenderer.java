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
package org.apache.beehive.netui.databinding.datagrid.runtime.rendering.table;

import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.tags.rendering.TableTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.TrTag;
import org.apache.beehive.netui.tags.rendering.TdTag;
import org.apache.beehive.netui.tags.rendering.ThTag;
import org.apache.beehive.netui.tags.rendering.CaptionTag;
import org.apache.beehive.netui.tags.rendering.THeadTag;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.TFootTag;
import org.apache.beehive.netui.tags.rendering.TBodyTag;

/**
 * 
 */
public final class TableRenderer {

    private static final String LINE_BREAK = "\n";

    private static final TableTag.State TABLE_STATE = new TableTag.State();
    private static final CaptionTag.State CAPTION_STATE = new CaptionTag.State();
    private static final THeadTag.State THEAD_STATE = new THeadTag.State();
    private static final TBodyTag.State TBODY_STATE = new TBodyTag.State();
    private static final TFootTag.State TFOOT_STATE = new TFootTag.State();
    private static final TrTag.State TR_STATE = new TrTag.State();

    private final HttpServletRequest _request;

    private TagRenderingBase _tableRenderer = null;
    private TagRenderingBase _captionRenderer = null;

    private TagRenderingBase _theadRenderer = null;
    private TagRenderingBase _tbodyRenderer = null;
    private TagRenderingBase _tfootRenderer = null;

    private TagRenderingBase _trRenderer = null;
    private TagRenderingBase _tdRenderer = null;
    private TagRenderingBase _thRenderer = null;

    public TableRenderer(HttpServletRequest request) {
        super();

        _request = request;
        _tableRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.TABLE_TAG, _request);

        _thRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.TH_TAG, _request);
        _trRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.TR_TAG, _request);
        _tdRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.TD_TAG, _request);
    }

    /* -------------------------------------------------------------

        Table Rendering

       ------------------------------------------------------------- */

    public void openTable(TableTag.State state, AbstractRenderAppender appender) {
        if(state == null)
            state = TABLE_STATE;

        appender.append(LINE_BREAK);
        _tableRenderer.doStartTag(appender, state);
        appender.append(LINE_BREAK);
    }

    public void closeTable(AbstractRenderAppender appender) {
        _tableRenderer.doEndTag(appender);
        appender.append(LINE_BREAK);
        appender.append(LINE_BREAK);
    }

    public void openCaption(CaptionTag.State state, AbstractRenderAppender appender) {
        if(state == null)
            state = CAPTION_STATE;

        appender.append(LINE_BREAK);

        if(_captionRenderer == null)
            _captionRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.CAPTION_TAG, _request);

        _captionRenderer.doStartTag(appender, state);
    }

    public void closeCaption(AbstractRenderAppender appender) {
        assert _captionRenderer != null : "Encountered a null THeadTag renderer.  Was openCaption called?";

        _captionRenderer.doEndTag(appender);
        appender.append(LINE_BREAK);
    }

    /* -------------------------------------------------------------

        Table Row Group Rendering

       ------------------------------------------------------------- */

    public final void openTableHead(THeadTag.State state, AbstractRenderAppender appender) {
        if(state == null)
            state = THEAD_STATE;

        if(_theadRenderer == null)
            _theadRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.THEAD_TAG, _request);

        appender.append(LINE_BREAK);
        _theadRenderer.doStartTag(appender, state);
    }

    public void closeTableHead(AbstractRenderAppender appender) {
        assert _theadRenderer != null : "Encountered a null THeadTag renderer.  Was openTableHead called?";

        appender.append(LINE_BREAK);
        _theadRenderer.doEndTag(appender);
        appender.append(LINE_BREAK);
    }

    public void openTableBody(TBodyTag.State state, AbstractRenderAppender appender) {
        if(state == null)
            state = TBODY_STATE;

        if(_tbodyRenderer == null)
            _tbodyRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.TBODY_TAG, _request);

        appender.append(LINE_BREAK);
        _tbodyRenderer.doStartTag(appender, state);
    }

    public void closeTableBody(AbstractRenderAppender appender) {
        assert _tbodyRenderer != null : "Encountered a null TBodyTag renderer.  Was openTableBody called?";

        appender.append(LINE_BREAK);
        _tbodyRenderer.doEndTag(appender);
    }

    public void openTableFoot(TFootTag.State state, AbstractRenderAppender appender) {
        if(state == null)
            state = TFOOT_STATE;

        if(_tfootRenderer == null)
            _tfootRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.TFOOT_TAG, _request);

        appender.append(LINE_BREAK);
        _tfootRenderer.doStartTag(appender, state);
        appender.append(LINE_BREAK);
    }

    public void closeTableFoot(AbstractRenderAppender appender) {
        assert _tfootRenderer != null : "Encountered a null TFootTag renderer.  Was openTableFoot called?";

        appender.append(LINE_BREAK);
        _tfootRenderer.doEndTag(appender);
        appender.append(LINE_BREAK);
    }


    /* -------------------------------------------------------------

        Table Row Rendering

       ------------------------------------------------------------- */

    public final void openHeaderRow(TrTag.State state, AbstractRenderAppender appender) {
        if(state == null)
            state = TR_STATE;

        appender.append(LINE_BREAK);
        _trRenderer.doStartTag(appender, state);
    }

    public void closeHeaderRow(AbstractRenderAppender appender) {
        appender.append(LINE_BREAK);
        _trRenderer.doEndTag(appender);
    }

    public void openTableRow(TrTag.State state, AbstractRenderAppender appender) {
        appender.append(LINE_BREAK);
        _trRenderer.doStartTag(appender, state);
    }

    public void closeTableRow(AbstractRenderAppender appender) {
        appender.append(LINE_BREAK);
        _trRenderer.doEndTag(appender);
    }

    public void openFooterRow(TrTag.State state, AbstractRenderAppender appender) {
        if(state == null)
            state = TR_STATE;

        appender.append(LINE_BREAK);
        _trRenderer.doStartTag(appender, state);
    }

    public void closeFooterRow(AbstractRenderAppender appender) {
        appender.append(LINE_BREAK);
        _trRenderer.doEndTag(appender);
    }

    /* -------------------------------------------------------------

        Table Cell Rendering

       ------------------------------------------------------------- */

    public void openHeaderCell(ThTag.State state, AbstractRenderAppender appender) {
        _thRenderer.doStartTag(appender, state);
    }

    public void closeHeaderCell(AbstractRenderAppender appender) {
        _thRenderer.doEndTag(appender);
        appender.append(LINE_BREAK);
    }

    public void openTableCell(TdTag.State state, AbstractRenderAppender appender) {
        _tdRenderer.doStartTag(appender, state);
    }

    public void closeTableCell(AbstractRenderAppender appender) {
        _tdRenderer.doEndTag(appender);
    }
}
