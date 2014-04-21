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
package org.apache.beehive.netui.tags.databinding.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import javax.servlet.jsp.JspException;

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.type.TypeUtils;

/**
 * <p>
 * An abstract base class for tags that are capable of reflectively invoking methods.  Specializations of this
 * tag provide method implementations that locate the object on which to invoke the method and that handle
 * any return value from the invoked method.
 * <p/>
 * <p>
 * The <code>CallMethod</code> tag can have child tags of type {@link MethodParameter}; these tags must be in the same
 * order as the parameter list in the method signature of the method that will be invoked.  To invoke an overloaded
 * method, the {@link MethodParameter#setType(String)} property must be set to the String name of the type to pass
 * to the method.  If the type attribute values on nested {@link MethodParameter} tags do not match any method
 * signature, an error will be reported in the page.
 * </p>
 */
public abstract class AbstractCallMethod
    extends AbstractClassicTag {

    private static final Logger LOGGER = Logger.getInstance(AbstractCallMethod.class);

    private static final Object[] EMPTY_ARGS = new Object[0];
    private static final String EMPTY_STRING = "";

    private List _parameters = null;
    private String _method = null;
    private boolean _failOnError = true;
    private String _resultId = null;
    private boolean _verifyTypes = false;

    /**
     * Sets the identifier at which the result of invoking the method will stored.  Once stored, the
     * result of the reflective invocation will be available via the JSP EL implicit object
     * <code>${pageScope}</code> with the attribute name set via this property.
     *
     * @param resultId a String that names an attribute in the PageContext's
     *                 attribute map where any resulting object will be stored.
     * @jsptagref.attributedescription
     * Sets the identifier at which the result of invoking the method will stored.  Once stored, the
     * result of the reflective invocation will be available via the JSP EL implicit object
     * <code>${pageScope}</code> with the attribute name set via this property.
     * @netui:attribute required="false"
     */
    public void setResultId(String resultId) {
        _resultId = resultId;
    }

    /**
     * Sets whether or not to report exceptions to the page when errors occur invoking a method on an object.
     *
     * @param failOnError a boolean that defines whether or not exceptions should be thrown when invocation fails.
     * @jsptagref.attributedescription
     * Sets whether or not to report exceptions to the page when errors occur invoking a method on an object.
     * @netui:attribute required="false"
     */
    public void setFailOnError(boolean failOnError) {
        _failOnError = failOnError;
    }

    /**
     * Sets the name of a method to invoke on the target object.
     *
     * @param method the name of the method to invoke
     * @jsptagref.attributedescription
     * Sets the name of a method to invoke on the target object.
     * @netui:attribute required="true"
     */
    public void setMethod(String method) {
        _method = method;
    }

    /**
     * Add a paramter that will be passed as an argument to the method that will be invoked.  This method
     * is implemented to allow the the {@link MethodParameter} tags to register their parameters.  This
     * object is passed in the position that it appeared in the set of child {@link MethodParameter} tags.
     *
     * @param type      a String of the type or class name of this parameter
     * @param parameter an object that should be passed as an argument to the invoked method
     * @see MethodParameter
     */
    public void addParameter(String type, Object parameter) {
        if(_parameters == null)
            _parameters = new ArrayList();

        // only check the types if necessary
        if(type != null) _verifyTypes = true;

        ParamNode pn = new ParamNode();
        pn.typeName = type;
        pn.paramValue = parameter;

        _parameters.add(pn);
    }

    /**
     * Causes the body of this tag to be rendered; only {@link MethodParameter} tags are allowed to be
     * contained inside of this tag.  The output of rendering the body is never written into the
     * output stream of the servlet.
     *
     * @return {@link #EVAL_BODY_BUFFERED}
     */
    public int doStartTag()
            throws JspException {
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Reflectively invokes the method specified by the <code>method</code> attribute,
     * {@link #findMethod(Object, String, boolean)}.  The arguments passed to the method are taken from any nested
     * {@link MethodParameter} tags.  When the parameters which are added by the
     * {@link MethodParameter} tags are {@link java.lang.String} types, an attempt is made to convert each of
     * these parameters into the type expected by the method.  This conversion is done using the
     * {@link TypeUtils#convertToObject(java.lang.String, java.lang.Class)} method.  If a String can not
     * be converted do the type expected by the method, an exception is thrown and the error is reported
     * in the tag.  Any return value that results from invoking the given method is passed to the
     * subclass implementation of the method {@link #handleReturnValue(java.lang.Object)}.
     *
     * @return {@link #EVAL_PAGE} to continue evaluating the page
     * @throws JspException if there are errors.  All exceptions that may be thrown
     *                      in the process of reflectively invoking the method and performing type
     *                      conversion are reported as {@link JspException}
     * @see #findMethod(Object, String, boolean)
     * @see #handleReturnValue(java.lang.Object)
     * @see MethodParameter
     * @see ObjectNotFoundException
     * @see TypeUtils#convertToObject(java.lang.String, java.lang.Class)
     * @see java.lang.String
     */
    public int doEndTag()
        throws JspException {

        // find the object on which to invoke the method
        Object object = null;
        try {
            object = resolveObject();
        }
        catch(ObjectNotFoundException onf) {
            Throwable cause = (onf.getCause() != null ? onf.getCause() : onf);
            String msg = Bundle.getErrorString("Tags_AbstractCallMethod_noSuchObject", new Object[]{getObjectName(), _method, cause});
            registerTagError(msg, null);
        }

        // if this tag can accept null invocation targets, 
        if(object == null) {
            if(allowNullInvocationTarget()) {
                // each implementation does this on their own
                handleReturnValue(null);
                localRelease();
                return EVAL_PAGE;
            }
            else {
                String msg = Bundle.getErrorString("Tags_AbstractCallMethod_objectIsNull", new Object[]{getObjectName(), _method});
                registerTagError(msg, null);
            }
        }

        if(hasErrors()) {
            reportErrors();
            localRelease();
            return EVAL_PAGE;
        }

        Method m = findMethod(object, _method, _verifyTypes);

        if(m == null) {
            String msg = null;
            if(_verifyTypes) {
                String paramString = prettyPrintParameterTypes(_parameters);
                msg = Bundle.getErrorString("Tags_AbstractCallMethod_noSuchMethodWithTypes",
                        new Object[]{_method,
                                     (_parameters != null ? new Integer(_parameters.size()) : new Integer(0)),
                                     paramString,
                                     getObjectName()});
            }
            else
                msg = Bundle.getErrorString("Tags_AbstractCallMethod_noSuchMethod",
                        new Object[]{_method,
                                     (_parameters != null ? new Integer(_parameters.size()) : new Integer(0)),
                                     getObjectName()});

            registerTagError(msg, null);
            reportErrors();
            localRelease();
            return EVAL_PAGE;
        }

        Object[] args = null;
        try {
            args = getArguments(m.getParameterTypes());
        }
        catch(IllegalArgumentException iae) {
            registerTagError(iae.getMessage(), null);
            reportErrors();
            localRelease();
            return EVAL_PAGE;
        }
        
        // invoke method
        Object result = null;
        try {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("method: " + m.toString());
                for(int i = 0; i < args.length; i++)
                    LOGGER.debug("arg[" + i + "]: " + (args[i] != null ? args[i].getClass().getName() : "null"));
            }

            result = m.invoke(object, args);
        }
        catch(Exception e) {
            assert e instanceof IllegalAccessException || e instanceof InvocationTargetException || e instanceof IllegalArgumentException;

            if(LOGGER.isErrorEnabled())
                LOGGER.error("Could not invoke method \"" + _method + "\" on the object named \"" + getObjectName() + "\" because: " + e, e);

            if(_failOnError) {
                String msg = Bundle.getErrorString("Tags_AbstractCallMethod_invocationError", new Object[]{_method, getObjectName(), e});
                registerTagError(msg, null);
                reportErrors();
                localRelease();
                return EVAL_PAGE;
            }
        }

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug((result != null ?
                    "return value is non-null and is of type \"" + result.getClass().getName() + "\"" :
                    "return value is null."));
        }

        /* allow the tag to handle the return value */
        handleReturnValue(result);

        localRelease();

        return EVAL_PAGE;
    }

    /**
     * Reset all of the fields of this tag.
     */
    protected void localRelease() {
        super.localRelease();
        _parameters = null;
        _method = null;
        _failOnError = true;
        _resultId = null;
        _verifyTypes = false;
    }

    /**
     * <p/>
     * Resolve the object on which the method should be invoked.  If there are errors resolving this object,
     * this method will throw an {@link ObjectNotFoundException}.
     * </p>
     * <p>
     * If the object is not found but no exception occurred, this method returns <code>null</code>.
     * </p>
     *
     * @return the object on which to reflectively invoke the method or <code>null</code> if the
     *         object was not found and no exception occurred.
     * @throws ObjectNotFoundException if an exception occurred attempting to resolve an object
     */
    protected abstract Object resolveObject() throws ObjectNotFoundException, JspException;

    /**
     * The default findMethod implementation is an uncached search of all
     * of the methods available on the Class of the <code>target</code>
     *
     * @param target      the object from which to find the method
     * @param methodName  the name of the method to find
     * @param verifyTypes a boolean that if true will match the type names in addition to the String method name
     * @return a Method object matching the methodName and types, if <code>verifyTypes</code> is true.
     *         <code>null</code> otherwise.
     */
    protected abstract Method findMethod(Object target, String methodName, boolean verifyTypes);

    /**
     * Get the name of the object that is the target of the invocation.  This is a generic method for this
     * tag that enables more specific error reporting.
     *
     * @return a name for the object on which the method will be invoked.
     */
    protected abstract String getObjectName();

    /**
     * When implemented to return true, this method allows a tag invoking a method to
     * accept a null invocation target and simply return null.  The default
     * implementation returns false.
     *
     * @return true if the object on which to invoke the method can be null; false otherwise.
     */
    protected boolean allowNullInvocationTarget() {
        return false;
    }

    /**
     * <p/>
     * A method that allows concrete classes to handle the result of the
     * reflective invocation in an implementation specific way.
     * </p>
     * <p/>
     * The default beahavior is to set the return value resulting from invoking the method
     * in the {@link javax.servlet.jsp.PageContext} attribute map of the current JSP page.
     * The result is set as an attribute if the <code>result</code> is not null and the
     * {@link CallMethod#setResultId(java.lang.String)} String is not null.  If the value returned
     * from calling a method is null and the {@link CallMethod#setResultId(java.lang.String)} is non-null,
     * the {@link javax.servlet.jsp.PageContext#removeAttribute(java.lang.String)}
     * is called to remove the attribute from the attribute map.
     * </p>
     *
     * @param result the object that was returned by calling the method on the object
     */
    protected void handleReturnValue(Object result) {
        if(_resultId != null) {
            if(result != null) {
                if(LOGGER.isInfoEnabled() && pageContext.getAttribute(_resultId) != null)
                    LOGGER.info("Overwriting attribute named \"" + _resultId + "\" in the PageContext with a new attribute of type \"" +
                            result.getClass().getName() + "\" returned from calling the method \"" + _method + "\" on an object named \"" +
                            getObjectName() + "\".");

                pageContext.setAttribute(_resultId, result);
            }
            else {
                if(LOGGER.isInfoEnabled())
                    LOGGER.info("Removing attribute named \"" + _resultId + "\" from the PageContext.  " +
                            "The value returned from calling the method \"" + _method + "\" on an object named \"" +
                            getObjectName() + "\" is null.");

                pageContext.removeAttribute(_resultId);
            }
        }
    }

    /**
     * Internal, read-only property used by subclasses to get
     * the list of parameters to be used when reflectively
     * invoking a method.  If the method takes no parameters, this
     * list will be of size zero.
     *
     * @return the list of parameters
     */
    protected List getParameterNodes() {
        return _parameters != null ? _parameters : Collections.EMPTY_LIST;
    }

    /**
     * Convert the arguments for a method from Strings set as attributes
     * on JSP tags to the types represented by teh list of Class[] objects
     * provided here.
     *
     * @return an Object[] that contains the parameters to pass to the method
     * @throws IllegalArgumentException if an error occurs converting an
     *                                  argument to a specific type.
     */
    private Object[] getArguments(Class[] paramTypes) {
        if(_parameters == null)
            return EMPTY_ARGS;

        Object[] args = new Object[paramTypes.length];

        for(int i = 0; i < _parameters.size(); i++) {
            ParamNode pn = (ParamNode)_parameters.get(i);

            if(LOGGER.isDebugEnabled())
                LOGGER.debug("argTypes[" + i + "]: " + paramTypes[i]);

            /* if the parameter should have been null, leave it null */
            if(pn.paramValue == MethodParameter.NULL_ARG)
                continue;

            Object value = pn.paramValue;
            try {
                /* if the value wasn't a String, assume it was referenced via an expression language
                   and is already of the correct type */
                if(!(value instanceof String) || value == null)
                    args[i] = value;
                /* convert a non-null String value using the registered type converters */
                else args[i] = TypeUtils.convertToObject((String)value, paramTypes[i]);
            }
            /* catch Exception here because almost anything can be thrown by TypeUtils.convertToObject(). */
            catch(Exception e) {
                String msg =
                    Bundle.getErrorString("Tags_AbstractCallMethod_parameterError",
                        new Object[]{paramTypes[i], new Integer(i), value, e.toString()});
                throw new IllegalArgumentException(msg);
            }
        }

        return args;
    }

    /**
     * Utility method that pretty-prints the types of the parameters
     * passed to a method; this is used in debugging.
     *
     * @param parameters the list of parameters
     * @return a String that represents the types of each of these paramters in order
     */
    private static String prettyPrintParameterTypes(List parameters) {
        InternalStringBuilder paramString;
        if(parameters != null && parameters.size() > 0) {
            paramString = new InternalStringBuilder(128);
            paramString.append("(");
            for(int i = 0; i < parameters.size(); i++) {
                if(i > 0)
                    paramString.append(", ");
                paramString.append(((ParamNode)parameters.get(i)).typeName);
            }
            paramString.append(")");

            return paramString.toString();
        }
        else return "";
    }

    /**
     * An internal struct that represents a parameter that will be passed to a
     * reflective method invocation call.  Instances of <code>ParamNode</code>
     * map 1:1 to the methodParameter tags that appear within the body of
     * an AbstrctCallMethod tag.
     *
     * @exclude
     */
    protected class ParamNode {
        /**
         * The fully qualified class name of the parameter type.  This value
         * can be null if parameter type checking does not need to occur.
         */
        String typeName = null;

        /**
         * The value of the parameter.  Often, this is a String expression
         * which is evaluated later and converted into some Object
         * type such as Integer or Foobar.
         */
        Object paramValue = null;
    }
}
