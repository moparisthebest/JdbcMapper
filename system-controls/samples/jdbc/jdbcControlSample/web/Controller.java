import javax.servlet.http.HttpSession;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.controls.api.bean.Control;
import controls.SimpleDBControl;
import controls.PopulateDBCtrl;
import shared.SharedFlow;

@Jpf.Controller(
    simpleActions={},
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="shared", type=shared.SharedFlow.class)
    }
)
public class Controller 
    extends PageFlowController
{
    @Jpf.SharedFlowField(name="shared")
    private shared.SharedFlow sharedFlow;

    // initialize the SimpleDBCtrl
    @Control()
    private SimpleDBControl _simpleDbCtrl;

    // initialize the PopulateDBCtrl
    @Control()
    private PopulateDBCtrl _popCtrl;

    public transient String[] productNames;
    public transient SimpleDBControl.Product productDetails;

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="success", path="index.jsp")
        }
    )
    protected Forward begin() {
        return new Forward("success");
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="success", path="demo.jsp",
        	            actionOutputs = {
        		          @Jpf.ActionOutput(name = "products",
        		        		            type = String[].class,
        		        		            required = true)
           }),
           @Jpf.Forward(name="error", path="error.jsp")
        }
    )
    protected Forward startDemo() {

        try {
            productNames = _simpleDbCtrl.getProductNames();
        } catch (SQLException sqe) {
            System.out.println("JdbcSampleApp: Error Calling Simpledb: " + sqe);
            return new Forward("error");
        }
        return new Forward("success", "products", productNames);
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="details", path="details.jsp"),
           @Jpf.Forward(name="error", path="error.jsp")
        }
    )
    protected Forward productDetails() {
        
        try {
            productDetails = _simpleDbCtrl.getProductDetails(getRequest().getParameter("key"));
        } catch (SQLException sqe) {
            System.out.println("JdbcSampleApp: Error Calling Simpledb: " + sqe);
            return new Forward("error");
        }
        return new Forward("details");
    }

    /**
     * Callback that is invoked when this controller instance is created.
     */
    protected void onCreate()
    {
        String check = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); 
        } catch (ClassNotFoundException e) {
            System.out.println("JdbcSampleApp: Class Not Found: " + e);
            return;
        }

        System.out.println("jdbcControlSample: checking to see if products table exists...");
        try {
            check = _popCtrl.isProductsTableEmpty();
        } catch (SQLException sqe) { 
        }

        // table has been loaded -- just return
        if (check != null) {
           System.out.println("jdbcControlSample: products table exists.");
           return;
        }

        System.out.println("jdbcControlSample: products table does not exist, creating...");
        try {
            _popCtrl.createProductsTable();
            _popCtrl.addProductRow("apple", "red apples", 200);
            _popCtrl.addProductRow("orange", "orange oranges", 400);
            _popCtrl.addProductRow("kiwi", "lots of kiwi", 800);
            _popCtrl.addProductRow("banana", "yellow", 123);
            _popCtrl.addProductRow("coconut", "coconutty", 21);
            _popCtrl.addProductRow("plum", "lots of plums", 999);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Callback that is invoked when this controller instance is destroyed.
     */
    protected void onDestroy(HttpSession session)
    {
    }
}
