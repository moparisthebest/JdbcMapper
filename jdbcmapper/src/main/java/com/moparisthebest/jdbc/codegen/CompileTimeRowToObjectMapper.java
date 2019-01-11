package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.CompilingRowToObjectMapper;
import com.moparisthebest.jdbc.MapperException;
import com.moparisthebest.jdbc.RowToObjectMapper;
import com.moparisthebest.jdbc.TypeMappingsFactory;

import javax.lang.model.element.*;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static com.moparisthebest.jdbc.CompilingRowToObjectMapper.escapeMapKeyString;
import static com.moparisthebest.jdbc.codegen.CompileTimeResultSetMapper.getConcreteClassCanonicalName;
import static com.moparisthebest.jdbc.codegen.JdbcMapperProcessor.typeMirrorToClass;

/**
 * Created by mopar on 6/7/17.
 */
public class CompileTimeRowToObjectMapper {

	protected static final TypeMappingsFactory _tmf = TypeMappingsFactory.getInstance();

	protected final CompileTimeResultSetMapper rsm;
	protected final String[] keys;

	/**
	 * Calendar instance for date/time mappings.
	 */
	protected final String _calendarName, _resultSetName;

	/**
	 * Class to map ResultSet Rows to.
	 */
	protected final TypeMirror _returnTypeClass;

	protected final TypeMirror _mapKeyType;

	protected final int _columnCount;

	protected final boolean mapOnlySecondColumn;

	// only non-null when _returnTypeClass is an array, or a map
	protected final TypeMirror componentType;
	protected final boolean returnMap, resultSetConstructor;

	protected Element[] _fields = null;
	protected int[] _fieldTypes, _fieldOrder;
	protected String[] _fieldClasses;

	protected final ReflectionFields reflectionFields;

