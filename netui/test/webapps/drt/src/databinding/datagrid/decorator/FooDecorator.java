package databinding.datagrid.decorator;

import javax.servlet.jsp.JspContext;

import org.apache.beehive.netui.databinding.datagrid.api.exceptions.CellDecoratorException;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellDecorator;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;

/**
 *
 */
public class FooDecorator
    extends CellDecorator {

    public void decorate(JspContext jspContext, AbstractRenderAppender appender, CellModel cellModel)
        throws CellDecoratorException {

        appender.append("&lt;foo&gt;");
        getNestedDecorator().decorate(jspContext, appender, cellModel);
        appender.append("&lt;/foo&gt;");
    }
}
