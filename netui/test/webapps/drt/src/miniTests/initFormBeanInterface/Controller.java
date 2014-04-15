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
package miniTests.initFormBeanInterface;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller()
public class Controller extends PageFlowController
{
    private MyFormInterface _form = new MyForm();

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    public Forward begin()
    {
        MyForm bean = new MyForm();
        bean.setFoo( "got it" );
        return new Forward( "index", bean );
    }

    @Jpf.Action(
        useFormBean="_form",
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    public Forward submit( MyFormInterface form )
    {
        return new Forward( "index" );
    }

    public static class MyForm
        implements MyFormInterface, Serializable
    {
        private String _foo;

        public String getFoo()
        {
            return _foo;
        }

        public void setFoo( String foo )
        {
            _foo = foo;
        }
    }

    public interface MyFormInterface
    {
        public String getFoo();
        public void setFoo( String foo );
    }
}
