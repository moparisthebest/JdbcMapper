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
package tags.tagSizeBinding;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller(
    )
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/tags/tagSizeBinding/Controller.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='100' name='x'/>",
        "  <property value='120' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:goImageErrors.do'>",
        "  <property value='260' name='x'/>",
        "  <property value='320' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:goForm.do'>",
        "  <property value='340' name='x'/>",
        "  <property value='220' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:submitForm.do#tags.tagSizeBinding.Controller.Form'>",
        "  <property value='560' name='x'/>",
        "  <property value='220' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:submitForm2.do#tags.tagSizeBinding.Controller.Form'>",
        "  <property value='640' name='x'/>",
        "  <property value='120' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:form.jsp@#@action:submitForm.do#tags.tagSizeBinding.Controller.Form@'>",
        "  <property value='476,500,500,524' name='elbowsX'/>",
        "  <property value='212,212,212,212' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:form.jsp'>",
        "  <property value='440' name='x'/>",
        "  <property value='220' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:imageErrors.jsp@#@action:begin.do@'>",
        "  <property value='164,150,150,136' name='elbowsX'/>",
        "  <property value='212,212,123,123' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_2' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:imageErrors.jsp@#@action:goForm.do@'>",
        "  <property value='236,270,270,304' name='elbowsX'/>",
        "  <property value='212,212,212,212' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:imageErrors.jsp'>",
        "  <property value='200' name='x'/>",
        "  <property value='220' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:goImageErrors.do@'>",
        "  <property value='260,260,260,260' name='elbowsX'/>",
        "  <property value='204,240,240,276' name='elbowsY'/>",
        "  <property value='South_1' name='fromPort'/>",
        "  <property value='North_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:begin.do@'>",
        "  <property value='224,180,180,136' name='elbowsX'/>",
        "  <property value='152,152,112,112' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:index.jsp'>",
        "  <property value='260' name='x'/>",
        "  <property value='160' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:begin.do@'>",
        "  <property value='136,180,180,224' name='elbowsX'/>",
        "  <property value='112,112,152,152' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#imageErrors.jsp#@action:goImageErrors.do@'>",
        "  <property value='224,200,200,200' name='elbowsX'/>",
        "  <property value='312,312,288,264' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='South_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#form.jsp#@action:goForm.do@'>",
        "  <property value='376,390,390,404' name='elbowsX'/>",
        "  <property value='212,212,212,212' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:formErrors.jsp'>",
        "  <property value='660' name='x'/>",
        "  <property value='220' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#formErrors.jsp#@action:submitForm.do#tags.tagSizeBinding.Controller.Form@'>",
        "  <property value='596,610,610,624' name='elbowsX'/>",
        "  <property value='212,212,212,212' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#begin.do#@action:submitForm2.do#tags.tagSizeBinding.Controller.Form@'>",
        "  <property value='604,370,370,136' name='elbowsX'/>",
        "  <property value='112,112,112,112' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='formbean:tags.tagSizeBinding.Controller.Form'/>",
        "<pageflow-object id='action-call:@page:formErrors.jsp@#@action:submitForm2.do#tags.tagSizeBinding.Controller.Form@'>",
        "  <property value='660,660,640,640' name='elbowsX'/>",
        "  <property value='176,170,170,164' name='elbowsY'/>",
        "  <property value='North_1' name='fromPort'/>",
        "  <property value='South_1' name='toPort'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class Controller extends PageFlowController
{
    private int imageHeight = 80;
    private int imageWidth = 600;
    private int imageVSpace = 10;
    private int imageHSpace = 5;
    private int imageBorder = 5;
    private String imageAlt = "Alt Text From the Page Flow";
    private int tbMaxLength = 30;
    private int tbSize = 30;
    private int taCols = 30;
    private int taRows = 5;
    private int selectSize = 3;

    public Object getNullBinding() {
        return null;
    }

    public int getImageHeight()
    {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight)
    {
        this.imageHeight = imageHeight;
    }

    public int getImageWidth()
    {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth)
    {
        this.imageWidth = imageWidth;
    }

    public int getImageVSpace()
    {
        return imageVSpace;
    }

    public void setImageVSpace(int imageVSpace)
    {
        this.imageVSpace = imageVSpace;
    }

    public int getImageHSpace()
    {
        return imageHSpace;
    }

    public void setImageHSpace(int imageHSpace)
    {
        this.imageHSpace = imageHSpace;
    }

    public int getImageBorder()
    {
        return imageBorder;
    }

    public void setImageBorder(int imageBorder)
    {
        this.imageBorder = imageBorder;
    }

    public String getImageAlt()
    {
        return imageAlt;
    }

    public void setImageAlt(String imageAlt)
    {
        this.imageAlt = imageAlt;
    }

    public int getTbMaxLength()
    {
        return tbMaxLength;
    }

    public void setTbMaxLength(int tbMaxLength)
    {
        this.tbMaxLength = tbMaxLength;
    }

    public int getTbSize()
    {
        return tbSize;
    }

    public void setTbSize(int tbSize)
    {
        this.tbSize = tbSize;
    }

    public int getTaCols()
    {
        return taCols;
    }

    public void setTaCols(int taCols)
    {
        this.taCols = taCols;
    }

    public int getTaRows()
    {
        return taRows;
    }

    public void setTaRows(int taRows)
    {
        this.taRows = taRows;
    }

    public int getSelectSize()
    {
        return selectSize;
    }

    public void setSelectSize(int selectSize)
    {
        this.selectSize = selectSize;
    }

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

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "imageErrors.jsp") 
        })
    protected Forward goImageErrors()
    {
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "form.jsp") 
        })
    protected Forward goForm()
    {
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "formErrors.jsp") 
        })
    protected Forward submitForm(Form form)
    {
        return new Forward("success");
    }

   @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "begin.do") 
        })
    protected Forward submitForm2(Form form)
    {
        return new Forward("success");
    }

    public static class Form implements Serializable
    {
        private String textBox;
        private String textArea;
        private String select = "Option Two";

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

        public String getSelect()
        {
            return select;
        }

        public void setSelect(String select)
        {
            this.select = select;
        }
     }
}
