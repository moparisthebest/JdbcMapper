package com.moparisthebest.jdbc;

import com.moparisthebest.classgen.Compiler;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;

/**
 * Map a ResultSet row to an Object. This mapper generates/compiles/executes java code to perform the mapping.
 *
 * @author Travis Burtrum (modifications from beehive)
 * @author Travis Burtrum
 * @see RowToObjectMapper for most details, will document here where this differs
 * <p>
 * Usage differences:
 * 1. Reflection can set non-public or final fields directly, direct java code cannot, so DTOs like that will result in
 * a compilation and therefore mapping error.
*/
public class CompilingRowToObjectMapper<K, T> extends RowToObjectMapper<K, T> {

	// do not remove, used from generated classes
	public static final String firstColumnError = "Cannot call getFirstColumn when mapKeyType is null!";

	protected final Compiler compiler;
	protected final ResultSetToObject<K, T> resultSetToObject;

	protected String[] keys = null; // for caching if we must generate class

	public CompilingRowToObjectMapper(final Compiler compiler, final Map<CachingRowToObjectMapper.ResultSetKey, ResultSetToObject<?,?>> cache, ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		this(compiler, cache, resultSet, returnTypeClass, cal, mapValType, mapKeyType, false);
	}

	public CompilingRowToObjectMapper(final Compiler compiler, final Map<CachingRowToObjectMapper.ResultSetKey, ResultSetToObject<?,?>> cache, ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType, final boolean caseInsensitiveMap) {
		super(resultSet, returnTypeClass, cal, mapValType, mapKeyType, caseInsensitiveMap);
		this.compiler = compiler;
		try {
			final CachingRowToObjectMapper.ResultSetKey keys = new CachingRowToObjectMapper.ResultSetKey(super.getKeysFromResultSet(), _returnTypeClass, _mapKeyType);
			//System.out.printf("keys: %s\n", keys);
			@SuppressWarnings("unchecked")
			final ResultSetToObject<K,T> resultSetToObject = (ResultSetToObject<K,T>) cache.get(keys);
			if (resultSetToObject == null) {
				//System.out.printf("cache miss, keys: %s\n", keys);
				// generate and put into cache
				this.keys = keys.keys;
				cache.put(keys, this.resultSetToObject = genClass());
				this.keys = null;
				this._fields = null;
				this._fieldTypes = null;
				this.constructor = null;
			} else {
				//System.out.printf("cache hit, keys: %s\n", keys);
				// load from cache
				this.resultSetToObject = resultSetToObject;
			}
		} catch (SQLException e) {
			throw new MapperException("CachingRowToObjectMapper: SQLException: " + e.getMessage(), e);
		}
	}

	@Override
	public T mapRowToReturnType() throws SQLException {
		return resultSetToObject.toObject(_resultSet, _cal);
	}

	@Override
	public K getMapKey() throws SQLException {
		return resultSetToObject.getFirstColumn(_resultSet, _cal);
	}
// todo: generate/cache these to make it faster for Map and MapCollection? maybe just getKey and getVal methods instead
	/*
	@Override
	public <E> E extractColumnValue(final int index, final Class<E> classType) throws SQLException {
		return super.extractColumnValue(index, classType);
	}
	*/

	@Override
	protected String[] getKeysFromResultSet() throws SQLException {
		if (keys == null)
			throw new MapperException("not supported here");
		return keys;
	}

	@Override
	protected void getFieldMappings() throws SQLException {
		throw new MapperException("not supported here");
	}

	public interface ResultSetToObject<K, T> {
		K getFirstColumn(final ResultSet rs, final Calendar cal) throws SQLException;
		T toObject(final ResultSet rs, final Calendar cal) throws SQLException;
	}

	protected String typeFromName(final Class<?> type) {
		if(type == null)
			return "Object";
		if(_columnCount == 1 && type.isPrimitive()) {
			// need the object equivalent here, what is the best way? this works, isn't pretty...
			if(type.equals(Character.TYPE))
				return "Character";
			if(type.equals(Integer.TYPE))
				return "Integer";
			final char[] name = type.getName().toCharArray();
			name[0] = Character.toUpperCase(name[0]);
			return new String(name);
		} else if (returnMap || componentType == null) {
			return type.getName();
		} else {
			// an array, annoying syntax
			final String name = type.getName();
			final char charType = name.charAt(1);
			switch (charType) {
				case 'L':
					return name.substring(2, name.length() - 1) + "[]";
				case 'Z':
					return "boolean[]";
				case 'B':
					return "byte[]";
				case 'C':
					return "char[]";
				case 'D':
					return "double[]";
				case 'F':
					return "float[]";
				case 'I':
					return "int[]";
				case 'J':
					return "long[]";
				case 'S':
					return "short[]";
				case '[':
				default:
					throw new MapperException("only supports single dimensional array");
			}
		}
	}

	protected String escapeMapKeyString(final String s) {
		// todo: escape key string, newlines, double quotes, backslashes...
		// actually it seems like those wouldn't be valid SQL column names, so we won't bother until we hear different...
		return '"' + s + '"';
	}

