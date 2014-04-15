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
package attachments.images;

import org.apache.beehive.attachments.ImageAttachments;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.awt.Image;
import java.io.InputStream;

/**
 * Purpose:
 */
@WebService()
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL,
             parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class ImagesRpcLit extends ImageAttachments {

    @WebMethod()
    public Image getJpgImage() {
        return loadImage("resources/bird1.jpg");
    }

    @WebMethod()
    public Image getGifImage() {
        return loadImage("resources/beehive_logo.gif");
    }

    @WebMethod()
    public Image[] getImages() {
        Image[] images = new Image[2];
        images[0] = getJpgImage();
        images[1] = getGifImage();
        return images;
    }

    protected InputStream getResourceStream(String nm) {
        return ImagesRpcLit.class.getResourceAsStream(nm);
    }
}
