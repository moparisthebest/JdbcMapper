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
package shop;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import org.apache.beehive.samples.petstore.controls.CatalogControl;
import org.apache.beehive.samples.petstore.controls.exceptions.InvalidIdentifierException;
import org.apache.beehive.samples.petstore.model.Cart;
import org.apache.beehive.samples.petstore.model.Category;
import org.apache.beehive.samples.petstore.model.Item;
import org.apache.beehive.samples.petstore.model.Product;
import org.apache.beehive.samples.petstore.forms.CartForm;
import org.apache.beehive.controls.api.bean.Control;

@Jpf.Controller(
    forwards = { 
        @Jpf.Forward(name = "cart", path = "cart.jsp")
    },
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="rootSharedFlow", type=webappRoot.SharedFlow.class)
    },
    simpleActions={
        @Jpf.SimpleAction(name="checkout", path="/checkout/Controller.jpf")
    }
)
public class Controller
    extends PageFlowController {

    @Control()
    private CatalogControl _catalogControl;

    @Jpf.SharedFlowField(name="rootSharedFlow")
    private webappRoot.SharedFlow _sharedFlow = null;

    /* todo: this should be shared for all users */
    /* categories */
    private Category[] _categories;

    /* current category */
    private Category _currentCategory;

    /* current product */
    private Product _currentProduct;

    /* current item */
    private Item _currentItem;

    private CartForm _cartForm;

    @Jpf.Action(
        forwards = { 
            @Jpf.Forward(name = "success", path = "index.jsp",
                         actionOutputs = { 
                             @Jpf.ActionOutput(name = "categories",
                                               type = org.apache.beehive.samples.petstore.model.Category[].class,
                                               required = true)
                         })
        }
    )
    public Forward begin() {
        if(_categories == null)
            _categories = _catalogControl.getCategoryList();

        return new Forward("success", "categories", _categories);
    }

    @Jpf.Action(
        forwards = { 
            @Jpf.Forward(name = "category", path = "category.jsp",
                         actionOutputs = { 
                             @Jpf.ActionOutput(name = "category",
                                               type = org.apache.beehive.samples.petstore.model.Category.class,
                                               required = true),
                             @Jpf.ActionOutput(name = "products",
                                               type = org.apache.beehive.samples.petstore.model.Product[].class,
                                               required = false)
                         })
        }
    )
    public Forward viewCategory()
        throws InvalidIdentifierException {
        String categoryId = getRequest().getParameter("catId");

        if(categoryId == null || categoryId.equals(""))
            throw new IllegalStateException("Found a null catId executing action viewCategory");

        _currentCategory = _catalogControl.getCategory(categoryId);
        Product[] productArray = _catalogControl.getProductListByCategory(categoryId);

        Forward forward = new Forward("category");
        forward.addActionOutput("products", productArray);
        forward.addActionOutput("category", _currentCategory);
        return forward;
    }

    @Jpf.Action(
        forwards = { 
            @Jpf.Forward(name = "product", path = "product.jsp",
                         actionOutputs = { 
                             @Jpf.ActionOutput(name = "product",
                                               type = org.apache.beehive.samples.petstore.model.Product.class,
                                               required = true),
                             @Jpf.ActionOutput(name = "items",
                                               type = org.apache.beehive.samples.petstore.model.Item[].class,
                                               required = true)
                         })
        }
    )
    public Forward viewProduct()
        throws InvalidIdentifierException {
        String productId = getRequest().getParameter("productId");

        if(productId == null || productId.equals(""))
            throw new IllegalStateException("Found a null productId executing action viewProduct");

        _currentProduct = _catalogControl.getProduct(productId);
        Item[] items = _catalogControl.getItemListByProduct(productId);

        Forward f = new Forward("product");
        f.addActionOutput("product", _currentProduct);
        f.addActionOutput("items", items);
        return f;
    }

    @Jpf.Action(
        forwards = { 
            @Jpf.Forward(name = "item", path = "item.jsp",
                         actionOutputs = { 
                             @Jpf.ActionOutput(name = "product",
                                               type = org.apache.beehive.samples.petstore.model.Product.class,
                                               required = true),
                             @Jpf.ActionOutput(name = "item",
                                               type = org.apache.beehive.samples.petstore.model.Item.class,
                                               required = true)
                         })
        }
    )
    public Forward viewItem()
        throws InvalidIdentifierException {
        String itemId = getRequest().getParameter("itemId");

        if(itemId == null || itemId.equals(""))
            throw new IllegalStateException("Found a null itemId executing action viewProduct");

        _currentItem = _catalogControl.getItem(itemId);
        _currentProduct = _catalogControl.getProduct(_currentItem.getProductId());

        Forward f = new Forward("item");
        f.addActionOutput("item", _currentItem);
        f.addActionOutput("product", _currentProduct);
        return f;
    }

    @Jpf.Action()
    public Forward addItemToCart()
        throws InvalidIdentifierException {

        String workingItemId = lookupCurrentItemId();

        if(workingItemId == null)
            throw new InvalidIdentifierException("Could not find working item identifier");

        Cart cart = lookupCart();
        if (cart.containsItemId(workingItemId))
            cart.incrementQuantityByItemId(workingItemId);
        else {
            Item toAddItem = _catalogControl.getItem(workingItemId);
            if (toAddItem != null)
                cart.addItem(toAddItem);
        }

        _cartForm = new CartForm();
        _cartForm.setCart(lookupCart());
        Forward forward = setupCartForward();
        forward.addActionOutput("product", _currentProduct);
        return forward;
    }

    @Jpf.Action
    public Forward viewCart() {
        _cartForm = new CartForm();
        _cartForm.setCart(lookupCart());
        return setupCartForward();
    }

    /* todo: need to get line-by-line updates working again here */
    @Jpf.Action(useFormBean="_cartForm")
    public Forward updateCartQuantities(CartForm cartForm) {
        Forward forward = setupCartForward();
        forward.addActionOutput("product", _currentProduct);
        return forward;
    }

    @Jpf.Action
    public Forward removeItemFromCart() {
        String itemId = lookupCurrentItemId();
        
        Cart cart = lookupCart();
        if(cart.containsItemId(itemId))
            cart.removeItemById(itemId);

        Forward forward = setupCartForward();
        forward.addActionOutput("product", _currentProduct);
        return forward;
    }

    private Forward setupCartForward() {
        CartForm form = new CartForm();
        form.setCart(lookupCart());

        Forward forward = new Forward("cart");
        forward.addOutputForm(form);
        return forward;
    }

    private String lookupCurrentItemId() {
        String itemId = getRequest().getParameter("workingItemId");
        return itemId;
    }

    private Cart lookupCart() {
        return _sharedFlow.getCart();
    }
}