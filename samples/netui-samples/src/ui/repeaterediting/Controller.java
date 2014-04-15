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
package ui.repeaterediting;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.Validatable;
import org.apache.beehive.samples.controls.pets.Pets;
import org.apache.beehive.samples.netui.beans.PetType;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 */
@Jpf.Controller(
    messageBundles={
        @Jpf.MessageBundle(bundlePath="org.apache.beehive.samples.netui.resources.repeaterediting.messages")
    },
    forwards=@Jpf.Forward(name="success", path="index.jsp")
)
public class Controller
    extends PageFlowController {

    private static final double GIFT_WRAP_PRICE = 4.95;

    @Control()
    private Pets _petControl;

    private CartForm _cartForm;

    @Jpf.Action(
        forwards=
            @Jpf.Forward(name="success", path="index.jsp",
                         actionOutputs=@Jpf.ActionOutput(name="totalPrice", type=java.lang.Double.class))
    )
    public Forward begin() {
        PetType[] pets = _petControl.getPetList();
        CartItem[] items = new CartItem[pets.length];
        for(int i = 0; i < items.length; i++) {
            items[i] = new PetCartItem(pets[i]);
            items[i].setQuantity(1);
            items[i].setGiftWrap(false);
        }

        _cartForm = new CartForm(items);

        Forward forward = new Forward("success");
        forward.addOutputForm(_cartForm);
        forward.addActionOutput("totalPrice", calculatePrice(_cartForm.getItems()));
        return forward;
    }

    @Jpf.Action(
        useFormBean="_cartForm",
        validationErrorForward=@Jpf.Forward(name="failure", navigateTo=Jpf.NavigateTo.currentPage)
    )
    public Forward update(CartForm cart) {
        Forward forward = new Forward("success");
        forward.addOutputForm(cart);
        forward.addActionOutput("totalPrice", calculatePrice(cart.getItems()));
        return forward;
    }

    private double calculatePrice(CartItem[] items) {
        double price = 0.0;
        for(CartItem item : items) {
            price += item.getQuantity() * item.getPrice();
            if(item.isGiftWrap())
                price += GIFT_WRAP_PRICE;
        }
        return price;
    }

    public static class CartForm
        implements Validatable, Serializable {

        private CartItem[] _items;

        public CartForm() {}

        public CartForm(CartItem[] items) {
            _items = items;
        }

        public CartItem[] getItems() {
            return _items;
        }

        public void validate(ActionMapping actionMapping,
                             HttpServletRequest httpServletRequest,
                             ActionMessages actionMessages) {

            for(int i = 0; i < _items.length; i++) {
                CartItem item = _items[i];
                if(item.getQuantity() <= 0) {
                    actionMessages.add("invalidQuantity" + i, new ActionMessage("invalidquantity", item.getQuantity()));
                }
            }
        }
    }

    public static abstract class CartItem
        implements Serializable {

        private int _quantity;
        private boolean _giftWrap;

        public int getQuantity() {
            return _quantity;
        }

        public void setQuantity(int quantity) {
            _quantity = quantity;
        }

        public boolean isGiftWrap() {
            return _giftWrap;
        }

        public void setGiftWrap(boolean giftWrap) {
            _giftWrap = giftWrap;
        }

        public abstract double getPrice();
    }

    public static class PetCartItem
        extends CartItem {

        private PetType _pet;

        public PetCartItem(PetType pet) {
            _pet = pet;
        }

        public int getPetId() {
            assert _pet != null;
            return _pet.getPetId();
        }

        public String getName() {
            assert _pet != null;
            return _pet.getName();
        }

        public double getPrice() {
            assert _pet != null;
            return _pet.getPrice();
        }

        public String getDescription() {
            assert _pet != null;
            return _pet.getDescription();
        }
    }
}
