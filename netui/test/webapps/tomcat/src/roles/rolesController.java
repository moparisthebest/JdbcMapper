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
package roles;
import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import javax.servlet.http.HttpServletRequest;

@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "success",
            path = "index.jsp") 
    },
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="badRoleAction", path="index.jsp", rolesAllowed={"manager"})
    },
    catches = {
        @Jpf.Catch(
            type = NotLoggedInException.class,
            path = "login.jsp"),
        @Jpf.Catch(
            type = UnfulfilledRolesException.class,
            path = "index.jsp") 
    })
public class rolesController extends PageFlowController
{
    @Jpf.Action(
        rolesAllowed={"role1"}
    )
    protected Forward goodRoleAction()
    {
        return new Forward( "success" );
    }

    @Jpf.Action(
        rolesAllowed={"tomcat","manager"}
    )
    protected Forward allRolesAction()
    {
        return new Forward( "success" );
    }

    @Jpf.Action()
    protected Forward logIn()
        throws Exception
    {
        login( "both", "tomcat" );  // this is an example role in {tomcat}/conf/tomcat-users.xml
        return new Forward( "success" );
    }

    @Jpf.Action()
    protected Forward logOut()
    {
        logout( false );
        return new Forward( "success" );
    }
}
