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

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 */
public class RecordSessionBean
    extends SessionBean {

    private List _requestData;
    private List _responseData;

    // if true indicates an error has occured in the session
    protected boolean _error = false;

    public RecordSessionBean(String sessionName) {
        _sessionName = sessionName;
        _requestData = new ArrayList();
        _responseData = new ArrayList();
    }

    public void setError(boolean error) {
        _error = error;
    }

    public boolean isError() {
        return _error;
    }

    public List getRequestData() {
        return Collections.unmodifiableList(_requestData);
    }

    /**
     * @param index
     * @return
     * @throws IndexOutOfBoundsException
     */
    public RequestData getRequestData(int index) {
        return (RequestData) _requestData.get(index);
    }

    public List getResponseData() {
        return Collections.unmodifiableList(_responseData);
    }

    /**
     * @param index
     * @return
     * @throws IndexOutOfBoundsException
     */
    public ResponseData getResponseData(int index) {
        return (ResponseData) _responseData.get(index);
    }

    public void addRequestResponseData(RequestData request, ResponseData response) {
        _requestData.add(request);
        _responseData.add(response);
    }

    /**
     * update the data for the most recent test
     *
     * @param request
     * @param response
     */
    public void updateRequestResponseData(RequestData request, ResponseData response) {
        if (_requestData.size() == 0 || _responseData.size() == 0)
            throw new IllegalStateException("data size is zero, unable to update");

        _requestData.set(_requestData.size() - 1, request);
        _responseData.add(_responseData.size() - 1, response);
    }

    public int getTestCount() {
        return _requestData.size();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(256);
        sb.append("[ ");
        sb.append("sessionName( " + getSessionName() + " )");
        sb.append(", tester( " + getTester() + " )");
        sb.append(", startDate( " + getStartDateString() + " )");
        sb.append(", endDate( " + getEndDateString() + " )");
        sb.append(", testCount( " + getTestCount() + " )");
        sb.append(", description( " + getDescription() + " )");
        sb.append(" ]");
        return sb.toString();
    }

}
