package com.moparisthebest.jdbc;

import com.mchange.v2.c3p0.DataSources;
import com.moparisthebest.jdbc.codegen.JdbcMapperFactory;
import com.moparisthebest.jdbc.codegen.QmDao;
import com.moparisthebest.jdbc.codegen.QueryMapperQmDao;
import com.moparisthebest.jdbc.codegen.QueryMapperTypeQmDao;
import com.moparisthebest.jdbc.dto.*;
import com.moparisthebest.jdbc.util.ResultSetIterable;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
//IFJAVA8_START
import java.time.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//IFJAVA8_END

import static com.moparisthebest.jdbc.OptimalInList.*;
import static com.moparisthebest.jdbc.TryClose.tryClose;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by mopar on 6/10/14.
 */
@RunWith(Parameterized.class)
public class QueryMapperTest {

	private static final long birthDateMillis = 1000; // this used to be 0 but mysql TIMESTAMP can only represent '1970-01-01 00:00:01' not '1970-01-01 00:00:00', nice! o_O
	public static final Person fieldPerson1 = new FieldPerson(1, new Date(birthDateMillis), "First", "Person");
	public static final Boss fieldBoss1 = new FieldBoss(2, new Date(birthDateMillis), "Second", "Person", "Finance", "Second");
	public static final Boss fieldBoss2 = new FieldBoss(3, new Date(birthDateMillis), "Third", "Person", "Finance", null);
	public static final Boss fieldBoss3 = new FieldBoss(4, new Date(birthDateMillis), null, "Person", "Finance", "Fourth");
	public static final Person fieldPerson2 = new FieldPerson(5, new Date(birthDateMillis), "Second", "Person");
	public static final Person fieldPerson3 = new FieldPerson(6, new Date(birthDateMillis), "Third", "Person");

	public static final Person fieldPerson1NullName = new FieldPerson(1, new Date(birthDateMillis), null, null);

	public static final Person[] people = new Person[]{fieldPerson1, fieldPerson2, fieldPerson3};
	public static final Boss[] bosses = new Boss[]{fieldBoss1, fieldBoss2, fieldBoss3};

	public static final Val[] vals = new Val[]{
			new Val(1, 1969, "1969"),
			new Val(2, 0, "America/New_York"),
			new Val(3, -5, "-5"),
			new Val(4, 4, null),
	};

	public static final Person setPerson1 = new SetPerson(fieldPerson1);
	public static final Boss setBoss1 = new SetBoss(fieldBoss1);
	public static final Boss setBoss2 = new SetBoss(fieldBoss2);
	public static final Boss setBoss3 = new SetBoss(fieldBoss3);

	public static final Person reverseFieldPerson1 = new ReverseFieldPerson(fieldPerson1);
	public static final Boss reverseFieldBoss1 = new ReverseFieldBoss(fieldBoss1);
	public static final Boss reverseFieldBoss2 = new ReverseFieldBoss(fieldBoss2);
	public static final Boss reverseFieldBoss3 = new ReverseFieldBoss(fieldBoss3);

	public static final Person reverseSetPerson1 = new ReverseSetPerson(fieldPerson1);
	public static final Boss reverseSetBoss1 = new ReverseSetBoss(fieldBoss1);
	public static final Boss reverseSetBoss2 = new ReverseSetBoss(fieldBoss2);
	public static final Boss reverseSetBoss3 = new ReverseSetBoss(fieldBoss3);

	public static final Collection<String> jdbcUrls;
	public static final Map<String, DataSource> dataSources = new HashMap<String, DataSource>();

	public static final Class<?> mssqlConnection = classForName("com.microsoft.sqlserver.jdbc.ISQLServerConnection");

