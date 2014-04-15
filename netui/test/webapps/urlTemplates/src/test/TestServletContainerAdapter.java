package test;

import org.apache.beehive.netui.pageflow.DefaultServletContainerAdapter;
import org.apache.beehive.netui.pageflow.SecurityProtocol;
import org.apache.beehive.netui.pageflow.adapter.AdapterContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Dummy adapter that gives secure/unsecure ports of 8443/8080, and says that any path that starts
 * with "/secure" is secure.
 */
public class TestServletContainerAdapter
    extends DefaultServletContainerAdapter
{
    public SecurityProtocol getSecurityProtocol( String uri, HttpServletRequest request )
    {
        if ( uri.indexOf( "/secure" ) != -1 )
        {
            return SecurityProtocol.SECURE;
        }

        return SecurityProtocol.UNSPECIFIED;
    }

    public int getListenPort( HttpServletRequest request )
    {
        return 8080;
    }

    public int getSecureListenPort( HttpServletRequest request )
    {
        return 8443;
    }

    public boolean accept( AdapterContext context )
    {
        return true;
    }
}
