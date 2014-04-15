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
package tiles.pf.anotherInherit;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    tilesDefinitionsConfigs = {
        "/WEB-INF/tiles-defs-common.xml",
        "/tiles/pf/tiles-defs.xml",
        "/tiles/pf/anotherInherit/tiles-defs.xml"
    },
    simpleActions={
        @Jpf.SimpleAction(
            name="begin",
            tilesDefinition = "localPage")
    }
)
public class Controller extends tiles.pf.Controller
{
    // forward to a different tiles definition.
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "continue",
                tilesDefinition = "overridePage")
        })
    public Forward another()
    {
        doWork("Overriding action, another");
        return new Forward("continue");
    }

    // forward to a local tiles definition.
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "continue",
                tilesDefinition = "localPage")
        })
    public Forward local()
    {
        doWork("new derived class action, local");
        return new Forward("continue");
    }
}