	// code generation down here
	protected ResultSetToObject<K, T> genClass() {
		final String className = "CompilingMapper";
		final String tType = typeFromName(_returnTypeClass);
		final String kType = typeFromName(_mapKeyType);
		final String header =
				"import static com.moparisthebest.jdbc.util.ResultSetUtil.*;\n\n" +
						"public final class " + className +
						" implements com.moparisthebest.jdbc.CompilingRowToObjectMapper.ResultSetToObject<"+ kType +"," + tType + "> {\n" +
						"  public " + tType + " toObject(final java.sql.ResultSet rs, final java.util.Calendar cal) throws java.sql.SQLException {\n";
		final String footer = ";\n  }\n" +
				"}\n";

		final StringBuilder java = new StringBuilder(header);
		//java.append("return null;\n");
		gen(java, tType);

		java.append("  }\n\n  public ").append(kType).append(" getFirstColumn(final java.sql.ResultSet rs, final java.util.Calendar cal) throws java.sql.SQLException {\n    ");
		if(_mapKeyType != null){
			java.append("return ");
			extractColumnValueString(java, 1, _tmf.getTypeId(_mapKeyType));
		} else {
			java.append("throw new com.moparisthebest.jdbc.MapperException(com.moparisthebest.jdbc.CompilingRowToObjectMapper.firstColumnError)");
		}

		java.append(footer);
		//System.out.println(java);
		return compiler.compile(className, java);
	}

	protected void gen(final StringBuilder java, final String tType) {

		if(mapOnlySecondColumn){
			java.append("return ");
			extractColumnValueString(java, 2, _tmf.getTypeId(_returnTypeClass));
			java.append(";\n");
			return;
		}

		lazyLoadConstructor();

		if (resultSetConstructor) {
			java.append("return new ").append(tType).append("(rs);\n");
			return;
		}

		if (returnMap) // we want a map
			try {
				// todo: does not call getMapImplementation, I think that's fine
				java.append("final ").append(tType).append("<String, Object> ret = new ").append(tType).append("<String, Object>();\n");
				final ResultSetMetaData md = _resultSet.getMetaData();
				final int columnLength = _columnCount + 1;
				if (componentType != null && componentType != Object.class) { // we want a specific value type
					int typeId = _tmf.getTypeId(componentType);
					for (int x = 1; x < columnLength; ++x) {
						java.append("ret.put(").append(escapeMapKeyString(md.getColumnName(x).toLowerCase())).append(", ");
						extractColumnValueString(java, x, typeId);
						java.append(");\n");
					}
				} else // we want a generic object type
					for (int x = 1; x < columnLength; ++x)
						java.append("ret.put(").append(escapeMapKeyString(md.getColumnName(x).toLowerCase())).append(", rs.getObject(").append(x).append("));\n");
				java.append("return ret;\n");
				return;
			} catch (Throwable e) {
				throw new MapperException(e.getClass().getName() + " when trying to create a Map<String, "
						+ (componentType == null ? "java.lang.Object" : componentType.getName()) + "> from a ResultSet row" +
						", all columns must be of the map value type", e);
			}
		else if (componentType != null) // we want an array
			try {
				java.append("final ").append(tType).append(" ret = new ").append(tType.substring(0, tType.length() - 1)).append(_columnCount).append("];\n");
				final int typeId = _tmf.getTypeId(componentType);
				for (int x = 0; x < _columnCount; ) {
					java.append("ret[").append(x).append("] = ");
					extractColumnValueString(java, ++x, typeId);
					java.append(";\n");
				}
				java.append("return ret;\n");
				return;
			} catch (Throwable e) {
				throw new MapperException(e.getClass().getName() + " when trying to create a "
						+ componentType.getName() + "[] from a ResultSet row, all columns must be of that type", e);
			}

		// if the ResultSet only contains a single column we may be able to map directly
		// to the return type -- if so we don't need to build any structures to support
		// mapping
		if (_columnCount == 1) {

			final int typeId = _tmf.getTypeId(_returnTypeClass);

			try {
				if (typeId != TypeMappingsFactory.TYPE_UNKNOWN) {
					java.append("return ");
					extractColumnValueString(java, 1, typeId);
					java.append(";\n");
					return;
				} else {
					// we still might want a single value (i.e. java.util.Date)
					/*
					Object val = extractColumnValue(1, typeId);
					if (_returnTypeClass.isAssignableFrom(val.getClass())) {
						return _returnTypeClass.cast(val);
					}
					*/
					// we could actually pull from first row like above and test it first and fail now, but maybe just failing during compilation is enough?
					java.append("return (").append(tType).append(") ");
					extractColumnValueString(java, 1, typeId);
					java.append(";\n");
					return;
				}
			} catch (Exception e) {
				throw new MapperException(e.getMessage(), e);
			}
		}

		if (_fields == null) {
			try {
				super.getFieldMappings();
			} catch (SQLException e) {
				throw new MapperException(e.getMessage(), e);
			}
		}

		java.append("final ").append(tType).append(" ret = new ").append(tType).append("();\n");

		for (int i = 1; i < _fields.length; i++) {
			AccessibleObject f = _fields[i];

			//_args[0] = extractColumnValue(i, _fieldTypes[i]);
			//System.out.printf("field: '%s' obj: '%s' fieldType: '%s'\n", _fields[i], _args[0], _fieldTypes[i]);
			// custom hacked-in support for enums, can do better when we scrap org.apache.beehive.controls.system.jdbc.TypeMappingsFactory
			if (_fieldTypes[i] == 0) {
				final Class<?> fieldType = f instanceof Field ? ((Field) f).getType() : ((Method) f).getParameterTypes()[0];
				if (Enum.class.isAssignableFrom(fieldType)) {
					_args[0] = Enum.valueOf((Class<? extends Enum>) fieldType, (String) _args[0]);
					if (f instanceof Field) {
						// if f not accessible (but super.getFieldMappings() sets it), throw exception during compilation is fine
						java.append("ret.").append(((Field) f).getName()).append(" = ").append(typeFromName(fieldType)).append(".valueOf(");
						extractColumnValueString(java, i, _fieldTypes[i]);
						java.append(");\n");
					} else {
						java.append("ret.").append(((Method) f).getName()).append("(").append(typeFromName(fieldType)).append(".valueOf(");
						extractColumnValueString(java, i, _fieldTypes[i]);
						java.append("));\n");
					}
				}
			}
			if (f instanceof Field) {
				// if f not accessible (but super.getFieldMappings() sets it), throw exception during compilation is fine
				java.append("ret.").append(((Field) f).getName()).append(" = ");
				extractColumnValueString(java, i, _fieldTypes[i]);
				java.append(";\n");
			} else {
				java.append("ret.").append(((Method) f).getName()).append("(");
				extractColumnValueString(java, i, _fieldTypes[i]);
				java.append(");\n");
			}
		}
		// if this resultObject is Finishable, call finish()
		if (Finishable.class.isAssignableFrom(_returnTypeClass))
			java.append("ret.finish(rs);\n");
		java.append("return ret;\n");
	}

