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

package controls.context;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.test.controls.context.ServiceGetterBean;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowControlContainer;
import org.apache.beehive.netui.pageflow.PageFlowControlContainerFactory;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Test getting other services avaliable to control context
 * The control is instantiated declaratively
 */
@Jpf.Controller(forwards = {@Jpf.Forward(name = "index", path = "index.jsp")})
public class Controller extends PageFlowController {

    @Control
    public ServiceGetterBean _serviceGetter;

    @Jpf.Action()
    protected Forward begin() {
        return new Forward("index");
    }

    @Jpf.Action()
    protected Forward testServices() {
        try {
            if (!checkBeanContextService(_serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServices.do: BeanContextService unavailable.");
            }

            if (!checkControlBeanContext(_serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServices.do: ControlBeanContext unavailable.");
            }

            if (!checkResourceContext(_serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServices.do: ResourceContext unavailable.");
            }

            if (!checkServletContext(_serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServices.do: ServletContext unavailable.");
            }

            if (!checkServletRequest(_serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServices.do: ServletRequest unavailable.");
            }

            if (!checkServletResponse(_serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServices.do: ServletResponse unavailable.");
            }

            if (!checkHttpServletRequest(_serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServices.do: HttpServletRequest unavailable.");
            }

            if (!checkHttpServletResponse(_serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServices.do: HttpServletResponse unavailable.");
            }

        }
        catch (ClassNotFoundException e) {
            return new Forward("index", "message", "ERROR: " + e.getMessage());
        }
        return new Forward("index", "message", "PASSED");
    }

    @Jpf.Action()
    protected Forward testServicesP() {
        try {

            PageFlowControlContainer pfcc = PageFlowControlContainerFactory.getControlContainer(getRequest(), getServletContext());
            pfcc.createAndBeginControlBeanContext(this, getRequest(), getResponse(), getServletContext());

            ServiceGetterBean serviceGetter = (ServiceGetterBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.context.ServiceGetterBean");

            if (!checkBeanContextService(serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServicesP.do: BeanContextService unavailable.");
            }

            if (!checkControlBeanContext(serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServicesP.do: ControlBeanContext unavailable.");
            }

            if (!checkResourceContext(serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServicesP.do: ResourceContext unavailable.");
            }

            if (!checkServletContext(serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServicesP.do: ServletContext unavailable.");
            }

            if (!checkServletRequest(serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServicesP.do: ServletRequest unavailable.");
            }

            if (!checkServletResponse(serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServicesP.do: ServletResponse unavailable.");
            }

            if (!checkHttpServletRequest(serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServicesP.do: HttpServletRequest unavailable.");
            }

            if (!checkHttpServletResponse(serviceGetter)) {
                return new Forward("index", "message", "ERROR: testServicesP.do: HttpServletResponse unavailable.");
            }

        }
        catch (Exception e) {
            return new Forward("index", "message", "ERROR: " + e.getMessage());
        }
        return new Forward("index", "message", "PASSED");
    }


    private boolean checkBeanContextService(ServiceGetterBean ctrl) {
        return ctrl.getBeanContext() != null;
    }

    private boolean checkControlBeanContext(ServiceGetterBean ctrl) {
        return ctrl.getControlBeanContext() != null;
    }

    private boolean checkResourceContext(ServiceGetterBean ctrl) throws ClassNotFoundException {
        return ctrl.getService(Class.forName("org.apache.beehive.controls.api.context.ResourceContext"), null) != null;
    }

    private boolean checkServletContext(ServiceGetterBean ctrl) throws ClassNotFoundException {
        return ctrl.getService(Class.forName("javax.servlet.ServletContext"), null) != null;
    }

    private boolean checkServletRequest(ServiceGetterBean ctrl) throws ClassNotFoundException {
        boolean result = false;
        Object servletrequest = ctrl.getService(Class.forName("javax.servlet.ServletRequest"), null);
        if ((servletrequest != null) && (servletrequest instanceof ServletRequestWrapper))
            result = true;

        return result;
    }

    private boolean checkServletResponse(ServiceGetterBean ctrl) throws ClassNotFoundException {
        boolean result = false;
        Object servletresponse = ctrl.getService(Class.forName("javax.servlet.ServletResponse"), null);
        if ((servletresponse != null) && (servletresponse instanceof ServletResponseWrapper))
            result = true;

        return result;
    }

    private boolean checkHttpServletRequest(ServiceGetterBean ctrl) throws ClassNotFoundException {
        boolean result = false;
        Object httpservletrequest = ctrl.getService(Class.forName("javax.servlet.http.HttpServletRequest"), null);
        if ((httpservletrequest != null) && (httpservletrequest instanceof HttpServletRequestWrapper))
            result = true;

        return result;
    }

    private boolean checkHttpServletResponse(ServiceGetterBean ctrl) throws ClassNotFoundException {
        boolean result = false;
        Object httpservletresponse = ctrl.getService(Class.forName("javax.servlet.http.HttpServletResponse"), null);
        if ((httpservletresponse != null) && (httpservletresponse instanceof HttpServletResponseWrapper))
            result = true;

        return result;
    }
}
