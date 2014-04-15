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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.test.servlet.ServletFactory;
import org.apache.beehive.netui.databinding.datagrid.api.filter.Filter;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterOperationHint;

/**
 *
 */
public class FilterCodecTest
    extends TestCase {

    private String _namespace = "customers";
    private ServletRequest _request;

    public void testFiller() {}
/*
    public void testSimpleDecode() {
        DataGridTestUtil.initQueryString(_request, "netui_filter=" + _namespace + ";CUSTOMERNAME~eq~homer");
        FilterCodec codec = new FilterCodec();

        Map<String, List<Filter>> allFilters = codec.decode(_request.getParameterMap());
        List<Filter> filters = allFilters.get(_namespace);

        assertTrue(filters != null);
        assertEquals(1, filters.size());
        assertEquals(FilterOperationHint.EQUAL, filters.get(0).getOperation().getOperationHint());
        assertEquals("homer", filters.get(0).getValue());
        assertEquals("CUSTOMERNAME", filters.get(0).getFilterExpression());
    }

    public void testDecode1() {
        DataGridTestUtil.initQueryString(_request, "netui_filter=" + _namespace + ";CUSTOMERNAME~eq~homer&netui_filter=products;ID~ne~1234");
        FilterCodec codec = new FilterCodec();

        Map<String, List<Filter>> allFilters = codec.decode(_request.getParameterMap());
        List<Filter> filters = allFilters.get(_namespace);

        assertTrue(filters != null);
        assertEquals(1, filters.size());
        assertEquals(FilterOperationHint.EQUAL, filters.get(0).getOperation().getOperationHint());
        assertEquals("homer", filters.get(0).getValue());
        assertEquals("CUSTOMERNAME", filters.get(0).getFilterExpression());

        filters = allFilters.get("products");
        assertTrue(filters != null);
        assertEquals(1, filters.size());
        assertEquals(FilterOperationHint.NOT_EQUAL, filters.get(0).getOperation().getOperationHint());
        assertEquals("1234", filters.get(0).getValue());
        assertEquals("ID", filters.get(0).getFilterExpression());
    }

    public void testDataGridStateService1() {
        DataGridTestUtil.initQueryString(_request,
            "netui_filter=" + _namespace + ";ZIP~eq~12345,CUSTOMERNAME~eq~homer&netui_filter=products;ID~ne~1234");
        DataGridURLService dgss = DataGridURLService.getInstance(_request);

        Map queryParams = null;
        String[] qp = null;

        queryParams = dgss.getQueryParamsOmitAllFilters(_namespace);
        assertEquals(1, queryParams.keySet().size());
        assertNotNull(queryParams.get("netui_filter"));
        qp = (String[])queryParams.get("netui_filter");
        assertEquals("products;ID~ne~1234", qp[0]);

        queryParams = dgss.getQueryParamsOmitFilter(_namespace, "CUSTOMERNAME");
        assertEquals(1, queryParams.keySet().size());
        qp = (String[])queryParams.get("netui_filter");
        assertEquals(2, qp.length);
    }

    public void testDataGridStateService2() {
        DataGridTestUtil.initQueryString(_request,
            "netui_filter=" + _namespace + ";ZIP~eq~12345,CUSTOMERNAME~eq~homer&netui_filter=products;ID~ne~1234");
        DataGridURLService dgss = DataGridURLService.getInstance(_request);

        Map queryParams = null;
        String[] qp = null;

        queryParams = dgss.getQueryParamsOmitAllFilters(_namespace);
        assertEquals(1, queryParams.keySet().size());
        assertNotNull(queryParams.get("netui_filter"));
        qp = (String[])queryParams.get("netui_filter");
        assertEquals("products;ID~ne~1234", qp[0]);

        queryParams = dgss.getQueryParamsOmitFilter(_namespace, "CUSTOMERNAME");
        assertEquals(1, queryParams.keySet().size());
        qp = (String[])queryParams.get("netui_filter");
        assertEquals(2, qp.length);
        assertEquals(_namespace + ";ZIP~eq~12345", qp[0]);
        assertEquals("products;ID~ne~1234", qp[1]);
    }

    public void testEncode1() {
        String customerNameParam = _namespace + ";CUSTOMERNAME~eq~homer";
        DataGridTestUtil.initQueryString(_request, "netui_filter=" + customerNameParam + "&netui_filter=products;ID~ne~1234");
        FilterCodec codec = new FilterCodec();

        Map<String, List<Filter>> allFilters = codec.decode(_request.getParameterMap());
        List<Filter> gridFilters = allFilters.get(_namespace);

        String filterQueryValue = codec.encode(_namespace, gridFilters);

        assertNotNull(filterQueryValue);
        assertEquals(customerNameParam, filterQueryValue);
    }
*/
    protected void setUp() {
        _request = ServletFactory.getServletRequest();
    }

    protected void tearDown() {
        _request = null;
    }

    public FilterCodecTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(FilterCodecTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
