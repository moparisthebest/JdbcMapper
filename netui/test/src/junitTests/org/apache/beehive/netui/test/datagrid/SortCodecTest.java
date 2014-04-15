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

import java.util.List;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.test.servlet.ServletFactory;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortDirection;
import org.apache.beehive.netui.databinding.datagrid.api.sort.Sort;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 */
public class SortCodecTest
    extends TestCase {

    private HttpServletRequest _request = null;
    private String _namespace = "customerGrid";

    private ServletRequest getRequest() {
        return _request;
    }

    public void testFiller() {}
    
/*
    public void testEmptySortService() {
        SortCodec codec = new SortCodec();
        Map<String, List<Sort>> allSorts = codec.decode(_request.getParameterMap());
        assert allSorts == null;
    }

    public void testSingleAscendingSort() {
        DataGridTestUtil.initQueryString(_request, "netui_sort=" + _namespace + ";CUSTOMERNAME");
        SortCodec codec = new SortCodec();
        Map<String, List<Sort>> allSorts = codec.decode(_request.getParameterMap());

        List<Sort> sorts = allSorts.get(_namespace);
        assert sorts != null;
        assert sorts.size() == 1;
        assert sorts.get(0).getDirection() == SortDirection.ASCENDING;
        assert sorts.get(0).getSortExpression().equals("CUSTOMERNAME");
    }

    public void testMultipleSort() {
        DataGridTestUtil.initQueryString(_request, "?" +
            "netui_sort=" + _namespace + ";CUSTOMERNAME,-UNITPRICE" +
            "&netui_sort=productGrid;QUANTITY");
        SortCodec codec = new SortCodec();
        Map<String, List<Sort>/> allSorts = codec.decode(_request.getParameterMap());
        List<Sort> sorts = allSorts.get(_namespace);

        assert sorts != null;
        assert sorts.size() == 2;
        assert sorts.get(0).getDirection() == SortDirection.ASCENDING;
        assert sorts.get(0).getSortExpression().equals("CUSTOMERNAME");
        assert sorts.get(1).getDirection() == SortDirection.DESCENDING;
        assert sorts.get(1).getSortExpression().equals("UNITPRICE");

        sorts = allSorts.get("productGrid");
        assert sorts != null;
        assert sorts.size() == 1;
        assert sorts.get(0).getDirection() == SortDirection.ASCENDING;
        assert sorts.get(0).getSortExpression().equals("QUANTITY");
    }

    public void testSingleDescendingSort() {
        DataGridTestUtil.initQueryString(_request, "netui_sort=" + _namespace + ";-CUSTOMERNAME");
        SortCodec codec = new SortCodec();
        Map<String, List<Sort>> allSorts = codec.decode(_request.getParameterMap());
        List<Sort> sorts = allSorts.get(_namespace);

        assert sorts != null;
        assert sorts.size() == 1;
        assert sorts.get(0).getDirection() == SortDirection.DESCENDING;
        assert sorts.get(0).getSortExpression().equals("CUSTOMERNAME");
    }

    public void testTwoDescendingSorts() {
        DataGridTestUtil.initQueryString(_request, "netui_sort=" + _namespace + ";-CUSTOMERNAME,-CUSTOMERID");
        SortCodec codec = new SortCodec();
        Map<String, List<Sort>> allSorts = codec.decode(_request.getParameterMap());
        List<Sort> sorts = allSorts.get(_namespace);

        assert sorts != null;
        assert sorts.size() == 2;
        assert sorts.get(0).getDirection() == SortDirection.DESCENDING;
        assert sorts.get(0).getSortExpression().equals("CUSTOMERNAME");
        assert sorts.get(1).getDirection() == SortDirection.DESCENDING;
        assert sorts.get(1).getSortExpression().equals("CUSTOMERID");
    }
*/

    protected void setUp() {
        _request = ServletFactory.getServletRequest();
    }

    protected void tearDown() {
        _request = null;
    }

    public SortCodecTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(SortCodecTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
