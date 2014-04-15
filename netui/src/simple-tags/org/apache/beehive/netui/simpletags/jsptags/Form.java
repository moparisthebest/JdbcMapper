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
import org.apache.beehive.netui.simpletags.behaviors.FormBehavior;

import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import java.io.IOException;

/**
 * This tag represents an input form, associated with a bean whose
 * properties correspond to the various fields of the form.
 * @jsptagref.tagdescription Renders an HTML form that can be submitted to a Java method
 * in the Controller file for processesing.
 *
 * <p><b>Submitting Data</b></p>
 *
 * <p>When a &lt;netui:form> is submitted, the form data is passed to a method
 * for processessing.  The data is passed as a Form Bean instance.
 * The &lt;netui:form>'s input fields correspond to the properties of the Form Bean.
 * When the form is submitted the following sequence of events occurs:
 * (1) a new Form Bean instance is created, (2) the form data is loaded into the
 * corresponding Form Bean properties,
 * and (3) the Form Bean instance is passed to the method
 * where the data is processed.
 *
 * <p>The <code>action</code> attribute determines the target method of the submission.
 * The parameter of the target method determines the Form Bean instance
 * that carries the submitted data.
 *
 * <p>For example, if a &lt;netui:form>'s target method is <code>someAction</code> ...
 *
 * <pre>      &lt;netui:form action="<b>someAction</b>">
 *               //
 *               // input fields go here
 *               //
 *          &lt;netui:button value="Submit" type="submit"/>
 *      &lt;/netui:form></pre>
 *
 * <p>...and the <code>someAction</code> method takes a Form Bean parameter of
 * type <code>SomeFormBean</code>...
 *
 * <pre>     &#x40;Jpf.Action(
 *        forwards={
 *            &#x40;Jpf.Forward(name="success", path="showData.jsp")
 *        }
 *    )
 *    protected Forward someAction(<b>SomeFormBean form</b>)</pre>
 *
 * <p>...then an instance of <code>SomeFormBean</code> will carry the submitted data.
 *
 * <p><b>Pre-populating Form Fields with the Session Object</b></p>
 *
 * <p>The <code>name</code>, <code>type</code>, and <code>scope</code> attributes can
 * be used together to pre-populate
 * the form fields with default values when they are first rendered in the browser.
 *
 * <p>In the Controller file, instantiate the appropriate Form Bean, set default values, and
 * store the Form Bean instance in the Session object.
 *
 * <pre>    protected void onCreate()
 *    {
 *      // Create a new Form Bean instance
 *      ProcessDataForm formInstance = new ProcessDataForm();
 *
 *      // Set default values.
 *      formInstance.setAge(32);
 *      formInstance.setName("John");
 *
 *      // Store the instance in the Session object.
 *      getSession().setAttribute("defaultValues", formInstance);
 *    }</pre>
 *
 * <p>Then, use the <code>name</code>, <code>type</code>, and <code>scope</code> attributes to pre-populate the
 * form fields.
 *
 * <pre>    &lt;netui:form
 *        action="processData"
 *        name="defaultValues"
 *        type="tagSamples.netui.form.FormController$ProcessDataForm"
 *        scope="session"></pre>
 *
 * <p><b>Note:</b> when the data is submitted, the data is passed as a Request-scoped Form
 * Bean, *not* as the Session-scoped Form Bean used to pre-populate the fields.  However, you
 * may pass the data as a Page Flow-scoped Form Bean, if the annotation
 * <code>{@link org.apache.beehive.netui.pageflow.annotations.Jpf.Action &#64;Jpf.Action}(
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Action#useFormBean useFormBean}="someFormBeanMemberVariable"
 * </code>...<code>)</code> is set on the receiving method.
 *
 * <p><b>Pre-populating Form Fields By Passing a Form Bean Instance to the JSP Page</b></p>
 *
 * <p>As an alternative to the pre-population technique above, you can set the
 * pre-population values in a Form Bean instance and then pass that instance to the JSP page.
 * For example, assume that index.jsp contains the &lt;netui:form> and input elements.
 * The following action method sets the pre-population values in a Form Bean instance
 * and passes that instance to the
 * &lt;netui:form> and its input elements.
 * Note that the Forward object returned by the method has two parameters, the String "success"
 * and the pre-populated form.
 *
 * <pre>     &#x40;Jpf.Action(
 *        forwards={
 *            &#x40;Jpf.Forward(name="success", path="index.jsp")
 *        }
 *    )
 *  protected Forward begin(ProcessDataForm form)
 *  {
 *      form.setAge(44);
 *      form.setName("Mark");
 *      return new Forward("success", form);
 *  }</pre>
 * @example In this first sample, the
 * &lt;netui:form> tag invokes the <code>processData</code>
 * action method in the Controller file when the form is submitted.
 * <pre>      &lt;netui:form action="processData">
 *              Name:
 *              &lt;netui:textBox dataSource="actionForm.name"/>
 *              Age:
 *              &lt;netui:textBox dataSource="actionForm.age"/>
 *              &lt;netui:button value="Submit" type="submit"/>
 *      &lt;/netui:form></pre>
 *
 * <p> Notice that the processData action method takes a parameter of
 * type <code>ProcessDataForm</code>.</p>
 * <pre>     &#x40;Jpf.Action(
 *        forwards={
 *            &#x40;Jpf.Forward(name="success", path="showData.jsp")
 *        }
 *    )
 *    protected Forward processData(ProcessDataForm form)
 *    {
 *        //
 *        // Process the submitted data here.
 *        //
 *
 *        return new Forward("success");
 *    }</pre>
 *
 * <p>This means that the submitted data is loaded into an
 * instance of ProcessDataForm before
 * it is passed to the action method.
 *
 * <p>In this next sample, the form fields are pre-populated based upon default values
 * stored in the Session object.
 * <pre>
 *      &lt;netui:form action="Action" type="corp.Controller$NameBean"
 *          scope="session" name="nameBean">
 *          Name: &lt;netui:textBox dataSource="actionForm.name" />
 *          &lt;netui:button value="Submit"/>
 *      &lt;/netui:form></pre>
 * @netui:tag name="form" body-content="scriptless" dynamic-attributes="true" description="Represents an input form, associated with a bean whose properties correspond to the various fields of the form."
 */
