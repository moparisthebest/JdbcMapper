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
package bugs.b14787;


import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "begin",
            path = "Begin.jsp") 
    })
public class Controller extends PageFlowController
{
    String _text1 = "text1";
    String _text1Default = "text1Default";

    String _text2 = null;
    String _text2Default = "text2Default";

    public String getText1() {
	    return _text1;
    }
    public void setText1(String text1) {
	    _text1 = text1;
    }

    public String getText1Default() {
	    return _text1Default;
    }

    public String getText2() {
	    return _text2;
    }
    public void setText2(String text2) {
	    _text2 = text2;
    }

    public String getText2Default() {
	    return _text2Default;
    }

    public static class Form implements Serializable 
    {
    }

    @Jpf.Action(
        )
    public Forward postback(Form form)
    {
        return new Forward("begin");
    }

    @Jpf.Action(
        )
    public Forward begin()
    {
        return new Forward("begin");
    }
}
