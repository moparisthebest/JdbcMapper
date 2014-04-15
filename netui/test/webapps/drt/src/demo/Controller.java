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
package demo;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller
public class Controller extends PageFlowController
{
    private String[] animals = {"pig", "frog", "dog"};
    private String[] frumpels = {"Bob", "Fred", "Pete"};
    private String[] none = {""};

    private String[] _results;
    private String _name;

    /**
     * @jpf:action
     * @jpf:forward name="demoResults" path="DemoResults.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "demoResults",
                path = "DemoResults.jsp") 
        })
    public Forward DemoSubmit( DemoForm demoForm )
    {
        _name = demoForm.getName();

        _results = none;
        if (demoForm.getName().equals("animals"))
	    _results = animals;
        else if (demoForm.getName().equals("frumpels"))
	    _results = frumpels;

        return new Forward( "demoResults" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="demo" path="Demo.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "demo",
                path = "Demo.jsp") 
        })
    public Forward begin()
    {
        return new Forward( "demo" );
    }


    public static final class DemoForm implements Serializable
    {
        private String _name = null;

        public void setName( String name )
        {
            _name = name;
        }

        public String getName()
        {
            return _name;
        }
    }

    public String[] getResults() {
	return _results;
    }

    public void setResults(String[] results) {
	_results = results;
    }

    public String getName() {
        return _name;
    }
    public void setName(String n) {
	_name = n;
    }

}
