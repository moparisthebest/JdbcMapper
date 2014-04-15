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
package org.apache.beehive.netui.xdoclet;

import org.apache.beehive.netui.compiler.processor.PageFlowCoreAnnotationProcessor;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;
import org.apache.beehive.netui.compiler.xdoclet.typesystem.impl.declaration.DeclarationImpl;
import org.apache.beehive.netui.compiler.xdoclet.typesystem.impl.env.CoreAnnotationProcessorEnvImpl;
import xdoclet.DocletContext;
import xdoclet.SubTask;
import xdoclet.XDocletException;
import xjavadoc.SourceClass;
import xjavadoc.XJavaDoc;

import java.util.Collection;
import java.util.Iterator;


/**
 * XJavaDoc subtask to run through a set of page flows in a webapp and generate Struts XML config files for them.
 * 
 * @ant.element          display-name="Netui" name="netui" parent="org.apache.beehive.netui.xdoclet.NetuiDocletTask"
 */
public class NetuiSubTask extends SubTask
{
    private SourceClass _currentSourceClass;
    private String _webContentDir;
    private String _sourcePath;
    
    /**
     * Main entry point for xjavadoc tasks. Here we iterate through all the classes found by
     * xjavadoc and process the ones that we recognize as pageflows.
     * 
     * @throws XDocletException
     */ 
    public void execute() throws XDocletException
    {
    /*
        if ( DocletContext.getInstance().isVerbose() )
            System.out.println( CompilerUtil.genMessage( "compiler.info.gen.location",
                    new String[] {getDestDir().getPath()} ) );
    */
        Collection classes = getXJavaDoc().getSourceClasses();
        
        /*
        // issue a warning if xjavadoc didn't find any classes at all
        if ( classes.size() == 0 )
        {
            System.out.println( CompilerUtil.genMessage( "no.classes.found" ) );
            return;
        }
        
        _pageflowCount = 0;
        */
        
        Iterator iter = classes.iterator();
        while ( iter.hasNext() )
        {
            SourceClass sourceClass = ( SourceClass ) iter.next();
            CoreAnnotationProcessorEnv env = CoreAnnotationProcessorEnvImpl.get( getContext(), this, sourceClass );
            AnnotationTypeDeclaration[] decls = DeclarationImpl.getAllAnnotations();    // TODO: filter appropriately
            
            PageFlowCoreAnnotationProcessor pfap = new PageFlowCoreAnnotationProcessor( decls, env );
            
            try
            {
                _currentSourceClass = sourceClass;
                pfap.process();
            }
            finally
            {
                _currentSourceClass = null;
            }
        }
    }

    public static NetuiSubTask get()
    {
        SubTask subtask = DocletContext.getInstance().getActiveSubTask();
        assert subtask instanceof NetuiSubTask : subtask.getClass().getName();
        return ( NetuiSubTask ) subtask;
    }

    public XJavaDoc getXJavaDoc()
    {
        return super.getXJavaDoc();
    }

    public SourceClass getCurrentSourceClass()
    {
        return _currentSourceClass;
    }

    public void setWebContentDir(String webContentDir)
    {
        _webContentDir = webContentDir;
    }

    public String getWebContentDir()
    {
        return _webContentDir;
    }

    public void setSourcePath(String sourcePath)
    {
        _sourcePath = sourcePath;
    }

    public String getSourcePath()
    {
        return _sourcePath;
    }
}
