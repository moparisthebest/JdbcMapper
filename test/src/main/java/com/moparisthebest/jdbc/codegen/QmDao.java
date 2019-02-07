package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.dto.*;
import com.moparisthebest.jdbc.util.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//IFJAVA8_START
import java.util.stream.Stream;
import java.time.*;
//IFJAVA8_END

@JdbcMapper.Mapper(
		cachePreparedStatements = JdbcMapper.OptionalBool.TRUE
		, allowReflection = JdbcMapper.OptionalBool.TRUE
)
public interface QmDao extends JdbcMapper {

	public static final String personRegular = "SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = {personNo}";
	public static final String bossRegularAndUnderscore = "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department, p.first_name " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = {personNo}";
	public static final String bossRegularAndUnderscoreReverse = "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department, p.first_name AS firstName " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = {personNo}";

	public static final String personRegularNoConstructor = "SELECT person_no, first_name, last_name, birth_date, first_name AS dummy FROM person WHERE person_no = {personNo}";
	public static final String bossRegularAndUnderscoreNoConstructor = "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department, p.first_name, p.first_name AS dummy " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = {personNo}";
	public static final String bossRegularAndUnderscoreReverseNoConstructor = "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department, p.first_name AS firstName, p.first_name AS dummy " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = {personNo}";

	public static final String bossRegular = "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = {personNo}";
	public static final String bossUnderscore = "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = {personNo}";
	public static final String allNames = "SELECT first_name, last_name FROM person WHERE person_no < 4";
	String selectPersonNo = "SELECT person_no FROM person WHERE person_no = {personNo}";
	String selectMapLongPerson = "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no in (2,3,4)";
	String selectLongLong = "SELECT person_no AS first_no, person_no AS last_no FROM person WHERE person_no < 4";
	String selectLongArray = "SELECT 1, 2, 3 FROM person WHERE person_no = 1";
	String bobTomMap = "SELECT 'bob' as bob, 'tom' as tom FROM person WHERE person_no = 1";
	String selectThreePeople = "SELECT person_no, first_name, last_name, birth_date from person WHERE person_no IN ({personNo1}, {personNo2}, {personNo3}) ORDER BY person_no";

	String selectBirthDate = "SELECT birth_date FROM person WHERE person_no = {personNo}";
	String selectNumVal = "SELECT num_val FROM val WHERE val_no = {valNo}";
	String selectStrVal = "SELECT str_val FROM val WHERE val_no = {valNo}";

	@JdbcMapper.SQL(personRegular)
	FieldPerson getFieldRegularPerson(long personNo) throws SQLException;

	@JdbcMapper.SQL(personRegular)
	BuilderPerson getBuilderPerson(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegularAndUnderscore)
	FieldBoss getFieldRegularAndUnderscore(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegularAndUnderscoreReverse)
	FieldBoss getFieldRegularAndUnderscoreReverse(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegularAndUnderscoreNoConstructor)
	FieldBoss getFieldRegularAndUnderscoreNoConstructor(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegularAndUnderscoreReverseNoConstructor)
	FieldBoss getFieldRegularAndUnderscoreReverseNoConstructor(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegular)
	FieldBoss getFieldRegular(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossUnderscore)
	FieldBoss getFieldUnderscore(long personNo) throws SQLException;

	@JdbcMapper.SQL(personRegular)
	SetPerson getSetRegularPerson(long personNo) throws SQLException;

	@JdbcMapper.SQL(personRegularNoConstructor)
	SetPerson getSetRegularPersonNoConstructor(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegularAndUnderscore)
	SetBoss getSetRegularAndUnderscore(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegularAndUnderscoreReverse)
	SetBoss getSetRegularAndUnderscoreReverse(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegular)
	SetBoss getSetRegular(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossUnderscore)
	SetBoss getSetUnderscore(long personNo) throws SQLException;

