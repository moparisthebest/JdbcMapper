/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */
package webappRoot;

import javax.servlet.ServletContext;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.PageFlowException;
import org.apache.beehive.netui.pageflow.NotLoggedInException;
import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.Forward;

import org.apache.beehive.samples.petstore.forms.ReturnToForm;
import org.apache.beehive.samples.petstore.forms.SearchForm;
import org.apache.beehive.samples.petstore.model.Account;
import org.apache.beehive.samples.petstore.model.Cart;
import org.apache.beehive.samples.petstore.model.Category;
import org.apache.beehive.samples.petstore.model.Product;
import org.apache.beehive.samples.petstore.controls.AccountControl;
import org.apache.beehive.samples.petstore.controls.CatalogControl;
import org.apache.beehive.samples.petstore.controls.exceptions.DataStoreException;
import org.apache.beehive.samples.petstore.controls.exceptions.NoSuchAccountException;
import org.apache.beehive.samples.petstore.controls.exceptions.InvalidIdentifierException;

@Jpf.Controller(
    catches={
        @Jpf.Catch(method="handlePageFlowException", type=PageFlowException.class),
        @Jpf.Catch(method="handleException", type=Exception.class),
        @Jpf.Catch(method="handleNotLoggedInException", type=NotLoggedInException.class),
        @Jpf.Catch(method="handleDataStoreException", type=DataStoreException.class)
    },
    simpleActions={
        @Jpf.SimpleAction(name="signoff", path="/auth/logout.do"),
        @Jpf.SimpleAction(name="globalShowHelp", path="/help.jsp"),
        @Jpf.SimpleAction(name="globalViewCart", path="/shop/viewCart.do"),
        @Jpf.SimpleAction(name="globalViewCategory", path="/shop/viewCategory.do"),
        @Jpf.SimpleAction(name="globalEditAccount", path="/account/edit/viewUpdateAccount.do"),
        @Jpf.SimpleAction(name="globalViewProductById", path="/shop/viewProduct.do"),
        @Jpf.SimpleAction(name="globalViewAddresses", path="/account/addresses/begin.do"),
        @Jpf.SimpleAction(name="globalViewCreateAccount", path="/account/create/Controller.jpf"),
        @Jpf.SimpleAction(name="globalViewOrders", path="/account/edit/listOrders.do"),
        @Jpf.SimpleAction(name="globalCheckOut", path="/checkout/createNewOrder.do")
    }
)
public class SharedFlow
    extends SharedFlowController {

    private static final String ATTR_CATEGORY_NAMES = SharedFlow.class + "CATEGORY_NAMES";

    @Control()
    private CatalogControl _catalogControl;

    @Control()
    private AccountControl _accountControl;

    private Account _account = null;
    private Cart _cart = null;
    private Product[] _myList;

    /**
     * Utility used to ensure that the user is logged into the site; an exception is
     * thrown when the the user is not logged in.
     */
    public void ensureLogin()
        throws NotLoggedInException {
        if (!isUserLoggedIn())
            throw new NotLoggedInException("User not logged in", this);
    }

    public void handleLogin(String username)
        throws NoSuchAccountException, InvalidIdentifierException {

        Account account = _accountControl.getAccount(username);

        // Throw an exception if the user is not found
        if(account == null)
			throw new NoSuchAccountException("Unknown user: " + username);
        
        // and set the account (this toggles the user's logged in status)
        setAccount(account);

        // set the user's favorites list if they are logged in
        if (account.isMyListOpt())
        {
            Product[] favProducts = _catalogControl.getProductListByCategory(account.getFavCategory());
            setMyList(favProducts);
        }
    }
    
    public void handleLogout() {
        setAccount(null);
        setCart(null);
    }

    public void handleCheckout() {
		_account.setStatus("OK");
        setCart(null);   
    }

    public void updateAccount(Account account)
        throws InvalidIdentifierException, NoSuchAccountException {

        assert account != null : "Received a null account!";
        // update the user's account info
        _account = account;

        // set the user's favorites once they're logged in
        if (account.isMyListOpt())
            setMyList(_catalogControl.getProductListByCategory(account.getFavCategory()));
    }
    
    public String[] getCategoryNames() {
        return lookupCategoryNames();
    }

    /**
     * Determine if the current user has logged in.
     */
    public boolean isUserLoggedIn() {
        return _account == null ? false : true;
    }

    public Product[] getMyList() {
        return _myList;
    }

    public Cart getCart() {
        if(_cart == null)
            _cart = new Cart();

        return _cart;
    }

    public Account getAccount() {
        return _account;
    }

    /**
     * This action is used to drop into the nested login controller
     * from a user click on a page
     */
    @Jpf.Action(
        forwards={@Jpf.Forward(name="auth", path="/auth/Controller.jpf")}
    )
    public Forward signon() {
        ReturnToForm initForm = new ReturnToForm(false);
        return new Forward("auth", initForm);
    }

   /**
     * This action is used to drop into the nested login controller
     * en-route to another action
     */
    @Jpf.Action(
        forwards={@Jpf.Forward(name="auth", path="/auth/Controller.jpf")}
    )
    public Forward actionSignon() {
        ReturnToForm initForm = new ReturnToForm(true);
        return new Forward("auth", initForm);
    }

    @Jpf.Action(
        forwards={@Jpf.Forward(name="search", path="/search/Controller.jpf")}
    )
    public Forward search(SearchForm form) {
        return new Forward("search");
    }

    @Jpf.Action(
        forwards={@Jpf.Forward(redirect=true, name="shop", path="/shop/Controller.jpf")}
    )
    public Forward globalShop() {
        return new Forward("shop");
    }

    /**
     * after login, depending on what the user was doing, we may want to return
     * to current page or previous action
     */
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="previousAction", navigateTo=Jpf.NavigateTo.previousAction),
            @Jpf.Forward(name="currentPage", navigateTo=Jpf.NavigateTo.currentPage)
        }
    )
    public Forward loginDone(ReturnToForm initForm) {
        if (initForm.getReturnToPreviousAction())
            return new Forward("previousAction");
        else return new Forward("currentPage");
    }

    @Jpf.ExceptionHandler(
        forwards={
            @Jpf.Forward(name="errorPage", path="/resources/beehive/version1/jsps/error.jsp")
        }
    )
    public Forward handleException(Exception ex, String actionName, String message, Object form) {
        System.err.print("[" + getRequest().getContextPath() + "] ");
        System.err.println("Unhandled exception caught in SharedFlow.jpfs:");
        ex.printStackTrace();
        return new Forward("errorPage");
    }

    @Jpf.ExceptionHandler(
        forwards={
            @Jpf.Forward(name="errorPage", path="/resources/beehive/version1/jsps/error.jsp")
        }
    )
    public Forward handleDataStoreException(DataStoreException ex, String actionName, String message, Object form) {
        System.err.print("[" + getRequest().getContextPath() + "] ");
        System.err.println("DataStore exception caught in SharedFlow.jpfs:");
        ex.printStackTrace();
        return new Forward("errorPage");
    }

	@Jpf.ExceptionHandler(
        forwards={@Jpf.Forward(name="auth", path="/auth/Controller.jpf")}
    )
    public Forward handleNotLoggedInException(NotLoggedInException ex, String actionName, String message, Object form) {
        ReturnToForm initForm = new ReturnToForm(true);
        return new Forward("auth", initForm);
    }
    
    @Jpf.ExceptionHandler()
    public Forward handlePageFlowException(PageFlowException ex, String message, String action, Object form)
        throws java.io.IOException {
        ex.sendError(getRequest(), getResponse());
        return null;
    }

    private void setAccount(Account account) {
        _account = account;
    }

    private void setCart(Cart cart) {
        _cart = cart;
    }

    private void setMyList(Product[] myList) {
        _myList = myList;
    }

    private String[] lookupCategoryNames() {
        ServletContext servletContext = getServletContext();
        Object obj = servletContext.getAttribute(ATTR_CATEGORY_NAMES);
        String[] categoryNames = null;
        if(obj == null) {
            assert _catalogControl != null : "Found a null CategoryControl";
            Category[] categories = _catalogControl.getCategoryList();
            categoryNames = new String[categories.length];
            for(int i = 0; i < categories.length; i++)
                categoryNames[i] = categories[i].getCatId();

            servletContext.setAttribute(ATTR_CATEGORY_NAMES, categoryNames);
        }
        else {
            assert obj instanceof String[] :
                "Found ServletContext \"" + ATTR_CATEGORY_NAMES + "\" that is not a String[]";
            categoryNames = (String[])obj;
        }

        assert categoryNames != null : "Found null category names";
        return categoryNames;
    }
}
