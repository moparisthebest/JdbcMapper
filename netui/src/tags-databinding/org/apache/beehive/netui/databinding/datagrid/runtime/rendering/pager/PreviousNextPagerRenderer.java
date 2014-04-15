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
package org.apache.beehive.netui.databinding.datagrid.runtime.rendering.pager;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.IDataGridMessageKeys;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.PagerRenderer;

public final class PreviousNextPagerRenderer
    extends PagerRenderer {

    protected String internalRender() {
        InternalStringBuilder buf = new InternalStringBuilder();
        PagerModel pagerModel = getPagerModel();
        int currentPage = pagerModel.getPage();
        int lastPage = pagerModel.getLastPage();

        buf.append(getDataGridTagModel().formatMessage(IDataGridMessageKeys.PAGER_FMT_BANNER,
                                                       new Object[]{new Integer(currentPage + 1),
                                                                    new Integer(lastPage  + 1)}));

        buf.append("&nbsp;&nbsp;");
        if(pagerModel.getPreviousPage() > -1)
            buf.append(buildLivePreviousLink());
        else if(lastPage > 0)
            buf.append(buildDeadPreviousLink());

        buf.append("&nbsp;");
        if(pagerModel.getNextPage() <= pagerModel.getLastPage())
            buf.append(buildLiveNextPageLink());
        else if(lastPage > 0)
            buf.append(buildDeadNextLink());

        return buf.toString();
    }
}
