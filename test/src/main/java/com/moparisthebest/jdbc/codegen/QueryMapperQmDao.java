package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.*;
import com.moparisthebest.jdbc.dto.*;
import com.moparisthebest.jdbc.util.CaseInsensitiveHashMap;
import com.moparisthebest.jdbc.util.ResultSetIterable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

//IFJAVA8_START
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.*;
//IFJAVA8_END

import static com.moparisthebest.jdbc.ListQueryMapper.inListReplace;
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

	public static final String personRegularNoConstructor = "SELECT person_no, first_name, last_name, birth_date, first_name AS dummy FROM person WHERE person_no = ?";
	public static final String bossRegularAndUnderscoreNoConstructor = "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department, p.first_name, p.first_name AS dummy " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = ?";
	public static final String bossRegularAndUnderscoreReverseNoConstructor = "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department, p.first_name AS firstName, p.first_name AS dummy " +
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

	public static final String selectBirthDate = "SELECT birth_date FROM person WHERE person_no = ?";
	public static final String selectNumVal = "SELECT num_val FROM val WHERE val_no = ?";
	public static final String selectStrVal = "SELECT str_val FROM val WHERE val_no = ?";

	private static final Collection<Class<?>> noArrayInListSupport;
	public static final Class<?> hsqlConnection, oracleConnection, mssqlConnection;

	static {
		Collection<Class<?>> no = new ArrayList<Class<?>>();
		for(final String connectionClassName : new String[]{
				"org.apache.derby.iapi.jdbc.EngineConnection"
				, "org.hsqldb.jdbc.JDBCConnection" // does not support ArrayInList but *does* support UnNestArrayInList
				, "org.sqlite.jdbc3.JDBC3Connection"
				, "org.mariadb.jdbc.MariaDbConnection"
				, "com.microsoft.sqlserver.jdbc.ISQLServerConnection"
				, "oracle.jdbc.OracleConnection" // does not support ArrayInList but *does* support OracleArrayInList
				// h2 doesn't support this with java6 either...
				/*IFJAVA6_START
				, "org.h2.jdbc.JdbcConnection"
				IFJAVA6_END*/
		})
			try {
				no.add(Class.forName(connectionClassName));
			} catch(Exception e) {
				// ignore
			}
		noArrayInListSupport = Collections.unmodifiableCollection(no);
		hsqlConnection = classForName("org.hsqldb.jdbc.JDBCConnection");
		oracleConnection = classForName("oracle.jdbc.OracleConnection");
        mssqlConnection = classForName("com.microsoft.sqlserver.jdbc.SQLServerConnection");
	}

	private static Class<?> classForName(final String className) {
		try {
			return Class.forName(className);
		} catch(Exception e) {
			return null;
		}
	}

    public static boolean isWrapperFor(final Connection conn, final Class<?> connectionClass) {
        if(connectionClass == null)
            return false;
        try {
            return conn.isWrapperFor(connectionClass);
        } catch(Exception e) {
            return false;
        }
    }

	public static boolean supportsArrayInList(final Connection conn) {
		for(final Class<?> connectionClass : noArrayInListSupport) {
			try {
				if(conn.isWrapperFor(connectionClass))
					return false;
			} catch (SQLException e) {
				// ignore... how could this happen?
			}
		}
		// assume Connections DO support this unless we KNOW otherwise
		return true;
	}

	public static InList getBestInList(final Connection conn) {
		if(isWrapperFor(conn, oracleConnection))
			return OracleArrayInList.instance();
		if(isWrapperFor(conn, hsqlConnection))
			return UnNestArrayInList.instance();
		if(supportsArrayInList(conn))
			return ArrayInList.instance();
		// works for everything
		return BindInList.instance();
	}

	protected final QueryMapper qm;
	protected final ListQueryMapper lqm;

	public QueryMapperQmDao(final Connection conn, final ResultSetMapper rsm) {
		this.qm = new QueryMapper(conn, rsm);
		this.lqm = new ListQueryMapper(qm, getBestInList(qm.getConnection()));
	}

	@Override
	public Connection getConnection() {
		return qm.getConnection();
	}

	public QueryMapper getQm() {
		return qm;
	}

	public ListQueryMapper getLqm() {
		return lqm;
	}

	@Override
	public void close() {
		tryClose(lqm);
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
	public FieldBoss getFieldRegularAndUnderscoreNoConstructor(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscoreNoConstructor, FieldBoss.class, personNo);
	}

	@Override
	public FieldBoss getFieldRegularAndUnderscoreReverseNoConstructor(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscoreReverseNoConstructor, FieldBoss.class, personNo);
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
	public SetPerson getSetRegularPersonNoConstructor(final long personNo) throws SQLException {
		return qm.toObject(personRegularNoConstructor, SetPerson.class, personNo);
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
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getAllNames() throws SQLException {
		return qm.toListMap(allNames, Map.class, String.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String>[] getAllNamesArray() throws SQLException {
		return qm.toArrayMap(allNames, Map.class, String.class);
	}

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

	@Override
	public Long[] getLongObjectArray() throws SQLException {
		return qm.toObject(selectLongArray, Long[].class);
	}

	@Override
	public long[] getLongPrimitiveArray() throws SQLException {
		return qm.toObject(selectLongArray, long[].class);
	}

	@Override
	public List<Map<String, String>> getBobTomMap() throws SQLException {
		return qm.toListMap(bobTomMap, String.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CaseInsensitiveHashMap<String, String>> getBobTomMapCaseInsensitive() throws SQLException {
		return (List<CaseInsensitiveHashMap<String, String>>)(Object)qm.toListMap(bobTomMap, CaseInsensitiveHashMap.class, String.class);
	}

	@Override
	public List<FieldPerson> getThreePeople(final long personNo1, final long personNo2, final long personNo3) throws SQLException {
		return  qm.toList(selectThreePeople,
				FieldPerson.class, personNo1, personNo2, personNo3);
	}

	@Override
	public ResultSetIterable<FieldPerson> getThreePeopleResultSetIterable(final long personNo1, final long personNo2, final long personNo3) throws SQLException {
		return  qm.toResultSetIterable(selectThreePeople,
				FieldPerson.class, personNo1, personNo2, personNo3);
	}

	//IFJAVA8_START

	@Override
	public Stream<FieldPerson> getThreePeopleStream(final long personNo1, final long personNo2, final long personNo3) throws SQLException {
		return  qm.toStream(selectThreePeople,
				FieldPerson.class, personNo1, personNo2, personNo3);
	}

	//IFJAVA8_END

	@Override
	public EnumPerson getEnumPerson(long personNo) throws SQLException {
		return qm.toObject("SELECT first_name, last_name FROM person WHERE person_no = ?", EnumPerson.class, personNo);
	}

	@Override
	public EnumPerson getEnumPersonConstructor(long personNo) throws SQLException {
		return qm.toObject("SELECT first_name FROM person WHERE person_no = ?", EnumPerson.class, personNo);
	}

	@Override
	public FirstName getFirstName(long personNo) throws SQLException {
		return qm.toObject("SELECT first_name FROM person WHERE person_no = ?", FirstName.class, personNo);
	}

    @Override
    public EnumPerson getEnumPersonNull() throws SQLException {
        return qm.toObject("SELECT str_val as first_name, str_val as last_name FROM val WHERE val_no = 4", EnumPerson.class);
    }

    @Override
    public FirstName getFirstNameNull() throws SQLException {
        return qm.toObject("SELECT str_val FROM val WHERE val_no = 4", FirstName.class);
    }

	@Override
	public CaseSensitivePerson getCaseSensitivePerson(long personNo) throws SQLException {
		return qm.toObject("SELECT first_name AS M_PERSON_FIRST_NAME FROM person WHERE person_no = ?", CaseSensitivePerson.class, personNo);
	}

	//IFJAVA8_START

	@Override
	public Instant getBirthdateInstant(long personNo) throws SQLException {
		return qm.toObject(selectBirthDate, Instant.class, personNo);
	}

	@Override
	public LocalDateTime getBirthdateLocalDateTime(long personNo) throws SQLException {
		return qm.toObject(selectBirthDate, LocalDateTime.class, personNo);
	}

	@Override
	public LocalDate getBirthdateLocalDate(long personNo) throws SQLException {
		return qm.toObject(selectBirthDate, LocalDate.class, personNo);
	}

	@Override
	public LocalTime getBirthdateLocalTime(long personNo) throws SQLException {
		return qm.toObject(selectBirthDate, LocalTime.class, personNo);
	}

	@Override
	public ZonedDateTime getBirthdateZonedDateTime(long personNo) throws SQLException {
		return qm.toObject(selectBirthDate, ZonedDateTime.class, personNo);
	}

	@Override
	public OffsetDateTime getBirthdateOffsetDateTime(long personNo) throws SQLException {
		return qm.toObject(selectBirthDate, OffsetDateTime.class, personNo);
	}

	@Override
	public OffsetTime getBirthdateOffsetTime(long personNo) throws SQLException {
		return qm.toObject(selectBirthDate, OffsetTime.class, personNo);
	}

	@Override
	public Year getYearInt(long valNo) throws SQLException {
		return qm.toObject(selectNumVal, Year.class, valNo);
	}

	@Override
	public Year getYearString(long valNo) throws SQLException {
		return qm.toObject(selectStrVal, Year.class, valNo);
	}

	@Override
	public ZoneId getZoneId(long valNo) throws SQLException {
		return qm.toObject(selectStrVal, ZoneId.class, valNo);
	}

	@Override
	public ZoneOffset getZoneOffsetInt(long valNo) throws SQLException {
		return qm.toObject(selectNumVal, ZoneOffset.class, valNo);
	}

	@Override
	public ZoneOffset getZoneOffsetString(long valNo) throws SQLException {
		return qm.toObject(selectStrVal, ZoneOffset.class, valNo);
	}

	@Override
	public List<FieldPerson> getFieldPeopleStream(final Stream<Long> personNos) throws SQLException {
		return lqm.toList("SELECT * from person WHERE " + inListReplace + " ORDER BY person_no", FieldPerson.class, lqm.inList("person_no", personNos.collect(Collectors.toList())));
	}

	//IFJAVA8_END

	@Override
	public List<FieldPerson> getFieldPeople(final List<Long> personNos) throws SQLException {
		return lqm.toList("SELECT * from person WHERE " + inListReplace + " ORDER BY person_no", FieldPerson.class, lqm.inList("person_no", personNos));
	}

	@Override
	public List<FieldPerson> getFieldPeopleByName(final List<Long> personNos, final List<String> names) throws SQLException {
		return lqm.toList("SELECT * from person WHERE " + inListReplace + " AND (" + inListReplace + " OR " + inListReplace + ") ORDER BY person_no",
				FieldPerson.class,
				lqm.inList("person_no", personNos),
				lqm.inList("first_name", names),
				lqm.inList("last_name", names)
				);
	}

	@Override
	public List<FieldPerson> getFieldPeopleNotIn(final List<Long> personNos) throws SQLException {
		return lqm.toList("SELECT * from person WHERE " + inListReplace + " ORDER BY person_no", FieldPerson.class, lqm.notInList("person_no", personNos));
	}
}
