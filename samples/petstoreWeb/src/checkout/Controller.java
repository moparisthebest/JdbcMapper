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
package checkout;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;

import org.apache.beehive.samples.petstore.controls.AddressControl;
import org.apache.beehive.samples.petstore.controls.OrderControl;
import org.apache.beehive.samples.petstore.controls.exceptions.NoSuchOrderException;
import org.apache.beehive.samples.petstore.controls.exceptions.InvalidIdentifierException;
import org.apache.beehive.samples.petstore.controls.exceptions.OrderAlreadyExistsException;
import org.apache.beehive.samples.petstore.model.Address;
import org.apache.beehive.samples.petstore.model.LineItem;
import org.apache.beehive.samples.petstore.model.Order;
import org.apache.beehive.samples.petstore.model.OrderItem;
import org.apache.beehive.samples.petstore.forms.CheckoutForm;
import org.apache.beehive.controls.api.bean.Control;

@Jpf.Controller(
    nested=true, 
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="rootSharedFlow", type=webappRoot.SharedFlow.class)
    },
    messageBundles={ 
        @Jpf.MessageBundle(bundlePath="org.apache.beehive.samples.petstore.resources.order")
    },
    	simpleActions={
			@Jpf.SimpleAction(name="viewItem", path="/shop/viewItem.do"),
			@Jpf.SimpleAction(name="viewOrders", path="/account/edit/listOrders.do")
    }
)
public class Controller
    extends PageFlowController
{
	@Control()
    private AddressControl _addressControl;

    @Control()
    private OrderControl _orderControl;

    @Jpf.SharedFlowField(name="rootSharedFlow")
    private webappRoot.SharedFlow _sharedFlow;

    private Order _order = null;
    private CheckoutForm _orderForm = null;

    @Jpf.Action(
        forwards={@Jpf.Forward(name="success", path="viewCart.jsp")}
    )
    public Forward begin() {
        return new Forward("success");
    }

    @Jpf.Action(
        forwards={@Jpf.Forward(name="success", action="viewCreateOrder")
        }
    )
    protected Forward createNewOrder() {
        _sharedFlow.ensureLogin();

		_sharedFlow.getAccount().setStatus("checking_out");
		_order = _orderControl.createOrder();
    	_order.initOrder(_sharedFlow.getAccount().getUserId(), _sharedFlow.getCart());

        _orderForm = new CheckoutForm();
        _orderForm.setCheckOut(true);
        _orderForm.setOrder(_order);
		_orderForm.setCart(_sharedFlow.getCart());

        return new Forward("success");
    }

    @Jpf.Action(
	        forwards={
	            @Jpf.Forward(name="success", path="newOrder.jsp",
	                         actionOutputs = {
	                             @Jpf.ActionOutput(name = "creditCardTypes",
	                                               type = java.util.List.class,
	                                               required = true),
	                             @Jpf.ActionOutput(name = "addresses",
	                                               type = org.apache.beehive.samples.petstore.model.Address[].class,
	                                               required = true)
	                         }
	            )
	        }
	    )
	    protected Forward viewCreateOrder() {

	        _sharedFlow.ensureLogin();

	        Forward forward = new Forward("success");
	        forward.addOutputForm(_orderForm);
	        forward.addActionOutput("creditCardTypes", _orderControl.getCreditCards());
			forward.addActionOutput("addresses", _addressControl.getUserAddresses(_sharedFlow.getAccount().getUserId()));

	        return forward;
	}

	
    @Jpf.Action(
        forwards={ 
            @Jpf.Forward(name="done", returnAction="checkoutDone")
        }
    )
    public Forward done() {
        return new Forward("done");
    }

    @Jpf.Action(
        useFormBean="_orderForm",
        forwards={ 
            @Jpf.Forward(name="confirm", path="confirm.jsp",
                         actionOutputs = { 
		                    @Jpf.ActionOutput(name = "lineItems",
		                            type = java.util.List.class,
		                            required = true),
		                    @Jpf.ActionOutput(name = "order",
		                            type = org.apache.beehive.samples.petstore.model.Order.class,
		                            required = true),
		                    @Jpf.ActionOutput(name = "shippingAddress",
		                            type = org.apache.beehive.samples.petstore.model.Address.class,
		                            required = true),
		                    @Jpf.ActionOutput(name = "billingAddress",
		                            type = org.apache.beehive.samples.petstore.model.Address.class,
		                            required = true)
                         })
        }, 
        validationErrorForward=@Jpf.Forward(name="failure", navigateTo=Jpf.NavigateTo.currentPage)
    )
    protected Forward viewConfirm(CheckoutForm orderForm)
        throws InvalidIdentifierException {
        _sharedFlow.ensureLogin();

        Forward f = new Forward("confirm");
        f.addActionOutput("order", _order);
        f.addActionOutput("lineItems", _sharedFlow.getCart().getLineItems());
        f.addActionOutput("billingAddress", _addressControl.getAddress(orderForm.getOrder().getBillingAddress()));
        f.addActionOutput("shippingAddress", _addressControl.getAddress(orderForm.getOrder().getShippingAddress()));
        return f;
    }

    @Jpf.Action(
        forwards={@Jpf.Forward(name="success", action="viewOrder")}
    )
    protected Forward confirm()
        throws NoSuchOrderException, InvalidIdentifierException, OrderAlreadyExistsException {

        _sharedFlow.ensureLogin();

        // Write the order to the DB
		int orderId = _orderControl.commitOrder(_order, _sharedFlow.getCart());
        _sharedFlow.handleCheckout();
		
		// putting the orderId in the attributes flags this request as a new order
		getRequest().setAttribute("orderId", orderId);
		
        return new Forward("success");
    }

    @Jpf.Action(
        forwards={ 
            @Jpf.Forward(name="success", path="viewOrder.jsp",
                         actionOutputs = { 
                             @Jpf.ActionOutput(name = "order",
                                               type = org.apache.beehive.samples.petstore.model.Order.class,
                                               required = true),
                             @Jpf.ActionOutput(name = "lineItems",
	                                   type = org.apache.beehive.samples.petstore.model.LineItem[].class,
	                                   required = true),
   		                    @Jpf.ActionOutput(name = "shippingAddress",
		                            type = org.apache.beehive.samples.petstore.model.Address.class,
		                            required = true),
		                    @Jpf.ActionOutput(name = "billingAddress",
		                            type = org.apache.beehive.samples.petstore.model.Address.class,
		                            required = true)
                         })
        }
    )
    public Forward viewOrder()
        throws NoSuchOrderException, InvalidIdentifierException {
        _sharedFlow.ensureLogin();
		
		int orderId = -1;
		boolean bNew = false;
		
		if (getRequest().getAttribute("orderId") == null)
		{
			// existing order coming in from the order list
			orderId = Integer.parseInt(getRequest().getParameter("orderId"));
		} else {
			// New order, need to flag as such
			orderId = (Integer)(getRequest().getAttribute("orderId"));
			bNew = true;
		}
		
		_order = _orderControl.getOrder(_sharedFlow.getAccount().getUserId(), orderId);

		if (bNew)
			_order.setStatus("new");
		
		Address billingAddress;
		Address shippingAddress;
		
		billingAddress = _addressControl.getAddress(_order.getBillingAddress());
		shippingAddress = _addressControl.getAddress(_order.getShippingAddress());
		
        Forward f=new Forward("success");
        f.addActionOutput("order", _order);
		f.addActionOutput("lineItems", getLineItems(_order.getOrderId()));
        f.addActionOutput("billingAddress", billingAddress);
        f.addActionOutput("shippingAddress", shippingAddress);
        return f;
    }
	
	private LineItem[] getLineItems(int orderId)
	{
		OrderItem[] orderItems = _orderControl.getOrderItemsByOrderId(orderId);
		LineItem[] lineItems = new LineItem[orderItems.length];
        for (int i = 0; i < orderItems.length; i++)
        {
			lineItems[i] = new LineItem(orderId, i, orderItems[i]);
			lineItems[i].setQuantity(orderItems[i].getQuantity());
        }
		return lineItems;
	}
}