package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.Finishable;
import com.moparisthebest.jdbc.ResultSetMapper;
import com.moparisthebest.jdbc.util.ResultSetIterable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.util.*;
//IFJAVA8_START
import java.util.stream.Stream;
//IFJAVA8_END

import static com.moparisthebest.jdbc.codegen.JdbcMapperProcessor.typeMirrorStringNoGenerics;
import static com.moparisthebest.jdbc.codegen.JdbcMapperProcessor.typeMirrorToClass;

/**
 * Created by mopar on 5/26/17.
 */
public class CompileTimeResultSetMapper {

	public static final SourceVersion RELEASE_8;

	static {
		SourceVersion rl8 = null;
		try {
			rl8 = SourceVersion.valueOf("RELEASE_8");
		} catch(Throwable e) {
			// ignore
		}
		RELEASE_8 = rl8;
	}

	public final Types types;
	public final TypeMirror collectionType, mapType, mapCollectionType, iteratorType, listIteratorType, finishableType, resultSetType, resultSetIterableType;
	//IFJAVA8_START
	public final TypeMirror streamType;
	//IFJAVA8_END
	private final boolean java8;

	public CompileTimeResultSetMapper(final ProcessingEnvironment processingEnv) {
		types = processingEnv.getTypeUtils();
		final Elements elements = processingEnv.getElementUtils();

		// is this the proper way to do this?
		java8 = RELEASE_8 != null && processingEnv.getSourceVersion().ordinal() >= RELEASE_8.ordinal();

		collectionType = types.getDeclaredType(elements.getTypeElement(Collection.class.getCanonicalName()), types.getWildcardType(null, null));
		mapType = types.getDeclaredType(elements.getTypeElement(Map.class.getCanonicalName()), types.getWildcardType(null, null), types.getWildcardType(null, null));
		mapCollectionType = types.getDeclaredType(elements.getTypeElement(Map.class.getCanonicalName()), types.getWildcardType(null, null), types.getWildcardType(collectionType, null));

		iteratorType = types.getDeclaredType(elements.getTypeElement(Iterator.class.getCanonicalName()), types.getWildcardType(null, null));
		listIteratorType = types.getDeclaredType(elements.getTypeElement(ListIterator.class.getCanonicalName()), types.getWildcardType(null, null));

		finishableType = elements.getTypeElement(Finishable.class.getCanonicalName()).asType();
		resultSetType = elements.getTypeElement(ResultSet.class.getCanonicalName()).asType();

		resultSetIterableType = types.getDeclaredType(elements.getTypeElement(ResultSetIterable.class.getCanonicalName()), types.getWildcardType(null, null));

		//IFJAVA8_START
		streamType = types.getDeclaredType(elements.getTypeElement(Stream.class.getCanonicalName()), types.getWildcardType(null, null));
		//IFJAVA8_END
	}

	public static String getConcreteClassCanonicalName(final TypeMirror returnType, final Class defaultConcreteClass) {
		final Set<Modifier> modifiers = ((DeclaredType) returnType).asElement().getModifiers();
		if (modifiers.contains(Modifier.ABSTRACT)) { // todo: no interface?
			try {
				final Class concrete = ResultSetMapper.interfaceToConcrete.get(typeMirrorToClass(returnType));
				return (concrete == null ? defaultConcreteClass : concrete).getCanonicalName();
			} catch (ClassNotFoundException e) {
				// ignore?
			}
		}
		return typeMirrorStringNoGenerics(returnType);
	}

