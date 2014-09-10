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

package org.apache.beehive.controls.system.jdbc;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ArrayType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.MirroredTypeException;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.VoidType;
import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.bean.ControlChecker;
import org.apache.beehive.controls.system.jdbc.parser.ParameterChecker;
import org.apache.beehive.controls.system.jdbc.parser.SqlParser;
import org.apache.beehive.controls.system.jdbc.parser.SqlStatement;

import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Locale;
import java.text.MessageFormat;

/**
 * Annotation checker for the JdbcControl.  Invoked at compile time by the controls framework.
 */
public class JdbcControlChecker implements ControlChecker {

    private Locale _locale;

    /**
     * Invoked by the control build-time infrastructure to process a declaration of
     * a control extension (ie, an interface annotated with @ControlExtension), or
     * a field instance of a control type.
     */
    public void check(Declaration decl, AnnotationProcessorEnvironment env) {

        _locale = Locale.getDefault();

        if (decl instanceof TypeDeclaration) {

            //
            // Check method annotations
            //
            Collection<? extends MethodDeclaration> methods = ((TypeDeclaration) decl).getMethods();
            for (MethodDeclaration method : methods) {
                checkSQL(method, env);

            }
        } else if (decl instanceof FieldDeclaration) {

            //
            // NOOP
            //
        } else {

            //
            // NOOP
            //
        }
    }

    /**
     * Check the SQL method annotation.  Lots to check here, stop checking as soon as an error is found.
     *
     * @param method Method to check.
     * @param env Processor env.
     */
    private void checkSQL(MethodDeclaration method, AnnotationProcessorEnvironment env) {

        final JdbcControl.SQL methodSQL = method.getAnnotation(JdbcControl.SQL.class);
        if (methodSQL == null) {
            return;
        }

        //
        // check for empty SQL statement member
        //
        if (methodSQL.statement() == null || methodSQL.statement().length() == 0) {
            env.getMessager().printError(method.getPosition(),
                                         getResourceString("jdbccontrol.empty.statement", method.getSimpleName()));
            return;
        }

        //
        // Make sure maxrows is not set to some negative number other than -1
        //
        int maxRows = methodSQL.maxRows();
        if (maxRows < JdbcControl.MAXROWS_ALL) {
            env.getMessager().printError(method.getPosition(),
                                         getResourceString("jdbccontrol.bad.maxrows", method.getSimpleName(),maxRows));
            return;
        }

        //
        //
        // parse the SQL
        //
        //
        SqlParser _p = new SqlParser();
        SqlStatement _statement;
        try {
            _statement = _p.parse(methodSQL.statement());
        } catch (ControlException ce) {
            env.getMessager().printError(method.getPosition(),  getResourceString("jdbccontrol.bad.parse",
                                                                                  method.getSimpleName(),
                                                                                  ce.toString()));
            return;
        }

        //
        // Check that the any statement element params (delimited by '{' and '}' can be
        // matched to method parameter names. NOTE: This check is only valid on non-compiled files,
        // once compiled to a class file method parameter names are replaced with 'arg0', 'arg1', etc.
        // and cannot be used for this check.
        //
        try {
            ParameterChecker.checkReflectionParameters(_statement, method);
        } catch (ControlException e) {
            env.getMessager().printError(method.getPosition(), e.getMessage());
            return;
        }

        //
        // check for case of generatedKeyColumns being set, when getGeneratedKeys is not set to true
        //
        final boolean getGeneratedKeys = methodSQL.getGeneratedKeys();
        final String[] generatedKeyColumnNames = methodSQL.generatedKeyColumnNames();
        final int[] generatedKeyIndexes = methodSQL.generatedKeyColumnIndexes();
        if (!getGeneratedKeys && (generatedKeyColumnNames.length != 0 || generatedKeyIndexes.length != 0)) {
            env.getMessager().printError(method.getPosition(),
                                         getResourceString("jdbccontrol.genkeys", method.getSimpleName()));
            return;
        }

        //
        // check that both generatedKeyColumnNames and generatedKeyColumnIndexes are not set
        //
        if (generatedKeyColumnNames.length > 0 && generatedKeyIndexes.length > 0) {
            env.getMessager().printError(method.getPosition(),
                                         getResourceString("jdbccontrol.genkeycolumns", method.getSimpleName()));

            return;
        }

        //
        // batch update methods must return int[]
        //
        final boolean batchUpdate = methodSQL.batchUpdate();
        final TypeMirror returnType = method.getReturnType();
        if (batchUpdate) {
            if (returnType instanceof ArrayType) {
                final TypeMirror aType = ((ArrayType) returnType).getComponentType();
                if (aType instanceof PrimitiveType == false
                        || ((PrimitiveType) aType).getKind() != PrimitiveType.Kind.INT) {
                    env.getMessager().printError(method.getPosition(),
                                                 getResourceString("jdbccontrol.batchupdate", method.getSimpleName()));
                    return;
                }
            } else if (returnType instanceof VoidType == false) {
                env.getMessager().printError(method.getPosition(),
                                             getResourceString("jdbccontrol.batchupdate", method.getSimpleName()));
                return;
            }

        }

        //
        // iterator type check match
        //
        if (returnType instanceof InterfaceType) {
            String iName = ((InterfaceType) returnType).getDeclaration().getQualifiedName();
            if ("java.util.Iterator".equals(iName)) {
                String iteratorClassName = null;
                try {
                    // this should always except
                    methodSQL.iteratorElementType();
                } catch (MirroredTypeException mte) {
                    iteratorClassName = mte.getQualifiedName();
                }

                if ("org.apache.beehive.controls.system.jdbc.JdbcControl.UndefinedIteratorType".equals(iteratorClassName)) {
                    env.getMessager().printError(method.getPosition(),
                                                 getResourceString("jdbccontrol.iterator.returntype",
                                                                   method.getSimpleName()));
                    return;
                }
            }
        }

        //
        // scrollable result set check
        //
        final JdbcControl.ScrollType scrollable = methodSQL.scrollableResultSet();
        switch (scrollable) {
            case SCROLL_INSENSITIVE:
            case SCROLL_SENSITIVE:
            case SCROLL_INSENSITIVE_UPDATABLE:
            case SCROLL_SENSITIVE_UPDATABLE:
            case FORWARD_ONLY_UPDATABLE:
                String typeName = null;
                if (returnType instanceof DeclaredType) {
                    typeName = ((DeclaredType) returnType).getDeclaration().getQualifiedName();
                }

                if (typeName == null || !"java.sql.ResultSet".equals(typeName)) {
                    env.getMessager().printError(method.getPosition(),
                                                 getResourceString("jdbccontrol.scrollresultset",
                                                                   method.getSimpleName()));
                    return;
                }
            case FORWARD_ONLY:
            default:
                break;
        }

        return;
    } // checkSQL

    private String getResourceString( String id, Object... args )
    {
        ResourceBundle rb = ResourceBundle.getBundle(this.getClass().getPackage().getName() + ".strings", _locale );
        String pattern = rb.getString(id);
        return MessageFormat.format(pattern, args);
    }
}
