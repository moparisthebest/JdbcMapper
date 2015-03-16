package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.dto.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

import static com.moparisthebest.jdbc.TryClose.tryClose;

/**
 * Created by mopar on 6/10/14.
 */
public class QueryMapperTest {

	private static Connection conn;
	protected static QueryMapper qm;

	protected static final Person fieldPerson1 = new FieldPerson(1, new Date(0), "First", "Person");
	protected static final Boss fieldBoss1 = new FieldBoss(2, new Date(0), "Second", "Person", "Finance", "Second");
	protected static final Boss fieldBoss2 = new FieldBoss(3, new Date(0), "Third", "Person", "Finance", null);
	protected static final Boss fieldBoss3 = new FieldBoss(4, new Date(0), null, "Person", "Finance", "Fourth");

	protected static final Person setPerson1 = new SetPerson(fieldPerson1);
	protected static final Boss setBoss1 = new SetBoss(fieldBoss1);
	protected static final Boss setBoss2 = new SetBoss(fieldBoss2);
	protected static final Boss setBoss3 = new SetBoss(fieldBoss3);

	protected static final Person reverseFieldPerson1 = new ReverseFieldPerson(fieldPerson1);
	protected static final Boss reverseFieldBoss1 = new ReverseFieldBoss(fieldBoss1);
	protected static final Boss reverseFieldBoss2 = new ReverseFieldBoss(fieldBoss2);
	protected static final Boss reverseFieldBoss3 = new ReverseFieldBoss(fieldBoss3);

	protected static final Person reverseSetPerson1 = new ReverseSetPerson(fieldPerson1);
	protected static final Boss reverseSetBoss1 = new ReverseSetBoss(fieldBoss1);
	protected static final Boss reverseSetBoss2 = new ReverseSetBoss(fieldBoss2);
	protected static final Boss reverseSetBoss3 = new ReverseSetBoss(fieldBoss3);

	protected static final String personRegular = "SELECT * FROM person WHERE person_no = ?";
	protected static final String bossRegularAndUnderscore = "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department, p.first_name " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = ?";
	protected static final String bossRegularAndUnderscoreReverse = "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department, p.first_name AS firstName " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = ?";
	protected static final String bossRegular = "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = ?";
	protected static final String bossUnderscore = "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = ?";

	@BeforeClass
	public static void setUp() throws Throwable {
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
		conn = DriverManager.getConnection("jdbc:derby:memory:derbyDB;create=true");
		qm = new QueryMapper(conn);
		qm.executeUpdate("CREATE TABLE person (person_no NUMERIC, first_name VARCHAR(40), last_name VARCHAR(40), birth_date TIMESTAMP)");
		qm.executeUpdate("CREATE TABLE boss (person_no NUMERIC, department VARCHAR(40))");
		for (final Person person : new Person[]{fieldPerson1})
			qm.executeUpdate("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES (?, ?, ?, ?)", person.getPersonNo(), person.getBirthDate(), person.getLastName(), person.getFirstName());
		for (final Boss boss : new Boss[]{fieldBoss1, fieldBoss2, fieldBoss3}) {
			qm.executeUpdate("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES (?, ?, ?, ?)", boss.getPersonNo(), boss.getBirthDate(), boss.getLastName(), boss.getFirstName() == null ? boss.getFirst_name() : boss.getFirstName());
			qm.executeUpdate("INSERT INTO boss (person_no, department) VALUES (?, ?)", boss.getPersonNo(), boss.getDepartment());
		}
	}

	@AfterClass
	public static void tearDown() throws Throwable {
		tryClose(qm);
		tryClose(conn);
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
		Assert.assertEquals(arrayMap.toArray(new Map[arrayMap.size()]), qm.toArrayMap("SELECT first_name, last_name FROM person WHERE person_no < 4", arrayMap.get(0).getClass(), String.class));
	}

	@Test
	public void testSelectMapString() throws Throwable {
		final Map<String, String> map = new HashMap<String, String>();
		for (final Person person : new Person[]{fieldPerson1, fieldBoss1, fieldBoss2})
			map.put(person.getFirstName(), person.getLastName());
		Assert.assertEquals(map, qm.toMap("SELECT first_name, last_name FROM person WHERE person_no < 4", String.class, String.class));
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
	public void testSelectLongObjectArray() throws Throwable {
		final Long[] expected = {fieldPerson1.getPersonNo()};
		Assert.assertArrayEquals(expected, qm.toArray("SELECT person_no FROM person WHERE person_no = ?", Long.class, expected[0]));
	}

	@Test
	public void testSelectPrimitiveArray() throws Throwable {
		final Long[] arr = {1L, 2L, 3L};
		Assert.assertArrayEquals(arr, qm.toObject("SELECT 1, 2, 3 FROM person WHERE person_no = ?", Long[].class, fieldPerson1.getPersonNo()));
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

	private static void testPerson(final Person expected, final String query) throws Throwable {
		final Person actual = qm.toObject(query, expected.getClass(), expected.getPersonNo());
		//System.out.println("expected: " + expected);
		//System.out.println("actual:   " + actual);
		Assert.assertEquals(expected, actual);
	}
}
