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
package databinding.typeConverter;

// java imports

// internal imports
import databinding.type.Person;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

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

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward begin()
    {
        PersonForm form = new PersonForm();
        return new Forward("index", form);
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward postback(PersonForm form)
    {
        return new Forward("index");
    }

    public static class PersonForm
        implements Serializable
    {
        private Person person = null;
        
        public PersonForm()
        {
        }

        public Person getPerson()
        {
            return person;
        }

        public void setPerson(Person person)
        {
            this.person = person;
        }
    }
}
