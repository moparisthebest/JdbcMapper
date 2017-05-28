package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.CompilingRowToObjectMapper;
import com.moparisthebest.jdbc.ResultSetMapper;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static com.moparisthebest.jdbc.codegen.JdbcMapperProcessor.typeMirrorToClass;

/**
 * Created by mopar on 5/26/17.
 */
public class CompileTimeResultSetMapper {

	@SuppressWarnings({"unchecked"})
	public void mapToResultType(final Writer w, final String[] keys, final ExecutableElement eeMethod, final int arrayMaxLength, final Calendar cal) throws IOException, NoSuchMethodException, ClassNotFoundException {
		final Method m = fromExecutableElement(eeMethod);
		final Class returnType = m.getReturnType();
		final TypeMirror returnTypeMirror = eeMethod.getReturnType();
		if (returnType.isArray()) {
			toArray(w, keys, ((ArrayType) returnTypeMirror).getComponentType(), returnType.getComponentType(), arrayMaxLength, cal);
		} else if (Collection.class.isAssignableFrom(returnType)) {
			toCollection(w, keys, returnTypeMirror, returnType, ((DeclaredType) returnTypeMirror).getTypeArguments().get(0), (Class) getActualTypeArguments(m)[0], arrayMaxLength, cal);
		} else if (Map.class.isAssignableFrom(returnType)) {
			final List<? extends TypeMirror> typeArguments = ((DeclaredType) returnTypeMirror).getTypeArguments();
			final Type[] types = getActualTypeArguments(m);
			if (types[1] instanceof ParameterizedType) { // for collectionMaps
				final ParameterizedType pt = (ParameterizedType) types[1];
				final Class collectionType = (Class) pt.getRawType();
				if (Collection.class.isAssignableFrom(collectionType)) {
					final TypeMirror collectionTypeMirror = typeArguments.get(1);
					toMapCollection(w, keys,
							returnTypeMirror, returnType,
							typeArguments.get(0), (Class) types[0],
							collectionTypeMirror, collectionType,
							((DeclaredType) collectionTypeMirror).getTypeArguments().get(0), (Class) pt.getActualTypeArguments()[0],
							arrayMaxLength, cal);
					return;
				}
			}
			toMap(w, keys, returnTypeMirror, returnType, typeArguments.get(0), (Class) types[0], typeArguments.get(1), (Class) types[1], arrayMaxLength, cal);
		} else if (Iterator.class.isAssignableFrom(returnType)) {
			if (ListIterator.class.isAssignableFrom(returnType))
				toListIterator(w, keys, ((DeclaredType) returnTypeMirror).getTypeArguments().get(0), (Class) getActualTypeArguments(m)[0], arrayMaxLength, cal);
			else
				toIterator(w, keys, ((DeclaredType) returnTypeMirror).getTypeArguments().get(0), (Class) getActualTypeArguments(m)[0], arrayMaxLength, cal);
		} else {
			toObject(w, keys, returnTypeMirror, returnType, cal);
		}
	}

	public <K, T> CompilingRowToObjectMapper<K, T> getRowMapper(final String[] keys, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		return new CompilingRowToObjectMapper<K, T>(keys, returnTypeClass, cal, mapValType, mapKeyType);
	}

	public void writeObject(final Writer w, final String[] keys, final TypeMirror returnTypeMirror, final Class<?> returnType, final Calendar cal) throws IOException {
		getRowMapper(keys, returnType, cal, null, null).gen(w, returnTypeMirror.toString());
	}

	public void toObject(final Writer w, final String[] keys, final TypeMirror returnTypeMirror, final Class<?> returnType, final Calendar cal) throws IOException {
		w.write("\t\t\tif(rs.next()) {\n");
		writeObject(w, keys, returnTypeMirror, returnType, cal);
		w.write("\t\t\t\treturn ret;\n\t\t\t} else {\n\t\t\t\treturn null;\n\t\t\t}\n");
	}

	@SuppressWarnings("unchecked")
	public <T extends Collection<E>, E> void writeCollection(final Writer w, final String[] keys, final String returnTypeString, Class<T> collectionType, final TypeMirror componentTypeMirror, Class<E> componentType, int arrayMaxLength, Calendar cal) throws IOException {
		collectionType = (Class<T>) ResultSetMapper.getConcreteClass(collectionType, ArrayList.class);
		w.write("\t\t\tfinal ");
		w.write(returnTypeString);
		w.write(" _colret = new ");
		w.write(collectionType.getCanonicalName());
		w.write(returnTypeString.substring(returnTypeString.indexOf('<')));
		w.write("();\n\t\t\twhile(rs.next()) {\n");
		writeObject(w, keys, componentTypeMirror, componentType, cal);
		w.write("\t\t\t\t_colret.add(ret);\n\t\t\t}\n");
	}

	public <T extends Collection<E>, E> void toCollection(final Writer w, final String[] keys, final TypeMirror collectionTypeMirror, Class<T> collectionType, final TypeMirror componentTypeMirror, Class<E> componentType, int arrayMaxLength, Calendar cal) throws IOException {
		writeCollection(w, keys, collectionTypeMirror.toString(), collectionType, componentTypeMirror, componentType, arrayMaxLength, cal);
		w.write("\t\t\treturn _colret;\n");
	}

