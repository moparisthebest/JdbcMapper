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
package databinding.datagrid.controls;

public class StockBean {

    private String _symbol;
    private String _name;
    private double _price;
    private String _web;
    private int _shares;
    private String _exchange;

    public StockBean() {}

    public StockBean(String symbol, String name, double price, String web, int shares, String exchange) {
	this();

	_symbol = symbol;
	_name = name;
	_price = price;
	_web = web;
	_shares = shares;
	_exchange = exchange;
    }

    public String getSymbol() {
	return _symbol;
    }

    public void setSymbol(String symbol) {
	_symbol = symbol;
    }

    public String getName() {
	return _name;
    }

    public void setName(String name) {
	_name = name;
    }

    public double getPrice() {
	return _price;
    }

    public void setPrice(double price) {
	_price = price;
    }

    public String getWeb() {
	return _web;
    }

    public void setWeb(String web) {
	_web = web;
    }

    public int getShares() {
	return _shares;
    }

    public void setShares(int shares) {
	_shares = shares;
    }

    public String getExchange() {
	return _exchange;
    }

    public void setExchange(String exchange) {
	_exchange = exchange;
    }
}

