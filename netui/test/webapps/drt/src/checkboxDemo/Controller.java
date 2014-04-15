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
package checkboxDemo;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletRequest;
import java.io.Serializable;

@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "begin",
            path = "Begin.jsp") 
    })
public class Controller extends PageFlowController
{
    private Form fileInfo;

    public Form getFileInfo() {
        return fileInfo;
    }

    public static class FileInfo implements java.io.Serializable
    {
        private String _file;
        private boolean _selected;

        public String getFile() {return _file;}
        public void setFile(String file) {_file = file;}
        public boolean getSelected() {return _selected;}
        public void setSelected(boolean selected) {_selected = selected;}
    }

    public static class Form implements Serializable
    {
        private FileInfo[] files;

        public Form() {
            files = new FileInfo[6];
        }

        public FileInfo[] getFiles() {
            return files;
        }

        public void reset(ActionMapping mapping, ServletRequest request) {
            files = new FileInfo[6];
        }
    }

    public String displayElement(boolean type)
    {
        return (type) ? "include" : "exclude";
    }

    @Jpf.Action(
        )
    public Forward postback(Form form)
    {
        return new Forward("begin",form);
    }

    @Jpf.Action(
        )
    public Forward begin()
    {
        Form f = new Form();
        f.files = new FileInfo[5];
        for (int i=0;i<f.files.length;i++) {
            f.files[i] = new FileInfo();
            f.files[i].setFile("File-" + i);
        }
        fileInfo = f;
        return new Forward("begin",f);
    }
}