	public CompileTimeRowToObjectMapper(final CompileTimeResultSetMapper rsm, final String[] keys, final TypeMirror returnTypeClass, final String resultSetName, final String calendarName, final TypeMirror mapValType, final TypeMirror mapKeyType, final ReflectionFields reflectionFields) {
		this.rsm = rsm;
		this.keys = keys;
		this.reflectionFields = reflectionFields;

		_calendarName = calendarName;
		_resultSetName = resultSetName;
		_mapKeyType = mapKeyType;

		_columnCount = keys.length - 1;

		mapOnlySecondColumn = _mapKeyType != null && _columnCount == 2;

		returnMap = rsm.types.isAssignable(returnTypeClass, rsm.mapType);
		if (returnMap) {
			_returnTypeClass = returnTypeClass;
			final List<? extends TypeMirror> typeArguments = ((DeclaredType) returnTypeClass).getTypeArguments();
			componentType = typeArguments.size() > 1 ? typeArguments.get(1) : mapValType;
			resultSetConstructor = false;
		} else {
			_returnTypeClass = returnTypeClass;
			// detect if we want an array back
			componentType = returnTypeClass.getKind() == TypeKind.ARRAY && !rsm.types.isSameType(returnTypeClass, rsm.byteArrayType) ? ((ArrayType) returnTypeClass).getComponentType() : null;

			// detect if returnTypeClass has a constructor that takes a ResultSet, if so, our job couldn't be easier...
			boolean resultSetConstructor = false, defaultConstructor = false, paramConstructor = false;
			if(_returnTypeClass.getKind() == TypeKind.DECLARED) {
				Map<String, Integer> strippedKeys = null;
				final List<? extends Element> methodsAndConstructors = ((TypeElement)((DeclaredType)_returnTypeClass).asElement()).getEnclosedElements();

				/*
				// uncomment this to show difference between java 1.8 with -parameters and not, prints this without -parameters (javac 1.6 gets this correct also):
				// methodsAndConstructors: FieldPerson(): '', FieldPerson(long,java.util.Date,java.lang.String,java.lang.String): 'long personNo, java.util.Date birthDate, java.lang.String firstName, java.lang.String lastName', FieldPerson(com.moparisthebest.jdbc.dto.Person): 'com.moparisthebest.jdbc.dto.Person person'
				// but javac 1.8 prints this with -parameters (wrongly):
				// methodsAndConstructors: FieldPerson(): '', FieldPerson(long,java.util.Date,java.lang.String,java.lang.String): 'long personNo, java.util.Date firstName, java.lang.String lastName, java.lang.String arg3', FieldPerson(com.moparisthebest.jdbc.dto.Person): 'com.moparisthebest.jdbc.dto.Person person'
				if(_returnTypeClass.toString().equals("com.moparisthebest.jdbc.dto.FieldPerson"))
					throw new MapperException("methodsAndConstructors: " + methodsAndConstructors.stream().filter(e -> e.getKind() == ElementKind.CONSTRUCTOR && e.getModifiers().contains(Modifier.PUBLIC)).map(e -> e.toString() +
							": '" + ((ExecutableElement)e).getParameters().stream().map(param ->  param.asType() + " " + param.getSimpleName().toString()).collect(java.util.stream.Collectors.joining(", ")) + "'"
					).collect(java.util.stream.Collectors.joining(", ")));
				*/
				outer:
				for(final Element e : methodsAndConstructors) {
					if(e.getKind() == ElementKind.CONSTRUCTOR && e.getModifiers().contains(Modifier.PUBLIC)) { // todo: public normally, but also package-private if same package, or protected if it's a sub-class of super...
						final List<? extends VariableElement> params = ((ExecutableElement)e).getParameters();
						if(params.isEmpty())
							defaultConstructor = true;
						else if(params.size() == 1 && rsm.types.isSameType(params.get(0).asType(), rsm.resultSetType))
							resultSetConstructor = true;
						else if(params.size() == _columnCount) {
							// maybe we want to call the constructor, if the names line up
							if(strippedKeys == null) {
								strippedKeys = new HashMap<String, Integer>(keys.length * 2);
								for (int x = 1; x <= _columnCount; ++x) {
									final String key = keys[x];
									strippedKeys.put(key, x);
									strippedKeys.put(key.replaceAll("_", ""), x);
								}
								_fieldOrder = new int[keys.length];
								_fieldTypes = new int[keys.length];
								_fieldClasses = new String[keys.length];
							}
							int count = 0;
							for(final VariableElement param : params) {
								final Integer index = strippedKeys.get(param.getSimpleName().toString().toUpperCase());
								if(index == null)
									continue outer;
								_fieldOrder[++count] = index;
								_fieldTypes[count] = getTypeId(param.asType());
								if(_fieldTypes[count] == TypeMappingsFactory.TYPE_ENUM) {
									_fieldClasses[count] = param.asType().toString();
								}
							}
							paramConstructor = true;
						}
					}
				}
			}
			if(!paramConstructor)
				_fieldOrder = null; // didn't successfully finish
			this.resultSetConstructor = resultSetConstructor;
			if(!resultSetConstructor && !defaultConstructor && !paramConstructor && _columnCount > 2 && componentType == null)
				throw new MapperException("Exception when trying to get constructor for : "+_returnTypeClass.toString() + " Must have default no-arg constructor, one that takes a single ResultSet, or one that takes parameters named like the query columns (in any order):\n" +
						RowToObjectMapper.keysToString(keys)+ "\nRequires compilation with -parameters argument if source isn't being compiled this pass, beware Bug ID: JDK-8191074 with jdk8, fixed in 9+");
		}
	}

	public static List<DeclaredType> getAllImplementedTypes(final DeclaredType type, final List<DeclaredType> ret) {
		ret.add(type);
		for(final TypeMirror tm : JdbcMapperProcessor.getTypes().directSupertypes(type))
			if(tm.getKind() == TypeKind.DECLARED && !tm.toString().equals("java.lang.Object"))
				getAllImplementedTypes((DeclaredType) tm, ret);
		return ret;
	}

