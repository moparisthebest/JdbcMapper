package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.*;
import com.moparisthebest.jdbc.dto.Person;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static com.moparisthebest.jdbc.QueryMapperTest.fieldPerson1;
import static org.junit.Assert.assertEquals;

/**
 * Created by mopar on 7/3/17.
 */
public class PersonDAOQueryRunnerTest {

	public static final QueryRunner<PersonDAO> pqr = QueryRunner.withRetry(JdbcMapperFactory.of(PersonDAO.class, new Factory<Connection>() {
		@Override
		public Connection create() throws SQLException {
			return QueryMapperTest.getConnection();
		}
	}), 10, QueryRunner.fixedDelay(5).withJitter(5));

	@Test
	public void testPerson() throws Exception {
		//final QueryRunner<ListQueryMapper> lqr = pqr.withFactory(() -> new ListQueryMapper(QueryMapperTest::getConnection));
		assertEquals(fieldPerson1, pqr.runRetryFuture(new QueryRunner.Runner<PersonDAO, Person>() {
			@Override
			public Person run(final PersonDAO dao) throws SQLException {
				if(Math.random() < 0.5) {
					System.out.println("fake fail");
					throw new SQLException("fake 50% failure rate");
				}
				return dao.getPerson(fieldPerson1.getPersonNo());
			}
		}).get());
	}
}
