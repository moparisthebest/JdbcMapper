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
package org.apache.beehive.controls.system.jms.samples;

import java.util.Date;

import javax.jms.Message;
import javax.jms.Queue;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.system.jms.samples.OrderQueueBean;
import org.apache.beehive.controls.system.jndi.JndiControlBean;

/**
 * Test the order-queue jms-control.
 * This test case will work with an ejb-container. Just set 
 * the system properties jndi.provider and jndi.factory.
 */
public class TestOrderQueue extends ControlTestCase
{    
    public TestOrderQueue(String name) throws Exception
    {
        super(name);
    }
    public void testQueue() throws Exception
    {
        // Programmatically instantiate order queue.
        OrderQueueBean orders = (OrderQueueBean)instantiateControl("org.apache.beehive.controls.system.jms.samples.OrderQueueBean");
        // Load up jndi info.
        String provider = System.getProperty("jndi.provider");
        if(provider != null)
        {
            orders.setJndiProviderURL(provider);
        }
        String factory = System.getProperty("jndi.factory");
        if(factory != null)
        {
            orders.setJndiContextFactory(factory);
        }
        // Submit an order.
        OrderQueue.Order order = new OrderQueue.Order(5,new String[] { "hair spray"});
        Date now = new Date();
        Message mess = orders.submitOrder(order,now);
        javax.jms.QueueSession sess = (javax.jms.QueueSession)orders.getSession();
        sess.commit();
        // Read the queue using the same session/connection.
        orders.getConnection().start();
        javax.jms.QueueReceiver consumer = sess.createReceiver((Queue)orders.getDestination());
        Message qmess = consumer.receive(5000);
        consumer.close();
        assertNotNull(qmess);
        String by = qmess.getStringProperty("DeliverBy");
        assertNotNull(by);
        assertEquals(by,now);
    }

}