	@JdbcMapper.SQL(personRegular)
	ReverseFieldPerson getReverseFieldRegularPerson(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegularAndUnderscore)
	ReverseFieldBoss getReverseFieldRegularAndUnderscore(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegularAndUnderscoreReverse)
	ReverseFieldBoss getReverseFieldRegularAndUnderscoreReverse(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegular)
	ReverseFieldBoss getReverseFieldRegular(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossUnderscore)
	ReverseFieldBoss getReverseFieldUnderscore(long personNo) throws SQLException;

	@JdbcMapper.SQL(personRegular)
	ReverseSetPerson getReverseSetRegularPerson(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegularAndUnderscore)
	ReverseSetBoss getReverseSetRegularAndUnderscore(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegularAndUnderscoreReverse)
	ReverseSetBoss getReverseSetRegularAndUnderscoreReverse(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossRegular)
	ReverseSetBoss getReverseSetRegular(long personNo) throws SQLException;

	@JdbcMapper.SQL(bossUnderscore)
	ReverseSetBoss getReverseSetUnderscore(long personNo) throws SQLException;

	@SQL(selectPersonNo)
	Long getPersonNo(long personNo) throws SQLException;

	@SQL(selectPersonNo)
	long getPersonNoPrimitive(long personNo) throws SQLException;

	@SQL(selectPersonNo)
	int getPersonNoPrimitiveInt(int personNo) throws SQLException;

	@SQL(selectPersonNo)
	Long[] getPersonNoObjectArray(Long personNo) throws SQLException;

	@SQL(allNames)
	List<Map<String, String>> getAllNames() throws SQLException;

	@SQL(allNames)
	Map<String, String>[] getAllNamesArray() throws SQLException;

	@SQL(allNames)
	Map<String, String> getAllNameMap() throws SQLException;

	@SQL(selectMapLongPerson)
	Map<Long, FieldBoss> getMapLongPerson() throws SQLException;

	@SQL(selectLongLong)
	Map<Long, Long> getMapLongLong() throws SQLException;


	@SQL(selectLongArray)
	@SingleRow
	Long[] getLongObjectArray() throws SQLException;

	@SQL(selectLongArray)
	@SingleRow
	long[] getLongPrimitiveArray() throws SQLException;

	@SQL(bobTomMap)
	List<Map<String, String>> getBobTomMap() throws SQLException;

	@SQL(bobTomMap)
	List<CaseInsensitiveHashMap<String, String>> getBobTomMapCaseInsensitive() throws SQLException;

	@SQL(selectThreePeople)
	List<FieldPerson> getThreePeople(long personNo1, long personNo2, long personNo3) throws SQLException;

	@SQL(selectThreePeople)
	ResultSetIterable<FieldPerson> getThreePeopleResultSetIterable(long personNo1, long personNo2, long personNo3) throws SQLException;

	//IFJAVA8_START

	@SQL(selectThreePeople)
	Stream<FieldPerson> getThreePeopleStream(long personNo1, long personNo2, long personNo3) throws SQLException;

	//IFJAVA8_END

	@SQL("SELECT first_name, last_name FROM person WHERE person_no = {personNo}")
	EnumPerson getEnumPerson(long personNo) throws SQLException;

	@SQL("SELECT first_name FROM person WHERE person_no = {personNo}")
	EnumPerson getEnumPersonConstructor(long personNo) throws SQLException;

	@SQL("SELECT first_name FROM person WHERE person_no = {personNo}")
	FirstName getFirstName(long personNo) throws SQLException;

    @SQL("SELECT str_val as first_name, str_val as last_name FROM val WHERE val_no = 4")
    EnumPerson getEnumPersonNull() throws SQLException;

    @SQL("SELECT str_val FROM val WHERE val_no = 4")
    FirstName getFirstNameNull() throws SQLException;

	@SQL("SELECT first_name AS M_PERSON_FIRST_NAME FROM person WHERE person_no = {personNo}")
	CaseSensitivePerson getCaseSensitivePerson(long personNo) throws SQLException;

