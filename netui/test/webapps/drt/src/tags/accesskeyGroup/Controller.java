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
package tags.accesskeyGroup;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.util.tags.GroupOption;

import java.io.Serializable;


@Jpf.Controller()
public class Controller extends PageFlowController
{    
    private GroupOption[] attributes = {
        new GroupOption("fenders (1)","fenders","finders",'1'),
        new GroupOption("wheels (2)","wheels","wheels",'2'),
        null,
        new GroupOption("windows (3)","windows","windows",'3'),
        };
        
    private GroupOption[] colors = {
        new GroupOption("red (a)","red","red",'A'),
        new GroupOption("blue (b)","blue","blue",'B'),
        null,
        new GroupOption("yellow (c)","yellow","yellow",'C'),
        };

    public GroupOption[] getAttributes() {
        return attributes;
    }
    public GroupOption[] getColors() {
        return colors;
    }


    // Uncomment this declaration to access Global.app.
    // 
    //     protected global.Global globalApp;
    // 

    // For an example of page flow exception handling see the example "catch" and "exception-handler"
    // annotations in {project}/WEB-INF/src/global/Global.app

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward begin()
    {
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "page2.jsp") 
        })
    protected Forward post(MyBean form)
    {
        return new Forward("success","form",form);
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class MyBean implements Serializable
    {
        private String[] attributes;

        private String type;

        public void setType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return this.type;
        }

        public void setAttributes(String[] attributes)
        {
            this.attributes = attributes;
        }

        public String[] getAttributes()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if(this.attributes == null || this.attributes.length == 0)
            {
                this.attributes = new String[1];
            }

            return this.attributes;
        }
    }
}
