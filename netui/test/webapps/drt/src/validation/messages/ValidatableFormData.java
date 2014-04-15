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
package validation.messages;

import org.apache.beehive.netui.pageflow.FormData;


public class ValidatableFormData extends FormData
{
    private String _item1;
    private String _item2;
    private String _item3;
    private String _item4;

    public String getItem1()
    { return _item1; }

    public void setItem1(String item)
    { _item1 = item; }

    public String getItem2()
    { return _item2; }

    public void setItem2(String item)
    { _item2 = item; }

    public String getItem3()
    { return _item3; }

    public void setItem3(String item)
    { _item3 = item; }

    public String getItem4()
    { return _item4; }

    public void setItem4(String item)
    { _item4 = item; }
}

