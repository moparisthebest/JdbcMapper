package invalid.packagename;

import org.apache.beehive.netui.pageflow.GlobalApp;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 *******************************************************************************
 *
 * @desc: This global PageFlow file is in an invalid package.  The spec. says that the
 * package name should be in package "global".
 *
 * @result: The compiler should catch this error and issue an error message, as well
 * as issue an error for not being in WEB-INF/src.
 *
 ******************************************************************************/
@Jpf.Controller()
public class Global extends GlobalApp
    {
    /**
     * @jpf:action
     * @jpf:forward name="page1" path="Page1.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "page1",
                path = "Page1.jsp")
        })
    protected Forward begin()
        {
        return new Forward("page1");
        }

    /**
     * @jpf:action
     * @jpf:forward name="page2" path="Page2.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "page2",
                path = "Page2.jsp")
        })
    protected Forward page2()
        {
        return new Forward("page2");
        }
    }
