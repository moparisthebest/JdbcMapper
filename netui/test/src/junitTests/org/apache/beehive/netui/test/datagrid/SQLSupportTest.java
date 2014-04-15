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

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridConfig;
import org.apache.beehive.netui.databinding.datagrid.api.filter.Filter;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterOperation;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterTypeHint;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterOperationHint;
import org.apache.beehive.netui.databinding.datagrid.api.sort.Sort;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortDirection;
import org.apache.beehive.netui.databinding.datagrid.runtime.sql.SQLSupport;
import org.apache.beehive.netui.databinding.datagrid.runtime.sql.SQLSupportConfigFactory;
import org.apache.beehive.netui.databinding.datagrid.runtime.sql.SQLSupportConfig;

/**
 *
 */
public class SQLSupportTest
        extends TestCase {

    public void testSortEncoding() {
        DataGridConfig config = DataGridTestUtil.getDataGridConfig();
        Sort sort = config.createSort();                            
        sort.setDirection(SortDirection.DESCENDING);
        sort.setSortExpression("CUSTOMERS");
        List list = new ArrayList();
        list.add(sort);

        String sortClause = SQLSupport.getInstance().createOrderByClause(list);
        assertEquals("ORDER BY CUSTOMERS DESC", sortClause);
    }

    public void testFitlerEncoding() {
        DataGridConfig config = DataGridTestUtil.getDataGridConfig();
        FilterOperation fOp = null;
        Filter filter = config.createFilter();
        filter.setFilterExpression("CUSTOMERID");
        filter.setValue("1234");
        List filters = Collections.singletonList(filter);

        SQLSupport sqlSupport = SQLSupport.getInstance();
        /* isnotempty testing */
        fOp = SQLSupport.mapFilterAbbreviationToOperation("isnotempty");
        filter.setOperation(fOp);
        assertEquals("WHERE (CUSTOMERID IS NOT NULL)", sqlSupport.createWhereClause(filters));
        fOp = SQLSupport.mapFilterHintToOperation(FilterOperationHint.IS_NOT_EMPTY);
        filter.setOperation(fOp);
        assertEquals("WHERE (CUSTOMERID IS NOT NULL)", sqlSupport.createWhereClause(filters));
        fOp = SQLSupport.mapFilterHintToOperation(FilterOperationHint.IS_NOT_EMPTY);
        filter.setOperation(fOp);
        filter.setTypeHint(FilterTypeHint.DATE);
        assertEquals("WHERE (CUSTOMERID IS NOT NULL)", sqlSupport.createWhereClause(filters));

        /* equals testing */
        fOp = SQLSupport.mapFilterAbbreviationToOperation("eq");
        filter.setOperation(fOp);
        filter.setTypeHint(FilterTypeHint.STRING);
        assertEquals("WHERE (CUSTOMERID='1234')", sqlSupport.createWhereClause(filters));
        fOp = SQLSupport.mapFilterHintToOperation(FilterOperationHint.EQUAL);
        filter.setOperation(fOp);
        assertEquals("WHERE (CUSTOMERID='1234')", sqlSupport.createWhereClause(filters));
        fOp = SQLSupport.mapFilterHintToOperation(FilterOperationHint.EQUAL);
        filter.setTypeHint(FilterTypeHint.NUMERIC);
        filter.setOperation(fOp);
        assertEquals("WHERE (CUSTOMERID=1234)", sqlSupport.createWhereClause(filters));
    }

    public void testFitlerEncodingDoubleQuote() {
        DataGridConfig config = DataGridTestUtil.getDataGridConfig();
        FilterOperation fOp = null;
        Filter filter = config.createFilter();
        filter.setFilterExpression("CUSTOMERID");
        filter.setValue("1234");
        List filters = Collections.singletonList(filter);

        SQLSupportConfig sqlSupportConfig = SQLSupportConfigFactory.getInstance();
        sqlSupportConfig.setQuoteChar("\"");

        SQLSupport sqlSupport = SQLSupport.getInstance(sqlSupportConfig);

        /* isnotempty testing */
        fOp = SQLSupport.mapFilterAbbreviationToOperation("isnotempty");
        filter.setOperation(fOp);
        assertEquals("WHERE (CUSTOMERID IS NOT NULL)", sqlSupport.createWhereClause(filters));
        fOp = SQLSupport.mapFilterHintToOperation(FilterOperationHint.IS_NOT_EMPTY);
        filter.setOperation(fOp);
        assertEquals("WHERE (CUSTOMERID IS NOT NULL)", sqlSupport.createWhereClause(filters));
        fOp = SQLSupport.mapFilterHintToOperation(FilterOperationHint.IS_NOT_EMPTY);
        filter.setOperation(fOp);
        filter.setTypeHint(FilterTypeHint.DATE);
        assertEquals("WHERE (CUSTOMERID IS NOT NULL)", sqlSupport.createWhereClause(filters));

        /* equals testing */
        fOp = SQLSupport.mapFilterAbbreviationToOperation("eq");
        filter.setOperation(fOp);
        filter.setTypeHint(FilterTypeHint.STRING);
        assertEquals("WHERE (CUSTOMERID=\"1234\")", sqlSupport.createWhereClause(filters));
        fOp = SQLSupport.mapFilterHintToOperation(FilterOperationHint.EQUAL);
        filter.setOperation(fOp);
        assertEquals("WHERE (CUSTOMERID=\"1234\")", sqlSupport.createWhereClause(filters));
        fOp = SQLSupport.mapFilterHintToOperation(FilterOperationHint.EQUAL);
        filter.setTypeHint(FilterTypeHint.NUMERIC);
        filter.setOperation(fOp);
        assertEquals("WHERE (CUSTOMERID=1234)", sqlSupport.createWhereClause(filters));
    }

    public void testFilterOperationInference() {
        DataGridConfig config = DataGridTestUtil.getDataGridConfig();
        Filter filter = config.createFilter();
        filter.setOperationHint(FilterOperationHint.GREATER_THAN);
        filter.setFilterExpression("CUSTOMERID");
        filter.setTypeHint(FilterTypeHint.NUMERIC);
        filter.setValue("1234");
        List filters = Collections.singletonList(filter);
        SQLSupport sqlSupport = SQLSupport.getInstance();
        assertEquals("WHERE (CUSTOMERID>1234)", sqlSupport.createWhereClause(filters));
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public SQLSupportTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(SQLSupportTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
