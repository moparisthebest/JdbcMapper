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

import java.util.Calendar;
import java.text.ParseException;

import org.apache.beehive.netui.tools.testrecorder.shared.util.DateHelper;

/**
 *
 */
public class SessionBean {

    protected String _sessionName;
    private String _tester;
    private Calendar _startDate;
    private Calendar _endDate;
    private String _description;

    public String getSessionName() {
        return _sessionName;
    }

    public String getTester() {
        return _tester;
    }

    public void setTester( String tester ) {
        _tester = tester;
    }

    protected Calendar getStartDate() {
        return _startDate;
    }

    public void setStartDate( Calendar startDate ) {
        _startDate = startDate;
    }

    public void setStartDate(String date) 
        throws ParseException {
        _startDate = DateHelper.getCalendarInstance(date);
    }

    public String getStartDateString() {
        if ( getStartDate() == null ) {
            return null;
        }
        return DateHelper.formatToString(getStartDate());
    }

    protected Calendar getEndDate() {
        return _endDate;
    }

    public void setEndDate( Calendar endDate ) {
        _endDate = endDate;
    }

    public void setEndDate( String date ) throws ParseException {
        _endDate = DateHelper.getCalendarInstance(date);
    }

    public String getEndDateString() {
        if ( getEndDate() == null ) {
            return null;
        }
        else return DateHelper.formatToString(getEndDate());
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription( String description ) {
        _description = description;
    }

}
