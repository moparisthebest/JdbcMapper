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
import org.apache.beehive.samples.petstore.controls.data.AddressDao;
import org.apache.beehive.samples.petstore.controls.exceptions.InvalidIdentifierException;
import org.apache.beehive.samples.petstore.controls.exceptions.NoSuchAddressException;
import org.apache.beehive.samples.petstore.model.Address;

@ControlImplementation( isTransient=true )
public class AddressControlImpl implements AddressControl
{
	@Control()
	private AddressDao _addressDao;

    public Address[] getUserAddresses(String key)
    {
		return _addressDao.getUserAddresses(key);
    }

	public Address getAddress(int key) throws InvalidIdentifierException
    {
		Address add = new Address();
		add = _addressDao.getAddress(key);
		if (add == null)
			throw new InvalidIdentifierException("Address: " + key + " not found!");

		return add;
    }

    public void updateAddress(Address address) 
        throws InvalidIdentifierException, NoSuchAddressException
    {
        String userId = address.getUserId();
		int addressId = address.getAddressId();
		
        if (userId == null || userId.length() == 0 || addressId == -1)
            throw new InvalidIdentifierException("cannot update Address with null or empty userId or addressId of -1");

		if (!_addressDao.checkAddressExists(addressId, userId))
			throw new NoSuchAddressException("no Address found for userId: " + userId + " and addressId " + addressId);
			
		_addressDao.updateAddress(address);
    }

    public void insertAddress(Address address) throws InvalidIdentifierException
    {
        String userId = address.getUserId();
        if (userId == null || userId.length() == 0)
            throw new InvalidIdentifierException("cannot insert Address with null userId");

		_addressDao.insertAddress(address);
    }

    public void deleteAddress(int addressId, String userId)
	    throws InvalidIdentifierException, NoSuchAddressException
	{
        if (userId == null || userId.length() == 0 || addressId == -1)
            throw new InvalidIdentifierException("cannot delete Address with null or empty userId or addressId of -1");

		if (!_addressDao.checkAddressExists(addressId, userId))
			throw new NoSuchAddressException("no Address found for userId: " + userId + " and addressId " + addressId);
			
		_addressDao.deleteAddress(addressId, userId);
	}
}
