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
package handler;

import org.apache.axis.message.PrefixedQName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 * This handler is used for testing the wsc's handler support.  It will modify a header on the request and
 * reqponse of a message.
 */
public class TestHeaderHandler extends GenericHandler {
    private static Log LOGGER = LogFactory.getLog(TestHeaderHandler.class);

    static final String OUTPUT_ELEMENTS_KEY = "handlerOutputElements";
    static final String INPUT_ELEMENTS_KEY = "handlerInputElements";

    private HandlerInfo _handlerInfo;

    /**
     * Append the string "** RequestHandler **" to the specified header's value.
     *
     * @param mc MessageContext.
     * @return true if handler chain processing should continue.
     */
    public boolean handleRequest(MessageContext mc) {
        LOGGER.debug("In TestHeaderHandler's handleRequest");
        QName headerName = _handlerInfo.getHeaders()[0];
        try {
            SOAPMessageContext smc = (SOAPMessageContext) mc;
            SOAPMessage msg = smc.getMessage();
            SOAPPart part = msg.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPHeader header = envelope.getHeader();
            String headerValue = ((SOAPElement) header.getChildElements(new PrefixedQName(headerName)).next()).getValue();
            header.detachNode();

            SOAPHeader h = envelope.addHeader();
            SOAPHeaderElement he = h.addHeaderElement(new PrefixedQName(headerName));
            he.setValue(headerValue + "** RequestHandler **");
            return true;

        }
        catch (SOAPException e) {
            e.printStackTrace();
            throw new JAXRPCException(e);
        }
    }

    /**
     * Append the string "***ResponseHandler***" to the specified header's value.
     *
     * @param mc MessageContext.
     * @return true if the next handler in the chain should be invoked.
     */
    public boolean handleResponse(MessageContext mc) {
        LOGGER.debug("In TestHeaderHandler's handleResponse");
        QName headerName = _handlerInfo.getHeaders()[0];
        try {
            SOAPMessageContext smc = (SOAPMessageContext) mc;
            SOAPMessage msg = smc.getMessage();
            SOAPPart part = msg.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPHeader header = envelope.getHeader();
            String headerValue = ((SOAPElement) header.getChildElements(new PrefixedQName(headerName)).next()).getValue();
            header.detachNode();

            SOAPHeader h = envelope.addHeader();
            SOAPHeaderElement he = h.addHeaderElement(new PrefixedQName(headerName));
            he.setValue(headerValue + "** ResponseHandler **");
            return true;

        }
        catch (SOAPException e) {
            e.printStackTrace();
            throw new JAXRPCException(e);
        }
    }

    /*
     * @see javax.xml.rpc.handler.Handler#init(javax.xml.rpc.handler.HandlerInfo)
     */
    public void init(HandlerInfo handlerInfo) {
        _handlerInfo = handlerInfo;
    }

    /**
     * @return array of headers
     */
    public QName[] getHeaders() {
        return null;
    }
}