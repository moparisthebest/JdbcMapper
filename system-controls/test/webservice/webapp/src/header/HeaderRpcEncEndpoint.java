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

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.rpc.holders.IntHolder;
import javax.xml.rpc.holders.StringHolder;

@WebService()
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.ENCODED)
public class HeaderRpcEncEndpoint implements HeaderEndpoint {
    static final long serialVersionUID = 1L;

    /* return the value of the header */
    public String echoReturnStringHeader(@WebParam(header = true, mode = WebParam.Mode.IN) String inputHeader) {
        return inputHeader;
    }

    /* set the value of an INOUT header */
    public void modifyStringHeader(@WebParam(header = true, mode = WebParam.Mode.INOUT) StringHolder inputHeader) {
        inputHeader.value = "Header Set By Service!";
    }

    /* echo a string, set the the value of an INOUT header */
    public String echoStringHeaderAndResult(@WebParam(header = true, mode = WebParam.Mode.INOUT) StringHolder inputHeader, String inputString) {
        inputHeader.value = "Header Set By Service!";
        return inputString;
    }

    /* swap the values of two INOUT headers */
    public void swapHeaderStrings(@WebParam(header = true, mode = WebParam.Mode.INOUT) StringHolder header1,
                                  @WebParam(header = true, mode = WebParam.Mode.INOUT) StringHolder header2) {
        String tmp = header1.value;
        header1.value = header2.value;
        header2.value = tmp;
    }

    /* set two OUT headers to setValue */
    public void setHeaderStrings(@WebParam(header = true, mode = WebParam.Mode.OUT) StringHolder header1,
                                 @WebParam(header = true, mode = WebParam.Mode.OUT) StringHolder header2,
                                 String setValue) {
        header1.value = setValue;
        header2.value = setValue;
    }

    public int echointHeader(@WebParam(header = true, mode = WebParam.Mode.INOUT) IntHolder inputHeader, int inputint) {
        inputHeader.value = inputint;
        return inputint;
    }
}
