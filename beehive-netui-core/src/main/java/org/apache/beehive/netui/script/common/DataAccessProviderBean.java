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
package org.apache.beehive.netui.script.common;

/**
 * JavaBean that provides a delegate wrapper for an implementation of the {@link IDataAccessProvider} interface.
 */
public class DataAccessProviderBean {

    private IDataAccessProvider _provider = null;

    public DataAccessProviderBean(IDataAccessProvider provider) {
        _provider = provider;
    }

    public Object getItem() {
        assert _provider != null;
        return _provider.getCurrentItem();
    }

    public Object getContainer() {
        assert _provider != null;
        return new DataAccessProviderBean(_provider.getProviderParent());
    }

    public int getIndex() {
        assert _provider != null;
        return _provider.getCurrentIndex();
    }

    public Object getMetadata() {
        assert _provider != null;
        return _provider.getCurrentMetadata();
    }
}


