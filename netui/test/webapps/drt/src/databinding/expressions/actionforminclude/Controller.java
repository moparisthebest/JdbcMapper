/**
 * Created by IntelliJ IDEA.
 * User: ekoneil
 * Date: May 4, 2006
 * Time: 11:17:41 AM
 * To change this template use File | Settings | File Templates.
 */
package databinding.expressions.actionforminclude;

import javax.servlet.http.HttpSession;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Jpf.Controller(
    forwards = {
    @Jpf.Forward(name = "index", path = "index.jsp"),
    @Jpf.Forward(name = "fileinclude", path = "formwithfileinclude.jsp"),
    @Jpf.Forward(name = "jspinclude", path = "formwithjspinclude.jsp"),
    @Jpf.Forward(name = "tagfile", path = "formwithtagfile.jsp")
        }
)
public class Controller
    extends PageFlowController {

    private static final Log LOG = LogFactory.getLog("webapp");

    @Jpf.Action()
    public Forward begin() {
        return new Forward("index");
    }

    @Jpf.Action
    public Forward showFileInclude() {
        return new Forward("fileinclude");
    }

    @Jpf.Action
    public Forward submitFileInclude(MessageForm form) {
        return new Forward("fileinclude", form);
    }

    @Jpf.Action
    public Forward showJspInclude() {
        return new Forward("jspinclude");
    }

    @Jpf.Action
    public Forward submitJspInclude(MessageForm form) {
        return new Forward("jspinclude", form);
    }

    @Jpf.Action
    public Forward showTagFile() {
        return new Forward("jspinclude");
    }

    @Jpf.Action
    public Forward submitTagFile(MessageForm form) {
        return new Forward("tagfile", form);
    }

    public static class MessageForm {
        private String _message;

        public String getMessage() {
            return _message;
        }

        public void setMessage(String message) {
            _message = message;
        }
    }
}
