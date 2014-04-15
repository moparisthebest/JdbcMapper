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
package faces.facesSmoke;  

import javax.faces.application.FacesMessage;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class TestValidator implements Validator, StateHolder
{ 
    boolean _transient = false;
    public boolean isTransient()
    {
        return _transient;
    }

    public void restoreState(FacesContext arg0, Object arg1)
    {
    }

    public Object saveState(FacesContext arg0)
    {
        return null;
    }

    public void setTransient(boolean transientVal)
    {
            _transient = transientVal;
    }

    public void validate(FacesContext ctxt, UIComponent component, Object toValidate) throws ValidatorException
    {
        System.err.println("Inside the validator:" + toValidate);
        String s = toValidate.toString();
        if (!s.startsWith("x-")) {
            FacesMessage errMsg =new FacesMessage("Text must start with 'x-'");
            throw new ValidatorException(errMsg);
        }
    }
} 
