package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.ResultSetMapper;

import javax.annotation.processing.ProcessingEnvironment;
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
import java.util.*;

import static com.moparisthebest.jdbc.codegen.JdbcMapperProcessor.typeMirrorStringNoGenerics;
import static com.moparisthebest.jdbc.codegen.JdbcMapperProcessor.typeMirrorToClass;

/**
 * Created by mopar on 5/26/17.
 */
public class CompileTimeResultSetMapper {

	private final Types types;
	private final TypeMirror collectionType, mapType, mapCollectionType, iteratorType, listIteratorType;

	public CompileTimeResultSetMapper(final ProcessingEnvironment processingEnv) {
		types = processingEnv.getTypeUtils();
		final Elements elements = processingEnv.getElementUtils();

		collectionType = types.getDeclaredType(elements.getTypeElement(Collection.class.getCanonicalName()), types.getWildcardType(null, null));
		mapType = types.getDeclaredType(elements.getTypeElement(Map.class.getCanonicalName()), types.getWildcardType(null, null), types.getWildcardType(null, null));
		mapCollectionType = types.getDeclaredType(elements.getTypeElement(Map.class.getCanonicalName()), types.getWildcardType(null, null), types.getWildcardType(collectionType, null));

		iteratorType = types.getDeclaredType(elements.getTypeElement(Iterator.class.getCanonicalName()), types.getWildcardType(null, null));
		listIteratorType = types.getDeclaredType(elements.getTypeElement(ListIterator.class.getCanonicalName()), types.getWildcardType(null, null));
	}

	@SuppressWarnings({"unchecked"})
	public static <T, E> String getConcreteClassCanonicalName(final TypeMirror returnType, final Class<E> defaultConcreteClass) {
		final Set<Modifier> modifiers = ((DeclaredType) returnType).asElement().getModifiers();
		if (modifiers.contains(Modifier.ABSTRACT)) { // todo: no interface?
			try {
				final Class<? extends T> concrete = (Class<? extends T>) ResultSetMapper.interfaceToConcrete.get(typeMirrorToClass(returnType));
				return (concrete == null ? (Class<? extends T>) defaultConcreteClass : concrete).getCanonicalName();
			} catch (ClassNotFoundException e) {
				// ignore?
			}
		}
		return typeMirrorStringNoGenerics(returnType);
	}

