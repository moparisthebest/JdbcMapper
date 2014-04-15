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
package header;

import org.apache.beehive.header.HeaderEndpoint;
import org.apache.beehive.header.HeaderAddress;
import org.apache.beehive.header.AddressHolder;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.rpc.holders.IntHolder;
import javax.xml.rpc.holders.StringHolder;

@WebService()
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL)
public class HeaderObjectRpcLitEndpoint {
    static final long serialVersionUID = 1L;

    /* modify the the po in the header, echo back the po param */
    public HeaderAddress echoObject(@WebParam(header = true, mode = WebParam.Mode.INOUT) AddressHolder address, HeaderAddress addr)
    {
        HeaderAddress ha = address.value;
        ha.setCity("Boulder");
        return addr;
    }
}
