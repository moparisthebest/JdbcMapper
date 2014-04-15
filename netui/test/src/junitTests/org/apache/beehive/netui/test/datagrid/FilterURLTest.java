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

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridConfig;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridState;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridURLBuilder;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridStateFactory;
import org.apache.beehive.netui.test.servlet.ServletFactory;

/**
 *
 */
public class FilterURLTest
        extends TestCase {

    private HttpServletRequest _request = null;

    public void testJIRA458() {
        /* Failing URL:
        http://localhost:8080/dataGridWeb/sql/filterTest/FilterTest.jpf?netui_filter=customers;customerid~contains~A,companyname~contains~e
         */
        String queryString = "netui_filter=customers;customerid~contains~A,companyname~contains~e";
        DataGridTestUtil.initQueryString(_request, queryString);
        DataGridStateFactory factory = DataGridStateFactory.getInstance(_request);
        DataGridState state = factory.getDataGridState("customers");
        DataGridURLBuilder urlBuilder = factory.getDataGridURLBuilder("customers");

        /* http://issues.apache.org/jira/browse/BEEHIVE-458

           This should not throw a ClassCastException or AssertionError
         */
        Map map = urlBuilder.getQueryParamsForNextPage();
    }

    protected void setUp() {
        _request = ServletFactory.getServletRequest();
    }

    protected void tearDown() {
        _request = null;
    }

    public FilterURLTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(FilterURLTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