	/**
	 *
	 * @return true if calling code should close rs (ResultSet) and ps (PreparedStatement) if closePs is false, false otherwise
	 */
	public boolean mapToResultType(final Writer w, final String[] keys, final ExecutableElement eeMethod, final MaxRows maxRows, final String cal, final String cleaner, final boolean closePs) throws IOException, NoSuchMethodException, ClassNotFoundException {
		//final Method m = fromExecutableElement(eeMethod);
		//final Class returnType = m.getReturnType();
		final TypeMirror returnTypeMirror = eeMethod.getReturnType();
		//final Class returnType = typeMirrorToClass(returnTypeMirror);
		if (returnTypeMirror.getKind() == TypeKind.ARRAY) {
			final TypeMirror componentType = ((ArrayType) returnTypeMirror).getComponentType();
			toArray(w, keys, componentType, maxRows, cal, cleaner);
		} else if (types.isAssignable(returnTypeMirror, collectionType)) {
			final List<? extends TypeMirror> typeArguments = ((DeclaredType) returnTypeMirror).getTypeArguments();
			toCollection(w, keys, returnTypeMirror, typeArguments.get(0), maxRows, cal, cleaner);
		} else if (types.isAssignable(returnTypeMirror, mapType)) {
			final List<? extends TypeMirror> typeArguments = ((DeclaredType) returnTypeMirror).getTypeArguments();
			//if (types[1] instanceof ParameterizedType) { // for collectionMaps
			if (types.isAssignable(returnTypeMirror, mapCollectionType)) { // for collectionMaps
				final TypeMirror collectionTypeMirror = typeArguments.get(1);
				final TypeMirror componentTypeMirror = ((DeclaredType) collectionTypeMirror).getTypeArguments().get(0);
				toMapCollection(w, keys,
						returnTypeMirror,
						typeArguments.get(0),
						collectionTypeMirror,
						componentTypeMirror,
						maxRows, cal, cleaner);
				return true;
			}
			toMap(w, keys, returnTypeMirror, typeArguments.get(0), typeArguments.get(1), maxRows, cal, cleaner);
		} else if (types.isAssignable(returnTypeMirror, iteratorType)) {
			final List<? extends TypeMirror> typeArguments = ((DeclaredType) returnTypeMirror).getTypeArguments();
			if (types.isAssignable(returnTypeMirror, resultSetIterableType)) {
				toResultSetIterable(w, keys, typeArguments.get(0), cal, cleaner, closePs);
				return false;
			} else if (types.isAssignable(returnTypeMirror, listIteratorType))
				toListIterator(w, keys, typeArguments.get(0), maxRows, cal, cleaner);
			else
				toIterator(w, keys, typeArguments.get(0), maxRows, cal, cleaner);
		}
		//IFJAVA8_START
		else if (types.isAssignable(returnTypeMirror, streamType)) {
			toStream(w, keys, ((DeclaredType) returnTypeMirror).getTypeArguments().get(0), cal, cleaner, closePs);
			return false;
		}
		//IFJAVA8_END
		else {
			toObject(w, keys, returnTypeMirror, cal, cleaner);
		}
		return true;
	}

	public CompileTimeRowToObjectMapper getRowMapper(final String[] keys, TypeMirror returnTypeClass, String cal, TypeMirror mapValType, TypeMirror mapKeyType) {
		return getRowMapper(keys, returnTypeClass, "rs", cal, mapValType, mapKeyType);
	}

	public CompileTimeRowToObjectMapper getRowMapper(final String[] keys, TypeMirror returnTypeClass, final String resultSetName, String cal, TypeMirror mapValType, TypeMirror mapKeyType) {
		return new CompileTimeRowToObjectMapper(this, keys, returnTypeClass, resultSetName, cal, mapValType, mapKeyType);
	}

	public void writeObject(final Writer w, final String[] keys, final TypeMirror returnTypeMirror, final String cal) throws IOException, ClassNotFoundException {
		writeObject(w, keys, returnTypeMirror, "rs", cal);
	}

	public void writeObject(final Writer w, final String[] keys, final TypeMirror returnTypeMirror, final String resultSetName, final String cal) throws IOException, ClassNotFoundException {
		getRowMapper(keys, returnTypeMirror, resultSetName, cal, null, null).gen(w, returnTypeMirror.toString());
	}

	public void toObject(final Writer w, final String[] keys, final TypeMirror returnTypeMirror, final String cal, final String cleaner) throws IOException, ClassNotFoundException {
		w.write("\t\t\tif(rs.next()) {\n");
		writeObject(w, keys, returnTypeMirror, cal);
		w.write("\t\t\t\treturn ");
		// this does not clean null on purpose, neither does CleaningResultSetMapper
		clean(w, cleaner).write(";\n\t\t\t} else {\n\t\t\t\treturn null;\n\t\t\t}\n");
	}

	private void toResultSetIterable(final Writer w, final String[] keys, final TypeMirror returnTypeMirror, final String cal, final String cleaner, final boolean closePs) throws IOException, ClassNotFoundException {
		w.write("\t\t\treturn com.moparisthebest.jdbc.util.ResultSetIterable.getResultSetIterable(rs,\n\t\t\t\t\trs.next() ? ");

		if(java8) {
			w.append("(_rs, _cal) -> {\n");
		} else {
			final String returnTypeString = returnTypeMirror.toString();
			w.append("new com.moparisthebest.jdbc.util.ResultSetToObject<")
					.append(returnTypeString).append(">() {\n\t\t\t\t\tpublic ")
					.append(returnTypeString).append(" toObject(final ResultSet _rs, final java.util.Calendar _cal) throws SQLException {\n");
		}

		// com.moparisthebest.jdbc.util.ResultSetToObject implementation
		writeObject(w, keys, returnTypeMirror, "_rs", cal == null ? null : "_cal");
		w.write("\t\t\t\t\t\treturn ");
		clean(w, cleaner).write(";\n");
		// end ResultSetToObject implementation

		if(!java8)
			w.write("\t\t\t\t\t}\n");

		w.append("\t\t\t\t\t}\n\t\t\t\t: null, ").append(cal == null ? "null" : cal).append(")");
		if(closePs)
			w.append(".setPreparedStatementToClose(ps)");
		w.append(";\n");
	}

