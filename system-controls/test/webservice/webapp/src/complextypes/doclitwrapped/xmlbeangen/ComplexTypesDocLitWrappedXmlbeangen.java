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
 * $Header:$Factory
 */
package complextypes.doclitwrapped.xmlbeangen;

import org.apache.beehive.complextypes.BankAccount;
import org.apache.beehive.complextypes.ComplexTypes;
import org.apache.beehive.complextypes.AccountException;
import org.apache.beehive.complextypes.ComplexAccountException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.rmi.RemoteException;

/**
 * doc/lit/wrapped service for test xmlbean generated types.
 */
@WebService
@SOAPBinding(
    style=SOAPBinding.Style.DOCUMENT,
    use=SOAPBinding.Use.LITERAL,
    parameterStyle=SOAPBinding.ParameterStyle.WRAPPED
)
public class ComplexTypesDocLitWrappedXmlbeangen implements ComplexTypes {

    @WebMethod
    public BankAccount echoAccount(BankAccount account) {
        return account;
    }

    @WebMethod()
    public int throwAccountException(int value) throws RemoteException {
        throw new AccountException("AccountException; input value=\"" + value + "\"");
    }

    @WebMethod()
    public int throwComplexAccountException() throws ComplexAccountException {
        throw new ComplexAccountException("FirstMessage", "SecondMessage");
    }
}
