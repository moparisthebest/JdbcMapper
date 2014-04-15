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
package miscJpf.test13;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class SubmitForm extends ActionForm
   {
   // Last Name
   //---------------------------------------------------------------------------
   private String lastName = "John Doe";
   public String getLastName()
      {
      return(this.lastName);
      }
   public void setLastName(String lastName)
      {
      this.lastName = lastName;
      }

   // Address
   //---------------------------------------------------------------------------
   private String address = null;
   public String getAddress()
      {
      return(this.address);
      }
   public void setAddress(String address)
      {
      this.address = address;
      }

   // Sex
   //---------------------------------------------------------------------------
   private String sex = null;
   public String getSex()
      {
      return(this.sex);
      }
   public void setSex(String sex)
      {
      this.sex = sex;
      }

   // Married status
   //---------------------------------------------------------------------------
   private String married = null;
   public String getMarried()
      {
      return(this.married);
      }
   public void setMarried(String married)
      {
      this.married = married;
      }

   // Age
   //---------------------------------------------------------------------------
   private String age = null;
   public String getAge()
      {
      return(this.age);
      }
   public void setAge(String age)
      {
      this.age = age;
      }

   // Form validation
   //---------------------------------------------------------------------------
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
      {
          //System.out.println(">>> SubmitForm.validate");

      // Check for all mandatory data
      //-----------------------------------------------------------------------
      ActionErrors errors = new ActionErrors();
      if (lastName == null || lastName.equals(""))
         {
             //System.out.println(">>> Last Name error");
         errors.add("Last Name", new ActionError("error.lastName"));
         }
      if (address == null || address.equals(""))
         {
             //System.out.println(">>> Address error");
         errors.add("Address", new ActionError("error.address"));
         }
      if (sex == null || sex.equals(""))
         {
             //System.out.println(">>> Sex error");
         errors.add("Sex", new ActionError("error.sex"));
         }
      if (age == null || age.equals(""))
         {
             //System.out.println(">>> Age error");
         errors.add("Age", new ActionError("error.age"));
         }
      return errors;
      }
   }
