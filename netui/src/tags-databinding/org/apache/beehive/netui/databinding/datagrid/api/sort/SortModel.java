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
package org.apache.beehive.netui.databinding.datagrid.api.sort;

import java.util.List;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortDirection;

/**
 * <p>
 * The SortModel class groups a set of {@link Sort} objects.  This class also provides a set of methods
 * for interacting with a group of {@link Sort} objects.
 * </p>
 * <p>
 * The list of {@link Sort} objects in a SortModel are ordered.  The first sort in group is known as the
 * "primary" sort and subsequent Sorts are "secondary".  The interpretation of using Sorts into
 * to order a data set is left to implementations of sort algoritms.  For example, one possible
 * sort implementation could order the sorts such that the secondary sort occurs inside the
 * data ordered by the primary sort and so on.
 * </p>
 * <p>
 * In addition to accessing the Sort objects, the SortModel provides a the ability to plug a simple state
 * machine that controls how to change a sort direction when cycling through the set of sort directions.
 * For example, when using a data grid to sort columns of data, a column may start off unsorted,
 * change to {@link SortDirection#ASCENDING}, to {@link SortDirection#DESCENDING}, and finally back to
 * {@link SortDirection#NONE}.  The {@link SortStrategy} allows this strategy to be changed so that the
 * sorts can change from {@link SortDirection#NONE} to {@link SortDirection#DESCENDING}, to
 * {@link SortDirection#ASCENDING}, and finally back to {@link SortDirection#NONE}.
 * </p>
 */
public class SortModel
    implements java.io.Serializable {

    /* todo: consider exposing a Map getSortMap() that would be JSP 2.0 EL bindable */
    /* todo: expose a convenience method to change the direction of a sort */

    private SortStrategy _sortStrategy = null;
    private List/*<Sort>*/ _sorts = null;

    /**
     * Construct the SortModel with a {@link List} of sorts.
     * @param sorts the sorts
     */
    public SortModel(List/*<Sort>*/ sorts) {
        _sorts = sorts;
    }

    /**
     * Get the {@link SortStrategy} used to cycle these {@link Sort} objects through various
     * {@link SortDirection}s.
     * @return the {@link SortStrategy}
     */
    public SortStrategy getSortStrategy() {
        return _sortStrategy;
    }

    /**
     * Set the {@link SortStrategy}.
     * @param sortStrategy the new {@link SortStrategy}
     */
    public void setSortStrategy(SortStrategy sortStrategy) {
        _sortStrategy = sortStrategy;
    }

    public List/*<Sort>*/ getSorts() {
        if(_sorts == null)
            return null;
        else
            return _sorts;
    }

    /**
     * Check to see if a sort expression is the first {@link Sort} in the {@link SortModel}.  If the
     * first {@link Sort} in the SortModel has a {@link Sort#getSortExpression()} that matches the
     * <code>sortExpression</code> parameter, the method returns <code>true</code>.  Otherwise, it
     * returns <code>false</code>.
     *
     * @param sortExpression the sort expression to use when checking the SortModel's first {@link Sort}
     * @return <code>true</code> if the first {@link Sort} has a sortExpression that matches the
     * sortExpression parameter.  <code>false</code> otherwise.
     */
    public boolean isPrimarySort(String sortExpression) {
        if(sortExpression == null)
            return false;

        /* optimizing for the case where the sortExpression *is* the primary sort */
        if(_sorts != null &&
           _sorts.size() > 0 &&
           ((Sort)_sorts.get(0)).getSortExpression().equals(sortExpression)) {
            return true;
        }
        else
            return false;
    }

    /**
     * Check to see if the SortModel contains a {@link Sort} whose sort expression matches the given
     * <code>sortExpression</code>.
     *
     * @param sortExpression the sortExpression used to locate a {@link Sort}
     * @return <code>true</code> if a {@link Sort} is found whose {@link Sort#getSortExpression()} matches
     * the given <code>sortExpression</code>.  <code>false</code> otherwise.
     */
    public boolean isSorted(String sortExpression) {
        if(sortExpression == null)
            return false;

        Sort term = findSort(sortExpression);
        if(term == null || term.getDirection() == SortDirection.NONE)
            return false;
        else return true;
    }

    /**
     * Get the {@link SortDirection} for a {@link Sort} given a sort expression.
     *
     * @param sortExpression the sort expression used to locate a {@link Sort} object.
     * @return a {@link Sort}'s {@link SortDirection} if one is found whose <code>sortExpression</code>
     * property matches the given <code>sortExpression</code>.  <code>null</code> otherwise. 
     */
    public SortDirection getSortDirection(String sortExpression) {
        Sort term = findSort(sortExpression);
        return term == null ? SortDirection.NONE : term.getDirection();
    }

    /**
     * Lookup a {@link Sort} object whose {@link Sort#getSortExpression()} matches
     * the given <code>sortExpression</code>.
     *
     * @param sortExpression the sortExpression used to locate a {@link Sort}
     * @return a {@link Sort} if one is found whose <code>sortExpression</code> property matches
     * the given <code>sortExpression</code>.  <code>null</code> otherwise.
     */
    public Sort lookupSort(String sortExpression) {
        return findSort(sortExpression);
    }

    private final Sort findSort(String sortExpression) {
        if(_sorts == null)
            return null;

        for(int i = 0; i < _sorts.size(); i++) {
            assert _sorts.get(i) instanceof Sort;
            Sort sort = (Sort)_sorts.get(i);
            if(sort.getSortExpression().equals(sortExpression))
                return sort;
        }

        return null;
    }
}
