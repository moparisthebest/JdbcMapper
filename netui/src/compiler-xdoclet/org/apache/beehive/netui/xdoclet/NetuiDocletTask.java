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

import org.apache.beehive.netui.compiler.typesystem.util.SourcePosition;
import org.apache.tools.ant.BuildException;
import xdoclet.DocletTask;
import xdoclet.SubTask;
import xdoclet.DocletContext;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Netui XDoclet task
 *
 * @ant.element name="netuidoclet" display-name="NetUI Task"
 */
public class NetuiDocletTask extends DocletTask
{
    private static HashMap _buildMessages = new HashMap();   // String filename -> List messages

    private File _webContentDir;
    private String _sourcePath;


    /**
     * @throws BuildException
     *
     */
    protected void start() throws BuildException
    {
        File destDir = getDestDir();
        if (destDir == null || destDir.getPath().equals("bogus")) {
            throw new BuildException("destdir is required");
        }

        if (_webContentDir == null) {
            throw new BuildException("webcontentdir is required");
        }

        if (_sourcePath == null) {
            throw new BuildException("sourcepath is required");
        }

        try
        {
            SubTask[] subtasks = DocletContext.getInstance().getSubTasks();
            for (int i = 0; i < subtasks.length; i++) {
                SubTask subTask = subtasks[i];
                if (subTask != null) {
                    assert subTask instanceof NetuiSubTask : subTask.getClass().getName();
                    NetuiSubTask netuiSubTask = (NetuiSubTask) subTask;
                    netuiSubTask.setWebContentDir(_webContentDir.getPath());
                    netuiSubTask.setSourcePath(_sourcePath);
                }
            }
        
            super.start();
        }
        finally
        {
            // list any warnings and errors
            boolean overallError = false;
            if ( _buildMessages != null )
            {
                Iterator i = _buildMessages.keySet().iterator();
                
                while ( i.hasNext() )
                {
                    String sourceFile = ( String ) i.next();
                    List messages = ( List ) _buildMessages.get( sourceFile );
                    int errorCount = 0;
                    int warningCount = 0;
                    
                    for ( Iterator j = messages.iterator(); j.hasNext(); )
                    {
                        BuildMessage message = ( BuildMessage ) j.next();
                        System.err.println();
                        System.err.print( sourceFile );
                        System.err.print( ": " );
                        
                        if ( message.getLine() > 0 )
                        {
                            String[] args = { new Integer( message.getLine() ).toString() };
                            System.err.println( XDocletCompilerUtils.getMessage( "compiler.line", args ) );
                        }
                        
                        if ( message.isError() )
                        {
                            overallError = true;
                            ++errorCount;
                        }
                        else
                        {
                            System.err.print( XDocletCompilerUtils.getMessage( "compiler.warning", null ) );
                            ++warningCount;
                        }
                        
                        System.err.println( message.getMessage() );
                    }
                    
                    System.err.println( XDocletCompilerUtils.getMessage( "compiler.build.results",
                                                    new String[]{ new Integer( errorCount ).toString(),
                                                                  new Integer( warningCount ).toString(),
                                                                  sourceFile } ) );
                }
            }

            _buildMessages = null;

            if ( overallError )
            {
                System.err.println( XDocletCompilerUtils.getMessage( "compiler.build.failed", null ) );
                throw new NetuiBuildException();
            }
        }
    }

    public static void addError( String error, SourcePosition sourcePosition )
    {
        assert sourcePosition != null;
        String sourceFilePath = sourcePosition.file().getPath();
        int line = sourcePosition.line();
        addError( error, sourceFilePath, line );
    }
    
    public static void addError( String error, String sourceFile, int line )
    {
        List messages = ( List ) _buildMessages.get( sourceFile );
        
        if ( messages == null )
        {
            messages = new ArrayList();
            _buildMessages.put( sourceFile, messages );
        }
        
        messages.add( new BuildMessage( error, line, true ) );
    }

    public static void addWarning( String warning, SourcePosition sourcePosition )
    {
        assert sourcePosition != null;
        String sourceFilePath = sourcePosition.file().getPath();
        int line = sourcePosition.line();
        addWarning( warning, sourceFilePath, line );
    }
    
    public static void addWarning( String warning, String sourceFile, int line )
    {
        List messages = ( List ) _buildMessages.get( sourceFile );
        
        if ( messages == null )
        {
            messages = new ArrayList();
            _buildMessages.put( sourceFile, messages );
        }
        
        messages.add( new BuildMessage( warning, line, false ) );
    }

    private static class BuildMessage
    {
        private String _message;
        private boolean _error;
        private int _line;

        public BuildMessage( String message, int line, boolean error )
        {
            _message = message;
            _error = error;
            _line = line;
        }

        public final String getMessage()
        {
            return _message;
        }

        public final boolean isError()
        {
            return _error;
        }

        public final int getLine()
        {
            return _line;
        }
    }
    
    /**
     * Called by superclass before start() is called
     *
     * @throws BuildException Describe the exception
     */
    protected void validateOptions() throws BuildException
    {
        // we don't support the destdir attribute; so if it's null, just fake it so super won't
        // fail validation
        if ( getDestDir() == null )
        {
            setDestDir( new File( "bogus" ) );
        }
        super.validateOptions();
    }

    public void setWebcontentdir( File file )
    {
        _webContentDir = file;
    }

    public void setSourcepath(String sourcePath)
    {
        _sourcePath = sourcePath;
    }
}
