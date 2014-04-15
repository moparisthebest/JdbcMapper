/*   Licensed to the Apache Software Foundation (ASF) under one or more
/*   contributor license agreements.  See the NOTICE file distributed with
/*   this work for additional information regarding copyright ownership.
/*   The ASF licenses this file to You under the Apache License, Version 2.0
/*   (the "License"); you may not use this file except in compliance with
/*   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package handlers;

import org.apache.beehive.handler.HandlerTestService;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.xml.rpc.holders.StringHolder;

/**
 * extremely simple handler web service -- service does not use handler's on the client does.  
 */
@WebService()
public class HandlerDocLitEndpoint implements HandlerTestService {

    @WebMethod()
    public void echoStringHeader(@WebParam(header = true, mode = WebParam.Mode.INOUT) StringHolder inputHeader) {
        inputHeader.value = inputHeader.value + "** Server **";
    }
}
