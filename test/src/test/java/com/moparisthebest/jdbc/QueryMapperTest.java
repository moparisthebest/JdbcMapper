package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.codegen.JdbcMapperFactory;
import com.moparisthebest.jdbc.codegen.QmDao;
import com.moparisthebest.jdbc.codegen.QueryMapperQmDao;
import com.moparisthebest.jdbc.dto.*;
import com.moparisthebest.jdbc.util.ResultSetIterable;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
//IFJAVA8_START
import java.util.stream.Collectors;
import java.util.stream.Stream;
//IFJAVA8_END

import static com.moparisthebest.jdbc.TryClose.tryClose;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by mopar on 6/10/14.
 */
@RunWith(Parameterized.class)
public class QueryMapperTest {

	private static Connection conn;

	public static final Person fieldPerson1 = new FieldPerson(1, new Date(0), "First", "Person");
	public static final Boss fieldBoss1 = new FieldBoss(2, new Date(0), "Second", "Person", "Finance", "Second");
	public static final Boss fieldBoss2 = new FieldBoss(3, new Date(0), "Third", "Person", "Finance", null);
	public static final Boss fieldBoss3 = new FieldBoss(4, new Date(0), null, "Person", "Finance", "Fourth");
	public static final Person fieldPerson2 = new FieldPerson(5, new Date(0), "Second", "Person");
	public static final Person fieldPerson3 = new FieldPerson(6, new Date(0), "Third", "Person");

	public static final Person fieldPerson1NullName = new FieldPerson(1, new Date(0), null, null);

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



