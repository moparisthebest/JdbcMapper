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
package org.apache.beehive.netui.pageflow;

import java.io.Serializable;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;

import org.apache.beehive.netui.util.internal.ServletUtils;
import org.apache.beehive.netui.pageflow.requeststate.NameService;

/**
 * <p>
 * Class that implements an {@link HttpSessionListener} that adds mutex objects to the
 * {@link javax.servlet.http.HttpSession}.  This listener adds a session scoped attribute
 * to the key {@link org.apache.beehive.netui.util.internal.ServletUtils#SESSION_MUTEX_ATTRIBUTE}
 * that can be used as a safe reference to lock access to the session. In addition
 * it adds a session scoped attribute that the {@link NameService} uses as a safe
 * object to lock on for synchronizing lazy initialization of the session scoped
 * NameService object.
 * </p>
 * <p>
 * To use this listener, it needs to be registered in <code>web.xml</code>.
 * </p>
 */
public final class HttpSessionMutexListener
    implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().setAttribute(ServletUtils.SESSION_MUTEX_ATTRIBUTE, new Mutex());
        httpSessionEvent.getSession().setAttribute(NameService.NAME_SERVICE_MUTEX_ATTRIBUTE, new NameServiceMutex());
    }

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().removeAttribute(ServletUtils.SESSION_MUTEX_ATTRIBUTE);
        httpSessionEvent.getSession().removeAttribute(NameService.NAME_SERVICE_MUTEX_ATTRIBUTE);
    }

    /**
     * An internal class that is instantiated per HttpSession to act as an object to use
     * when locking an HttpSession.
     */
    private static final class Mutex implements Serializable {
    }

    /**
     * An internal class that is instantiated per HttpSession to act as an object
     * to use when synchronizing lazy initialization of the session scoped
     * NameService object.
     */
    private static final class NameServiceMutex implements Serializable {
    }
}