	public <E> void toArray(final Writer w, final String[] keys, final TypeMirror componentTypeMirror, Class<E> componentType, int arrayMaxLength, Calendar cal) throws IOException {
		final String returnTypeString = componentTypeMirror.toString();
		writeCollection(w, keys, "java.util.List<" + returnTypeString + ">", ArrayList.class, componentTypeMirror, componentType, arrayMaxLength, cal);
		w.write("\t\t\treturn _colret.toArray(new ");
		w.write(returnTypeString);
		w.write("[_colret.size()]);\n");
	}

	public <E> void toListIterator(final Writer w, final String[] keys, final TypeMirror componentTypeMirror, Class<E> componentType, int arrayMaxLength, Calendar cal) throws IOException {
		final String returnTypeString = componentTypeMirror.toString();
		writeCollection(w, keys, "java.util.List<" + returnTypeString + ">", ArrayList.class, componentTypeMirror, componentType, arrayMaxLength, cal);
		w.write("\t\t\treturn _colret.listIterator();\n");
	}

	public <E> void toIterator(final Writer w, final String[] keys, final TypeMirror componentTypeMirror, Class<E> componentType, int arrayMaxLength, Calendar cal) throws IOException {
		final String returnTypeString = componentTypeMirror.toString();
		writeCollection(w, keys, "java.util.List<" + returnTypeString + ">", ArrayList.class, componentTypeMirror, componentType, arrayMaxLength, cal);
		w.write("\t\t\treturn _colret.iterator();\n");
	}

	@SuppressWarnings("unchecked")
	public <T extends Map<K, E>, K, E> void toMap(final Writer w, final String[] keys, final TypeMirror mapTypeMirror, Class<T> mapType, final TypeMirror mapKeyTypeMirror, Class<E> mapKeyType, final TypeMirror componentTypeMirror, Class<E> componentType, int arrayMaxLength, Calendar cal) throws IOException {
		mapType = (Class<T>) ResultSetMapper.getConcreteClass(mapType, HashMap.class);
		final String returnTypeString = mapTypeMirror.toString();
		w.write("\t\t\tfinal ");
		w.write(returnTypeString);
		w.write(" _colret = new ");
		w.write(mapType.getCanonicalName());
		w.write(returnTypeString.substring(returnTypeString.indexOf('<')));
		w.write("();\n\t\t\twhile(rs.next()) {\n");

		//writeObject(w, keys, componentTypeMirror, componentType, cal);
		final CompilingRowToObjectMapper rm = getRowMapper(keys, componentType, cal, null, mapKeyType);
		rm.gen(w, componentTypeMirror.toString());
		w.write("\t\t\t\t_colret.put(");
		rm.extractColumnValueString(w, 1, mapKeyType);
		w.write(", ret);\n\t\t\t}\n");
		w.write("\t\t\treturn _colret;\n");
	}

	@SuppressWarnings("unchecked")
	public <T extends Map<K, E>, K, E extends Collection<C>, C> void toMapCollection(final Writer w, final String[] keys,
																					 final TypeMirror mapTypeMirror, Class<T> mapType,
																					 final TypeMirror mapKeyTypeMirror, Class<E> mapKeyType,
																					 final TypeMirror collectionTypeMirror, Class<E> collectionType,
																					 final TypeMirror componentTypeMirror, Class<E> componentType,
																					 int arrayMaxLength, Calendar cal) throws IOException {
		mapType = (Class<T>) ResultSetMapper.getConcreteClass(mapType, HashMap.class);
		collectionType = (Class<E>) ResultSetMapper.getConcreteClass(collectionType, ArrayList.class);
		final String returnTypeString = mapTypeMirror.toString();
		final String collectionTypeString = collectionTypeMirror.toString();
		w.write("\t\t\tfinal ");
		w.write(returnTypeString);
		w.write(" _colret = new ");
		w.write(mapType.getCanonicalName());
		w.write(returnTypeString.substring(returnTypeString.indexOf('<')));
		w.write("();\n\t\t\twhile(rs.next()) {\n");

		//writeObject(w, keys, componentTypeMirror, componentType, cal);
		final CompilingRowToObjectMapper rm = getRowMapper(keys, componentType, cal, null, mapKeyType);
		rm.gen(w, componentTypeMirror.toString());

		w.write("\t\t\t\tfinal ");
		w.write(mapKeyTypeMirror.toString());
		w.write(" _colkey = ");
		rm.extractColumnValueString(w, 1, mapKeyType);
		w.write(";\n\t\t\t\t");

		w.write(collectionTypeString);
		w.write(" _collist = _colret.get(_colkey);\n\t\t\t\tif(_collist == null) {\n\t\t\t\t\t_collist = new ");
		w.write(collectionType.getCanonicalName());
		w.write(collectionTypeString.substring(collectionTypeString.indexOf('<')));
		w.write("();\n\t\t\t\t\t_colret.put(_colkey, _collist);\n\t\t\t\t}\n\t\t\t\t_collist.add(ret);\n\t\t\t}\n\t\t\treturn _colret;\n");
	}

	private static Type[] getActualTypeArguments(Method m) {
		return ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments();
	}

	@SuppressWarnings("unchecked")
	private Method fromExecutableElement(final ExecutableElement eeMethod) throws ClassNotFoundException, NoSuchMethodException {
		final Class c = Class.forName(eeMethod.getEnclosingElement().asType().toString());
		final List<? extends VariableElement> params = eeMethod.getParameters();
		final Class<?>[] parameterTypes = new Class[params.size()];
		int count = -1;
		for (final VariableElement param : params) {
			parameterTypes[++count] = typeMirrorToClass(param.asType());
		}
		return c.getDeclaredMethod(eeMethod.getSimpleName().toString(), parameterTypes);
	}
}
