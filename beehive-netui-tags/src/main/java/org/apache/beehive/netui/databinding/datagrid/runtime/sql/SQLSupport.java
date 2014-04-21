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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterOperation;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterOperationHint;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterTypeHint;
import org.apache.beehive.netui.databinding.datagrid.api.filter.Filter;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortDirection;
import org.apache.beehive.netui.databinding.datagrid.api.sort.Sort;
import org.apache.beehive.netui.databinding.datagrid.api.exceptions.IllegalFilterException;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * <p>
 * This class provides support for the SQL query language for a data grid's {@link Sort} and {@link Filter} JavaBeans.
 * Support is provided here for obtaining a list of supported SQL filter operations.  There is also support for
 * generating two kinds of SQL statements:
 * <ul>
 * <li>ORDER BY clause given a {@link List} of {@link Sort} beans</li>
 * <li>WHERE clause given a {@link List} of {@link Filter} beans</li>
 * </ul>
 * </p>
 */
public final class SQLSupport {

    private static final Logger LOGGER = Logger.getInstance(SQLSupport.class);
    private static final String EMPTY = "";
    private static final FilterOperation[] FILTER_OPERATIONS;
    private static final SQLSupportConfig DEFAULT_SQL_SUPPORT_CONFIG = SQLSupportConfigFactory.getInstance();

    static {
        FILTER_OPERATIONS = new FilterOperation[]{
            new FilterOperation(0, "*", "filter.sql.none", FilterOperationHint.NONE),
            new FilterOperation(1, "eq", "filter.sql.equal", FilterOperationHint.EQUAL),
            new FilterOperation(2, "ne", "filter.sql.notequal", FilterOperationHint.NOT_EQUAL),
            new FilterOperation(3, "gt", "filter.sql.greaterthan", FilterOperationHint.GREATER_THAN),
            new FilterOperation(4, "lt", "filter.sql.lessthan", FilterOperationHint.LESS_THAN),
            new FilterOperation(5, "ge", "filter.sql.greaterthanorequal", FilterOperationHint.GREATER_THAN_OR_EQUAL),
            new FilterOperation(6, "le", "filter.sql.lessthanorequal", FilterOperationHint.LESS_THAN_OR_EQUAL),
            new FilterOperation(7, "in", "filter.sql.isoneof", FilterOperationHint.IS_ONE_OF),
            new FilterOperation(8, "startswith", "filter.sql.startswith", FilterOperationHint.STARTS_WITH),
            new FilterOperation(9, "contains", "filter.sql.contains", FilterOperationHint.CONTAINS),
            new FilterOperation(10, "isempty", "filter.sql.isempty", FilterOperationHint.IS_EMPTY),
            new FilterOperation(11, "isnotempty", "filter.sql.isnotempty", FilterOperationHint.IS_NOT_EMPTY)
        };
    }

    private static final FilterOperation DEFAULT_STRING_FILTER_OPERATION = FILTER_OPERATIONS[9];
    private static final FilterOperation DEFAULT_NONSTRING_FILTER_OPERATION = FILTER_OPERATIONS[1];

    /**
     * Get an instance of this class configured using a default {@link SQLSupportConfig}.
     *
     * @return a SQLSupport instance
     */
    public static SQLSupport getInstance() {
        return getInstance(DEFAULT_SQL_SUPPORT_CONFIG);
    }

    /**
     * Get an instance of this class configured using a {@link SQLSupportConfig} that has been configured using
     * the provided {@link DatabaseMetaData}.
     * @param databaseMetaData the database metadata used to configure a {@link SQLSupportConfig} object
     * @return a SQLSupport instance
     * @throws SQLException when an error occurs reading from {@link DatabaseMetaData}
     */
    public static SQLSupport getInstance(DatabaseMetaData databaseMetaData)
            throws SQLException {
        SQLSupportConfig config = SQLSupportConfigFactory.getInstance(databaseMetaData);
        return getInstance(config);
    }

