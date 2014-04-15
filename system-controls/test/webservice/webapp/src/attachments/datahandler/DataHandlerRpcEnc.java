/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package attachments.datahandler;

import org.apache.beehive.attachments.ByteArrayDataSource;
import org.apache.beehive.attachments.DataHandlerAttachments;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.activation.DataHandler;
import java.io.InputStream;

/**
 * Purpose:
 */
@WebService()
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.ENCODED)
public class DataHandlerRpcEnc extends DataHandlerAttachments {

    @WebMethod()
    public DataHandler getPlainText() {
        return new DataHandler("Some plain text.", "text/plain");
    }

    @WebMethod()
    public DataHandler getXmlText() {
        return new DataHandler("<user><name>Fred</name></user>", "text/xml");
    }

    @WebMethod()
    public DataHandler getJpgImage() {
        byte[] img = loadImage("resources/bird1.jpg");
        return new DataHandler(new ByteArrayDataSource("Image data", img, "image/jpeg"));
    }

    @WebMethod()
    public DataHandler getGifImage() {
        byte[] img = loadImage("resources/beehive_logo.gif");
        return new DataHandler(new ByteArrayDataSource("Image data", img, "image/gif"));
    }

    protected InputStream getResourceStream(String nm) {
        return DataHandlerRpcEnc.class.getResourceAsStream(nm);
    }
}
