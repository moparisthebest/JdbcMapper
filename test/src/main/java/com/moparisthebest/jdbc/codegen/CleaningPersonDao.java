package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.Cleaner;
import com.moparisthebest.jdbc.dto.FieldPerson;

import java.sql.SQLException;

@JdbcMapper.Mapper(
		cachePreparedStatements = JdbcMapper.OptionalBool.FALSE
		, allowReflection = JdbcMapper.OptionalBool.TRUE
)
public interface CleaningPersonDao {
	@JdbcMapper.SQL(value = "SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	FieldPerson getPerson(long personNo, Cleaner<FieldPerson> personCleaner) throws SQLException;
}
