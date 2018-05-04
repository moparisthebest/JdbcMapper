package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.QueryMapper;
import com.moparisthebest.jdbc.ResultSetMapper;
import com.moparisthebest.jdbc.TypeReference;
import com.moparisthebest.jdbc.dto.*;
import com.moparisthebest.jdbc.util.CaseInsensitiveHashMap;
import com.moparisthebest.jdbc.util.ResultSetIterable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.moparisthebest.jdbc.TryClose.tryClose;

public class QueryMapperQmDao implements QmDao {

	public static final String personRegular = "SELECT * FROM person WHERE person_no = ?";
	public static final String bossRegularAndUnderscore = "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department, p.first_name " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = ?";
	public static final String bossRegularAndUnderscoreReverse = "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department, p.first_name AS firstName " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = ?";
	public static final String bossRegular = "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = ?";
	public static final String bossUnderscore = "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = ?";
	public static final String selectPersonNo = "SELECT person_no FROM person WHERE person_no = ?";
	public static final String selectThreePeople = "SELECT * from person WHERE person_no IN (?,?,?) ORDER BY person_no";

	private final QueryMapper qm;

	public QueryMapperQmDao(final Connection conn, final ResultSetMapper rsm) {
		this.qm = new QueryMapper(conn, rsm);
	}

	@Override
	public Connection getConnection() {
		return qm.getConnection();
	}

	public QueryMapper getQm() {
		return qm;
	}

	@Override
	public void close() {
		tryClose(qm);
	}

	@Override
	public FieldPerson getFieldRegularPerson(final long personNo) throws SQLException {
		return qm.toObject(personRegular, FieldPerson.class, personNo);
	}

	@Override
	public BuilderPerson getBuilderPerson(final long personNo) throws SQLException {
		return qm.toObject(personRegular, BuilderPerson.class, personNo);
	}

