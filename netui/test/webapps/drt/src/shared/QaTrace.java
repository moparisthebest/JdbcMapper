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
package shared;

import java.util.Vector;

import java.util.Hashtable;
import java.io.Serializable;

import javax.servlet.http.HttpSession;

/**
 * This QaTrace class is sort of a singleton.  I say sort of because there can be more
 * then one instance at a time but only one instance per session.
 *
 * The Class serves two purposes:
 * 1) It store String trace messages that tests want to log.  Each trace message is
 *    written to System.out which should be the console and then it is save in a
 *    Vector so that the whole sequence of messages can be retrieved at any point.
 * 2) It also is used to count the number of instances of a class.  There are times
 *    during testing when it is handly to know if two objects are the same or different
 *    instances.  Doing a toString() on the class works but is not reproducable from
 *    test run to test run.  So in the constructor of a class it can call to obtain
 *    an integer value that uniquely identifies itself.
 */
public class QaTrace implements Serializable
    {
    // Static
    public static final String QA_TRACE = "QaTrace";
    private static Object _lockClass = new Object();

    // Instance
    private Object _lockTrace = new Object();
    private Vector _traceVec = new Vector();

    private Object _lockInstance = new Object();
    private Hashtable _instanceTable = new Hashtable();

    private boolean _logToConsole = false;

    /**
     * Constructor
     * Since QaTrace is basically a singleton (only one per session) the constructor
     * is private.  You retrieve the one instance by calling the static method
     * getTrace().
     */
    private QaTrace()
        {
        super();
        }

    /**
     * Method which accepts the session as a parameter and returns the one instance
     * of the QaTrace object for the session.  Logging to the console will be
     * uneffected.  If it's on it will remain on, if off it will remain off.
     *
     * @param session the current session.
     * @return The one instance of QaTrace.
     */
    public static QaTrace getTrace(HttpSession session) throws Exception
        {
        QaTrace trace = getIt(session);
        return trace;
        }

    /**
     * Method which accepts the session and boolean as parameters and returns
     * the one instance of the QaTrace object for the session and turns on or
     * off console logging based on the value of the boolean.
     *
     * @param session the current session.
     * @param logToConsole <code>true</code> will console logging on.
     *        <code>false</code> will turn console logging off.
     * @return The one instance of QaTrace.
     */
    public static QaTrace getTrace(HttpSession session, boolean logToConsole) throws Exception
        {
        QaTrace trace = getIt(session);
        trace._logToConsole = logToConsole;
        return trace;
        }

    /*
     * Method which accpets the session and returns the one instance of the
     * QaTrace object for the session.  If an instance of QaTrace is in the session
     * it is simply returned.  If no instance exists, one is instanciated and
     * placed in the session then returned.
     */
    private static QaTrace getIt(HttpSession session) throws Exception
        {
        if (session == null)
            {
            throw new Exception("The session parameter can not be null!");
            }
        synchronized (_lockClass)
            {
            QaTrace trace = (QaTrace) session.getAttribute(QA_TRACE);
            if (trace == null)
                {
                trace = new QaTrace();
                session.setAttribute(QA_TRACE, trace);
                }
            return trace;
            }
        }

    /**
     * Method accepts a message string and logs it as a trace point.  Side effects:
     * The trace vector is locked to log the message then released and if console
     * logging is on the message will be written to System.out.
     *
     * @param traceMessage the message that is to be logged.
     */
    public void tracePoint(String traceMessage)
        {
        synchronized (_lockTrace)
            {
            String tmpStr = ">>> " + traceMessage;
            if (_logToConsole == true) System.out.println(tmpStr);
            _traceVec.add(tmpStr);
            }
        return;
        }

    /**
     * Method returns the trace point vector.
     *
     * @return The trace point vector.
     */
    public Vector getTracePoints()
        {
        synchronized (_lockTrace)
            {
            return _traceVec;
            }
        }

    /**
     * Method that will clear all the trace point in the trace point vector.
     */
    public void clearTracePoints()
        {
        synchronized (_lockTrace)
            {
            _traceVec.clear();
            }
        return;
        }

    /**
     * Method accepts an Object and increments a counter that is unique for
     * the Object type.  This counter can be used to uniquely identify an instance
     * of a Class.  Using the Objects reference is not reproducable for testing
     * purposes as the Object reference will change from test run to test run.  This
     * count is reproducable from run to run.
     *
     * @param caller the calling object.
     * @return <code>int</code> the value of the counter for this Object type.
     */
    public int newClass(Object caller)
        {
        int cnt;
        synchronized (_lockInstance)
            {
            String na = caller.getClass().getName();
            Integer cntI = (Integer) _instanceTable.get(na);
            if (cntI == null) cntI = new Integer(0);
            cnt = cntI.intValue();
            cnt++;
            _instanceTable.put(na, new Integer(cnt));
            }
        return cnt;
        }

    /**
     * Method accepts an Object and returns the counter value for the Object type.
     *
     * @param caller the calling object.
     * @return <code>int</code> the value of the counter for this Object type.
     */
    public int getClassCnter(Object caller)
        {
        synchronized (_lockInstance)
            {
            String na = caller.getClass().getName();
            Integer cnt = (Integer) _instanceTable.get(na);
            if (cnt == null) cnt = new Integer(0);
            int icnt = cnt.intValue();
            return icnt;
            }
        }
    }
