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
package org.apache.beehive.netui.compiler.genmodel;

import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.FlowControllerInfo;
import org.apache.beehive.netui.compiler.JpfLanguageConstants;
import org.apache.beehive.netui.compiler.MergedControllerAnnotation;
import org.apache.beehive.netui.compiler.Diagnostics;
import org.apache.beehive.netui.compiler.FatalCompileTimeException;
import org.apache.beehive.netui.compiler.model.FormBeanModel;
import org.apache.beehive.netui.compiler.model.ForwardModel;
import org.apache.beehive.netui.compiler.model.MessageResourcesModel;
import org.apache.beehive.netui.compiler.model.StrutsApp;
import org.apache.beehive.netui.compiler.model.XmlModelWriterException;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.MethodDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.Modifier;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class GenStrutsApp
        extends StrutsApp
        implements JpfLanguageConstants
{
    private ClassDeclaration _jclass;
    private String _containingPackage;
    private File _strutsConfigFile;
    private File _sourceFile;
    private CoreAnnotationProcessorEnv _env;
    private FlowControllerInfo _fcInfo;
    
    protected void recalculateStrutsConfigFile()
        throws IOException, FatalCompileTimeException
    {
        _strutsConfigFile = calculateStrutsConfigFile(); // caching this
    }

    FlowControllerInfo getFlowControllerInfo()
    {
        return _fcInfo;
    }

    public GenStrutsApp( File sourceFile, ClassDeclaration jclass, CoreAnnotationProcessorEnv env,
                         FlowControllerInfo fcInfo, boolean checkOnly, Diagnostics diagnostics )
        throws IOException, FatalCompileTimeException
    {
        super( jclass.getQualifiedName() );
        
        _jclass = jclass;
        _containingPackage = jclass.getPackage().getQualifiedName();
        _sourceFile = sourceFile;
        _env = env;
        assert fcInfo != null;
        _fcInfo = fcInfo;
        
        recalculateStrutsConfigFile();
        
        if ( checkOnly ) return;
        
        if ( _jclass != null )
        {
            MergedControllerAnnotation mca = fcInfo.getMergedControllerAnnotation();
            setNestedPageFlow( mca.isNested() );
            setLongLivedPageFlow( mca.isLongLived() );
            addMessageResources( mca.getMessageResources() );   // messageResources is deprecated
            addMessageBundles( mca.getMessageBundles() );   // messageBundles is not
            addSimpleActions( mca.getSimpleActions() );
            setMultipartHandler( mca.getMultipartHandler() );
            GenForwardModel.addForwards( mca.getForwards(), this, _jclass, this, null );
            
            // TODO: comment
            addForward( new ForwardModel( "_auto", "", this ) );
            
            GenExceptionModel.addCatches( mca.getCatches(), this, _jclass, this, this );
            addTilesDefinitionsConfigs( mca.getTilesDefinitionsConfigs() );
            setAdditionalValidatorConfigs( mca.getCustomValidatorConfigs() );
            addActionMethods();
            addFormBeans( _jclass );
            setAbstract(_jclass.hasModifier(Modifier.ABSTRACT));
        }
        
        if ( fcInfo != null )
        {
            setSharedFlows( fcInfo.getSharedFlowTypeNames() );
            setReturnToActionDisabled( ! fcInfo.isNavigateToActionEnabled() );
            setReturnToPageDisabled( ! fcInfo.isNavigateToPageEnabled() );
        }
    }
    
    private void addFormBeans( ClassDeclaration jclass )
    {
        Collection innerTypes = CompilerUtils.getClassNestedTypes( jclass );
        
        for ( Iterator ii = innerTypes.iterator(); ii.hasNext(); )  
        {
            TypeDeclaration innerType = ( TypeDeclaration ) ii.next();
            if ( innerType instanceof ClassDeclaration )
            {
                ClassDeclaration innerClass = ( ClassDeclaration ) innerType;
                
                if ( innerType.hasModifier( Modifier.PUBLIC ) 
                     && CompilerUtils.isAssignableFrom( PAGEFLOW_FORM_CLASS_NAME, innerClass, _env ) )
                {
                    getMatchingFormBeans(innerClass, Boolean.valueOf(false));
                }
            }
        }
        
    }

    /**
     * Returns a non-empty List of FormBeanModels that match the given form
     * bean type. The <code>usesPageFlowScopedFormBean</code> parameter can
     * be used to get the FormBeanModel for either a page flow scoped bean
     * (<code>true</code>), not flow scoped (<code>false</code>), or both
     * (<code>null</code>).
     * @param formType the form bean class type to match
     * @param usesPageFlowScopedFormBean flag to indicate that the bean is
     * page flow scoped. If null, return all FormBeanModels of the given type
     * regardless of being flow scoped or not.
     * @return a non-empty List of FormBeanModels that match the given type
     */
    List getMatchingFormBeans( TypeDeclaration formType, Boolean usesPageFlowScopedFormBean )
    {
        //
        // Use the actual type of form to create the name.
        // This avoids conflicts if there are multiple forms using the
        // ANY_FORM_CLASS_NAME type.
        //
        String actualType = CompilerUtils.getLoadableName( formType );

        //
        // See if the app already has a form-bean of this type.  If so,
        // we'll just use it; otherwise, we need to create it.
        //
        List formBeans = getFormBeansByActualType( actualType, usesPageFlowScopedFormBean );

        if (formBeans == null) {
            // if not indicated assume not flow scoped when adding a new bean
            boolean isFlowScoped = false;
            if (usesPageFlowScopedFormBean != null) {
                isFlowScoped = usesPageFlowScopedFormBean.booleanValue();
            }
            FormBeanModel formBeanModel = addNewFormBean(formType, isFlowScoped);
            formBeans = new ArrayList();
            formBeans.add(formBeanModel);
        }
        
        assert formBeans.size() > 0;
        return formBeans;
    }
    
    private FormBeanModel addNewFormBean(TypeDeclaration formType, boolean usesFlowScopedFormBean)
    {
        String actualTypeName = CompilerUtils.getLoadableName( formType );
        String formClass = CompilerUtils.getFormClassName( formType, _env );
        String name = getFormNameForType( actualTypeName, usesFlowScopedFormBean );
        String key = getMessageResourcesFromFormType( formType );
        FormBeanModel fb = new FormBeanModel(name, formClass, actualTypeName, usesFlowScopedFormBean, key, this);
        addFormBean( fb );
        return fb;
    }
    
    private void addMessageResources( Collection messageResources )
    {
        if ( messageResources != null )
        {
            for ( Iterator ii = messageResources.iterator(); ii.hasNext(); )  
            {
                AnnotationInstance ann = ( AnnotationInstance ) ii.next();
                addMessageResources( new GenMessageBundleModel( this, ann ) );
            }
        }
    }
    
    private void addMessageBundles( Collection messageBundles )
    {
        if ( messageBundles != null )
        {
            for ( Iterator ii = messageBundles.iterator(); ii.hasNext(); )  
            {
                AnnotationInstance ann = ( AnnotationInstance ) ii.next();
                addMessageResources( new GenMessageBundleModel( this, ann ) );
            }
        }
    }
    
    private void addSimpleActions( Collection simpleActionAnnotations )
    {
        if ( simpleActionAnnotations != null )
        {
            for ( Iterator ii = simpleActionAnnotations.iterator(); ii.hasNext(); )  
            {
                AnnotationInstance ann = ( AnnotationInstance ) ii.next();
                TypeDeclaration containingType = ann.getContainingType();
                
                // If this is an inherited method, add a delegating action mapping.
                if (CompilerUtils.typesAreEqual(_jclass, containingType)) {
                    addActionMapping( new GenSimpleActionModel(ann, this, _jclass));
                } else {
                    addActionMapping(new DelegatingSimpleActionModel(ann, containingType, this, _jclass));
                }
            }
        }
    }
    
    private void setMultipartHandler( String mpHandler )
    {
        if ( mpHandler != null )
        {
            if ( mpHandler.equals( MULTIPART_HANDLER_DISABLED_STR ) )
            {
                setMultipartHandlerClassName( "none" );
            }
            else
            {
                setMultipartHandlerClassName( COMMONS_MULTIPART_HANDLER_CLASSNAME );
                
                if ( mpHandler.equals( MULTIPART_HANDLER_DISK_STR ) )
                {
                    setMemFileSize( "0K" );
                }
                else
                {
                    assert mpHandler.equals( MULTIPART_HANDLER_MEMORY_STR ) : mpHandler;
                }
            }
        }
    }
    
    private void addTilesDefinitionsConfigs( List tilesDefinitionsConfigs )
    {
        if ( tilesDefinitionsConfigs == null || tilesDefinitionsConfigs.isEmpty() )
        {
            return;
        }

        List paths = new ArrayList();

        for ( Iterator ii = tilesDefinitionsConfigs.iterator(); ii.hasNext(); )  
        {
            String definitionsConfig = ( String ) ii.next();

            if ( definitionsConfig != null && definitionsConfig.length() > 0 )
            {
                paths.add( definitionsConfig );
            }
        }

        setTilesDefinitionsConfigs( paths );
    }

    private void addActionMethods()
    {
        MethodDeclaration[] actionMethods = CompilerUtils.getClassMethods( _jclass, ACTION_TAG_NAME );
        
        for ( int i = 0; i < actionMethods.length; i++ )
        {
            MethodDeclaration actionMethod = actionMethods[i];
            
            if ( ! actionMethod.hasModifier( Modifier.ABSTRACT ) )
            {
                // If this is an inherited method, add a delegating action mapping.
                TypeDeclaration declaringType = actionMethod.getDeclaringType();
                if (CompilerUtils.typesAreEqual(_jclass, declaringType)) {
                    addActionMapping(new GenActionModel(actionMethod, this, _jclass));
                } else {
                    addActionMapping(new DelegatingActionModel(actionMethod, declaringType, this, _jclass));
                }
            }
        }
    }

    /**
     * @return the message-resources key for the form bean's message bundle
     */
    String getMessageResourcesFromFormType( TypeDeclaration formTypeDecl )
    {
        if ( ! ( formTypeDecl instanceof ClassDeclaration ) ) return null;
        
        ClassDeclaration formClassDecl = ( ClassDeclaration ) formTypeDecl;
        
        AnnotationInstance ann = CompilerUtils.getAnnotation( formClassDecl, FORM_BEAN_TAG_NAME, true );
        
        if ( ann != null )
        {
            String defaultMessageResources = CompilerUtils.getString( ann, MESSAGE_BUNDLE_ATTR, true );
            
            if ( defaultMessageResources != null )
            {
                String key = "formMessages:" + CompilerUtils.getLoadableName( formClassDecl );
                
                for ( Iterator ii = getMessageResourcesList().iterator(); ii.hasNext(); )  
                {
                    MessageResourcesModel i = ( MessageResourcesModel ) ii.next();
                    if ( key.equals( i.getKey() ) && i.getParameter().equals( defaultMessageResources ) ) return key;
                }
                
                MessageResourcesModel mrm = new MessageResourcesModel( this );
                mrm.setKey( key );
                mrm.setParameter( defaultMessageResources );
                mrm.setReturnNull( true );
                addMessageResources( mrm );
                return key;
            }
        }
        
        return null;
    }
    
    protected String getMergeFileName()
    {
        return getFlowControllerInfo().getMergedControllerAnnotation().getStrutsMerge();
    }
    
    public void writeToFile()
        throws FileNotFoundException, IOException, XmlModelWriterException, FatalCompileTimeException
    {
        File strutsMergeFile = getMergeFile( getMergeFileName() );
        PrintWriter writer = getEnv().getFiler().createTextFile( _strutsConfigFile );
        
        try
        {
            writeXml( writer, strutsMergeFile );
        }
        finally
        {
            writer.close();
        }
    }
    
    public boolean isStale()
            throws FatalCompileTimeException
    {
        return isStale( getMergeFile( getMergeFileName() ) );
    }

    String getOutputFileURI( String filePrefix )
    {
        return getOutputFileURI( filePrefix, _containingPackage, false );
    }
    
    String getStrutsConfigURI()
    {
        return getStrutsConfigURI( _containingPackage, false );
    }

    protected String getContainingPackage()
    {
        return _containingPackage;
    }
    
    private File calculateStrutsConfigFile()
    {
        return new File( getStrutsConfigURI() );
    }
    
    /**
     * Tell whether the struts output file (struts-config-*.xml) is out of date, based on the
     * file times of the source file and the (optional) struts-merge file.
     */ 
    public boolean isStale( File mergeFile )
    {
        //
        // We can write to the file if it doesn't exist yet.
        //
        if ( ! _strutsConfigFile.exists() )
        {
            return true;
        }
        
        long lastWrite = _strutsConfigFile.lastModified();
        
        if ( mergeFile != null && mergeFile.exists() && mergeFile.lastModified() > lastWrite )
        {
            return true;
        }
        
        if ( _sourceFile.lastModified() > lastWrite )
        {
            return true;
        }
        
        return false;
    }
    /**
     * In some cases, canWrite() does not guarantee that a FileNotFoundException will not
     * be thrown when trying to write to a file.  This method actually tries to overwrite
     * the file as a test to see whether it's possible.
     */ 
    public boolean canWrite()
    {
        if ( ! _strutsConfigFile.canWrite() )
        {
            return false;
        }
        
        try
        {
            //
            // This appears to be the only way to predict whether the file can actually be
            // written to; it may be that canWrite() returns true, but the file permissions
            // (NTFS only?) will cause an exception to be thrown.
            //
            new FileOutputStream( _strutsConfigFile, true ).close();
        }
        catch ( FileNotFoundException e )
        {
            return false;
        }
        catch ( IOException e )
        {
            return false;
        }
        
        return true;
    }


    public File getStrutsConfigFile()
    {
        return _strutsConfigFile;
    }

    public File getMergeFile( String mergeFileName )
        throws FatalCompileTimeException
    {
        if ( mergeFileName != null )
        {
            return CompilerUtils.getFileRelativeToSourceFile( _jclass, mergeFileName, getEnv() );
        }

        return null;
    }

    protected String getHeaderComment( File mergeFile )
        throws FatalCompileTimeException
    {
        StringBuffer comment = new StringBuffer( " Generated from " );
        comment.append( getWebappRelativePath( _sourceFile ) );
        if ( mergeFile != null )
        {
            comment.append( " and " ).append( getWebappRelativePath( mergeFile ) );
        }
        comment.append( " on " ).append( new Date().toString() ).append( ' ' );
        return comment.toString();
    }
    
    private String getWebappRelativePath( File file )
        throws FatalCompileTimeException
    {
        String filePath = file.getAbsoluteFile().getPath();
        String[] sourceRoots = CompilerUtils.getWebSourceRoots( _env );
        
        //
        // Look through the source roots.
        //
        for ( int i = 0; i < sourceRoots.length; i++ )
        {
            String sourceRoot = sourceRoots[i].replace( '/', File.separatorChar );
            
            if ( filePath.startsWith( sourceRoot ) )
            {
                return filePath.substring( sourceRoot.length() ).replace( '\\', '/' );
            }
        }
        
        //
        // Look in the web content root.
        //
        String[] webContentRoots = CompilerUtils.getWebContentRoots( getEnv() );
        for ( int i = 0; i < webContentRoots.length; i++ )
        {
            String webContentRoot = webContentRoots[i].replace( '/', File.separatorChar );
            
            if ( filePath.startsWith( webContentRoot ) )
            {
                return filePath.substring( webContentRoot.length() ).replace( '\\', '/' );
            }
        }
        
        return file.toString();
    }
    
    CoreAnnotationProcessorEnv getEnv()
    {
        return _env;
    }
    
    protected String getValidationFilePrefix()
    {
        return "pageflow-validation";
    }
    
    ClassDeclaration getFlowControllerClass()
    {
        return _jclass;
    }
}
