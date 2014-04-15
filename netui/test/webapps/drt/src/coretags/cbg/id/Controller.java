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
package coretags.cbg.id;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller()
public class Controller extends PageFlowController
{
    private Options[] opts;
    private Options[] opts2;
    private String[] resultsOne;
    private String[] resultsTwo;

    public Options[] getOpts()
    {
        return opts;
    }

    public void setOpts(Options[] opts)
    {
        this.opts = opts;
    }

    public Options[] getOpts2()
    {
        return opts2;
    }

    public void setOpts2(Options[] opts)
    {
        this.opts2 = opts;
    }

    public String[] getResultsOne()
    {
        return resultsOne;
    }

    public void setResultsOne(String[] resultsOne)
    {
        this.resultsOne = resultsOne;
    }

    public String[] getResultsTwo()
    {
        return resultsTwo;
    }

    public void setResultsTwo(String[] resultsTwo)
    {
        this.resultsTwo = resultsTwo;
    }

    protected void onCreate()
    {        
        // initialize the opts
        opts = new Options[3];
        opts[0] = new Options("Option One","opt-1", "normal","id1");
        opts[1] = new Options("Option Two","opt-2", "normal2","id2");
        opts[2] = new Options("Option Three","opt-3", "normal3","id3");

        opts2 = new Options[3];
        opts2[0] = new Options("Option One","opt-1", "normal","Rid1");
        opts2[1] = new Options("Option Two","opt-2", "normal2","Rid2");
        opts2[2] = new Options("Option Three","opt-3", "normal3","Rid3");
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
	private String _id;
        
        public Options(String name, String value, String style,String id) {
            _name = name;
            _optionValue = value;
            _style = style;
	    _id = id;
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

        public void setId(String id) {
            _id = id;
        }
        public String getId() {
            return _id;
        }
    }

}
