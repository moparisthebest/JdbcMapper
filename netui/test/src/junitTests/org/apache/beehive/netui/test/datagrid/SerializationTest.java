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

import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import javax.servlet.ServletRequest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridStateFactory;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridState;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridConfig;
import org.apache.beehive.netui.test.servlet.ServletFactory;

/**
 *
 */
public class SerializationTest
        extends TestCase {

    private ServletRequest _request = null;

    public void testDataGridState()
        throws Exception {
        DataGridState state = createDataGridState();
        serialize(state.getPagerModel());
        serialize(state.getSortModel());
        serialize(state.getFilterModel());
        serialize(state);
    }

    public void testDataGridConfig()
        throws Exception {
        DataGridConfig config = DataGridTestUtil.getDataGridConfig();
        serialize(config);
    }

    private DataGridState createDataGridState() {
        String queryString = "netui_filter=customers;customerid~contains~A,companyname~contains~e";
        DataGridTestUtil.initQueryString(_request, queryString);
        DataGridStateFactory factory = DataGridStateFactory.getInstance(_request);
        DataGridState state = factory.getDataGridState("customers");
        return state;
    }

    private void serialize(Object object)
        throws Exception {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
        }
        finally {
            if(oos != null)
                oos.close();
            if(baos != null)
                baos.close();
        }
    }

    protected void setUp() {
        _request = ServletFactory.getServletRequest();
    }

    protected void tearDown() {
        _request = null;
    }

    public SerializationTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(SerializationTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}