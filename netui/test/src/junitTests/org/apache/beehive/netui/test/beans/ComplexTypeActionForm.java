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
package org.apache.beehive.netui.test.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ComplexTypeActionForm
    extends SimpleTypeActionForm {

    private Map _map = null;
    private List _list = null;
    private SimpleTypeActionForm[] _array = null;
    private BigDecimal _bigDecimal = null;
    private Date _date = null;

    public ComplexTypeActionForm() {
        _map = new HashMap();
        _list = new ArrayList();
        _array = new SimpleTypeActionForm[10];

        populate();
    }

    public Map getMap() {
        return _map;
    }

    public List getList() {
        return _list;
    }

    public SimpleTypeActionForm[] getArray() {
        return _array;
    }

    public void setDate(Date date) {
        _date = date;
    }

    public Date getDate() {
        return _date;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        _bigDecimal = bigDecimal;
    }

    public BigDecimal getBigDecimal() {
        return _bigDecimal;
    }

    private void populate() {
        for(int i = 0; i < sampleData.length; i++) {
            _list.add(sampleData[i]);
            _map.put(mapKeys[i], sampleData[i]);
        }

        for(int i = 0; i < _array.length; i++) {
            _array[i] = new SimpleTypeActionForm();
        }

        _map.put("\"", "double quote");
        _map.put("\"\"", "double, double quote");
        _map.put("\'", "single quote");
        _map.put("\'\'", "double, single quote");
        _map.put("", "empty string");
        _map.put(" ", "space");
        _map.put("*", "asterisk");
        _map.put("%", "percent");
    }

    private Object[] sampleData =
        {new Boolean(true),
         new Byte((byte)42),
         new Character('U'),
         new Double(42.42),
         new Float(3.14),
         new Integer(27),
         new Long(123456789),
         new Short((short)4),
         new String("some sample text"),
         new SimpleTypeActionForm()
        };

    private String[] mapKeys =
        {
            "booleanWrapper",
            "byteWrapper",
            "charWrapper",
            "doubleWrapper",
            "floatWrapper",
            "integerWrapper",
            "longWrapper",
            "shortWrapper",
            "string",
            "bean"
        };

}

