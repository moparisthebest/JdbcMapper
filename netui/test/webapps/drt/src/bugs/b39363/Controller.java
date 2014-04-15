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
package bugs.b39363;

// java imports
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// external imports

/**
 * @jpf:forward name="index" path="index.jsp"
 */
@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "index",
            path = "index.jsp") 
    })
public class Controller
    extends PageFlowController
{
    @Jpf.Action()
    public Forward begin()
    {
        ListForm form = new ListForm();
        List list = new ArrayList();
        list.add("foo1");
        list.add("bar1");
        form.setList(list);
        
        return new Forward("index", form);
    }

    @Jpf.Action()
    public Forward postback(ListForm form)
    {
        return new Forward("index");
    }

    public static class ListForm
        implements Serializable
    {
        private List list = null;

        public List getList()
        {
            return list;
        }
        
        public void setList(List list)
        {
            this.list = list;
        }

        public ListForm()
        {
            list = new ArrayList();
            // need to fill the list for the number of items
            // that might show up here
            list.add(null);
            list.add(null);
        }
    }
}
