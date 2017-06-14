package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.dto.EnumPerson;
import com.moparisthebest.jdbc.dto.FieldPerson;
import com.moparisthebest.jdbc.dto.FirstName;
import com.moparisthebest.jdbc.util.ResultSetIterable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//IFJAVA8_START
import java.util.stream.Collectors;
import java.util.stream.Stream;
//IFJAVA8_END

import static com.moparisthebest.jdbc.QueryMapperTest.fieldPerson1;
import static com.moparisthebest.jdbc.QueryMapperTest.getConnection;
import static com.moparisthebest.jdbc.QueryMapperTest.people;
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
}