	//IFJAVA8_START

	@SQL(selectBirthDate)
	Instant getBirthdateInstant(long personNo) throws SQLException;

	@SQL(selectBirthDate)
	LocalDateTime getBirthdateLocalDateTime(long personNo) throws SQLException;

	@SQL(selectBirthDate)
	LocalDate getBirthdateLocalDate(long personNo) throws SQLException;

	@SQL(selectBirthDate)
	LocalTime getBirthdateLocalTime(long personNo) throws SQLException;

	@SQL(selectBirthDate)
	ZonedDateTime getBirthdateZonedDateTime(long personNo) throws SQLException;

	@SQL(selectBirthDate)
	OffsetDateTime getBirthdateOffsetDateTime(long personNo) throws SQLException;

	@SQL(selectBirthDate)
	OffsetTime getBirthdateOffsetTime(long personNo) throws SQLException;

	@SQL(selectNumVal)
	Year getYearInt(long valNo) throws SQLException;

	@SQL(selectStrVal)
	Year getYearString(long valNo) throws SQLException;

	@SQL(selectStrVal)
	ZoneId getZoneId(long valNo) throws SQLException;

	@SQL(selectStrVal)
	ZoneOffset getZoneOffsetInt(long valNo) throws SQLException;

	@SQL(selectStrVal)
	ZoneOffset getZoneOffsetString(long valNo) throws SQLException;

	@SQL("SELECT person_no, first_name, last_name, birth_date from person WHERE {person_no IN personNos} ORDER BY person_no")
	List<FieldPerson> getFieldPeopleStream(Stream<Long> personNos) throws SQLException;

	//IFJAVA8_END

	@SQL("SELECT person_no, first_name, last_name, birth_date from person WHERE {person_no IN personNos} ORDER BY person_no")
	List<FieldPerson> getFieldPeople(List<Long> personNos) throws SQLException;

	@SQL("SELECT person_no, first_name, last_name, birth_date from person WHERE {person_no IN personNos} AND ({first_name IN names} OR {last_name IN names}) ORDER BY person_no")
	List<FieldPerson> getFieldPeopleByName(List<Long> personNos, List<String> names) throws SQLException;

	@SQL("SELECT person_no, first_name, last_name, birth_date from person WHERE {person_no NOT IN personNos} ORDER BY person_no")
	List<FieldPerson> getFieldPeopleNotIn(List<Long> personNos) throws SQLException;

	@SQL("INSERT INTO a_thaoeu_table (a_thaoeu_table_no, a_thaoeu_table_val) VALUES (a_thaoeu_table_seq.nextval, {value})")
	long insertGetGeneratedKeyOracle(long value) throws SQLException;

	@SQL("INSERT INTO a_thaoeu_table (a_thaoeu_table_val) VALUES ({value})")
	long insertGetGeneratedKey(long value) throws SQLException;

	@SQL("SELECT person_no FROM person WHERE {sql:sql}")
	List<Long> selectRandomSql(String sql) throws SQLException;

	@SQL("SELECT person_no FROM person WHERE {sql:sql}")
	List<Long> selectRandomSqlBuilder(SqlBuilder sql) throws SQLException;

	@SQL("SELECT person_no FROM person WHERE person_no = {personNo1} {sql:sql} OR first_name = {firstName}")
	List<Long> selectRandomSql(long personNo1, String sql, String firstName) throws SQLException;

	@SQL("SELECT person_no FROM person WHERE person_no = {personNo1} {sql:sql} OR first_name = {firstName}")
	List<Long> selectRandomSqlBuilder(long personNo1, Bindable sql, String firstName) throws SQLException;

	// these we just check if they generated
	@SQL("INSERT {sql:sql}")
	void insertRandomSqlCollection(Collection<Long> sql) throws SQLException;

	@SQL("INSERT {sql:sql}")
	void insertRandomSqlIterable(Iterable<Long> sql) throws SQLException;
}
