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
package org.apache.beehive.samples.petstore.model;

public class Address {

	private int _addressId = -1;
	private String _userId;
    private String _name;
    private String _phone;

    private String _addr1;
    private String _addr2;
    private String _city;
    private String _state;
    private String _zip;
    private String _country;

    public Address() {
    }

    public int getAddressId() {
        return _addressId;
    }

    public void setAddressId(int addressId) {
        _addressId = addressId;
    }

    public String getUserId() {
        return _userId;
    }

    public void setUserId(String userId) {
        _userId = userId;
    }

	public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getAddr1() {
        return _addr1;
    }

    public void setAddr1(String addr1) {
        _addr1 = addr1;
    }

    public String getAddr2() {
        return _addr2;
    }

    public void setAddr2(String addr2) {
        _addr2 = addr2;
    }

    public String getCity() {
        return _city;
    }

    public void setCity(String city) {
        _city = city;
    }

    public String getState() {
        return _state;
    }

    public void setState(String state) {
        _state = state;
    }

    public String getZip() {
        return _zip;
    }

    public void setZip(String zip) {
        _zip = zip;
    }

    public String getCountry() {
        return _country;
    }

    public void setCountry(String country) {
        _country = country;
    }

    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }
}
