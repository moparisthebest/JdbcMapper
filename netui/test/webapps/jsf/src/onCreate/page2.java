package onCreate;

import org.apache.beehive.netui.pageflow.FacesBackingBean;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

@Jpf.FacesBacking
public class page2 extends FacesBackingBean
{
    protected void onCreate()
    {
        String msg = (String) getPageInput("foo");
        FacesContext.getCurrentInstance().addMessage("foo", new FacesMessage(msg));
    }

    public String getFoo()
    {
        return "This is a property in the backing bean.";
    }
}
