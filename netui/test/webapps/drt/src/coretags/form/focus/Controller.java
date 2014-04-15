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
package coretags.form.focus;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
public class Controller extends PageFlowController
{
    private String _results;
    public String getResults() {
	return _results;
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward begin()
    {
        return new Forward("index");
    }
        
    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "index.jsp")
	})
    protected Forward post(FormBean bean)        {
        Forward forward = new Forward("success");
	_results = bean.getText1() + " " + bean.getText2() + " " +
	    bean.getText3();
	return forward;
    }

    public static class FormBean implements Serializable
    {
        private String _text1;
        private String _text2;
        private String _text3;

        public String getText1()
        { return _text1; }
        public void setText1(String value)
        { _text1 = value; }

        public String getText2()
        { return _text2; }
        public void setText2(String value)
        { _text2 = value; }

        public String getText3()
        { return _text3; }
        public void setText3(String value)
        { _text3 = value; }

    }
}
