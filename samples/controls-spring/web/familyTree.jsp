<%--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at
   
       http://www.apache.org/licenses/LICENSE-2.0
   
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
  
   $Header:$
--%>
<%@ page language="java" contentType="text/html;charset=UTF-8" session="true" 
         import="org.apache.beehive.controls.api.bean.Controls,
                 org.apache.beehive.controls.api.context.ControlThreadContext,
                 org.apache.beehive.samples.spring.control.PersonBean,
                 org.apache.beehive.samples.spring.control.AdultImpl" %>

<% response.setHeader("Cache-Control","no-cache"); %>

<%!
    /**
     * A simple util method that will take a PersonBean and generated formatted HTML output
     * describing the person and any nested children
     */
    public void showPerson(JspWriter out,  PersonBean person) throws java.io.IOException
    {
        out.println("Name: " + person.getFullName() + "<br>");
        out.println("Gender: " + person.getGender() + "<br>");
        out.println("Age: " + person.getAge() + "<br>");
        out.println("ID: "   + person.getControlID() + "<br>");
        String [] children = person.getChildren();
        if (children.length != 0)
        {
            out.println("Children: <br>");
            out.println("<ul>");
            for (int i = 0; i < children.length; i++)
            {
                PersonBean child = person.getChild(children[i]);
                out.println("<li>" + children[i] + "<br>");
                if (child != null)
                    showPerson(out, child);
            }
            out.println("</ul>");
        }
    }
%>

<%
    /**
     * Use the Controls.instantiate API to create the root of the tree.  This corresponds
     * the the "root" bean definition in application.xml.  The Spring BeanFactory APIs could
     * also be used to instantiate this control.
     */
    PersonBean root = Controls.instantiate(PersonBean.class, null,
                                           ControlThreadContext.getContext(), "root");
    showPerson(out, root);
%>
