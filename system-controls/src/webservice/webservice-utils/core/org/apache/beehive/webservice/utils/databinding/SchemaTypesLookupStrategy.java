/*
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 $Header:$
 */
package org.apache.beehive.webservice.utils.databinding;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;

/**
 *
 */
public class SchemaTypesLookupStrategy
    implements BindingLookupStrategy, TypeMappingConstants {

    private static HashMap<Class, QName> SCHEMA_TYPES = new HashMap<Class, QName>();

    public QName class2qname(Class clazz) {
        return SCHEMA_TYPES.get(clazz);
    }

    public QName class2qname(Class clazz, String namespace) {
        return SCHEMA_TYPES.get(clazz);
    }

    public Class qname2class(QName qname) {
        Iterator values = SCHEMA_TYPES.entrySet().iterator();
        while(values.hasNext()) {
            Map.Entry entry = (Map.Entry)values.next();
            if(entry.getValue().equals(qname))
                return (Class)entry.getKey();
        }
        return null;
    }

    static {
        addBuiltInType(XSD_HEXBIN, byte[].class);
        addBuiltInType(XSD_BYTE, byte[].class);
        addBuiltInType(XSD_BASE64, byte[].class);
        // anySimpleType is mapped to java.lang.String according to JAX-RPC 1.1 spec.
        addBuiltInType(XSD_ANYSIMPLETYPE, String.class);
        // If SOAP 1.1 over the wire, map wrapper classes to XSD primitives.
        addBuiltInType(XSD_STRING, String.class);
        addBuiltInType(XSD_BOOLEAN, Boolean.class);
        addBuiltInType(XSD_DOUBLE, Double.class);
        addBuiltInType(XSD_FLOAT, Float.class);
        addBuiltInType(XSD_INT, Integer.class);
        addBuiltInType(XSD_INTEGER, java.math.BigInteger.class);
        addBuiltInType(XSD_DECIMAL, java.math.BigDecimal.class);
        addBuiltInType(XSD_LONG, Long.class);
        addBuiltInType(XSD_SHORT, Short.class);
        addBuiltInType(XSD_BYTE, Byte.class);
        // The XSD Primitives are mapped to java primitives.
        addBuiltInType(XSD_BOOLEAN, boolean.class);
        addBuiltInType(XSD_DOUBLE, double.class);
        addBuiltInType(XSD_FLOAT, float.class);
        addBuiltInType(XSD_INT, int.class);
        addBuiltInType(XSD_LONG, long.class);
        addBuiltInType(XSD_SHORT, short.class);
        addBuiltInType(XSD_BYTE, byte.class);
        // Map QNAME to the jax rpc QName class
        addBuiltInType(XSD_QNAME, QName.class);
        // The closest match for anytype is Object
        addBuiltInType(XSD_ANYTYPE, Object.class);
        // See the SchemaVersion classes for where the registration of
        // dateTime (for 2001) and timeInstant (for 1999 & 2000) happen.
        addBuiltInType(XSD_DATE, java.sql.Date.class);
        // See the SchemaVersion classes for where the registration of
        // dateTime (for 2001) and timeInstant (for 1999 & 2000) happen.
        addBuiltInType(XSD_DATETIME, java.util.Date.class);
        addBuiltInType(XSD_DATETIME, java.util.Calendar.class);

        addBuiltInType(SOAP_ARRAY, java.util.ArrayList.class);
    }

    private static void addBuiltInType(QName qname, Class clazz) {
        SCHEMA_TYPES.put(clazz, qname);
    }
}