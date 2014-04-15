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

import org.apache.beehive.test.controls.system.controls.OrderTopicControl;
import org.apache.beehive.test.controls.system.test.util.CactusControlTestCase;
import org.apache.beehive.controls.api.bean.Control;

import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.jms.Topic;
import javax.jms.DeliveryMode;
import javax.jms.ObjectMessage;
import javax.jms.BytesMessage;
import javax.jms.MapMessage;
import java.util.Date;
import java.util.HashMap;
import java.io.Serializable;

/**
 * Test JMS control for Topic support.
 */
public class TestTopicJmsControl extends CactusControlTestCase
{
    @Control
    private OrderTopicControl _jmsControl;

    public void testSendOrderMessage() throws Exception {
        // create a subscription to the topic
        TopicSession sess = (TopicSession)_jmsControl.getSession();
        _jmsControl.getConnection().start();
        TopicSubscriber consumer = sess.createSubscriber((Topic)_jmsControl.getDestination());

        OrderTopicControl.Order o = new OrderTopicControl.Order(1234, new String[] {"heee","haw"});
        long now = new Date().getTime();
        Message m = _jmsControl.submitOrder(o, now);
        assertNotNull(m);

        // get the message from the topic
        Message qmess = consumer.receive(5000);
        consumer.close();

        ObjectMessage om = (ObjectMessage)qmess;
        Serializable s =  om.getObject();
        assertNotNull(s);

        OrderTopicControl.Order out = (OrderTopicControl.Order)s;
        assertNotNull(out);
        assertEquals(o.getBuyerId(), out.getBuyerId());
        assertEquals(o.getItemList()[0], out.getItemList()[0]);
        assertEquals(o.getItemList()[1], out.getItemList()[1]);
    }

    public void testSendOrderWithPriority() throws Exception {
        // create a subscription to the topic
        TopicSession sess = (TopicSession)_jmsControl.getSession();
        _jmsControl.getConnection().start();
        TopicSubscriber consumer = sess.createSubscriber((Topic)_jmsControl.getDestination());

        // send the message
        OrderTopicControl.Order o = new OrderTopicControl.Order(333, new String[] {"hee","haw"});
        Message m = _jmsControl.submitOrderWithPriority(o);
        assertNotNull(m);

        // get the message from the topic
        Message qmess = consumer.receive(5000);
        consumer.close();
        assertEquals(3, qmess.getJMSPriority());
    }

    public void testSendOrderWithCorrelationId() throws Exception {
        // create a subscription to the topic
        TopicSession sess = (TopicSession)_jmsControl.getSession();
        _jmsControl.getConnection().start();
        TopicSubscriber consumer = sess.createSubscriber((Topic)_jmsControl.getDestination());

        // send the message
        OrderTopicControl.Order o = new OrderTopicControl.Order(444, new String[] {"hee","haw"});
        Message m = _jmsControl.submitOrderWithCorrelationId(o);
        assertNotNull(m);

        // get the message from the topic
        Message qmess = consumer.receive(5000);
        consumer.close();
        assertEquals("666", qmess.getJMSCorrelationID());
    }

    public void testSendOrderWithDeliveryMode() throws Exception {
        // create a subscription to the topic
        TopicSession sess = (TopicSession)_jmsControl.getSession();
        _jmsControl.getConnection().start();
        TopicSubscriber consumer = sess.createSubscriber((Topic)_jmsControl.getDestination());

        // send the message
        OrderTopicControl.Order o = new OrderTopicControl.Order(555, new String[] {"hee","haw"});
        Message m = _jmsControl.submitOrderWithDeliveryMode(o);
        assertNotNull(m);

        // get the message from the topic
        Message qmess = consumer.receive(5000);
        consumer.close();
        assertEquals(DeliveryMode.NON_PERSISTENT, qmess.getJMSDeliveryMode());
    }

    public void testSendTextMessage() throws Exception {

        // create a subscription to the topic
        TopicSession sess = (TopicSession)_jmsControl.getSession();
        _jmsControl.getConnection().start();
        TopicSubscriber consumer = sess.createSubscriber((Topic)_jmsControl.getDestination());

        // send the message
        Message m = _jmsControl.sendTextMessage("Hello There!");
        assertNotNull(m);

        // get the message from the topic
        Message qmess = consumer.receive(5000);
        consumer.close();


        TextMessage tm = (TextMessage)qmess;
        assertEquals("Hello There!", tm.getText());
    }

    public void testSendBytesMessage() throws Exception {

        // create a subscription to the topic
        TopicSession sess = (TopicSession)_jmsControl.getSession();
        _jmsControl.getConnection().start();
        TopicSubscriber consumer = sess.createSubscriber((Topic)_jmsControl.getDestination());

        // send the message
        byte[] bytes = new byte[] {Byte.MAX_VALUE, Byte.MIN_VALUE, Byte.MAX_VALUE};
        Message m = _jmsControl.sendBytesMessage(bytes);
        assertNotNull(m);

        // get the message from the topic
        Message qmess = consumer.receive(5000);
        consumer.close();


        BytesMessage bm = (BytesMessage)qmess;
        assertEquals(Byte.MAX_VALUE, bm.readByte());
        assertEquals(Byte.MIN_VALUE, bm.readByte());
        assertEquals(Byte.MAX_VALUE, bm.readByte());
    }

    public void testSendMapMessage() throws Exception {

        // create a subscription to the topic
        TopicSession sess = (TopicSession)_jmsControl.getSession();
        _jmsControl.getConnection().start();
        TopicSubscriber consumer = sess.createSubscriber((Topic)_jmsControl.getDestination());

        // send the message
        HashMap map = new HashMap();
        map.put("key1", "hello");
        map.put("key2", "there");
        Message m = _jmsControl.sendMapMessage(map);
        assertNotNull(m);

        // get the message from the topic
        Message qmess = consumer.receive(5000);
        consumer.close();


        MapMessage mp = (MapMessage)qmess;
        assertEquals("hello", mp.getString("key1"));
        assertEquals("there", mp.getString("key2"));
    }
}
