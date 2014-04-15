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

package org.apache.beehive.attachments;

import javax.activation.DataHandler;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

/**
 */
public abstract class DataHandlerAttachments {

    public abstract DataHandler getPlainText();

    public abstract DataHandler getXmlText();

    public abstract DataHandler getJpgImage();

    public abstract DataHandler getGifImage();

    protected abstract InputStream getResourceStream(String imageName);

    protected byte[] loadImage(String imageName) {

        InputStream is = getResourceStream(imageName);

        byte[] buffer = new byte[1024];
        byte[] content = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            int count;
            while ((count = is.read(buffer, 0, buffer.length)) > 0) {
                os.write(buffer, 0, count);
            }
            content = os.toByteArray();
            is.close();
            os.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
        return content;
    }
}
