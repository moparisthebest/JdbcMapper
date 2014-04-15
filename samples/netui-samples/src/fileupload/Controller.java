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
package fileupload;

import java.io.Serializable;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Validatable;
import org.apache.struts.upload.FormFile;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;

/**
 * Demonstration of file upload to a page flow action.
 */
@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin",path="index.jsp")
    },
    messageBundles={
        @Jpf.MessageBundle(
            bundlePath="org.apache.beehive.samples.netui.resources.fileupload.messages"
        )
    },
    // For security, multipart request handling is disabled by default.  It can be enabled on a
    // per-pageflow basis (as it is here), or in WEB-INF/beehive-netui-config.xml, using the
    // <multipart-handler> element in <pageflow-config>.
    multipartHandler = Jpf.MultipartHandler.memory
)
public class Controller extends PageFlowController {

    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="success",
                path="results.jsp",
                actionOutputs={
                    @Jpf.ActionOutput(name="uploadForm", type=UploadForm.class, required=true)
                }
            )
            },
        validationErrorForward = @Jpf.Forward(name = "failure", path = "index.jsp")
    )
    public Forward upload(UploadForm form) {
        // Add the UploadForm as an "action output" for the results page.
        Forward fwd = new Forward("success");
        fwd.addActionOutput("uploadForm", form);
        return fwd;
    }

    @Jpf.FormBean()
    public static class UploadForm
        implements Serializable, Validatable {

        private String _label;
        private FormFile _file;

        public void setLabel(String label) {
            _label = label;
        }

        public String getLabel() {
            return _label;
        }

        public void setFile(FormFile file) {
            _file = file;
        }

        /**
         * The submitted file (required) ends up as this FormFile object.
         */
        @Jpf.ValidatableProperty(
            validateRequired = @Jpf.ValidateRequired(messageKey = "errors.filerequired")
        )
        public FormFile getFile() {
            return _file;
        }

        /**
         * In addition to any declarative validation that is defined per-property, this method
         * ensures that the submitted file is an HTML file or a text file.
         */
        public void validate(ActionMapping mapping, HttpServletRequest req, ActionMessages errors) {
            if (_file != null) {
                String fileName = _file.getFileName();
                if (! fileName.endsWith(".html") && ! fileName.endsWith(".txt")) {
                    errors.add("file", new ActionMessage("errors.fileextension"));
                }
            }
        }

        /**
         * This getter is bound to from results.jsp.
         */
        public String getFileDataString()
            throws FileNotFoundException, IOException {
            return new String(_file.getFileData());
        }
    }
}
