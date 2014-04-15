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

import org.apache.beehive.samples.petstore.model.Address;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

/** 
 * This control contains access to the pets database
 */ 

@org.apache.beehive.controls.api.bean.ControlExtension
@JdbcControl.ConnectionDriver(databaseDriverClass="org.apache.derby.jdbc.EmbeddedDriver",
		databaseURL="jdbc:derby:" + DBProperties.dbLocation + "/petStoreDB;create=true")  
public interface DerbyAddressDBControl extends JdbcControl {
    
	// checkAddressExists
    @SQL(statement="select count(*) from Addresses where addressId = {addressId} and userId = {userId}")
    public int checkAddressExists(int addressId, String userId) throws SQLException;

	// getUserAddresses
    @SQL(statement="select addressId, userId, name, phone, addr1, addr2, " +
    		"city, state, zip, country from Addresses where userId = {userId}") 
    public Address[] getUserAddresses(String userId) throws SQLException;

	// getAddress
    @SQL(statement="select addressId, userId, name, phone, addr1, addr2, " +
    		"city, state, zip, country from Addresses where addressId = {addressId}")
    public Address getAddress(int addressId) throws SQLException;

	// updateAddress
    @SQL(statement="update Addresses set name = {address.name}, " +
    		"phone = {address.phone}, addr1 = {address.addr1}, addr2 = {address.addr2}, " +
    		"city = {address.city}, state = {address.state}, zip = {address.zip}, country = {address.country} " +
    		"where addressId = {address.addressId}")
    public void updateAddress(Address address) throws SQLException;

	// insertAddress
    @SQL(statement="insert into Addresses(userId, name, phone, addr1, addr2, " +
    		"city, state, zip, country) values ({address.userId}, {address.name}, " +
    		"{address.phone}, {address.addr1}, {address.addr2}, " +
    		"{address.city}, {address.state}, {address.zip}, {address.country})")
    public void insertAddress(Address address) throws SQLException;

	// deleteAddress
    @SQL(statement="delete from Addresses where addressId = {addressId} and userId = {userId}")
    public void deleteAddress(int addressId, String userId) throws SQLException;
	
}