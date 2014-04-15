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
package complextypes.rpclit.pojo;


/**
 * Complex exception for fault testing.
 */
public class ComplexAccountException extends Exception implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String _msg1;
    private String _msg2;

    public ComplexAccountException() {
    }

    public ComplexAccountException(String msg1, String msg2) {
        _msg1 = msg1;
        _msg2 = msg2;
    }

    public void setMsg1(String msg1) {
        _msg1 = msg1;
    }

    public void setMsg2(String msg2) {
        _msg2 = msg2;
    }

    public String getMsg1() {
        return _msg1;
    }

    public String getMsg2() {
        return _msg2;
    }
}
