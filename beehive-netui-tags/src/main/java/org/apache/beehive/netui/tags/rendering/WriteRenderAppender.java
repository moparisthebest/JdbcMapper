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
package org.apache.beehive.netui.tags.rendering;

import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.struts.util.RequestUtils;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class WriteRenderAppender extends AbstractRenderAppender
{
    private static final Logger logger = Logger.getInstance(WriteRenderAppender.class);

    private JspContext _jspC;

    public WriteRenderAppender()
    {
    }

    public WriteRenderAppender(JspContext jspC)
    {
        _jspC = jspC;
    }

    public void setPageContext(JspContext jspC)
    {
        _jspC = jspC;
    }

    public void append(String s)
    {
        JspWriter writer = _jspC.getOut();
        try {
            writer.print(s);
        }
        catch (IOException e) {
            if (_jspC instanceof PageContext)
                RequestUtils.saveException((PageContext) _jspC, e);
            logger.error(Bundle.getString("Tags_WriteException"), e);
        }
    }

    /**
     * This is a method not found on ResponseUtils and one that should be there.
     * @param c
     */
    public void append(char c)
    {
        JspWriter writer = _jspC.getOut();
        try {
            writer.print(c);
        }
        catch (IOException e) {
            if (_jspC instanceof PageContext)
                RequestUtils.saveException((PageContext) _jspC, e);
            logger.error(Bundle.getString("Tags_WriteException"), e);
        }
    }

}