	//IFJAVA8_START

	// being in this method implies java8 is true already, how else could you be compiling code using Stream? so this won't have the checks for lambdas toResultSetIterable does...
	private void toStream(final Writer w, final String[] keys, final TypeMirror returnTypeMirror, final String cal, final String cleaner, final boolean closePs) throws IOException, ClassNotFoundException {

		if(closePs)
			w.write("\t\t\tfinal PreparedStatement finalPs = ps;\n");

		w.write("\t\t\treturn com.moparisthebest.jdbc.util.ResultSetIterable.getStream(rs,\n\t\t\t\t\trs.next() ? ");

		w.append("(_rs, _cal) -> {\n");

		// com.moparisthebest.jdbc.util.ResultSetToObject implementation
		writeObject(w, keys, returnTypeMirror, "_rs", cal == null ? null : "_cal");
		w.write("\t\t\t\t\t\treturn ");
		clean(w, cleaner).write(";\n");
		// end ResultSetToObject implementation

		w.append("\t\t\t\t\t}\n\t\t\t\t: null, ").append(cal == null ? "null" : cal).append(")");
		if(closePs)
			w.append(".onClose(() -> tryClose(finalPs))");
		w.append(";\n");
	}

	//IFJAVA8_END

	public void writeCollection(final Writer w, final String[] keys, final String returnTypeString, final String concreteTypeString, final TypeMirror componentTypeMirror, MaxRows maxRows, String cal, final String cleaner) throws IOException, ClassNotFoundException {
		maxRowInit(w, maxRows).write("\t\t\tfinal ");
		w.write(returnTypeString);
		w.write(" _colret = new ");
		w.write(concreteTypeString);
		w.write(returnTypeString.substring(returnTypeString.indexOf('<')));
		w.write("();\n\t\t\twhile(rs.next()) {\n");
		writeObject(w, keys, componentTypeMirror, cal);
		w.write("\t\t\t\t_colret.add(");
		clean(w, cleaner).write(");\n");
		maxRowBreak(w, maxRows).write("\t\t\t}\n");
	}

	public  void toCollection(final Writer w, final String[] keys, final TypeMirror collectionTypeMirror, final TypeMirror componentTypeMirror, MaxRows maxRows, String cal, final String cleaner) throws IOException, ClassNotFoundException {
		final String collectionType = getConcreteClassCanonicalName(collectionTypeMirror, ArrayList.class);
		writeCollection(w, keys, collectionTypeMirror.toString(), collectionType, componentTypeMirror, maxRows, cal, cleaner);
		w.write("\t\t\treturn _colret;\n");
	}

	public void toArray(final Writer w, final String[] keys, final TypeMirror componentTypeMirror, MaxRows maxRows, String cal, final String cleaner) throws IOException, ClassNotFoundException {
		final String returnTypeString = componentTypeMirror.toString();
		writeCollection(w, keys, "java.util.List<" + returnTypeString + ">", "java.util.ArrayList", componentTypeMirror, maxRows, cal, cleaner);
		w.write("\t\t\treturn _colret.toArray(new ");
		w.write(returnTypeString);
		w.write("[_colret.size()]);\n");
	}

	public void toListIterator(final Writer w, final String[] keys, final TypeMirror componentTypeMirror, MaxRows maxRows, String cal, final String cleaner) throws IOException, ClassNotFoundException {
		final String returnTypeString = componentTypeMirror.toString();
		writeCollection(w, keys, "java.util.List<" + returnTypeString + ">", "java.util.ArrayList", componentTypeMirror, maxRows, cal, cleaner);
		w.write("\t\t\treturn _colret.listIterator();\n");
	}

	public void toIterator(final Writer w, final String[] keys, final TypeMirror componentTypeMirror, MaxRows maxRows, String cal, final String cleaner) throws IOException, ClassNotFoundException {
		final String returnTypeString = componentTypeMirror.toString();
		writeCollection(w, keys, "java.util.List<" + returnTypeString + ">", "java.util.ArrayList", componentTypeMirror, maxRows, cal, cleaner);
		w.write("\t\t\treturn _colret.iterator();\n");
	}

