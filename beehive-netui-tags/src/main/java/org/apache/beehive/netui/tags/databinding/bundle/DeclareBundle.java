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
package org.apache.beehive.netui.tags.databinding.bundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TryCatchFinally;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.script.common.BundleMap;

/**
 * <p>Declares a {@link java.util.ResourceBundle java.util.ResourceBundle} as a source for displaying internationalized
 * messages.  The declared resource bundle is accessible using the <code>${bundle...}</code> data binding context.
 * The required <code>name</code> attribute specifies the identifier used to refer to the ResourceBundle in an expression.
 * For example:
 * <br/>
 * <pre>
 *     &lt;netui-data:declareBundle name="someMessages" bundlePath="com/foobar/resources/WebAppMessages"/>
 * </pre>
 * <p>
 * This tag declares a bundle that is referenced in a data binding expression as <code>${bundle.someMessages}</code>.
 * </p>
 * <p>
 * The bundle that is referenced depends on the
 * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/Locale.html" target="_blank">java.util.Locale</a>
 * specified. The resource bundle properties files that are accessed are located
 * in the package <code>com/foobar/resources</code> with the root properties file
 * name of <code>WebAppMessages</code>. The naming conventions for properties can
 * be found in Sun's Java documentation at
 * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/ResourceBundle.html#getBundle(java.lang.String, java.util.Locale, java.lang.ClassLoader)" target="_blank">ResourceBundle.getBundle(String,Locale, ClassLoader)</a> .
 * These files must be located in a classpath that is available to the web application. Often, they are stored in
 * <code>WEB-INF/classes</code>.  If the properties file contains a key called <code>helloWorld</code>, then the
 * expression <code>${bundle.someMessages.helloWorld}</code> would look-up the message
 * matching the Locale specified on the tag. Bundle binding expressions can be
 * used in any data bindable &lt;netui...> tag attribute.
 * </p>
 * <p>It is possible to have keys that contain multiple words separated by spaces, commas, or periods. If this is the
 * case, then you must use slightly differente syntax to reference those keys in your data binding statement. The
 * following list illustrates three ways to access the key <code>My helloWorld</code> from the
 * <code>someMessages</code> property file:
 * </p>
 * <ul>
 * <li><code>&lt;netui:span value=&quot;${bundle.someMessages['My helloWorld']}&quot;/&gt;</code></li>
 * <li><code>&lt;netui:span value='${bundle.someMessages[&quot;My helloWorld&quot;]}'/&gt;</code></li>
 * <li><code>&lt;netui:span value=&quot;${bundle.someMessages[\&quot;My helloWorld\&quot;]}&quot;/&gt;</code></li>
 * </ul>
 * <p>
 * <b>Note:</b> the name <code>default</code> is a bundle identifier that is reserved for use by the
 * &lt;netui-data:declareBundle> tag.  If this value is used for the <code>name</code> attribute on a
 * &lt;netui-data:declareBundle> tag, an error will be reported in the page.  The <code>default</code> bundle
 * is reserved for use when accessing internationalized messages from the "current" Struts module's
 * default properties file.</p>
 * <p/>
 * <p>
 * This tag provides a high level of customizability for rendering internationalized messages.  Specifically, the
 * {@link Locale} for which to look-up messages can be specified on the &lt;netui-data:declareBundle>
 * tag.  By default, the Locale for the current request is used, but this Locale can be overridden by
 * setting the <code>language</code>, <code>country</code>, and <code>variant</code> tag attributes
 * as necessary.  See java.util.Locale for more information on the possible values for these attributes.</p>
 * <p>The Locale can be overridden by setting these attributes in three combinations:</p>
 * <blockquote>
 * <ul>
 * <li>country, language, variant</li>
 * <li>country, language</li>
 * <li>country</li>
 * </ul>
 * </blockquote>
 * Any other combinations will throw an exception.
 * </p>
 * <p>The &lt;netui-data:declareBundle> tag and the Struts &lt;i18n:getMessage> tags have the following differences.
 * The &lt;netui-data:declareBundle> tag lets you customize the use of a particular resource bundle with attributes
 * to set the country, language, and variant explicitly, but it does not write a message out to the JSP page.
 * Writing out a message from this bundle is done inside of any of the other tags using the <code>bundle</code>
 * JSP EL implicit object.
 * <p/>
 * <pre>
 *     &lt;netui:span value="<b>${bundle.messages.messageKey}</b>"/>
 * </pre>
 * <p/>
 * <p>The Struts &lt;i18n:getMessage> tag is used to access a bundle <i>and</i> write the message out. It
 * is roughly equivalent to doing the following:
 * <p/>
 * <pre>
 *     &lt;netui-data:declareBundle bundlePath="com/foobar/resources/messages" name="messages"/>
 *     &lt;netui:span value="${bundle.messages.messageKey}"/></pre>
 * <p/>
 * <p>An advantage of using the ${bundle...} data binding context, is that it lets you write into the
 * &lt;span>...&lt;/span> that the &lt;netui:span> creates, or into a &lt;neuti:checkBox> name, etc.
 * just like using a regular String.
 *
 * @jsptagref.tagdescription
 * <p>Declares a {@link java.util.ResourceBundle java.util.ResourceBundle} as a source for displaying internationalized
 * messages.  The declared resource bundle is accessible using the <code>${bundle...}</code> data binding context.
 * The required <code>name</code> attribute specifies the identifier used to refer to the ResourceBundle in an expression.
 * For example:
 * <br/>
 * <pre>
 *     &lt;netui-data:declareBundle name="someMessages" bundlePath="com/foobar/resources/WebAppMessages"/>
 * </pre>
 * <p>
 * This tag declares a bundle that is referenced in a data binding expression as <code>${bundle.someMessages}</code>.
 * </p>
 * <p>
 * The bundle that is referenced depends on the
 * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/Locale.html" target="_blank">java.util.Locale</a>
 * specified. The resource bundle properties files that are accessed are located
 * in the package <code>com/foobar/resources</code> with the root properties file
 * name of <code>WebAppMessages</code>. The naming conventions for properties can
 * be found in Sun's Java documentation at
 * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/ResourceBundle.html#getBundle(java.lang.String, java.util.Locale, java.lang.ClassLoader)" target="_blank">ResourceBundle.getBundle(String,Locale, ClassLoader)</a> .
 * These files must be located in a classpath that is available to the web application. Often, they are stored in
 * <code>WEB-INF/classes</code>.  If the properties file contains a key called <code>helloWorld</code>, then the
 * expression <code>${bundle.someMessages.helloWorld}</code> would look-up the message
 * matching the Locale specified on the tag. Bundle binding expressions can be
 * used in any data bindable &lt;netui...> tag attribute.
 * </p>
 * <p>It is possible to have keys that contain multiple words separated by spaces,
 * commas, or periods. If this is the case, then you must use slightly different
 * syntax to reference those keys in your data binding statement. The following
 * list illustrates three ways to access the key <code>My helloWorld</code> from the <code>someMessages</code>
 * property file:
 * </p>
 * <ul>
 * <li><code>&lt;netui:span value=&quot;${bundle.someMessages['My helloWorld']}&quot;/&gt;</code></li>
 * <li><code>&lt;netui:span value='${bundle.someMessages[&quot;My helloWorld&quot;]}'/&gt;</code></li>
 * <li><code>&lt;netui:span value=&quot;${bundle.someMessages[\&quot;My helloWorld\&quot;]}&quot;/&gt;</code></li>
 * </ul>
 * <p>
 * <b>Note:</b> the name <code>default</code> is a bundle identifier that is reserved for use by the
 * &lt;netui-data:declareBundle> tag.  If this value is used for the <code>name</code> attribute on a
 * &lt;netui-data:declareBundle> tag,
 * an error will be reported in the page.  The <code>default</code> bundle is reserved for use when
 * accessing internationalized messages from the "current" Struts module's default properties file.</p>
 * <p/>
 * <p>
 * This tag provides a high level of customizability for rendering internationalized messages.
 * Specifically, the Locale for which to look-up messages can be specified on the
 * &lt;netui-data:declareBundle>
 * tag.  By default, the Locale for the current request is used, but this Locale can be overridden by
 * setting the <code>language</code>, <code>country</code>, and <code>variant</code> tag attributes
 * as necessary.  See java.util.Locale for more information on the possible values for these attributes.</p>
 * <p>The Locale can be overridden by setting these attributes in three combinations:</p>
 * <blockquote>
 * <ul>
 * <li>country, language, variant</li>
 * <li>country, language</li>
 * <li>country</li>
 * </ul>
 * </blockquote>
 * Any other combinations will throw an exception.
 * </p>
 * <p>The &lt;netui-data:declareBundle> tag and the Struts &lt;i18n:getMessage> tags have the following differences.
 * The &lt;netui-data:declareBundle> tag lets you customize the use of a particular resource bundle with attributes
 * to set the country, language, and variant explicitly, but it does not write a message out to the JSP page.
 * Writing out a message from this bundle is done inside of any of the other tags using the <code>bundle</code>
 * JSP EL implicit object.
 * <p/>
 * <pre>    &lt;netui:span value="<b>${bundle.messages.messageKey}</b>"/></pre>
 * <p/>
 * <p>The Struts &lt;i18n:getMessage> tag is used to access a bundle
 * <i>and</i> write the message out. It is roughly equivalent to doing the following:
 * <p/>
 * <pre>
 *     &lt;netui-data:declareBundle bundlePath="com/foobar/resources/messages" name="messages"/>
 *     &lt;netui:span value="${bundle.messages.messageKey}"/></pre>
 * <p/>
 * <p>An advantage of using the ${bundle...} data binding context, is that it lets you write into the
 * &lt;span>...&lt;/span> that the &lt;netui:span> creates, or into a &lt;neuti:checkBox> name, etc.
 * just like using a regular String.
 *
 * @netui:tag name="declareBundle"
 *            description="Use this tag to declare a resource bundle that is available in the bundle databinding context"
 */
