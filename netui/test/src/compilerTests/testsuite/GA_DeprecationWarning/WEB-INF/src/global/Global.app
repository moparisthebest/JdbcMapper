package global;

import org.apache.beehive.netui.pageflow.GlobalApp;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/*******************************************************************************
 * ******************************************************************************
 * 
 * @desc: This Global.app file is designed to compile without errors.
 * 
 * @result: This test must compile without errors.
 * 
 ******************************************************************************/
@Jpf.Controller() public class Global extends GlobalApp {
    /**
     * @jpf:action
     * @jpf:forward name="page1" path="/GA_DeprecationWarning/Page1.jsp"
     */
    @Jpf.Action(forwards = 
       { @Jpf.Forward(name = "page1", 
                      path = "/GA_DeprecationWarning/Page1.jsp") }) 
        protected Forward begin() {
        return new Forward("page1");
    }

    /**
     * @jpf:action
     * @jpf:forward name="page2" path="/GA_DeprecationWarning/Page2.jsp"
     */
    @Jpf.Action(forwards = 
        { @Jpf.Forward(name = "page2", 
                       path = "/GA_DeprecationWarning/Page2.jsp") }) 
        protected Forward page2() {
        return new Forward("page2");
    }
}
