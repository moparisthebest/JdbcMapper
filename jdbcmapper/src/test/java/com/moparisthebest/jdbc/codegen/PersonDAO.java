package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.Cleaner;
import com.moparisthebest.jdbc.dto.*;
import com.moparisthebest.jdbc.util.ResultSetIterable;

import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
//IFJAVA8_START
import java.util.stream.Stream;
import java.time.*;
//IFJAVA8_END

/**
 * Created by mopar on 5/24/17.
 */
@JdbcMapper.Mapper(
		jndiName = "bob",
//		databaseType = JdbcMapper.DatabaseType.ORACLE
		cachePreparedStatements = JdbcMapper.OptionalBool.FALSE
//		, sqlParser = SimpleSQLParser.class
		, allowReflection = JdbcMapper.OptionalBool.TRUE
)
public interface PersonDAO extends JdbcMapper {

	@JdbcMapper.SQL("CREATE TABLE person (person_no NUMERIC, first_name VARCHAR(40), last_name VARCHAR(40), birth_date TIMESTAMP)")
	void createTablePerson();

	@JdbcMapper.SQL("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES ({personNo}, {birthDate}, {firstName}, {lastName})")
	int insertPerson(long personNo, Date birthDate, String firstName, String lastName);

	@JdbcMapper.SQL("UPDATE person SET first_name = {firstName} WHERE last_name = {lastName}")
	int setFirstName(String firstName, String lastName);

	@JdbcMapper.SQL("UPDATE person SET first_name = {firstName} WHERE person_no = {personNo}")
	void setFirstName(String firstName, long personNo) throws SQLException;

	@JdbcMapper.SQL("UPDATE person SET first_name = {firstName} WHERE person_no = {personNo}")
	void setFirstNameBlob(byte[] firstName, long personNo) throws SQLException;

	@JdbcMapper.SQL("UPDATE person SET first_name = {firstName} WHERE person_no = {personNo}")
	void setFirstNameBlob(@JdbcMapper.Blob String firstName, long personNo) throws SQLException;

	@JdbcMapper.SQL("SELECT person_no FROM person WHERE last_name = {lastName}")
	long getPersonNo(String lastName) throws SQLException;

	@JdbcMapper.WarnOnUnusedParams
	@JdbcMapper.SQL("SELECT person_no FROM person WHERE last_name = {lastName}")
	long getPersonNoUnusedParam(String lastName, String unused) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE last_name = {lastName}")
	ResultSet getPeopleResultSet(String lastName) throws SQLException;

	@JdbcMapper.SQL(value = "SELECT first_name, last_name FROM person WHERE last_name = {lastName}", cachePreparedStatement = JdbcMapper.OptionalBool.TRUE)
	ResultSet getPeopleResultSetCached(String lastName) throws SQLException;


	@JdbcMapper.SQL(value = "SELECT first_name FROM person WHERE person_no = {personNo}", columnNames = {"firstName"})
	String getFirstNameColumnNames(long personNo) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name FROM person WHERE person_no = {personNo}")
	String getFirstName(long personNo) throws SQLException;

	@JdbcMapper.SQL(value = "SELECT first_name FROM person WHERE person_no = {personNo}", isSelect = false)
	int getFirstNameUpdate(long personNo) throws SQLException;

	@JdbcMapper.SQL(value = "SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = {personNo}",
					columnNames = {"personNo", "firstName", "lastName", "birthDate"})
	FieldPerson getPersonColumnNames(long personNo) throws SQLException;

