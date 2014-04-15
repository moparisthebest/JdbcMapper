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
package org.apache.beehive.samples.petstore.controls;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.samples.petstore.controls.AccountControl;
import org.apache.beehive.samples.petstore.controls.data.AccountDao;
import org.apache.beehive.samples.petstore.controls.exceptions.AccountAlreadyExistsException;
import org.apache.beehive.samples.petstore.controls.exceptions.InvalidIdentifierException;
import org.apache.beehive.samples.petstore.controls.exceptions.NoSuchAccountException;
import org.apache.beehive.samples.petstore.model.Account;

@ControlImplementation( isTransient=true )
public class AccountControlImpl implements AccountControl
{
	@Control()
	private AccountDao _accountDao;

    public boolean checkAccountExists(String key)
    {
		return _accountDao.checkAccountExists(key);
    }

    public Account getAccount(String key) throws NoSuchAccountException
    {
        if (!checkAccountExists(key))
            throw new NoSuchAccountException("no Account found for userId: " + key);

		Account acct = new Account();
		acct = _accountDao.getAccountById(key);
		if (acct.getUserId() == null)
			throw new NoSuchAccountException("no Account found for userId: " + key);
		return acct;
    }

    public void updateAccount(Account account) 
        throws InvalidIdentifierException, NoSuchAccountException
    {
        String userId = account.getUserId();
        if (userId == null || userId.length()==0)
            throw new InvalidIdentifierException("cannot update Account with null or empty userId");

        if (!checkAccountExists(userId))
            throw new NoSuchAccountException("no Account found for userId: "+userId);

        doUpdateAccount(account);
    }

    public void insertAccount(Account account)
         throws InvalidIdentifierException, AccountAlreadyExistsException
    {
        String userId = account.getUserId();
        if (userId == null || userId.length() == 0)
        {
            throw new InvalidIdentifierException("cannot insert Account with null userId");
        }

        if (checkAccountExists(userId))
            throw new AccountAlreadyExistsException("attempted to insert Account with duplicate userId: "+userId);

        doInsertAccount(account);
    }

    private void doUpdateAccount(Account account)
    {
		_accountDao.updateAccount(account);
    }

    private void doInsertAccount(Account account)
    {
		_accountDao.insertAccount(account);
    }
}
