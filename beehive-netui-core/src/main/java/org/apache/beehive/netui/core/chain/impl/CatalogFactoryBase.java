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
package org.apache.beehive.netui.core.chain.impl;

import java.util.HashMap;

import org.apache.beehive.netui.core.chain.CatalogFactory;
import org.apache.beehive.netui.core.chain.Catalog;

/**
 * <p>
 * This is a simple {@link CatalogFactory} implementation that supports a two step lifecycle
 * where {@link Catalog}s are added and can then be retrieved.  Once reading of catalogs starts,
 * no more catalogs can be added.  This avoids an unnecessary synchronization point
 * for every catalog access and makes Catalog lookups fast.
 * </p>
 */
public class CatalogFactoryBase
    extends CatalogFactory {

    private boolean _locked = false;
    private Catalog _defaultCatalog;
    private HashMap _catalogs = new HashMap();

    public void setCatalog(Catalog catalog) {
        _defaultCatalog = catalog;
    }

    public Catalog getCatalog() {
        return _defaultCatalog;
    }

    public void addCatalog(String name, Catalog catalog) {
        if(_locked)
            throw new IllegalStateException();

        synchronized(_catalogs) {
            _catalogs.put(name, catalog);
        }
    }

    public Catalog getCatalog(String name) {
        _locked = true;
        return (Catalog)_catalogs.get(name);
    }
}
