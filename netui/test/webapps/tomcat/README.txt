To run the Tomcat DRTs (where VERSION is 5.0.x, 5.5.x, etc.):
    - Install beehive-netui-tomcat-server-VERSION.jar to server/lib in a Tomcat installation.
    - Install beehive-netui-tomcat-common-VERSION.jar to common/lib in the Tomcat installation.
    - Edit conf/server.xml in the Tomcat installation:
        1) Change the ServerLifecycleListener entry so that it uses the NetUI descriptors:
             <Listener className="org.apache.catalina.mbeans.ServerLifecycleListener"
                       debug="0"
                       descriptors="/org/apache/beehive/netui/tomcat/mbeans-descriptors.xml" />

        2) If you are using Tomcat 5.0.x, add the following <Context> entry in the <Host> element:

            <Context path="/tomcatWeb"
                     docBase="<path to the tomcatWeb directory"
                     debug="0"
                     reloadable="true"
                     crossContext="true">
                <Valve className="org.apache.beehive.netui.tomcat.PageflowValve" debug="0" />
            </Context>

           In Tomcat 5.5.x and above, this is picked up from META-INF/context.xml in the webapp.
    

    - Run 'ant build.VERSION'.
    - Start Tomcat.
    - Run 'ant deploy' (for 5.5.x)
    - Run 'ant bvt.running'.
