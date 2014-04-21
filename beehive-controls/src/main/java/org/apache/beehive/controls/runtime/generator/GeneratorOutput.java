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
package org.apache.beehive.controls.runtime.generator;

import java.io.Writer;
import java.util.HashMap;

/**
 * The GeneratorOutput class represents a single file output by the code generation process,
 * as well as the template and context information necessary to generate it.
 */
public class GeneratorOutput
{
    public GeneratorOutput(Writer outWriter, String templateName, HashMap<String,Object> context)
    {
        _outWriter = outWriter;
        _templateName = templateName;
        _context = context;
    }

    public String getTemplateName() {
        return _templateName;
    }

    public HashMap<String,Object> getContext() {
        return _context;
    }

    public Writer getWriter() {
        return _outWriter; 
    }

    String _templateName;
    Writer _outWriter;
    HashMap<String, Object> _context;
}
