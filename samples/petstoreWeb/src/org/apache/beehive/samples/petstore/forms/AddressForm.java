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
package org.apache.beehive.samples.petstore.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import org.apache.beehive.samples.petstore.model.Address;

public abstract class AddressForm
    extends AbstractPetstoreForm {

    private Address _address;

    public AddressForm() {
        super();
    }

    public AddressForm(Address address) {
        this();
        _address = address;
    }

    protected Address getAddress() {
        return _address;
    }

    public int getAddressId() {
        return _address.getAddressId();
    }

    public void setAddressId(int addressId) {
        _address.setAddressId(addressId);
    }
	
	public String getUserId() {
        return _address.getUserId();
    }

    public String getName() {
        return _address.getName();
    }

    public void setName(String name) {
        _address.setName(name);
    }

	public String getAddr1() {
        return _address.getAddr1();
    }

    public void setAddr1(String value) {
        _address.setAddr1(value);
    }

    public String getAddr2() {
        return _address.getAddr2();
    }

    public void setAddr2(String value) {
        _address.setAddr2(value);
    }

    public String getCity() {
        return _address.getCity();
    }

    public void setCity(String value) {
        _address.setCity(value);
    }

    public String getState() {
        return _address.getState();
    }

    public void setState(String value) {
        _address.setState(value);
    }

    public String getZip() {
        return _address.getZip();
    }

    public void setZip(String value) {
        _address.setZip(value);
    }

    public String getCountry() {
        return _address.getCountry();
    }

    public void setCountry(String value) {
        _address.setCountry(value);
    }

    public String getPhone() {
        return _address.getPhone();
    }

    public void setPhone(String value) {
        _address.setPhone(value);
    }
	
    public ActionErrors baseValidate(ActionErrors errors, ActionMapping mapping, HttpServletRequest request) {
        addErrorIfStringEmpty(errors, "name", "address.error.name.required", _address.getName());
        addErrorIfStringEmpty(errors, "phone", "address.error.phone.required", _address.getPhone());
        addErrorIfStringEmpty(errors, "address1", "address.error.address1.required", _address.getAddr1());
        addErrorIfStringEmpty(errors, "city", "address.error.city.required", _address.getCity());
        addErrorIfStringEmpty(errors, "state", "address.error.state.required", _address.getState());
        addErrorIfStringEmpty(errors, "zip", "address.error.zip.required", _address.getZip());
        addErrorIfStringEmpty(errors, "country", "address.error.country.required", _address.getCountry());

        return errors;
    }

    public static final Address getAddress(AddressForm form) {
        return form.getAddress();
    }
}