	static {
		// load db once
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			Connection conn = null;
			QueryMapper qm = null;
			try {
				conn = getConnection();
				qm = new QueryMapper(conn);
				qm.executeUpdate("CREATE TABLE person (person_no NUMERIC, first_name VARCHAR(40), last_name VARCHAR(40), birth_date TIMESTAMP)");
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
				tryClose(conn);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static void insertPerson(final QueryMapper qm, final Person person) throws SQLException {
		qm.executeUpdate("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES (?, ?, ?, ?)", person.getPersonNo(), person.getBirthDate(), person.getLastName(), person.getFirstName());
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:derby:memory:derbyDB;create=true");
	}

	@BeforeClass
	public static void setUp() throws Throwable {
		conn = getConnection();
	}

	@AfterClass
	public static void tearDown() throws Throwable {
		tryClose(conn);
	}

	protected QmDao qm;
	protected final ResultSetMapper rsm;

	public QueryMapperTest(final ResultSetMapper rsm) {
		this.rsm = rsm;
	}

	@Before
	public void open() {
		qm = this.rsm == null ? JdbcMapperFactory.create(QmDao.class, conn) : new QueryMapperQmDao(conn, rsm);
	}

	@After
	public void close() {
		tryClose(qm);
	}

	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> getParameters()
	{
		return Arrays.asList(new Object[][] {
				{ new ResultSetMapper() },
				{ new CachingResultSetMapper() },
				{ new CaseInsensitiveMapResultSetMapper() },
				{ new CompilingResultSetMapper(new CompilingRowToObjectMapper.Cache(true)) },
				{ null /* means QmDao.class is used */ },
		});
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
		final Person expected = fieldBoss1; // todo: these call constructor, write another test to call setters
		Assert.assertEquals(expected, qm.getFieldRegularAndUnderscoreReverse(expected.getPersonNo()));
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

	/*
	// todo: fix jdbcmapper for this
	@Test
	public void testSelectArrayMap() throws Throwable {
		final List<Map<String, String>> arrayMap = getListMap();
		assertArrayEquals(arrayMap.toArray(new Map[arrayMap.size()]), qm.getAllNamesArray());
	}
	*/

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
/*
	// todo: fix these
	@Test
	public void testSelectObjectArray() throws Throwable {
		final Long[] arr = {1L, 2L, 3L};
		assertArrayEquals(arr, qm.getLongObjectArray());
	}

	@Test
	public void testSelectPrimitiveArray() throws Throwable {
		final long[] arr = {1L, 2L, 3L};
		assertArrayEquals(arr, qm.toObject("SELECT 1, 2, 3 FROM person WHERE person_no = ?", long[].class, fieldPerson1.getPersonNo()));
	}
*/
	@Test(expected = com.moparisthebest.jdbc.MapperException.class)
	public void testNoDefaultConstructorFails() throws Throwable {
		if(qm instanceof QueryMapperQmDao)
			((QueryMapperQmDao)qm).getQm().toObject("SELECT 1, 2, 3 FROM person WHERE person_no = ?", Long.class, fieldPerson1.getPersonNo());
		else
			throw new MapperException("JdbcMapper wouldn't compile so skipping this...");
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
		if(qm instanceof QueryMapperQmDao)
			return; // skip todo: java.lang.ClassCastException: sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl cannot be cast to java.lang.Class
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
	public void testListType() throws SQLException {
		final List<FieldPerson> fromDb = qm.getThreePeopleType(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo());
		assertArrayEquals(people, fromDb.toArray());
	}
/*
	// todo: port this one
	@Test
	public void testListQueryMapperList() throws SQLException {
		final ListQueryMapper lqm = new ListQueryMapper(qm);
		final List<FieldPerson> fromDb = lqm.toList("SELECT * from person WHERE " + ListQueryMapper.inListReplace + " ORDER BY person_no",
				FieldPerson.class, lqm.inList("person_no", Arrays.asList(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo())));
		assertArrayEquals(people, fromDb.toArray());
		lqm.close();
	}
*/
	@Test
	public void testResultSetIterable() throws SQLException {
		final ResultSetIterable<FieldPerson> rsi = qm.getThreePeopleResultSetIterable(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo());
		final List<FieldPerson> fromDb = new ArrayList<FieldPerson>();
		for(final FieldPerson fieldPerson : rsi)
			fromDb.add(fieldPerson);
		rsi.close();
		assertArrayEquals(people, fromDb.toArray());
	}

	//IFJAVA 8_START

	@Test
	public void testStream() throws SQLException {
		final List<FieldPerson> fromDb;
		try(Stream<FieldPerson> rsi = qm.getThreePeopleStream(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo())) {
			fromDb = rsi.collect(Collectors.toList());
		}
		assertArrayEquals(people, fromDb.toArray());
	}

	//IFJAVA 8_END

/*
	@Test
	public void testEnumPerson() throws SQLException {
		assertEquals(new EnumPerson(FirstName.First), qm.toObject("SELECT first_name, last_name FROM person WHERE person_no = ?", EnumPerson.class, fieldPerson1.getPersonNo()));
	}

	@Test
	public void testEnumPersonConstructor() throws SQLException {
		assertEquals(new EnumPerson(FirstName.First), qm.toObject("SELECT first_name FROM person WHERE person_no = ?", EnumPerson.class, fieldPerson1.getPersonNo()));
	}

	@Test
	public void testEnum() throws SQLException {
		assertEquals(FirstName.First, qm.toObject("SELECT first_name FROM person WHERE person_no = ?", FirstName.class, fieldPerson1.getPersonNo()));
	}

	@Test
	public void testEnumPersonNull() throws SQLException {
		assertEquals(new EnumPerson(null), qm.toObject("SELECT str_val as first_name, str_val as last_name FROM val WHERE val_no = 4", EnumPerson.class));
	}

	@Test
	public void testEnumNull() throws SQLException {
		assertEquals(null, qm.toObject("SELECT str_val FROM val WHERE val_no = 4", FirstName.class));
	}

	@Test
	public void testCaseInsensitiveMethods() throws SQLException {
		final CaseSensitivePerson expected = new CaseSensitivePerson();
		expected.setmPersonFirstName(fieldPerson1.getFirstName());
		assertEquals(expected, qm.toObject("SELECT first_name AS M_PERSON_FIRST_NAME FROM person WHERE person_no = ?", CaseSensitivePerson.class, fieldPerson1.getPersonNo()));
	}

	//IFJAVA 8_START

	@Test
	public void testInstant() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant(),
				qm.toObject("SELECT birth_date FROM person WHERE person_no = ?", Instant.class, fieldPerson1.getPersonNo()));
	}

	@Test
	public void testLocalDateTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
				qm.toObject("SELECT birth_date FROM person WHERE person_no = ?", LocalDateTime.class, fieldPerson1.getPersonNo()));
	}

	@Test
	public void testLocalDate() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
				qm.toObject("SELECT birth_date FROM person WHERE person_no = ?", LocalDate.class, fieldPerson1.getPersonNo()));
	}

	@Test
	public void testLocalTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime(),
				qm.toObject("SELECT birth_date FROM person WHERE person_no = ?", LocalTime.class, fieldPerson1.getPersonNo()));
	}

	@Test
	public void testZonedDateTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()),
				qm.toObject("SELECT birth_date FROM person WHERE person_no = ?", ZonedDateTime.class, fieldPerson1.getPersonNo()));
	}

	@Test
	public void testOffsetDateTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime(),
				qm.toObject("SELECT birth_date FROM person WHERE person_no = ?", OffsetDateTime.class, fieldPerson1.getPersonNo()));
	}

	@Test
	public void testZonedOffsetTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime().toOffsetTime(),
				qm.toObject("SELECT birth_date FROM person WHERE person_no = ?", OffsetTime.class, fieldPerson1.getPersonNo()));
	}

	@Test
	public void testYearInt() throws SQLException {
		final Val val = vals[0];
		assertEquals(Year.of((int)val.numVal),
				qm.toObject("SELECT num_val FROM val WHERE val_no = ?", Year.class, val.valNo)
		);
	}

	@Test
	public void testYearString() throws SQLException {
		final Val val = vals[0];
		assertEquals(Year.parse(val.strVal),
				qm.toObject("SELECT str_val FROM val WHERE val_no = ?", Year.class, val.valNo)
		);
	}

	@Test
	public void testZoneId() throws SQLException {
		final Val val = vals[1];
		assertEquals(ZoneId.of(val.strVal),
				qm.toObject("SELECT str_val FROM val WHERE val_no = ?", ZoneId.class, val.valNo)
		);
	}

	@Test
	public void testZoneOffsetInt() throws SQLException {
		final Val val = vals[2];
		assertEquals(ZoneOffset.of(val.strVal),
				qm.toObject("SELECT str_val FROM val WHERE val_no = ?", ZoneOffset.class, val.valNo)
		);
	}

	@Test
	public void testZoneOffsetStr() throws SQLException {
		final Val val = vals[2];
		assertEquals(ZoneOffset.ofHours((int)val.numVal),
				qm.toObject("SELECT num_val FROM val WHERE val_no = ?", ZoneOffset.class, val.valNo)
		);
	}

	//IFJAVA 8_END
	*/
}
