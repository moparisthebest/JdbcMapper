package global; 

import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.io.Serializable;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import shared.FormA;
import shared.QaTrace;
import pageFlowCore.returnToTest.InputForm;

@Jpf.Controller(
    strutsMerge="merge-jpf-struts-config.xml"
)
public class Global extends GlobalApp
{
    private String _globalAppInfo = "";
    private String _results;

    public String getGlobalAppInfo() {
        return _globalAppInfo;
    }

    public String getResults() {
        return _results;
    }
    public void setResults(String results) {
        _results = results;
    }
    
    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="success",
                navigateTo=Jpf.NavigateTo.currentPage
            )
        }
    )
    public Forward pageFlowCore_sharedFlow_globalAction()
    {
        return new Forward( "success", "message", "hit Global.app" );
    }

    @Jpf.Action(forwards = { @Jpf.Forward(name = "nesting", path = "/globalJpfs/nestReturn/Controller.jpf") })
    public Forward globalAction_action()
    {
        return new Forward("nesting");
    }

    @Jpf.Action(forwards = { @Jpf.Forward(name = "nesting", path = "/globalJpfs/nestReturn/Controller.jpf") })
    public Forward globalAction_form(GlobalForm form)
    {
        return new Forward("nesting");
    }

    @Jpf.Action(forwards = { @Jpf.Forward(name = "return", navigateTo = Jpf.NavigateTo.currentPage) })
    public Forward globalAction_getInfo(GlobalForm form)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<table border='1' cellspacing='0'cellpaddinig='2'>");

        sb = new StringBuffer();
        sb.append("<table border='1' cellspacing='0'cellpaddinig='2'>");

        HttpServletRequest req = getRequest();
        sb.append("<tr><td>Request</td><td>");
        sb.append(req.getRequestURI());
        sb.append("</td></tr>");


        HttpServletResponse resp = getResponse();
        sb.append("<tr><td>Response</td><td>Committed:");
        sb.append("" + resp.isCommitted());
        sb.append("</td></tr>");


        HttpSession session = getSession();
        session.setMaxInactiveInterval( 3600 );
        sb.append("<tr><td>Session</td><td>In active interval:");
        sb.append("" + session.getMaxInactiveInterval());
        sb.append("</td></tr>");

        // nestable
        sb.append("<tr><td>Nestable</td><td>");
        sb.append("" + isNestable());
        sb.append("</td></tr>");

        // nestable
        sb.append("<tr><td>PageFlow</td><td>");
        sb.append("" + isPageFlow());
        sb.append("</td></tr>");

        // get action
        sb.append("<tr><td>Actions</td><td>");
        String[] actions = getActions();
        Arrays.sort(actions);
        for (int i=0;i<actions.length;i++) {
            sb.append(actions[i]);
            sb.append("<br />");
        }
        sb.append("</td></tr>");


        // get mapping
        ActionMapping map = getMapping();
        sb.append("<tr><td>ActionMapping</td><td>");
        sb.append("" + map);
        sb.append("</td></tr>");

        // URI
        sb.append("<tr><td>URI</td><td>");
        sb.append("" + getURI());
        sb.append("</td></tr>");

        // parent dir
        sb.append("<tr><td>parentDir</td><td>");
        sb.append("" + getModulePath());
        sb.append("</td></tr>");


        sb.append("</table>");
        _globalAppInfo = sb.toString();

        return new Forward("return");
    }


    @Jpf.Action(forwards = { @Jpf.Forward(name = "results", path = "/results/Controller.jpf") })
    public Forward globalAction_results()
    {
        return new Forward("results");
    }

    public static class GlobalForm implements Serializable
    {
        String _search;
        public void setSearch(String search) {
            _search = search;
        }
        public String getSearch() {
            return _search;
        }

	}

    public String getAppState() {
	return "running";
    }

    public String getAppInfo() {
	return "App DRT";
    }

    /** 
     * This property is referenced by the databinding/globalApp/nojpf/ BVT.
     */
    public String getSayHello()
    {
        return "Hello World! -- the Global.app";
    }

    public String[] getStrings()
    {
        return new String[] {"One", "Fish", "Two", "Fish"}; 
    }

    //
    // The following actions are for /pageFlowCore/returnToTest/returnToTestController.jpf.
    //
    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.currentPage) })
    protected Forward globalReturnToCurrentPageOverrideInputs()
    {
        Forward fwd = new Forward( "success", "input1", "overridden #1" );
        fwd.addPageInput( "input2", "overridden #2" );
        return fwd;
    }

    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.currentPage) })
    protected Forward globalReturnToCurrentPageOverrideForm()
    {
        InputForm overrideForm = new InputForm();
        overrideForm.setFoo( "override foo" );
        overrideForm.setBar( "override bar" );
        return new Forward("success", overrideForm);
    }

    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.currentPage) })
    protected Forward globalReturnToCurrentPage()
    {
        return new Forward("success");
    }

    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.currentPage) })
    protected Forward globalReturnToCurrentPageWithForm( InputForm form )
    {
        return new Forward("success");
    }



    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.currentPage) })
    protected Forward globalReturnToPageOverrideInputs()
    {
        Forward fwd = new Forward( "success", "input1", "overridden #1" );
        fwd.addPageInput( "input2", "overridden #2" );
        return fwd;
    }

    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.currentPage) })
    protected Forward globalReturnToPageOverrideForm()
    {
        InputForm overrideForm = new InputForm();
        overrideForm.setFoo( "override foo" );
        overrideForm.setBar( "override bar" );
        return new Forward("success", overrideForm);
    }

    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.currentPage) })
    protected Forward globalReturnToPage()
    {
        return new Forward("success");
    }

    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.currentPage) })
    protected Forward globalReturnToPageWithForm( InputForm form )
    {
        return new Forward("success");
    }

    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.previousPage) })
    protected Forward globalReturnToPreviousPage()
    {
        return new Forward("success");
    }

    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.previousPage) })
    protected Forward globalReturnToPreviousPageOverrideInputs()
    {
        Forward fwd = new Forward( "success", "input1", "overridden #1" );
        fwd.addPageInput( "input2", "overridden #2" );
        return fwd;
    }

    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.previousPage) })
    protected Forward globalReturnToPreviousPageOverrideForm()
    {
        InputForm overrideForm = new InputForm();
        overrideForm.setFoo( "override foo" );
        overrideForm.setBar( "override bar" );
        return new Forward("success", overrideForm);
    }
   
    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.previousAction) })
    protected Forward globalReturnToPreviousAction()
    {
        return new Forward("success");
    }

    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.previousAction) })
    protected Forward globalReturnToAction()
    {
        return new Forward("success");
    }

        @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "goBack",
                navigateTo = Jpf.NavigateTo.previousPage) 
        })
   public Forward test35GlobalAction()
      {
          //_log.tracePoint("Global.test35GlobalAction():" + _cnt + " - Test35");
      return new Forward("goBack");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2",
                path = "/returnTo/test36/Jsp2.jsp") 
        })
   public Forward test36GlobalAction()
      {
          //_log.tracePoint("Global.test36GlobalAction():" + _cnt + " - Test36");
      return new Forward("gotoPg2");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "goBack",
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
   public Forward test39GlobalAction()
      {
          //_log.tracePoint("Global.test39GlobalAction():" + _cnt + " - Test39");
      return new Forward("goBack");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2",
                path = "/returnTo/test40/Jsp2.jsp") 
        })
   public Forward test40GlobalAction()
      {
      //_log.tracePoint("Global.test40GlobalAction():" + _cnt + " - Test40");
      return new Forward("gotoPg2");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "goBack",
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
   public Forward test40GlobalAction_B()
      {
      //_log.tracePoint("Global.test40GlobalAction_B():" + _cnt + " - Test40");
      return new Forward("goBack");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "goBack",
                navigateTo = Jpf.NavigateTo.previousAction) 
        })
   public Forward test45GlobalAction()
      {
      //_log.tracePoint("Global.test45GlobalAction():" + _cnt + " - Test45");
      return new Forward("goBack");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoAct2",
                path = "/returnTo/test46/action2.do") 
        })
   public Forward test46GlobalAction()
      {
      //_log.tracePoint("Global.test46GlobalAction():" + _cnt + " - Test46");
      if (_toggle == true)
         {
         _toggle = false;
         return new Forward("gotoAct2");
         }
      _toggle = true;
      return new Forward("gotoError");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "goBack",
                navigateTo = Jpf.NavigateTo.previousAction) 
        })
   public Forward test48GlobalAction(FormA inForm)
