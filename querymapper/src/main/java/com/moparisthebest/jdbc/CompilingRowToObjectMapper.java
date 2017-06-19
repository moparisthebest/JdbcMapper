package com.moparisthebest.jdbc;

import com.moparisthebest.classgen.Compiler;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map a ResultSet row to an Object. This mapper generates/compiles/executes java code to perform the mapping.
 *
 * @author Travis Burtrum (modifications from beehive)
 * @author Travis Burtrum
 * @see RowToObjectMapper for most details, will document here where this differs
 * <p>
 * Usage differences:
 * 1. Reflection can set non-public or final fields directly, direct java code cannot, so DTOs like that will result in
 * a compilation and therefore mapping error, unless the Cache sent in has allowReflection = true which will use reflection
 * for these Fields in the generated code.
*/
public class CompilingRowToObjectMapper<K, T> extends RowToObjectMapper<K, T> {

	// do not remove, used from generated classes
	public static final String firstColumnError = "Cannot call getFirstColumn when mapKeyType is null!";

	protected final Compiler compiler;
	protected final ResultSetToObject<K, T> resultSetToObject;

	protected String _calendarName = null;

	protected int reflectionFieldIndex = -1;
	protected boolean allowReflection = false;

	public CompilingRowToObjectMapper(final Compiler compiler, final Cache cache, ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		this(compiler, cache, resultSet, returnTypeClass, cal, mapValType, mapKeyType, false);
	}

