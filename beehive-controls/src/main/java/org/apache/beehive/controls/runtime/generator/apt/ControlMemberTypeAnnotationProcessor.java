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
package org.apache.beehive.controls.runtime.generator.apt;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.beehive.controls.api.bean.AnnotationMemberTypes;
import org.apache.beehive.controls.runtime.generator.AptControlInterface;
import org.apache.beehive.controls.runtime.generator.apt.TwoPhaseAnnotationProcessor;
import org.apache.beehive.controls.runtime.bean.AnnotationConstraintValidator;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.type.VoidType;

public class ControlMemberTypeAnnotationProcessor extends TwoPhaseAnnotationProcessor
{
    /**
     * @param atds
     * @param env
     */
    public ControlMemberTypeAnnotationProcessor(
            Set<AnnotationTypeDeclaration> atds,
            AnnotationProcessorEnvironment env)
    {
        super(atds, env);
    }

    public void check()
    {
        super.check();
        
    }
    public void check(Declaration decl)
    {
        if (decl.getAnnotation(AnnotationMemberTypes.Date.class) != null)
        {
        	checkDate(decl);
        }
    }

    public void generate(Declaration decl)
    {
    }

    public void checkDate(Declaration decl)
    {
    	AnnotationMemberTypes.Date date = decl.getAnnotation(AnnotationMemberTypes.Date.class);
    	
    	try
    	{
    		String dateValue = date.minValue();
    		String format = date.format();
    		
        	//Validate the date format specified
    		SimpleDateFormat sdFormat = new SimpleDateFormat(date.format());

        	//Validate that the date specified is in the specified format.
    		if (dateValue != null && dateValue.length() > 0)
    			AnnotationConstraintValidator.parseDate(format, dateValue);
    		dateValue = date.maxValue();
    		if (dateValue != null && dateValue.length() > 0)
    			AnnotationConstraintValidator.parseDate(format, dateValue);
    	} 
    	catch (ParseException pe)
    	{
    		printError( decl, "control.member.type.invalid.date.value.error");
    	}
    	catch (Exception e)
    	{
    		printError( decl, "control.member.type.invalid.date.format.error");    		
    	}

    }
}
