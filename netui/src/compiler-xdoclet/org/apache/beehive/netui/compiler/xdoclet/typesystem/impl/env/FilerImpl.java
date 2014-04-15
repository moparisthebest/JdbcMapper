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
package org.apache.beehive.netui.compiler.xdoclet.typesystem.impl.env;

import org.apache.beehive.netui.compiler.typesystem.env.Filer;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class FilerImpl implements Filer {

    private File _destDir;

    public FilerImpl(String destDirPath) {
        _destDir = new File(destDirPath);
    }

    public PrintWriter createTextFile(File file) throws IOException {
        File outputFile = new File(_destDir, file.getPath());
        outputFile.getParentFile().mkdirs();
        return new PrintWriter(new FileWriter(outputFile));
    }
}
