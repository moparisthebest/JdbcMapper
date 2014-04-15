/**
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
package org.apache.beehive.samples.petstore.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.samples.petstore.model.Account;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 *
 */
public class UpdateAccountForm
    extends AccountForm {

    private String _newPassword;
    private String _repeatedPassword;

    public UpdateAccountForm() {
        super();
    }

    public UpdateAccountForm(Account account) {
        super(account);
    }

    public String getNewPassword() {
        return _newPassword;
    }

    public void setNewPassword(String value) {
        _newPassword = value;
    }

    public String getRepeatedPassword() {
        return _repeatedPassword;
    }

    public void setRepeatedPassword(String value) {
        _repeatedPassword = value;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        baseValidate(errors, mapping, request);
        return errors;
    }
}
