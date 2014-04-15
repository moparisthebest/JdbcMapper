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
package miniTests.tiles;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.http.HttpServletRequest;

// Note: normally, a tiles definitions config file would go in WEB-INF.
// It's here (tiles-defs.xml) to encapsulate the test.
@Jpf.Controller(
    tilesDefinitionsConfigs = { "/miniTests/tiles/tiles-defs.xml" },
    simpleActions={
        @Jpf.SimpleAction(name="begin", tilesDefinition="defaultPage")
    })
public class Controller extends PageFlowController
{
    protected StringBuffer _sb = new StringBuffer();
    public String getResults()
    {
        return _sb.toString();
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "continue",
                tilesDefinition = "defaultPage")
        })
    public Forward home()
    {
        doWork();
        return new Forward("continue");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "continue",
                tilesDefinition = "defaultStrutsPage")
        })
    public Forward struts()
    {
        doWork();
        return new Forward("continue");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "continue",
                tilesDefinition = "blankPage")
        })
    public Forward blank()
    {
        doWork();
        return new Forward("continue");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "continue",
                tilesDefinition = "editMenuPage")
        })
    public Forward edit()
    {
        doWork();
        return new Forward("continue");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "continue",
                tilesDefinition = "reversePanels")
        })
    public Forward reversePanels()
    {
        doWork();
        return new Forward("continue");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "continue",
                tilesDefinition = "reverseBody")
        })
    public Forward changeBody()
    {
        doWork();
        return new Forward("continue");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "continue",
                tilesDefinition = "j158Page")
        })
    public Forward alternate()
    {
        doWork();
        return new Forward("continue");
    }
    
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "continue",
                tilesDefinition = "alternateLayout")
        })
    public Forward j158()
    {
        doWork();
        return new Forward("continue");
    }
    
    protected void doWork()
    {
        _sb = new StringBuffer();

        HttpServletRequest req = getRequest();
        _sb.append("Request URI: " + req.getRequestURI());
        _sb.append("\n");
    }
}