	@SuppressWarnings({"unchecked"})
	public void mapToResultType(final Writer w, final String[] keys, final ExecutableElement eeMethod, final int arrayMaxLength, final Calendar cal) throws IOException, NoSuchMethodException, ClassNotFoundException {
		//final Method m = fromExecutableElement(eeMethod);
		//final Class returnType = m.getReturnType();
		final TypeMirror returnTypeMirror = eeMethod.getReturnType();
		//final Class returnType = typeMirrorToClass(returnTypeMirror);
		if (returnTypeMirror.getKind() == TypeKind.ARRAY) {
			final TypeMirror componentType = ((ArrayType) returnTypeMirror).getComponentType();
			toArray(w, keys, componentType, arrayMaxLength, cal);
		} else if (types.isAssignable(returnTypeMirror, collectionType)) {
			final List<? extends TypeMirror> typeArguments = ((DeclaredType) returnTypeMirror).getTypeArguments();
			toCollection(w, keys, returnTypeMirror, typeArguments.get(0), arrayMaxLength, cal);
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
						arrayMaxLength, cal);
				return;
			}
			toMap(w, keys, returnTypeMirror, typeArguments.get(0), typeArguments.get(1), arrayMaxLength, cal);
		} else if (types.isAssignable(returnTypeMirror, iteratorType)) {
			final List<? extends TypeMirror> typeArguments = ((DeclaredType) returnTypeMirror).getTypeArguments();
			if (types.isAssignable(returnTypeMirror, listIteratorType))
				toListIterator(w, keys, typeArguments.get(0), arrayMaxLength, cal);
			else
				toIterator(w, keys, typeArguments.get(0), arrayMaxLength, cal);
		} else {
			toObject(w, keys, returnTypeMirror, cal);
		}
	}

	@SuppressWarnings("unchecked")
	public CompileTimeRowToObjectMapper getRowMapper(final String[] keys, TypeMirror returnTypeClass, Calendar cal, TypeMirror mapValType, TypeMirror mapKeyType) {
		return new CompileTimeRowToObjectMapper(keys, returnTypeClass, cal, mapValType, mapKeyType);
	}

	public void writeObject(final Writer w, final String[] keys, final TypeMirror returnTypeMirror, final Calendar cal) throws IOException, ClassNotFoundException {
		getRowMapper(keys, returnTypeMirror, cal, null, null).gen(w, returnTypeMirror.toString());
	}

	public void toObject(final Writer w, final String[] keys, final TypeMirror returnTypeMirror, final Calendar cal) throws IOException, ClassNotFoundException {
		w.write("\t\t\tif(rs.next()) {\n");
		writeObject(w, keys, returnTypeMirror, cal);
		w.write("\t\t\t\treturn ret;\n\t\t\t} else {\n\t\t\t\treturn null;\n\t\t\t}\n");
	}

	@SuppressWarnings("unchecked")
	public <T extends Collection<E>, E> void writeCollection(final Writer w, final String[] keys, final String returnTypeString, final String concreteTypeString, final TypeMirror componentTypeMirror, int arrayMaxLength, Calendar cal) throws IOException, ClassNotFoundException {
		w.write("\t\t\tfinal ");
		w.write(returnTypeString);
		w.write(" _colret = new ");
		w.write(concreteTypeString);
		w.write(returnTypeString.substring(returnTypeString.indexOf('<')));
		w.write("();\n\t\t\twhile(rs.next()) {\n");
		writeObject(w, keys, componentTypeMirror, cal);
		w.write("\t\t\t\t_colret.add(ret);\n\t\t\t}\n");
	}

	public <T extends Collection<E>, E> void toCollection(final Writer w, final String[] keys, final TypeMirror collectionTypeMirror, final TypeMirror componentTypeMirror, int arrayMaxLength, Calendar cal) throws IOException, ClassNotFoundException {
		final String collectionType = getConcreteClassCanonicalName(collectionTypeMirror, ArrayList.class);
		writeCollection(w, keys, collectionTypeMirror.toString(), collectionType, componentTypeMirror, arrayMaxLength, cal);
		w.write("\t\t\treturn _colret;\n");
	}

	public <E> void toArray(final Writer w, final String[] keys, final TypeMirror componentTypeMirror, int arrayMaxLength, Calendar cal) throws IOException, ClassNotFoundException {
		final String returnTypeString = componentTypeMirror.toString();
		writeCollection(w, keys, "java.util.List<" + returnTypeString + ">", "java.util.ArrayList", componentTypeMirror, arrayMaxLength, cal);
		w.write("\t\t\treturn _colret.toArray(new ");
		w.write(returnTypeString);
		w.write("[_colret.size()]);\n");
	}

	public <E> void toListIterator(final Writer w, final String[] keys, final TypeMirror componentTypeMirror, int arrayMaxLength, Calendar cal) throws IOException, ClassNotFoundException {
		final String returnTypeString = componentTypeMirror.toString();
		writeCollection(w, keys, "java.util.List<" + returnTypeString + ">", "java.util.ArrayList", componentTypeMirror, arrayMaxLength, cal);
		w.write("\t\t\treturn _colret.listIterator();\n");
	}

	public <E> void toIterator(final Writer w, final String[] keys, final TypeMirror componentTypeMirror, int arrayMaxLength, Calendar cal) throws IOException, ClassNotFoundException {
		final String returnTypeString = componentTypeMirror.toString();
		writeCollection(w, keys, "java.util.List<" + returnTypeString + ">", "java.util.ArrayList", componentTypeMirror, arrayMaxLength, cal);
		w.write("\t\t\treturn _colret.iterator();\n");
	}

	@SuppressWarnings("unchecked")
	public <T extends Map<K, E>, K, E> void toMap(final Writer w, final String[] keys, final TypeMirror mapTypeMirror, final TypeMirror mapKeyTypeMirror, final TypeMirror componentTypeMirror, int arrayMaxLength, Calendar cal) throws IOException, ClassNotFoundException {
		final String mapType = getConcreteClassCanonicalName(mapTypeMirror, HashMap.class);
		final String returnTypeString = mapTypeMirror.toString();
		w.write("\t\t\tfinal ");
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
		w.write(", ret);\n\t\t\t}\n");
		w.write("\t\t\treturn _colret;\n");
	}

	@SuppressWarnings("unchecked")
	public <T extends Map<K, E>, K, E extends Collection<C>, C> void toMapCollection(final Writer w, final String[] keys,
																					 final TypeMirror mapTypeMirror,
																					 final TypeMirror mapKeyTypeMirror,
																					 final TypeMirror collectionTypeMirror,
																					 final TypeMirror componentTypeMirror,
																					 int arrayMaxLength, Calendar cal) throws IOException, ClassNotFoundException {
		final String mapType = getConcreteClassCanonicalName(mapTypeMirror, HashMap.class);
		final String collectionType = getConcreteClassCanonicalName(collectionTypeMirror, ArrayList.class);
		final String returnTypeString = mapTypeMirror.toString();
		final String collectionTypeString = collectionTypeMirror.toString();
		w.write("\t\t\tfinal ");
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
		w.write("();\n\t\t\t\t\t_colret.put(_colkey, _collist);\n\t\t\t\t}\n\t\t\t\t_collist.add(ret);\n\t\t\t}\n\t\t\treturn _colret;\n");
	}
}
