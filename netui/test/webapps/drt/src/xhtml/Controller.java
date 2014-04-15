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
package xhtml;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.tags.tree.TreeElement;

import java.io.Serializable;

@Jpf.Controller()
public class Controller extends PageFlowController
{
    private TreeElement _tree1;

    // checkbox tests
    private String[] checkBoxOptions = {"CBG Option 1", "CBG Option 2", "CBG Option 3"};    
    private String[] checkBoxMapOptions = {"Map Option 1", "Map Option 2"};
    
    // radiobutton tests
    private String[] radioButtonOptions = {"RB Option 1","RB Option 2", "RB Option 3"};
    private String[] radioButtonMapOptions = {"RB Map 1", "RB Map 2"};
    
    // select tests
    private String[] selectOptions = {"Select Option 1", "Select Option 2", "Select Option 3"};
    private String[] selectMapOptions = {"Sel Opt 1", "Sel Opt 2"};
    private String[] nullSelectOptions = {"Null Opt 1", "Null Opt 2", "Null Opt 3"};
    private String selectDefault = "Default Opt";
        
    private Options[] opts;
    private String labelOne = "Label One";

    public String[] getCheckBoxOptions() {return checkBoxOptions;}
    public String[] getCheckBoxMapOptions() {return checkBoxMapOptions;}
    public String[] getRadioButtonOptions() {return radioButtonOptions;}
    public String[] getRadioButtonMapOptions() {return radioButtonMapOptions;}
    public String[] getSelectOptions() {return selectOptions;}
    public String[] getSelectMapOptions() {return selectMapOptions;}
    public String[] getNullSelectOptions() {return nullSelectOptions;}
    public String getSelectDefault() {return selectDefault;}
    public Options[] getOpts() {return opts;}
    public String getLabelOne() {return labelOne;}
    
    public TreeElement getTree1() {
        return _tree1;
    }

    public void setTree1(TreeElement tn) {
        _tree1 = tn;
    }

