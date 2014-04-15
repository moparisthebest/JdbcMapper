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
package bugs.b39552;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "success",
            path = "index.jsp") 
    })
public class Controller extends PageFlowController
{

    private Map jpfMap = null;
    private Map jpfRadio = null;
    private Map jpfCheck = null;

    public Map getJpfMap() {
        return jpfMap;
    }
    public Map getJpfRadio() {
        return jpfRadio;
    }
    public Map getJpfCheck() {
        return jpfCheck;
    }
    
    protected void onCreate()
    {
        jpfMap = new LinkedHashMap();
        jpfMap.put("1", "Option 1");
        jpfMap.put(null, "Option 2");
        jpfMap.put("3", "Option 3");
        jpfMap.put("4", "Option 4");
        jpfMap.put("5", "Option 5");
        
        jpfRadio = new LinkedHashMap();
        jpfRadio.put("1", "Option 1");
        jpfRadio.put(null, "Option 2");
        jpfRadio.put("3", "Option 3");
        jpfRadio.put("4", "Option 4");
        jpfRadio.put("5", "Option 5");
        
        jpfCheck = new LinkedHashMap();
        jpfCheck.put("1", "Option 1");
        jpfCheck.put(null, "Option 2");
        jpfCheck.put("3", "Option 3");
        jpfCheck.put("4", "Option 4");
        jpfCheck.put("5", "Option 5");
   }
    
    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward begin()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward postback(PostbackForm form)
    {
        return new Forward("success");
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class PostbackForm implements Serializable
    {
        private String[] selectedItems;

        public void setSelectedItems(String[] selectedItems)
        {
            this.selectedItems = selectedItems;
        }

        public String[] getSelectedItems()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if(this.selectedItems == null || this.selectedItems.length == 0)
            {
                this.selectedItems = new String[1];
            }

            return this.selectedItems;
        }

        private String radioItem;

        public void setRadioItem(String radioItem)
        {
            this.radioItem = radioItem;
        }

        public String getRadioItem()
        {
            return this.radioItem;
        }

        private String[] checkboxItems;

        public void setCheckboxItems(String[] checkboxItems)
        {
            this.checkboxItems = checkboxItems;
        }

        public String[] getCheckboxItems()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if(this.checkboxItems == null || this.checkboxItems.length == 0)
            {
                this.checkboxItems = new String[1];
            }

            return this.checkboxItems;
        }
    }
}
