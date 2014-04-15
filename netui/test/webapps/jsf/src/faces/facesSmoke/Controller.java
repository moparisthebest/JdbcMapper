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
package faces.facesSmoke;

import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UISelectItem;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

@Jpf.Controller
public class Controller extends PageFlowController
{
    private Date _date;
    private int _number = 123456;
    private FormOne _formOne;
    private FormTwo _formTwo;
    private String _menuOne;
    private ListBean[] _list;
    
    private ArrayList _selectItems;
    
    public Date getDate() {
        return _date;
    }
    
    public int getNumber() {
        return _number;
    }
    
    public FormOne getFormOne() {
        return _formOne;
    }
    
    public void setFormOne(FormOne formOne) {
        _formOne = formOne;
    }
    
    public FormTwo getFormTwo() {
        return _formTwo;
    }
      
      
    public ArrayList getSelectItems() {
        return _selectItems;
    }
    
    public String getMenuOne() {
        return _menuOne;
    }
    
    public void setMenuOne(String menuOne) {
        _menuOne = menuOne;
    }
    
    public ListBean[] getList() {
        return _list;
    }
        
    public void onCreate()
    {
        try {
            
            HttpSession session = this.getSession();
            createBacking(session);
            session.setAttribute("PageFlow", this); 
            
            DateFormat df = DateFormat.getInstance();
            _date = df.parse("01/12/2000 4:5 PM, PDT");
            
            _formOne = new FormOne();
            _formOne.setText("x-initial Text Value");
            _formOne.setTextArea("initial Text Area Value"); 
            _formOne.setTextInt(80); 
            
            _formTwo = new FormTwo();
            
            _selectItems = new ArrayList();
            _selectItems.add(new SelectItem("Select Items One"));
            _selectItems.add(new SelectItem("Select Items Two"));
            
            _list = new ListBean[5];
            for (int i=0;i<_list.length;i++) {
                _list[i] = new ListBean("Name " + i, "Type " + i);
            }
            
            CustomRenderBacking crb = new CustomRenderBacking();
            crb.setPageFlow(this);
            session.setAttribute("CustomRenderBacking",crb);
                   
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createBacking(HttpSession session)
    {
            Backing b = new CustomRenderBacking();
            b.setPageFlow(this);
            session.setAttribute("CustomRenderBacking",b); 
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.faces"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "index",
                path = "index.faces") 
        })
    protected Forward begin()
    {
        return new Forward("index");
    }


    /**
     * @jpf:action
     * @jpf:forward name="success" path="Panels.faces"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "Panels.faces") 
        })
    protected Forward goPanels()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="Image.faces"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "Image.faces") 
        })
    protected Forward goImages()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="Anchors.faces"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "Anchors.faces") 
        })
    protected Forward goAnchors()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="Convert.faces"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "Convert.faces") 
        })
    protected Forward goConvert()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="FormOne.faces"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "FormOne.faces") 
        })
    protected Forward goFormOne()
    {
        return new Forward("success");
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="Bundle.faces"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "Bundle.faces") 
        })
    protected Forward goBundle()
    {
        return new Forward("success");
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="FormTwo.faces"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "FormTwo.faces") 
        })
    protected Forward goFormTwo()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="DataTable.faces"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "DataTable.faces") 
        })
    protected Forward goDataTable()
    {
        return new Forward("success");
    }

    public static class FormOne extends org.apache.beehive.netui.pageflow.FormData
    {
        private String password;

        private int textInt;

        private String textArea;

        private String text;

        public String getText()
                {
            return this.text;
            }


        public void setText(String text)
                {
            this.text = text;
            }


        public String getTextArea()
                {
            return this.textArea;
            }


        public void setTextArea(String textArea)
                {
            this.textArea = textArea;
            }


        public int getTextInt()
                {
            return this.textInt;
            }


        public void setTextInt(int textInt)
                {
            this.textInt = textInt;
            }


        public String getPassword()
                {
            return this.password;
            }


        public void setPassword(String password)
                {
            this.password = password;
            }

    }



    public static class FormTwo extends org.apache.beehive.netui.pageflow.FormData
    {
        private String[] manyCheckboxTwo;

        private String[] manyCheckbox;

        private boolean checkbox;

        public boolean getCheckbox()
                {
            return this.checkbox;
            }


        public void setCheckbox(boolean checkbox)
                {
            this.checkbox = checkbox;
            }


        public String[] getManyCheckbox()
                {
            return this.manyCheckbox;
            }


        public void setManyCheckbox(String[] manyCheckbox)
                {
            this.manyCheckbox = manyCheckbox;
            }


        public String[] getManyCheckboxTwo()
                {
            return this.manyCheckboxTwo;
            }


        public void setManyCheckboxTwo(String[] manyCheckboxTwo)
                {
            this.manyCheckboxTwo = manyCheckboxTwo;
            }

    }

    public static class ListBean extends org.apache.beehive.netui.pageflow.FormData
    {
        private String type;

        private String name;
        
        public ListBean() {
        }
        
        public ListBean(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName()
                {
            return this.name;
            }


        public void setName(String name)
                {
            this.name = name;
            }


        public String getType()
                {
            return this.type;
            }


        public void setType(String type)
                {
            this.type = type;
            }

    }
}
