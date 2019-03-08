package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.ResultSetMapper;
import com.moparisthebest.jdbc.TypeReference;
import com.moparisthebest.jdbc.dto.*;
import com.moparisthebest.jdbc.util.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

//IFJAVA8_START
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.*;
//IFJAVA8_END

import static com.moparisthebest.jdbc.QueryMapper.inListReplace;

public class QueryMapperTypeQmDao extends QueryMapperQmDao {

	public QueryMapperTypeQmDao(final Connection conn, final ResultSetMapper rsm) {
		super(conn, rsm);
	}

	@Override
	public FieldPerson getFieldRegularPerson(final long personNo) throws SQLException {
		return qm.toType(personRegular, new TypeReference<FieldPerson>() {}, personNo);
	}

	@Override
	public BuilderPerson getBuilderPerson(final long personNo) throws SQLException {
		return qm.toType(personRegular, new TypeReference<BuilderPerson>() {}, personNo);
	}

	@Override
	public FieldBoss getFieldRegularAndUnderscore(final long personNo) throws SQLException {
		return qm.toType(bossRegularAndUnderscore, new TypeReference<FieldBoss>() {}, personNo);
	}

	@Override
	public FieldBoss getFieldRegularAndUnderscoreReverse(final long personNo) throws SQLException {
		return qm.toType(bossRegularAndUnderscoreReverse, new TypeReference<FieldBoss>() {}, personNo);
	}

	@Override
	public FieldBoss getFieldRegularAndUnderscoreNoConstructor(final long personNo) throws SQLException {
		return qm.toType(bossRegularAndUnderscoreNoConstructor, new TypeReference<FieldBoss>() {}, personNo);
	}

	@Override
	public FieldBoss getFieldRegularAndUnderscoreReverseNoConstructor(final long personNo) throws SQLException {
		return qm.toType(bossRegularAndUnderscoreReverseNoConstructor, new TypeReference<FieldBoss>() {}, personNo);
	}

	@Override
	public FieldBoss getFieldRegular(final long personNo) throws SQLException {
		return qm.toType(bossRegular, new TypeReference<FieldBoss>() {}, personNo);
	}

	@Override
	public FieldBoss getFieldUnderscore(final long personNo) throws SQLException {
		return qm.toType(bossUnderscore, new TypeReference<FieldBoss>() {}, personNo);
	}

	@Override
	public SetPerson getSetRegularPerson(final long personNo) throws SQLException {
		return qm.toType(personRegular, new TypeReference<SetPerson>() {}, personNo);
	}

	@Override
	public SetPerson getSetRegularPersonNoConstructor(final long personNo) throws SQLException {
		return qm.toType(personRegularNoConstructor, new TypeReference<SetPerson>() {}, personNo);
	}

	@Override
	public SetBoss getSetRegularAndUnderscore(final long personNo) throws SQLException {
		return qm.toType(bossRegularAndUnderscore, new TypeReference<SetBoss>() {}, personNo);
	}

	@Override
	public SetBoss getSetRegularAndUnderscoreReverse(final long personNo) throws SQLException {
		return qm.toType(bossRegularAndUnderscoreReverse, new TypeReference<SetBoss>() {}, personNo);
	}

	@Override
	public SetBoss getSetRegular(final long personNo) throws SQLException {
		return qm.toType(bossRegular, new TypeReference<SetBoss>() {}, personNo);
	}

	@Override
	public SetBoss getSetUnderscore(final long personNo) throws SQLException {
		return qm.toType(bossUnderscore, new TypeReference<SetBoss>() {}, personNo);
	}

	@Override
	public ReverseFieldPerson getReverseFieldRegularPerson(final long personNo) throws SQLException {
		return qm.toType(personRegular, new TypeReference<ReverseFieldPerson>() {}, personNo);
	}

	@Override
	public ReverseFieldBoss getReverseFieldRegularAndUnderscore(final long personNo) throws SQLException {
		return qm.toType(bossRegularAndUnderscore, new TypeReference<ReverseFieldBoss>() {}, personNo);
	}

