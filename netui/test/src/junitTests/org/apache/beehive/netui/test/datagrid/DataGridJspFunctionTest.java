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

import javax.servlet.ServletRequest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridState;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspFunctions;
import org.apache.beehive.netui.test.servlet.ServletFactory;

/**
 *
 */
public class DataGridJspFunctionTest
        extends TestCase {

    private static final String GRID_NAME = "bugs";
    private ServletRequest _servletRequest = null;

    public void testSortFunctions() {
        DataGridTestUtil.initQueryString(_servletRequest, "foo=bar&netui_sort=bugs;id,-name");
        SortModel sortModel = DataGridTestUtil.createSortModel(_servletRequest, GRID_NAME);

        boolean isAscending = JspFunctions.isSortedAscending(sortModel, "id");
        assertTrue(isAscending);

        isAscending = JspFunctions.isSortedAscending(sortModel, "name");
        assertFalse(isAscending);
    }

    protected void setUp() {
        _servletRequest = ServletFactory.getServletRequest();
    }

    protected void tearDown() {
        _servletRequest = null;
    }

    public DataGridJspFunctionTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(DataGridJspFunctionTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}