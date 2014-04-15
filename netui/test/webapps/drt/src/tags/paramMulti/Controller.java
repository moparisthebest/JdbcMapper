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
package tags.paramMulti;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is the default controller for a blank web application.
 *
 * @jpf:controller
 */
@Jpf.Controller(
    )
public class Controller extends PageFlowController
{
    private String[] formFoo;
    private String[] stringValues = {"String 1", "String 2"};
    private ArrayList listValues;
    private String[] nullString = null;
    private ArrayList nullList = null;
    private String[] emptyString = new String[0];
    private ArrayList emptyList = new ArrayList();
    private String nullValue = null;
    private int[] intValues = {1,3,5,7};
    private String[] singleStringValue = {"Single String"};
    private ArrayList singleListValue;

    public String[] getFormFoo()
    {
        return formFoo;
    }

    public void setFormFoo(String[] formFoo)
    {
        this.formFoo = formFoo;
    }

    public String[] getStringValues()
    {
        return stringValues;
    }

    public void setStringValues(String[] stringValues)
    {
        this.stringValues = stringValues;
    }

    public ArrayList getListValues()
    {
        return listValues;
    }

    public void setListValues(ArrayList listValues)
    {
        this.listValues = listValues;
    }

    public String[] getNullString()
    {
        return nullString;
    }

    public void setNullString(String[] nullString)
    {
        this.nullString = nullString;
    }

    public ArrayList getNullList()
    {
        return nullList;
    }

    public void setNullList(ArrayList nullList)
    {
        this.nullList = nullList;
    }

    public String[] getEmptyString()
    {
        return emptyString;
    }

    public void setEmptyString(String[] emptyString)
    {
        this.emptyString = emptyString;
    }

    public ArrayList getEmptyList()
    {
        return emptyList;
    }

    public void setEmptyList(ArrayList emptyList)
    {
        this.emptyList = emptyList;
    }

    public String getNullValue()
    {
        return nullValue;
    }

    public void setNullValue(String nullValue)
    {
        this.nullValue = nullValue;
    }

    public int[] getIntValues()
    {
        return intValues;
    }

    public void setIntValues(int[] intValues)
    {
        this.intValues = intValues;
    }

    public String[] getSingleStringValue()
    {
        return singleStringValue;
    }

    public void setSingleStringValue(String[] singleStringValue)
    {
        this.singleStringValue = singleStringValue;
    }

    public ArrayList getSingleListValue()
    {
        return singleListValue;
    }

    public void setSingleListValue(ArrayList singleListValue)
    {
        this.singleListValue = singleListValue;
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
        listValues = new ArrayList();
        listValues.add("List 1");
        listValues.add("List 2");
        listValues.add("List 3");
        singleListValue = new ArrayList();
        singleListValue.add("Single List");
        return new Forward("index");
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
    protected Forward home()
    {
        return new Forward("index");
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
    protected Forward postback(NameBean form)
    {
        formFoo = getRequest().getParameterValues("foo");
        if (formFoo == null)
            formFoo = new String[0];
        return new Forward("success");
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
    protected Forward link()
    {
        formFoo = getRequest().getParameterValues("foo");
        if (formFoo == null) {
            formFoo = new String[1]; 
            formFoo[0] = "Foo was not found as a paramter";
        }
        return new Forward("success");
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class NameBean implements Serializable
    {
        private String name;


        public void setName(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }
    }
}
