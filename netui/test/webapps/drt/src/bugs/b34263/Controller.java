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
package bugs.b34263;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

/**
 * @jpf:controller
 */
@Jpf.Controller()
public class Controller extends PageFlowController
{
    private boolean advanced = true;
    public boolean getAdvanced() {
        return advanced;
    }

    private boolean verbose = false;
    public boolean getVerbose() {
        return verbose;
    }
    
   /**
     * @jpf:action
     * @jpf:forward name="begin" path="begin.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "begin.jsp") 
        })
    public Forward begin()
    {
        return new Forward("begin");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "begin.do") 
        })
    protected Forward EnterName(Name form)
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="begin.do"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "begin.do") 
        })
    protected Forward EnterSearch(Search form)
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="begin.do"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "begin.do") 
        })
    protected Forward EnterAdvancedSearch(Search form)
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="begin.do"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "begin.do") 
        })
    protected Forward DeleteName(Name form)
    {
        return new Forward("success");
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class Search implements Serializable
    {
        private java.lang.String query;

        public void setQuery(java.lang.String query)
        {
            this.query = query;
        }

        public java.lang.String getQuery()
        {
            return this.query;
        }
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class Name implements Serializable
    {
        private java.lang.String lastName;

        private java.lang.String firstName;

        public void setFirstName(java.lang.String firstName)
        {
            this.firstName = firstName;
        }

        public java.lang.String getFirstName()
        {
            return this.firstName;
        }

        public void setLastName(java.lang.String lastName)
        {
            this.lastName = lastName;
        }

        public java.lang.String getLastName()
        {
            return this.lastName;
        }
    }
}
