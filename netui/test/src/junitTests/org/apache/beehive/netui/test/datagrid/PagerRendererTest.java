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
package org.apache.beehive.netui.test.datagrid;

import javax.servlet.jsp.JspContext;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.test.servlet.ServletFactory;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.pager.FirstPreviousNextLastPagerRenderer;
import org.apache.beehive.netui.util.config.ConfigUtil;

/**
 *
 */
public class PagerRendererTest
    extends TestCase {

    private JspContext _jspContext = null;

    public void testFPNL() {

        DataGridTagModel dgm = DataGridTestUtil.getDataGridTagModel(_jspContext);
        FirstPreviousNextLastPagerRenderer fpnl = new FirstPreviousNextLastPagerRenderer();
        fpnl.setDataGridTagModel(dgm);
        String pager = fpnl.render();

        System.out.println(pager);
    }

    public void testFPNL2() {
        DataGridTagModel dgm = DataGridTestUtil.getDataGridTagModel(_jspContext);
        DataGridTestUtil.getPagerModel(dgm).setPageSize(5);
        FirstPreviousNextLastPagerRenderer fpnl = new FirstPreviousNextLastPagerRenderer();
        fpnl.setDataGridTagModel(dgm);

        String pager = fpnl.render();

        System.out.println(pager);
    }

    public void testFPNL3() {
        DataGridTestUtil.initQueryString(_jspContext, "netui_row=" + DataGridTestUtil.DEFAULT_DATA_GRID_NAME + "~9");
        DataGridTagModel dgm = DataGridTestUtil.getDataGridTagModel(_jspContext);
        DataGridTestUtil.getPagerModel(dgm).setPageSize(5);
        
        FirstPreviousNextLastPagerRenderer fpnl = new FirstPreviousNextLastPagerRenderer();
        fpnl.setDataGridTagModel(dgm);
        String pager = fpnl.render();

        System.out.println(pager);
    }

    protected void setUp() {
        /* todo: this is a hack that causes the config file to be loaded.
                 it should probably be the case that the default config file
                 is loaded if "getConfig()" is called first
         */
        try {
            ConfigUtil.init(null);
            ConfigUtil.getConfig();
        } catch(Exception e) {
            e.printStackTrace();
        }

        _jspContext = ServletFactory.getPageContext();
    }

    protected void tearDown() {
        _jspContext = null;
    }

    public PagerRendererTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(PagerRendererTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
