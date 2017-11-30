package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.dto.FieldPerson;
import com.moparisthebest.jdbc.dto.Person;

import java.sql.SQLException;

@JdbcMapper.Mapper(
//		databaseType = JdbcMapper.DatabaseType.ORACLE
		cachePreparedStatements = JdbcMapper.OptionalBool.FALSE
//		, sqlParser = SimpleSQLParser.class
		, allowReflection = JdbcMapper.OptionalBool.TRUE
)
public abstract class AbstractDao {

	@JdbcMapper.SQL(value = "SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	abstract FieldPerson getPerson(long personNo) throws SQLException;

	@JdbcMapper.SQL("SELECT person_no FROM person WHERE last_name = {lastName}")
	abstract long getPersonNo(String lastName) throws SQLException;

	//IFJAVA8_START
	@JdbcMapper.RunInTransaction
	//IFJAVA8_END
	protected Person getPersonInTransaction(final String lastName) throws SQLException {
		return getPerson(getPersonNo(lastName));
	}
}