//   public Forward test48GlobalAction()
      {
      //_log.tracePoint("Global.test48GlobalAction(FormA):"
      //                  + _cnt
      //                  + " - Test48 - FormA instance:"
      //                   + _log.getClassCnter(inForm));
      return new Forward("goBack");
      }

          private String _pageGroupState = "";

    // from QA Global.app
    private  boolean  _toggle  = true;
    private QaTrace _log = null;
    private int     _cnt = 0;

    public String getPageFlowState() {
	return _pageGroupState;
    }
    public void setPageFlowState(String pgs) {
	_pageGroupState = pgs;
    }

    // from QA Web
    /**
     * onCreate
     */
    public void onCreate() throws Exception
        {
        HttpSession session = getSession();
        _log = QaTrace.getTrace(getSession(), true);
        //_cnt = _log.newClass(this);
        //_log.tracePoint("Global.onCreate():" + session.hashCode());
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "struts1Do",
                path = "/interOp/tests/test2/gotoStrutsJSP.do") 
        })
   public Forward toLegacy()
      {
          // _log.tracePoint("Global.toLegacy():" + _cnt + " - Test2");
      return new Forward("struts1Do");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "struts2Do",
                path = "/interOp/tests/test5/gotoStruts2JSP.do") 
        })
   public Forward globalAction()
      {
          //_log.tracePoint("Global.globalAction():" + _cnt + " - Test5");
      return new Forward("struts2Do");
      }

    @Jpf.Action(
        )
   public Forward unKnown()
      {
      ActionMapping mapping = getMapping();
      //_log.tracePoint("Global.unKnown():"
      //                  + _cnt
      //                  + " - Parameter:"
      //                  + mapping.getParameter());
      return new Forward("gotoError");
      }

   /**
    * StrutsMergeTest2 will raise this "unHandledAction" action.  The forward
    * "jpf:gotoAction1" below will be overridden by the "struts-merge"
    * annotation above and send control back to a valid action in the
    * StrutsMergeTest2 pageflow.
    */
    @Jpf.Action(
        )
   public Forward unHandledAction()
      {
      ActionMapping mapping = getMapping();
      //_log.tracePoint("Global.unHandledAction():"
      //                  + _cnt
      //                  + " - Parameter:"
      //                  + mapping.getParameter());
      return new Forward("gotoAction1");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoResults",
                path = "/miscJpf/bug30448/jpfReturn1.do") 
        })
   public Forward jpfReturn1(FormA inForm)
      {
      //_log.tracePoint("Global.jpfReturn1(FormA):" + _cnt + " - bug30448");
      return new Forward("gotoResults");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "/scopedJpf/jpfTest5/jpf1/Jpf1.jpf") 
        })
   public Forward jpf1Begin()
      {
      //_log.tracePoint("Global.jpf1Begin():" + _cnt + " - JpfTest5");
      return new Forward("success");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "/scopedJpf/jpfTest5/jpf2/Jpf2.jpf") 
        })
   public Forward jpf2Begin()
      {
      //_log.tracePoint("Global.jpf2Begin():" + _cnt + " - JpfTest5");
      return new Forward("success");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "/scopedJpf/jpfTest5/jpf1/goNested.do") 
        })
   public Forward jpf1GoNested(FormA form)
      {
      //_log.tracePoint("Global.jpf1GoNested(FormA):" + _cnt + " - JpfTest5");
      return new Forward("success");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "/scopedJpf/jpfTest5/jpf2/goNested.do") 
        })
   public Forward jpf2GoNested(FormA form)
      {
      //_log.tracePoint("Global.jpf2GoNested(FormA):" + _cnt + " - JpfTest5");
      return new Forward("success");
      }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "/singletonJpf/jpfTest10/jpf1/Jpf1.jpf") 
        })
   public Forward gblJpfTest10()
      {
      //_log.tracePoint("Global.jpfTest10():" + _cnt + " - JpfTest10");
      return new Forward("success");
      }

} 
