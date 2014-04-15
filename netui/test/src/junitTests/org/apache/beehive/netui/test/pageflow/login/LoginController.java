package org.apache.beehive.netui.test.pageflow.login;

import javax.security.auth.login.LoginException;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.doesnotexist")
    }
)
public class LoginController extends PageFlowController
{
    public static class LoginForm
    {
        private String _username;
        private String _password;

        public String getUsername() {
            return _username;
        }

        public void setUsername(String username) {
            _username = username;
        }

        public String getPassword() {
            return _password;
        }

        public void setPassword(String password) {
            _password = password;
        }
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="success", path="success.doesnotexist")
        },
        catches={
            @Jpf.Catch(type=LoginException.class, path="failure.doesnotexist")
        }
    )
    public Forward login(LoginForm form)
        throws LoginException
    {
        login(form.getUsername(), form.getPassword());
        Forward fwd = new Forward("success");
        fwd.addActionOutput("userPrincipal", getUserPrincipal());
        fwd.addActionOutput("isUserInGoodrole", Boolean.valueOf(isUserInRole("goodrole")));
        fwd.addActionOutput("isUserInBadrole", Boolean.valueOf(isUserInRole("badrole")));
        return fwd;
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="success", path="success.doesnotexist")
        }
    )
    public Forward logout()
    {
        logout(false);
        Forward fwd = new Forward("success");
        fwd.addActionOutput("userPrincipal", getUserPrincipal());
        fwd.addActionOutput("isUserInGoodrole", Boolean.valueOf(isUserInRole("goodrole")));
        return fwd;
    }
}
