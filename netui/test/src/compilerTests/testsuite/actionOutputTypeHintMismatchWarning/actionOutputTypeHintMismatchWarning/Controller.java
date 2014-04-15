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
package actionOutputTypeHintMismatchWarning;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import java.util.*;

/**
 * Test to ensure that there are warnings when the typeHint doesn't match the type on @Jpf.ActionOutput.
 */
@Jpf.Controller
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="dne",
                path="doesnotexist",
                actionOutputs={
                    @Jpf.ActionOutput(name="good1", type=ArrayList.class, typeHint="java.util.ArrayList<String>"),
                    @Jpf.ActionOutput(name="good2", type=List.class, typeHint="java.util.ArrayList<String>"),
                    @Jpf.ActionOutput(name="good3", type=ArrayList.class, typeHint="java.util.ArrayList"),
                    @Jpf.ActionOutput(name="bad1", type=ArrayList.class, typeHint="java.util.HashSet"),
                    @Jpf.ActionOutput(name="bad2", type=ArrayList.class, typeHint="java.util.HashSet<String>"),
                    @Jpf.ActionOutput(name="bad3", type=ArrayList.class, typeHint="SomeUnresolvableType")
                }
            )
        }
    )
    public Forward begin()
    {
        return new Forward("dne");
    }
}
