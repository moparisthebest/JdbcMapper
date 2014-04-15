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
package perf;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
public class Controller extends PageFlowController
{
    private String _value;
    public String getValue() {
        return _value;
    }
    public void setValue(String value) {
        _value = value;
    }

    private String[] _options = {"Option 1","Option 2","Option 3",
                                 "Option 4","Option 5","Option 6"};
    public String[] getOptions() {
        return _options;
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

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="page", path="select100.jsp")
        }
    )
    protected Forward select100()
    {
        return new Forward("page");
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="page", path="empty.jsp")
        }
    )
    protected Forward empty()
    {
        return new Forward("page");
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="page", path="select100.jsp")
        }
    )
    protected Forward postSelect100()
    {
        return new Forward("page");
    }


}