	@Override
	public FieldBoss getFieldRegularAndUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscore, FieldBoss.class, personNo);
	}

	@Override
	public FieldBoss getFieldRegularAndUnderscoreReverse(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscoreReverse, FieldBoss.class, personNo);
	}

	@Override
	public FieldBoss getFieldRegular(final long personNo) throws SQLException {
		return qm.toObject(bossRegular, FieldBoss.class, personNo);
	}

	@Override
	public FieldBoss getFieldUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossUnderscore, FieldBoss.class, personNo);
	}

	@Override
	public SetPerson getSetRegularPerson(final long personNo) throws SQLException {
		return qm.toObject(personRegular, SetPerson.class, personNo);
	}

	@Override
	public SetBoss getSetRegularAndUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscore, SetBoss.class, personNo);
	}

	@Override
	public SetBoss getSetRegularAndUnderscoreReverse(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscoreReverse, SetBoss.class, personNo);
	}

	@Override
	public SetBoss getSetRegular(final long personNo) throws SQLException {
		return qm.toObject(bossRegular, SetBoss.class, personNo);
	}

	@Override
	public SetBoss getSetUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossUnderscore, SetBoss.class, personNo);
	}

	@Override
	public ReverseFieldPerson getReverseFieldRegularPerson(final long personNo) throws SQLException {
		return qm.toObject(personRegular, ReverseFieldPerson.class, personNo);
	}

	@Override
	public ReverseFieldBoss getReverseFieldRegularAndUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscore, ReverseFieldBoss.class, personNo);
	}

	@Override
	public ReverseFieldBoss getReverseFieldRegularAndUnderscoreReverse(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscoreReverse, ReverseFieldBoss.class, personNo);
	}

	@Override
	public ReverseFieldBoss getReverseFieldRegular(final long personNo) throws SQLException {
		return qm.toObject(bossRegular, ReverseFieldBoss.class, personNo);
	}

	@Override
	public ReverseFieldBoss getReverseFieldUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossUnderscore, ReverseFieldBoss.class, personNo);
	}

	@Override
	public ReverseSetPerson getReverseSetRegularPerson(final long personNo) throws SQLException {
		return qm.toObject(personRegular, ReverseSetPerson.class, personNo);
	}

	@Override
	public ReverseSetBoss getReverseSetRegularAndUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscore, ReverseSetBoss.class, personNo);
	}

	@Override
	public ReverseSetBoss getReverseSetRegularAndUnderscoreReverse(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscoreReverse, ReverseSetBoss.class, personNo);
	}

	@Override
	public ReverseSetBoss getReverseSetRegular(final long personNo) throws SQLException {
		return qm.toObject(bossRegular, ReverseSetBoss.class, personNo);
	}

	@Override
	public ReverseSetBoss getReverseSetUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossUnderscore, ReverseSetBoss.class, personNo);
	}

	@Override
	public Long getPersonNo(final long personNo) throws SQLException {
		return qm.toObject(selectPersonNo, Long.class, personNo);
	}

	@Override
	public long getPersonNoPrimitive(final long personNo) throws SQLException {
		return qm.toObject(selectPersonNo, long.class, personNo);
	}

	@Override
	public int getPersonNoPrimitiveInt(final int personNo) throws SQLException {
		return qm.toObject(selectPersonNo, int.class, personNo);
	}

	@Override
	public Long[] getPersonNoObjectArray(final Long personNo) throws SQLException {
		return qm.toArray(selectPersonNo, Long.class, personNo);
	}

	@Override
	public List<Map<String, String>> getAllNames() throws SQLException {
		return qm.toListMap(allNames, Map.class, String.class);
	}

	/*
	@Override
	public Map<String, String>[] getAllNamesArray() throws SQLException {
		return qm.toArrayMap(allNames, Map.class, String.class);
	}
	*/

	@Override
	public Map<String, String> getAllNameMap() throws SQLException {
		return qm.toMap(allNames, String.class, String.class);
	}

	@Override
	public Map<Long, FieldBoss> getMapLongPerson() throws SQLException {
		return qm.toMap(selectMapLongPerson, Long.class, FieldBoss.class);
	}

	@Override
	public Map<Long, Long> getMapLongLong() throws SQLException {
		return qm.toMap(selectLongLong, Long.class, Long.class);
	}

	/*
	@Override
	public Long[] getLongObjectArray() throws SQLException {
		return qm.toObject(selectLongArray, Long[].class);
	}

	@Override
	public long[] getLongPrimitiveArray() throws SQLException {
		return qm.toObject(selectLongArray, long[].class);
	}
	*/

	@Override
	public List<Map<String, String>> getBobTomMap() throws SQLException {
		return qm.toListMap(bobTomMap, String.class);
	}

	@Override
	public List<CaseInsensitiveHashMap<String, String>> getBobTomMapCaseInsensitive() throws SQLException {
		return qm.toType(bobTomMap, new TypeReference<List<CaseInsensitiveHashMap<String, String>>>() {});
	}

	@Override
	public List<FieldPerson> getThreePeople(final long personNo1, final long personNo2, final long personNo3) throws SQLException {
		return  qm.toList(selectThreePeople,
				FieldPerson.class, personNo1, personNo2, personNo3);
	}

	@Override
	public List<FieldPerson> getThreePeopleType(final long personNo1, final long personNo2, final long personNo3) throws SQLException {
		return  qm.toType(selectThreePeople,
				new TypeReference<List<FieldPerson>>() {}, personNo1, personNo2, personNo3);
	}

	@Override
	public ResultSetIterable<FieldPerson> getThreePeopleResultSetIterable(final long personNo1, final long personNo2, final long personNo3) throws SQLException {
		return  qm.toResultSetIterable(selectThreePeople,
				FieldPerson.class, personNo1, personNo2, personNo3);
	}

	//IFJAVA 8_START

	@Override
	public java.util.stream.Stream<FieldPerson> getThreePeopleStream(final long personNo1, final long personNo2, final long personNo3) throws SQLException {
		return  qm.toStream(selectThreePeople,
				FieldPerson.class, personNo1, personNo2, personNo3);
	}

	//IFJAVA 8_END
}
