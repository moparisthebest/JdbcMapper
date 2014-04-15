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
package mockportal.unwrapmultipart;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Jpf.Controller()
public class Controller extends PageFlowController
{

    @Jpf.Action
    (
        forwards = { @Jpf.Forward(name = "success", path = "form.jsp") }
    )
    protected Forward begin()
    {
        return new Forward( "success" );
    }

    @Jpf.Action
    (
        forwards = { @Jpf.Forward(name = "success", path = "form.jsp") }
    )
    protected Forward getFile( TestUploadForm form )
    {
        HttpServletRequest request = getRequest();
        HttpServletRequest outerRequest = ScopedServletUtils.getOuterRequest( request );
        outerRequest.setAttribute( "testAttr", "testAttrValue" );
        //System.out.println("$$$ pageflow outerRequest: " + outerRequest);

        return new Forward( "success" );
    }

    public static class TestUploadForm implements Serializable
    {
        protected FormFile file;

        public FormFile getFile()
        {
            return file;
        }
        public void setFile(FormFile f)
        {
            file = f;
        }
    }
}
