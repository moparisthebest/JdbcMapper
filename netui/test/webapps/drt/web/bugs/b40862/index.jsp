<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.beehive.netui.test.databinding.beans.b40682.Company" %>
<%@ page import="org.apache.beehive.netui.test.databinding.beans.b40682.Report" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>




<table cellpadding="0" width="100%" cellspacing="0">
    <tr>
            <td height="215" valign="top" class="Overline">
                  <table width="100%" border="0" cellpadding="2" cellspacing="0" class="portaltext">
                    <!--DWLayoutTable-->
                    <tr>
                      <td valign="top"> <p><strong>Reports &amp; Research</strong>
                          <span class="hightlite">(<em>Web</em> Notes Login Required)</span>
                          <br />

                            <netui-data:getData resultId="reports" value="${pageInput.reports}"></netui-data:getData>

                            <netui-data:repeater dataSource="pageInput.reports">
                                <netui-data:repeaterHeader></netui-data:repeaterHeader>
                                <netui-data:repeaterItem>
                                    <p>
                                        <netui:anchor href="${container.item.url}">
                                            <netui:span value="${container.item.text}"/>
                                        </netui:anchor>
                                    &nbsp;&nbsp;
                                        <netui:anchor action="editCompanies">
                                            edit
                                            <netui:parameter name="reportNo" value="${container.item.number}"/>
                                        </netui:anchor>
                                    <p>
                                    <!-- If there is a list of companies, render them here -->
                                    <netui-data:getData resultId="companies" value="${container.item.companies}"></netui-data:getData>
                                    <netui-data:getData resultId="url" value="${container.item.url}"></netui-data:getData>
                                    <netui-data:getData resultId="text" value="${container.item.text}"></netui-data:getData>

                                    <%
                                        Report[] reports = (Report[]) pageContext.getAttribute("reports");
                                        Company[] companies = (Company[]) pageContext.getAttribute("companies");
                                        String url = (String) pageContext.getAttribute("url");
                                        String text = (String) pageContext.getAttribute("text");

                                        if (companies != null)
                                        {
                                    %>
                                    <table border=1>
                                        <tr>
                                    <%
                                            for (int i=0; i < companies.length; i++ )
                                                if ( companies[i] != null && !companies[i].getSymbol().equals("") )
                                                {
                                                    if ( !companies[i].getStatus().equals("Down") )
                                                    {
                                    %>
                                        <td><font color="red"><%=companies[i].getSymbol()%></font></td>
                                    <%
                                                    }
                                                    else
                                                    {
                                    %>
                                        <td><font color="blue"><%=companies[i].getSymbol()%></font></td>
                                    <%
                                                    }
                                                }
                                    %>
                                        </tr>
                                    </table>
                                    <%
                                        }
                                    %>
                                </netui-data:repeaterItem>
                                <netui-data:repeaterFooter></netui-data:repeaterFooter>
                            </netui-data:repeater>

                        </td>
                    </tr>
                  </table>
                </td>
        </tr>

</table>
