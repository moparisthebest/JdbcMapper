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
package coretags.form.location;

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
        
                return forward;
    }

    public static class FormBean implements Serializable
    {
        private String _type = "Default Type Value";

        public String getType()
        { return _type; }

        public void setType(String type)
        { _type = type; }
    }
}
