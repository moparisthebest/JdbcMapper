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
package getTemplates;

import org.apache.beehive.netui.core.urltemplates.URLTemplate;
import org.apache.beehive.netui.core.urltemplates.URLTemplatesFactory;
import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.util.ArrayList;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    public String getTemplateData() {
        StringBuilder results = new StringBuilder(256);
        URLTemplatesFactory factory = URLTemplatesFactory.getURLTemplatesFactory(getServletContext());
        if (factory != null) {
            URLTemplate[] templates = factory.getURLTemplates();
            if (templates != null) {
                ArrayList names = new ArrayList();
                for (int i = 0; i < templates.length; i++) {
                    names.add(templates[i].getName());
                }
                java.util.Collections.sort(names);
                for (int i = 0; i < names.size(); i++) {
                    String name = (String) names.get(i);
                    assert name != null;
                    URLTemplate template = factory.getURLTemplate(name);
                    assert name.equals(template.getName());
                    results.append(name);
                    results.append(" = ");
                    results.append(template.getTemplate());
                    results.append("\n");
                }
            }
        }

        return results.toString();
    }
}
