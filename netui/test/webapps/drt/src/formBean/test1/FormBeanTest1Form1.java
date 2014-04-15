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
package formBean.test1;

import org.apache.struts.action.ActionForm;

import java.io.Serializable;

/***************************************************************************
 *
 **************************************************************************/
public class FormBeanTest1Form1 extends ActionForm
    {
    public final static String DEFAULT_FORM_VALUE   = "Default form value";
    private             String field1               = DEFAULT_FORM_VALUE;

    public FormBeanTest1Form1()
        {
        super();
        //System.out.println(">>> FormBeanTest1Form1.constructor - ("
        //                   + this.toString() + ").");
        }
    /***************************************************************************
     * Field1
     **************************************************************************/
    public void setField1(String inField1)
        {
            //System.out.println(">>> FormBeanTest1Form1.setField1 - ("
            //                + this.toString() + ") old value: ("
            //               + field1 + ") new value: ("
            //               + inField1 + ").");
        this.field1 = inField1;
        }
    public String getField1()
        {
            //System.out.println(">>> FormBeanTest1Form1.getField1 - ("
            //               + this.toString() + ") field1 value: ("
            //               + field1 + ").");
        return this.field1;
        }
    }
