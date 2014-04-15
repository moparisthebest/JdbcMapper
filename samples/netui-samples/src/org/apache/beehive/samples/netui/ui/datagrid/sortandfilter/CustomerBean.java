/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   $Header:$
*/
package org.apache.beehive.samples.netui.ui.datagrid.sortandfilter;

import java.util.LinkedList;
import java.util.List;

/**
 * JavaBean representing a Customer object.
 */
public class CustomerBean {

    private int _customerId;
    private String _companyName;
    private String _contactName;
    private String _contactTitle;
    private String _address;
    private String _city;
    private String _region;
    private String _postalCode;
    private String _country;
    private String _phone;
    private String _fax;

    public CustomerBean() {
    }

    public CustomerBean(int customerId,
                        String companyName,
                        String contactName,
                        String contactTitle,
                        String address,
                        String city,
                        String region,
                        String postalCode,
                        String country,
                        String phone,
                        String fax) {
        _customerId = customerId;
        _companyName = companyName;
        _contactName = contactName;
        _contactTitle = contactTitle;
        _address = address;
        _city = city;
        _region = region;
        _postalCode = postalCode;
        _country = country;
        _phone = phone;
        _fax = fax;
    }

    public int getCustomerId() {
        return _customerId;
    }

    public void setCustomerId(int customerId) {
        _customerId = customerId;
    }

    public String getCompanyName() {
        return _companyName;
    }

    public void setCompanyName(String companyName) {
        _companyName = companyName;
    }

    public String getContactName() {
        return _contactName;
    }

    public void setContactName(String contactName) {
        _contactName = contactName;
    }

    public String getContactTitle() {
        return _contactTitle;
    }

    public void setContactTitle(String contactTitle) {
        _contactTitle = contactTitle;
    }

    public String getAddress() {
        return _address;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public String getCity() {
        return _city;
    }

    public void setCity(String city) {
        _city = city;
    }

    public String getRegion() {
        return _region;
    }

    public void setRegion(String region) {
        _region = region;
    }

    public String getPostalCode() {
        return _postalCode;
    }

    public void setPostalCode(String postalCode) {
        _postalCode = postalCode;
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

    public String getFax() {
        return _fax;
    }

    public void setFax(String fax) {
        _fax = fax;
    }
}