    /**
     * Get an instance of this class configured using the provided {@link SQLSupportConfig}.  The caller
     * should create and appropriately initialize the config object.
     * @param config the config object use to configure a SQLSupport instance
     * @return a SQLSupport instance
     */
    public static SQLSupport getInstance(SQLSupportConfig config) {
        SQLSupport sqlSupport = new SQLSupport();
        sqlSupport.configure(config);
        return sqlSupport;
    }

    /**
     * Get the readable string labels for a filter operation.  This {@link Map} contains a set of
     * filter operation abbreviations mapped to a label for that filter operation.  The abbreviations
     * can be used to lookup the correct filter operation.  This method accepts values enumerated
     * in {@link FilterTypeHint} and available via
     * {@link org.apache.beehive.netui.databinding.datagrid.api.filter.FilterTypeHint#getHint()}.
     *
     * @param typeHint the type hint whose matching operations to lookup
     * @return Map a {@link Map} of String abbreviations to readable string names for the operation
     */
    public static Map lookupFilterOperationLabels(String typeHint) {
        LinkedHashMap ops = new LinkedHashMap/*<String, String>*/();

        /* todo: i18n */
        /* todo: caching of the filterOps for a given type hint */
        ops.put(FILTER_OPERATIONS[0].getAbbreviation(), "No Filter");
        ops.put(FILTER_OPERATIONS[1].getAbbreviation(), "Equals");
        ops.put(FILTER_OPERATIONS[2].getAbbreviation(), "Not Equal");
        ops.put(FILTER_OPERATIONS[3].getAbbreviation(), "Greater Than");
        ops.put(FILTER_OPERATIONS[4].getAbbreviation(), "Less Than");
        ops.put(FILTER_OPERATIONS[5].getAbbreviation(), "Is Greater Than or Equal To");
        ops.put(FILTER_OPERATIONS[6].getAbbreviation(), "Is Less Than or Equal To");

        if(!(FilterTypeHint.DATE.equals(FilterTypeHint.getTypeHint(typeHint)))) {
            ops.put(FILTER_OPERATIONS[7].getAbbreviation(), "Is One Of (eg: 1;2;3)");
        }

        if(typeHint == null || FilterTypeHint.STRING.equals(FilterTypeHint.getTypeHint(typeHint))) {
            ops.put(FILTER_OPERATIONS[8].getAbbreviation(), "Starts With");
            ops.put(FILTER_OPERATIONS[9].getAbbreviation(), "Contains");
            ops.put(FILTER_OPERATIONS[10].getAbbreviation(), "Is Empty");
            ops.put(FILTER_OPERATIONS[11].getAbbreviation(), "Is Not Empty");
        }

        return ops;
    }

    /* todo: method returning filter labels given a FitlerTypeHint */
    /* todo: method returning the FILTER_OPERATIONS */
    /* todo: method returning FILTER_OPERATIONS given a FitlerTypeHint */

    /**
     * <p>
     * Lookup the default filter operation's abbreviation given a filter type hint abbreviation.  The type hint
     * should be obtained via {@link org.apache.beehive.netui.databinding.datagrid.api.filter.FilterTypeHint#getHint()}.
     * The default filter operations for a given FilterTypeHint string maps to the following FilterOperationHint.
     * The string returned is the associated FilterOperation's abbreviation
     * <table>
     * <tr><td>Type Hint</td><td></td></tr>
     * <tr><td>{@link FilterTypeHint#DATE}</td><td>{@link FilterOperationHint#EQUAL}</td></tr>
     * <tr><td>{@link FilterTypeHint#NUMERIC}</td><td>{@link FilterOperationHint#EQUAL}</td></tr>
     * <tr><td>{@link FilterTypeHint#STRING}</td><td>{@link FilterOperationHint#CONTAINS}</td></tr>
     * </table>
     * </p>
     * @param typeHint the type hint whose default operation to lookup
     * @return String the abbreviation
     */
    public static String lookoupDefaultFilterOperationAbbreviation(String typeHint) {
        FilterOperation fOp = DEFAULT_NONSTRING_FILTER_OPERATION;
        if(typeHint == null || FilterTypeHint.STRING.equals(FilterTypeHint.getTypeHint(typeHint)))
            fOp = DEFAULT_STRING_FILTER_OPERATION;

        return fOp.getAbbreviation();
    }