	/**
	 * Build the structures necessary to do the mapping
	 *
	 * @throws SQLException on error.
	 */
	protected void getFieldMappings() {

		if(_returnTypeClass.getKind() != TypeKind.DECLARED)
			throw new MapperException("_returnTypeClass " + _returnTypeClass + " not TypeKind.DECLARED ?? how??");

		final DeclaredType declaredReturnType = (DeclaredType)_returnTypeClass;

		// added this to handle stripping '_' from keys
		Map<String, String> strippedKeys = new HashMap<String, String>();
		for (final String key : keys) {
			String strippedKey = key;
			if (key != null) {
				strippedKey = key.replaceAll("_", "");
				if (key.equals(strippedKey))
					continue;
				strippedKeys.put(strippedKey, key);
			}
		}
		//System.out.println("strippedKeys: "+strippedKeys);
		//
		// find fields or setters for return class
		//
		HashMap<String, Element> mapFields = new HashMap<String, Element>(_columnCount * 2);
		for (int i = 1; i <= _columnCount; i++) {
			mapFields.put(keys[i], null);
		}

		final List<DeclaredType> allTypes = getAllImplementedTypes(declaredReturnType, new ArrayList<DeclaredType>());

		// public methods
		// have to loop to get super methods too
		for (final DeclaredType clazz : allTypes) {
			for (Element e : ((TypeElement) clazz.asElement()).getEnclosedElements()) {
				if (e.getKind() != ElementKind.METHOD)
					continue;
				final ExecutableElement m = (ExecutableElement) e;
				//System.out.printf("method: '%s', isSetterMethod: '%s'\n", m, isSetterMethod(m));
				if (isSetterMethod(m, declaredReturnType)) { // todo: in RowToObjectMapper we send in this top one, but how does it or this handle methods on parent classes?
					String fieldName = m.getSimpleName().toString().substring(3).toUpperCase();
					//System.out.println("METHOD-fieldName1: "+fieldName);
					if (!mapFields.containsKey(fieldName)) {
						fieldName = strippedKeys.get(fieldName);
						if (fieldName == null)
							continue;
						//System.out.println("METHOD-fieldName2: "+fieldName);
					}
					final Element field = mapFields.get(fieldName);
					// check for overloads
					if (field == null) {
						mapFields.put(fieldName, m);
					} else {
						// todo: does this work?
						// fix for 'overloaded' methods when it comes to stripped keys, we want the exact match
						final String thisName = m.getSimpleName().toString().substring(3).toUpperCase();
						final String previousName = field.getSimpleName().toString().substring(3).toUpperCase();
						//System.out.printf("thisName: '%s', previousName: '%s', mapFields.containsKey(thisName): %b, strippedKeys.containsKey(previousName): %b\n", thisName, previousName, mapFields.containsKey(thisName), strippedKeys.containsKey(previousName));
						if (mapFields.containsKey(thisName) && strippedKeys.containsKey(previousName)) {
							mapFields.put(fieldName, m);
						} else if (!mapFields.containsKey(previousName) || !strippedKeys.containsKey(thisName)) {
							throw new MapperException("Unable to choose between overloaded methods '" + m.getSimpleName().toString()
									+ "' and '" + field.getSimpleName().toString() + "' for field '" + fieldName + "' on the '" + _returnTypeClass.toString() + "' class. Mapping is done using "
									+ "a case insensitive comparison of SQL ResultSet columns to field "
									+ "names and public setter methods on the return class. Columns are also "
									+ "stripped of '_' and compared if no match is found with them.");
						}
						// then the 'overloaded' method is already correct
					}
				}
			}
		}

		// fix for 8813: include inherited and non-public fields
		for (final DeclaredType clazz : allTypes) {
			//System.out.println("fields in class: "+Arrays.toString(classFields));
			for (Element e : ((TypeElement)clazz.asElement()).getEnclosedElements()) {
				if(e.getKind() != ElementKind.FIELD)
					continue;
				final VariableElement f = (VariableElement)e;
				//System.out.println("f.fieldName: "+f.getSimpleName());
				if (f.getModifiers().contains(Modifier.STATIC)) continue;
				//if (reflectionFields == null && !f.getModifiers().contains(Modifier.PUBLIC)) continue;
				String fieldName = f.getSimpleName().toString().toUpperCase();
				//System.out.println("fieldName: "+fieldName);
				if (!mapFields.containsKey(fieldName)) {
					fieldName = strippedKeys.get(fieldName);
					if (fieldName == null)
						continue;
				}
				final Element field = mapFields.get(fieldName);
				if (field == null) {
					mapFields.put(fieldName, f);
				} else if(field.getKind() == ElementKind.FIELD) {
					// fix for 'overloaded' fields when it comes to stripped keys, we want the exact match
					final String thisName = f.getSimpleName().toString().toUpperCase();
					final String previousName = field.getSimpleName().toString().toUpperCase();
					//System.out.printf("thisName: '%s', previousName: '%s', mapFields.containsKey(thisName): %b, strippedKeys.containsKey(previousName): %b\n", thisName, previousName, mapFields.containsKey(thisName), strippedKeys.containsKey(previousName));
					if(mapFields.containsKey(thisName) && strippedKeys.containsKey(previousName)) {
						mapFields.put(fieldName, f);
					} else if (!mapFields.containsKey(previousName) || !strippedKeys.containsKey(thisName)) {
						throw new MapperException("Unable to choose between overloaded fields '" + f.getSimpleName().toString()
								+ "' and '" + field.getSimpleName().toString() + "' for field '" + fieldName + "' on the '" + _returnTypeClass.toString() + "' class. Mapping is done using "
								+ "a case insensitive comparison of SQL ResultSet columns to field "
								+ "names and public setter methods on the return class. Columns are also "
								+ "stripped of '_' and compared if no match is found with them.");
					}
					// then the 'overloaded' field is already correct
				}
			}
		}

		// finally actually init the fields array
		_fields = new Element[_columnCount + 1];
		_fieldTypes = new int[_columnCount + 1];

		for (int i = 1; i < _fields.length; i++) {
			Element f = mapFields.get(keys[i]);
			if (f == null) {
				throw new MapperException("Unable to map the SQL column '" + keys[i]
						+ "' to a field on the '" + _returnTypeClass.toString() +
						"' class. Mapping is done using a case insensitive comparison of SQL ResultSet "
						+ "columns to field "
						+ "names and public setter methods on the return class. Columns are also "
						+ "stripped of '_' and compared if no match is found with them.");
			}
			_fields[i] = f;
			if (f.getKind() == ElementKind.FIELD) {
				_fieldTypes[i] = getTypeId(((VariableElement) f).asType());
			} else {
				_fieldTypes[i] = getTypeId(((ExecutableElement) f).getParameters().get(0).asType());
			}
		}
	}