	@Override
	public ReverseFieldBoss getReverseFieldRegularAndUnderscoreReverse(final long personNo) throws SQLException {
		return qm.toType(bossRegularAndUnderscoreReverse, new TypeReference<ReverseFieldBoss>() {}, personNo);
	}

	@Override
	public ReverseFieldBoss getReverseFieldRegular(final long personNo) throws SQLException {
		return qm.toType(bossRegular, new TypeReference<ReverseFieldBoss>() {}, personNo);
	}

	@Override
	public ReverseFieldBoss getReverseFieldUnderscore(final long personNo) throws SQLException {
		return qm.toType(bossUnderscore, new TypeReference<ReverseFieldBoss>() {}, personNo);
	}

	@Override
	public ReverseSetPerson getReverseSetRegularPerson(final long personNo) throws SQLException {
		return qm.toType(personRegular, new TypeReference<ReverseSetPerson>() {}, personNo);
	}

	@Override
	public ReverseSetBoss getReverseSetRegularAndUnderscore(final long personNo) throws SQLException {
		return qm.toType(bossRegularAndUnderscore, new TypeReference<ReverseSetBoss>() {}, personNo);
	}

	@Override
	public ReverseSetBoss getReverseSetRegularAndUnderscoreReverse(final long personNo) throws SQLException {
		return qm.toType(bossRegularAndUnderscoreReverse, new TypeReference<ReverseSetBoss>() {}, personNo);
	}

	@Override
	public ReverseSetBoss getReverseSetRegular(final long personNo) throws SQLException {
		return qm.toType(bossRegular, new TypeReference<ReverseSetBoss>() {}, personNo);
	}

	@Override
	public ReverseSetBoss getReverseSetUnderscore(final long personNo) throws SQLException {
		return qm.toType(bossUnderscore, new TypeReference<ReverseSetBoss>() {}, personNo);
	}

	@Override
	public Long getPersonNo(final long personNo) throws SQLException {
		return qm.toType(selectPersonNo, new TypeReference<Long>() {}, personNo);
	}

	@Override
	public long getPersonNoPrimitive(final long personNo) throws SQLException {
		return super.getPersonNoPrimitive(personNo);
		// todo: anything we can do with primitives here?
		//return qm.toType(selectPersonNo, new TypeReference<long>() {}, personNo);
	}

	@Override
	public int getPersonNoPrimitiveInt(final int personNo) throws SQLException {
		return super.getPersonNoPrimitiveInt(personNo);
		// todo: anything we can do with primitives here?
		//return qm.toType(selectPersonNo, new TypeReference<int>() {}, personNo);
	}

	@Override
	public Long[] getPersonNoObjectArray(final Long personNo) throws SQLException {
		return qm.toType(selectPersonNo, new TypeReference<Long[]>() {}, personNo);
	}

	@Override
	public List<Map<String, String>> getAllNames() throws SQLException {
		return qm.toType(allNames, new TypeReference<List<Map<String, String>>>() {});
	}

	@Override
	public Map<String, String>[] getAllNamesArray() throws SQLException {
		return qm.toType(allNames, new TypeReference<Map<String, String>[]>() {});
	}

	@Override
	public Map<String, String> getAllNameMap() throws SQLException {
		return qm.toType(allNames, new TypeReference<Map<String, String>>() {});
	}

	@Override
	public Map<Long, FieldBoss> getMapLongPerson() throws SQLException {
		return qm.toType(selectMapLongPerson, new TypeReference<Map<Long, FieldBoss>>() {});
	}

	@Override
	public Map<Long, Long> getMapLongLong() throws SQLException {
		return qm.toType(selectLongLong, new TypeReference<Map<Long, Long>>() {});
	}

	@Override
	public Long[] getLongObjectArray() throws SQLException {
		return super.getLongObjectArray();
		// todo: anything we can do with @SingleRow here?
		//return qm.toType(selectLongArray, new TypeReference<Long[]>() {});
	}

	@Override
	public long[] getLongPrimitiveArray() throws SQLException {
		return super.getLongPrimitiveArray();
		// todo: anything we can do with @SingleRow here?
		//return qm.toType(selectLongArray, new TypeReference<long[]>() {});
	}

