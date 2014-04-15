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
package directFormTest;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;
import java.util.ArrayList;

@Jpf.Controller
public class Controller extends PageFlowController
{
    private Form _form = new Form();
    private ArrayList   _info;
    private Info        _textBox;
    private Info        _textArea;
    private Info        _checkBox1;
    private Info        _checkBox2;
    private Info        _select;
    private Info        _radio;

    private Form _exposed = new Form();

    public Form getExposed() {
	return _exposed;
    }
    public void setExposed(Form exposed) {
	_exposed = exposed;
    }
	
    public ArrayList getInfo() {
        return _info;
    }
    public void setInfo(ArrayList info) {
        _info = info;
    }
        

    /**
     * @jpf:action
     * @jpf:forward name="formTest" path="formTest.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "formTest",
                path = "formTest.jsp") 
        })
    public Forward submit(Form form)
    {
        if (_info == null)
            createInfo();

	try {
            _textBox.setChange(" ");
            _textArea.setChange(" ");
            _checkBox1.setChange(" ");
            _checkBox2.setChange(" ");
            _select.setChange(" ");
            _radio.setChange(" ");

	    if (!_form.getText().equals(_exposed.getText())) {
                _textBox.setChange("Change, old value '" +
                                   _form.getText() + "' new value '" +
                                   _exposed.getText() + "'");
		_form.setText(_exposed.getText());
	    }
	    if (!_form.getTextArea().equals(_exposed.getTextArea())) {
                _textArea.setChange("Change, old value '" +
                                   _form.getTextArea() + "' new value '" +
                                   _exposed.getTextArea() + "'");

		_form.setTextArea(_exposed.getTextArea());
	    }
            if (_form.getCheckBox1() != _exposed.getCheckBox1()) {
                _checkBox1.setChange("Changed");
		_form.setCheckBox1(_exposed.getCheckBox1());
            }
            if (_form.getCheckBox2() != _exposed.getCheckBox2()) {
                _checkBox2.setChange("Changed");
		_form.setCheckBox2(_exposed.getCheckBox2());
            }
            if (!_form.getSelect().equals(_exposed.getSelect())) {
                _select.setChange("Change, old value '" +
                                   _form.getSelect() + "' new value '" +
                                   _exposed.getSelect() + "'");
		_form.setSelect(_exposed.getSelect());
            }
            if (!_form.getRadio().equals(_exposed.getRadio())) {
                _radio.setChange("Change, old value '" +
                                   _form.getRadio() + "' new value '" +
                                   _exposed.getRadio() + "'");
		_form.setRadio(_exposed.getRadio());
            }
            updateInfo();
	    return new Forward( "formTest" );
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return new Forward( "formTest" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="formTest" path="formTest.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "formTest",
                path = "formTest.jsp") 
        })
    public Forward begin()
    {
        if (_info == null)
            createInfo();

        return new Forward( "formTest" );
    }

    public static class Info implements Serializable {
        private String _name;
        private String _value;
        private String _change;

        public String getName() {
            return _name;
        }
        public void setName(String name) {
            _name = name;
        }

        public String getValue() {
            return _value;
        }
        public void setValue(String value) {
            _value = value;
        }

        public String getChange() {
            return _change;
        }
        public void setChange(String change) {
            _change = change;
        }
    }

    public static class Form implements Serializable
    {
	private String _text = "";
	private String _textArea = "";
        private boolean _checkBox1 = false;
        private boolean _checkBox2 = false;
        private String _select = "2";
        private String _radio = "2";

	public String getText() {
	    return _text;
	}
	public void setText(String text) {
	    _text = text;
	}

	public String getTextArea() {
	    return _textArea;
	}
	public void setTextArea(String textArea) {
	    _textArea = textArea;
	}

	public boolean getCheckBox1() {
	    return _checkBox1;
	}
	public void setCheckBox1(boolean checkBox1) {
	    _checkBox1 = checkBox1;
	}

	public boolean getCheckBox2() {
	    return _checkBox2;
	}
	public void setCheckBox2(boolean checkBox2) {
	    _checkBox2 = checkBox2;
	}

	public String getSelect() {
	    return _select;
	}
	public void setSelect(String select) {
	    _select = select;
	}

	public String getRadio() {
	    return _radio;
	}
	public void setRadio(String radio) {
	    _radio = radio;
	}
        
    }

    private void updateInfo()
    {
        _textBox.setValue(_form.getText());
        _textArea.setValue(_form.getTextArea());
        _checkBox1.setValue("" + _form.getCheckBox1());
        _checkBox2.setValue("" + _form.getCheckBox2());
        _select.setValue(_form.getSelect());
        _radio.setValue(_form.getRadio());
    }

    private void createInfo()
    {
        Info inf;
        _info = new ArrayList();

        // TextBox
        _textBox = new Info();
        _textBox.setName("TextBox");
        _textBox.setValue(_form.getText());
        _info.add(_textBox);

        // TextBox
        _textArea = new Info();
        _textArea.setName("TextArea");
        _textArea.setValue(_form.getTextArea());
        _info.add(_textArea);

        // checkBox1
        _checkBox1 = new Info();
        _checkBox1.setName("Checkbox1");
        _checkBox1.setValue(""+_form.getCheckBox1());
        _info.add(_checkBox1);

        // checkBox2
        _checkBox2 = new Info();
        _checkBox2.setName("Checkbox2");
        _checkBox2.setValue(""+_form.getCheckBox2());
        _info.add(_checkBox2);

        // Select
        _select = new Info();
        _select.setName("Select");
        _select.setValue(_form.getSelect());
        _info.add(_select);

        // Radio
        _radio = new Info();
        _radio.setName("Radio");
        _radio.setValue(_form.getRadio());
        _info.add(_radio);
    }
}
