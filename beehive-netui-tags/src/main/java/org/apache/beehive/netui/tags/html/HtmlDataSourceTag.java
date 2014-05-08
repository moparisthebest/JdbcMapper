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

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.tags.ByRef;
import org.apache.beehive.netui.tags.ExpressionHandling;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlControlState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.util.List;

/**
 * Abstract Base class adding support for the <code>dataSource</code> attribute.
 */
abstract public class HtmlDataSourceTag
        extends HtmlFocusBaseTag
{
    protected String _dataSource;

    public HtmlDataSourceTag()
    {
        super();
    }

    /**
     * Sets the tag's data source (can be an expression).
     * @param dataSource the data source
     * @jsptagref.attributedescription <p>The <code>dataSource</code> attribute determines both
     * (1) the source of populating data for the tag and
     * (2) the object to which the tag submits data.
     *
     * <p>For example, assume that the Controller file (= JPF file) contains
     * a Form Bean with the property foo.  Then the following &lt;netui:textBox> tag will
     * (1) draw populating data from the Form Bean's foo property and (2)
     * submit user defined data to the same property.
     *
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;netui:textBox dataSource="actionForm.foo" /></code>
     *
     * <p>The <code>dataSource</code> attribute takes either a data binding expression or
     * the name of a Form Bean property.  In the
     * above example, <code>&lt;netui:textBox dataSource="foo" /></code>
     * would have the exactly same behavior.
     *
     * <p>When the tag is used to submit data, the data binding expression must
     * refer to a Form Bean property.
     * In cases where the tag is not used to submit data, but is used for
     * displaying data only, the data
     * binding expression need not refer to a Form Bean property.  For example,
     * assume that myIterativeData is a member variable on
     * the Controller file ( = JPF file).  The following &lt;netui-data:repeater>
     * tag draws its data from myIterativeData.
     *
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;netui-data:repeater dataSource="pageFlow.myIterativeData"></code>
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>expression_datasource</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="The <code>dataSource</code> attribute determines both
     * the source of populating data for the tag and
     * the object to which the tag submits data."
     */
    public void setDataSource(String dataSource)
    {
        _dataSource = dataSource;
    }

    /**
     * Return an <code>ArrayList</code> which represents a chain of <code>INameInterceptor</code>
     * objects.  This method by default returns <code>null</code> and should be overridden
     * by objects that support naming.
     * @return an <code>ArrayList</code> that will contain <code>INameInterceptor</code> objects.
     */
    protected List getNamingChain()
    {
        return AbstractClassicTag.DefaultNamingChain;
    }

    /**
     * Return the Object that is represented by the specified data source.
     * @return Object
     * @throws JspException
     */
    protected Object evaluateDataSource()
            throws JspException
    {
        // @todo: at some point we need to switch the expression evaluation to not require '{'
        ExpressionHandling expr = new ExpressionHandling(this);
        String datasource = "{" + _dataSource + "}";
        String ds = expr.ensureValidExpression(datasource, "dataSource", "DataSourceError");
        if (ds == null)
            return null;

        // have a valid expression
        return expr.evaluateExpression(datasource, "dataSource", pageContext);
    }

    /**
     * This method will create the name of the form element (HTML Control) that has a name. The
     * <b>name</b> attribute represent the "control name" for the control.  This name is scoped
     * into the form element.  In addition, a control may have a <b>id</b> attribute which is
     * specified by setting the <b>tagId</b>.  These two value are set in this routine.  The name
     * is always the expression mapping the data to it's backing element and is conotrolled
     * by the optional naming chain provided by the tag.  The <b>tagId</b> specifies the <b>id</b>
     * attribute.  If this is present then we write out a JavaScript that allows mapping
     * the tagId set on the tag to both the real <b>id</b> value and also the <b>name</b> value.
     * The <b>id</b> is formed by passing the <b>tagId</b> to the URL rewritter service.
     * @param state      The tag state structure.  This contains both the name and id attributes.  The
     *                   id attribute should be set with the initial value from the tagId.
     * @param javaScript A ByRef element that will contain any JavaScript that should be written out
     *                   by the calling tag.  A value is returned only if tagId is set and there is not IScriptReporter
     *                   found.
     * @throws JspException Pass through the exception from applyNamingChain.
     */
    protected void nameHtmlControl(AbstractHtmlControlState state, ByRef javaScript)
            throws JspException
    {
        assert (javaScript != null) : "paramater 'javaScript' may not be null";
        assert (state != null) : "parameter 'state' may not be null";
        assert (_dataSource != null) : "dataSource is Null";

        // create the expression (name)
        String datasource = "{" + _dataSource + "}";
        state.name = applyNamingChain(datasource);

        Form parentForm = getNearestForm();
        String idScript = renderNameAndId((HttpServletRequest) pageContext.getRequest(), state, parentForm);
        if (idScript != null)
            javaScript.setRef(idScript);
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();
        _dataSource = null;
    }
}
