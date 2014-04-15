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
package org.apache.beehive.controls.test.controls.assembly;

import org.apache.beehive.controls.api.assembly.ControlAssembler;
import org.apache.beehive.controls.api.assembly.ControlAssemblyContext;
import org.apache.beehive.controls.api.assembly.ControlAssemblyException;

import java.io.File;
import java.io.FileWriter;

public class AssemblyTestAssembler2 implements ControlAssembler {

    public void assemble(ControlAssemblyContext cac) throws ControlAssemblyException {
        
        String genPackageName = "org.apache.beehive.controls.test.assembly.generated";
        String genClassName = "AssemblyTest2Generated";

        /* Write basic class structure out for later
           compiliation and then finally instantiation in a test */
        try {
            File dir = new File(cac.getSrcOutputDir(), genPackageName.replace(".", File.separator));
            dir.mkdirs();

            FileWriter fw = new FileWriter(new File(dir, genClassName + ".java"));
            fw.write("package " + genPackageName + ";" +
                    "public class " + genClassName + "{ }");
            fw.close();
        }
        catch (java.io.IOException ioe) {
            throw new ControlAssemblyException("Error writing " +
                    genPackageName.replace(".", File.separator) +
                    File.separator + genClassName, ioe);
        }
    }
}
