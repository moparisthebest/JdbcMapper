package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.ArrayInList;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.sql.SQLException;
import java.util.List;

public interface SQLChecker {
	void checkSql(final ProcessingEnvironment processingEnv, final TypeElement classElement, final JdbcMapper.Mapper mapper, final JdbcMapper.DatabaseType databaseType, final ExecutableElement method, final String sqlStatement, final List<VariableElement> bindParams, final ArrayInList arrayInList) throws SQLException;
}
