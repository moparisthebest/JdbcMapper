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
package org.apache.beehive.netui.util;

import java.util.Map;
import java.util.List;
import java.lang.reflect.Array;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * This class is used by NetUI tags that use parameters.
 */
public class ParamHelper
{
    private static final Logger logger = Logger.getInstance(ParamHelper.class);

    /**
     * Add a new parameter or update an existing parameter's list of values.
     * <p/>
     * <em>Implementation Note:</em> in the case that a Map was provided for
     * the <code>value</code> parameter, the this returns without doing
     * anything; in any other case, params is updated (even in
     * <code>value</code> is null).
     * </p>
     * <p/>
     * If value is some object (not an array or list), the string
     * representation of that object is added as a value for name.  If the
     * value is a list (or array) of objects, then the string representation
     * of each element is added as a value for name.  When there are multiple
     * values for a name, then an array of Strings is used in Map.
     * </p>
     *
     * @param params an existing Map of names and values to update
     * @param name   the name of the parameter to add or update
     * @param value  an item or list of items to put into the map
     * @throws IllegalArgumentException in the case that either the params
     *                                  <p/>
     *                                  or name given was null
     */
    public static void addParam(Map params, String name, Object value)
    {


        if (params == null)
            throw new IllegalArgumentException("Parameter map cannot be null");
        if (name == null)
            throw new IllegalArgumentException("Parameter name cannot be null");

        if (value instanceof Map) {
            logger.warn(Bundle.getString("Tags_BadParameterType", name));
            return;
        }

        if (value == null)
            value = "";

        // check to see if we are adding a new element
        //      or if this is an existing element
        Object o = params.get(name);
        int length = 0;

        if (o != null) {
            assert (o instanceof String ||
                    o instanceof String[]);

            if (o.getClass().isArray()) {
                length = Array.getLength(o);
            }
            else {
                length++;
            }
        }

        // check how much size the output needs to be
        if (value.getClass().isArray()) {
            length += Array.getLength(value);
        }
        else if (value instanceof List) {
            length += ((List) value).size();
        }
        else {
            length++;
        }

        if (length == 0)
            return;

        //System.err.println("Number of vaues:" + length);
        // if there is only a single value push it to the parameter table
        if (length == 1) {
            if (value.getClass().isArray()) {
                Object val = Array.get(value, 0);
                if (val != null)
                    params.put(name,val.toString());
                else
                    params.put(name,"");
            }
            else if (value instanceof List) {
                List list = (List) value;
                Object val = list.get(0);
                if (val != null)
                    params.put(name,val.toString());
                else
                    params.put(name,"");
            }
            else
                params.put(name,value.toString());
            return;
        }

        // allocate the string for the multiple values
        String[] values = new String[length];
        int offset = 0;

        // if we had old values, push them to the new array
        if (o != null) {
            if (o.getClass().isArray()) {
                String[] obs = (String[]) o;
                for (;offset<obs.length;offset++) {
                    values[offset] = obs[offset];
                }
            }
            else {
                values[0] = o.toString();
                offset = 1;
            }
        }

        // now move the new values to the array starting at the offset
        // position
        if (value.getClass().isArray())
        {
            //need to convert this array into a String[]
            int size = Array.getLength(value);
            for (int i=0; i < size; i++)
            {
                Object val = Array.get(value, i);
                if (val != null)
                    values[i+offset] = val.toString();
                else
                   values[i+offset] = "";
            }
        }
        else if (value instanceof List)
        {
            List list = (List) value;
            int size = list.size();
            for (int i=0; i < size; i++)
            {
                if (list.get(i) != null)
                    values[i+offset] = list.get(i).toString();
                else
                    values[i+offset] = "";
            }
        }
        else {
            values[offset] = value.toString();
        }
        // store the new values array
        params.put(name, values);
    }
}
