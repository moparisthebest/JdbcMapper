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
package org.apache.beehive.controls.system.jndi.impl;

import java.util.Hashtable;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.system.jndi.JndiControl;

/**
 * Implementation of the jndi control.
 */
@ControlImplementation
public class JndiControlImpl
    implements JndiControl, java.io.Serializable {

    /**
     * @see JndiControl#getResource(String, Class)
     */
    public Object getResource(String resource, Class resourceClass) throws ControlException {

        try {
            InitialContext cntxt = getInitialContext();
            Object obj = cntxt.lookup(resource);
            if (resourceClass != null && !(resourceClass.isInstance(obj)))
                throw new ControlException("JNDI resource '" + resource + "' is not an instance of '" + resourceClass.getName() + "'");
            else return obj;
        }
        catch (NamingException e) {
            throw new ControlException("Cannot load JNDI resource '" + resource + "'", e);
        }
    }

    /**
     * Get the initial context.
     *
     * @return the initial context.
     */
    public InitialContext getInitialContext() throws ControlException {
        if (_initialContext != null) {
            return _initialContext;
        }
        Properties props = (Properties) _context.getControlPropertySet(Properties.class);
        String url = nullIfEmpty(props.url());
        String factory = nullIfEmpty(props.factory());
        if (url == null && factory == null) {
            try {
                return new InitialContext();
            }
            catch (NamingException e) {
                throw new ControlException("Cannot get default JNDI initial context", e);
            }

        }
        if (url == null || factory == null) {
            throw new ControlException("Both the provider-url and jndi factory need to be provided");

        }
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, factory);
        env.put(javax.naming.Context.PROVIDER_URL, url);

        String username = nullIfEmpty(props.jndiSecurityPrincipal());
        if (username != null)
            env.put(javax.naming.Context.SECURITY_PRINCIPAL, username);

        String password = nullIfEmpty(props.jndiSecurityCredentials());
        if (password != null)
            env.put(javax.naming.Context.SECURITY_CREDENTIALS, password);

        try {
            return _initialContext = new InitialContext(env);
        }
        catch (NamingException e) {
            throw new ControlException("Cannot get JNDI initial context at provider '" + url + "' with factory '" + factory + "'", e);
        }
    }

    /**
     * Return null if the given string is null or an empty string.
     *
     * @param str a string.
     * @return null or the string.
     */
    protected String nullIfEmpty(String str) {
        return (str == null || str.trim().length() == 0) ? null : str;
    }

    @Context
    ControlBeanContext _context;

    /**
     * The initial context.
     */
    private transient InitialContext _initialContext;
}
