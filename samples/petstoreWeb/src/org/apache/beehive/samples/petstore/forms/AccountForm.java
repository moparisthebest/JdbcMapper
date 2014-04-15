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

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.beehive.samples.petstore.model.Account;

import javax.servlet.http.HttpServletRequest;

public abstract class AccountForm
    extends AbstractPetstoreForm {

    private Account _account;

    public AccountForm() {
        super();
    }

    public AccountForm(Account account) {
        this();
        _account = account;
    }

    protected Account getAccount() {
        return _account;
    }

    public String getUserId() {
        return _account.getUserId();
    }

    public String getFirstName() {
        return _account.getFirstName();
    }

    public void setFirstName(String firstName) {
        _account.setFirstName(firstName);
    }

    public String getLastName() {
        return _account.getLastName();
    }

    public void setLastName(String lastName) {
        _account.setLastName(lastName);
    }

    public String getEmail() {
        return _account.getEmail();
    }

    public void setEmail(String email) {
        _account.setEmail(email);
    }

    public String getFavCategory() {
        return _account.getFavCategory();
    }

    public void setFavCategory(String value) {
        _account.setFavCategory(value);
    }

    public String getLangPref() {
        return _account.getLangPref();
    }

    public void setLangPref(String value) {
        _account.setLangPref(value);
    }

    public boolean isMyListOpt() {
        return _account.isMyListOpt();
    }

    public void setMyListOpt(boolean value) {
        _account.setMyListOpt(value);
    }

    public boolean isBannerOpt() {
        return _account.isBannerOpt();
    }

    public void setBannerOpt(boolean value) {
        _account.setBannerOpt(value);
    }

    public ActionErrors baseValidate(ActionErrors errors, ActionMapping mapping, HttpServletRequest request) {
        addErrorIfStringEmpty(errors, "firstName", "account.error.firstName.required", _account.getFirstName());
        addErrorIfStringEmpty(errors, "lastName", "account.error.lastName.required", _account.getLastName());
        addErrorIfStringEmpty(errors, "email", "account.error.email.required", _account.getEmail());

        return errors;
    }

    public static final Account getAccount(AccountForm form) {
        return form.getAccount();
    }
}
