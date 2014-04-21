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
package org.apache.beehive.netui.databinding.datagrid.runtime.config;

import org.apache.beehive.netui.databinding.datagrid.api.sort.SortDirection;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortStrategy;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * Default implementation of a {@link SortStrategy}.  This class is used by the default data grid config
 * object to provide a very simple state machine for cycling through sort directions as they
 * are changed via URLs from a JSP.
 * </p>
 */
class DefaultSortStrategy
    extends SortStrategy {

    /**
     * Package protected constructor.
     */
    DefaultSortStrategy() {
    }

    /**
     * Get the default sort direction -- {@link SortDirection#ASCENDING}
     * @return the default sort direction
     */
    public SortDirection getDefaultDirection() {
        return SortDirection.ASCENDING;
    }

    /**
     * <p>
     * Given a sort direction, get the next sort direction.  This implements a simple sort machine
     * that cycles through the sort directions in the following order:
     * <pre>
     *     SortDirection.NONE > SortDirection.ASCENDING > SortDirection.DESCENDING > repeat
     * </pre>
     * </p>
     * @param direction the current {@link SortDirection}
     * @return the next {@link SortDirection}
     */
    public SortDirection nextDirection(SortDirection direction) {
        if(direction == SortDirection.NONE)
            return SortDirection.ASCENDING;
        else if(direction == SortDirection.ASCENDING)
            return SortDirection.DESCENDING;
        else if(direction == SortDirection.DESCENDING)
            return SortDirection.NONE;
        else throw new IllegalStateException(Bundle.getErrorString("SortStrategy_InvalidSortDirection", new Object[]{direction}));
    }
}
