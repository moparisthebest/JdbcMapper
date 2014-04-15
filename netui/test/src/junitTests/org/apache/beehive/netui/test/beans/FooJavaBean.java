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
package org.apache.beehive.netui.test.beans;

/**
 *
 */
public class FooJavaBean {

    private boolean _bool = false;
    private int _intProperty = -1;
    private String _text = null;
    private BarJavaBean _bar = null;

    public FooJavaBean() {
        _bar = new BarJavaBean();
        _text = "ATextProperty";
    }

    public FooJavaBean(String text) {
        this();
        _bar = new BarJavaBean();
        _text = text;
    }

    public BarJavaBean getBarJavaBean() {
        return _bar;
    }

    public void setBarJavaBean(BarJavaBean bar) {
        _bar = bar;
    }

    public String getTextProperty() {
        return _text;
    }

    public void setTextProperty(String text) {
        _text = text;
    }

    public boolean getBooleanProperty() {
        return _bool;
    }

    public void setBooleanProperty(boolean bool) {
        _bool = bool;
    }

    public int getIntegerProperty() {
        return _intProperty;
    }

    public void setIntegerProperty(int intProperty) {
        _intProperty = intProperty;
    }
}
