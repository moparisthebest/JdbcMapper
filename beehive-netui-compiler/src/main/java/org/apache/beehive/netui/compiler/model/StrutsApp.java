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
package org.apache.beehive.netui.compiler.model;

import org.apache.beehive.netui.compiler.model.validation.ValidationModel;
import org.apache.beehive.netui.compiler.JpfLanguageConstants;
import org.apache.beehive.netui.compiler.FatalCompileTimeException;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;


public class StrutsApp
        extends AbstractForwardContainer
        implements ForwardContainer, ExceptionContainer, JpfLanguageConstants
{
    //protected boolean _isRootApp = false;
    private HashMap _actionMappings = new HashMap();
    private ArrayList _exceptionCatches = new ArrayList();
    private ArrayList _messageResources = new ArrayList();
    private HashMap _formBeans = new HashMap();
    private ValidationModel _validationModel;
    private List _additionalValidatorConfigs;

    private boolean _returnToPageDisabled = true;
    private boolean _returnToActionDisabled = true;
    private boolean _isNestedPageFlow = false;
    private boolean _isLongLivedPageFlow = false;
    private boolean _isSharedFlow = false;
    private boolean _isAbstract = false;
    /** Map of name to typename */
    private Map _sharedFlows = null;
    private String _controllerClassName = null;
    private String _multipartHandlerClassName = null;
    private String _memFileSize = null;
    private List _tilesDefinitionsConfigs = null;


    protected static final String DUPLICATE_ACTION_COMMENT = "Note that there is more than one action with path \"{0}\"."
                                                           + "  Use a form-qualified action path if this is not the "
                                                           + "one you want.";
    
    protected static final String PAGEFLOW_REQUESTPROCESSOR_CLASSNAME
                                   = PAGEFLOW_PACKAGE + ".PageFlowRequestProcessor";

    protected static final String PAGEFLOW_CONTROLLER_CONFIG_CLASSNAME
                                   = PAGEFLOW_PACKAGE + ".config.PageFlowControllerConfig";

    protected static final String STRUTS_CONFIG_PREFIX = "struts-config";
    protected static final String STRUTS_CONFIG_EXTENSION = ".xml";
    public static final char STRUTS_CONFIG_SEPARATOR = '-';
    protected static final String WEBINF_DIR_NAME = "WEB-INF";
    protected static final String STRUTSCONFIG_OUTPUT_DIR = "_pageflow";
    protected static final String VALIDATOR_PLUG_IN_CLASSNAME = STRUTS_PACKAGE + ".validator.ValidatorPlugIn";
    protected static final String VALIDATOR_PATHNAMES_PROPERTY = "pathnames";
    protected static final String TILES_PLUG_IN_CLASSNAME = STRUTS_PACKAGE + ".tiles.TilesPlugin";
    protected static final String TILES_DEFINITIONS_CONFIG_PROPERTY = "definitions-config";
    protected static final String TILES_MODULE_AWARE_PROPERTY = "moduleAware";
    protected static final String NETUI_VALIDATOR_RULES_URI = '/' + WEBINF_DIR_NAME + "/beehive-netui-validator-rules.xml";
    protected static final String STRUTS_VALIDATOR_RULES_URI = '/' + WEBINF_DIR_NAME + "/validator-rules.xml";
    
    private static final String[] NODE_ORDER =
            {
                "data-sources",
                "form-beans",
                "global-exceptions",
                "global-forwards",
                "action-mappings",
                "controller",
                "message-resources",
                "plug-in"
            };

    
    public StrutsApp( String controllerClassName )
    {
        super( null );
        setParentApp( this );
        _controllerClassName = controllerClassName;
        
        //
        // Add a reference for the default validation message resources (in beehive-netui-pageflow.jar).
        //
        MessageResourcesModel mrm = new MessageResourcesModel( this );
        mrm.setParameter( DEFAULT_VALIDATION_MESSAGE_BUNDLE );
        mrm.setKey( DEFAULT_VALIDATION_MESSAGE_BUNDLE_NAME );
        mrm.setReturnNull( true );
        addMessageResources( mrm );
    }

    public void addMessageResources( MessageResourcesModel mr )
    {
        _messageResources.add( mr );
    }
    
    private void addDisambiguatedActionMapping( ActionModel mapping )
    {
        if ( mapping.getFormBeanName() != null )
        {
            String qualifiedPath = getFormQualifiedActionPath( mapping );
            String path = mapping.getPath();
            mapping.setPath( qualifiedPath );
            mapping.setUnqualifiedActionPath( path );
            _actionMappings.put( qualifiedPath, mapping );
        }
    }
    
    /**
     * Adds a new ActionMapping to this StrutsApp.
     */
    public void addActionMapping( ActionModel mapping )
    {
        String mappingPath = mapping.getPath();
        ActionModel conflictingActionMapping = ( ActionModel ) _actionMappings.get( mappingPath );
        
        if ( conflictingActionMapping != null )
        {
            ActionModel defaultMappingForThisPath = conflictingActionMapping;
            
            //
            // If the new action mapping takes no form, then it has the highest precedence, and replaces the existing
            // "natural" mapping for the given path.  Otherwise, replace the existing one if the existing one has a
            // form bean and if the new mapping's form bean type comes alphabetically before the existing one's.
            //
            if ( mapping.getFormBeanName() == null
                 || ( conflictingActionMapping.getFormBeanName() != null
                      && getBeanType( mapping ).compareTo( getBeanType( conflictingActionMapping ) ) < 0 ) )
            {
                _actionMappings.remove( mappingPath );
                _actionMappings.put( mappingPath, mapping );
                defaultMappingForThisPath = mapping;
                conflictingActionMapping.setOverloaded( false );
                addDisambiguatedActionMapping( conflictingActionMapping );
            }
            else
            {
                addDisambiguatedActionMapping( mapping );
            }
            
            defaultMappingForThisPath.setOverloaded( true );
            defaultMappingForThisPath.setComment( DUPLICATE_ACTION_COMMENT.replaceAll( "\\{0\\}", mappingPath ) );  // @TODO I18N
        }
        else
        {
            _actionMappings.put( mappingPath, mapping );
        }
    }

    
    //
    // Returns either the "form class" (specified for non-ActionForm-derived bean types), or the type of the
    // associated FormBeanModel.
    //
    public String getBeanType( ActionModel actionMapping )
    {
        String beanType = actionMapping.getFormClass();    // will be non-null for non-ActionForm-derived types
        
        if ( beanType == null )
        {
            FormBeanModel bean = getFormBean( actionMapping.getFormBeanName() );
            assert bean != null;
            beanType = bean.getType();
        }
        
        return beanType;
    }
    
    protected String getFormQualifiedActionPath( ActionModel action )
    {
        assert action.getFormBeanName() != null : "action " + action.getPath() + " has no form bean";
        String beanType = getBeanType( action );
        return action.getPath() + '_' + makeFullyQualifiedBeanName( beanType );
    }
    
    /**
     * Implemented for {@link ExceptionContainer}.
     */
    public void addException( ExceptionModel c )
    {
        _exceptionCatches.add( c );
    }

    /**
     * Implemented for {@link ExceptionContainer}.
     */
    public String getActionPath()
    {
        return null;
    }

    /**
     * Returns all of the form beans that are defined for this
     * StrutsApp.
     */
    public FormBeanModel[] getFormBeans()
    {
        return ( FormBeanModel[] ) getFormBeansAsList().toArray( new FormBeanModel[0] );
    }

    /**
     * Returns a list of all the form beans that are defined for this StrutsApp.
     */
    public List getFormBeansAsList()
    {
        ArrayList retList = new ArrayList();
        
        for ( Iterator i = _formBeans.values().iterator(); i.hasNext(); )
        {
            FormBeanModel fb = ( FormBeanModel ) i.next();
            if ( fb != null ) retList.add( fb );
        }
        
        return retList;
    }

    public FormBeanModel getFormBean( String formBeanName )
    {
        return ( FormBeanModel ) _formBeans.get( formBeanName );
    }

    /**
     * Returns a list of {@link FormBeanModel}.
     */ 
    public List getFormBeansByActualType( String actualTypeName, Boolean usesPageFlowScopedBean )
    {
        ArrayList beans = null;
        
        for ( Iterator i = _formBeans.values().iterator(); i.hasNext(); )
        {
            FormBeanModel formBean = ( FormBeanModel ) i.next();
            
            if ( formBean != null && formBean.getActualType().equals( actualTypeName )
                 && ( usesPageFlowScopedBean == null || usesPageFlowScopedBean.booleanValue() == formBean.isPageFlowScoped() ) )
            {
                if ( beans == null ) beans = new ArrayList();
                beans.add( formBean );
            }
        }

        return beans;
    }

    /**
     * Adds a new form bean to this StrutsApp.
     */
    public void addFormBean( FormBeanModel newFormBean )
    {
        _formBeans.put( newFormBean.getName(), newFormBean );
    }

    /**
     * Delete the given form-bean.
     */
    public void deleteFormBean( FormBeanModel formBean )
    {
        _formBeans.remove( formBean.getName() );
    }

    public String getFormNameForType( String formType, boolean isPageFlowScoped )
    {
        //
        // First try and create a form-bean name that is a camelcased version of the classname without all of its
        // package/outer-class qualifiers.  If one with that name already exists, munge the fully-qualified classname.
        //
        int lastQualifier = formType.lastIndexOf( '$' );
        if ( lastQualifier == -1 ) lastQualifier = formType.lastIndexOf( '.' );

        String formBeanName = formType.substring( lastQualifier + 1 );
        formBeanName = Character.toLowerCase( formBeanName.charAt( 0 ) ) + formBeanName.substring( 1 );

        //
        // If there's a name conflict, we need to disambiguate.
        //
        if ( _formBeans.containsKey( formBeanName ) )
        {
            String conflictingName = formBeanName;
            FormBeanModel conflictingBean = ( FormBeanModel ) _formBeans.get( conflictingName );
            
            //
            // If the conflicting form bean has a different value for isPageFlowScoped(), we can simply add a suffix
            // to this bean name.
            //
            if ( conflictingBean != null && isPageFlowScoped != conflictingBean.isPageFlowScoped() )
            {
                formBeanName += isPageFlowScoped ? "_flowScoped" : "_nonFlowScoped";
                assert ! _formBeans.containsKey( formBeanName );    // we generate this name -- shouldn't conflict
                return formBeanName;
            }
            
            formBeanName = makeFullyQualifiedBeanName( formType );
            
            //
            // Now look for the one we're conflicting with 
            //
            if ( conflictingBean != null )
            {
                String nonConflictingName = makeFullyQualifiedBeanName( conflictingBean.getType() );
                conflictingBean.setName( nonConflictingName );
                _formBeans.put( nonConflictingName, conflictingBean );
                
                //
                // Now look for any action mappings that are using the conflicting name...
                //
                for ( Iterator i = _actionMappings.values().iterator(); i.hasNext(); )
                {
                    ActionModel mapping = ( ActionModel ) i.next();
                    
                    if ( mapping.getFormBeanName() != null && mapping.getFormBeanName().equals( conflictingName ) )
                    {
                        mapping.setFormBeanName( nonConflictingName );
                    }
                }
            }
            
            _formBeans.put( conflictingName, null );
        }

        return formBeanName;
    }

    protected static String makeFullyQualifiedBeanName( String formType )
    {
        return formType.replace( '.', '_' ).replace( '$', '_' );
    }

    protected static class ActionMappingComparator implements Comparator
    {
        public int compare( Object o1, Object o2 )
        {
            assert o1 instanceof ActionModel && o2 instanceof ActionModel;
            
            ActionModel am1 = ( ActionModel ) o1;
            ActionModel am2 = ( ActionModel ) o2;
            
            assert ! am1.getPath().equals( am2.getPath() );     // there should be no duplicate paths
            return am1.getPath().compareTo( am2.getPath() );
        }
    }

    protected Map getFormBeansMap()
    {
        return _formBeans;
    }
    
    protected List getExceptionCatchesList()
    {
        return _exceptionCatches;
    }
    
    protected List getSortedActionMappings()
    {
        ArrayList sortedActionMappings = new ArrayList();
        sortedActionMappings.addAll( _actionMappings.values() );
        Collections.sort( sortedActionMappings, new ActionMappingComparator() );
        return sortedActionMappings;
    }
    
    protected List getMessageResourcesList()
    {
        return _messageResources;
    }
    
    /**
     * Get the MessageResourcesModel for which no "key" is set (the default one used at runtime).
     */ 
    public MessageResourcesModel getDefaultMessageResources()
    {
        for ( java.util.Iterator ii = _messageResources.iterator(); ii.hasNext(); )  
        {
            MessageResourcesModel i = ( MessageResourcesModel ) ii.next();
            if ( i.getKey() == null ) return i;
        }
        
        return null;
    }
    
    public boolean isReturnToPageDisabled()
    {
        return _returnToPageDisabled;
    }

    public boolean isReturnToActionDisabled()
    {
        return _returnToActionDisabled;
    }
    
    public void setReturnToPageDisabled( boolean disabled )
    {
        _returnToPageDisabled = disabled;
    }
    
    public void setReturnToActionDisabled( boolean disabled )
    {
        _returnToActionDisabled = disabled;
    }
    
    public void setAdditionalValidatorConfigs( List additionalValidatorConfigs )
    {
        if ( additionalValidatorConfigs != null && ! additionalValidatorConfigs.isEmpty() )
        {
            _additionalValidatorConfigs = additionalValidatorConfigs;
        }
    }
    
    public void setValidationModel( ValidationModel validationModel )
    {
        if ( ! validationModel.isEmpty() )  // if there's nothing in the validation model, we don't care about it.
        {
            _validationModel = validationModel;
        }
    }
    
    public void writeXml( PrintWriter writer, File mergeFile )
        throws IOException, FatalCompileTimeException, XmlModelWriterException
    {
        XmlModelWriter xw = new XmlModelWriter( mergeFile, "struts-config",
                "-//Apache Software Foundation//DTD Struts Configuration 1.1//EN",
                "http://jakarta.apache.org/struts/dtds/struts-config_1_1.dtd",
                getHeaderComment(mergeFile));
        writeXML(xw, xw.getRootElement());
        xw.simpleFastWrite(writer);
    }

    protected void writeToElement(XmlModelWriter xw, Element element) {
        // form-beans
        writeFormBeans(xw, element);
        
        // global-exceptions
        writeExceptions(xw, element);
        
        // global-forwards
        Element globalForwardsElement = findChildElement(xw, element, "global-forwards", true, NODE_ORDER);
        writeForwards(xw, globalForwardsElement);
        
        // action-mappings
        writeActionMappings(xw, element);
        
        // message-resources
        writeMessageResources(xw, element);
        
        // controller (request-processor, etc.)
        writeControllerElement(xw, element);
        
        // ValidatorPlugIn
        writeValidatorInit(xw, element);
        
        // TilesPlugin
        writeTilesInit(xw, element);
    }

    private void writeMessageResources(XmlModelWriter xw, Element element)
    {
        for ( Iterator i = getMessageResourcesList().iterator(); i.hasNext(); )
        {
            MessageResourcesModel mr = ( MessageResourcesModel ) i.next();
            
            if ( mr != null )
            {
                Element mrToEdit = findChildElement(xw, element, "message-resources", "key", mr.getKey(), true, NODE_ORDER);
                mr.writeXML( xw, mrToEdit );
            }
        }
    }
    
    private void writeActionMappings(XmlModelWriter xw, Element element)
    {
        Element actionMappingsElement = findChildElement(xw, element, "action-mappings", true, NODE_ORDER);
        List actionMappingsList = getSortedActionMappings();
        
        for ( int i = 0; i < actionMappingsList.size(); ++i )
        {
            ActionModel am = ( ActionModel ) actionMappingsList.get( i );
            Element actionMappingtoEdit = findChildElement(xw, actionMappingsElement, "action", "path", am.getPath(), true, null);
            am.writeXML(xw, actionMappingtoEdit);
        }
    }
    
    private void writeExceptions(XmlModelWriter xw, Element element)
    {
        Element exceptionsElement = findChildElement(xw, element, "global-exceptions", true, NODE_ORDER);
        List exceptionCatches = getExceptionCatchesList();
        
        if ( exceptionCatches != null && ! exceptionCatches.isEmpty() )
        {
            for ( int i = 0; i < exceptionCatches.size(); ++i )
            {
                ExceptionModel ec = ( ExceptionModel ) exceptionCatches.get( i );
                Element exceptionToEdit = findChildElement(xw, exceptionsElement, "exception", "type", ec.getType(), true, null);
                ec.writeXML( xw, exceptionToEdit );
            }
        } 
    }
    

    private void writeFormBeans(XmlModelWriter xw, Element element)
    {
        Element formBeansElement = findChildElement(xw, element, "form-beans", true, NODE_ORDER);
        
        for (Iterator i = getFormBeansMap().values().iterator(); i.hasNext(); )
        {
            FormBeanModel fb = (FormBeanModel) i.next();
            
            // fb may be null -- we do this to prevent a name from being used.
            if (fb != null) {
                Element formBean = findChildElement(xw, formBeansElement, "form-bean", "name", fb.getName(), true, null);
                fb.writeXML(xw, formBean);
            }
        }
    }
    
    protected void writeControllerElement(XmlModelWriter xw, Element element)
    {
        Element controllerElement = findChildElement(xw, element, "controller", false, NODE_ORDER);
        
        // Insert it before the first <message-resources> element.
        if (controllerElement == null) {
            controllerElement = xw.getDocument().createElement("controller");
            Element[] messageResourcesElements = getChildElements(element, "message-resources");
            if (messageResourcesElements.length > 0) {
                element.insertBefore(controllerElement, messageResourcesElements[0]);
            } else {
                element.appendChild(controllerElement);
            }
        }
        
        setElementAttribute(controllerElement, "processorClass", PAGEFLOW_REQUESTPROCESSOR_CLASSNAME);
        setElementAttribute(controllerElement, "inputForward", true);
        setElementAttribute(controllerElement, "multipartClass", _multipartHandlerClassName);
        setElementAttribute(controllerElement, "memFileSize", _memFileSize);
        
        
        if ( _isNestedPageFlow ) addSetProperty( xw, controllerElement, "isNestedPageFlow", "true" );
        if ( _isLongLivedPageFlow ) addSetProperty( xw, controllerElement, "isLongLivedPageFlow", "true" );
        if ( _isSharedFlow ) addSetProperty( xw, controllerElement, "isSharedFlow", "true" );
        if ( _isAbstract ) addSetProperty( xw, controllerElement, "isAbstract", "true" );
        if ( isReturnToPageDisabled() ) addSetProperty( xw, controllerElement, "isReturnToPageDisabled", "true" );
        if ( isReturnToActionDisabled() ) addSetProperty( xw, controllerElement, "isReturnToActionDisabled", "true" );
        
        if ( _sharedFlows != null )
        {
            StringBuffer str = new StringBuffer();
            boolean first = true;
            
            for ( java.util.Iterator ii = _sharedFlows.entrySet().iterator(); ii.hasNext(); )  
            {
                Map.Entry entry = ( Map.Entry ) ii.next();
                if ( ! first ) str.append( ',' );
                first = false;
                String name = ( String ) entry.getKey();
                String type = ( String ) entry.getValue();
                str.append( name ).append( '=' ).append( type );
            }
            
           addSetProperty( xw, controllerElement, "sharedFlows", str.toString() );
        }
        
        addSetProperty( xw, controllerElement, "controllerClass", _controllerClassName );
        
        //
        // If there is not a default MessageResources element in the generated XML, add a special set-property
        // to communicate this to the runtime.
        //
        if (findChildElement(xw, element, "message-resources", "key", null) == null) {
            addSetProperty( xw, controllerElement, "isMissingDefaultMessages", "true" );
        }
    }
    
    protected void addSetProperty( XmlModelWriter xw, Element element, String propName, String propValue )
    {
        setCustomProperty(xw, element, propName, propValue, PAGEFLOW_CONTROLLER_CONFIG_CLASSNAME);
    }
    
    protected void writeValidatorInit(XmlModelWriter xw, Element element)
    {
        if ( ( _validationModel != null && ! _validationModel.isEmpty() ) || _additionalValidatorConfigs != null )
        {
            Element plugInElementToEdit = findChildElement(xw, element, "plug-in", "className",
                                                          VALIDATOR_PLUG_IN_CLASSNAME, true, NODE_ORDER);
            
            Element pathnamesPropertyElement =
              findChildElement(xw, plugInElementToEdit, "set-property", "property", VALIDATOR_PATHNAMES_PROPERTY, true, null);
            
            pathnamesPropertyElement.setAttribute("property", VALIDATOR_PATHNAMES_PROPERTY);
            StringBuffer pathNames = new StringBuffer();
            pathNames.append( NETUI_VALIDATOR_RULES_URI );
            pathNames.append( ',' ).append( STRUTS_VALIDATOR_RULES_URI );
            
            if ( _validationModel != null && ! _validationModel.isEmpty() )
            {
                pathNames.append( ",/" ).append( _validationModel.getOutputFileURI() );
            }
            
            if ( _additionalValidatorConfigs != null )
            {
                for ( java.util.Iterator ii = _additionalValidatorConfigs.iterator(); ii.hasNext(); )  
                {
                    String configFile = ( String ) ii.next();
                    pathNames.append( ',' ).append( configFile );
                }
            }
            
            setElementAttribute(pathnamesPropertyElement, "value", pathNames.toString());
        }
    }
    
    protected void writeTilesInit(XmlModelWriter xw, Element element)
    {
        if ( _tilesDefinitionsConfigs == null || _tilesDefinitionsConfigs.isEmpty() ) {
            return;
        }

        Element plugInElementToEdit =
                findChildElement(xw, element, "plug-in", "className", TILES_PLUG_IN_CLASSNAME, true, NODE_ORDER);

        
        
        Element definitionsElement = findChildElement(xw, plugInElementToEdit, "set-property", "property",
                                                      TILES_DEFINITIONS_CONFIG_PROPERTY, true, null);
        StringBuffer pathNames = new StringBuffer();
        boolean firstOne = true;

        for ( java.util.Iterator ii = _tilesDefinitionsConfigs.iterator(); ii.hasNext(); )  
        {
            String definitionsConfig = ( String ) ii.next();
            if ( ! firstOne ) pathNames.append( ',' );
            firstOne = false;
            pathNames.append( definitionsConfig );
        }
        setElementAttribute(definitionsElement, "value", pathNames.toString());

        Element moduleAwareElement =
                findChildElement(xw, plugInElementToEdit, "set-property", "property", TILES_MODULE_AWARE_PROPERTY, true, null);
        setElementAttribute(moduleAwareElement, "value", true);
    }

    protected String getHeaderComment( File mergeFile )
            throws FatalCompileTimeException
    {
        return null;
    }
       
    public void setNestedPageFlow( boolean nestedPageFlow )
    {
        _isNestedPageFlow = nestedPageFlow;
    }

    public void setLongLivedPageFlow( boolean longLivedPageFlow )
    {
        _isLongLivedPageFlow = longLivedPageFlow;
    }
    
    protected static String getStrutsConfigURI( String containingPackage, boolean isSharedFlow )
    {
        return getOutputFileURI( STRUTS_CONFIG_PREFIX, containingPackage, isSharedFlow );
    }
    
    public static String getOutputFileURI( String filePrefix, String containingPackage, boolean isSharedFlow )
    {
        StringBuffer fileName = new StringBuffer( STRUTSCONFIG_OUTPUT_DIR );
        fileName.append( '/' ).append( filePrefix );
        if ( containingPackage != null && containingPackage.length() > 0 ) fileName.append( STRUTS_CONFIG_SEPARATOR );
        if ( isSharedFlow ) {
            fileName.append( STRUTS_CONFIG_SEPARATOR );
            if (containingPackage == null || containingPackage.length() == 0) {
                fileName.append(STRUTS_CONFIG_SEPARATOR);
            }
        }
        if ( containingPackage != null ) fileName.append( containingPackage.replace( '.', STRUTS_CONFIG_SEPARATOR ) );
        fileName.append( STRUTS_CONFIG_EXTENSION );
        return fileName.toString();
    }
    
    protected void setSharedFlow( boolean sharedFlow )
    {
        _isSharedFlow = sharedFlow;
    }

    protected void setSharedFlows( Map sharedFlows )
    {
        _sharedFlows = sharedFlows;
    }
    
    public String getMultipartHandlerClassName()
    {
        return _multipartHandlerClassName;
    }

    protected void setMultipartHandlerClassName( String multipartHandlerClassName )
    {
        _multipartHandlerClassName = multipartHandlerClassName;
    }

    public void setTilesDefinitionsConfigs( List tilesDefinitionsConfigs )
    {
        _tilesDefinitionsConfigs = tilesDefinitionsConfigs;
    }

    protected boolean isSharedFlow()
    {
        return _isSharedFlow;
    }

    public boolean isAbstract() {
        return _isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        _isAbstract = anAbstract;
    }

    /**
     * Get the threshold for keeping a file in memory when processing a multipart request.  An example is
     * <code>256K</code>
     */ 
    protected String getMemFileSize()
    {
        return _memFileSize;
    }

    /**
     * Set the threshold for keeping a file in memory when processing a multipart request.  An example is
     * <code>256K</code>
     */ 
    protected void setMemFileSize( String memFileSize )
    {
        _memFileSize = memFileSize;
    }
}
