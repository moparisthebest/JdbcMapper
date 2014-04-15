<netui-data:dataGrid dataSource="pageScope.stocks" name="portfolio" renderRowGroups="true">
    <netui-data:configurePager pageHref="${pageContext.request.contextPath}"/>
    <netui-data:header tagId="theadTagId">
        <netui-data:headerCell headerText="Symbol"/>
        <netui-data:headerCell headerText="Price"/>
        <netui-data:headerCell headerText="Web"/>
        <netui-data:headerCell headerText="Web Image (empty cells)"/>
        <netui-data:headerCell headerText="Web Image Anchor (empty cells)"/>
    </netui-data:header>
    <netui-data:rows>
        <netui-data:spanCell value="${container.item.symbol}"/>
        <netui-data:spanCell value="${container.item.price}"/>
        <netui-data:anchorCell value="${container.item.name}" href="${container.item.web}" tagId="symbolAnchor">
            <netui:parameter name="rowid" value="${container.index}"/>
            <netui:parameter name="symbol" value="${container.item.symbol}"/>
        </netui-data:anchorCell>
        <netui-data:imageCell src="${container.item.web}"/>
        <netui-data:imageAnchorCell src="${container.item.web}"/>
    </netui-data:rows>
</netui-data:dataGrid>