    /**
     * Lookup a filter operation given a filter operation abbreviation.  The abbreviation should be obtained
     * via {@link org.apache.beehive.netui.databinding.datagrid.api.filter.FilterOperation#getAbbreviation()}.
     * @param abbrev
     * @return the filter operation
     */
    public static final FilterOperation mapFilterAbbreviationToOperation(String abbrev) {
        for(int i = 0; i < FILTER_OPERATIONS.length; i++) {
            FilterOperation fOp = FILTER_OPERATIONS[i];
            if(fOp.getAbbreviation().equals(abbrev))
                return fOp;
        }
        return null;
    }

    /**
     * Map a {@link FilterOperationHint} to a SQL-specific {@link FilterOperation}.  When using SQL as a query
     * language, all of the operations defined in {@link FilterOperationHint} should be supported.
     *
     * @param hint the hint
     * @return the {@link FilterOperation} matching the given hint.
     */
    public static final FilterOperation mapFilterHintToOperation(FilterOperationHint hint) {
        for(int i = 0; i < FILTER_OPERATIONS.length; i++) {
            FilterOperation op = FILTER_OPERATIONS[i];
            if(op.getOperationHint().equals(hint))
                return op;
        }
        return null;
    }

    private SQLSupportConfig _config = null;

    /**
     * Private constructor.  All access to this should be done through the static factory methods
     * on the class.
     */
    private SQLSupport() {
    }

    /**
     * Set the {@link SQLSupportConfig} object useed to configure the SQL statements produced by this class.
     * @param config the config object
     */
    public void configure(SQLSupportConfig config) {
        _config = config;
    }

    /**
     * <p>
     * Create a SQL order fragment from the list of {@link Sort} objects.  This fragment does not begin with
     * ORDER BY and is just the <i>fragment</i> for such a clause.  If the given list of
     * sorts contains a sort with sort expression "foo" and sort direction {@link SortDirection#DESCENDING},
     * the generated SQL statement will appear as:
     * <pre>
     *     foo DESC
     * </pre>
     * </p>
     * @param sorts the list of {@link Sort} objects
     * @return the generated SQL statement order fragment or an emtpy string if there are no sorts
     */
    public final String createOrderByFragment(List/*<Sort>*/ sorts) {
        if(sorts == null || sorts.size() == 0)
            return EMPTY;

        InternalStringBuilder sql = new InternalStringBuilder();
        internalCreateOrderByFragment(sql, sorts);
        return sql.toString();
    }

    /**
     * <p>
     * Create a SQL ORDER BY clause from the list of {@link Sort} objects.  This fragment begins with
     * ORDER BY.  If the given list of sorts contains a sort with sort expression "foo" and sort direction
     * {@link SortDirection#DESCENDING}, the generated SQL statement will appear as:
     * <pre>
     *     ORDER BY foo DESC
     * </pre>
     * </p>
     * @param sorts the list of {@link Sort} objects
     * @return the generated SQL ORDER BY clause or an emtpy string if there are no sorts
     */
    public final String createOrderByClause(List/*<Sort>*/ sorts) {
        if(sorts == null || sorts.size() == 0)
            return EMPTY;

        InternalStringBuilder sql = new InternalStringBuilder(64);
        sql.append("ORDER BY ");
        internalCreateOrderByFragment(sql, sorts);
        return sql.toString();
    }

