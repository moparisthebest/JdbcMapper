package useFormBean;

import javax.faces.component.UIOutput;
import org.apache.beehive.netui.pageflow.FacesBackingBean;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.FacesBacking
public class input extends FacesBackingBean
{
    private Controller.SomeFormBean _theForm = new Controller.SomeFormBean();
    public void setTheForm( Controller.SomeFormBean bean ) { _theForm = bean; }
    public Controller.SomeFormBean getTheForm() { return _theForm; }

    @Jpf.CommandHandler(
        raiseActions={
            @Jpf.RaiseAction(action="submit", outputFormBean="_theForm")
        }
    )
    public String raisePageFlowAction()
    {
        return "submit";
    }
}