	@Override
	public List<Map<String, String>> getBobTomMap() throws SQLException {
		return qm.toType(bobTomMap, new TypeReference<List<Map<String, String>>>() {});
	}

	@Override
	public List<CaseInsensitiveHashMap<String, String>> getBobTomMapCaseInsensitive() throws SQLException {
		return qm.toType(bobTomMap, new TypeReference<List<CaseInsensitiveHashMap<String, String>>>() {});
	}

	@Override
	public List<FieldPerson> getThreePeople(final long personNo1, final long personNo2, final long personNo3) throws SQLException {
		return  qm.toType(selectThreePeople,
				new TypeReference<List<FieldPerson>>() {}, personNo1, personNo2, personNo3);
	}

	@Override
	public ResultSetIterable<FieldPerson> getThreePeopleResultSetIterable(final long personNo1, final long personNo2, final long personNo3) throws SQLException {
		return  qm.toType(selectThreePeople,
				new TypeReference<ResultSetIterable<FieldPerson>>() {}, personNo1, personNo2, personNo3);
	}

	//IFJAVA8_START

	@Override
	public Stream<FieldPerson> getThreePeopleStream(final long personNo1, final long personNo2, final long personNo3) throws SQLException {
		return  qm.toType(selectThreePeople,
				new TypeReference<Stream<FieldPerson>>() {}, personNo1, personNo2, personNo3);
	}

	//IFJAVA8_END

	@Override
	public EnumPerson getEnumPerson(long personNo) throws SQLException {
		return qm.toType("SELECT first_name, last_name FROM person WHERE person_no = ?", new TypeReference<EnumPerson>() {}, personNo);
	}

	@Override
	public EnumPerson getEnumPersonConstructor(long personNo) throws SQLException {
		return qm.toType("SELECT first_name FROM person WHERE person_no = ?", new TypeReference<EnumPerson>() {}, personNo);
	}

	@Override
	public FirstName getFirstName(long personNo) throws SQLException {
		return qm.toType("SELECT first_name FROM person WHERE person_no = ?", new TypeReference<FirstName>() {}, personNo);
	}

    @Override
    public EnumPerson getEnumPersonNull() throws SQLException {
        return qm.toType("SELECT str_val as first_name, str_val as last_name FROM val WHERE val_no = 4", new TypeReference<EnumPerson>() {});
    }

    @Override
    public FirstName getFirstNameNull() throws SQLException {
        return qm.toType("SELECT str_val FROM val WHERE val_no = 4", new TypeReference<FirstName>() {});
    }

	@Override
	public CaseSensitivePerson getCaseSensitivePerson(long personNo) throws SQLException {
		return qm.toType("SELECT first_name AS M_PERSON_FIRST_NAME FROM person WHERE person_no = ?", new TypeReference<CaseSensitivePerson>() {}, personNo);
	}

	//IFJAVA8_START

	@Override
	public Instant getBirthdateInstant(long personNo) throws SQLException {
		return qm.toType(selectBirthDate, new TypeReference<Instant>() {}, personNo);
	}

	@Override
	public LocalDateTime getBirthdateLocalDateTime(long personNo) throws SQLException {
		return qm.toType(selectBirthDate, new TypeReference<LocalDateTime>() {}, personNo);
	}

	@Override
	public LocalDate getBirthdateLocalDate(long personNo) throws SQLException {
		return qm.toType(selectBirthDate, new TypeReference<LocalDate>() {}, personNo);
	}

	@Override
	public LocalTime getBirthdateLocalTime(long personNo) throws SQLException {
		return qm.toType(selectBirthDate, new TypeReference<LocalTime>() {}, personNo);
	}

	@Override
	public ZonedDateTime getBirthdateZonedDateTime(long personNo) throws SQLException {
		return qm.toType(selectBirthDate, new TypeReference<ZonedDateTime>() {}, personNo);
	}

	@Override
	public OffsetDateTime getBirthdateOffsetDateTime(long personNo) throws SQLException {
		return qm.toType(selectBirthDate, new TypeReference<OffsetDateTime>() {}, personNo);
	}

