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
package bugs.b35084;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;


/**
 */
@Jpf.Controller
public class Controller extends PageFlowController
{
    private String _name = "foo";
    public void setName(String name) {
        _name = name;
    }
    public String getName() {
        return _name;
    }

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
                path = "lastName.jsp")
        })
    public Forward start(NameActionForm form)
    {
        return new Forward( "success", form);
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "confirm",
                path = "done.jsp")
        })
    public Forward next(NameActionForm form)
    {
        return new Forward("confirm");
    }

    public static class NameActionForm implements Serializable
    {
        private java.lang.String firstname;
        private java.lang.String lastname;

        public void setLastname(java.lang.String lastname)
        { this.lastname = lastname; }

        public java.lang.String getLastname()
        { return this.lastname; }

        public void setFirstname(java.lang.String firstname)
        { this.firstname = firstname; }

        public java.lang.String getFirstname()
        { return this.firstname; }
    }
}