public class Form extends HtmlBaseTag
{
    public Form() {
        _behavior = new FormBehavior();
    }

    /**
     * Return the action of the Form.
     * @return a String representing the action name of the Form.
     */
    public String getAction()
    {
        return ((FormBehavior) _behavior).getAction();
    }

    /**
     * Set the name of the action for the Form.
     * @param action the name of the action to set for the Form.
     * @jsptagref.attributedescription Required. The action method invoked on form submit.  Form data is passed to this method.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_action</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="The action method invoked on form submit.  Form data is passed to this method."
     */
    public void setAction(String action)
            throws JspException
    {
        ((FormBehavior) _behavior).setAction(action);
    }

    /**
     * Set the content encoding to be used on a post submit.
     * @param enctype the content encoding type.
     * @jsptagref.attributedescription The content encoding to be used on a POST submit.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_enctype</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The content encoding to be used on a POST submit."
     */
    public void setEnctype(String enctype)
    {
        ((FormBehavior) _behavior).setEnctype(enctype);
    }

    /**
     * Set the name of the field to receive focus.
     * @param focus the focus field name.
     * @jsptagref.attributedescription The <code>tagID</code> of an input field which should receive initial focus.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_focus</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The tagID of an input field which should receive initial focus."
     */
    public void setFocus(String focus)
    {
        ((FormBehavior) _behavior).setFocus(setNonEmptyValueAttribute(focus));
    }

    /**
     * Get the focus flag.
     * @return <code>true</code> if the focus has been set.  <code>false</code> otherwise.
     */
    public boolean isFocusSet() {
        return ((FormBehavior) _behavior).isFocusSet();
    }

    /**
     * Set the location hash to append to the url.
     * @param location the location hash
     * @jsptagref.attributedescription The location hash to append to the URL.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_location</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The location hash to append to the URL."
     */
    public void setLocation(String location)
    {
        ((FormBehavior) _behavior).setLocation(location);
    }

    /**
     * Return the value of the location.
     * @return  The value of the location.
     */
    public String getLocation() {
        return ((FormBehavior) _behavior).getLocation();
    }

    /**
     * Set the request method used when submitting this form.
     * @param method the request method
     * @jsptagref.attributedescription The request method used when submitting this form.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_method</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The request method used when submitting this form."
     */
    public void setMethod(String method)
            throws JspException
    {
        ((FormBehavior) _behavior).setMethod(method);
    }

