package org.apache.beehive.test.tools.antext;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.Target;

import java.net.InetAddress;

public class GetHostName extends Task
{
    public void execute() throws BuildException
    {
        try
        {   
            Target t = this.getOwningTarget();
            InetAddress inetAddr = InetAddress.getLocalHost();
            t.getProject().setProperty( "hostname", inetAddr.getHostName() );
            t.getProject().setProperty( "hostname.domain", inetAddr.getCanonicalHostName() );
        }
        catch ( Exception e )
        {
            throw new BuildException( "Error: " + e, e );
        }

    } // execute()

} // GetHostName
