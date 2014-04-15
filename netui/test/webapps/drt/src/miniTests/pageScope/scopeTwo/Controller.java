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
package miniTests.pageScope.scopeTwo;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller
public class Controller extends PageFlowController
{
    private int _visit = 0;

    public int getVisits() {
	return _visit;
    }
    public void setVisits(int visit) {
	_visit = visit;
    }

    /**
     * @jpf:action
     * @jpf:forward name="next" path="Begin.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "next",
                path = "Begin.jsp") 
        })
    public Forward nav()
    {
	_visit++;
	return new Forward("next");
    }

    /**
     * @jpf:action
     * @jpf:forward name="scope" path="/miniTests/pageScope/scopeOne/Controller.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "scope",
                path = "/miniTests/pageScope/scopeOne/Controller.jpf") 
        })
    public Forward changePageFlow()
    {
	_visit++;
	return new Forward("scope");
    }

    /**
     * @jpf:action
     * @jpf:forward name="back" path="Begin.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "back",
                path = "Begin.jsp") 
        })
    public Forward back()
    {
	_visit++;
	return new Forward("back");
    }


    /**
     * @jpf:action
     * @jpf:forward name="begin" path="Begin.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "Begin.jsp") 
        })
    public Forward begin()
    {
	_visit++;
        return new Forward("begin");
    }
}
