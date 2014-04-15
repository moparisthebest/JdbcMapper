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

package org.apache.beehive.test.controls.system.test;

import org.apache.beehive.test.controls.system.controls.OrderQueueControl;
import org.apache.beehive.test.controls.system.test.util.CactusControlTestCase;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.system.jms.JMSControl;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.DeliveryMode;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import javax.jms.QueueSession;
import javax.jms.Queue;
import javax.jms.MapMessage;
import javax.jms.BytesMessage;
import java.util.Date;
import java.util.HashMap;
import java.io.Serializable;

/**
 * JMS Control Queue tests.
 */
public class TestQueueJmsControl extends CactusControlTestCase
{

    @Control
    private OrderQueueControl _jmsControl;

    /**
     *  empty the message queue
     */
    public void testAAA() throws Exception {

        while (true) {
        if (getMessage(_jmsControl) == null)
            break;
        }
    }

    public void testSendOrderMessage() throws Exception {

        OrderQueueControl.Order o = new OrderQueueControl.Order(1234, new String[] {"heee","haw"});
        long now = new Date().getTime();
        Message m = _jmsControl.submitOrder(o, now);
        assertNotNull(m);
    }

    public void testRecieveOrderMessage() throws Exception {
        Message qmess = getMessage(_jmsControl);

        OrderQueueControl.Order o = new OrderQueueControl.Order(1234, new String[] {"heee","haw"});
        ObjectMessage om = (ObjectMessage)qmess;
        Serializable s =  om.getObject();
        assertNotNull(s);

        OrderQueueControl.Order out = (OrderQueueControl.Order)s;
        assertNotNull(out);
        assertEquals(o.getBuyerId(), out.getBuyerId());
        assertEquals(o.getItemList()[0], out.getItemList()[0]);
        assertEquals(o.getItemList()[1], out.getItemList()[1]);
    }

    public void testSendOrderWithPriority() throws Exception {
        OrderQueueControl.Order o = new OrderQueueControl.Order(333, new String[] {"hee","haw"});
        Message m = _jmsControl.submitOrderWithPriority(o);
        assertNotNull(m);
    }

    public void testReceiveOrderWithPriority() throws Exception {
        Message qmess = getMessage(_jmsControl);
        assertEquals(3, qmess.getJMSPriority());
    }

    public void testSendOrderWithCorrelationId() throws Exception {
        OrderQueueControl.Order o = new OrderQueueControl.Order(444, new String[] {"hee","haw"});
        Message m = _jmsControl.submitOrderWithCorrelationId(o);
        assertNotNull(m);
    }

    public void testReceiveOrderWithCorrelationId() throws Exception {
        Message qmess = getMessage(_jmsControl);
        assertEquals("666", qmess.getJMSCorrelationID());
    }

    public void testSendOrderWithDeliveryMode() throws Exception {
        OrderQueueControl.Order o = new OrderQueueControl.Order(555, new String[] {"hee","haw"});
        Message m = _jmsControl.submitOrderWithDeliveryMode(o);
        assertNotNull(m);
    }

    public void testReceiveOrderWithDeliveryMode() throws Exception {
        Message qmess = getMessage(_jmsControl);
        assertEquals(DeliveryMode.NON_PERSISTENT, qmess.getJMSDeliveryMode());
    }

    public void testSendTextMessage() throws Exception {
        Message m = _jmsControl.sendTextMessage("Hello There!");
        assertNotNull(m);
    }

    public void testReceiveTextMessage() throws Exception {
        Message qmess = getMessage(_jmsControl);
        TextMessage tm = (TextMessage)qmess;
        assertEquals("Hello There!", tm.getText());
    }

    public void testSendMapMessage() throws Exception {
        HashMap map = new HashMap();
        map.put("key1", "hello");
        map.put("key2", "there");
        Message m = _jmsControl.sendMapMessage(map);
        assertNotNull(m);
    }

    public void testReceiveMapMessage() throws Exception {
        MapMessage qmess = (MapMessage)getMessage(_jmsControl);
        assertEquals("hello", qmess.getString("key1"));
        assertEquals("there", qmess.getString("key2"));
    }

    public void testSendBytesMessage() throws Exception {
        byte[] bytes = new byte[] {Byte.MAX_VALUE, Byte.MIN_VALUE, Byte.MAX_VALUE};
        Message m = _jmsControl.sendBytesMessage(bytes);
        assertNotNull(m);
    }

    public void testReceiveBytesMessage() throws Exception {
        BytesMessage qmess = (BytesMessage)getMessage(_jmsControl);
        assertEquals(Byte.MAX_VALUE, qmess.readByte());
        assertEquals(Byte.MIN_VALUE, qmess.readByte());
        assertEquals(Byte.MAX_VALUE, qmess.readByte());
    }

    private static Message getMessage(JMSControl jmsControl) throws JMSException {

        QueueSession sess = (QueueSession)jmsControl.getSession();

        jmsControl.getConnection().start();
        javax.jms.QueueReceiver consumer = sess.createReceiver((Queue)jmsControl.getDestination());
        Message qmess = consumer.receive(5000);
        consumer.close();
        return qmess;
    }
}
