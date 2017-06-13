package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.dto.FieldPerson;
import com.moparisthebest.jdbc.util.ResultSetIterable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
