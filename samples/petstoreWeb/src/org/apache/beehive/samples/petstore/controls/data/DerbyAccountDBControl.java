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
package org.apache.beehive.samples.petstore.controls.data;

import java.sql.SQLException;

import org.apache.beehive.samples.petstore.model.Account;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

/** 
 * This control contains access to the pets database
 */ 

@org.apache.beehive.controls.api.bean.ControlExtension
@JdbcControl.ConnectionDriver(databaseDriverClass="org.apache.derby.jdbc.EmbeddedDriver",
		databaseURL="jdbc:derby:" + DBProperties.dbLocation + "/petStoreDB;create=true")  

public interface DerbyAccountDBControl extends JdbcControl {
	
	// getAccounts
    @SQL(statement="select userId, password, email, firstname, lastname, status, favCategory, " +
    		"langPref, bannerData, myListOpt as myListState, bannerOpt as bannerOptState from Accounts")
    public Account[] getAccounts() throws SQLException;

	// checkAccountExists
    @SQL(statement="select count(*) from Accounts where userId = {userId}")
    public int checkAccountExists(String userId) throws SQLException;
	
	// getAccountById
    @SQL(statement="select userId, password, email, firstname, lastname, status, favCategory, " +
    		"langPref, bannerData, myListOpt as myListState, bannerOpt as bannerOptState from Accounts " +
    		"where userId = {userId}")
    public Account getAccountById(String userId) throws SQLException;

	// updateAccount
    @SQL(statement="update Accounts set password = {account.password}, email = {account.email}, " +
    		"firstName = {account.firstName}, lastName = {account.lastName}, status = {account.status}, " +
    		"favCategory = {account.favCategory}, langPref = {account.langPref}, bannerData = {account.bannerData}, " +
    		"myListOpt = {account.myListState}, bannerOpt = {account.bannerOptState} where userId = {account.userId}")
    public void updateAccount(Account account) throws SQLException;

	// insertAccount
    @SQL(statement="insert into Accounts values ({account.userId}, {account.password}, {account.email}, {account.firstName}, " +
    		"{account.lastName}, {account.status}, {account.favCategory}, {account.langPref}, " +
    		"'', {account.myListState}, {account.bannerOptState})")
    public void insertAccount(Account account) throws SQLException;
}