    protected void onCreate()
    {
        // version that is used by all the options
        
        // initialize the opts
        opts = new Options[3];
        opts[0] = new Options("Option One","opt-1", "normal");
        opts[1] = new Options("Option Two","opt-2", "normal2");
        opts[2] = new Options("Option Three","opt-3", "normal3");
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "index",
                path = "index.jsp") 
        })
    protected Forward begin()
    {
        return new Forward("index");
    }


      
      
    /**
     * @jpf:action
     * @jpf:forward name="success" path="anchorTargets.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "anchorTargets.jsp") 
        })
    protected Forward goAnchorTarget()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="anchorTest.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "anchorTest.jsp") 
        })
    protected Forward goAnchorTest()
    {
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "treeTest.jsp") 
        })
    protected Forward goTreeTest()
    {
        return new Forward("success");
    }

    @Jpf.Action(forwards = { @org.apache.beehive.netui.pageflow.annotations.Jpf.Forward(name = "success", navigateTo = org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo.currentPage)})
    protected Forward postback()
    {
        Forward forward = new Forward("success");
        return forward;
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="checkboxTest.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "checkboxTest.jsp") 
        })
    protected Forward goCheckboxTest()
    {
        return new Forward("success");
    }


    /**
     * @jpf:action
     * @jpf:forward name="success" path="checkboxResults.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "checkboxResults.jsp") 
        })
    protected Forward postCheckForm(CheckboxTests form)
    {
        Forward f = new Forward("success");
        f.addPageInput("formData",form);
        f.addPageInput("action","postFormOne");
        return f;
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="checkboxResults.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "checkboxResults.jsp") 
        })
    protected Forward postCheckFormTwo(CheckboxTests form)
    {
        Forward f = new Forward("success");
        f.addPageInput("formData",form);
        f.addPageInput("action","postFormTwo");
        return f;
    }
    
    /**
     * @jpf:action
     * @jpf:forward path="formLabelResults.jsp" name="success"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                path = "formLabelResults.jsp",
                name = "success") 
        })
    protected Forward postFormLabelResults(FormLabelTests form)
    {
        Forward f = new Forward("success");
        f.addPageInput("formData",form);        
        return f;
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="radioTest.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "radioTest.jsp") 
        })
    protected Forward goRadioTest()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="radioResults.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "radioResults.jsp") 
        })
    protected Forward postRadioForm(xhtml.Controller.RadioTests form)
    {
        Forward f = new Forward("success");
        f.addPageInput("formData",form);
        f.addPageInput("action","postFormOne");
        return f;
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="selectTest.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "selectTest.jsp") 
        })
    protected Forward goSelectTest()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="selectResults.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "selectResults.jsp") 
        })
    protected Forward postSelectForm(xhtml.Controller.SelectTests form)
    {
        Forward f = new Forward("success");
        f.addPageInput("formData",form);
        f.addPageInput("action","postFormOne");
        return f;
    }
    
    public static class Options implements java.io.Serializable {
        private String _name;
        private String _optionValue;
        private String _style;
        
        public Options(String name, String value, String style) {
            _name = name;
            _optionValue = value;
            _style = style;
        }
        
        public void setName(String name) {
            _name = name;
        }
        public String getName() {
            return _name;
        }

        public void setOptionValue(String optionValue) {
            _optionValue = optionValue;
        }
        public String getOptionValue() {
            return _optionValue;
        }
        
        public void setStyle(String style) {
            _style = style;
        }
        public String getStyle() {
            return _style;
        }
    }

    public static class CheckboxTests implements Serializable
    {
        private String[] optCbgMap = {"opt-1"};

        private String[] optCbg = {"CBG Option 2"};

        private String[] cbg = {"CheckBox Option 2"};

        private boolean check2 = true;      // force a checked

        private boolean check1;

        public void setCheck1(boolean check1)
        {
            this.check1 = check1;
        }

        public boolean isCheck1()
        {
            return this.check1;
        }

        public void setCheck2(boolean check2)
        {
            this.check2 = check2;
        }

        public boolean isCheck2()
        {
            return this.check2;
        }

        public void setCbg(java.lang.String[] cbg)
        {
            this.cbg = cbg;
        }

        public String[] getCbg()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if(this.cbg == null || this.cbg.length == 0)
            {
                this.cbg = new String[1];
            }

            return this.cbg;
        }

        public void setOptCbg(java.lang.String[] optCbg)
        {
            this.optCbg = optCbg;
        }

        public String[] getOptCbg()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if(this.optCbg == null || this.optCbg.length == 0)
            {
                this.optCbg = new String[1];
            }

            return this.optCbg;
        }

        public void setOptCbgMap(java.lang.String[] optCbgMap)
        {
            this.optCbgMap = optCbgMap;
        }

        public String[] getOptCbgMap()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if(this.optCbgMap == null || this.optCbgMap.length == 0)
            {
                this.optCbgMap = new String[1];
            }

            return this.optCbgMap;
        }
    }


    public static class RadioTests implements Serializable
    {
        private String rbgOptsMap = "RB Map 2";

        private String rbgOpts = "RB Option 3";

        private String rbg = "Radio Opt 1";
        
        private String rbgComplex = "opt-13";

        public void setRbg(String rbg)
        {
            this.rbg = rbg;
        }

        public String getRbg()
        {
            return rbg;
        }

        public void setRbgOpts(String rbgOpts)
        {
            this.rbgOpts = rbgOpts;
        }

        public String getRbgOpts()
        {
            return rbgOpts;
        }

        public void setRbgOptsMap(java.lang.String rbgOptsMap)
        {
            this.rbgOptsMap = rbgOptsMap;
        }

        public String getRbgOptsMap()
        {
            return this.rbgOptsMap;
        }
        
        public void setRbgComplex(java.lang.String rbgComplex)
        {
            this.rbgComplex = rbgComplex;
        }

        public String getRbgComplex()
        {
            return this.rbgComplex;
        }
    }

    public static class SelectTests implements Serializable
    {
        private String[] nullable = {"Form Opt 1", "Form Opt 2"};

        private String selOpts;

        private String[] multiSel;

        private String sel;

        public void setSel(java.lang.String sel)
        {
            this.sel = sel;
        }

        public String getSel()
        {
            return this.sel;
        }

        public void setMultiSel(java.lang.String[] multiSel)
        {
            this.multiSel = multiSel;
        }

        public String[] getMultiSel()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if(this.multiSel == null || this.multiSel.length == 0)
            {
                this.multiSel = new String[1];
            }

            return this.multiSel;
        }

        public void setSelOpts(java.lang.String selOpts)
        {
            this.selOpts = selOpts;
        }

        public String getSelOpts()
        {
            return this.selOpts;
        }

        public void setNullable(java.lang.String[] nullable)
        {
            this.nullable = nullable;
        }

        public String[] getNullable()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if(this.nullable == null || this.nullable.length == 0)
            {
                this.nullable = new String[1];
            }

            return this.nullable;
        }
    }
      
      
    /**
     * @jpf:action
     * @jpf:forward path="imageTest.jsp" name="success"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                path = "imageTest.jsp",
                name = "success") 
        })
    protected Forward goImageTest()
    {
        return new Forward("success");
    }

      
    /**
     * @jpf:action
     * @jpf:forward path="imageResults.jsp" name="success"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                path = "imageResults.jsp",
                name = "success") 
        })
    protected Forward postImageForm()
    {
        return new Forward("success");
    }

      
    /**
     * @jpf:action
     * @jpf:forward path="labelTest.jsp" name="success"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                path = "labelTest.jsp",
                name = "success") 
        })
    protected Forward goLabelTest()
    {
        return new Forward("success");
    }

      
    /**
     * @jpf:action
     * @jpf:forward path="formLabelTest.jsp" name="success"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                path = "formLabelTest.jsp",
                name = "success") 
        })
    protected Forward goFormLabelTest()
    {
        return new Forward("success");
    }

      

    public static class FormLabelTests implements Serializable 
    {
        private String valueThree;

        private String valueTwo;

        private String valueOne;

        public String getValueOne()
                {
            return this.valueOne;
            }


        public void setValueOne(String valueOne)
                {
            this.valueOne = valueOne;
            }


        public String getValueTwo()
                {
            return this.valueTwo;
            }


        public void setValueTwo(String valueTwo)
                {
            this.valueTwo = valueTwo;
            }


        public String getValueThree()
                {
            return this.valueThree;
            }


        public void setValueThree(String valueThree)
                {
            this.valueThree = valueThree;
            }

    }
      
}
