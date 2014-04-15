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

package org.apache.beehive.netui.test.controls.event;

/**
 * A listener class for Hello.EventSet2 event
 */
public class Event2Listener implements Hello.EventSet2 {
    private String method1Result = "method1NotReceived";
    private String method2Result = "set2method2NotReceived";
    private String overloadMethod1 = "overloadMethod1NotReceived";
    private String overloadMethod2 = "overloadMethod2NotReceived";


    public void method1() {
        //Event received, flip the counter
        method1Result = "0";
    }

    public int set2Method2() {
        //Event received, flip the counter
        method2Result = "0";
        return 0;
    }

    public boolean set2OverloadedMethod() {
        //Event received, flip the counter
        overloadMethod1 = "0";
        return true;
    }

    public boolean set2OverloadedMethod(int anArg) {
        //Event received, flip the counter
        overloadMethod2 = "0";
        return true;
    }

    public String getMethod2Result() {

        return method2Result;
    }

    public String getAllResult() {

        return method1Result + method2Result + overloadMethod1 + overloadMethod2;
    }
}
