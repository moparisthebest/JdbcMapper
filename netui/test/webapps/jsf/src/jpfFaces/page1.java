package jpfFaces;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.FacesBackingBean;


@Jpf.FacesBacking
public class page1
    extends FacesBackingBean
{
    public JpfFacesController.BarForm bar = new JpfFacesController.BarForm();
    private String _foo;

    public void setFoo( String foo )
    {
        _foo = foo;
    }

    public String getFoo()
    {
        return _foo;
    }

    @Jpf.CommandHandler(
        raiseActions = {
            @Jpf.RaiseAction( action="go2", outputFormBean="bar" )
        }
    )
    public String commandHandler2()
    {
        if ( "stay".equals( _foo ) ) return null;
        bar.setBar( _foo );
        return "go2";
    }

    @Jpf.CommandHandler(
        raiseActions = {
            @Jpf.RaiseAction( action="go3" )
        }
    )
    public String commandHandler3()
    {
        return "go3";
    }
}