	public CompilingRowToObjectMapper(final Compiler compiler, final Cache cache, ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType, final boolean caseInsensitiveMap) {
		super(resultSet, returnTypeClass, cal, mapValType, mapKeyType, caseInsensitiveMap);
		this.compiler = compiler;
		try {
			final CompilingRowToObjectMapper.ResultSetKey keys = new CompilingRowToObjectMapper.ResultSetKey(super.getKeysFromResultSet(), _returnTypeClass, _mapKeyType, cal != null);
			//System.out.printf("keys: %s\n", keys);
			@SuppressWarnings("unchecked")
			final ResultSetToObject<K,T> resultSetToObject = (ResultSetToObject<K,T>) cache.cache.get(keys);
			if (resultSetToObject == null) {
				//System.out.printf("cache miss, keys: %s\n", keys);
				// generate and put into cache
				if(keys.hasCalendar)
					_calendarName = "cal";
				allowReflection = cache.allowReflection;
				cache.cache.put(keys, this.resultSetToObject = genClass());
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
		} catch (IOException e) {
			throw new MapperException("CachingRowToObjectMapper: IOException (should never happen?): " + e.getMessage(), e);
		}
	}

	public CompilingRowToObjectMapper(final String[] keys, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		super(keys,null, returnTypeClass, cal, mapValType, mapKeyType, false);
		this.compiler = null;
		this.resultSetToObject = null;
	}

	@Override
	public T mapRowToReturnType() throws SQLException {
		return resultSetToObject.toObject(_resultSet, _cal);
	}

	@Override
	public K getMapKey() throws SQLException {
		return resultSetToObject.getFirstColumn(_resultSet, _cal);
	}

	@Override
	public T toObject(final ResultSet rs, final Calendar cal) throws SQLException {
		return resultSetToObject.toObject(rs, cal);
	}

	@Override
	public com.moparisthebest.jdbc.util.ResultSetToObject<T> getResultSetToObject() {
		return resultSetToObject;
	}

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

	public interface ResultSetToObject<K, T> extends com.moparisthebest.jdbc.util.ResultSetToObject<T> {
		K getFirstColumn(final ResultSet rs, final Calendar cal) throws SQLException;
		T toObject(final ResultSet rs, final Calendar cal) throws SQLException;
	}

	public static class Cache {
		private final Map<CompilingRowToObjectMapper.ResultSetKey, ResultSetToObject<?,?>> cache;
		private final boolean allowReflection;

		public Cache(final Map<ResultSetKey, ResultSetToObject<?, ?>> cache, final boolean allowReflection) {
			if(cache == null)
				throw new NullPointerException("cache cannot be null");
			this.cache = cache;
			this.allowReflection = allowReflection;
		}

		public Cache(final Map<ResultSetKey, ResultSetToObject<?, ?>> cache) {
			this(cache, false);
		}

		public Cache() {
			this(new HashMap<CompilingRowToObjectMapper.ResultSetKey, ResultSetToObject<?,?>>());
		}

		public Cache(final boolean allowReflection) {
			this(new HashMap<CompilingRowToObjectMapper.ResultSetKey, ResultSetToObject<?,?>>(), allowReflection);
		}
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

	public static String escapeMapKeyString(final String s) {
		// todo: escape key string, newlines, double quotes, backslashes...
		// actually it seems like those wouldn't be valid SQL column names, so we won't bother until we hear different...
		return '"' + s + '"';
	}

	// code generation down here
	protected ResultSetToObject<K, T> genClass() throws IOException {
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
		java.append("return ret;\n");

		java.append("  }\n\n  public ").append(kType).append(" getFirstColumn(final java.sql.ResultSet rs, final java.util.Calendar cal) throws java.sql.SQLException {\n    ");
		if(_mapKeyType != null){
			java.append("return ");
			extractColumnValueString(java, 1, _mapKeyType);
		} else {
			java.append("throw new com.moparisthebest.jdbc.MapperException(com.moparisthebest.jdbc.CompilingRowToObjectMapper.firstColumnError)");
		}

		if(reflectionFieldIndex == -1) {
			java.append(footer);
		} else {
			// otherwise we have a reflection field array to set up...
			java.append(";\n  }\n\n");
			java.append("private static final java.lang.reflect.Field[] _fields = new java.lang.reflect.Field[]{\n");
			for(final AccessibleObject ao : _fields)
				if(ao instanceof ReflectionAccessibleObject) {
					final Field f = ((ReflectionAccessibleObject)ao).field;
					java.append("com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(")
							.append(f.getDeclaringClass().getCanonicalName()).append(".class, \"")
							.append(f.getName()).append("\"),\n");
				}
			java.append("};\n}\n");
		}
		//System.out.println(java);
		return compiler.compile(className, java);
	}

	public void gen(final Appendable java, final String tType) throws IOException {

		if(mapOnlySecondColumn){
			java.append("final ").append(tType).append(" ret = ");
			extractColumnValueString(java, 2, _returnTypeClass);
			java.append(";\n");
			return;
		}

		lazyLoadConstructor();

		if (resultSetConstructor) {
			java.append("final ").append(tType).append(" ret = new ").append(tType).append("(rs);\n");
			return;
		}

		if (returnMap) // we want a map
			try {
				java.append("final ").append(tType).append("<String, Object> ret = new ").append(tType).append("<String, Object>();\n");
				final int columnLength = _columnCount + 1;
				if (componentType != null && componentType != Object.class) { // we want a specific value type
					int typeId = _tmf.getTypeId(componentType);
					final String enumName = componentType.getCanonicalName();
					for (int x = 1; x < columnLength; ++x) {
						java.append("ret.put(").append(escapeMapKeyString(keys[x]).toLowerCase()).append(", ");
						extractColumnValueString(java, x, typeId, enumName);
						java.append(");\n");
					}
				} else // we want a generic object type
					for (int x = 1; x < columnLength; ++x)
						java.append("ret.put(").append(escapeMapKeyString(keys[x].toLowerCase())).append(", rs.getObject(").append(String.valueOf(x)).append("));\n");
				return;
			} catch (Throwable e) {
				throw new MapperException(e.getClass().getName() + " when trying to create a Map<String, "
						+ (componentType == null ? "java.lang.Object" : componentType.getName()) + "> from a ResultSet row" +
						", all columns must be of the map value type", e);
			}
		else if (componentType != null) // we want an array
			try {
				java.append("final ").append(tType).append(" ret = new ").append(tType.substring(0, tType.length() - 1)).append(String.valueOf(_columnCount)).append("];\n");
				final int typeId = _tmf.getTypeId(componentType);
				final String enumName = componentType.getCanonicalName();
				for (int x = 0; x < _columnCount; ) {
					java.append("ret[").append(String.valueOf(x)).append("] = ");
					extractColumnValueString(java, ++x, typeId, enumName);
					java.append(";\n");
				}
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
					java.append("final ").append(tType).append(" ret = ");
					extractColumnValueString(java, 1, typeId, _returnTypeClass.getCanonicalName());
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
					java.append("final ").append(tType).append(" ret = (").append(tType).append(") ");
					extractColumnValueString(java, 1, typeId, _returnTypeClass.getCanonicalName());
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
			if (f instanceof Field) {
				// if f not accessible (but super.getFieldMappings() sets it), throw exception during compilation is fine
				java.append("ret.").append(((Field) f).getName()).append(" = ");
				extractColumnValueString(java, i, _fieldTypes[i],
						_fieldTypes[i] == TypeMappingsFactory.TYPE_ENUM ? ((Field) f).getType().getCanonicalName() : null);
				java.append(";\n");
			} else if (f instanceof ReflectionAccessibleObject) {
				java.append("com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[").append(String.valueOf(((ReflectionAccessibleObject)f).index)).append("], ret, ");
				extractColumnValueString(java, i, _fieldTypes[i],
						_fieldTypes[i] == TypeMappingsFactory.TYPE_ENUM ? ((ReflectionAccessibleObject) f).field.getType().getCanonicalName() : null);
				java.append(");\n");
			} else {
				java.append("ret.").append(((Method) f).getName()).append("(");
				extractColumnValueString(java, i, _fieldTypes[i],
						_fieldTypes[i] == TypeMappingsFactory.TYPE_ENUM ? ((Method) f).getParameterTypes()[0].getCanonicalName() : null);
				java.append(");\n");
			}
		}
		// if this resultObject is Finishable, call finish()
		if (Finishable.class.isAssignableFrom(_returnTypeClass))
			java.append("ret.finish(rs);\n");
	}

	@Override
	protected AccessibleObject modField(final Field field, final int index) {
		if(!allowReflection)
			return field;
		final int modifiers = field.getModifiers();
		if(Modifier.isFinal(modifiers) || Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers)) {
			return new ReflectionAccessibleObject(field, ++reflectionFieldIndex);
		}
		return field;
	}

	public void extractColumnValueString(final Appendable java, final int index, final int resultType, final String enumName) throws IOException {
		extractColumnValueString(java, index, resultType, enumName, _calendarName);
	}

	public void extractColumnValueString(final Appendable java, final int index, final Class resultType) throws IOException {
		extractColumnValueString(java, index, resultType, _calendarName);
	}

	public static class ResultSetKey extends CachingRowToObjectMapper.ResultSetKey {
		protected final boolean hasCalendar;

		public ResultSetKey(final String[] keys, final Class<?> returnTypeClass, final Class<?> mapKeyType, final boolean hasCalendar) {
			super(keys, returnTypeClass, mapKeyType);
			this.hasCalendar = hasCalendar;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) return true;
			if (!(o instanceof ResultSetKey)) return false;
			final ResultSetKey that = (ResultSetKey) o;
			return hasCalendar == that.hasCalendar && super.equals(o);
		}

		@Override
		public int hashCode() {
			int result = super.hashCode();
			result = 31 * result + (hasCalendar ? 1 : 0);
			return result;
		}
	}

