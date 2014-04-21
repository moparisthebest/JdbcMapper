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
package org.apache.beehive.netui.databinding.datagrid.runtime.sql;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * <p>
 * Factory used to create {@link SQLSupportConfig} instances.  Clients can request either a JavaBean
 * on which to manually set properties or a SQLSupportConfig object that is populated via
 * a {@link DatabaseMetaData} object.
 * </p>
 */
public final class SQLSupportConfigFactory {

    /**
     * Create a default {@link SQLSupportConfig} JavaBean.
     * @return the sql support config JavaBean
     */
    public static final SQLSupportConfig getInstance() {
        return new DefaultSQLSupportConfig();
    }

    /**
     * Create an instance of a {@link SQLSupportConfig} JavaBean that is created from a
     * {@link DatabaseMetaData} object.  The {@link DatabaseMetaData} instance will be
     * used to set the SQLSupportConfig JavaBean properties based on the metadata
     * from the database.
     * @param databaseMetaData the metadata to use to populate the SQLSupportConfig object
     * @return the SQLSupportConfig object
     * @throws SQLException if an error is thrown creating the SQLSupportConfig JavaBean
     */
    public static final SQLSupportConfig getInstance(DatabaseMetaData databaseMetaData)
            throws SQLException {
        SQLSupportConfig config = new DatabaseMetaDataSQLSupportConfig(databaseMetaData);
        return config;
    }
}
