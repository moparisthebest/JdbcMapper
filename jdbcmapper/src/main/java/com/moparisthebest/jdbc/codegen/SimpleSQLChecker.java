package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.ArrayInList;
import com.moparisthebest.jdbc.QueryMapper;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.moparisthebest.jdbc.TryClose.tryClose;
import static com.moparisthebest.jdbc.codegen.JdbcMapperProcessor.*;

public class SimpleSQLChecker implements SQLChecker {
	@Override
	public void checkSql(final ProcessingEnvironment processingEnv, final TypeElement classElement, final JdbcMapper.Mapper mapper, final JdbcMapper.DatabaseType databaseType, final ExecutableElement method, final String sqlStatement, final List<VariableElement> bindParams, final ArrayInList arrayInList) throws SQLException {
		Connection conn = null;
		QueryMapper qm = null;
		try {
			conn = getConnection(classElement, mapper, databaseType);
			if (conn == null) {
				getMessager().printMessage(Diagnostic.Kind.ERROR, "connection must be non-null for testing", classElement);
				return;
			}
			conn.setAutoCommit(false);
			final String sql = getSqlToExecute(classElement, conn, databaseType, sqlStatement);
			if(sql == null)
				return; // skip this test
			qm = new QueryMapper(conn);
			qm.executeUpdate(sql, getFakeBindParams(bindParams, conn, arrayInList));
		} catch (Exception e) {
			handleException(getMessager(), e, classElement, method);
		} finally {
			if (conn != null) {
				try {
					conn.rollback();
					conn.setAutoCommit(true);
				} catch (Throwable e) {
					// ignore
				}
				tryClose(conn);
			}
			tryClose(qm);
		}
	}

	public Connection getConnection(final TypeElement classElement, final JdbcMapper.Mapper mapper, final JdbcMapper.DatabaseType databaseType) throws SQLException {
		if (mapper.jndiName().isEmpty()) {
			throw new RuntimeException("@JdbcMapper.jndiName must be non-null for testing");
		}
		return JdbcMapperFactory.connectionFactory(mapper.jndiName()).create();
	}

	public String getSqlToExecute(final TypeElement classElement, final Connection conn, final JdbcMapper.DatabaseType databaseType, final String sqlStatement) {
		switch (databaseType) {
			case ORACLE:
				// oracle, being terrible, gives this error message for explain plans on MERGES
				// so we will just execute those directly instead...
				// ORA-00600: internal error code, arguments: [qctfrc : bfc], [22], [0], [5], [1], [1], [2], [371], [], [], [], []
				// EXPLAIN PLAN FOR also does not work for BEGIN...
				// we are assuming length is at least 5 after being trimmed, if anyone can come up with a valid SQL statement that is shorter let me know...
				final String firstWord = sqlStatement.trim().substring(0, 5).toUpperCase();
				if (firstWord.equals("MERGE") || firstWord.equals("BEGIN"))
					return sqlStatement;
				return "EXPLAIN PLAN FOR " + sqlStatement;
			case ANY:
				return "EXPLAIN " + sqlStatement;
		}
		return sqlStatement;
	}

	public void handleException(final Messager messager, final Exception e, final TypeElement classElement, final ExecutableElement method) {
		if (e != null && e.getMessage() != null && e.getMessage().startsWith("ORA-02291: integrity constraint"))
			return; // since we make up values and rollback we can ignore this, it means SQL is correct at least
		messager.printMessage(Diagnostic.Kind.ERROR, e == null || e.getMessage() == null ? JdbcMapperProcessor.toString(e) : e.getMessage(), method);
	}

	public Collection<Object> getFakeBindParams(final List<VariableElement> bindParams, final Connection conn, final ArrayInList arrayInList) throws SQLException {
		final Collection<Object> ret = new ArrayList<Object>(bindParams.size());
		for (final VariableElement bindParam : bindParams) {
			ret.add(getFakeBindParam(bindParam, conn, arrayInList));
		}
		return ret;
	}

