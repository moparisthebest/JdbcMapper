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

/**
 * <p>
 * A SortStrategy is an implementation for the state machine which is used to cycle through sort directions.
 * </p>
 */
public abstract class SortStrategy
    implements java.io.Serializable {

    /**
     * Get the default {@link SortDirection}.
     * @return the default {@link SortDirection}
     */
    public abstract SortDirection getDefaultDirection();

    /**
     * Given a <code>direction</code> compute the next {@link SortDirection}.  Implementations
     * are free to use arbitrary logic to compute the next direction.
     * @param direction the current sort direction
     * @return the next sort direction
     */
    public abstract SortDirection nextDirection(SortDirection direction);
}
