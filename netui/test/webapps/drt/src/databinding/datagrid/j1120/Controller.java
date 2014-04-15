/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.    
 *    
 * $Header:$
 */
package databinding.datagrid.j1120;

import javax.servlet.http.HttpSession;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import java.util.List;
import java.util.ArrayList;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller
    extends PageFlowController
{
    public Pet[] pets;
    public Pet[] getPets() { return pets; }

    /**
     * Method that is invoked when this controller instance is created.
     */
    protected void onCreate()
    {
       pets = new Pet[2];
       pets[0] = new Pet("1", "dog", "white", "10.00");
       pets[1] = new Pet("2", "dog", "purple", "10.00");
    }

    public class Pet {
        private String _petId;
        private String _name;
        private String _description;
        private String _price;

        public Pet(String id, String name, String desc, String price) {
            _petId = id;
            _name = name;
            _description = desc;
            _price = price;
        }
        public String getPetId() { return _petId; }
        public String getName() { return _name; }
        public String getDescription() { return _description; }
        public String getPrice() { return _price; }
    }

    protected void onDestroy(HttpSession session)
    {
    }

    @Jpf.Action(
        forwards={@Jpf.Forward(name="success", path="index.jsp")})
    public Forward submit() {
        Forward fwd = new Forward("success");
        return fwd;
    }

    @Jpf.Action(
        forwards={@Jpf.Forward(name="success", path="index.jsp")})
    public Forward save() {
        Forward fwd = new Forward("success");
        return fwd;
    }

    @Jpf.Action(
        forwards={@Jpf.Forward(name="success", path="index.jsp")})
    public Forward sortingAction() {
        Pet p = pets[0];
        pets[0] = pets[1];
        pets[1] = p;
        Forward fwd = new Forward("success");
        return fwd;
    }
}