	static {
		final Collection<String> jUrls = new ArrayList<String>();
		final String jdbcUrl = System.getProperty("jdbcUrl", "all");
		if(jdbcUrl.equals("all") || jdbcUrl.equals("bind")) {
			jUrls.add("jdbc:hsqldb:mem:testDB");
			jUrls.add("jdbc:derby:memory:testDB;create=true");
			jUrls.add("jdbc:h2:mem:testDB");
			jUrls.add("jdbc:sqlite::memory:");
		} else if(jdbcUrl.equals("hsql") || jdbcUrl.equals("hsqldb") || jdbcUrl.equals("unnest")) {
			jUrls.add("jdbc:hsqldb:mem:testDB");
		} else if(jdbcUrl.equals("derby")) {
			jUrls.add("jdbc:derby:memory:testDB;create=true");
		} else if(jdbcUrl.equals("h2") || jdbcUrl.equals("any")) {
			jUrls.add("jdbc:h2:mem:testDB");
		} else if(jdbcUrl.equals("sqlite")) {
			jUrls.add("jdbc:sqlite::memory:");
		} else {
			jUrls.add(jdbcUrl);
		}
		for(int x = 1; ; ++x) {
			final String extrajdbcUrl = System.getProperty("jdbcUrl" + x);
			if(extrajdbcUrl != null)
				jUrls.add(extrajdbcUrl);
			else
				break;
		}
		jdbcUrls = Collections.unmodifiableCollection(jUrls);
		/*IFJAVA6_START
		// this seems to only be needed for java <8
		for(final String driverClass : new String[]{"org.hsqldb.jdbc.JDBCDriver", "org.h2.Driver"})
			try {
				Class.forName(driverClass);
			} catch(Exception e) {
				// ignore, any real errors will be caught during test running
			}
		IFJAVA6_END*/
		for(final String jUrl : jdbcUrls) {
		    // oracle randomly fails without using a datasource...
            // https://dba.stackexchange.com/questions/110819/oracle-intermittently-throws-ora-12516-tnslistener-could-not-find-available-h
		    if(jUrl.startsWith("jdbc:oracle"))
                try {
                    dataSources.put(jUrl, DataSources.pooledDataSource(DataSources.unpooledDataSource(jUrl)));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
		}
	}

	public static void insertPerson(final QueryMapper qm, final Person person) throws SQLException {
		qm.executeUpdate("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES (?, ?, ?, ?)", person.getPersonNo(), person.getBirthDate(), person.getLastName(), person.getFirstName());
	}

	public static Connection getConnection() throws SQLException {
		return getConnection(jdbcUrls.iterator().next());
	}

	public static Connection getConnection(final String url) throws SQLException {
        final DataSource ds = dataSources.get(url);
		final Connection conn = ds != null ? ds.getConnection() : DriverManager.getConnection(url);
		QueryMapper qm = null;
		try {
			qm = new QueryMapper(conn);
			try {
				if (fieldPerson1.getPersonNo() == qm.toObject("SELECT person_no FROM person WHERE person_no = ?", Long.class, fieldPerson1.getPersonNo()))
					return conn;
			} catch(Exception e) {
				// ignore, means the database hasn't been set up yet
			}
			if(isWrapperFor(conn, oracleConnection)) {
			    // OracleArrayInList support requires types created
                qm.executeUpdate("create or replace TYPE \"ARRAY_NUM_TYPE\" is table of number");
                qm.executeUpdate("create or replace TYPE \"ARRAY_STR_TYPE\" is table of varchar2(32767)");
            }
			if(isWrapperFor(conn, mssqlConnection)) {
				// mssql doesn't support inserting into TIMESTAMP
				qm.executeUpdate("CREATE TABLE person (person_no NUMERIC, first_name VARCHAR(40), last_name VARCHAR(40), birth_date DATETIME)");
			} else {
				// derby doesn't support DATETIME
				qm.executeUpdate("CREATE TABLE person (person_no NUMERIC, first_name VARCHAR(40), last_name VARCHAR(40), birth_date TIMESTAMP)");
			}
			qm.executeUpdate("CREATE TABLE boss (person_no NUMERIC, department VARCHAR(40))");
			qm.executeUpdate("CREATE TABLE val (val_no NUMERIC, num_val NUMERIC, str_val VARCHAR(40))");
			for (final Person person : people)
				insertPerson(qm, person);
			for (final Boss boss : bosses) {
				qm.executeUpdate("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES (?, ?, ?, ?)", boss.getPersonNo(), boss.getBirthDate(), boss.getLastName(), boss.getFirstName() == null ? boss.getFirst_name() : boss.getFirstName());
				qm.executeUpdate("INSERT INTO boss (person_no, department) VALUES (?, ?)", boss.getPersonNo(), boss.getDepartment());
			}
			for (final Val val : vals)
				qm.executeUpdate("INSERT INTO val (val_no, num_val, str_val) VALUES (?, ?, ?)", val.valNo, val.numVal, val.strVal);
		} finally {
			tryClose(qm);
		}
		return conn;
	}

	protected Connection conn;
	protected QmDao qm;
	protected final String jdbcUrl;
	protected final int qmDaoType;
	protected final ResultSetMapper rsm;

	public QueryMapperTest(final String jdbcUrl, final int qmDaoType, final ResultSetMapper rsm) {
		this.jdbcUrl = jdbcUrl;
        this.qmDaoType = qmDaoType;
		this.rsm = rsm;
	}

	@Before
	public void open() throws SQLException {
		this.conn = getConnection(jdbcUrl);
	    switch (qmDaoType) {
            case 0:
                this.qm = new QueryMapperQmDao(conn, rsm);
                return;
            case 1:
                this.qm = new QueryMapperTypeQmDao(conn, rsm);
                return;
            case 2:
                this.qm = JdbcMapperFactory.create(QmDao.class, conn);
                return;
        }
		throw new RuntimeException("unknown qmDaoType");
	}

	@After
	public void close() {
		tryClose(qm);
		tryClose(conn);
	}

	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> getParameters()
	{
		final Collection<Object[]> params = new ArrayList<Object[]>();
		for(final String jdbcUrl : jdbcUrls)
			params.addAll(Arrays.asList(new Object[][] {
					{ jdbcUrl, 0, new ResultSetMapper() },
					{ jdbcUrl, 0, new CachingResultSetMapper() },
					{ jdbcUrl, 0, new CaseInsensitiveMapResultSetMapper() },
					{ jdbcUrl, 0, new CompilingResultSetMapper(new CompilingRowToObjectMapper.Cache(true)) },

					{ jdbcUrl, 1, new ResultSetMapper() },
					{ jdbcUrl, 1, new CachingResultSetMapper() },
					{ jdbcUrl, 1, new CaseInsensitiveMapResultSetMapper() },
					{ jdbcUrl, 1, new CompilingResultSetMapper(new CompilingRowToObjectMapper.Cache(true)) },

					{ jdbcUrl, 2, null /* means QmDao.class is used */ },
			}));
		return params;
	}

	// fields

	@Test
	public void testFieldRegularPerson() throws Throwable {
		final Person expected = fieldPerson1;
		Assert.assertEquals(expected, qm.getFieldRegularPerson(expected.getPersonNo()));
	}

	@Test
	public void testBuilderPerson() throws Throwable {
		final Person expected = new BuilderPerson(fieldPerson1);
		Assert.assertEquals(expected, qm.getBuilderPerson(expected.getPersonNo()));
	}

	@Test
	public void testFieldRegularAndUnderscore() throws Throwable {
		final Person expected = fieldBoss1;
		Assert.assertEquals(expected, qm.getFieldRegularAndUnderscore(expected.getPersonNo()));
	}

	@Test
	public void testFieldRegularAndUnderscoreReverse() throws Throwable {
		final Person expected = fieldBoss1;
		Assert.assertEquals(expected, qm.getFieldRegularAndUnderscoreReverse(expected.getPersonNo()));
	}

	@Test
	public void testFieldRegularAndUnderscoreNoConstructor() throws Throwable {
		final Person expected = fieldBoss1;
		Assert.assertEquals(expected, qm.getFieldRegularAndUnderscoreNoConstructor(expected.getPersonNo()));
	}

	@Test
	public void testFieldRegularAndUnderscoreReverseNoConstructor() throws Throwable {
		final Person expected = fieldBoss1;
		Assert.assertEquals(expected, qm.getFieldRegularAndUnderscoreReverseNoConstructor(expected.getPersonNo()));
	}

	@Test
	public void testFieldRegular() throws Throwable {
		final Person expected = fieldBoss2;
		Assert.assertEquals(expected, qm.getFieldRegular(expected.getPersonNo()));
	}

	@Test
	public void testFieldUnderscore() throws Throwable {
		final Person expected = fieldBoss3;
		Assert.assertEquals(expected, qm.getFieldUnderscore(expected.getPersonNo()));
	}

	// sets

	@Test
	public void testSetRegularPerson() throws Throwable {
		final Person expected = setPerson1;
		Assert.assertEquals(expected, qm.getSetRegularPerson(expected.getPersonNo()));
	}

	@Test
	public void testSetRegularPersonNoConstructor() throws Throwable {
		final Person expected = setPerson1;
		Assert.assertEquals(expected, qm.getSetRegularPersonNoConstructor(expected.getPersonNo()));
	}

	@Test
	public void testSetRegularAndUnderscore() throws Throwable {
		final Person expected = setBoss1;
		Assert.assertEquals(expected, qm.getSetRegularAndUnderscore(expected.getPersonNo()));
	}

	@Test
	public void testSetRegularAndUnderscoreReverse() throws Throwable {
		final Person expected = setBoss1;
		Assert.assertEquals(expected, qm.getSetRegularAndUnderscoreReverse(expected.getPersonNo()));
	}

	@Test
	public void testSetRegular() throws Throwable {
		final Person expected = setBoss2;
		Assert.assertEquals(expected, qm.getSetRegular(expected.getPersonNo()));
	}

	@Test
	public void testSetUnderscore() throws Throwable {
		final Person expected = setBoss3;
		Assert.assertEquals(expected, qm.getSetUnderscore(expected.getPersonNo()));
	}

	// reverse fields

	@Test
	public void testReverseFieldRegularPerson() throws Throwable {
		final Person expected = reverseFieldPerson1;
		Assert.assertEquals(expected, qm.getReverseFieldRegularPerson(expected.getPersonNo()));
	}

	@Test
	public void testReverseFieldRegularAndUnderscore() throws Throwable {
		final Person expected = reverseFieldBoss1;
		Assert.assertEquals(expected, qm.getReverseFieldRegularAndUnderscore(expected.getPersonNo()));
	}

	@Test
	public void testReverseFieldRegularAndUnderscoreReverse() throws Throwable {
		final Person expected = reverseFieldBoss1;
		Assert.assertEquals(expected, qm.getReverseFieldRegularAndUnderscoreReverse(expected.getPersonNo()));
	}

	@Test
	public void testReverseFieldRegular() throws Throwable {
		final Person expected = reverseFieldBoss3;
		Assert.assertEquals(expected, qm.getReverseFieldRegular(expected.getPersonNo()));
	}

	@Test
	public void testReverseFieldUnderscore() throws Throwable {
		final Person expected = reverseFieldBoss2;
		Assert.assertEquals(expected, qm.getReverseFieldUnderscore(expected.getPersonNo()));
	}

	// reverse sets

	@Test
	public void testReverseSetRegularPerson() throws Throwable {
		final Person expected = reverseSetPerson1;
		Assert.assertEquals(expected, qm.getReverseSetRegularPerson(expected.getPersonNo()));
	}

	@Test
	public void testReverseSetRegularAndUnderscore() throws Throwable {
		final Person expected = reverseSetBoss1;
		Assert.assertEquals(expected, qm.getReverseSetRegularAndUnderscore(expected.getPersonNo()));
	}

	@Test
	public void testReverseSetRegularAndUnderscoreReverse() throws Throwable {
		final Person expected = reverseSetBoss1;
		Assert.assertEquals(expected, qm.getReverseSetRegularAndUnderscoreReverse(expected.getPersonNo()));
	}

	@Test
	public void testReverseSetRegular() throws Throwable {
		final Person expected = reverseSetBoss3;
		Assert.assertEquals(expected, qm.getReverseSetRegular(expected.getPersonNo()));
	}

	@Test
	public void testReverseSetUnderscore() throws Throwable {
		final Person expected = reverseSetBoss2;
		Assert.assertEquals(expected, qm.getReverseSetUnderscore(expected.getPersonNo()));
	}

	@Test
	public void testSelectListMap() throws Throwable {
		final List<Map<String, String>> arrayMap = getListMap();
		Assert.assertEquals(arrayMap, qm.getAllNames());
	}

	@Test
	public void testSelectArrayMap() throws Throwable {
		final List<Map<String, String>> arrayMap = getListMap();
		assertArrayEquals(arrayMap.toArray(new Map[arrayMap.size()]), qm.getAllNamesArray());
	}

	@Test
	public void testSelectMapString() throws Throwable {
		final Map<String, String> map = new HashMap<String, String>();
		for (final Person person : new Person[]{fieldPerson1, fieldBoss1, fieldBoss2})
			map.put(person.getFirstName(), person.getLastName());
		Assert.assertEquals(map, qm.getAllNameMap());
	}

	@Test
	public void testSelectMapLongPerson() throws Throwable {
		final Map<Long, Person> map = new HashMap<Long, Person>();
		for (final Person person : new Person[]{
				qm.getFieldRegular(2),
				qm.getFieldRegular(3),
				qm.getFieldRegular(4),
				})
			map.put(person.getPersonNo(), person);
		Assert.assertEquals(map, qm.getMapLongPerson());
	}

	@Test
	public void testSelectMapLong() throws Throwable {
		final Map<Long, Long> map = new HashMap<Long, Long>();
		for (final Person person : new Person[]{fieldPerson1, fieldBoss1, fieldBoss2})
			map.put(person.getPersonNo(), person.getPersonNo());
		Assert.assertEquals(map, qm.getMapLongLong());
	}

	@Test
	public void testSelectLongObject() throws Throwable {
		final Long expected = fieldPerson1.getPersonNo();
		Assert.assertEquals(expected, qm.getPersonNo(expected));
	}

	@Test
	public void testSelectLongPrimitive() throws Throwable {
		final long expected = fieldPerson1.getPersonNo();
		Assert.assertEquals(expected, qm.getPersonNoPrimitive(expected));
	}

	@Test
	public void testSelectIntPrimitive() throws Throwable {
		final int expected = (int)fieldPerson1.getPersonNo();
		Assert.assertEquals(expected, qm.getPersonNoPrimitiveInt(expected));
	}

	@Test
	public void testSelectLongObjectArray() throws Throwable {
		final Long[] expected = {fieldPerson1.getPersonNo()};
		assertArrayEquals(expected, qm.getPersonNoObjectArray(expected[0]));
	}

	@Test
	public void testSelectObjectArray() throws Throwable {
		final Long[] arr = {1L, 2L, 3L};
		assertArrayEquals(arr, qm.getLongObjectArray());
	}

	@Test
	public void testSelectPrimitiveArray() throws Throwable {
		final long[] arr = {1L, 2L, 3L};
		assertArrayEquals(arr, qm.getLongPrimitiveArray());
	}

	@Test(expected = com.moparisthebest.jdbc.MapperException.class)
	public void testNoDefaultConstructorFails() throws Throwable {
		if(qm instanceof QueryMapperQmDao)
			((QueryMapperQmDao)qm).getQm().toObject("SELECT 1, 2, 3 FROM person WHERE person_no = ?", Long.class, fieldPerson1.getPersonNo());
		else
			throw new MapperException("JdbcMapper wouldn't compile so skipping this...");
	}

	@Test
	public void testGetGeneratedKeysSingleLong() throws SQLException {
		QueryMapper qm = null;
		try {
			qm = new QueryMapper(this.qm.getConnection());
			// auto increment stuff for getGeneratedKeys, how obnoxious are these subtle differences...
			if (isWrapperFor(qm.getConnection(), classForName("org.sqlite.SQLiteConnection"))) {
				qm.executeUpdate("CREATE TABLE a_thaoeu_table(\n" +
						"   a_thaoeu_table_no INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
						"   a_thaoeu_table_val NUMERIC\n" +
						")");
			} else if (isWrapperFor(qm.getConnection(), classForName("org.mariadb.jdbc.MariaDbPooledConnection"))) {
				qm.executeUpdate("CREATE TABLE a_thaoeu_table(\n" +
						"   a_thaoeu_table_no INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
						"   a_thaoeu_table_val NUMERIC\n" +
						")");
			} else if (isWrapperFor(qm.getConnection(), postgreConnection)) {
				qm.executeUpdate("CREATE TABLE a_thaoeu_table(\n" +
						"   a_thaoeu_table_no SERIAL PRIMARY KEY,\n" +
						"   a_thaoeu_table_val NUMERIC\n" +
						")");
			} else if (isWrapperFor(qm.getConnection(), mssqlConnection)) {
				qm.executeUpdate("CREATE TABLE a_thaoeu_table(\n" +
						"   a_thaoeu_table_no INTEGER IDENTITY(1,1) PRIMARY KEY,\n" +
						"   a_thaoeu_table_val NUMERIC\n" +
						")");
			} else if (isWrapperFor(qm.getConnection(), oracleConnection)) {
				qm.executeUpdate("CREATE TABLE a_thaoeu_table(\n" +
						"   a_thaoeu_table_no NUMBER PRIMARY KEY,\n" +
						"   a_thaoeu_table_val NUMERIC\n" +
						")");
				qm.executeUpdate("CREATE SEQUENCE a_thaoeu_table_seq\n" +
						"MINVALUE 1\n" +
						"START WITH 1\n" +
						"INCREMENT BY 1\n" +
						"CACHE 10");

				// if this is JdbcMapper, not QueryMapper, this will fail unless compiled for ORACLE, so just exit early unless that's the case
				if(!(this.qm instanceof QueryMapperQmDao) && !"ORACLE".equals(System.getProperty("jdbcMapper.databaseType")))
					return;

				// so different we have to do test here
				for (long expected = 1; expected < 5; ++expected) {
					final long autoTableNo = this.qm.insertGetGeneratedKeyOracle(expected * 2);
					assertEquals(expected, autoTableNo);
				}
				return;
			} else {
				return; // can't do test...
			}

			for (long expected = 1; expected < 5; ++expected) {
				final long autoTableNo = this.qm.insertGetGeneratedKey(expected * 2);
				assertEquals(expected, autoTableNo);
			}
		} finally {
			if(qm != null)
				try {
					qm.executeUpdate("DROP TABLE a_thaoeu_table");
					qm.executeUpdate("DROP SEQUENCE  a_thaoeu_table_seq");
				} catch(Exception e) {
					// ignore
				}
			tryClose(qm);
		}
	}

	private List<Map<String, String>> getListMap() {
		final List<Map<String, String>> arrayMap = new ArrayList<Map<String, String>>();
		for (final Person person : new Person[]{fieldPerson1, fieldBoss1, fieldBoss2}) {
			final Map<String, String> map = new HashMap<String, String>();
			map.put("last_name", person.getLastName());
			map.put("first_name", person.getFirstName());
			arrayMap.add(map);
		}
		return arrayMap;
	}

	@Test
	public void testCaseInsensitiveMap() throws Throwable {
		final Map<String, String> map = qm.getBobTomMap().get(0);
		if (rsm instanceof CaseInsensitiveMapResultSetMapper) {
			assertEquals("bob", map.get("bob"));
			assertEquals("bob", map.get("Bob"));
			assertEquals("bob", map.get("BoB"));
			assertEquals("bob", map.get("BOb"));
			assertEquals("bob", map.get("BOB"));
			assertEquals("tom", map.get("tom"));
			assertEquals("tom", map.get("Tom"));
			assertEquals("tom", map.get("ToM"));
			assertEquals("tom", map.get("TOm"));
			assertEquals("tom", map.get("TOM"));
		} else {
			assertEquals("bob", map.get("bob"));
			assertNull(map.get("Bob"));
			assertNull(map.get("BoB"));
			assertNull(map.get("BOb"));
			assertNull(map.get("BOB"));
			assertEquals("tom", map.get("tom"));
			assertNull(map.get("Tom"));
			assertNull(map.get("ToM"));
			assertNull(map.get("TOm"));
			assertNull(map.get("TOM"));
		}
	}

	@Test
	public void testCaseInsensitiveMapJdbcMapper() throws Throwable {
		final Map<String, String> map = qm.getBobTomMapCaseInsensitive().get(0);
		assertEquals("bob", map.get("bob"));
		assertEquals("bob", map.get("Bob"));
		assertEquals("bob", map.get("BoB"));
		assertEquals("bob", map.get("BOb"));
		assertEquals("bob", map.get("BOB"));
		assertEquals("tom", map.get("tom"));
		assertEquals("tom", map.get("Tom"));
		assertEquals("tom", map.get("ToM"));
		assertEquals("tom", map.get("TOm"));
		assertEquals("tom", map.get("TOM"));
	}

	@Test
	public void testList() throws SQLException {
		final List<FieldPerson> fromDb = qm.getThreePeople(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo());
		assertArrayEquals(people, fromDb.toArray());
	}

	@Test
	public void testResultSetIterable() throws SQLException {
		final ResultSetIterable<FieldPerson> rsi = qm.getThreePeopleResultSetIterable(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo());
		final List<FieldPerson> fromDb = new ArrayList<FieldPerson>();
		for(final FieldPerson fieldPerson : rsi)
			fromDb.add(fieldPerson);
		rsi.close();
		assertArrayEquals(people, fromDb.toArray());
	}

	//IFJAVA8_START

	@Test
	public void testStream() throws SQLException {
		final List<FieldPerson> fromDb;
		try(Stream<FieldPerson> rsi = qm.getThreePeopleStream(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo())) {
			fromDb = rsi.collect(Collectors.toList());
		}
		assertArrayEquals(people, fromDb.toArray());
	}

	//IFJAVA8_END


	@Test
	public void testEnumPerson() throws SQLException {
		assertEquals(new EnumPerson(FirstName.First), qm.getEnumPerson(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testEnumPersonConstructor() throws SQLException {
		assertEquals(new EnumPerson(FirstName.First), qm.getEnumPersonConstructor(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testEnum() throws SQLException {
		assertEquals(FirstName.First, qm.getFirstName(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testEnumPersonNull() throws SQLException {
		assertEquals(new EnumPerson(null), qm.getEnumPersonNull());
	}

	@Test
	public void testEnumNull() throws SQLException {
		assertNull(qm.getFirstNameNull());
	}

	@Test
	public void testCaseInsensitiveMethods() throws SQLException {
		final CaseSensitivePerson expected = new CaseSensitivePerson();
		expected.setmPersonFirstName(fieldPerson1.getFirstName());
		assertEquals(expected, qm.getCaseSensitivePerson(fieldPerson1.getPersonNo()));
	}

	//IFJAVA8_START

	@Test
	public void testInstant() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant(),
				qm.getBirthdateInstant(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testLocalDateTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
				qm.getBirthdateLocalDateTime(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testLocalDate() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
				qm.getBirthdateLocalDate(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testLocalTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime(),
				qm.getBirthdateLocalTime(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testZonedDateTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()),
				qm.getBirthdateZonedDateTime(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testOffsetDateTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime(),
				qm.getBirthdateOffsetDateTime(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testZonedOffsetTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime().toOffsetTime(),
				qm.getBirthdateOffsetTime(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testYearInt() throws SQLException {
		final Val val = vals[0];
		assertEquals(Year.of((int)val.numVal),
				qm.getYearInt(val.valNo)
		);
	}

	@Test
	public void testYearString() throws SQLException {
		final Val val = vals[0];
		assertEquals(Year.parse(val.strVal),
				qm.getYearString(val.valNo)
		);
	}

	@Test
	public void testZoneId() throws SQLException {
		final Val val = vals[1];
		assertEquals(ZoneId.of(val.strVal),
				qm.getZoneId(val.valNo)
		);
	}

	@Test
	public void testZoneOffsetInt() throws SQLException {
		final Val val = vals[2];
		assertEquals(ZoneOffset.ofHours((int)val.numVal),
				qm.getZoneOffsetInt(val.valNo)
		);
	}

	@Test
	public void testZoneOffsetStr() throws SQLException {
		final Val val = vals[2];
		assertEquals(ZoneOffset.of(val.strVal),
				qm.getZoneOffsetString(val.valNo)
		);
	}

    @Test
    public void testListQueryMapperStream() throws SQLException {
        final List<FieldPerson> fromDb = qm.getFieldPeopleStream(Stream.of(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo()));
        assertArrayEquals(people, fromDb.toArray());
    }

	//IFJAVA8_END

    @Test
    public void testListQueryMapperList() throws SQLException {
        final List<FieldPerson> fromDb = qm.getFieldPeople(Arrays.asList(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo()));
        assertArrayEquals(people, fromDb.toArray());
    }

    @Test
    public void testListQueryMapperListMultiple() throws SQLException {
        final List<FieldPerson> fromDb = qm.getFieldPeopleByName(
                Arrays.asList(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo()),
                Arrays.asList(people[0].getFirstName(), people[1].getFirstName(), people[2].getFirstName()));
        assertArrayEquals(people, fromDb.toArray());
    }

	@Test
	public void testListQueryMapperListNotIn() throws SQLException {
		final List<FieldPerson> fromDb = qm.getFieldPeopleNotIn(Arrays.asList(bosses[0].getPersonNo(), bosses[1].getPersonNo(), bosses[2].getPersonNo()));
		assertArrayEquals(people, fromDb.toArray());
	}
}