public class DeclareBundle
    extends AbstractClassicTag
    implements TryCatchFinally {

    private String _name = null;
    private String _bundlePath = null;
    private String _language = null;
    private String _country = null;
    private String _variant = null;

    /**
     * Get the name of this tag.  This is used to identify the type of this tag
     * for reporting tag errors.
     *
     * @return a constant String representing the name of this tag.
     */
    public String getTagName() {
        return "DeclareBundle";
    }

    /**
     * Set the language to use when looking-up resource bundle messages.
     * The two-letter lowercase ISO-639 language code for the Locale from which to look-up resource bundle messages.
     * This value is used to further specify the name of the .properties file from which message keys will be read.
     *
     * @param language the two-letter lowercase ISO-639 code for a language.
     * @jsptagref.attributedescription
     * Set the language to use when looking-up resource bundle messages.
     * The two-letter lowercase ISO-639 language code for the Locale from which to look-up resource bundle messages.
     * This value is used to further specify the name of the .properties file from which message keys will be read.
     * @jsptagref.attributesyntaxvalue <i>string_language</i>
     * @netui:attribute required="false" rtexprvalue="true"
     */
    public void setLanguage(String language) {
        _language = language;
    }

    /**
     * Set the country to use when looking-up resource bundle messages.  The two-letter uppercase ISO-3166
     * country / region code for the Locale from which to look-up resource bundle messages.  This value is used to
     * further specify the name of the .properties file from which message keys will be read.
     *
     * @param country the two-letter uppercase ISO-3166 code for a country
     * @jsptagref.attributedescription
     * Set the country to use when looking-up resource bundle messages.  The two-letter uppercase ISO-3166
     * country / region code for the Locale from which to look-up resource bundle messages.  This value is used to
     * further specify the name of the .properties file from which message keys will be read.
     * @jsptagref.attributesyntaxvalue <i>string_country</i>
     * @netui:attribute required="false" rtexprvalue="true"
     */
    public void setCountry(String country) {
        _country = country;
    }

    /**
     * Sets a vendor / browser specific code for further parameterizign the Locale from which to look-up resource bundle messages.
     *
     * @param variant the variant
     * @jsptagref.attributedescription
     * Sets a vendor / browser specific code for further parameterizign the Locale from which to look-up resource bundle messages.
     * @jsptagref.attributesyntaxvalue <i>string_variant</i>
     * @netui:attribute required="false" rtexprvalue="true"
     */
    public void setVariant(String variant) {
        _variant = variant;
    }

    /**
     * The name inside of the ${bundle...} databinding context under which the properties in this bundle are available.
     * The identifier <code>default</code> is an illegal value for this attribute and is reserved for use by this tag.
     *
     * @param name the name of the bundle
     * @jsptagref.attributedescription
     * The name inside of the ${bundle...} databinding context under which the properties in this bundle are available.
     * The identifier <code>default</code> is an illegal value for this attribute and is reserved for use by this tag.
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     * @netui:attribute required="true"
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Set the path to the resource bundle's properties files.  This can be a slash or dot delimited classpath.
     * Valid values should appear as:
     * <blockquote>
     * <ul>
     * <li>com/foobar/resources/WebAppProperties</li>
     * <li>com.foobar.resources.WebAppProperties</li>
     * </ul>
     * </blockquote>
     * <p>These are treated as equivalent values.  The {@link java.util.ResourceBundle} class
     * will handle appending the <code>.properties</code> file type and locale information
     * as necessary.  See {@link  java.util.ResourceBundle#getBundle(String, Locale, ClassLoader)}
     * for the <code>.properties</code> file naming conventions.  These files must be available in
     * classpath for the webapp in order to be successfully located.
     *
     * @param bundlePath the path to the bundle's properties files.
     * @jsptagref.attributedescription
     * Set the path to the resource bundle's properties files.  This can be a slash or dot delimited classpath.
     * Valid values should appear as:
     * <blockquote>
     * <ul>
     * <li>com/foobar/resources/WebAppProperties</li>
     * <li>com.foobar.resources.WebAppProperties</li>
     * </ul>
     * </blockquote>
     * <p>These are treated as equivalent values.  The {@link java.util.ResourceBundle} class
     * will handle appending the <code>.properties</code> file type and locale information
     * as necessary.  See {@link  java.util.ResourceBundle#getBundle(String, Locale, ClassLoader)}
     * for the <code>.properties</code> file naming conventions.  These files must be available in
     * classpath for the webapp in order to be successfully located.
     * @jsptagref.attributesyntaxvalue <i>string_bundlePath</i>
     * @netui:attribute required="true"
     */
    public void setBundlePath(String bundlePath) {
        _bundlePath = bundlePath;
    }

    /**
     * Start the JSP rendering lifecycle for this tag; it skips its body.
     *
     * @return {@link #SKIP_BODY}
     */
    public int doStartTag() {
        return SKIP_BODY;
    }

    /**
     * Register a ResourceBundle that is available for the scope of this page.  Errors raised
     * during the execution of this tag will be reported in the page, which will continue
     * rendering.
     *
     * @return {@link #EVAL_PAGE}
     * @throws JspException if error conditions are encountered during this method which can not
     *                      be reported in the page.
     */
    public int doEndTag()
            throws JspException {

        if(_name.length() == 0) {
            String msg = Bundle.getErrorString("Tags_DeclareBundle_invalidName", new Object[]{_name});
            registerTagError(msg, null);
        }

        if(_name.equals(BundleMap.DEFAULT_STRUTS_BUNDLE_NAME)) {
            String msg = Bundle.getErrorString("Tags_DeclareBundle_defaultIsReservedWord", null);
            registerTagError(msg, null);
        }

        if(_bundlePath.length() == 0) {
            String msg = Bundle.getErrorString("Tags_DeclareBundle_invalidResourcePath", new Object[]{_bundlePath});
            registerTagError(msg, null);
        }

        Locale locale = getCurrentLocale();

        if(hasErrors()) {
            reportErrors();
            return EVAL_PAGE;
        }

        BundleMap bundleMap = null;
        Object obj = pageContext.getAttribute("bundle");
        if(obj == null) {
            // NetUI v2 -- in JSP 2.0 EL, the BundleMap is dropped into the PageContext so that
            // it's available to the EL runtime.
            bundleMap = new BundleMap((HttpServletRequest)pageContext.getRequest(), pageContext.getServletContext());
            pageContext.setAttribute("bundle", bundleMap);
        }
        else if(!(obj instanceof BundleMap)) {
            String msg = Bundle.getErrorString("Tags_DeclareBundle_wrongContextType", new Object[]{obj.getClass().getName()});
            registerTagError(msg, null);
            if(hasErrors())
                reportErrors();
            return EVAL_PAGE;
        }
        else bundleMap = (BundleMap)obj;

        bundleMap.registerResourceBundle(_name, _bundlePath, locale);

        return EVAL_PAGE;
    }

    public void doFinally() {
        localRelease();
    }

    public void doCatch(Throwable t)
            throws Throwable {
        throw t;
    }

    /**
     * Reset all of the fields of this tag.
     */
    protected void localRelease() {
        super.localRelease();
        _name = null;
        _bundlePath = null;
        _language = null;
        _country = null;
        _variant = null;
    }

    private Locale getCurrentLocale()
            throws JspException {
        if(_language == null && _country == null && _variant == null)
            return getUserLocale();

        if(hasErrors())
            return null;

        if(_language != null && _country != null && _variant != null)
            return new Locale(_language, _country, _variant);
        else if(_language != null && _country != null)
            return new Locale(_language, _country);
        else if(_language != null)
            return new Locale(_language);
        else if(_country != null || _variant != null) {
            String msg = Bundle.getErrorString("Tags_DeclareBundle_invalidLocaleOverride", new Object[]{_language, _country, _variant});
            registerTagError(msg, null);
        }

        return getUserLocale();
    }
}
