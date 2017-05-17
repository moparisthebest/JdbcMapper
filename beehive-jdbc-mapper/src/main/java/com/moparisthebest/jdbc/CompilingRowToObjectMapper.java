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
 * Created by mopar on 5/16/17.
 */
public class CompilingRowToObjectMapper<T> extends RowToObjectMapper<T> {
	protected final Compiler compiler;
	protected final ResultSetToObject<T> resultSetToObject;

	protected String[] keys = null; // for caching if we must generate class

	public CompilingRowToObjectMapper(final Compiler compiler, final Map<CachingRowToObjectMapper.ResultSetKey, ResultSetToObject<?>> cache, ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType) {
		super(resultSet, returnTypeClass, cal, mapValType);
		this.compiler = compiler;
		try {
			final CachingRowToObjectMapper.ResultSetKey keys = new CachingRowToObjectMapper.ResultSetKey(super.getKeysFromResultSet(), _returnTypeClass);
			//System.out.printf("keys: %s\n", keys);
			@SuppressWarnings("unchecked")
			final ResultSetToObject<T> resultSetToObject = (ResultSetToObject<T>) cache.get(keys);
			if (resultSetToObject == null) {
				//System.out.printf("cache miss, keys: %s\n", keys);
				// generate and put into cache
				this.keys = keys.keys;
				cache.put(keys, this.resultSetToObject = genClass());
				this.keys = null;
				this._fields = null;
				this._fieldTypes = null;
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
	public T mapRowToReturnType() {
		try {
			return resultSetToObject.toObject(_resultSet, _cal);
		} catch (SQLException e) {
			throw new MapperException(e.getMessage(), e);
		}
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

	public interface ResultSetToObject<T> {
		T toObject(final ResultSet rs, final Calendar cal) throws SQLException;
	}

	protected String typeFromName(final Class<?> type) {
		return type.getName(); // todo: naive for now
	}

	protected String escapeJavaString(final String s) {
		// todo: escape key string, newlines, double quotes, backslashes...
		return s;
	}

	// code generation down here
	protected ResultSetToObject<T> genClass() {
		final String className = "CompilingMapper";
		final String tType = typeFromName(_returnTypeClass);
		final String header =
				"import static com.moparisthebest.jdbc.util.ResultSetUtil.*;\n\n" +
						"public final class " + className +
						" implements com.moparisthebest.jdbc.CompilingRowToObjectMapper.ResultSetToObject<" + tType + "> {\n" +
						"  public " + tType + " toObject(final java.sql.ResultSet rs, final java.util.Calendar cal) throws java.sql.SQLException {\n";
		final String footer = "  }\n" +
				"}\n";

		final StringBuilder java = new StringBuilder(header);
		//java.append("return null;\n");
		gen(java, tType);
		java.append(footer);
		//System.out.println(java);
		return compiler.compile(className, java);
	}

	protected void gen(final StringBuilder java, final String tType) {

		if (resultSetConstructor) {
			java.append("return new ").append(tType).append("(rs);\n");
			return;
		}

		if (returnMap) // we want a map
			try {
				// todo: does not call getMapImplementation, I think that's fine
				java.append("final Map<String, Object> ret = new ").append(tType).append("<String, Object>();\n");
				final ResultSetMetaData md = _resultSet.getMetaData();
				final int columnLength = _columnCount + 1;
				if (componentType != null && componentType != Object.class) { // we want a specific value type
					int typeId = _tmf.getTypeId(componentType);
					for (int x = 1; x < columnLength; ++x) {
						java.append("ret.put(").append(escapeJavaString(md.getColumnName(x).toLowerCase())).append(", ");
						extractColumnValueString(java, x, typeId);
						java.append(");\n");
					}
				} else // we want a generic object type
					for (int x = 1; x < columnLength; ++x)
						java.append("ret.put(").append(escapeJavaString(md.getColumnName(x).toLowerCase())).append(", rs.getObject(").append(x).append("));\n");
				java.append("return ret;\n");
				return;
			} catch (Throwable e) {
				throw new MapperException(e.getClass().getName() + " when trying to create a Map<String, "
						+ (componentType == null ? "java.lang.Object" : componentType.getName()) + "> from a ResultSet row" +
						", all columns must be of the map value type", e);
			}
		else if (componentType != null) // we want an array
			try {
				// todo: array initialization syntax?
				java.append("final ").append(tType).append("[] ret = new ").append(tType).append("[").append(_columnCount).append("];\n");
				final int typeId = _tmf.getTypeId(componentType);
				for (int x = 0; x < _columnCount; ) {
					java.append("ret[").append(x).append("] = ");
					extractColumnValueString(java, ++x, typeId);
					java.append(";\n");
				}
				java.append("return ret;\n");
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
		// todo: custom boolean
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