	/**
	 * Determine if the given method is a java bean setter method.
	 * @param method Method to check
	 * @return True if the method is a setter method.
	 */
	protected boolean isSetterMethod(final ExecutableElement method, final TypeMirror enclosingClass) {
		if (method.getSimpleName().toString().startsWith("set")) {

			final Set<Modifier> modifiers = method.getModifiers();
			if (modifiers.contains(Modifier.STATIC)) return false;
			if (!modifiers.contains(Modifier.PUBLIC)) return false;
			final TypeMirror methodReturnType = method.getReturnType();
			if (TypeKind.VOID != methodReturnType.getKind() && !rsm.types.isSameType(enclosingClass, methodReturnType)) return false;

			// method parameter checks
			final List<? extends VariableElement> params = method.getParameters();
			if (params.size() != 1) return false;
			if (TypeMappingsFactory.TYPE_UNKNOWN == getTypeId(params.get(0).asType())) return false;

			return true;
		}
		return false;
	}

	public void gen(final Appendable java, final String tType) throws IOException, ClassNotFoundException {

		if(mapOnlySecondColumn){
			java.append("final ").append(tType).append(" ret = ");
			extractColumnValueString(java, 2, _returnTypeClass);
			java.append(";\n");
			return;
		}

		if (resultSetConstructor) {
			java.append("final ").append(tType).append(" ret = new ").append(tType).append("(").append(_resultSetName).append(");\n");;
			finishIfNeeded(java);
			return;
		}

		if(_fieldOrder != null) {
			java.append("final ").append(tType).append(" ret = new ").append(tType).append("(\n");
			for(int x = 1; x <= _columnCount; ++x) {
				extractColumnValueString(java, _fieldOrder[x], _fieldTypes[x], _fieldClasses[x]);
				if(x != _columnCount)
					java.append(",\n");
			}
			java.append(");\n");
			finishIfNeeded(java);
			return;
		}

		if (returnMap) // we want a map
			try {
				java.append("final ").append(tType).append(" ret = new ").append(getConcreteClassCanonicalName(_returnTypeClass, HashMap.class)).append(tType.substring(tType.indexOf('<'))).append("();\n");
				final int columnLength = _columnCount + 1;
				int typeId = getTypeId(componentType);
				if (componentType != null && typeId != TypeMappingsFactory.TYPE_UNKNOWN) { // we want a specific value type
					final String enumName = componentType.toString();
					for (int x = 1; x < columnLength; ++x) {
						java.append("ret.put(").append(escapeMapKeyString(keys[x]).toLowerCase()).append(", ");
						extractColumnValueString(java, x, typeId, enumName);
						java.append(");\n");
					}
				} else // we want a generic object type
					for (int x = 1; x < columnLength; ++x)
						java.append("ret.put(").append(escapeMapKeyString(keys[x].toLowerCase())).append(", ").append(_resultSetName).append(".getObject(").append(String.valueOf(x)).append("));\n");
				return;
			} catch (Throwable e) {
				throw new MapperException(e.getClass().getName() + " when trying to create a Map<String, "
						+ (componentType == null ? "java.lang.Object" : componentType.toString()) + "> from a ResultSet row" +
						", all columns must be of the map value type", e);
			}
		else if (componentType != null) // we want an array
			try {
				java.append("final ").append(tType).append(" ret = new ").append(tType.substring(0, tType.length() - 1)).append(String.valueOf(_columnCount)).append("];\n");
				final int typeId = getTypeId(componentType);
				final String enumName = componentType.toString();
				for (int x = 0; x < _columnCount; ) {
					java.append("ret[").append(String.valueOf(x)).append("] = ");
					extractColumnValueString(java, ++x, typeId, enumName);
					java.append(";\n");
				}
				return;
			} catch (Throwable e) {
				throw new MapperException(e.getClass().getName() + " when trying to create a "
						+ componentType + "[] from a ResultSet row, all columns must be of that type", e);
			}

		// if the ResultSet only contains a single column we may be able to map directly
		// to the return type -- if so we don't need to build any structures to support
		// mapping
		if (_columnCount == 1) {


			try {
				final int typeId = getTypeId(_returnTypeClass);
				if (typeId != TypeMappingsFactory.TYPE_UNKNOWN) {
					java.append("final ").append(tType).append(" ret = ");
					extractColumnValueString(java, 1, typeId, _returnTypeClass.toString());
					java.append(";\n");
					finishIfNeeded(java);
					return;
				} else {
					// we still might want a single value (i.e. java.util.Date)
					/*
					Object val = extractColumnValue(1, typeId);
					if (_returnTypeClass.isAssignableFrom(val.getClass())) {
						return _returnTypeClass.cast(val);
					}
					*/
					// todo: we could actually pull from first row like above and test it first, but for now we will fall-through to field mappings...
				}
			} catch (Exception e) {
				throw new MapperException(e.getMessage(), e);
			}
		}

		if (_fields == null)
			getFieldMappings();

		java.append("final ").append(tType).append(" ret = new ").append(tType).append("();\n");

		for (int i = 1; i < _fields.length; i++) {
			final Element f = _fields[i];
			final boolean isField = f.getKind() == ElementKind.FIELD;

			String enumName = null;
			if (_fieldTypes[i] == TypeMappingsFactory.TYPE_ENUM) {
				if (f.getKind() == ElementKind.FIELD) {
					enumName = ((VariableElement) f).asType().toString();
				} else {
					enumName = ((ExecutableElement) f).getParameters().get(0).asType().toString();
				}
			}
			if (isField) {
				// if f not accessible (but super.getFieldMappings() sets it), throw exception during compilation is fine
				final Set<Modifier> mods = reflectionFields == null ? null : f.getModifiers();
				if(mods != null && (mods.contains(Modifier.PRIVATE) || mods.contains(Modifier.PROTECTED) || mods.contains(Modifier.FINAL))) {
					java.append("com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[").append(String.valueOf((reflectionFields.addGetIndex((VariableElement)f)))).append("], ret, ");
					extractColumnValueString(java, i, _fieldTypes[i], enumName);
					java.append(");\n");
				} else {
					java.append("ret.").append(f.getSimpleName().toString()).append(" = ");
					extractColumnValueString(java, i, _fieldTypes[i], enumName);
					java.append(";\n");
				}
			} else {
				java.append("ret.").append(f.getSimpleName().toString()).append("(");
				extractColumnValueString(java, i, _fieldTypes[i], enumName);
				java.append(");\n");
			}
		}
		finishIfNeeded(java);
	}

	public void finishIfNeeded(final Appendable java) throws IOException {
		// if this resultObject is Finishable, call finish()
		if (rsm.types.isAssignable(_returnTypeClass, rsm.finishableType))
			java.append("ret.finish(").append(_resultSetName).append(");\n");
	}

	public int getTypeId(TypeMirror classType) {
		try {
			return _tmf.getTypeId(typeMirrorToClass(classType));
		} catch (ClassNotFoundException e) {
			return rsm.types.isAssignable(classType, rsm.enumType) ? TypeMappingsFactory.TYPE_ENUM : TypeMappingsFactory.TYPE_UNKNOWN;
		}
	}

	public void extractColumnValueString(final Appendable java, final int index, final int resultType, final String enumName) throws IOException, ClassNotFoundException {
		CompilingRowToObjectMapper.extractColumnValueString(java, index, resultType, enumName, _resultSetName, _calendarName);
	}

	public void extractColumnValueString(final Appendable java, final int index, final TypeMirror resultType) throws IOException, ClassNotFoundException {
		CompilingRowToObjectMapper.extractColumnValueString(java, index, getTypeId(resultType), resultType.toString(), _resultSetName, _calendarName);
	}
}