	public static void extractColumnValueString(final Appendable java, final int index, final Class resultType, final String calendarName) throws IOException {
		extractColumnValueString(java, index, _tmf.getTypeId(resultType), resultType.getCanonicalName(), "rs", calendarName);
	}

	public static void extractColumnValueString(final Appendable java, final int index, final int resultType, final String enumName, final String calendarName) throws IOException {
		extractColumnValueString(java, index, resultType, enumName, "rs", calendarName);
	}

	/**
	 * Extract a column value from the ResultSet and return it as resultType.
	 *
	 * @param index      The column index of the value to extract from the ResultSet.
	 * @param resultType The return type. Defined in TypeMappingsFactory.
	 * @return The extracted value
	 * @throws java.sql.SQLException on error.
	 */
	public static void extractColumnValueString(final Appendable java, final int index, final int resultType, final String enumName, final String resultSetName, final String calendarName) throws IOException {
		switch (resultType) {
			case TypeMappingsFactory.TYPE_INT:
				java.append(resultSetName).append(".getInt(").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_LONG:
				java.append(resultSetName).append(".getLong(").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_FLOAT:
				java.append(resultSetName).append(".getFloat(").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_DOUBLE:
				java.append(resultSetName).append(".getDouble(").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_BYTE:
				java.append(resultSetName).append(".getByte(").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_SHORT:
				java.append(resultSetName).append(".getInt(").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_BOOLEAN:
				java.append("getBooleanYN(").append(resultSetName).append(", ").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_INT_OBJ:
				java.append("getObjectInt(").append(resultSetName).append(", ").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_LONG_OBJ:
				java.append("getObjectLong(").append(resultSetName).append(", ").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_FLOAT_OBJ:
				java.append("getObjectFloat(").append(resultSetName).append(", ").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_DOUBLE_OBJ:
				java.append("getObjectDouble(").append(resultSetName).append(", ").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_BYTE_OBJ:
				java.append("getObjectByte(").append(resultSetName).append(", ").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_SHORT_OBJ:
				java.append("getObjectShort(").append(resultSetName).append(", ").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_BOOLEAN_OBJ:
				java.append("getObjectBooleanYN(").append(resultSetName).append(", ").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_STRING:
			case TypeMappingsFactory.TYPE_XMLBEAN_ENUM:
				java.append(resultSetName).append(".getString(").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_ENUM:
				java.append(enumName).append(".valueOf(").append(resultSetName).append(".getString(").append(String.valueOf(index)).append("))");
				return;
			case TypeMappingsFactory.TYPE_BIG_DECIMAL:
				java.append(resultSetName).append(".getBigDecimal(").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_BYTES:
				java.append(resultSetName).append(".getBytes(").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_TIMESTAMP:
				java.append(resultSetName).append(".getTimestamp(").append(String.valueOf(index));
				if(calendarName != null)
					java.append(", ").append(calendarName);
				java.append(")");
				return;
			case TypeMappingsFactory.TYPE_TIME:
				java.append(resultSetName).append(".getTime(").append(String.valueOf(index));
				if(calendarName != null)
					java.append(", ").append(calendarName);
				java.append(")");
				return;
			case TypeMappingsFactory.TYPE_SQLDATE:
				java.append(resultSetName).append(".getDate(").append(String.valueOf(index));
				if(calendarName != null)
					java.append(", ").append(calendarName);
				java.append(")");
				return;
			case TypeMappingsFactory.TYPE_DATE:
				java.append("getUtilDate(").append(resultSetName).append(", ").append(String.valueOf(index));
				if(calendarName != null)
					java.append(", ").append(calendarName);
				java.append(")");
				return;
			case TypeMappingsFactory.TYPE_CALENDAR:
				java.append("getCalendar(").append(resultSetName).append(", ").append(String.valueOf(index));
				if(calendarName != null)
					java.append(", ").append(calendarName);
				java.append(")");
				return;
			case TypeMappingsFactory.TYPE_REF:
				java.append(resultSetName).append(".getRef(").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_BLOB:
				java.append(resultSetName).append(".getBlob(").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_CLOB:
				java.append(resultSetName).append(".getClob(").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_ARRAY:
				java.append(resultSetName).append(".getArray(").append(String.valueOf(index)).append(")");
				return;
			case TypeMappingsFactory.TYPE_READER:
			case TypeMappingsFactory.TYPE_STREAM:
				throw new MapperException("streaming return types are not supported by the JdbcControl; use ResultSet instead");
			// start java.time support
			case TypeMappingsFactory.TYPE_INSTANT:
				java.append("getInstant(").append(resultSetName).append(", ").append(String.valueOf(index));
				if(calendarName != null)
					java.append(", ").append(calendarName);
				java.append(")");
				return;
			case TypeMappingsFactory.TYPE_LOCALDATETIME:
				java.append("getLocalDateTime(").append(resultSetName).append(", ").append(String.valueOf(index));
				if(calendarName != null)
					java.append(", ").append(calendarName);
				java.append(")");
				return;
			case TypeMappingsFactory.TYPE_LOCALDATE:
				java.append("getLocalDate(").append(resultSetName).append(", ").append(String.valueOf(index));
				if(calendarName != null)
					java.append(", ").append(calendarName);
				java.append(")");
				return;
			case TypeMappingsFactory.TYPE_LOCALTIME:
				java.append("getLocalTime(").append(resultSetName).append(", ").append(String.valueOf(index));
				if(calendarName != null)
					java.append(", ").append(calendarName);
				java.append(")");
				return;
			// todo: send in ZoneId here?
			case TypeMappingsFactory.TYPE_ZONEDDATETIME:
				java.append("getZonedDateTime(").append(resultSetName).append(", ").append(String.valueOf(index));
				if(calendarName != null)
					java.append(", ").append(calendarName);
				java.append(")");
				return;
			case TypeMappingsFactory.TYPE_OFFSETDATETIME:
				java.append("getOffsetDateTime(").append(resultSetName).append(", ").append(String.valueOf(index));
				if(calendarName != null)
					java.append(", ").append(calendarName);
				java.append(")");
				return;
			case TypeMappingsFactory.TYPE_OFFSETTIME:
				java.append("getOffsetTime(").append(resultSetName).append(", ").append(String.valueOf(index));
				if(calendarName != null)
					java.append(", ").append(calendarName);
				java.append(")");
				return;
			case TypeMappingsFactory.TYPE_YEAR:
				java.append("java.time.Year.parse(").append(resultSetName).append(".getString(").append(String.valueOf(index)).append("))");
				return;
			case TypeMappingsFactory.TYPE_ZONEID:
				java.append("java.time.ZoneId.of(").append(resultSetName).append(".getString(").append(String.valueOf(index)).append("))");
				return;
			case TypeMappingsFactory.TYPE_ZONEOFFSET:
				java.append("java.time.ZoneOffset.of(").append(resultSetName).append(".getString(").append(String.valueOf(index)).append("))");
				return;
			// end java.time support
			case TypeMappingsFactory.TYPE_STRUCT:
			case TypeMappingsFactory.TYPE_UNKNOWN:
				// JAVA_TYPE (could be any), or REF
				java.append(resultSetName).append(".getObject(").append(String.valueOf(index)).append(")");
				return;
			default:
				throw new MapperException("internal error: unknown type ID: " + Integer.toString(resultType));
		}
	}
}
