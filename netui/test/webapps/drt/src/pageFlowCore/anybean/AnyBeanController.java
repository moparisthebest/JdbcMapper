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
package pageFlowCore.anybean;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller
public class AnyBeanController extends PageFlowController
{
    private SomeBean _someBean;


    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        })
    protected Forward begin()
    {
        return new Forward( "index" );
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="submitPage", path="submit.jsp")
        })
    protected Forward showSubmit()
    {
        SomeBean init = new SomeBean();
        init.setFoo( "initial value" );
        return new Forward( "submitPage", init );
    }

    @Jpf.Action(
        useFormBean="_someBean",
        forwards={ 
            @Jpf.Forward(name = "success", path = "result.jsp")
        })    
    protected Forward submit( SomeBean bean )    
    {
        return new Forward( "success", "foo", bean.getFoo() );
    }
    
    public static class SomeBean
        implements Serializable
    {
        private String _foo;
        
        public SomeBean()
        {
        }

        public SomeBean( String foo )
        {
            _foo = foo;
        }

        public String getFoo()
        {
            return _foo;
        }

        public void setFoo( String foo )
        {
            _foo = foo;
        }
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name = "success", path = "/pageFlowCore/anybean/nested/NestedController.jpf")
        })
    protected Forward goNested()
    {
        return new Forward("success", new pageFlowCore.anybean.nested.NestedController.Input( "hello" ));
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="nestingResult", path="nestingResult.jsp")
        })
    protected Forward nestedDone( pageFlowCore.anybean.nested.NestedController.Output output )
    {
        return new Forward( "nestingResult", "result", output );
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="submit", path="submit.do")
        })
    protected Forward chain()
    {
        return new Forward( "submit", new SomeBean( "from a chained action" ) );
    }
}
