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

package org.apache.beehive.test.controls.system.ejb.entity;

import javax.ejb.EJBException;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;

/**
 * Simple entity bean for testing control assembly.
 */
public class EntityBean implements javax.ejb.EntityBean {
    public EntityBean() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbLoad() {
    }

    public void ejbStore() {
    }

    public void setEntityContext(EntityContext context) {
    }

    public void unsetEntityContext() {
    }

    public String ejbCreate(String id) {
        return "foo";
    }

    public void ejbPostCreate(String id) {
    }

    public void ejbRemove() throws EJBException, RemoteException {
    }

    public String ejbFindByPrimaryKey(String key) throws FinderException {
        return "entBean1";
    }

    public Collection ejbFindByEntryValue(String key, String value) {
        return Collections.EMPTY_LIST;
    }
}
