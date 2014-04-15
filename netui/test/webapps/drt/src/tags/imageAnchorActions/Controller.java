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
package tags.imageAnchorActions;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller
 * 
 */
@Jpf.Controller(
    )
public class Controller extends PageFlowController
{
    
    public static class Info implements java.io.Serializable {
        public Info(String action, String image) {
            this.action = action;
            this.image = image;
        }
        public String action;
        public String image;

        public String getAction()
        {
            return action;
        }

        public void setAction(String action)
        {
            this.action = action;
        }

        public String getImage()
        {
            return image;
        }

        public void setImage(String image)
        {
            this.image = image;
        }
    }

    private Info[] pageFlowActions = {new Info("ActionOne","/coreWeb/resources/images/back.gif"),
                             new Info("ActionTwo","/coreWeb/resources/images/cancel.gif"),
                             new Info("ActionThree","/coreWeb/resources/images/details.gif")
    };
    private String nullAction = null;
    private String lastAction = null;
    private String invalidAction = "invalidAction";

    public Info[] getPageFlowActions()
    {
        return pageFlowActions;
    }

    public String getNullAction()
    {
        return nullAction;
    }

    public String getLastAction()
    {
        return lastAction;
    }

    public String getInvalidAction()
    {
        return invalidAction;
    }
    /**
     * This method represents the point of entry into the pageflow
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward begin()
    {
        lastAction = "begin";
        return new Forward( "success" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="index.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward ActionOne()
    {
        lastAction = "ActionOne";
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="index.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward ActionTwo()
    {
        lastAction = "ActionTwo";
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="index.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward ActionThree()
    {
        lastAction = "ActionThree";
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="error.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "error.jsp") 
        })
    protected Forward goErrors()
    {
		return new Forward( "success" );
	}

    public String[] getSortedActions()
    {
        String[] actions = getActions();
        java.util.Arrays.sort( actions );
        return actions;
    }
}
