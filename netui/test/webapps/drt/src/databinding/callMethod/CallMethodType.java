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
package databinding.callMethod;

// java imports

// internal imports

// external imports

/**
 *
 */
public class CallMethodType
{
    public String publicMethod() {return "public method";}
    private String privateMethod() {return "private method";}
    protected String protectedMethod() {return "protected method";}

    public static String publicStaticMethod() {return "public static method";}

    public String publicMethodZeroArg() {return "public method: zero arg";}
    public String publicMethodOneArg(int value1) {return "public method: value1: " + value1;}
    public String publicMethodTwoArg(int value1, int value2) {return "public method: value1: " + value1 + " value2: " + value2;}
}
