package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.classgen.SQLParser;
import com.moparisthebest.classgen.SimpleSQLParser;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.*;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.moparisthebest.jdbc.TryClose.tryClose;

/**
 * Created by mopar on 5/24/17.
 */
@SupportedAnnotationTypes("com.moparisthebest.jdbc.codegen.JdbcMapper.Mapper")
@SupportedSourceVersion(SourceVersion.RELEASE_5)
public class JdbcMapperProcessor extends AbstractProcessor {

	private static final Pattern paramPattern = Pattern.compile("\\{([^}]+)\\}");
	private static final CompileTimeResultSetMapper rsm = new CompileTimeResultSetMapper();

	private Types types;
	private TypeMirror sqlExceptionType, stringType, numberType, utilDateType, readerType, clobType,
			byteArrayType, inputStreamType, fileType, blobType
			//, clobStringType, blobStringType,  arrayListObjectType
			;

	public JdbcMapperProcessor() {
		//out.println("JdbcMapperProcessor running!");
	}

	@Override
	public synchronized void init(final ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.types = processingEnv.getTypeUtils();
		final Elements elements = processingEnv.getElementUtils();
		sqlExceptionType = elements.getTypeElement(SQLException.class.getCanonicalName()).asType();
		stringType = elements.getTypeElement(String.class.getCanonicalName()).asType();
		numberType = elements.getTypeElement(Number.class.getCanonicalName()).asType();
		utilDateType = elements.getTypeElement(java.util.Date.class.getCanonicalName()).asType();
		readerType = elements.getTypeElement(Reader.class.getCanonicalName()).asType();
		clobType = elements.getTypeElement(Clob.class.getCanonicalName()).asType();
		inputStreamType = elements.getTypeElement(InputStream.class.getCanonicalName()).asType();
		fileType = elements.getTypeElement(File.class.getCanonicalName()).asType();
		blobType = elements.getTypeElement(Blob.class.getCanonicalName()).asType();
		// throws NPE:
		//byteArrayType = elements.getTypeElement(byte[].class.getCanonicalName()).asType();
		//byteArrayType = this.types.getArrayType(elements.getTypeElement(byte.class.getCanonicalName()).asType());
		//byteArrayType = elements.getTypeElement(byte.class.getCanonicalName()).asType();
		byteArrayType = types.getArrayType(types.getPrimitiveType(TypeKind.BYTE));
		/*
		clobStringType = elements.getTypeElement(ClobString.class.getCanonicalName()).asType();
		blobStringType = elements.getTypeElement(BlobString.class.getCanonicalName()).asType();
		arrayListObjectType = elements.getTypeElement(ArrayInList.ArrayListObject.class.getCanonicalName()).asType();
		*/
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		//if(true) return false;
		if (annotations.isEmpty() || roundEnv.processingOver()) {
			// write out test classes
			return false;
		}
		if (isInitialized())
			//System.out.println("annotations: " + annotations);
			//System.out.println("roundEnv: " + roundEnv);
			for (final Element element : roundEnv.getElementsAnnotatedWith(JdbcMapper.Mapper.class))
				try {
					if (element.getKind() != ElementKind.CLASS && element.getKind() != ElementKind.INTERFACE && !element.getModifiers().contains(Modifier.ABSTRACT)) {
						processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@JdbcMapper.Mapper can only annotate an interface or abstract class", element);
						continue;
					}
					final TypeElement genClass = (TypeElement) element;
					final JdbcMapper.Mapper mapper = genClass.getAnnotation(JdbcMapper.Mapper.class);
					final SQLParser parser = new SimpleSQLParser();//(SQLParser)Class.forName(mapper.sqlParser().getCanonicalName()).newInstance();
					final String qualifiedName = genClass.getQualifiedName().toString();
					final boolean isInterface = genClass.getKind().isInterface();
					final boolean doJndi = !mapper.jndiName().isEmpty();
					Writer w = null;
					try {
						w = processingEnv.getFiler().createSourceFile(qualifiedName + JdbcMapperFactory.SUFFIX).openWriter();
						final String packageName = ((PackageElement) genClass.getEnclosingElement()).getQualifiedName().toString();
						final String className = genClass.getSimpleName() + JdbcMapperFactory.SUFFIX;
						if (!packageName.isEmpty()) {
							w.write("package ");
							w.write(packageName);
							w.write(";\n\n");
						}
						if (doJndi) {
							w.write("import javax.naming.InitialContext;\n");
							w.write("import javax.sql.DataSource;\n");
						}
						w.write("import java.sql.*;\n\n");
						w.write("import static com.moparisthebest.jdbc.TryClose.tryClose;\n\n");
						w.write("public class ");
						w.write(className);
						if (isInterface) {
							w.write(" implements ");
						} else {
							w.write(" extends ");
						}
						w.write(genClass.getSimpleName().toString());
						w.write(" {\n\n\tprivate final Connection conn;\n");
						if (doJndi) {
							w.write("\tprivate final InitialContext ctx;\n\n\tpublic ");
							w.write(className);
							w.write("() {\n\t\tthis(null);\n\t}\n");
						}
						w.write("\n\tpublic ");
						w.write(className);
						w.write("(Connection conn) {\n\t\t");
						if (doJndi) {
							w.write("InitialContext ctx = null;\n" +
									"\t\tif (conn == null)\n" +
									"\t\t\ttry {\n" +
									"\t\t\t\tctx = new InitialContext();\n" +
									"\t\t\t\tDataSource ds = (DataSource) ctx.lookup(\"");
							w.write(mapper.jndiName()); // todo: escape this? I don't think anyone needs that, for now...
							w.write("\");\n" +
									"\t\t\t\tconn = ds.getConnection();\n" +
									"\t\t\t} catch (Throwable e) {\n" +
									"\t\t\t\ttryClose(ctx);\n" +
									"\t\t\t\ttryClose(conn);\n" +
									"\t\t\t\tthrow new RuntimeException(e);\n" +
									"\t\t\t}\n" +
									"\t\tthis.conn = conn;\n" +
									"\t\tthis.ctx = ctx;"
							);
						} else {
							w.write("this.conn = conn;");
						}
						w.write("\n\t\tif (this.conn == null)\n" +
								"\t\t\tthrow new NullPointerException(\"Connection needs to be non-null for JdbcMapper...\");\n\t}\n" +
								"\n\t@Override\n\tpublic Connection getConnection() {\n\t\treturn this.conn;\n\t}\n"
						);

						// loop through methods
						final Types typeUtils = processingEnv.getTypeUtils();
						int cachedPreparedStatements = 0;
						for (final Element methodElement : genClass.getEnclosedElements()) {
							// can only implement abstract methods
							if (methodElement.getKind() != ElementKind.METHOD || !methodElement.getModifiers().contains(Modifier.ABSTRACT))
								continue;
							final ExecutableElement eeMethod = (ExecutableElement) methodElement;
							final JdbcMapper.SQL sql = eeMethod.getAnnotation(JdbcMapper.SQL.class);
							if (sql == null || sql.value().isEmpty()) {
								processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@JdbcMapper.SQL with non-empty query is required on abstract or interface methods", methodElement);
								continue;
							}
							w.write("\n\t@Override\n\tpublic ");
							final String returnType = eeMethod.getReturnType().toString();
							w.write(returnType);
							w.write(" ");
							w.write(eeMethod.getSimpleName().toString());
							w.write('(');

							// build query and bind param order
							final List<VariableElement> bindParams = new ArrayList<VariableElement>();
							final String sqlStatement;
							boolean sqlExceptionThrown = false;
							{
								// now parameters
								final List<? extends VariableElement> params = eeMethod.getParameters();
								final Map<String, VariableElement> paramMap = new HashMap<String, VariableElement>(params.size());
								final int numParams = params.size();
								int count = 0;
								for (final VariableElement param : params) {
									w.write("final ");
									w.write(param.asType().toString());
									w.write(' ');
									final String name = param.getSimpleName().toString();
									w.write(name);
									paramMap.put(name, param);
									if (++count != numParams)
										w.write(", ");
								}

								// throws?
								w.write(")");
								final List<? extends TypeMirror> thrownTypes = eeMethod.getThrownTypes();
								final int numThrownTypes = thrownTypes.size();
								if (numThrownTypes > 0) {
									count = 0;
									w.write(" throws ");
								}
								for (final TypeMirror thrownType : thrownTypes) {
									sqlExceptionThrown |= typeUtils.isSameType(thrownType, sqlExceptionType);
									w.write(thrownType.toString());
									if (++count != numThrownTypes)
										w.write(", ");
								}
								w.write(" {\n");

								final Matcher bindParamMatcher = paramPattern.matcher(sql.value());
								final StringBuffer sb = new StringBuffer();
								while (bindParamMatcher.find()) {
									final String paramName = bindParamMatcher.group(1);
									final VariableElement bindParam = paramMap.get(paramName);
									if (bindParam == null) {
										processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("@JdbcMapper.SQL sql has bind param '%s' not in method parameter list", paramName), methodElement);
										continue;
									}
									bindParams.add(bindParam);
									bindParamMatcher.appendReplacement(sb, "?");
								}
								bindParamMatcher.appendTail(sb);
								sqlStatement = sb.toString();
							}

							final SQLParser parsedSQl = parser.parse(sqlStatement);
							// now implementation
							w.write("\t\tPreparedStatement ps = null;\n");
							if (parsedSQl.isSelect())
								w.write("\t\tResultSet rs = null;\n");
							w.write("\t\ttry {\n\t\t\tps = ");
							final boolean cachePreparedStatements = sql.cachePreparedStatement().combine(mapper.cachePreparedStatements());
							if (cachePreparedStatements) {
								w.write("this.prepareStatement(");
								w.write(Integer.toString(cachedPreparedStatements));
								w.write(", ");
								++cachedPreparedStatements;
							} else {
								w.write("conn.prepareStatement(");
							}
							w.write('"');
							w.write(sqlStatement);
							w.write("\");\n");

							// now bind parameters
							int count = 0;
							for (final VariableElement param : bindParams)
								setObject(w, ++count, param.getSimpleName().toString(), param.asType());

							if (!parsedSQl.isSelect()) {
								if (returnType.equals("void")) {
									w.write("\t\t\tps.executeUpdate();\n");
								} else if (returnType.equals("int") || returnType.equals("java.lang.Integer")) {
									w.write("\t\t\treturn ps.executeUpdate();\n");
								} else {
									processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@JdbcMapper.SQL sql other than SELECT must return either void, int, or Integer", methodElement);
									continue;
								}
							} else {
								w.write("\t\t\trs = ps.executeQuery();\n");
								final String[] keys = parsedSQl.columnNames();
								if (keys == null || keys.length < 2) {
									processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@JdbcMapper.SQL sql parsed no column names, proper SQL? Wildcard? or bad parser?", methodElement);
									continue;
								}
								for (final String key : keys)
									if ("*".equals(key)) {
										processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@JdbcMapper.SQL sql parsed a wildcard column name which is not supported", methodElement);
										return false;
									}
								rsm.mapToResultType(w, keys, eeMethod, sql.arrayMaxLength(), null);
							}

							// if no SQLException is thrown, we have to catch it here and wrap it with RuntimeException...
							if (!sqlExceptionThrown) {
								w.write("\t\t} catch(SQLException e) {\n\t\t\tthrow new RuntimeException(e);\n");
							}

							// close things
							w.write("\t\t} finally {\n");
							if (parsedSQl.isSelect())
								w.write("\t\t\ttryClose(rs);\n");
							if (!cachePreparedStatements)
								w.write("\t\t\ttryClose(ps);\n");
							w.write("\t\t}\n");

							w.write("\t}\n");
						}

						if (cachedPreparedStatements > 0) {
							w.write("\n\tprivate final PreparedStatement[] psCache = new PreparedStatement[");
							w.write(Integer.toString(cachedPreparedStatements));
							w.write("];\n\n\tprivate PreparedStatement prepareStatement(final int index, final String sql) throws SQLException {\n" +
									"\t\tfinal PreparedStatement ps = psCache[index];\n" +
									"\t\treturn ps == null ? (psCache[index] = conn.prepareStatement(sql)) : ps;\n" +
									"\t}\n");
						}

						// close method
						w.write("\n\t@Override\n\tpublic void close() {\n\t\t");
						if (cachedPreparedStatements > 0)
							w.write("for(final PreparedStatement ps : psCache)\n\t\t\ttryClose(ps);\n\t\t");
						w.write("tryClose(conn);\n");
						if (doJndi)
							w.write("\t\ttryClose(ctx);\n");
						w.write("\t}\n");
						// end close method

						w.write("}\n");
					} finally {
						tryClose(w);
					}
				} catch (Exception e) {
					processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage(), element);
					return false;
				}
		return true;
	}

	private void setObject(final Writer w, final int index, String variableName, final TypeMirror o) throws SQLException, IOException {
		w.write("\t\t\t");
		// we are going to put most common ones up top so it should execute faster normally
		String method = null;
		// todo: avoid string concat here
		if (o.getKind().isPrimitive() || types.isAssignable(o, stringType) || types.isAssignable(o, numberType)) {
			method = "Object";
			// java.util.Date support, put it in a Timestamp
		} else if (types.isAssignable(o, utilDateType)) {
			method = "Object";
			// might need to wrap with Timestamp
			if (types.isSameType(o, utilDateType))
				variableName = "new java.sql.Timestamp(" + variableName + ".getTime())";
			// CLOB support
		} else if (types.isAssignable(o, readerType) || types.isAssignable(o, clobType)) {
			method = "Clob";
		/*
		} else if (o instanceof ClobString) {
			ps.setObject(index, ((ClobString) o).s == null ? null : ((ClobString) o).s);
		*/
			// BLOB support
		} else if (types.isAssignable(o, byteArrayType)) {
			method = "Blob";
			variableName = "new java.io.ByteArrayInputStream(" + variableName + ")";
		} else if (types.isAssignable(o, inputStreamType) || types.isAssignable(o, blobType)) {
			method = "Blob";
		} else if (types.isAssignable(o, fileType)) {
			// todo: does this close the InputStream properly????
			w.write("\t\t\ttry {\n" +
					"\t\t\t\tps.setBlob(" + index + ", new java.io.FileInputStream(" + variableName + "));\n" +
					"\t\t\t} catch (java.io.FileNotFoundException e) {\n" +
					"\t\t\t\tthrow new SQLException(\"File to Blob FileNotFoundException\", e);\n" +
					"\t\t\t}");
			return;
			/*
		} else if (o instanceof BlobString) {
			try {
				ps.setBlob(index, ((BlobString) o).s == null ? null : new ByteArrayInputStream(((BlobString) o).s.getBytes("UTF-8")));
			} catch (UnsupportedEncodingException e) {
				throw new SQLException("String to Blob UnsupportedEncodingException", e);
			}
		} else if (o instanceof ArrayInList.ArrayListObject) {
			ps.setArray(index, ((ArrayInList.ArrayListObject) o).getArray());
			*/
		} else {
			// probably won't get here ever, but just in case...
			method = "Object";
		}
		w.write("ps.set");
		w.write(method);
		w.write('(');
		w.write(Integer.toString(index));
		w.write(", ");
		w.write(variableName);
		w.write(");\n");
	}

	public static Class<?> typeMirrorToClass(final TypeMirror tm) throws ClassNotFoundException {
		switch (tm.getKind()) {
			case BOOLEAN:
				return boolean.class;
			case BYTE:
				return byte.class;
			case SHORT:
				return short.class;
			case INT:
				return int.class;
			case LONG:
				return long.class;
			case CHAR:
				return char.class;
			case FLOAT:
				return float.class;
			case DOUBLE:
				return double.class;
			default:
				return Class.forName(tm.toString());
		}
	}
}
