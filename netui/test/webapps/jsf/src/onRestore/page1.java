package onRestore;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.FacesBackingBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Jpf.FacesBacking
public class page1
    extends FacesBackingBean
{
    protected void onCreate()
    {
        FacesMessage msg = new FacesMessage((String) getPageInput("message"));
        FacesContext.getCurrentInstance().addMessage("message", msg);
    }

    protected void onRestore()
    {
        FacesMessage msg = new FacesMessage((String) getPageInput("message"));
        FacesContext.getCurrentInstance().addMessage("message", msg);
    }
}
