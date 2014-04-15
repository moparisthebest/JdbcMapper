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
package singletonJpf.jpfTest6.subJpf1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import shared.QaTrace;

/**
 * @jpf:controller nested="true"
 */
@Jpf.Controller(
    nested = true)
public class SubJpf1 extends PageFlowController
    {
    private QaTrace _log = null;
    private int     _cnt = 0;

    /**
     * onCreate
     */
    public void onCreate() throws Exception
        {
        _log = QaTrace.getTrace(getSession());
        _cnt = _log.newClass(this);
        _log.tracePoint("SubJpf1.onCreate:" + _cnt);
        }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="SubJsp1a.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "SubJsp1a.jsp") 
        })
    protected Forward begin()
        {
        _log.tracePoint("SubJpf1.begin:" + _cnt);
        return new Forward("success");
        }

    /**
     * @jpf:action
     * @jpf:forward name="done" return-action="nestedDone"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "done",
                returnAction = "nestedDone") 
        })
    public Forward finish()
        {
        _log.tracePoint("SubJpf1.done:" + _cnt);
        return new Forward("done");
        }
    }
