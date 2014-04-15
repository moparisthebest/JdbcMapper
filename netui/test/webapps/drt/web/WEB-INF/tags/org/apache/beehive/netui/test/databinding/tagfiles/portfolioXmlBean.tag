<%@ tag body-content="empty" %>
<%@ tag import="databinding.datagrid.controls.PortfolioControlBean"%>
<%@ variable name-given="stocks" variable-class="java.util.LinkedList" scope="AT_END" %>
<%--
    Create a portfolio XMLBean bindable with ${pageScope.stocks}
--%>
<jsp:useBean id="portfolioBean" class="databinding.datagrid.controls.PortfolioControlBean" scope="page"/>
<%
  // for back-compat with the old way of getting the Portfolio
  PortfolioControlBean pcb = (PortfolioControlBean)getJspContext().getAttribute("portfolioBean");
  getJspContext().setAttribute("stocks", pcb.getPortfolio().getStocks());
%>
