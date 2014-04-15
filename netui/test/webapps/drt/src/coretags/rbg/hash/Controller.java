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
package coretags.rbg.hash;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.util.TreeMap;

@Jpf.Controller(
    )
public class Controller extends PageFlowController
{
    private TreeMap opts;
    private String resultsOne;

    public TreeMap getOpts()
    {
        return opts;
    }

    public String getResultsOne()
    {
        return resultsOne;
    }

    public void setResultsOne(String resultsOne)
    {
        this.resultsOne = resultsOne;
    }

    protected void onCreate()
    {        
        opts = new TreeMap();
        opts.put("val1","Value One");
        opts.put("val2","Value Two");
        opts.put("val3","Value Three");
	opts.put("junk","Junk Value");
        opts.put("val4","Value Four");
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
}
