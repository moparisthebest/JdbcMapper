package restorePreviousBackingBean;

import org.apache.beehive.netui.pageflow.FacesBackingBean;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.FacesBacking
public class page1 extends FacesBackingBean
{
    private String _foo = "init val";

    public void setFoo( String foo )
    {
        _foo = foo;
    }

    public String getFoo()
    {
        return _foo;
    }
}
