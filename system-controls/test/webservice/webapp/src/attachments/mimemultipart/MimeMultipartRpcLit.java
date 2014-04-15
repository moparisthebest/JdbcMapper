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
package attachments.mimemultipart;

import org.apache.beehive.attachments.ByteArrayDataSource;
import org.apache.beehive.attachments.MimeMultipartAttachments;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.MessagingException;
import javax.activation.DataHandler;
import java.io.InputStream;


/**
 * Purpose: Test MultipartMime attachements for doc/lit/wrapped service.
 */
@WebService()
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL,
             parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class MimeMultipartRpcLit extends MimeMultipartAttachments {

    @WebMethod()
    public MimeMultipart echoPlainText(MimeMultipart pt) {
        return pt;
    }

    @WebMethod()
    public MimeMultipart echoXmlText(MimeMultipart pt) {
        return pt;
    }

    @WebMethod()
    public MimeMultipart getImages() {
        MimeMultipart mmp = new MimeMultipart();
        MimeBodyPart mbp = new MimeBodyPart();

        try {
            byte[] img = loadImage("resources/bird1.jpg");
            DataHandler dh = new DataHandler(new ByteArrayDataSource("Image data", img, "image/jpeg"));

            mbp.setDataHandler(dh);
            mbp.setHeader("Content-Type", "image/jpeg");
            mmp.addBodyPart(mbp);

            img = loadImage("resources/beehive_logo.gif");
            dh = new DataHandler(new ByteArrayDataSource("Image data2", img, "image/gif"));
            mbp = new MimeBodyPart();
            mbp.setDataHandler(dh);
            mbp.setHeader("Content-Type", "image/gif");
            mmp.addBodyPart(mbp);
        }
        catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
        return mmp;
    }

    protected InputStream getResourceStream(String nm) {
        return MimeMultipartRpcLit.class.getResourceAsStream(nm);
    }
}