    /**
     * <p>
     * Create a SQL WHERE clause from the list of {@link Filter} objects.  This fragment does not begin with
     * WHERE.  If the given list of sorts contains a Filter with filter expression "foo", operation equals,
     * and value '42', the generated SQL statement will appear as:
     * <pre>
     *     foo = 42
     * </pre>
     * When multiple Filters in the list, the filters will be AND'ed together in the generated SQL statement.
     * </p>
     * @param filters the list of {@link Filter} objects
     * @return the generated SQL where clause fragment or an emtpy string if there are no filters
     */
    public String createWhereFragment(List/*<Filter>*/ filters) {
        if(filters == null || filters.size() == 0)
            return EMPTY;

        InternalStringBuilder sql = new InternalStringBuilder(64);
        internalCreateWhereFragment(sql, filters);
        return sql.toString();
    }

    /**
     * <p>
     * Create a SQL WHERE clause from the list of {@link Filter} objects.  This fragment begins with
     * WHERE.  If the given list of sorts contains a Filter with filter expression "foo", operation equals,
     * and value '42', the generated SQL statement will appear as:
     * <pre>
     *     WHERE foo = 42
     * </pre>
     * When multiple Filters in the list, the filters will be AND'ed together in the generated SQL statement.
     * </p>
     * @param filters the list of {@link Filter} objects
     * @return the generated SQL WHERE clause or an emtpy string if there are no filters
     */
    public String createWhereClause(List/*<Filter>*/ filters) {
        if(filters == null || filters.size() == 0)
            return EMPTY;

        InternalStringBuilder sql = new InternalStringBuilder();
        sql.append("WHERE ");
        internalCreateWhereFragment(sql, filters);
        return sql.toString();
    }

    /**
     *
     * @param sql
     * @param sorts
     */
    private void internalCreateOrderByFragment(InternalStringBuilder sql, List/*<Sort>*/ sorts) {
        for(int i = 0; i < sorts.size(); i++) {
            Sort sort = (Sort)sorts.get(i);
            if(i > 0)
                sql.append(", ");
            sql.append(sort.getSortExpression());
            if(sort.getDirection() == SortDirection.DESCENDING)
                sql.append(" DESC");
        }
    }

