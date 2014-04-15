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
package pageFlowCore.pfPageInput;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "globalSuccessMissingNotNullable",
            actionOutputs={@Jpf.ActionOutput(name="missingNotNullable", type=String.class)},
            navigateTo = Jpf.NavigateTo.currentPage),
        @Jpf.Forward(
            name = "globalSuccessMismatched",
            actionOutputs={@Jpf.ActionOutput(name="mismatched", type=String.class)},
            navigateTo = Jpf.NavigateTo.currentPage),
        @Jpf.Forward(
            name = "globalSuccessGood",
            actionOutputs={@Jpf.ActionOutput(name="goodArray", type=String[].class)},
            navigateTo = Jpf.NavigateTo.currentPage),
        @Jpf.Forward(
            name = "globalSuccessMissingButNullable",
            actionOutputs={@Jpf.ActionOutput(name="missingButNullable", type=String.class, required=false)},
            navigateTo = Jpf.NavigateTo.currentPage) 
    }
)
public class PageInputController extends PageFlowController
{
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
        return new Forward( "index" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                actionOutputs={@Jpf.ActionOutput(name="missingNotNullable", type=String.class)},
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
    protected Forward missingNotNullable1()
    {
        return new Forward( "success" );
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward missingNotNullable2()
    {
        return new Forward( "globalSuccessMissingNotNullable" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                actionOutputs={@Jpf.ActionOutput(name="mismatched", type=String.class)},
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
    protected Forward mismatched1()
    {
        return new Forward( "success", "mismatched", new Integer( 0 ) );
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward mismatched2()
    {
        return new Forward( "globalSuccessMismatched", "mismatched", new String[]{ "hi" } );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                actionOutputs={@Jpf.ActionOutput(name="goodString", type=String.class)},
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
    protected Forward good1()
    {
        return new Forward( "success", "goodString", "hello" );
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward good2()
    {
        return new Forward( "globalSuccessGood", "goodArray", new String[]{ "hello", "there" } );
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" page-inputs="missingButNullable" return-to="currentPage"
     * @jpf:page-input name="missingButNullable" type="String"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                actionOutputs={@Jpf.ActionOutput(name="missingButNullable", type=String.class, required=false)},
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
    protected Forward missingButNullable1()
    {
        return new Forward( "success" );
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward missingButNullable2()
    {
        return new Forward( "globalSuccessMissingButNullable" );
    }


    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                actionOutputs={
                    @Jpf.ActionOutput(name="pi1", type=Serializable.class),
                    @Jpf.ActionOutput(name="pi2", type=String[].class),
                    @Jpf.ActionOutput(name="pi3", type=String[][].class)
                },
                path = "lots.jsp") 
        })
    protected Forward lots()
    {
        Forward fwd = new Forward("success");
        fwd.addPageInput( "pi1", "hi" );
        fwd.addPageInput( "pi2", new String[]{ "hi", "there" } );
        fwd.addPageInput( "pi3", new String[][]{ { "hi", "there" }, { "this", "works" } } );
        return fwd;
    }
}
