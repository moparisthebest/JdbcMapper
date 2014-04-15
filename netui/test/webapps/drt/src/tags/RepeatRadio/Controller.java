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
package tags.RepeatRadio;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * This is the default controller for a blank web application.
 *
 * @jpf:controller 
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="formbean:Options"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="80" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#index#index.jsp#@action:begin.do@">
 *   <property value="116,140,140,164" name="elbowsX"/>
 *   <property value="92,92,92,92" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="index" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="action:post.do"/>
 * <pageflow-object id="forward:path#index#Results.jsp#@action:post.do@"/>
 * <pageflow-object id="page:index.jsp">
 *   <property value="220" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:Results.jsp"/>
 * <pageflow-object id="action-call:@page:index.jsp@#@action:post.do@"/>
 * <pageflow-object id="action-call:@page:Results.jsp@#@action:begin.do@"/>
 * </view-properties>
 * ::
 */
@Jpf.Controller(
    )
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='formbean:Options'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='80' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#index#index.jsp#@action:begin.do@'>",
        "  <property value='116,140,140,164' name='elbowsX'/>",
        "  <property value='92,92,92,92' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='index' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:post.do'/>",
        "<pageflow-object id='forward:path#index#Results.jsp#@action:post.do@'/>",
        "<pageflow-object id='page:index.jsp'>",
        "  <property value='220' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:Results.jsp'/>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:post.do@'/>",
        "<pageflow-object id='action-call:@page:Results.jsp@#@action:begin.do@'/>",
        "</view-properties>"
    })
public class Controller extends PageFlowController
{
    private Options[] Opts;
    private String ResultsOne;
    private String ResultsTwo;

    public Options[] getOpts()
    {
        return Opts;
    }

    public void setOpts(Options[] opts)
    {
        Opts = opts;
    }

    public String getResultsTwo()
    {
        return ResultsTwo;
    }

    public void setResultsTwo(String resultsTwo)
    {
        ResultsTwo = resultsTwo;
    }

    public String getResultsOne()
    {
        return ResultsOne;
    }

    public void setResultsOne(String resultsOne)
    {
        ResultsOne = resultsOne;
    }

    protected void onCreate()
    {        
        // initialize the opts
        Opts = new Options[3];
        Opts[0] = new Options("Option One","opt-1", "normal");
        Opts[1] = new Options("Option Two","opt-2", "normal2");
        Opts[2] = new Options("Option Three","opt-3", "normal3");
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "index",
                path = "index.jsp") 
        })
    protected Forward begin()
    {
        return new Forward("index");
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="Results.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "index",
                path = "Results.jsp") 
        })
    protected Forward post()
    {
        return new Forward("index");
    }

    public static class Options implements java.io.Serializable {
        private String _name;
        private String _optionValue;
        private String _style;
        
        public Options(String name, String value, String style) {
            _name = name;
            _optionValue = value;
            _style = style;
        }
        
        public void setName(String name) {
            _name = name;
        }
        public String getName() {
            return _name;
        }

        public void setOptionValue(String optionValue) {
            _optionValue = optionValue;
        }
        public String getOptionValue() {
            return _optionValue;
        }
        
        public void setStyle(String style) {
            _style = style;
        }
        public String getStyle() {
            return _style;
        }
    }

}
