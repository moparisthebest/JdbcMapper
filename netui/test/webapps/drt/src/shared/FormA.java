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

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * FormA
 *
 * This class used to maintain a static counter for each instance of this class
 * so that tests could destinguish different classes.  This functionality was
 * moved to the QaTrace class.  There are some legacy things left here to keep
 * older tests from breaking.
 */
public class FormA implements Serializable
    {
    public  static  final   String  STR_VAL1    = "String 1 value";
    public  static  final   String  STR_VAL2    = "String 2 value";
    private                 String  _str1       = STR_VAL1;
    private                 String  _str2       = STR_VAL2;
    private                 int     _int1       = 1;
    private                 int     _int2       = 2;

    private QaTrace _log = null;
    private int     _cnt = 0;

    /**
     * Constructor
     *
     * Keep this constructor else the older tests will break.  The constructor that
     * takes an HttpSession is the one that should be called.
     */
    public FormA()
        {
        super();
        //System.out.println(">>> FormA() - No QaTrace.");
        return;
        }

    /**
     * Constructor
     */
    public FormA(HttpSession session) throws Exception
        {
        super();
        _log = QaTrace.getTrace(session);
        _cnt = _log.newClass(this);
        //_log.tracePoint("FormA.(HttpSession):" + _cnt);
        return;
        }

    // String1 getter/setter
    //---------------------------------------------------------------------------
    public void setString1(String inVal)
        { _str1 = inVal; }
    public String getString1()
        { return _str1; }

    // String2 getter/setter
    //---------------------------------------------------------------------------
    public void setString2(String inVal)
        { _str2 = inVal; }
    public String getString2()
        { return _str2; }

    // Int1 getter/setter
    //---------------------------------------------------------------------------
    public void setInt1(int inVal)
        { _int1 = inVal; }
    public int getInt1()
        { return _int1; }

    // Int2 getter/setter
    //---------------------------------------------------------------------------
    public void setInt2(int inVal)
        { _int2 = inVal; }
    public int getInt2()
        { return _int2; }

    // Counter getter
    //---------------------------------------------------------------------------
    public String getCounter()
        {
        Integer cntI = new Integer(_cnt);
        String cntS = cntI.toString();
        return cntS; }

    /**
     * Reset the class counter
     * Keep for the older tests.
     */
    public static void resetCounter()
        {
        return;
        }
    }

