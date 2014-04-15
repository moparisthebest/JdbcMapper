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
package org.apache.beehive.netui.test.util.iterator;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

/**
 *
 */
final class ResultSetTestSupport {

    static final ResultSet getResultSet() {
        MockResultSet rs = new MockResultSet();
        return (ResultSet) Proxy.newProxyInstance(rs.getClass().getClassLoader(),
                new Class[]{ResultSet.class},
                rs);
    }

    private static final ResultSetMetaData getResultSetMetaData() {
        MockResultSetMetaData rsmd = new MockResultSetMetaData();
        return (ResultSetMetaData) Proxy.newProxyInstance(rsmd.getClass().getClassLoader(),
                new Class[]{ResultSetMetaData.class},
                rsmd);
    }

    static class MockResultSet
            implements InvocationHandler {

        private static List/*<CustomerBean>*/ DATA = new ArrayList/*<CustomerBean>*/();

        static {
            DATA.add(new CustomerBean(1, "bob"));
            DATA.add(new CustomerBean(2, "frank"));
            DATA.add(new CustomerBean(3, "joe"));
            DATA.add(new CustomerBean(4, "tom"));
            DATA.add(new CustomerBean(5, "dick"));
            DATA.add(new CustomerBean(6, "harry"));
        }

        private int _row = -1;
        private ResultSetMetaData _rsmd = ResultSetTestSupport.getResultSetMetaData();

        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {

            String methodName = method.getName();
            if (methodName.equals("getMetaData"))
                return _rsmd;
            else if(methodName.equals("isLast"))
                return Boolean.valueOf(_row+1 == DATA.size());
            else if(methodName.equals("wasNull"))
                return Boolean.FALSE;
            else if(methodName.equals("getObject")) {
                int col = ((Integer)args[0]).intValue();
                /* the order of the indices here must match the order of the COLUMN_NAMES in the metadata */
                if(col == 1)
                    return ((CustomerBean)DATA.get(_row)).getName();
                else if(col == 2)
                    return ((CustomerBean)DATA.get(_row)).getId();
                else throw new SQLException("Invalid column value \"" + col + "\"");
            }
            else if(methodName.equals("next")) {
                _row++;
                return Boolean.valueOf(_row < DATA.size());
            }
            else throw new UnsupportedOperationException("Can not invoke the method \"" + methodName + "\" as it is not supported");
        }
    }

    static class MockResultSetMetaData
        implements InvocationHandler {

        private static final int COLUMN_COUNT = 2;
        private static final String[] COLUMN_NAMES = new String[] {"NAME", "ID"};

        private int _index = 0;

        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {

            String methodName = method.getName();
            if (methodName.equals("getColumnCount"))
                return new Integer(COLUMN_COUNT);
            else if(methodName.equals("getColumnName")) {
                int col = ((Integer)args[0]).intValue();
                return COLUMN_NAMES[col-1];
            }
            else throw new UnsupportedOperationException("Can not invoke the method \"" + methodName + "\" as it is not supported");
        }
    }

    static class CustomerBean {

        private Integer _id;
        private String _name;

        CustomerBean(int id, String name) {
            _id = new Integer(id);
            _name = name;
        }

        public Integer getId() {
            return _id;
        }

        public void setId(Integer id) {
            _id = id;
        }

        public String getName() {
            return _name;
        }

        public void setName(String name) {
            _name = name;
        }
    }
}
