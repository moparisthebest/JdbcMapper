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
package strutsMerge.test7;

import org.apache.struts.action.ActionForm;

import java.io.Serializable;

/***************************************************************************
 * Form bean
 **************************************************************************/
public class Form1 extends ActionForm
    {
    public static final String  DEFAULT_FORM_VALUE  = "Form1 default value";
    private String field1 = DEFAULT_FORM_VALUE;

    public Form1()
        {
        super();
        //System.out.println(">>> Form1.constructor - instance: ("
        //                   + this.toString() + ").");
        }
    public void setField1(String inField1)
        {
            //System.out.println(">>> Form1.setField1 - instance: (" + this.toString()
            //               + ")\n\told value: (" + this.field1
            //               + ")\n\tnew value: (" + inField1 + ").");
        this.field1 = inField1;
        }
    public String getField1()
        {
    //System.out.println(">>> Form1.getField1 - instance: (" + this.toString()
    //                       + ")\n\tfield1 value: (" + this.field1 + ").");
        return this.field1;
        }
    }
