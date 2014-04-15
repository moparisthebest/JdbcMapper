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
 * $Header:$Factory
 */
package org.apache.beehive.webservice.utils;

import java.lang.reflect.Array;

/**
 *
 */
public class JavaClassUtils {

    /*
    Need to handle two cases:
        [B -> String class name
        [B -> Class
     */
    public static boolean isArray(String className) {
        /* todo: this is a really cheezy way of doing this, but it's also pretty cheap for now.  */
        return className.startsWith("[") || className.endsWith("[]");
    }

    /* convert [B -> Class.  This is used during runtime. */
    public static Class convertToClass(String className) {

        if(className.equals("byte"))
            return byte.class;
        else if(className.equals("boolean"))
            return boolean.class;
        else if(className.equals("short"))
            return short.class;
        else if(className.equals("int"))
            return int.class;
        else if(className.equals("double"))
            return double.class;
        else if(className.equals("long"))
            return long.class;
        else if(className.equals("char"))
            return char.class;
        else if(className.equals("float"))
            return float.class;
        else if(className.equals("void"))
            return void.class;
        else if(className.equals("long[]"))
            return Array.newInstance(long.class, 0).getClass();
        else if(className.equals("float[]"))
            return Array.newInstance(float.class, 0).getClass();
        else if(className.equals("int[]"))
            return Array.newInstance(int.class, 0).getClass();
        else if(className.equals("short[]"))
            return Array.newInstance(short.class, 0).getClass();
        else if(className.equals("byte[]"))
            return Array.newInstance(byte.class, 0).getClass();
        else if(className.equals("double[]"))
            return Array.newInstance(double.class, 0).getClass();
        else if(className.equals("boolean[]"))
            return Array.newInstance(boolean.class, 0).getClass();
        /* todo: smarter array handling */
        else if(className.endsWith("[]")) {
            try {
                Class clazz = Class.forName(className.substring(0, className.length()-2));
                return Array.newInstance(clazz, 0).getClass();
            }
            catch(ClassNotFoundException e) {
                throw new RuntimeException("Could not locate class '" + className + "'");
            }
        }
        else {
            try {
                return Class.forName(className);
            }
            catch(ClassNotFoundException e) {
                /* todo: need a real RuntimeException subclass here */
                throw new RuntimeException("Could not locate class '" + className + "'");
            }
        }
    }

    /* convert [B -> String.  This is used during code-gen. */
    public static String convertToReadableName(String className) {
        /* todo: error checking */
        if(className.equals("[B"))
            return "byte[]";
        else if(className.equals("[Z"))
            return "boolean[]";
        else if(className.equals("[C"))
            return "char[]";
        else if(className.equals("[D"))
            return "double[]";
        else if(className.equals("[F"))
            return "float[]";
        else if(className.equals("[I"))
            return "int[]";
        else if(className.equals("[J"))
            return "long[]";
        else if(className.equals("[S"))
            return "short[]";
        else return className;
    }
}
