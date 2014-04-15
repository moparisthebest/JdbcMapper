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
package org.apache.beehive.controls.system.jdbc;

import org.apache.beehive.controls.api.ControlException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Calendar;

/**
 * Map a ResultSet row to a java.util.Map object.
 */
public final class RowToMapMapper extends RowMapper {
    
    private final String[] _keys;

    /**
     * Create a new RowToMapMapper.
     * @param resultSet ResultSet to map
     * @param returnTypeClass Class to map to.
     * @param cal Calendar instance for date/time mappings.
     */
    RowToMapMapper(ResultSet resultSet, Class returnTypeClass, Calendar cal) {
        super(resultSet, returnTypeClass, cal);
        try {
        _keys = getKeysFromResultSet();
        } catch (SQLException e) {
            throw new ControlException("RowToMapMapper: SQLException: " + e.getMessage(), e);
        }
    }

    /**
     * Do the mapping.
     * @return A Map.
     * @throws ControlException on error.
     */
    public Object mapRowToReturnType() {
        try {
        return Collections.unmodifiableMap(new ResultSetHashMap(_resultSet, _keys));
        } catch (SQLException e) {
           throw new ControlException("Exception while creating ResultSetHashMap.", e);
        }
    }
}
