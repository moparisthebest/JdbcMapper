package initMemberFields;

import org.apache.beehive.netui.pageflow.FacesBackingBean;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import javax.faces.component.UIOutput;


/**
 * This is the default Faces Backing file for a JSF Page.
 */
@Jpf.FacesBacking
public class page1 extends FacesBackingBean
{
    @Jpf.SharedFlowField(name="sf")
    InitMemberFieldsSharedFlow _sharedFlow;

    @Jpf.PageFlowField
    InitMemberFieldsController _pageFlow;

    private UIOutput _sharedFlowField = new UIOutput();
    private UIOutput _pageFlowField = new UIOutput();

    public UIOutput getSharedFlowField()
    {
        return _sharedFlowField;
    }

    public void setSharedFlowField( UIOutput sharedFlowField )
    {
        _sharedFlowField = sharedFlowField;
    }

    public UIOutput getPageFlowField()
    {
        return _pageFlowField;
    }

    public void setPageFlowField( UIOutput pageFlowField )
    {
        _pageFlowField = pageFlowField;
    }

    public String checkFields()
    {
        _sharedFlowField.setValue( _sharedFlow.getClass().getName() );
        _pageFlowField.setValue( _pageFlow.getClass().getName() );
        return null;
    }
}