	/**
	 * Extract a column value from the ResultSet and return it as resultType.
	 *
	 * @param index      The column index of the value to extract from the ResultSet.
	 * @param resultType The return type. Defined in TypeMappingsFactory.
	 * @return The extracted value
	 * @throws java.sql.SQLException on error.
	 */
	protected void extractColumnValueString(final StringBuilder java, final int index, final int resultType) {
		switch (resultType) {
			case TypeMappingsFactory.TYPE_INT:
				java.append("rs.getInt(").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_LONG:
				java.append("rs.getLong(").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_FLOAT:
				java.append("rs.getFloat(").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_DOUBLE:
				java.append("rs.getDouble(").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_BYTE:
				java.append("rs.getByte(").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_SHORT:
				java.append("rs.getInt(").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_BOOLEAN:
				java.append("getBooleanYN(rs, ").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_INT_OBJ:
				java.append("getObjectInt(rs, ").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_LONG_OBJ:
				java.append("getObjectLong(rs, ").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_FLOAT_OBJ:
				java.append("getObjectFloat(rs, ").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_DOUBLE_OBJ:
				java.append("getObjectDouble(rs, ").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_BYTE_OBJ:
				java.append("getObjectByte(rs, ").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_SHORT_OBJ:
				java.append("getObjectShort(rs, ").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_BOOLEAN_OBJ:
				java.append("getObjectBooleanYN(rs, ").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_STRING:
			case TypeMappingsFactory.TYPE_XMLBEAN_ENUM:
				java.append("rs.getString(").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_BIG_DECIMAL:
				java.append("rs.getBigDecimal(").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_BYTES:
				java.append("rs.getBytes(").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_TIMESTAMP:
				java.append("getTimestamp(rs, cal, ").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_TIME:
				java.append("getTime(rs, cal, ").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_SQLDATE:
				java.append("getSqlDate(rs, cal, ").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_DATE:
				java.append("getUtilDate(rs, cal, ").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_CALENDAR:
				java.append("getCalendar(rs, cal, ").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_REF:
				java.append("rs.getRef(").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_BLOB:
				java.append("rs.getBlob(").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_CLOB:
				java.append("rs.getClob(").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_ARRAY:
				java.append("rs.getArray(").append(index).append(")");
				return;
			case TypeMappingsFactory.TYPE_READER:
			case TypeMappingsFactory.TYPE_STREAM:
				throw new MapperException("streaming return types are not supported by the JdbcControl; use ResultSet instead");
			case TypeMappingsFactory.TYPE_STRUCT:
			case TypeMappingsFactory.TYPE_UNKNOWN:
				// JAVA_TYPE (could be any), or REF
				java.append("rs.getObject(").append(index).append(")");
				return;
			default:
				throw new MapperException("internal error: unknown type ID: " + Integer.toString(resultType));
		}
	}
}
