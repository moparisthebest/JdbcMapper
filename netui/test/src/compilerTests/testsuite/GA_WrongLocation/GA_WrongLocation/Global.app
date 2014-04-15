package global;

import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 *******************************************************************************
 *
 * Test description
 * -----------------------------------------------------------------------------
 * This is a Global PageFlow file and resides in test directory GA_WrongLocation.
 * According to the spec. a global PageGroup (Global.app) can only reside in
 * the WEB-INF/src/global directory.
 *
 * Expected results
 * -----------------------------------------------------------------------------
 * The compiler should catch the fact that the file is in the wrong location and
 * issue an error.
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
    protected Forward page1()
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
