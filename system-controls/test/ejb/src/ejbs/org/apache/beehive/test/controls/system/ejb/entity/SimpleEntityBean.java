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
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Simple entity bean for testing, uses BMP pattern although doesn't ever persist any data.
 */
public class SimpleEntityBean implements EntityBean
{
    private String _name;
    private int _ref;

    public String getName() {
       return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setRef(int ref) {
        _ref = ref;
    }
    public int getRef() {
        return _ref;
    }

    private EntityContext _context = null;


    public SimpleEntityBean() { }

    public String sayHello() {
        return "Hello!";
    }

    public String echo(String echoString) {
        return echoString;
    }

    public void ejbActivate() {
        System.err.println("SimpleEntityBean activated.");
    }

    public void ejbPassivate() {
        System.err.println("SimpleEntityBean passivated.");
    }

    public void ejbLoad() {
        System.err.println("SimpleEntityBean loaded.");
    }

    public void ejbStore() {
        System.err.println("SimpleEntityBean stored.");
    }

    public void setEntityContext(EntityContext context) {
        _context = context;
        System.err.println("SimpleEntityBean context set.");
    }

    public void unsetEntityContext() {
        _context = null;
        System.err.println("SimpleEntityBean context unset.");
    }

    public String ejbCreate(String id) {
        System.err.println("SimpleEntityBean created with id="+id);
        setName(id);
        setRef(66);
        return id;
    }

    public void ejbPostCreate(String id) {
        System.err.println("SimpleEntityBean post-created with id=."+id);
    }

    public void ejbRemove() throws EJBException, RemoteException {
        System.err.println("SimpleEntityBean removed.");
    }

    public String ejbFindByPrimaryKey(String key) throws FinderException {
        // hard coded for test
        return "entBean1";
    }

    public Collection ejbFindByEntryValue(String key, String value) {
       // hard coded for test
        LinkedList<String> list = new LinkedList<String>();
        list.add("entBean1");
        list.add("entBean2");
        list.add("entBean3");
        return list;
    }

}
