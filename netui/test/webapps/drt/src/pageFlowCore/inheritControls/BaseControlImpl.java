package pageFlowCore.inheritControls;

import org.apache.beehive.controls.api.bean.ControlImplementation;

@ControlImplementation(isTransient=true)
public class BaseControlImpl implements BaseControl
{ 
    public String hello()
    {
        return "hi there";
    }
}
