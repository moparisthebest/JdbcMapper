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
package search;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;

import org.apache.beehive.samples.petstore.forms.SearchForm;
import org.apache.beehive.samples.petstore.model.Product;
import org.apache.beehive.samples.petstore.controls.CatalogControl;
import org.apache.beehive.controls.api.bean.Control;

@Jpf.Controller(
    nested = true,
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="rootSharedFlow", type=webappRoot.SharedFlow.class)
    }
)
public class Controller
    extends PageFlowController {

    @Control()
    private CatalogControl _catalogControl;

    @Jpf.SharedFlowField(name="rootSharedFlow")
    private webappRoot.SharedFlow _sharedFlow;

    @Jpf.Action(
        forwards = { 
            @Jpf.Forward(name = "success", path = "index.jsp",
                         actionOutputs = { 
                             @Jpf.ActionOutput(name = "searchResults",
                                               type = org.apache.beehive.samples.petstore.model.Product[].class,
                                               required = false)
                         })
        }
    )
    protected Forward begin(SearchForm form) {
        Product[] searchResults = _catalogControl.searchProductList(form.getKeyword());
        
        return new Forward("success", "searchResults", searchResults);
    }

    @Jpf.Action(
        forwards = { 
            @Jpf.Forward(name = "done", returnAction = "begin")
        }
    )
    public Forward done() {
        return new Forward("done");
    }
}