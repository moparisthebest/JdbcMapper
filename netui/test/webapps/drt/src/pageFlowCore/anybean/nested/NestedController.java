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
package pageFlowCore.anybean.nested;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller( nested=true )
public class NestedController extends PageFlowController
{
    public static class Input implements Serializable
    {
        private String _msg;

        public Input( String msg )
        {
            _msg = msg;
        }

        public Input()
        {
        }

        public String getMsg()
        {
            return _msg;
        }

        public void setMsg( String msg )
        {
            _msg = msg;
        }
    }

    public static class Output extends Input
    {
        public Output( String msg )
        {
            super( msg );
        }

        public Output()
        {
            super( "default msg" );
        }
    }

    private Input _input = new Input();
    private Output _output;

    @Jpf.Action(
        useFormBean="_input",
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        })
    protected Forward begin( Input input )
    {
        return new Forward("index");
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="done", returnAction="nestedDone", outputFormBeanType=Output.class)
        })
    protected Forward done1()
    {
    	return new Forward( "done" );
    }

    @Jpf.Action(
        useFormBean="_output",
        forwards={
           @Jpf.Forward(name="done", returnAction="nestedDone", outputFormBean="_output")
        })
    protected Forward done2( Output output )
    {
        _output.setMsg( _input.getMsg() );
    	return new Forward( "done" );
    }
}
