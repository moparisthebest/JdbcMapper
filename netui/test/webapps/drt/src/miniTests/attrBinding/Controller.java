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
package miniTests.attrBinding;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller
public class Controller extends PageFlowController
{

    private int _prop1 = 17;
    private String _prop2 = "StringBinding";
    private float _prop3 = (float) 17.17;
    private boolean _prop4 = true;

    private int[] _props1 = {1,2,3};
    private String[] _props2 = {"foo", "bar", "blee"};
    private float[] _props3 = {(float) 16.16, (float) 17.17, (float) 18.18};
    private boolean[] _props4 = {true, false, true, true, false};


    ///****************** Scalars ********************
    public int getProp1() {
	    return _prop1;
    }
    public void setProp1(int prop1) {
	    _prop1 = prop1;
    }

    public String getProp2() {
	    return _prop2;
    }
    public void setProp2(String prop2) {
	    _prop2 = prop2;
    }

    public float getProp3() {
	    return _prop3;
    }
    public void setProp3(float prop3) {
	    _prop3 = prop3;
    }

    public boolean getProp4() {
	    return _prop4;
    }
    public void setProp4(boolean prop4) {
	    _prop4 = prop4;
    }

    ///****************** Arrays ********************
    public int[] getProps1() {
	    return _props1;
    }
    public void setProps1(int[] prop1) {
	    _props1 = prop1;
    }

    public String[] getProps2() {
	    return _props2;
    }
    public void setProps2(String[] prop2) {
	    _props2 = prop2;
    }

    public float[] getProps3() {
	    return _props3;
    }
    public void setProps3(float[] prop3) {
	    _props3 = prop3;
    }

    public boolean[] getProps4() {
	    return _props4;
    }
    public void setProps4(boolean[] prop4) {
	    _props4 = prop4;
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "page",
                path = "Begin.jsp") 
        })
    protected Forward begin(){
        return new Forward("page");
    }
 }


