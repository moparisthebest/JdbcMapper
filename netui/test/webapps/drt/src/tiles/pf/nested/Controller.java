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
package tiles.pf.nested;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.http.HttpServletRequest;

@Jpf.Controller(
    nested = true,
    tilesDefinitionsConfigs = {
        "/WEB-INF/tiles-defs-common.xml",
        "/tiles/pf/tiles-defs.xml",
        "/tiles/pf/nested/tiles-defs.xml"
    }
)
public class Controller extends PageFlowController
{
    StringBuffer _sb = new StringBuffer();
    public String getResults()
    {
        return _sb.toString();
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                tilesDefinition = "nestedPage")
        })
    public Forward begin()
    {
        _sb = new StringBuffer();
        return new Forward("begin");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "home") 
        })
    public Forward backOne()
    {
        doWork("in backOne(), returnAction = home");
	return new Forward("return");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "another") 
        })
    public Forward backTwo()
    {
        doWork("in backOne(), returnAction = another");
	return new Forward("return");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "continue",
                tilesDefinition = "nestedPage")
        })
    public Forward nestOne()
    {
        doWork("in nestOne(), go to local tiles def - nestedPage");
        return new Forward("continue");
    }

    private void doWork(String msg)
    {
        _sb = new StringBuffer(msg);
        _sb.append("\n");
        HttpServletRequest req = getRequest();
        _sb.append("Request URI: " + req.getRequestURI());
        _sb.append("\n");
    }
}

