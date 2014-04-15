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

package org.apache.beehive.controls.system.jdbc.test.dbconnection;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * Test type mapper class.
 */
public class TestTypeMapper implements SQLData {

    /**
     * Returns the fully-qualified
     * name of the SQL user-defined type that this object represents.
     * This method is called by the JDBC driver to get the name of the
     * UDT instance that is being mapped to this instance of
     * <code>SQLData</code>.
     *
     * @return the type name that was passed to the method <code>readSql</code>
     *         when this object was constructed and populated
     * @throws java.sql.SQLException if there is a database access error
     * @since 1.2
     */
    public String getSQLTypeName() throws SQLException {
        return null;
    }

    /**
     * Populates this object with data read from the database.
     * The implementation of the method must follow this protocol:
     * <UL>
     * <LI>It must read each of the attributes or elements of the SQL
     * type  from the given input stream.  This is done
     * by calling a method of the input stream to read each
     * item, in the order that they appear in the SQL definition
     * of the type.
     * <LI>The method <code>readSQL</code> then
     * assigns the data to appropriate fields or
     * elements (of this or other objects).
     * Specifically, it must call the appropriate <i>reader</i> method
     * (<code>SQLInput.readString</code>, <code>SQLInput.readBigDecimal</code>,
     * and so on) method(s) to do the following:
     * for a distinct type, read its single data element;
     * for a structured type, read a value for each attribute of the SQL type.
     * </UL>
     * The JDBC driver initializes the input stream with a type map
     * before calling this method, which is used by the appropriate
     * <code>SQLInput</code> reader method on the stream.
     *
     * @param stream   the <code>SQLInput</code> object from which to read the data for
     *                 the value that is being custom mapped
     * @param typeName the SQL type name of the value on the data stream
     * @throws java.sql.SQLException if there is a database access error
     * @see java.sql.SQLInput
     */
    public void readSQL(SQLInput stream, String typeName) throws SQLException { }

    /**
     * Writes this object to the given SQL data stream, converting it back to
     * its SQL value in the data source.
     * The implementation of the method must follow this protocol:<BR>
     * It must write each of the attributes of the SQL type
     * to the given output stream.  This is done by calling a
     * method of the output stream to write each item, in the order that
     * they appear in the SQL definition of the type.
     * Specifically, it must call the appropriate <code>SQLOutput</code> writer
     * method(s) (<code>writeInt</code>, <code>writeString</code>, and so on)
     * to do the following: for a Distinct Type, write its single data element;
     * for a Structured Type, write a value for each attribute of the SQL type.
     *
     * @param stream the <code>SQLOutput</code> object to which to write the data for
     *               the value that was custom mapped
     * @throws java.sql.SQLException if there is a database access error
     * @see java.sql.SQLOutput
     * @since 1.2
     */
    public void writeSQL(SQLOutput stream) throws SQLException { }
}
