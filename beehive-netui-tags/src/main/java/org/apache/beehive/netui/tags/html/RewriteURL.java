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
package org.apache.beehive.netui.tags.html;

import org.apache.beehive.netui.core.urls.FreezableMutableURI;
import org.apache.beehive.netui.core.urls.URIContext;
import org.apache.beehive.netui.core.urls.URLType;
import org.apache.beehive.netui.core.urls.URLRewriterService;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.internal.URIContextFactory;
import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.ParamHelper;
import org.apache.beehive.netui.util.config.ConfigUtil;
import org.apache.beehive.netui.util.config.bean.UrlConfig;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Allow a URL to participate in rewritting.  Some containers rewrite URLs.
 * This tag will pass the URL attribute through the rewriters to generate a
 * rewritten URL before it is output into the HTML stream.
 * @jsptagref.tagdescription Allows a URL to participate in URL rewritting.
 * Some containers rewrite URLs. This tag will pass the URL attribute through
 * the rewriters to generate a rewritten URL before it is output into the HTML stream.
 * @example In this sample, the URL attribute will be rewritten and output within
 * the span tags.  The actual value that will be written to the rendered HTML may
 * change depending on the application container.
 * <pre>    &lt;span>URL: &lt;netui:rewriteURL URL="foo.do"/&gt;&lt;/span></pre>
 * @netui:tag name="rewriteURL" description="Allows the URL Rewriter to rewrite the url attribute before it is output into the HTML stream."
 */
public class RewriteURL extends AbstractClassicTag implements IUrlParams
{
    private Map _params;                         // Any parameters to the submit
    protected String url = null;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "RewriteURL";
    }

    /**
     * Sets the URL to be rewritten.
     * @param url the value of the URL that will be rewritten.
     * @jsptagref.attributedescription The value of the URL that will be rewritten.
     * @jsptagref.attributesyntaxvalue <i>string_url</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="The value of the URL that will be rewritten."
     */
    public void setURL(String url)
    {
        this.url = url;
    }

    /**
     * This method will allow a tag that produces one or more Urls to have parameters set
     * on the tag.  The name and value should be required.  The facet is optional, and
     * allows tags producing more than one URL to have parameters set on different URLs.
     *
     * @param name  The name of the parameter to be added to the URL.
     * @param value The value of the parameter.
     * @param facet The name of a facet for which the parameter should be added.
     * @throws javax.servlet.jsp.JspException
     */
    public void addParameter(String name, Object value, String facet)
            throws JspException
    {
        if (_params == null) {
            _params = new HashMap();
        }
        ParamHelper.addParam(_params, name, value);
    }

    /**
     * Render the beginning of the rewriteURL tag.
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        // Evaluate the body of this tag
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Render the end of the rewriteURL tag.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException
    {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        ServletContext context = pageContext.getServletContext();

        try {
            boolean encoded = false;
            UrlConfig urlConfig = ConfigUtil.getConfig().getUrlConfig();
            if (urlConfig != null) {
                encoded = !urlConfig.isUrlEncodeUrls();
            }

            FreezableMutableURI uri = new FreezableMutableURI();
            uri.setEncoding(response.getCharacterEncoding());
            uri.setURI(url, encoded);
            boolean needsToBeSecure = false;
            if (_params != null) {
                uri.addParameters(_params, false );
            }
            if (!uri.isAbsolute() && PageFlowUtils.needsToBeSecure(context, request, url, true)) {
                needsToBeSecure = true;
            }

            URLRewriterService.rewriteURL(context, request, response, uri, URLType.ACTION, needsToBeSecure);
            String key = PageFlowUtils.getURLTemplateKey(URLType.ACTION, needsToBeSecure);
            boolean forXML = TagRenderingBase.Factory.isXHTML(request);
            URIContext uriContext = URIContextFactory.getInstance(forXML);
            String uriString = URLRewriterService.getTemplatedURL(context, request, uri, key, uriContext);
            write(response.encodeURL(uriString));
        }
        catch (URISyntaxException e) {
            // report the error...
            String s = Bundle.getString("Tags_RewriteURL_URLException",
                    new Object[]{url, e.getMessage()});
            registerTagError(s, e);
            reportErrors();
        }

        localRelease();
        return EVAL_PAGE;
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();
        _params = null;
        url = null;
    }

}
