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
package coretags.select.selectOrderError;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
)
public class Controller extends PageFlowController
{
    private Options[] opts;
    private String[] results1;

    public Options[] getOpts()
    {
        return opts;
    }

    public void setOpts(Options[] opts)
    {
        this.opts = opts;
    }

    public String[] getResults1()
    {
        return results1;
    }
    public void setResults1(String[] results1)
    {
        this.results1 = results1;
    }

    protected void onCreate()
    {        
        // initialize the opts
        opts = new Options[3];
        opts[0] = new Options("Option One","opt-1", "normal");
        opts[1] = new Options("Option Two","opt-2", "normal2");
        opts[2] = new Options("Option Three","opt-3", "normal3");
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
