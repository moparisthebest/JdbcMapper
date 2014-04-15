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

import java.util.ArrayList;

public class Portfolio
    implements java.io.Serializable
{
    private ArrayList<Stock> _stocks;

    public Stock[] getStocks()
    {
        return _stocks.toArray(new Stock[0]);
    }

    public void addStock(Stock stock)
    {
        if(_stocks == null)
            _stocks = new ArrayList<Stock>();

        _stocks.add(stock);
    }

    public void removeStock(Stock stock)
    {
        if(_stocks != null && _stocks.contains(stock))
            _stocks.remove(stock);
    }
}