    /**
     *
     * @param sql
     * @param filters
     */
    private void internalCreateWhereFragment(InternalStringBuilder sql, List/*<Filter>*/ filters) {

        for(int i = 0; i < filters.size(); i++) {
            Filter filter = (Filter)filters.get(i);

            if(filter == null)
                continue;

            FilterOperation fOp = filter.getOperation();
            FilterOperationHint fOpHint = null;
            String fExpr = filter.getFilterExpression();
            if(fOp == null && filter.getOperationHint() != null) {
                fOpHint = filter.getOperationHint();
                fOp = mapFilterHintToOperation(fOpHint);
            }
            else {
                fOpHint = filter.getOperation().getOperationHint();
            }

            if(fOp == null) {
                String message = Bundle.getErrorString("DataGridFilter_NoFilterOperation", new Object[]{filter.getFilterExpression()});
                LOGGER.error(message);
                throw new IllegalFilterException(message);
            }

            /* todo: feature. pluggable conjunctions AND and OR here */
            if(i > 0)
                sql.append(" AND ");

            if(filter.getValue() == null) {
                if(fOpHint == FilterOperationHint.EQUAL) {
                    sql.append("(");
                    sql.append(fExpr);
                    sql.append(" IS NULL)");
                }
                else if(fOpHint == FilterOperationHint.NOT_EQUAL) {
                    sql.append("(");
                    sql.append(fExpr);
                    sql.append(" IS NOT NULL)");
                }
            }

            switch(fOpHint.getValue()) {
                case FilterOperationHint.INT_STARTS_WITH:
                case FilterOperationHint.INT_CONTAINS:
                    {
                        boolean bEscape = _config.getSupportsLikeEscapeClause();
                        String strValue = bEscape ? convertSQLPattern(filter.getValue()) : filter.getValue().toString();
                        strValue = convertSQLString(strValue);
                        sql.append("(").append(fExpr).append(" LIKE '");
                        if(fOpHint == FilterOperationHint.CONTAINS)
                            sql.append("%");
                        sql.append(strValue).append("%'");
                        if(bEscape)
                            sql.append(" ESCAPE '\\'");
                        sql.append(')');
                        break;
                    }
                case FilterOperationHint.INT_IS_NOT_EMPTY:
                    {
                        sql.append("(").append(fExpr).append(" IS NOT NULL)");
                        break;
                    }
                case FilterOperationHint.INT_IS_EMPTY:
                    {
                        sql.append("(").append(fExpr).append(" IS NULL)");
                        break;
                    }
                case FilterOperationHint.INT_EQUAL:
                case FilterOperationHint.INT_LESS_THAN:
                case FilterOperationHint.INT_LESS_THAN_OR_EQUAL:
                case FilterOperationHint.INT_GREATER_THAN:
                case FilterOperationHint.INT_GREATER_THAN_OR_EQUAL:
                case FilterOperationHint.INT_NOT_EQUAL:
                    {
                        /* todo: conider using SQLFragment from the DatabaseControl here. */
                        sql.append("(");
                        sql.append(fExpr);
                        sql.append(lookupOperator(fOpHint));
                        addParameter(sql, filter.getValue(), filter.getTypeHint());
                        if(fOpHint == FilterOperationHint.NOT_EQUAL) {
                            sql.append(" OR ");
                            sql.append(fExpr);
                            sql.append(" IS NULL");
                        }
                        sql.append(")");
                        break;
                    }
                case FilterOperationHint.INT_IS_ONE_OF:
                    {
                        Object[] arr;
                        if(filter.getValue().getClass().isArray())
                            arr = (Object[])filter.getValue();
                        else
                            arr = new Object[]{filter.getValue()};

                        if(arr.length == 0)
                            break;

                        sql.append("(");
                        sql.append(fExpr);
                        sql.append(" IN (");
                        String comma = "";
                        for(int j = 0; j < arr.length; j++) {
                            sql.append(comma);
                            /* todo: date handling. probably some type normalization required here */
                            addParameter(sql, arr[i], filter.getTypeHint());
                            comma = ",";
                        }
                        sql.append("))");
                        break;
                    }
                default:
                    throw new IllegalFilterException(Bundle.getErrorString("DataGridFilter_UnknownFilterOperation", new Object[]{fOp}));
            }
        }
    }

    /**
     *
     * @param o
     * @return
     */
    private String convertSQLPattern(Object o) {
        if(o == null)
            return EMPTY;
        else {
            String s = o.toString();
            s = s.replaceAll("\\\\", "\\\\\\\\");
            s = s.replaceAll("%", "\\\\%");
            s = s.replaceAll("_", "\\\\_");
            return s;
        }
    }

    /**
     *
     * @param o
     * @return
     */
    private String convertSQLString(Object o) {
        if(o == null)
            return EMPTY;
        else
            return (o.toString()).replaceAll("'", "''");
    }

    /**
     *
     * @param sql
     * @param value
     * @param typeHint
     */
    private void addParameter(InternalStringBuilder sql, Object value, FilterTypeHint typeHint) {
        if(typeHint == FilterTypeHint.STRING)
            sql.append(_config.getQuoteChar()).append(value).append(_config.getQuoteChar());
        else
            sql.append(value);
    }

    /**
     *
     * @param op
     * @return
     */
    private String lookupOperator(FilterOperationHint op) {
        switch(op.getValue()) {
            case FilterOperationHint.INT_EQUAL:
                return "=";
            case FilterOperationHint.INT_NOT_EQUAL:
                return "!=";
            case FilterOperationHint.INT_GREATER_THAN:
                return ">";
            case FilterOperationHint.INT_LESS_THAN:
                return "<";
            case FilterOperationHint.INT_GREATER_THAN_OR_EQUAL:
                return ">=";
            case FilterOperationHint.INT_LESS_THAN_OR_EQUAL:
                return "<=";
            default:
                assert false : "lookupOperation received an invalid FilterOperation: " + op;
        }
        return null;
    }
}
