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
package bugs.b13881;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import java.io.Serializable;

@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "begin",
            path = "Begin.jsp") 
    })
public class Controller extends PageFlowController
{
    private Form _form = new Form();
    private boolean _formCheckBox = false;
    private boolean _formCheckBox2 = true;

    public Form getForm() {
	    return _form;
    }
    public void setForm(Form form) {
	    _form = form;
    }

    public boolean getCheckBox() {
	    return _formCheckBox;
    }
    public void setCheckBox(boolean formCheckbox) {
	    _formCheckBox = formCheckbox;
    }

    public boolean getCheckBox2() {
	    return _formCheckBox2;
    }
    public void setCheckBox2(boolean formCheckbox) {
	    _formCheckBox2 = formCheckbox;
    }

    public static class Form implements Serializable {
        boolean _checkBox1;
        boolean _checkBox2 = true;

        public Form() {
            super();
        }

        public boolean getCheckBox1() {
            return _checkBox1;
        }
        public void setCheckBox1(boolean checkbox) {
            _checkBox1 = checkbox;
        }

        public boolean getCheckBox2() {
            return _checkBox2;
        }
        public void setCheckBox2(boolean checkbox) {
            _checkBox2 = checkbox;
        }
    }

    @Jpf.Action(
        )
    public Forward next(Form form)
    {
        _form.setCheckBox1(form.getCheckBox1());
        _form.setCheckBox2(form.getCheckBox2());
        return new Forward("begin",_form);
    }

    @Jpf.Action(
        )
    public Forward begin()
    {
        return new Forward("begin");
    }
}
