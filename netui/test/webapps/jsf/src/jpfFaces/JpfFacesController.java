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
package jpfFaces;

import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller
public class JpfFacesController extends PageFlowController
{
    public static class BarForm extends FormData
    {
        private String _bar;

        public void setBar( String bar )
        {
            _bar = bar;
        }

        public String getBar()
        {
            return _bar;
        }
    }
    
    @Jpf.Action(forwards=@Jpf.Forward(name="page1", path="page1.faces"))
    public Forward begin()
    {
        return new Forward( "page1" );
    }

    @Jpf.Action(forwards=@Jpf.Forward(name="page1", path="page1.faces"))
    public Forward go1()
    {
        return new Forward( "page1" );
    }

    @Jpf.Action(forwards=@Jpf.Forward(name="page2", path="page2.faces"))
    public Forward go2( BarForm form )
    {
        getRequest().setAttribute( "message", "got form; bar was '" + form.getBar() + "'" );
        return new Forward( "page2" );
    }

    @Jpf.Action(forwards=@Jpf.Forward(name="page3", path="page3.faces"))
    public Forward go3()
    {
        return new Forward( "page3" );
    }

    @Jpf.Action(forwards=@Jpf.Forward(name="page4", path="page4.faces"))
    public Forward go4()
    {
        return new Forward( "page4" );
    }
}
