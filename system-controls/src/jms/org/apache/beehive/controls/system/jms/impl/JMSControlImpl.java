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
package org.apache.beehive.controls.system.jms.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.ConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.BytesMessage;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Extensible;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.system.jms.JMSControl;
import org.apache.beehive.controls.system.jndi.JndiControlBean;

/**
 * <p/>
 * Implementation of the {@link JMSControl}.
 * </p>
 */
@ControlImplementation
public class JMSControlImpl
    implements JMSControl, Extensible, java.io.Serializable {

    private static Class XMLOBJ_CLASS = null;

    static {
        try {
            XMLOBJ_CLASS = Class.forName("org.apache.xmlbeans.XmlObject");
        }
        catch (ClassNotFoundException e) {
            // NOOP if apache xml beans not present
        }
    }

    /**
     * Implementation of the {@link org.apache.beehive.controls.system.jms.JMSControl#getSession()} method.
     *
     * @return the {@link Session}
     * @throws ControlException when an error occurs trying to create a JMS session
     */
    public Session getSession() throws ControlException {
        if (_session == null) {
            try {
                switch (getDestinationType()) {
                    case Auto:
                        determineDestination();
                        return getSession();
                    case Topic:
                        createTopicSession();
                        break;
                    case Queue:
                        createQueueSession();
                        break;
                }
            }
            catch (JMSException e) {
                throw new ControlException("Failure to get JMS connection or session", e);
            }
        }
        return _session;
    }

    /**
     * Get the JMS {@link javax.jms.Destination}.  Implementation of the
     * {@link org.apache.beehive.controls.system.jms.JMSControl#getDestination()} method.
     */
    public javax.jms.Destination getDestination() throws ControlException {
        if (_destination == null) {
            _destination =
                (javax.jms.Destination) getJndiControl().getResource(getDestinationProperties().sendJndiName(),
                    javax.jms.Destination.class);
        }
        return _destination;
    }

    /**
     * Get the JMS {@link javax.jms.Connection}.  Implementation of the
     * {@link JMSControl#getConnection()}.
     */
    public javax.jms.Connection getConnection() throws ControlException {
        getSession();
        return _connection;
    }

    /**
     * @see JMSControl#setHeaders
     */
    public void setHeaders(Map headers) {
        if (headers == null)
            return;

        HashMap<JMSControl.HeaderType, Object> map = new HashMap<JMSControl.HeaderType, Object>();
        for (Object name : headers.keySet()) {

            Object value = headers.get(name);

            HeaderType type = null;
            /*
            * Allow for string valued or HeaderType valued
            * map entries.
            */
            if (name instanceof HeaderType)
                type = (HeaderType) name;
            else {
                if (name.equals(HeaderType.JMSCorrelationID.toString()))
                    type = HeaderType.JMSCorrelationID;
                else if (name.equals(HeaderType.JMSDeliveryMode.toString()))
                    type = HeaderType.JMSDeliveryMode;
                else if (name.equals(HeaderType.JMSExpiration.toString()))
                    type = HeaderType.JMSExpiration;
                else if (name.equals(HeaderType.JMSMessageID.toString()))
                    type = HeaderType.JMSMessageID;
                else if (name.equals(HeaderType.JMSPriority.toString()))
                    type = HeaderType.JMSPriority;
                else if (name.equals(HeaderType.JMSRedelivered.toString()))
                    type = HeaderType.JMSRedelivered;
                else if (name.equals(HeaderType.JMSTimestamp.toString()))
                    type = HeaderType.JMSTimestamp;
                else if (name.equals(HeaderType.JMSType.toString()))
                    type = HeaderType.JMSType;
                else throw new IllegalArgumentException("Invalid JMS header type '" + name + "'");
            }
            map.put(type, value);
        }

        for (HeaderType key : map.keySet())
            setHeader(key, map.get(key));
    }

    /**
     * @see JMSControl#setHeader
     */
    public void setHeader(JMSControl.HeaderType type, Object value) {
        if (_headers == null)
            _headers = new HashMap<HeaderType, Object>();

        _headers.put(type, value);
    }


    /**
     * @see JMSControl#setProperties
     */
    public void setProperties(Map properties) {
        if (properties == null)
            return;

        /* todo: this is making an implicit assumption that the Map contains Strings */
        Iterator<String> i = properties.keySet().iterator();
        while (i.hasNext()) {
            String name = i.next();
            Object value = properties.get(name);
            setProperty(name, value);
        }
    }

    /**
     * @see JMSControl#setProperty
     */
    public void setProperty(String name, Object value) {
        if (_properties == null)
            _properties = new HashMap<String, Object>();

        _properties.put(name, value);
    }


    /**
     * Implementation of the {@link Extensible#invoke(java.lang.reflect.Method, Object[])} method.  This
     * method allows extension by interface.
     */
    public Object invoke(Method method, Object[] args) throws ControlException {
        assert (method != null && args != null);
        javax.jms.Message m = null;
        boolean isBody = false;
        try {
            Destination props = getDestinationProperties();
            Session session = getSession();
            String jmsType = null;
            String correlationId = null;
            /*
                * Set the deliveryMode, priority and expiration.
                */
            int deliveryMode = getProducer().getDeliveryMode();
            int priority = getProducer().getPriority();
            long expiration = getProducer().getTimeToLive();
            Object mode = getHeader(HeaderType.JMSDeliveryMode);
            if (mode != null)
                deliveryMode = deliveryModeToJmsMode(mode);

            Integer v = getHeaderAsInteger(HeaderType.JMSPriority);
            if (v != null)
                priority = v.intValue();

            Long l = getHeaderAsLong(HeaderType.JMSExpiration);
            if (l != null)
                expiration = l.longValue();

            /* Get the body of the message. */
            Object body = null;

            // Check to see if any parameter has annotation. If it doesn't then don't bother checking them.
            boolean hasAnnotation = method.getParameterAnnotations().length > 0;
            for (int i = 0; i < args.length; i++) {
                if (hasAnnotation) {
                    if (_context.getParameterPropertySet(method, i, Priority.class) != null)
                        continue;
                    if (_context.getParameterPropertySet(method, i, Property.class) != null)
                        continue;
                    if (_context.getParameterPropertySet(method, i, Expiration.class) != null)
                        continue;
                    if (_context.getParameterPropertySet(method, i, Delivery.class) != null)
                        continue;
                    if (_context.getParameterPropertySet(method, i, Type.class) != null)
                        continue;
                    if (_context.getParameterPropertySet(method, i, CorrelationId.class) != null)
                        continue;
                }

                if (isBody)
                    throw new IllegalArgumentException("At most one parameter may be defined as the body of the JMS message");

                body = args[i];
                isBody = true;
            }
            /*
                * Get the method level annotation properties.
                */
            Priority pri = _context.getMethodPropertySet(method, Priority.class);
            if (pri != null && pri.value() != -1)
                priority = pri.value();

            Expiration exp = _context.getMethodPropertySet(method, Expiration.class);
            if (exp != null && exp.value() != -1L)
                expiration = exp.value();

            Delivery del = _context.getMethodPropertySet(method, Delivery.class);
            if (del != null && del.value() != DeliveryMode.Auto)
                deliveryMode = deliveryModeToJmsMode(del.value());

            Type t = _context.getMethodPropertySet(method, Type.class);
            if (t != null && t.value().length() != 0)
                jmsType = t.value();

            CorrelationId id = _context.getMethodPropertySet(method, CorrelationId.class);
            if (id != null && id.value().length() != 0)
                correlationId = id.value();

            /* Create a message of the appropriate type and set the body. */
            JMSControl.Message mess = _context.getMethodPropertySet(method, JMSControl.Message.class);
            MessageType type = MessageType.Auto;
            if (mess != null)
                type = mess.value();

            if (type.equals(MessageType.Auto)) {
                if (body instanceof byte[])
                    type = MessageType.Bytes;
                else if (body instanceof Map || !isBody)
                    type = MessageType.Map;
                else if (body instanceof String)
                    type = MessageType.Text;
                else if (XMLOBJ_CLASS != null && XMLOBJ_CLASS.isAssignableFrom(body.getClass()))
                    type = MessageType.Text;
                else if (body instanceof javax.jms.Message)
                    type = MessageType.JMSMessage;
                else if (body instanceof Serializable)
                    type = MessageType.Object;
                else throw new ControlException("Cannot determine message type from body");
            }
            switch (type) {
                case Object:
                    checkBody(body, Serializable.class);
                    m = session.createObjectMessage((Serializable) body);
                    break;
                case Bytes:
                    BytesMessage sm;
                    checkBody(body, byte[].class);
                    m = sm = session.createBytesMessage();
                    sm.writeBytes((byte[]) body);
                    break;
                case Text:
                    if (XMLOBJ_CLASS != null && XMLOBJ_CLASS.isAssignableFrom(body.getClass())) {
                        try {
                            Method xmlText = XMLOBJ_CLASS.getMethod("xmlText", new Class[]{});
                            body = xmlText.invoke(body, new Object[]{});
                        }
                        catch (NoSuchMethodException e) {
                            throw new ControlException(e.getMessage(), e);
                        }
                        catch (IllegalAccessException e) {
                            throw new ControlException(e.getMessage(), e);
                        }
                        catch (InvocationTargetException e) {
                            throw new ControlException(e.getMessage(), e);
                        }
                    }
                    checkBody(body, String.class);
                    m = session.createTextMessage((String) body);
                    break;
                case Map:
                    MapMessage mm;
                    checkBody(body, Map.class);
                    m = mm = session.createMapMessage();
                    Map map = (Map)body;
                    Iterator iter = map.keySet().iterator();
                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        mm.setObject(key, map.get(key));
                    }
                    break;
                case JMSMessage:
                    checkBody(body, javax.jms.Message.class);
                    m = (javax.jms.Message) body;
                    break;
            }
            /*
                * Set the correlation id if given.
                */
            String correlationProp = props.sendCorrelationProperty();
            if (correlationProp != null && correlationProp.length() == 0)
                correlationProp = null;

            if (correlationId == null)
                correlationId = getHeaderAsString(HeaderType.JMSCorrelationID);

            Properties jmsProps = (Properties)_context.getMethodPropertySet(method, Properties.class);
            if (jmsProps != null && jmsProps.value() != null) {
                PropertyValue[] jprops = jmsProps.value();
                for (int i = 0; i < jprops.length; i++) {
                    PropertyValue jprop = jprops[i];
                    Class cls = jprop.type();
                    if (cls.equals(String.class))
                        m.setStringProperty(jprop.name(), jprop.value());
                    else if (cls.equals(Integer.class))
                        m.setIntProperty(jprop.name(), valueAsInteger(jprop.value()));
                    else if (cls.equals(Long.class))
                        m.setLongProperty(jprop.name(), valueAsLong(jprop.value()));
                    else
                        throw new IllegalArgumentException("Invalid type for property-value");
                }
            }
            /*
                * Set the properties/headers of the message from
                * the parameters to the invoke.
                */
            if (hasAnnotation) {
                for (int i = 0; i < args.length; i++) {
                    Property jmsProp = (Property) _context.getParameterPropertySet(method, i, Property.class);
                    if (jmsProp != null)
                        m.setObjectProperty(jmsProp.name(), args[i]);

                    Priority jmsPriority = (Priority) _context.getParameterPropertySet(method, i, Priority.class);
                    if (jmsPriority != null) {
                        Integer p = valueAsInteger(args[i]);
                        if (p != null)
                            priority = p.intValue();
                    }
                    Expiration jmsExpiration = (Expiration) _context.getParameterPropertySet(method, i, Expiration.class);
                    if (jmsExpiration != null) {
                        Long e = valueAsLong(args[i]);
                        if (e != null)
                            expiration = e.longValue();
                    }
                    Delivery jmsDelivery = (Delivery) _context.getParameterPropertySet(method, i, Delivery.class);
                    if (jmsDelivery != null && args[i] != null) {
                        deliveryMode = deliveryModeToJmsMode(args[i]);
                    }
                    t = (Type) _context.getParameterPropertySet(method, i, Type.class);
                    if (t != null && args[i] != null)
                        jmsType = (String) args[i];

                    CorrelationId jmsId = (CorrelationId)_context.getParameterPropertySet(method, i, CorrelationId.class);
                    if (jmsId != null && args[i] != null)
                        correlationId = (String) args[i];
                }
            }
            if (correlationProp != null)
                m.setStringProperty(correlationProp, correlationId);
            else
                m.setJMSCorrelationID(correlationId);

            /* Set the headers and properties from maps provided by setProperties() and setHeaders() */
            m.setJMSExpiration(expiration);
            m.setJMSDeliveryMode(deliveryMode);
            m.setJMSPriority(priority);
            setMessageHeaders(m);
            setMessageProperties(m);
            expiration = m.getJMSExpiration();
            deliveryMode = m.getJMSDeliveryMode();
            priority = m.getJMSPriority();

            _headers = null;
            _properties = null;

            /* Send the message. */
            switch (getDestinationType()) {
                case Topic:
                    ((TopicPublisher) getProducer()).publish(m, deliveryMode, priority, expiration);
                    break;
                case Queue:
                    getProducer().send(m, deliveryMode, priority, expiration);
                    break;
            }
        }
        catch (JMSException e) {
            throw new ControlException("Error in sending message", e);
        }
        return m;
    }


    @EventHandler(field="_resourceContext", eventSet=ResourceContext.ResourceEvents.class, eventName="onAcquire")
    public void onAcquire() {
    }

    /*
     * The onRelease event handler for the associated context This method will
     * release all resource acquired by onAcquire.
     */
    @EventHandler(field="_resourceContext", eventSet=ResourceContext.ResourceEvents.class, eventName="onRelease")
    public void onRelease() {
        close();
    }

    /**
     * Release any JMS related resources.
     */
    protected void close() {
        try {
            if (_producer != null) {
                _producer.close();
                _producer = null;
            }
            if (_session != null) {
                _session.close();
                _session = null;
            }
            if (_connection != null) {
                _connection.close();
                _connection = null;
            }
        }
        catch (JMSException e) {
            throw new ControlException("Unable to release JMS resource", e);
        }
    }

    /**
     * Deterimine whether we are working with a queue or a topic.
     */
    protected void determineDestination() {
        Destination props = getDestinationProperties();
        String factory = props.jndiConnectionFactory();
        ConnectionFactory cfactory =
            (ConnectionFactory) getJndiControl().getResource(factory, ConnectionFactory.class);

        if (cfactory instanceof QueueConnectionFactory && cfactory instanceof TopicConnectionFactory) {
            javax.jms.Destination dest = getDestination();
            if (dest instanceof Queue && dest instanceof Topic) {
                /* Try to create a topic producer...if fail then assume that it is a queue */
                try {
                    createTopicSession();
                    _producer = ((TopicSession) getSession()).createPublisher((Topic) getDestination());
                }
                catch (Exception e) {
                    close();
                    _destinationType = DestinationType.Queue;
                    return;
                }
                _destinationType = DestinationType.Topic;
            }
            else {
                if (dest instanceof javax.jms.Queue)
                    _destinationType = DestinationType.Queue;
                if (dest instanceof javax.jms.Topic)
                    _destinationType = DestinationType.Topic;
            }
        }
        else {
            if (cfactory instanceof QueueConnectionFactory)
                _destinationType = DestinationType.Queue;
            if (cfactory instanceof TopicConnectionFactory)
                _destinationType = DestinationType.Topic;
        }
    }

    /**
     * Get the queue/topic producer.
     *
     * @return the JMS producer.
     */
    protected MessageProducer getProducer() {
        if (_producer == null) {
            // Acquire the publisher/sender.
            try {
                javax.jms.Session sess = getSession();
                switch (getDestinationType()) {
                    case Auto:
                        try {
                            _producer = ((QueueSession) sess).createSender((Queue)getDestination());
                        }
                        catch (Exception e) {
                            /* todo: should never be catching an exception and running code like this */
                            _producer = ((TopicSession) sess).createPublisher((Topic)getDestination());
                        }
                        break;
                    case Topic:
                        _producer = ((TopicSession) sess).createPublisher((Topic)getDestination());
                        break;
                    case Queue:
                        _producer = ((QueueSession) sess).createSender((Queue)getDestination());
                        break;
                }
            }
            catch (JMSException e) {
                throw new ControlException("Unable to acquire JMS resource", e);
            }
        }
        return _producer;
    }


    /**
     * Creates a topic session.
     */
    protected void createTopicSession()
        throws JMSException {
        Destination props = getDestinationProperties();
        String factory = props.jndiConnectionFactory();
        boolean transacted = props.transacted();

        AcknowledgeMode ackMode = props.acknowledgeMode();
        TopicConnectionFactory connFactory =
            (TopicConnectionFactory) getJndiControl().getResource(factory, TopicConnectionFactory.class);

        _connection = connFactory.createTopicConnection();
        _session = ((TopicConnection) _connection).createTopicSession(transacted, modeToJmsMode(ackMode));
    }


    /**
     * Creates a queue session.
     */
    protected void createQueueSession()
        throws JMSException {
        Destination props = getDestinationProperties();
        String factory = props.jndiConnectionFactory();
        boolean transacted = props.transacted();
        AcknowledgeMode ackMode = props.acknowledgeMode();
        QueueConnectionFactory connFactory =
            (QueueConnectionFactory) getJndiControl().getResource(factory, QueueConnectionFactory.class);
        _connection = connFactory.createQueueConnection();
        _session = ((QueueConnection) _connection).createQueueSession(transacted, modeToJmsMode(ackMode));
    }

    /**
     * Convert the enum to the JMS ack mode.
     *
     * @param mode the enum mode.
     * @return the JMS mode.
     */
    protected int modeToJmsMode(AcknowledgeMode mode) {
        if (mode == AcknowledgeMode.Auto)
            return Session.AUTO_ACKNOWLEDGE;
        else if (mode == AcknowledgeMode.Client)
            return Session.CLIENT_ACKNOWLEDGE;
        else
            return Session.DUPS_OK_ACKNOWLEDGE;
    }

    /**
     * Convert the object to the JMS delivery mode.
     *
     * @param value a integer valued string, integer or DeliveryMode.
     * @return the JMS delivery mode.
     */
    protected int deliveryModeToJmsMode(Object value) {
        if (value instanceof DeliveryMode) {
            DeliveryMode mode = (DeliveryMode) value;
            switch (mode) {
                case Persistent:
                    return javax.jms.DeliveryMode.PERSISTENT;
                case NonPersistent:
                    return javax.jms.DeliveryMode.NON_PERSISTENT;
            }
        }

        if (value instanceof Number)
            return ((Number) value).intValue();
        else if (value instanceof String)
            return Integer.parseInt((String) value);
        else throw new IllegalArgumentException("Invalid delivery mode value");
    }

    /**
     * Check the given value to see if is appropriate for the given message value class.
     *
     * @param value the body.
     * @param cls   the expected class.
     */
    protected void checkBody(Object value, Class cls)
        throws ControlException {

        if (!cls.isInstance(value))
            throw new ControlException("Message body is not of correct type expected " + cls.getName());
    }

    /**
     * Get the destination annotation info for the message.
     *
     * @return the destination method annotation.
     */
    protected Destination getDestinationProperties() {
        return _context.getControlPropertySet(Destination.class);
    }

    /**
     * Get the destination type as specified via the
     * {@link org.apache.beehive.controls.system.jms.JMSControl.Destination#sendType()}
     * annotation.
     */
    protected DestinationType getDestinationType()
        throws ControlException {

        /*
           when previously unset, obtain from the value of the annotation.
           this value tends to be overridden later based on the configuration
           of the control instance.
         */
        if (_destinationType == null) {
            Destination d = getDestinationProperties();
            assert d != null : "Found a null Destination annotation";
            _destinationType = d.sendType();
        }

        return _destinationType;
    }

    /**
     * Set the properties of the given message.
     *
     * @param msg the message.
     */
    protected void setMessageProperties(javax.jms.Message msg)
        throws ControlException {

        if (_properties == null)
            return;

        Iterator i = _properties.keySet().iterator();
        while (i.hasNext()) {
            String name = (String) i.next();
            Object value = _properties.get(name);
            try {
                msg.setObjectProperty(name, value);
            }
            catch (JMSException e) {
                throw new ControlException("Cannot set property '" + name + "' into JMS message");
            }
        }
    }

    /**
     * Set the headers. Accessing msg may throw exception.
     *
     * @param msg the message.
     */
    protected void setMessageHeaders(javax.jms.Message msg)
        throws ControlException {

        if (_headers == null)
            return;

        for (HeaderType name : _headers.keySet()) {
            Object value = _headers.get(name);
            setMessageHeader(msg, name, value);
        }
    }

    /**
     * Set the header value of the given message.
     *
     * @param msg   the message.
     * @param type  the header type.
     * @param value the value.
     */
    protected void setMessageHeader(javax.jms.Message msg, HeaderType type, Object value) {
        switch (type) {
            case JMSCorrelationID:
                try {
                    if (value instanceof byte[])
                        msg.setJMSCorrelationIDAsBytes((byte[]) value);
                    else if (value instanceof String)
                        msg.setJMSCorrelationID((String) value);
                }
                catch (javax.jms.JMSException e) {
                    throw new ControlException("Error setting JMSCorrelationID for message", e);
                }
                break;
            case JMSPriority:
                try {
                    if (value instanceof Integer)
                        msg.setJMSPriority((Integer)value);
                    else if (value instanceof String)
                        msg.setJMSPriority(Integer.getInteger((String) value));
                }
                catch (javax.jms.JMSException e) {
                    throw new ControlException("Error setting JMSPriority for message", e);
                }
                break;
            case JMSExpiration:
                try {
                    if (value instanceof Long)
                        msg.setJMSExpiration((Long)value);
                    else if (value instanceof String)
                        msg.setJMSExpiration(Long.parseLong((String) value));
                }
                catch (javax.jms.JMSException e) {
                    throw new ControlException("Error setting JMSExpiration for message", e);
                }
                break;
            case JMSType:
                try {
                    msg.setJMSType((String) value);
                }
                catch (javax.jms.JMSException e) {
                    throw new ControlException("Error setting JMSType for message", e);
                }
                break;
        }
    }

    /**
     * Get the header type value.
     *
     * @param name the header type.
     * @return the value or null.
     */
    protected Object getHeader(HeaderType name) {
        assert name != null : "Can't get header with null name";

        if (_headers == null)
            return null;
        else return _headers.get(name.toString());
    }

    /**
     * Get the header type value as a string.
     *
     * @param name the header type.
     * @return the string value or null.
     */
    protected String getHeaderAsString(HeaderType name) {
        Object obj = getHeader(name);
        return obj != null ? obj.toString() : null;
    }

    /**
     * Get the header type value as a int.
     *
     * @param name the header type.
     * @return the int value or null.
     */
    protected Integer getHeaderAsInteger(HeaderType name) {
        return valueAsInteger(getHeader(name));
    }

    /**
     * Return the given value as an integer.
     *
     * @param obj a string or number object.
     * @return the integer value or null.
     */
    protected Integer valueAsInteger(Object obj) {
        if (obj instanceof String)
            return Integer.parseInt((String)obj);
        else if (obj instanceof Integer)
            return (Integer)obj;
        else if (obj instanceof Number)
            return new Integer(((Number)obj).intValue());
        return null;
    }

    /**
     * Get the header type value as a long.
     *
     * @param name the header type.
     * @return the long value or null.
     */
    protected Long getHeaderAsLong(HeaderType name) {
        return valueAsLong(getHeader(name));
    }

    /**
     * Return the given value as a long.
     *
     * @param obj a string or number object.
     * @return the long value or null.
     */
    protected Long valueAsLong(Object obj) {
        if (obj instanceof String)
            return Long.parseLong((String) obj);
        else if (obj instanceof Long)
            return (Long) obj;
        else if (obj instanceof Number)
            return new Long(((Number)obj).longValue());
        return null;
    }

    /**
     * Get the JNDI control instance.
     *
     * @return the jndi-control.
     */
    protected JndiControlBean getJndiControl() {
        if (!_jndiInitialized) {

            _jndiInitialized = true;
            Destination dest = getDestinationProperties();

            String contextFactory = nullIfEmpty(dest.jndiContextFactory());
            if (contextFactory != null)
                _jndiControl.setFactory(contextFactory);

            String providerURL = nullIfEmpty(dest.jndiProviderURL());
            if (providerURL != null)
                _jndiControl.setUrl(providerURL);

            String userName = nullIfEmpty(dest.jndiUsername());
            if (userName != null)
                _jndiControl.setJndiSecurityPrincipal(userName);

            String password = nullIfEmpty(dest.jndiPassword());
            if (password != null)
                _jndiControl.setJndiSecurityCredentials(password);
        }
        return _jndiControl;
    }

    /**
     * Return null if the given string is null or an empty string.
     *
     * @param str a string.
     * @return null or the string.
     */
    protected String nullIfEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return str;
    }


    @Context
    private ResourceContext _resourceContext;

    @Context
    private ControlBeanContext _context;

    @Control
    private JndiControlBean _jndiControl;

    /** The destination */
    private transient javax.jms.Destination _destination;

    private transient DestinationType _destinationType = null;

    /** The JMS connection. */
    private transient javax.jms.Connection _connection;

    /** The JMS session */
    private transient Session _session;

    /** The message producer. */
    private transient MessageProducer _producer;

    /** The JNDI control has been initialized */
    private boolean _jndiInitialized = false;

    /** The headers to set in the next message to be sent. */
    private Map<HeaderType, Object> _headers;

    /** The properties to set in the next message to be sent. */
    private Map<String, Object> _properties;
}
