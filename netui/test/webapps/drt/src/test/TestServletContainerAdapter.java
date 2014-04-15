package test;

import org.apache.beehive.netui.pageflow.DefaultServletContainerAdapter;
import org.apache.beehive.netui.pageflow.SecurityProtocol;
import org.apache.beehive.netui.pageflow.adapter.AdapterContext;
import org.apache.beehive.netui.pageflow.PageFlowEventReporter;
import org.apache.beehive.netui.pageflow.adapter.AdapterContext;

import javax.servlet.http.HttpServletRequest;

import pageFlowCore.eventReporter.TestEventReporter;

/**
 * Adapter that is used by tests in coreWeb (specifically, EventReporter).
 */
public class TestServletContainerAdapter
    extends DefaultServletContainerAdapter
{
    public boolean accept( AdapterContext context )
    {
        return true;
    }

    public PageFlowEventReporter createEventReporter()
    {
        return new TestEventReporter( getServletContext() );
    }
}
