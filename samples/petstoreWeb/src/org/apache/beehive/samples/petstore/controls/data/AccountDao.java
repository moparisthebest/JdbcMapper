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

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.samples.petstore.controls.exceptions.DataStoreException;
import org.apache.beehive.samples.petstore.model.Account;

@ControlInterface(
    defaultBinding="org.apache.beehive.samples.petstore.controls.data.DerbyAccountDao"
)
public interface AccountDao {

    public boolean checkAccountExists( String userId ) throws DataStoreException;
    public Account getAccountById( String userId ) throws DataStoreException;
    public void updateAccount( Account account ) throws DataStoreException;
    public void insertAccount( Account account ) throws DataStoreException;
}

