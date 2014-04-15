<%@ page language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
   <head>
      <title>MiscJpf Test13</title>
   </head>
   <body>
      <h3 align="center">MiscJpf Test13 - submit.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <html:errors/>
      </center>

      <html:form action="/submit">
         <table align="center" width="50%" border="0" >
            <tr align="center">
               <td align="right">Last Name:</td>
               <td align="left"><html:text property="lastName" /></td>
            </tr>
            <tr align="center">
               <td align="right">Address:</td>
               <td align="left"><html:textarea property="address" /></td>
            </tr>
            <tr align="center">
               <td align="right">Sex:</td>
               <td align="left">
                  <html:radio property="sex" value="M" />Male
                  <html:radio property="sex" value="F" />Female
               </td>
            </tr>
            <tr align="center">
               <td align="right">Married:</td>
               <td align="left"><html:checkbox property="married" /></td>
            </tr>
            <tr align="center">
               <td align="right">Age:</td>
               <td align="left"><html:select property="age">
                                <html:option     value="a">0-19</html:option>
                                <html:option     value="b">20-49</html:option>
                                <html:option     value="c">50-</html:option>
                                </html:select>
               </td>
            </tr>
            <tr align="center">
               <td align="center" colspan="2"><br/><br/><html:submit/></td>
            </tr>
         </table>
      </html:form>

      <center>
         <logic:present name="lastName" scope="request" >
            Hello
            <logic:equal name="submitForm" property="age" value="a" >
               young
            </logic:equal>
            <logic:equal name="submitForm" property="age" value="c" >
               old
            </logic:equal>
            <bean:write name="lastName" scope="request"/>
         </logic:present>
      </center>
   </body>
</html>

