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
package tags.formatTags;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.util.Calendar;
import java.util.Date;

/**
 * @jpf:forward name="begin" path="Begin.jsp"
 */
@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "begin",
            path = "Begin.jsp") 
    })
public class Controller extends PageFlowController
{
    private String _letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String _hash = "#Hashs#";
    private String _spaces = "<     >";
    private String _strPattern = "##-##";
    private String _number = "123456.789";
    private Date _date = null;

    public Controller() {
        Calendar c = Calendar.getInstance();
        c.set(2002,0,17,13,30,8);
        _date = c.getTime();
    }

    public String getLetters() {
        return _letters;
    }
    public String getHash() {
        return _hash;
    }
    public String getSpaces() {
        return _spaces;
    }
    public String getStringPattern() {
        return _strPattern;
    }
    public String getNumber() {
        return _number;
    }
    public Date getDate() {
        return _date;
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward postback()
    {
        return new Forward("begin");
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward begin()
    {
        return new Forward("begin");
    }
}
