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
package org.apache.beehive.netui.pageflow;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.beehive.netui.util.internal.FileUtils;

/**
 * File filter that accepts any {@link PageFlowController} source file (*.jpf).
 * 
 * @deprecated This class will be removed without replacement in the next major release.
 */ 
public class PageFlowFileFilter implements FilenameFilter
{
    public boolean accept( File dir, String name )
    {
        return FileUtils.osSensitiveEndsWith( name, PageFlowConstants.PAGEFLOW_EXTENSION );
    }
}
