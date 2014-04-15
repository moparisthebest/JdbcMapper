import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.LinkedList;

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
    private static final String SQL_FILE = "sql/initDB.sql";	
	
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
           })
        }
    )
    protected Forward startDemo() throws SQLException {

        productNames = _simpleDbCtrl.getProductNames();
        Forward f = new Forward("success");
        f.addActionOutput("products", productNames);
        return f;        
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="success", path="details.jsp",
                         actionOutputs = {
                           @Jpf.ActionOutput(name = "details",
                                             type = controls.SimpleDBControl.Product.class,
                                             required = true)
            })
        }
    )
    protected Forward productDetails() throws SQLException {
        
        productDetails = _simpleDbCtrl.getProductDetails(getRequest().getParameter("key"));
        Forward f = new Forward("success");
        f.addActionOutput("details", productDetails);
        return f;        
    }

    /**
     * Method that is invoked when this controller instance is created.
     */
    protected void onCreate()
    {
    	 /*
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
        */
         
        System.out.println("jdbcControlSample: creating sample tables...");
        try {
            
            initDB();
      /*
        	_popCtrl.createProductsTable();
            _popCtrl.addProductRow("apple", "red apples", 200);
            _popCtrl.addProductRow("orange", "orange oranges", 400);
            _popCtrl.addProductRow("kiwi", "lots of kiwi", 800);
            _popCtrl.addProductRow("banana", "yellow", 123);
            _popCtrl.addProductRow("coconut", "coconutty", 21);
            _popCtrl.addProductRow("plum", "lots of plums", 999);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Jpf.Action
    public Forward initDB() 
      throws Exception {
        LinkedList<String> dbInit = getSqlStatements(SQL_FILE);

        for(String sql : dbInit) {
            try {
                _popCtrl.initDB(sql);
            } 
            catch(Exception e) {
                // Ignore exceptions on DROP statements since the tables may simply not exist
                if (!sql.startsWith("drop"))
                    throw e;
            }
        }
        getRequest().setAttribute("message", "DB Initialized.");

        return new Forward("index");
    }

private LinkedList<String> getSqlStatements(String sqlFile) 
    throws Exception {

    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sqlFile);
    BufferedReader fs = new BufferedReader(new InputStreamReader(is));
    LinkedList<String> statements = new LinkedList<String>();
    try {
        String line = null;
        while((line = fs.readLine()) != null) 
        {
            // Ignore comments and empty lines
            if (line.endsWith(";") && !(line.startsWith("--"))) 
                statements.add(line.replace(';', ' '));
        }
    } 
    catch(IOException io) {
        throw new IllegalStateException("Unable to read input stream: " + sqlFile);
    } 
    finally {
        try {
            if(fs != null) fs.close();
        } catch(Exception ignore) {}
    }

    for(String s : statements) {
        System.out.println(s + "\n");
    }

    return statements;
} 
    
    
    /**
     * Method that is invoked when this controller instance is destroyed.
     */
    protected void onDestroy(HttpSession session)
    {
    }
}
