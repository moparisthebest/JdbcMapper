package pageFlowCore.inheritLocalPaths;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="doInheritLocalPaths", path="derived/Controller.jpf"),
        @Jpf.SimpleAction(name="doNoInheritLocalPaths", path="derivedNoInheritLocalPaths/Controller.jpf"),
        @Jpf.SimpleAction(name="nestedDone", navigateTo=Jpf.NavigateTo.currentPage)
    }
)
public class Controller
        extends PageFlowController
{
}
