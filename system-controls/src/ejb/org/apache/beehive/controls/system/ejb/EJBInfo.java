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
package org.apache.beehive.controls.system.ejb;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import org.apache.beehive.controls.api.ControlException;

/**
 * The EJBInfo class is a support class that derives EJB information by
 * reflecting on an EJB control interface.  This is implemented by a
 * static inner class to make this functionality accesible in both static
 * and non-static contexts
 */
public class EJBInfo {

    /**
     * @deprecated Use the JavaBean getter / setter instead
     */
    public String _refName;

    /**
     * @deprecated Use the JavaBean getter / setter instead
     */
    public Class _homeInterface;

    /**
     * @deprecated Use the JavaBean getter / setter instead
     */
    public Class _beanInterface;

    /**
     * @deprecated Use the JavaBean getter / setter instead
     */
    public String _beanType;

    /**
     * @deprecated Use the JavaBean getter / setter instead
     */
    public boolean _isLocal;

    /**
     * Derives bean attributes from the control interface
     */
    public EJBInfo(Class controlInterface) {
        _refName = getEJBRefName(controlInterface);

        Class localHome = null;
        Class localBean = null;
        Class remoteHome = null;
        Class remoteBean = null;

        //
        // To identify the identify home and bean interfaces, we
        // must reflect the interface hierarchy of the provided
        // class.
        //
        Vector checkList = new Vector();
        Class [] subintfs = controlInterface.getInterfaces();
        for (int i = 0; i < subintfs.length; i++)
            checkList.add(subintfs[i]);

        HashMap derivesFrom = new HashMap();
        for (int i = 0; i < checkList.size(); i++) {
            Class intf = (Class) checkList.elementAt(i);

            if (javax.ejb.EJBHome.class.isAssignableFrom(intf))
                remoteHome = intf;
            else if (javax.ejb.EJBLocalHome.class.isAssignableFrom(intf))
                localHome = intf;
            else if (javax.ejb.EJBObject.class.isAssignableFrom(intf))
                remoteBean = intf;
            else if (javax.ejb.EJBLocalObject.class.isAssignableFrom(intf))
                localBean = intf;
            else {
                //
                // If none of the above, add any new subinterfaces to
                // the search list.
                //
                subintfs = intf.getInterfaces();
                for (int j = 0; j < subintfs.length; j++) {
                    if (!checkList.contains(subintfs[j])) {
                        checkList.add(subintfs[j]);
                        derivesFrom.put(subintfs[j], intf);
                    }
                }
            }
        }

        //
        // From the located methods, identify the home/bean interfaces.
        //
        if (remoteHome != null) {
            if (localHome != null)
                throw new ControlException(controlInterface + " extends multiple EJB home interfaces.");
            _homeInterface = getRoot(remoteHome, derivesFrom);
        }
        else if (localHome != null)
            _homeInterface = getRoot(localHome, derivesFrom);
        else throw new ControlException(controlInterface + " does not extend the EJBHome or EJBLocalHome interfaces");

        if (remoteBean != null) {
            if (localBean != null)
                throw new ControlException("Interface " + controlInterface + " extends multiple EJB object interfaces.");
            _beanInterface = getRoot(remoteBean, derivesFrom);
        }
        else if (localBean != null)
            _beanInterface = getRoot(localBean, derivesFrom);
        else throw new ControlException("Interface " + controlInterface + " does not extend the EJBObject or EJBLocalObject interfaces");

        // Identify the bean type via bean interface reflection
        _beanType = "Session";
        Method [] homeMethods = _homeInterface.getMethods();
        for (int i = 0; i < homeMethods.length; i++) {
            if (isFinderMethod(homeMethods[i])) {
                _beanType = "Entity";
                break;
            }
        }

        _isLocal = (EJBLocalHome.class.isAssignableFrom(_homeInterface));
    }

    /**
     * Unwinds the results of reflecting through the interface inheritance
     * hierachy to find the original root class from a derived class
     */
    public Class getRoot(Class clazz, HashMap derivesFrom) {
        while (derivesFrom.containsKey(clazz)) clazz = (Class) derivesFrom.get(clazz);
        return clazz;
    }

    /**
     * Computes a unique local ejb ref name based upon the JCX class name
     */
    public static String getEJBRefName(Class jcxClass) {
        return jcxClass.getName() + ".jcx";
    }

    public String getRefName() {
        return _refName;
    }

    public void setRefName(String refName) {
        _refName = refName;
    }

    public Class getHomeInterface() {
        return _homeInterface;
    }

    public void setHomeInterface(Class homeInterface) {
        _homeInterface = homeInterface;
    }

    public Class getBeanInterface() {
        return _beanInterface;
    }

    public void setBeanInterface(Class beanInterface) {
        _beanInterface = beanInterface;
    }

    public String getBeanType() {
        return _beanType;
    }

    public void setBeanType(String beanType) {
        _beanType = beanType;
    }

    public boolean isLocal() {
        return _isLocal;
    }

    public void setLocal(boolean local) {
        _isLocal = local;
    }

    public String toString() {
        return
            "{refname=" + _refName +
                " home=" + _homeInterface.getName() +
                " remote=" + _beanInterface.getName() +
                " type=" + _beanType +
                " local=" + _isLocal + "}";
    }

    protected static boolean isFinderMethod(Method m) {
        if (!m.getName().startsWith("find")) // EJB enforced pattern
            return false;
        return methodThrows(m, FinderException.class);
    }

    protected static boolean methodThrows(Method m, Class exceptionClass) {
        Class [] exceptions = m.getExceptionTypes();
        for (int j = 0; j < exceptions.length; j++)
            if (exceptionClass.isAssignableFrom(exceptions[j]))
                return true;
        return false;
    }
}

