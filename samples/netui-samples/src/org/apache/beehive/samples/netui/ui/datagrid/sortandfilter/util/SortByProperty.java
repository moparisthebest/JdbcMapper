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
package org.apache.beehive.samples.netui.ui.datagrid.sortandfilter.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.beans.Introspector;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;

import org.apache.beehive.netui.databinding.datagrid.api.sort.Sort;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortDirection;

/**
 * Simple implementation of an algorithm to sort a data set by a single property value.  This expects that
 * the List has a homogeneous set of objects.  This code does not cache reflected information and is not ready
 * for production applications -- it's just enables example that show how sorting and filtering work in the
 * NetUI data grid.
 */
public class SortByProperty {

    public List sort(Sort sort, List list) {
        if (list == null || list.size() == 0)
            return list;

        List sorted = new ArrayList(list);
        SortByPropertyComparator sorter = new SortByPropertyComparator(sort);
        Collections.sort(sorted, sorter);
        return sorted;
    }

    private class SortByPropertyComparator
        implements Comparator {

        private Sort _sort = null;

        SortByPropertyComparator(Sort sort) {
            _sort = sort;
        }

        public int compare(Object o1, Object o2) {
            int comparison = 0;
            Object value1 = extractPropertyValue(o1, _sort.getSortExpression());
            Object value2 = extractPropertyValue(o2, _sort.getSortExpression());

            assert value1 instanceof Comparable;

            comparison = ((Comparable) value1).compareTo(value2);

            if (_sort.getDirection() == SortDirection.ASCENDING)
                return comparison;
            else if (_sort.getDirection() == SortDirection.DESCENDING)
                return -1 * comparison;
            else return 0;
        }

        private Object extractPropertyValue(Object object, String propertyName) {
            assert object != null;
            assert propertyName != null && !propertyName.equals("");

            Method propAccessor = lookupPropertyAccessor(object.getClass(), propertyName);
            if (propAccessor == null)
                throw new IllegalStateException("Unable to find property accessor matching property name \"" + propertyName + "\"");

            Object value = null;
            try {
                value = propAccessor.invoke(object, (Object[])null);
            }
            catch (IllegalAccessException e) {
                throw new IllegalStateException("Exception occurred invoking property getter \"" + propAccessor.getName() + "\"");
            }
            catch (InvocationTargetException e) {
                throw new IllegalStateException("Exception occurred invoking property getter \"" + propAccessor.getName() + "\"");
            }

            return value;
        }

        private Method lookupPropertyAccessor(Class clazz, String propertyName) {
            BeanInfo beanInfo = null;
            try {
                beanInfo = Introspector.getBeanInfo(clazz);
            }
            catch (IntrospectionException e) {
                throw new IllegalStateException("Unable to find property named \"" + propertyName + "\" on type \"" +
                    clazz.getName() + "\"", e);
            }

            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                if (pd.getName().equalsIgnoreCase(propertyName))
                    return pd.getReadMethod();
            }
            return null;
        }
    }
}