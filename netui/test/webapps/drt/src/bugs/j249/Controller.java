package bugs.j249;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.http.HttpSession;
import java.util.Calendar;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
public class Controller extends PageFlowController
{
    private Calendar _date;

    public void setDate(Calendar date) {
        _date = date;
    }
    public Calendar getDate() {
        return _date;
    }

    @Jpf.Action(
            forwards = {
            @Jpf.Forward(name = "index", path = "index.jsp")
            }
            )
    protected Forward begin()
    {
        return new Forward("index");
    }
    /**
     * Callback that is invoked when this controller instance is created.
     */
    protected void onCreate()
    {
        _date = Calendar.getInstance();
        _date.set(2002,0,17,13,30,8);
    }

    /**
     * Callback that is invoked when this controller instance is destroyed.
     */
    protected void onDestroy(HttpSession session)
    {
    }
}
