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
package miniTests.binding;

/*
Array Syntax: <netui:label value="{pageFlow.foo}" /><br />
 */
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.util.HashMap;

@Jpf.Controller
public class Controller extends PageFlowController
{
    String[] _values = {"0", "1", "2", "3", "4", "5"};
    HashMap _hashValues;
    Info _info = new Info();

    String pubField = "Public Field";
    String[] pubArray = _values;
    HashMap pubHash = _hashValues;
    Info pubInfo = _info;
    String nullField = null;

    public String getPubField() {
        return pubField;
    }

    public String[] getPubArray() {
        return pubArray;
    }
    
    public HashMap getPubHash() {
        return pubHash;
    }

    public Info getPubInfo() {
        return pubInfo;
    }

    public String getNullField() {
        return nullField;
    }

    public String[] getArray() {
        return _values;
    }

    public HashMap getHashValue() {
        return _hashValues;
    }

    public Info getInfo() {
        return _info;
    }

    /**
     * @jpf:action
     * @jpf:forward name="page" path="Begin.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "page",
                path = "Begin.jsp") 
        })
    protected Forward begin(){
    _hashValues = new HashMap();
    _hashValues.put("foo","bar");
    _hashValues.put("baz","blee");
    _hashValues.put("array",_values);
    _hashValues.put("info",_info);
    pubHash = _hashValues;
        return new Forward("page");
    }

    public static class Info implements java.io.Serializable {
        public String value = "Info.value";
        public String getValue() {
            return value;
        }
    }
 }


