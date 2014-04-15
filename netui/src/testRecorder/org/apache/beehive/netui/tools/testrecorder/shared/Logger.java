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

package org.apache.beehive.netui.tools.testrecorder.shared;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;

public class Logger
    implements Log {

    public static Logger getInstance(Class loggerClient) {
        return new Logger(org.apache.commons.logging.LogFactory.getLog(loggerClient.getName()));
    }

    private Log _logDelegate = null;

    public Logger(Log logDelegate) {
        _logDelegate = logDelegate;
    }

    public boolean isDebugEnabled() {
        return _logDelegate.isDebugEnabled();
    }

    public boolean isErrorEnabled() {
        return _logDelegate.isErrorEnabled();
    }

    public boolean isFatalEnabled() {
        return _logDelegate.isFatalEnabled();
    }

    public boolean isInfoEnabled() {
        return _logDelegate.isInfoEnabled();
    }

    public boolean isTraceEnabled() {
        return _logDelegate.isTraceEnabled();
    }

    public boolean isWarnEnabled() {
        return _logDelegate.isWarnEnabled();
    }

    public void debug( Object message ) {
        if(isDebugEnabled())
            _logDelegate.debug(message);
    }

    public void debug(Object message, Throwable t) {
        if(isDebugEnabled())
            _logDelegate.debug(format(message, t));
    }

    public void trace(Object message) {
        if(isTraceEnabled())
            _logDelegate.trace(message);
    }

    public void trace(Object message, Throwable t) {
        if(isTraceEnabled())
            _logDelegate.trace(format(message, t));
    }

    public void info(Object message) {
        if(isInfoEnabled())
            _logDelegate.info(message);
    }

    public void info(Object message, Throwable t) {
        if(isInfoEnabled())
            _logDelegate.info(format(message, t));
    }

    public void warn(Object message) {
        if(isWarnEnabled())
            _logDelegate.warn(message);
    }

    public void warn(Object message, Throwable t) {
        if(isWarnEnabled())
            _logDelegate.warn(format(message, t));
    }

    public void error(Object message) {
        if(isErrorEnabled())
            _logDelegate.error(message);
    }

    public void error(Object message, Throwable t) {
        if(isErrorEnabled())
            _logDelegate.error(format(message, t));
    }

    public void fatal(Object message) {
        if(isFatalEnabled())
            _logDelegate.fatal(message);
    }

    public void fatal(Object message, Throwable t) {
        if(isFatalEnabled())
            _logDelegate.fatal(format(message, t));
    }

    public static String format(Object m, Throwable t) {
        if(t == null)
            return m.toString();

        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));

        /* note, no reason to close a StringWriter */

        return m + "\n\n" + "Throwable: " + t.toString() + "\nStack Trace:\n" + sw.toString();
    }
}
