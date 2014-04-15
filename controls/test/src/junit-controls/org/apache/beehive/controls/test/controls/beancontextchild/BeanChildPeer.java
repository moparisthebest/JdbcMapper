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

package org.apache.beehive.controls.test.controls.beancontextchild;

import java.beans.beancontext.BeanContextChild;
import java.beans.beancontext.BeanContext;
import java.beans.PropertyVetoException;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: cschoett
 * Date: Apr 13, 2006
 * Time: 9:20:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class BeanChildPeer implements BeanContextChild, Serializable {

    private String _message;

    public String getMessage() { return _message; }
    public void setMessage(String message) { _message = message; }

    public void setBeanContext(BeanContext bc) throws PropertyVetoException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public BeanContext getBeanContext() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addPropertyChangeListener(String name, PropertyChangeListener pcl) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removePropertyChangeListener(String name, PropertyChangeListener pcl) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addVetoableChangeListener(String name, VetoableChangeListener vcl) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeVetoableChangeListener(String name, VetoableChangeListener vcl) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
