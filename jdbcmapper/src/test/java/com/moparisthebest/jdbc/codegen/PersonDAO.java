package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.dto.FieldPerson;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by mopar on 5/24/17.
 */
@JdbcMapper.Mapper(
//		jndiName = "bob",
//		databaseType = JdbcMapper.DatabaseType.ORACLE
//		cachePreparedStatements = false
//		, sqlParser = SimpleSQLParser.class
)
public interface PersonDAO {

	@JdbcMapper.SQL("UPDATE person SET first_name = {firstName} WHERE last_name = {lastName}")
	int setFirstName(String firstName, String lastName);

	@JdbcMapper.SQL("UPDATE person SET first_name = {firstName} WHERE person_no = {personNo}")
	void setFirstName(String firstName, long personNo) throws SQLException;

	@JdbcMapper.SQL("UPDATE person SET first_name = {firstName} WHERE person_no = {personNo}")
	void setFirstNameBlob(byte[] firstName, long personNo) throws SQLException;

	@JdbcMapper.SQL("UPDATE person SET first_name = {firstName} WHERE person_no = {personNo}")
	void setFirstNameBlob(@JdbcMapper.Blob String firstName, long personNo) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name FROM person WHERE person_no = {personNo}")
	String getFirstName(long personNo) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	FieldPerson getPerson(long personNo, Calendar cal) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	FieldPerson getPerson(long personNo) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE last_name = {lastName}")
	List<FieldPerson> getPeople(String lastName) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE {last_name not in lastName}")
	List<FieldPerson> getPeople(String[] lastName) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE {last_name in lastName}")
	List<FieldPerson> getPeople(List<String> lastName) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE last_name = {lastName}")
	FieldPerson[] getPeopleArray(String lastName) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE last_name = {lastName}")
	Iterator<FieldPerson> getPeopleIterator(String lastName) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE last_name = {lastName}")
	ListIterator<FieldPerson> getPeopleListIterator(String lastName) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name, last_name, person_no FROM person WHERE last_name = {lastName}")
	Map<String, FieldPerson> getPersonMap(String lastName) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name, last_name, person_no FROM person WHERE last_name = {lastName}")
	Map<String, List<FieldPerson>> getPersonMapList(String lastName) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name FROM person WHERE person_no = {personNo} and last_name = {lastName}")
	String getFirstName(long personNo, String lastName) throws SQLException;

	// various date checks here
	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	Calendar getBirthDateCalendar(long personNo) throws SQLException;
	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	Calendar getBirthDateCalendar(long personNo, Calendar mycal) throws SQLException;

	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	Timestamp getBirthDateTimestamp(long personNo) throws SQLException;
	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	Timestamp getBirthDateTimestamp(long personNo, Calendar mycal) throws SQLException;

	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	Time getBirthDateTime(long personNo) throws SQLException;
	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	Time getBirthDateTime(long personNo, Calendar mycal) throws SQLException;

	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	java.sql.Date getBirthDateSqlDate(long personNo) throws SQLException;
	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	java.sql.Date getBirthDateSqlDate(long personNo, Calendar mycal) throws SQLException;

	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	java.util.Date getBirthDateUtilDate(long personNo) throws SQLException;
	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	java.util.Date getBirthDateUtilDate(long personNo, Calendar mycal) throws SQLException;
}
