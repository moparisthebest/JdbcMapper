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

/**
 * <p>
 * Configuration JavaBean used in conjunction with the {@link SQLSupport} class to configure
 * the way in which SQL statements are created.  This abstract class can be implemented by
 * subclasses to provide varying ways of configuring these properties.
 * </p>
 */
public abstract class SQLSupportConfig {

    /**
     * Set the quote character used to quote strings types when embedded inside of SQL statements.
     * @param quoteChar the quote character
     */
    public abstract void setQuoteChar(String quoteChar);

    /**
     * Get the quote character used to wrap string types inside of SQL statements.
     * @return the quote character
     */
    public abstract String getQuoteChar();

    /**
     * Set a boolean that enables or disables generating an ESCAPE clause when generating a
     * LIKE clause into a SQL WHERE filter.
     * @param supportsLikeEscapeClause boolean to support escaping a LIKE clause
     */
    public abstract void setSupportsLikeEscapeClause(boolean supportsLikeEscapeClause);

    /**
     * Get a boolean that enables or disables an ESCAPE clause when generating a
     * LIKE clause in a SQL statement.
     * @return <code>true</code> if escaping is enabled; <code>false</code> otherwise.
     */
    public abstract boolean getSupportsLikeEscapeClause();
}
