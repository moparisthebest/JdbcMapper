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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.databinding.datagrid.api.sort.SortDirection;
import org.apache.beehive.netui.databinding.datagrid.api.sort.Sort;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridConfig;
import org.apache.beehive.netui.databinding.datagrid.runtime.config.DefaultDataGridConfig;

/**
 *
 */
public class DefaultSortTest
    extends TestCase {

    private Sort createSort() {
        DataGridConfig config = new DefaultDataGridConfig();
        return config.createSort();
    }

    public void testChangeSortDirection() {
        Sort sort = createSort();

        sort.setDirection(SortDirection.ASCENDING);
        sort.setSortExpression("customername");
        assertTrue(sort.getDirection() == SortDirection.ASCENDING);
/*
        sort.changeSortDirection();
        assertTrue(sort.getDirection() == SortDirection.DESCENDING);

        sort.changeSortDirection();
        assertTrue(sort.getDirection() == SortDirection.ASCENDING);
*/        
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public DefaultSortTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(DefaultSortTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
