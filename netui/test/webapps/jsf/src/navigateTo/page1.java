package navigateTo;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.FacesBackingBean;


@Jpf.FacesBacking
public class page1
    extends FacesBackingBean
{
    private String _message = "";

    public void setMessage( String message )
    {
        _message = message;
    }

    public String getMessage()
    {
        return _message;
    }
}