	public void toMap(final Writer w, final String[] keys, final TypeMirror mapTypeMirror, final TypeMirror mapKeyTypeMirror, final TypeMirror componentTypeMirror, MaxRows maxRows, String cal, final String cleaner) throws IOException, ClassNotFoundException {
		final String mapType = getConcreteClassCanonicalName(mapTypeMirror, HashMap.class);
		final String returnTypeString = mapTypeMirror.toString();
		maxRowInit(w, maxRows).write("\t\t\tfinal ");
		w.write(returnTypeString);
		w.write(" _colret = new ");
		w.write(mapType);
		w.write(returnTypeString.substring(returnTypeString.indexOf('<')));
		w.write("();\n\t\t\twhile(rs.next()) {\n");

		//writeObject(w, keys, componentTypeMirror, componentType, cal);
		final CompileTimeRowToObjectMapper rm = getRowMapper(keys, componentTypeMirror, cal, null, mapKeyTypeMirror);
		rm.gen(w, componentTypeMirror.toString());
		w.write("\t\t\t\t_colret.put(");
		rm.extractColumnValueString(w, 1, mapKeyTypeMirror);
		w.write(", ");
		clean(w, cleaner).write(");\n");
		maxRowBreak(w, maxRows).write("\t\t\t}\n");
		w.write("\t\t\treturn _colret;\n");
	}

	public void toMapCollection(final Writer w, final String[] keys,
								final TypeMirror mapTypeMirror,
								final TypeMirror mapKeyTypeMirror,
								final TypeMirror collectionTypeMirror,
								final TypeMirror componentTypeMirror,
								MaxRows maxRows, String cal, final String cleaner) throws IOException, ClassNotFoundException {
		final String mapType = getConcreteClassCanonicalName(mapTypeMirror, HashMap.class);
		final String collectionType = getConcreteClassCanonicalName(collectionTypeMirror, ArrayList.class);
		final String returnTypeString = mapTypeMirror.toString();
		final String collectionTypeString = collectionTypeMirror.toString();

		maxRowInit(w, maxRows).write("\t\t\tfinal ");
		w.write(returnTypeString);
		w.write(" _colret = new ");
		w.write(mapType);
		w.write(returnTypeString.substring(returnTypeString.indexOf('<')));
		w.write("();\n\t\t\twhile(rs.next()) {\n");

		//writeObject(w, keys, componentTypeMirror, componentType, cal);
		final CompileTimeRowToObjectMapper rm = getRowMapper(keys, componentTypeMirror, cal, null, mapKeyTypeMirror);
		rm.gen(w, componentTypeMirror.toString());

		w.write("\t\t\t\tfinal ");
		w.write(mapKeyTypeMirror.toString());
		w.write(" _colkey = ");
		rm.extractColumnValueString(w, 1, mapKeyTypeMirror);
		w.write(";\n\t\t\t\t");

		w.write(collectionTypeString);
		w.write(" _collist = _colret.get(_colkey);\n\t\t\t\tif(_collist == null) {\n\t\t\t\t\t_collist = new ");
		w.write(collectionType);
		w.write(collectionTypeString.substring(collectionTypeString.indexOf('<')));
		w.write("();\n\t\t\t\t\t_colret.put(_colkey, _collist);\n\t\t\t\t}\n\t\t\t\t_collist.add(");
		clean(w, cleaner).write(");\n");
		maxRowBreak(w, maxRows).write("\t\t\t}\n\t\t\treturn _colret;\n");
	}

	private Writer maxRowInit(final Writer w, final MaxRows maxRows) throws IOException {
		if(maxRows != null)
			w.append("\t\t\t").append(maxRows.type).append(" _rowCount = 0;\n");
		return w;
	}

	private Writer maxRowBreak(final Writer w, final MaxRows maxRows) throws IOException {
		if(maxRows != null) {
			w.append("\t\t\t\tif(");
			if(maxRows.dynamic)
				w.append(maxRows.value).append(" > 0 && ");
			w.append("++_rowCount == ").append(maxRows.value).append(")\n\t\t\t\t\tbreak;\n");
		}
		return w;
	}

	private Writer clean(final Writer w, final String cleaner) throws IOException {
		if(cleaner == null)
			w.write("ret");
		else {
			w.append(cleaner).append(".clean(ret)");
		}
		return w;
	}
	
	static class MaxRows {
		final String value, type;
		final boolean dynamic;
		
		static MaxRows getMaxRows(final long value) {
			return value < 1 ? null : new MaxRows(value);
		}

		static MaxRows getMaxRows(final String value, final String type) {
			return new MaxRows(value, type);
		}

		private MaxRows(final long value) {
			String valueString = Long.toString(value);
			this.dynamic = false;
			if(value <= Byte.MAX_VALUE)
				this.type = "byte";
			else if(value <= Short.MAX_VALUE)
				this.type = "short";
			else if(value <= Integer.MAX_VALUE)
				this.type = "int";
			else {
				this.type = "long";
				valueString += "L"; // fun!
			}
			this.value = valueString;
		}

		private MaxRows(final String value, final String type) {
			this.value = value;
			this.type = type;
			this.dynamic = true;
		}
	}
}
