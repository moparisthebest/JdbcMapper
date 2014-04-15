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
package tags.formatSelect;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:forward name="begin" path="Begin.jsp"
 */
@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "begin",
            path = "Begin.jsp") 
    })
public class Controller extends PageFlowController
{
    private String select; 

    public String getSelect() {
        return(select);
    } 
    public void setSelect(String select) {
        this.select= select;
    } 

    private Integer textBox = new Integer(123456); 
    
    public Integer getTextBox() {
        return(textBox);
    } 
    public void setTextBox(Integer textBox) {
        this.textBox = textBox;
    } 
    
    private String textArea; 

    public String getTextArea() {
        return(textArea);
    } 
    public void setTextArea(String textArea) {
        this.textArea = textArea;
    } 

    private String textDefault = "12345"; 

    public String getTextDefault() {
        return(textDefault);
    } 
    public void setTextDefault(String textDefault)  {
        this.textDefault = textDefault;
    } 


    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward begin()
    {
        return new Forward("begin");
    }
}
