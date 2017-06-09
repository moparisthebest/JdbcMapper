package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.dto.*;
import com.moparisthebest.jdbc.util.ResultSetIterable;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

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

	public static final Person[] people = new Person[]{fieldPerson1, fieldPerson2, fieldPerson3};
	public static final Boss[] bosses =new Boss[]{fieldBoss1, fieldBoss2, fieldBoss3};

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
				for (final Person person : people)
					qm.executeUpdate("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES (?, ?, ?, ?)", person.getPersonNo(), person.getBirthDate(), person.getLastName(), person.getFirstName());
				for (final Boss boss : bosses) {
					qm.executeUpdate("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES (?, ?, ?, ?)", boss.getPersonNo(), boss.getBirthDate(), boss.getLastName(), boss.getFirstName() == null ? boss.getFirst_name() : boss.getFirstName());
					qm.executeUpdate("INSERT INTO boss (person_no, department) VALUES (?, ?)", boss.getPersonNo(), boss.getDepartment());
				}
			} finally {
				tryClose(qm);
				tryClose(conn);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static Connection getConnection() throws Throwable {
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

	protected QueryMapper qm;
	protected final ResultSetMapper rsm;

	public QueryMapperTest(final ResultSetMapper rsm) {
		this.rsm = rsm;
	}

	@Before
	public void open() {
		this.qm = new QueryMapper(conn, rsm);
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
				{ new CompilingResultSetMapper() },
		});
	}

	// fields

	@Test
	public void testFieldRegularPerson() throws Throwable {
		testPerson(fieldPerson1, personRegular);
	}

	@Test
	public void testFieldRegularAndUnderscore() throws Throwable {
		testPerson(fieldBoss1, bossRegularAndUnderscore);
	}

	@Test
	public void testFieldRegularAndUnderscoreReverse() throws Throwable {
		testPerson(fieldBoss1, bossRegularAndUnderscoreReverse);
	}

	@Test
	public void testFieldRegular() throws Throwable {
		testPerson(fieldBoss2, bossRegular);
	}

	@Test
	public void testFieldUnderscore() throws Throwable {
		testPerson(fieldBoss3, bossUnderscore);
	}

	// sets

	@Test
	public void testSetRegularPerson() throws Throwable {
		testPerson(setPerson1, personRegular);
	}

	@Test
	public void testSetRegularAndUnderscore() throws Throwable {
		testPerson(setBoss1, bossRegularAndUnderscore);
	}

	@Test
	public void testSetRegularAndUnderscoreReverse() throws Throwable {
		testPerson(setBoss1, bossRegularAndUnderscoreReverse);
	}

	@Test
	public void testSetRegular() throws Throwable {
		testPerson(setBoss2, bossRegular);
	}

	@Test
	public void testSetUnderscore() throws Throwable {
		testPerson(setBoss3, bossUnderscore);
	}

	// reverse fields

	@Test
	public void testReverseFieldRegularPerson() throws Throwable {
		testPerson(reverseFieldPerson1, personRegular);
	}

	@Test
	public void testReverseFieldRegularAndUnderscore() throws Throwable {
		testPerson(reverseFieldBoss1, bossRegularAndUnderscore);
	}

	@Test
	public void testReverseFieldRegularAndUnderscoreReverse() throws Throwable {
		testPerson(reverseFieldBoss1, bossRegularAndUnderscoreReverse);
	}

	@Test
	public void testReverseFieldRegular() throws Throwable {
		testPerson(reverseFieldBoss3, bossRegular);
	}

	@Test
	public void testReverseFieldUnderscore() throws Throwable {
		testPerson(reverseFieldBoss2, bossUnderscore);
	}

	// reverse sets

	@Test
	public void testReverseSetRegularPerson() throws Throwable {
		testPerson(reverseSetPerson1, personRegular);
	}

	@Test
	public void testReverseSetRegularAndUnderscore() throws Throwable {
		testPerson(reverseSetBoss1, bossRegularAndUnderscore);
	}

	@Test
	public void testReverseSetRegularAndUnderscoreReverse() throws Throwable {
		testPerson(reverseSetBoss1, bossRegularAndUnderscoreReverse);
	}

	@Test
	public void testReverseSetRegular() throws Throwable {
		testPerson(reverseSetBoss3, bossRegular);
	}

	@Test
	public void testReverseSetUnderscore() throws Throwable {
		testPerson(reverseSetBoss2, bossUnderscore);
	}

	@Test
	public void testSelectLong() throws Throwable {
		Assert.assertEquals(new Long(1L), qm.toObject("SELECT person_no FROM person WHERE person_no = ?", Long.class, 1L));
	}

	@Test
	public void testSelectListMap() throws Throwable {
		final List<Map<String, String>> arrayMap = getListMap();
		Assert.assertEquals(arrayMap, qm.toListMap("SELECT first_name, last_name FROM person WHERE person_no < 4", arrayMap.get(0).getClass(), String.class));
	}

	@Test
	public void testSelectArrayMap() throws Throwable {
		final List<Map<String, String>> arrayMap = getListMap();
		assertArrayEquals(arrayMap.toArray(new Map[arrayMap.size()]), qm.toArrayMap("SELECT first_name, last_name FROM person WHERE person_no < 4", arrayMap.get(0).getClass(), String.class));
	}

	@Test
	public void testSelectMapString() throws Throwable {
		final Map<String, String> map = new HashMap<String, String>();
		for (final Person person : new Person[]{fieldPerson1, fieldBoss1, fieldBoss2})
			map.put(person.getFirstName(), person.getLastName());
		Assert.assertEquals(map, qm.toMap("SELECT first_name, last_name FROM person WHERE person_no < 4", String.class, String.class));
	}

	@Test
	public void testSelectMapLongPerson() throws Throwable {
		final Map<Long, Person> map = new HashMap<Long, Person>();
		for (final Person person : new Person[]{
				qm.toObject(bossRegular, FieldBoss.class, 2),
				qm.toObject(bossRegular, FieldBoss.class, 3),
				qm.toObject(bossRegular, FieldBoss.class, 4),
				})
			map.put(person.getPersonNo(), person);
		Assert.assertEquals(map, qm.toMap("SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department " +
				"FROM person p " +
				"JOIN boss b ON p.person_no = b.person_no " +
				"WHERE p.person_no in (2,3,4)", Long.class, FieldBoss.class));
	}

	@Test
	public void testSelectMapLong() throws Throwable {
		final Map<Long, Long> map = new HashMap<Long, Long>();
		for (final Person person : new Person[]{fieldPerson1, fieldBoss1, fieldBoss2})
			map.put(person.getPersonNo(), person.getPersonNo());
		Assert.assertEquals(map, qm.toMap("SELECT person_no AS first_no, person_no AS last_no FROM person WHERE person_no < 4", Long.class, Long.class));
	}

	@Test
	public void testSelectLongObject() throws Throwable {
		final Long expected = fieldPerson1.getPersonNo();
		Assert.assertEquals(expected, qm.toObject("SELECT person_no FROM person WHERE person_no = ?", Long.class, expected));
	}

	@Test
	public void testSelectLongPrimitive() throws Throwable {
		final long expected = fieldPerson1.getPersonNo();
		Assert.assertEquals((Object)expected, qm.toObject("SELECT person_no FROM person WHERE person_no = ?", long.class, expected));
	}

	@Test
	public void testSelectIntPrimitive() throws Throwable {
		final int expected = (int)fieldPerson1.getPersonNo();
		Assert.assertEquals((Object)expected, qm.toObject("SELECT person_no FROM person WHERE person_no = ?", int.class, expected));
	}

	@Test
	public void testSelectLongObjectArray() throws Throwable {
		final Long[] expected = {fieldPerson1.getPersonNo()};
		assertArrayEquals(expected, qm.toArray("SELECT person_no FROM person WHERE person_no = ?", Long.class, expected[0]));
	}

	@Test
	public void testSelectObjectArray() throws Throwable {
		final Long[] arr = {1L, 2L, 3L};
		assertArrayEquals(arr, qm.toObject("SELECT 1, 2, 3 FROM person WHERE person_no = ?", Long[].class, fieldPerson1.getPersonNo()));
	}

	@Test
	public void testSelectPrimitiveArray() throws Throwable {
		final long[] arr = {1L, 2L, 3L};
		assertArrayEquals(arr, qm.toObject("SELECT 1, 2, 3 FROM person WHERE person_no = ?", long[].class, fieldPerson1.getPersonNo()));
	}

	@Test(expected = com.moparisthebest.jdbc.MapperException.class)
	public void testNoDefaultConstructorFails() throws Throwable {
		qm.toObject("SELECT 1, 2, 3 FROM person WHERE person_no = ?", Long.class, fieldPerson1.getPersonNo());
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

	private void testPerson(final Person expected, final String query) throws Throwable {
		final Person actual = qm.toObject(query, expected.getClass(), expected.getPersonNo());
		/*
		System.out.println("expected: " + expected);
		System.out.println("actual:   " + actual);
		*/
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCaseInsensitiveMap() throws Throwable {
		final Map<String, String> map = qm.toListMap("SELECT 'bob' as bob, 'tom' as tom FROM person WHERE person_no = ?", String.class, 1).get(0);
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
	public void testList() throws SQLException {
		final List<FieldPerson> fromDb = qm.toList("SELECT * from person WHERE person_no IN (?,?,?) ORDER BY person_no",
				FieldPerson.class, people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo());
		assertArrayEquals(people, fromDb.toArray());
	}

	@Test
	public void testListQueryMapperList() throws SQLException {
		final ListQueryMapper lqm = new ListQueryMapper(qm);
		final List<FieldPerson> fromDb = lqm.toList("SELECT * from person WHERE " + ListQueryMapper.inListReplace + " ORDER BY person_no",
				FieldPerson.class, lqm.inList("person_no", Arrays.asList(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo())));
		assertArrayEquals(people, fromDb.toArray());
		lqm.close();
	}

	@Test
	public void testResultSetIterable() throws SQLException {
		final ResultSetIterable<FieldPerson> rsi = qm.toResultSetIterable("SELECT * from person WHERE person_no IN (?,?,?) ORDER BY person_no",
				FieldPerson.class, people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo());
		final List<FieldPerson> fromDb = new ArrayList<FieldPerson>();
		for(final FieldPerson fieldPerson : rsi)
			fromDb.add(fieldPerson);
		rsi.close();
		assertArrayEquals(people, fromDb.toArray());
	}
}
