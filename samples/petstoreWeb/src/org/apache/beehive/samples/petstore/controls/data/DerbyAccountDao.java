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
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.samples.petstore.controls.exceptions.DataStoreException;
import org.apache.beehive.samples.petstore.model.Account;
import org.apache.log4j.Logger;

@ControlImplementation( isTransient=true )
public class DerbyAccountDao implements AccountDao {

	@Control
    private DerbyAccountDBControl _dbControl;
	private static final Logger _logger = Logger.getLogger( DerbyAccountDao.class );

    public boolean checkAccountExists( String userId ) throws DataStoreException
    {
        try {
            int accountExists = _dbControl.checkAccountExists( userId );
            if( accountExists > 0 ) {
                return true;
            } else {
                return false;
            }
        } catch( SQLException e ) {
            _logger.error( "Unexpected DAO exception", e );
            throw new DataStoreException( "unexpected database exception" );
        }
    }

    public Account getAccountById( String userId ) throws DataStoreException
    {
        try {
            return _dbControl.getAccountById( userId );
        } catch( SQLException e ) {
            _logger.error( "Unexpected DAO exception", e );
            throw new DataStoreException( "unexpected database exception" );
        }
    }

    public void updateAccount( Account account ) throws DataStoreException
    {
        try {
            _dbControl.updateAccount( account );
        } catch( SQLException e ) {
            _logger.error( "Unexpected DAO exception", e );
            throw new DataStoreException( "unexpected database exception" );
        }
    }

    public void insertAccount( Account account ) throws DataStoreException
    {
        try {
            _dbControl.insertAccount( account );
        } catch( SQLException e ) {
            _logger.error( "Unexpected DAO exception", e );
            throw new DataStoreException( "unexpected database exception" );
        }
    }
}