	@JdbcMapper.SQL(value = "SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	FieldPerson getPerson(long personNo) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	FieldPerson getPerson(long personNo, Calendar cal) throws SQLException;

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

	// cleaner checks
	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	FieldPerson getPersonCleanFieldPerson(long personNo, Cleaner<FieldPerson> clean) throws SQLException;
	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	FieldPerson getPersonCleanPerson(long personNo, Cleaner<Person> clean) throws SQLException;
	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	FieldPerson getPersonCleanObject(long personNo, Cleaner<Object> clean) throws SQLException;
	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	List<FieldPerson> getPersonCleanPersonList(long personNo, Cleaner<Person> clean) throws SQLException;
	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	Map<String, FieldPerson> getPersonCleanPersonMap(long personNo, Cleaner<Person> clean) throws SQLException;
	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	Map<String, List<FieldPerson>> getPersonCleanPersonMapList(long personNo, Cleaner<Person> clean) throws SQLException;
	/*
	// this should NOT compile:
	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	FieldPerson getPersonCleanNumber(long personNo, Cleaner<Number> clean) throws SQLException;
	*/

	// max row checks
	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	Map<String, FieldPerson> getPersonDynamicLimit(long personNo, byte maxRows) throws SQLException;
	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	Map<String, FieldPerson> getPersonDynamicLimit(long personNo, short arrayMaxLength) throws SQLException;
	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	List<FieldPerson> getPersonDynamicLimit(long personNo, int maxRows) throws SQLException;
	@JdbcMapper.SQL("SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	Map<String, List<FieldPerson>> getPersonDynamicLimit(long personNo, long rowLimit) throws SQLException;


	@JdbcMapper.SQL(value = "SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}", maxRows = Byte.MAX_VALUE)
	List<FieldPerson> getPersonStaticLimitListByte(long personNo) throws SQLException;
	@JdbcMapper.SQL(value = "SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}", maxRows = Short.MAX_VALUE)
	List<FieldPerson> getPersonStaticLimitList(long personNo) throws SQLException;
	@JdbcMapper.SQL(value = "SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}", maxRows = Integer.MAX_VALUE)
	Map<String, FieldPerson> getPersonStaticLimitMap(long personNo) throws SQLException;
	@JdbcMapper.SQL(value = "SELECT first_name, last_name, birth_date FROM person WHERE person_no = {personNo}", maxRows = Long.MAX_VALUE)
	Map<String, List<FieldPerson>> getPersonStaticLimitMapList(long personNo) throws SQLException;


	@JdbcMapper.SQL("SELECT person_no, birth_date, last_name, first_name from person WHERE person_no IN ({personNo1},{personNo2},{personNo3}) ORDER BY person_no")
	List<FieldPerson> getPeopleList(long personNo1, long personNo2, long personNo3) throws SQLException;

	@JdbcMapper.SQL("SELECT person_no, birth_date, last_name, first_name from person WHERE person_no IN ({personNo1},{personNo2},{personNo3}) ORDER BY person_no")
	ResultSetIterable<FieldPerson> getPeopleResultSetIterable(long personNo1, long personNo2, long personNo3) throws SQLException;

	@JdbcMapper.SQL(value = "SELECT person_no, birth_date, last_name, first_name from person WHERE person_no IN ({personNo1},{personNo2},{personNo3}) ORDER BY person_no", cachePreparedStatement = JdbcMapper.OptionalBool.TRUE)
	ResultSetIterable<FieldPerson> getPeopleResultSetIterableCachedPreparedStatement(long personNo1, long personNo2, long personNo3) throws SQLException;

	//IFJAVA8_START

	@JdbcMapper.SQL("SELECT person_no, birth_date, last_name, first_name from person WHERE person_no IN ({personNo1},{personNo2},{personNo3}) ORDER BY person_no")
	Stream<FieldPerson> getPeopleStream(long personNo1, long personNo2, long personNo3) throws SQLException;

	@JdbcMapper.SQL(value = "SELECT person_no, birth_date, last_name, first_name from person WHERE person_no IN ({personNo1},{personNo2},{personNo3}) ORDER BY person_no", cachePreparedStatement = JdbcMapper.OptionalBool.TRUE)
	Stream<FieldPerson> getPeopleStreamCachedPreparedStatement(long personNo1, long personNo2, long personNo3) throws SQLException;

	//IFJAVA8_END

	@JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE person_no = {personNo}")
	EnumPerson getEnumPerson(long personNo) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name FROM person WHERE person_no = {personNo}")
	EnumPerson getEnumPersonConstructor(long personNo) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name FROM person WHERE person_no = {personNo}")
	FirstName getFirstNameEnum(long personNo) throws SQLException;

	@JdbcMapper.SQL("SELECT str_val as first_name, str_val as last_name FROM val WHERE val_no = 4")
	EnumPerson getEnumPersonNull() throws SQLException;

	@JdbcMapper.SQL("SELECT str_val FROM val WHERE val_no = 4")
	FirstName getEnumNull() throws SQLException;

	@JdbcMapper.SQL("SELECT first_name AS M_PERSON_FIRST_NAME FROM person WHERE person_no = {personNo}")
	CaseSensitivePerson getCaseSensitivePerson(long personNo);

	//IFJAVA8_START

	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	Instant getBirthDateInstant(long personNo);

	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	LocalDateTime getBirthDateLocalDateTime(long personNo);

	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	LocalDate getBirthDateLocalDate(long personNo);

	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	LocalTime getBirthDateLocalTime(long personNo);

	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	ZonedDateTime getBirthDateZonedDateTime(long personNo);

	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	OffsetDateTime getBirthDateOffsetDateTime(long personNo);

	@JdbcMapper.SQL("SELECT birth_date FROM person WHERE person_no = {personNo}")
	OffsetTime getBirthDateZonedOffsetTime(long personNo);

	@JdbcMapper.SQL("SELECT num_val FROM val WHERE val_no = {valNo}")
	Year getYearInt(long valNo);

	@JdbcMapper.SQL("SELECT str_val FROM val WHERE val_no = {valNo}")
	Year getYearString(long valNo);

	@JdbcMapper.SQL("SELECT str_val FROM val WHERE val_no = {valNo}")
	ZoneId getZoneId(long valNo);

	@JdbcMapper.SQL("SELECT num_val FROM val WHERE val_no = {valNo}")
	ZoneOffset getZoneOffsetInt(long valNo);

	@JdbcMapper.SQL("SELECT str_val FROM val WHERE val_no = {valNo}")
	ZoneOffset getZoneOffsetStr(long valNo);

	@JdbcMapper.RunInTransaction
	default Person getPersonInTransaction(final String lastName) throws SQLException {
		return getPerson(getPersonNo(lastName));
	}

	//IFJAVA8_END

	// test blob
	@JdbcMapper.SQL("SELECT some_blob FROM val WHERE val_no = {valNo}")
	byte[] getBlob(long valNo);

	// test Single rows
	@JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE person_no = {personNo}")
	@SingleRow
	String[] getSinglePersonNameArray(long personNo) throws SQLException;

	@JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE person_no = {personNo}")
	@SingleRow
	Map<String,String> getSinglePersonNameMap(long personNo) throws SQLException;
}