    /**
     * Set the attribute key under which our associated bean is stored.
     * @param name the attribute key name
     * @jsptagref.attributedescription The attribute key under which the associated Form Bean used to populate the input form is stored.
     * This Form Bean is found in the scope defined by the <code>scope</code> attribute.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The attribute key under which the associated Form Bean used to populate the input form is stored.
     * This Form Bean is found in the scope defined by the scope attribute."
     */
    public void setBeanName(String name)
            throws JspException
    {
        ((FormBehavior) _behavior).setBeanName(name);
    }

    /**
     * Set the target "scope" for the form's action.  Multiple active page flows may exist concurrently within named
     * scopes.  This attribute selects which named scope to use.  If omitted, the default scope is assumed.
     * @param targetScope the name of the target scope in which the associated action's page flow resides.
     * @jsptagref.attributedescription The target scope in which the associated action's page flow resides.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_targetScope</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The target scope in which the associated action's page flow resides"
     */
    public void setTargetScope(String targetScope)
    {
        ((FormBehavior) _behavior).setTargetScope(targetScope);
    }

    /**
     * Sets the onReset javascript event.
     * @param onReset the onReset event.
     * @jsptagref.attributedescription The JavaScript onReset event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onSubmit</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The JavaScript onReset event."
     */
    public void setOnReset(String onReset)
    {
        ((FormBehavior) _behavior).setOnReset(onReset);
    }

    /**
     * Sets the onSubmit javascript event.
     * @param onSubmit the onReset event.
     * @jsptagref.attributedescription The JavaScript onSubmit event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onSumbit</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The JavaScript onSubmit event."
     */
    public void setOnSubmit(String onSubmit)
    {
        ((FormBehavior) _behavior).setOnSubmit(onSubmit);
    }

    /**
     * Sets the scope (request or session) under which the associated bean
     * is stored.
     * @param scope the scope.
     * @jsptagref.attributedescription The scope (<code>request</code> or <code>session</code>) under which the associated Form Bean
     * used to populate the form input fields is stored.
     * Using the <code>name</code>, <code>type</code>  and <code>scope</code> attributes defines
     * the Form Bean used.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_scope</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The scope (request or session) under which the associated Form Bean
     * used to populate the form input fields is stored.
     * Using the name, type  and scope attributes defines
     * the Form Bean used."
     */
    public void setBeanScope(String scope)
            throws JspException
    {
        ((FormBehavior) _behavior).setBeanScope(scope);
    }

    /**
     * Sets the window target.
     * @param target the window target.
     * @jsptagref.attributedescription The window target
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_windowTarget</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The window target"
     */
    public void setTarget(String target)
    {
        ((FormBehavior) _behavior).setTarget(target);
    }

    /**
     * Sets the Java class name of the bean to be created, if necessary.
     * @param type the class name
     * @jsptagref.attributedescription The Java class name of the Form Bean to be created, if necessary.
     * This Form Bean will be created if the <code>name</code> and <code>scope</code> attributes are set.
     * The Form Bean is then used to populate the form input fields.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_type</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The Java class name of the Form Bean to be created, if necessary.
     * This Form Bean will be created if the name and scope attributes are set.
     * The Form Bean is then used to populate the form input fields."
     */
    public void setBeanType(String type)
            throws JspException
    {
        ((FormBehavior) _behavior).setBeanType(type);
    }

    /**
     * Generate the JavaScript which will submit a form.  This is usually generated if the
     * form contains anchors that are used to submit the form.  This will make sure the JavaScript is
     * always generated.
     * @param formSubmit boolean value indicating that the form submit JavaScript should be generated.  Default is false.
     * @jsptagref.attributedescription Generate the form submit JavaScript even if the form does not
     * contain anchors. Default is <code>false</code>.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_formSubmit</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Generate the form submit JavaScript even if the form does not contain anchors."
     */
    public void setGenJavaScriptFormSubmit(boolean formSubmit)
    {
        ((FormBehavior) _behavior).setGenJavaScriptFormSubmit(formSubmit);
    }

    //********************************************* Do the Work  *******************************************************
    /**
     * Render the beginning of this form.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag() throws JspException, IOException
    {
        _behavior.start();

        // The form requires the pageContext...
        ((FormBehavior) _behavior).setPageContext(getPageContext());

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
