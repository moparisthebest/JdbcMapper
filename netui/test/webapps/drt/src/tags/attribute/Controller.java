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
package tags.attribute;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.upload.FormFile;

import java.io.Serializable;

@Jpf.Controller()
public class Controller extends PageFlowController
{
    // used for the radioButtonOption tag test
    private String defaultOption = "Two";
    public String getDefaultOption()
    {
        return defaultOption;
    }
    public void setDefaultOption(String option)
    {
        defaultOption = option;
    }

    // used for the checkBoxOption tag test
    private String[] defaultOptions = new String[] {"One", "Two", "Three", "Four"};
    public String[] getDefaultOptions()
    {
        return defaultOptions;
    }
    public void setDefaultOptions(String[] options)
    {
        defaultOptions = options;
    }

    // used for the select, selectOption tag tests
    private String[] defaultSelectedItems = new String[] {"One", "Two"};
    public String[] getDefaultSelectedItems()
    {
        return defaultSelectedItems;
    }
    public void setDefaultSelectedItems(String[] defaultSelectedItems)
    {
        this.defaultSelectedItems = defaultSelectedItems;
    }

    // used for the checkBoxOption, radioButtonOption, selectOption tag tests
    private String optionOne = "One";
    public String getOptionOne(){return optionOne;}
    public void setOptionOne(String option) {optionOne = option;}
    private String optionTwo = "Two";
    public String getOptionTwo(){return optionTwo;}
    public void setOptionTwo(String option) {optionTwo = option;}
    private String optionThree = "Three";
    public String getOptionThree(){return optionThree;}
    public void setOptionThree(String option) {optionThree = option;}

    // used for the hidden tag test
    public String hiddenParam = "hiddenValue";
    public String getHiddenParam(){
        return hiddenParam;
    }
    public void setHiddenParam(String myParam){
        hiddenParam = myParam;
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", path="index.jsp")
    })
    protected Forward begin()
    {
        return new Forward("success");
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", path="index.jsp")
    })
    protected Forward submitIt(SubmitItForm form)
    {
        return new Forward("success");
    }

    /**
     * FormData with get and set methods for various tests
     */
    public static class SubmitItForm implements Serializable
    {
        // general property for tests with form
        private String theProperty;
        public void setTheProperty(String property)
        {
            theProperty = property;
        }
        public String getTheProperty()
        {
            return theProperty;
        }

        // property for checkBox tag test
        private Boolean boxChecked;
        public void setBoxChecked(Boolean checked)
        {
            boxChecked = checked;
        }
        public Boolean getBoxChecked()
        {
            return boxChecked;
        }

        // property for checkBoxOption tag test
        private String[] checkedBoxes;
        public void setCheckedBoxes(String[] checkedBoxes)
        {
            this.checkedBoxes = checkedBoxes;
        }
        public String[] getCheckedBoxes()
        {
            return this.checkedBoxes;
        }

        // property for fileUpload tag test
        private FormFile theFile;
        public void setTheFile(FormFile file)
        {
            theFile = file;
        }
        public FormFile getTheFile()
        {
            return theFile;
        }

        // property for select tag test
        private String[] selectedItems;
        public void setSelectedItems(String[] items)
        {
            selectedItems = items;
        }
        public String[] getSelectedItems()
        {
            return selectedItems;
        }
    }
}
