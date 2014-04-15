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

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.samples.petstore.controls.exceptions.DataStoreException;
import org.apache.beehive.samples.petstore.model.Address;
import org.apache.log4j.Logger;
import java.sql.SQLException;

@ControlImplementation( isTransient=true )
public class DerbyAddressDao implements AddressDao
{
	@Control()
	private DerbyAddressDBControl _dbControl;
	private static final Logger _logger = Logger.getLogger( DerbyAccountDao.class );

	public Address[] getUserAddresses(String key)
    {
		Address[] addresses;
		try {
			addresses = _dbControl.getUserAddresses(key);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
		return addresses;
    }

	public Address getAddress(int key)
    {
		Address add = new Address();
		try {
			add = _dbControl.getAddress(key);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
		return add;
    }

    public void updateAddress(Address address)
    {
		try {
			_dbControl.updateAddress(address);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
    }

    public void insertAddress(Address address)
    {
		try {
			_dbControl.insertAddress(address);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
    }

    public void deleteAddress(int addressId, String userId)
	{
		try {
			_dbControl.deleteAddress(addressId, userId);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
	}
	
	public boolean checkAddressExists(int addressId, String userId)
	{
		boolean ret = false;
		try {
			if (_dbControl.checkAddressExists(addressId, userId) > 0)
				ret = true;
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
		return ret;
		
	}
}
