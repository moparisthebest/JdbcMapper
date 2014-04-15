package bindingInFaces;

import javax.faces.component.UIOutput;
import org.apache.beehive.netui.pageflow.FacesBackingBean;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * This is the default Faces Backing file for a JSF Page.
 */
@Jpf.FacesBacking
public class page1 extends FacesBackingBean
{
    private String _someProperty = "backing file property";
    private UIOutput _outputField = new UIOutput();

    public UIOutput getOutputField()
    {
        return _outputField;
    }

    public void setOutputField( UIOutput outputField )
    {
        _outputField = outputField;
    }
    
    public String getSomeProperty()
    {
        return _someProperty;
    }

    public void setSomeProperty( String someProperty )
    {
        _someProperty = someProperty;
    }

    public String getPageInput()
    {
        _outputField.setValue( getPageInput( "somePageInput" ) );
        return null;
    }
}
