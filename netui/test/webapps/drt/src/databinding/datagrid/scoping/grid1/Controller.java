/**
 * Created by IntelliJ IDEA.
 * User: ekoneil
 * Date: Apr 25, 2006
 * Time: 3:31:14 PM
 * To change this template use File | Settings | File Templates.
 */
package databinding.datagrid.scoping.grid1;

import javax.servlet.http.HttpSession;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Jpf.Controller()
public class Controller
    extends PageFlowController {

    private static final Log LOG = LogFactory.getLog("webapp");

    @Jpf.Action(forwards={@Jpf.Forward(name="index", path="grid.jsp")})
    public Forward begin() {
        return new Forward("index");
    }

    public void beforeAction() {
    }

    public void afterAction() {
    }

    public void onCreate() {
    }

    public void onDestroy(HttpSession httpSession) {
    }
}
