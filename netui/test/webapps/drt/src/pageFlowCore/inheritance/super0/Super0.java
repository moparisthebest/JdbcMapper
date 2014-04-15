package pageFlowCore.inheritance.super0;

import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.annotations.*;

import java.util.Arrays;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="simpleAction0", path="index.jsp")
    }
)
public class Super0 extends PageFlowController
{
    @Jpf.Action
    public Forward methodAction0()
    {
        return null;
    }

    public String[] getSortedActions()
    {
        String[] actions = getActions();
        Arrays.sort( actions );
        return actions;
    }
}
