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

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.runtime.servlet.ServletBeanContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ControlImplementation
public class PortfolioControlImpl 
        implements PortfolioControl, java.io.Serializable
{
    private static final Log LOG = LogFactory.getLog(PortfolioControlImpl.class);

    @Context
    private ControlBeanContext _context;

    @Context
    private ResourceContext _resourceContext;

    private static Portfolio _portfolio = null;

    static
    {
        _portfolio = new Portfolio();
        _portfolio.addStock(new Stock("AAAA", "AAAA Systems",
                                      "http://...", 1500));
        _portfolio.addStock(new Stock("BBBB", "BBBB",
                                      "http://...", 500));
        _portfolio.addStock(new Stock("CCCC", "CCCC Inc",
                                      "http://...", 1000));
        _portfolio.addStock(new Stock("ZZZZ", "ZZZZ Inc",
                                      "http://...", 300));
    }
    
    public Portfolio getPortfolio()
    {
        return _portfolio;
    }

    public Stock getStockBySymbol(String symbol)
    {
        Stock theStock = null;
        Stock[] stocks = _portfolio.getStocks();
        for(int i = 0; i < stocks.length; i++)
        {
            String curSymbol = stocks[i].getSymbol();
            if(curSymbol.equals(symbol))
            {
                theStock = stocks[i];
                break;
            }
        }

        checkContainerContext();

        return theStock;
    }

    @EventHandler(
            field = "_context",
            eventSet = ControlBeanContext.LifeCycle.class,
            eventName = "onCreate")
    public void onCreate() {
        LOG.info("bean context event -- onCreate");
        checkContainerContext();
    }

    @EventHandler(
            field = "_resourceContext",
            eventSet = ResourceContext.ResourceEvents.class,
            eventName = "onAcquire")
    public void onAquire() {
        LOG.info("bean context event -- onAcquire");
        checkContainerContext();
    }

    @EventHandler(
            field = "_resourceContext",
            eventSet = ResourceContext.ResourceEvents.class,
            eventName = "onRelease")
    public void onRelease() {
        LOG.info("bean context event -- onRelease");
        checkContainerContext();
    }

    private void checkContainerContext() {
        ControlContainerContext ccc = ControlThreadContext.getContext();
        LOG.info("control container context: " + (ccc != null ? ccc.hashCode() : "is null"));

        if(ccc == null)
            throw new IllegalStateException("Control could not find a valid ControlContainerContext!");

        if(!(ccc instanceof ServletBeanContext))
            throw new IllegalStateException("Control container context is not aServletBeanContext");

        ServletBeanContext servletBeanContext = (ServletBeanContext)ccc;
        if(servletBeanContext.getServletRequest() == null)
            throw new IllegalStateException("ServletBeanContext could not provide a valid ServletRequest!");

        if(servletBeanContext.getServletResponse() == null)
            throw new IllegalStateException("ServletBeanContext could not provide a valid ServletResponse!");

        if(servletBeanContext.getServletContext() == null)
            throw new IllegalStateException("ServletBeanContext could not provide a valid ServletContext!");
    }
}
