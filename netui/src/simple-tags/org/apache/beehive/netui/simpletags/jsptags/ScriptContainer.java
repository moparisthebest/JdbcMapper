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
package org.apache.beehive.netui.simpletags.jsptags;

import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.appender.ResponseAppender;
import org.apache.beehive.netui.simpletags.behaviors.ScriptContainerBehavior;

import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

/**
 * Acts as a container that will bundle up JavaScript created by other NetUI tags,
 * and output it within a single &lt;script> tag. This is especially needed for
 * Portal web applications, because they often cannot rely on having
 * &lt;html> ... &lt;/html> tags to provide a default container. In a portlet,
 * some JSP pages might be included into other JSP pages. Having redundant
 * &lt;html> ... &lt;/html> tags in the rendered portlet JSP can result in display
 * problems for some browsers. However, omitting the &lt;html> tag (and the
 * container it provides) can result in cluttered code, especially where Javascript
 * appears in the file. To solve this issue, Beehive provides the
 * &lt;netui:scriptContainer> tag.
 * 
 * @jsptagref.tagdescription Acts as a container that will bundle up JavaScript created by other &lt;netui...> tags,
 * and outputs it within a single &lt;script> tag. This is especially useful for
 * Portal web applications, because they often cannot rely on having
 * &lt;html> ... &lt;/html> tags to provide a default container. In a Portlet,
 * some JSP pages might be included in other JSP pages. Having redundant
 * &lt;html> ... &lt;/html> tags in the rendered Portlet JSP can result in display
 * problems for some browsers. On the other hand, omitting the &lt;html> tag (and the
 * container it provides) can result in cluttered code, especially where JavaScript
 * appears in the file. To solve this issue, Beehive provides the
 * &lt;netui:scriptContainer> tag.
 *
 * <p>The &lt;netui:scriptContainer> ... &lt;/netui:scriptContainer> tag set should
 * enclose those &lt;netui:...> tags that you want included in the script container.
 * The first &lt;netui:scriptContainer> tag should appear after the JSP's &lt;body> tag.
 * The closing &lt;/netui:scriptContainer> tag should appear before the JSP's &lt;/body> tag.
 * @example The &lt;netui:scriptContainer> ... &lt;/netui:scriptContainer tag set simply
 * encloses other NetUI tags that you want to belong to that script container.
 * The first &lt;netui:scriptContainer> tag should appear after the JSP's &lt;body> tag.
 * The closing &lt;/netui:scriptContainer> tag should appear before the JSP's &lt;/body> tag.
 * @netui:tag name="scriptContainer" body-content="scriptless" dynamic-attributes="true"  description="ScriptContainers defines a container that will gather all of the JavaScript of their children and output it in a single &lt;script> tag.  In addition, they providing scoping of tagIds."
 */
public class ScriptContainer extends AbstractSimpleTag
{
    public final static String SCOPE_ID = "netui:scopeId";

    public ScriptContainer()
    {
        _behavior = new ScriptContainerBehavior();
    }

    /////////////////////////// Attributes ////////////////////////////

    /**
     * Set the idScope associated with the code methods
     * @jsptagref.attributedescription The id that is associated with the script methods.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_scopeId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The id that is associated with the script methods."
     */
    public void setIdScope(String idScope)
    {
        ((ScriptContainerBehavior) _behavior).setIdScope(idScope);
    }

    /**
     * return the scopeId associated with the ScriptContainer
     */
    public String getIdScope()
    {
        return ((ScriptContainerBehavior) _behavior).getIdScope();
    }

    /**
     * If true generate a scope id for this script container.  If this is set to true
     * and a scopeId is also set, the scopeId set will be written.
     * @jsptagref.attributedescription Automatically generate a scopeId for this script container.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_generateScopeId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Automatically generate a ScopeId."
     */
    public void setGenerateIdScope(boolean genScopeValue)
    {
        ((ScriptContainerBehavior) _behavior).setGenerateIdScope(genScopeValue);
    }
    ///////////////////////////////// Tag Methods ////////////////////////////////////////

    public void doTag()
            throws JspException, java.io.IOException
    {
        _behavior.start();

        ServletResponse response = getPageContext().getResponse();
        Appender appender = new ResponseAppender(response);
        _behavior.preRender();
        _behavior.renderStart(appender);

        JspFragment frag = getJspBody();
        if (frag != null) {
            frag.invoke(response.getWriter());
        }

        _behavior.renderEnd(appender);
        _behavior.postRender();
        _behavior.terminate();
    }
}
