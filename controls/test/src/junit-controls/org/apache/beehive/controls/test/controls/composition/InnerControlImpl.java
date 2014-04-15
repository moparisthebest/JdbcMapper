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
package org.apache.beehive.controls.test.controls.composition;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.events.Client;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;

/*
 * A control impl to test control composition.
 * This control shall be instantiated by another control.
 */
@ControlImplementation
public class InnerControlImpl
    implements InnerControl, java.io.Serializable {

    @Context
    ControlBeanContext context;

    @Client
    Activity activity;

    @Client
    Action action;

    /*Gets property value from context*/
    public String getNameFromContext(){
        Identity identity = context.getControlPropertySet(InnerControl.Identity.class);
        return identity.name();
    }

    /*Gets property value from context*/
    public String getJobFromContext(){
        Identity identity = context.getControlPropertySet(InnerControl.Identity.class);
        return identity.job();
    }
    
    public void fireEvent(String eventSet, String eventName){
    	
    	if ((eventSet!=null)&&(eventName!=null)){
    	
    		if (eventSet.equalsIgnoreCase("Activity")){
    			if (eventName.equalsIgnoreCase("wakeup"))
    				activity.wakeup();
    			else if(eventName.equalsIgnoreCase("readMessage"))
    				activity.readMessage("message from nested control");
    			else if(eventName.equalsIgnoreCase("report"))
    				activity.report();    			
    		}
    		else if (eventSet.equalsIgnoreCase("Action")){
    		    	if (eventName.equalsIgnoreCase("shopping"))
		    		action.shopping(999.99d);
		    	else if(eventName.equalsIgnoreCase("doStuff"))
		    		action.doStuff("stuff to do");
    		}
    	}
    	
    }

    public void fireAllEvents(){
    	activity.wakeup();
        activity.readMessage("message from nested control");
        activity.report();
        action.shopping(999.99d);
        action.doStuff("stuff to do");
    }
}
