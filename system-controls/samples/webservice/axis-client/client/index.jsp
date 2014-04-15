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
                 javax.servlet.jsp.JspWriter,
                 wsc.axis.sample.CalculatorServiceBean" %>

<% response.setHeader("Cache-Control", "no-cache"); %>

<h3>WSC Sample Axis Client</h3>
<p>The value of each expression on this page is calculated using a web service control
    communicating  with the Calculator web service.</p>

<%!
    public void runTest(JspWriter out, CalculatorServiceBean wsc) throws java.io.IOException {
        try {
            out.write("1 + 2 = " + wsc.add(1, 2) + "<BR>");
            out.write("2 + 3 = " + wsc.add(2, 3) + "<BR>");
            out.write("2 - 3 = " + wsc.subtract(2, 3) + "<BR>");
        }
        catch (Exception e) {
            out.write(e.getMessage());
        }
    }
%>

<%
    CalculatorServiceBean wsc = Controls.instantiate(CalculatorServiceBean.class, null,
                                                     ControlThreadContext.getContext(), "root");
    runTest(out, wsc);
%>
