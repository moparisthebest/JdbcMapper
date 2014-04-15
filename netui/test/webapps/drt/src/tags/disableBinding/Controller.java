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
package tags.disableBinding;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

/**
 * @jpf:controller
 */
@Jpf.Controller(
    )
public class Controller extends PageFlowController
{
    public boolean disableBinding = true;
    public String[] radioOptions = {"Radio 1", "Radio 2", "Radio 3"};
    public String[] selectOptions = {"Option 1", "Option 2", "Option 3"};
    public String[] checkOptions = {"Check 1", "Check 2", "Check 3"};

    public boolean getDisableBinding() {
        return disableBinding;
    }
    public String[] getRadioOptions() {
        return radioOptions;
    }
    public String[] getSelectOptions() {
        return selectOptions;
    }
    public String[] getCheckOptions() {
        return checkOptions;
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
    protected Forward begin()
    {
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
    protected Forward flipBinding()
    {
        disableBinding = !disableBinding;
        return new Forward( "success" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="results.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "results.jsp") 
        })
    protected Forward submitForm(Form form)
    {
        getRequest().setAttribute("form",form);
        return new Forward("success");
    }

    public static class Form implements Serializable
    {
        private String textBox;
        private String textArea;
        private String[] select;
        private String[] selectTwo;
        private String[] selectThree;
        private String radioGroup;
        private String radioGroup2;
        private boolean check1 = true;
        private boolean check2 = false;
        private String[] checkOne;
        private String[] checkTwo;

        public String getTextBox()
        {
            return textBox;
        }

        public void setTextBox(String textBox)
        {
            this.textBox = textBox;
        }

        public String getTextArea()
        {
            return textArea;
        }

        public void setTextArea(String textArea)
        {
            this.textArea = textArea;
        }

        public String[] getSelect()
        {
            return select;
        }

        public void setSelect(String[] select)
        {
            this.select = select;
        }

        public String[] getSelectTwo()
        {
            return selectTwo;
        }

        public void setSelectTwo(String[] selectTwo)
        {
            this.selectTwo = selectTwo;
        }

        public String[] getSelectThree()
        {
            return selectThree;
        }

        public void setSelectThree(String[] selectThree)
        {
            this.selectThree = selectThree;
        }

        public String getRadioGroup()
        {
            return radioGroup;
        }

        public void setRadioGroup(String radioGroup)
        {
            this.radioGroup = radioGroup;
        }

        public String getRadioGroup2()
        {
            return radioGroup2;
        }

        public void setRadioGroup2(String radioGroup2)
        {
            this.radioGroup2 = radioGroup2;
        }

        public boolean isCheck1()
        {
            return check1;
        }

        public void setCheck1(boolean check1)
        {
            this.check1 = check1;
        }

        public boolean isCheck2()
        {
            return check2;
        }

        public void setCheck2(boolean check2)
        {
            this.check2 = check2;
        }

        public String[] getCheckOne()
        {
            return checkOne;
        }

        public void setCheckOne(String[] checkOne)
        {
            this.checkOne = checkOne;
        }

        public String[] getCheckTwo()
        {
            return checkTwo;
        }

        public void setCheckTwo(String[] checkTwo)
        {
            this.checkTwo = checkTwo;
        }
     }
}
