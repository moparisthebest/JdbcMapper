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

package org.apache.beehive.header;

public class HeaderAddressAxisgen {
    private String _street;
    private String _city;
    private String _state;
    private int _zip;

    public HeaderAddressAxisgen() { }

    public void setStreet(String street) { _street = street; }
    public String getStreet() { return _street; }

    public void setCity(String city) { _city = city; }
    public String getCity() { return _city; }

    public void setState(String state) { _state = state; }
    public String getState() { return _state; }

    public void setZip(int zip) { _zip = zip; }
    public int getZip() { return _zip; }
}
