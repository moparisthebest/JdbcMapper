package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.dto.EnumPerson;
import com.moparisthebest.jdbc.dto.FieldPerson;
import com.moparisthebest.jdbc.dto.FirstName;
import com.moparisthebest.jdbc.dto.Val;
import com.moparisthebest.jdbc.util.ResultSetIterable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//IFJAVA8_START
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.*;
//IFJAVA8_END

import static com.moparisthebest.jdbc.QueryMapperTest.*;
import static com.moparisthebest.jdbc.TryClose.tryClose;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by mopar on 5/24/17.
 */
public class JdbcMapperTest {

	public static PersonDAO dao;

	@BeforeClass
	public static void setUp() throws Throwable {
		dao = JdbcMapperFactory.create(PersonDAO.class, getConnection());
		//dao = new com.moparisthebest.jdbc.codegen.PersonDAOBean(getConnection());
	}

	@AfterClass
	public static void tearDown() throws Throwable {
		tryClose(dao);
	}

	@Test
	public void testName() throws Throwable {
		assertEquals(fieldPerson1.getFirstName(), dao.getFirstName(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testList() throws SQLException {
		final List<FieldPerson> fromDb = dao.getPeopleList(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo());
		assertArrayEquals(people, fromDb.toArray());
	}

	@Test
	public void testResultSetIterable() throws SQLException {
		final ResultSetIterable<FieldPerson> rsi = dao.getPeopleResultSetIterable(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo());
		final List<FieldPerson> fromDb = new ArrayList<FieldPerson>();
		for(final FieldPerson fieldPerson : rsi)
			fromDb.add(fieldPerson);
		rsi.close();
		assertArrayEquals(people, fromDb.toArray());
	}

	@Test
	public void testResultSetIterableCachedPreparedStatement() throws SQLException {
		final ResultSetIterable<FieldPerson> rsi = dao.getPeopleResultSetIterableCachedPreparedStatement(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo());
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
		try(Stream<FieldPerson> rsi = dao.getPeopleStream(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo());) {
			fromDb = rsi.collect(Collectors.toList());
		}
		assertArrayEquals(people, fromDb.toArray());
	}

	@Test
	public void testStreamCachedPreparedStatement() throws SQLException {
		final List<FieldPerson> fromDb;
		try(Stream<FieldPerson> rsi = dao.getPeopleStreamCachedPreparedStatement(people[0].getPersonNo(), people[1].getPersonNo(), people[2].getPersonNo());) {
			fromDb = rsi.collect(Collectors.toList());
		}
		assertArrayEquals(people, fromDb.toArray());
	}

	//IFJAVA8_END

	@Test
	public void testEnumPerson() throws SQLException {
		assertEquals(new EnumPerson(FirstName.First), dao.getEnumPerson(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testEnum() throws SQLException {
		assertEquals(FirstName.First, dao.getFirstNameEnum(fieldPerson1.getPersonNo()));
	}

	//IFJAVA8_START

	@Test
	public void testInstant() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant(),
				dao.getBirthDateInstant(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testLocalDateTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
				dao.getBirthDateLocalDateTime(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testLocalDate() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
				dao.getBirthDateLocalDate(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testLocalTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime(),
				dao.getBirthDateLocalTime(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testZonedDateTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()),
				dao.getBirthDateZonedDateTime(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testOffsetDateTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime(),
				dao.getBirthDateOffsetDateTime(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testZonedOffsetTime() throws SQLException {
		assertEquals(fieldPerson1.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime().toOffsetTime(),
				dao.getBirthDateZonedOffsetTime(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testYearInt() throws SQLException {
		final Val val = vals[0];
		assertEquals(Year.of((int)val.numVal),
				dao.getYearInt(val.valNo)
		);
	}

	@Test
	public void testYearString() throws SQLException {
		final Val val = vals[0];
		assertEquals(Year.parse(val.strVal),
				dao.getYearString(val.valNo)
		);
	}

	@Test
	public void testZoneId() throws SQLException {
		final Val val = vals[1];
		assertEquals(ZoneId.of(val.strVal),
				dao.getZoneId(val.valNo)
		);
	}

	@Test
	public void testZoneOffsetInt() throws SQLException {
		final Val val = vals[2];
		assertEquals(ZoneOffset.of(val.strVal),
				dao.getZoneOffsetInt(val.valNo)
		);
	}

	@Test
	public void testZoneOffsetStr() throws SQLException {
		final Val val = vals[2];
		assertEquals(ZoneOffset.ofHours((int)val.numVal),
				dao.getZoneOffsetStr(val.valNo)
		);
	}

	//IFJAVA8_END

	@Test
	public void testPerson() throws SQLException {
		assertEquals(fieldPerson1, dao.getPerson(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testSinglePersonNameArray() throws SQLException {
		assertArrayEquals(new String[]{fieldPerson1.getFirstName(), fieldPerson1.getLastName()}, dao.getSinglePersonNameArray(fieldPerson1.getPersonNo()));
	}

	@Test
	public void testSinglePersonNameMap() throws SQLException {
		final Map<String, String> expected = new HashMap<String, String>();
		expected.put("first_name", fieldPerson1.getFirstName());
		expected.put("last_name", fieldPerson1.getLastName());
		assertEquals(expected, dao.getSinglePersonNameMap(fieldPerson1.getPersonNo()));
	}
}
