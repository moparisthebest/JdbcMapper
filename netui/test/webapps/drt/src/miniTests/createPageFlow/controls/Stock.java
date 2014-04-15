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
package miniTests.createPageFlow.controls; 

public class Stock implements java.io.Serializable
{
    private String _symbol = null;
    private String _name = null;
    private String _web = null;
    private int _shares = 0;

    public Stock(String symbol, String name, String web, int shares)
    {
        _symbol = symbol;
        _name = name;
        _web = web;
        _shares = shares;
    }

    public String getSymbol() {return _symbol;}
    public void setSymbol(String symbol) {_symbol = symbol;}

    public String getWeb() {return _web;}
    public void setWeb(String web) {_web = web;}

    public String getName() {return _name;}
    public void setName(String name) {_name = name;}

    public int getShares() {return _shares;}
    public void setShares(int shares) {_shares = shares;}
} 
