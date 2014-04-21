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

package org.apache.beehive.controls.system.jdbc.parser;

import org.apache.beehive.controls.api.ControlException;

import java.util.HashMap;
import java.io.StringReader;

/**
 * The SqlParser class is a thread-safe class which parses a string containing a SQL statement
 * with JdbcControl substitituion delimiters. It is important to note that the SQL is not parsed/validated - only
 * the sections within the SQL string which are delimited by '{' and '}' are parsed.
 * <p/>
 * Parsing is accomplished using the JavaCC grammar file <tt>SqlGrammer.jj</tt>.  As the string is parsed it is broken
 * into fragments by the parser.  Any portion of the string which is not between '{' and '}' delimiters becomes a
 * <tt>LiteralFragment</tt>.  The portions of the SQL string which fall between the start and end delimiters are categorized as
 * either <tt>JdbcFragment</tt>, <tt>ReflectionFragment</tt>, or <tt>SqlSubstitutionFragment</tt>.
 * <p/>
 * Fragments which subclass <tt>SqlFragmentContainer</tt> may contain other fragments as children.  Fragements subclassed
 * from <tt>SqlFragment</tt> my not contain child fragments. Upon completion of parsing a <tt>SqlStatement</tt> is
 * returned to the caller.  The <tt>SqlStatement</tt> contains the heirarchary of fragments which have been derived
 * from the orignal SQL string.
 * <p/>
 * The parser will also cache all <tt>SqlStatements</tt> which contain non-volitale SQL. Only <tt>SqlEscapeFragments</tt>
 * contain volitile SQL at this point.
 */
public final class SqlParser {

    // maintain a cache of SQLStatements which have already been parsed
    private HashMap<String, SqlStatement> _cachedSqlStatements;

    /**
     * Create a new instance of the SqlParser.
     */
    public SqlParser() {
        _cachedSqlStatements = new HashMap<String, SqlStatement>();
    }

    /**
     * Parse the sql and return an SqlStatement.
     *
     * @param sql A String contianing the sql to parse.
     * @return A SqlStatement instance.
     */
    public SqlStatement parse(String sql) {

        // does a cached parse result exist for this statement?
        if (_cachedSqlStatements.containsKey(sql)) {
            return _cachedSqlStatements.get(sql);
        }

        SqlGrammar _parser = new SqlGrammar(new StringReader(sql));
        SqlStatement parsed = null;
        try {
            parsed = _parser.parse();
        } catch (ParseException e) {
            throw new ControlException("Error parsing SQL statment." + e.getMessage(), e);
        } catch (TokenMgrError tme) {
            throw new ControlException("Error parsing SQL statment. " + tme.getMessage(), tme);
        }

        if (parsed.isCacheable()) {
            _cachedSqlStatements.put(sql, parsed);
        }
        return parsed;
    }
}
