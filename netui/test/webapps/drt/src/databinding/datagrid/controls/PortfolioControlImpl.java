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

import org.apache.beehive.controls.api.bean.ControlImplementation;

@ControlImplementation(isTransient=true)
public class PortfolioControlImpl
    implements PortfolioControl
{
    public PortfolioBean getPortfolio() {
	PortfolioBean portfolio = new PortfolioBean();
	portfolio.addStock(new StockBean("BEAS", "BEA Systems", 14.35, "http://www.bea.com", 500, "nasdaq"));
	portfolio.addStock(new StockBean("CSCO", "Cisco Systems", 19.42, "http://www.cisco.com", 400, "nasdaq"));
	portfolio.addStock(new StockBean("GE", "General Electric", 59.42, "http://www.ge.com", 300, "nyse"));
	portfolio.addStock(new StockBean("RHAT", "RedHat Systems", 18.20, "http://www.redhat.com", 200, "nasdaq"));
	portfolio.addStock(new StockBean("YHOO", "Yahoo Inc", 48.16, "http://www.yahoo.com", 100, "nyse"));
	return portfolio;
    }
}