	@Override
	public OffsetTime getBirthdateOffsetTime(long personNo) throws SQLException {
		return qm.toType(selectBirthDate, new TypeReference<OffsetTime>() {}, personNo);
	}

	@Override
	public Year getYearInt(long valNo) throws SQLException {
		return qm.toType(selectNumVal, new TypeReference<Year>() {}, valNo);
	}

	@Override
	public Year getYearString(long valNo) throws SQLException {
		return qm.toType(selectStrVal, new TypeReference<Year>() {}, valNo);
	}

	@Override
	public ZoneId getZoneId(long valNo) throws SQLException {
		return qm.toType(selectStrVal, new TypeReference<ZoneId>() {}, valNo);
	}

	@Override
	public ZoneOffset getZoneOffsetInt(long valNo) throws SQLException {
		return qm.toType(selectNumVal, new TypeReference<ZoneOffset>() {}, valNo);
	}

	@Override
	public ZoneOffset getZoneOffsetString(long valNo) throws SQLException {
		return qm.toType(selectStrVal, new TypeReference<ZoneOffset>() {}, valNo);
	}

	@Override
	public List<FieldPerson> getFieldPeopleStream(final Stream<Long> personNos) throws SQLException {
		return qm.toType("SELECT * from person WHERE " + inListReplace + " ORDER BY person_no", new TypeReference<List<FieldPerson>>() {}, qm.inList("person_no", personNos.collect(Collectors.toList())));
	}

	//IFJAVA8_END

	@Override
	public List<FieldPerson> getFieldPeople(final List<Long> personNos) throws SQLException {
		return qm.toType("SELECT * from person WHERE " + inListReplace + " ORDER BY person_no", new TypeReference<List<FieldPerson>>() {}, qm.inList("person_no", personNos));
	}

	@Override
	public List<FieldPerson> getFieldPeopleByName(final List<Long> personNos, final List<String> names) throws SQLException {
		return qm.toType("SELECT * from person WHERE " + inListReplace + " AND (" + inListReplace + " OR " + inListReplace + ") ORDER BY person_no",
				new TypeReference<List<FieldPerson>>() {},
				qm.inList("person_no", personNos),
				qm.inList("first_name", names),
				qm.inList("last_name", names)
		);
	}

	@Override
	public List<FieldPerson> getFieldPeopleNotIn(final List<Long> personNos) throws SQLException {
		return qm.toType("SELECT * from person WHERE " + inListReplace + " ORDER BY person_no", new TypeReference<List<FieldPerson>>() {}, qm.notInList("person_no", personNos));
	}

	@Override
	public List<Long> selectRandomSql(final String sql) throws SQLException {
		return qm.toType("SELECT person_no FROM person WHERE " + sql, new TypeReference<List<Long>>() {});
	}

	@Override
	public List<Long> selectRandomSqlBuilder(final SqlBuilder sql) throws SQLException {
		return qm.toType("SELECT person_no FROM person WHERE " + sql, new TypeReference<List<Long>>() {}, sql);
	}

	@Override
	public List<Long> selectRandomSql(final long personNo1, final String sql, final String firstName) throws SQLException {
		return qm.toType("SELECT person_no FROM person WHERE person_no = ? " + sql + " OR first_name = ?", new TypeReference<List<Long>>() {}, personNo1, firstName);
	}

	@Override
	public List<Long> selectRandomSqlBuilder(final long personNo1, final Bindable sql, final String firstName) throws SQLException {
		return qm.toType("SELECT person_no FROM person WHERE person_no = ? " + sql + " OR first_name = ?", new TypeReference<List<Long>>() {}, personNo1, sql, firstName);
	}

	@Override
	public ResultSet getFieldPeopleResultSet(final List<Long> personNos) throws SQLException {
		return qm.toType("SELECT person_no, first_name, last_name, birth_date from person WHERE " + inListReplace + " ORDER BY person_no", new TypeReference<ResultSet>() {}, qm.inList("person_no", personNos));
	}
}
