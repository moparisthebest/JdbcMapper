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
package org.apache.beehive.netui.pageflow.internal;

import org.apache.beehive.netui.pageflow.PageFlowManagedObject;
import org.apache.beehive.netui.pageflow.internal.annotationreader.ProcessedAnnotation;
import org.apache.beehive.netui.util.logging.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CachedSharedFlowRefInfo
{
    private static final Logger LOG = Logger.getInstance( CachedSharedFlowRefInfo.class );

    public static class SharedFlowFieldInfo
    {
        public Field field;
        public String sharedFlowName;
    }

    /**
     * The SharedFlowController-initialized member fields -- may or may not be present.
     */
    private SharedFlowFieldInfo[] _sharedFlowMemberFields;

    protected void initSharedFlowFields( AnnotationReader annReader, Class clazz )
    {
        if (annReader == null) {
            LOG.error("Null AnnotationReader for checking shared flow ref annotations");
            return;
        }
        if (clazz == null) {
            LOG.error("Null class for checking shared flow ref annotations");
            return;
        }

        //
        // Go through this class and all superclasses, looking for shared fields.
        // Make sure that a superclass control field never replaces a subclass
        // control field (this is what the 'fieldNames' HashSet is for).
        // Note that the annnotation reader doesn't change per-class.
        // Inherited annotated elements are all read.
        //
        HashSet fieldNames = new HashSet();
        List/*< SharedFlowFieldInfo >*/ sharedFlowFields = null;
        do {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String fieldName = field.getName();
                ProcessedAnnotation sharedFlowFieldAnn = annReader.getJpfAnnotation(field, "SharedFlowField");
                if (! fieldNames.contains(fieldName)) {
                    if (sharedFlowFieldAnn != null) {
                        if (! Modifier.isPublic(field.getModifiers())) {
                            field.setAccessible(true);
                        }
                        if (sharedFlowFields == null) {
                            sharedFlowFields = new ArrayList/*< SharedFlowFieldInfo >*/();
                        }
                        SharedFlowFieldInfo info = new SharedFlowFieldInfo();
                        info.field = field;
                        info.sharedFlowName = AnnotationReader.getStringAttribute(sharedFlowFieldAnn, "name");
                        sharedFlowFields.add(info);
                        fieldNames.add( fieldName );
                    }
                    else if (field.getName().equals(InternalConstants.GLOBALAPP_MEMBER_NAME)) {
                        // Legacy behavior: initialize a field called 'globalApp' with the Global.app instance.
                        if (! Modifier.isPublic(field.getModifiers())) {
                            field.setAccessible(true);
                        }
                        if (sharedFlowFields == null) {
                            sharedFlowFields = new ArrayList/*< SharedFlowFieldInfo >*/();
                        }
                        SharedFlowFieldInfo info = new SharedFlowFieldInfo();
                        info.field = field;
                        info.sharedFlowName = null;
                        sharedFlowFields.add(info);
                        fieldNames.add( fieldName );
                    }
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null
                 && !PageFlowManagedObject.class.equals(clazz)
                 && PageFlowManagedObject.class.isAssignableFrom(clazz));

        if (sharedFlowFields != null) {
            _sharedFlowMemberFields = (SharedFlowFieldInfo[]) sharedFlowFields.toArray(new SharedFlowFieldInfo[ sharedFlowFields.size() ]);
        }
    }

    public SharedFlowFieldInfo[] getSharedFlowMemberFields()
    {
        return _sharedFlowMemberFields;
    }
}