	private static final String defaultString = "n"; // oracle considers empty strings to be null...

	public Object getFakeBindParam(final VariableElement param, final Connection conn, final ArrayInList arrayInList) throws SQLException {
		final TypeMirror o = param.asType();
		// special behavior
		if (param instanceof SpecialVariableElement) {
			final SpecialVariableElement specialParam = (SpecialVariableElement) param;
			switch (specialParam.specialType) {
				case IN_LIST: {
					final TypeMirror componentType;
					if (o.getKind() == TypeKind.ARRAY) {
						componentType = ((ArrayType) o).getComponentType();
					} else if (o.getKind() == TypeKind.DECLARED && types.isAssignable(o, collectionType)) {
						final DeclaredType dt = (DeclaredType) o;
						componentType = dt.getTypeArguments().get(0);
					} else {
						getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, "invalid in-list-variable type, returning null", ((SpecialVariableElement) param).delegate);
						return null;
					}
					if (types.isAssignable(componentType, numberType)) {
						return arrayInList.toArray(conn, true, new Long[]{0L}); // todo: not quite right, oh well for now
					} else if (types.isAssignable(componentType, stringType) || types.isAssignable(componentType, enumType)) {
						return arrayInList.toArray(conn, false, new String[]{defaultString});
					} else {
						getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, "invalid in-list-variable type, returning null", ((SpecialVariableElement) param).delegate);
						return null;
					}
				}
				case BLOB:
					return new ByteArrayInputStream(new byte[1]);
				case CLOB:
					return new StringReader(defaultString);
				case SQL:
					return specialParam.blobStringCharset == null ? "" : specialParam.blobStringCharset;
			}
		}
		// end special behavior
		// we are going to put most common ones up top so it should execute faster normally
		if (types.isAssignable(o, stringType) || types.isAssignable(o, enumType)) { // todo: hack for enum, might try to get one of the options? is it even possible?
			return defaultString;
		} else if (o.getKind().isPrimitive() || types.isAssignable(o, numberType)) {
			switch (o.getKind()) {
				case BOOLEAN:
					return true;
				case BYTE:
				case SHORT:
				case INT:
					return 0;
				case LONG:
					return 0L;
				case CHAR:
					return ' ';
				case FLOAT:
					return 0F;
				case DOUBLE:
					return 0D;
			}
			return null;//new BigDecimal(0D); // todo: not quite right, maybe good enough
			// java.util.Date support, put it in a Timestamp
		} else if (types.isAssignable(o, utilDateType)) {
			return new java.sql.Timestamp(0);
		}
		//IFJAVA8_START
		else if (types.isAssignable(o, instantType) || types.isAssignable(o, localDateTimeType) || types.isAssignable(o, zonedDateTimeType) || types.isAssignable(o, offsetDateTimeType)) {
			return new java.sql.Timestamp(0);
		} else if (types.isAssignable(o, localDateType)) {
			return new java.sql.Date(0);
		} else if (types.isAssignable(o, localTimeType) || types.isAssignable(o, offsetTimeType)) {
			return new java.sql.Time(0);
		}
		//IFJAVA8_END
		// CLOB support
		else if (types.isAssignable(o, readerType) || types.isAssignable(o, clobType)) {
			return new StringReader(defaultString);
		} else if (types.isAssignable(o, inputStreamType) || types.isAssignable(o, blobType) || types.isAssignable(o, fileType)) {
			return new ByteArrayInputStream(new byte[1]);
		} else if (types.isAssignable(o, byteArrayType)) {
			return new byte[1];
		} else if (types.isAssignable(o, sqlArrayType)) {
			getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, "java.sql.Array not yet supported for getFakeBindParam, returning null", param);
			return null;
		} else {
			// shouldn't get here ever, if we do the types should be more specific
			getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, "unsupported type for getFakeBindParam, returning null", param);
			return null;
		}
	}
}
