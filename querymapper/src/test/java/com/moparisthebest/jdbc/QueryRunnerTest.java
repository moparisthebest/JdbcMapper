package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.codegen.JdbcMapperFactory;
import com.moparisthebest.jdbc.dto.Person;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static com.moparisthebest.jdbc.QueryMapperTest.*;

/**
 * Created by mopar on 7/1/17.
 */
public class QueryRunnerTest {
	public static final QueryRunner<QueryMapper> qr = new QueryRunner<QueryMapper>(new JdbcMapperFactory<QueryMapper>(QueryMapper.class, new Factory<Connection>() {
		@Override
		public Connection create() throws SQLException {
			return QueryMapperTest.getConnection();
		}
	}), QueryRunner.withJitter(QueryRunner.fixedDelay(5), 5));

	private void testPerson(final Person expected, final String query) throws Throwable {
		final Person actual =
				//qr.run(
				//qr.runRetry(
				qr.runRetryFuture(
				//qr.runRetryCompletableFuture(
				new QueryRunner.Runner<QueryMapper, Person>() {
			@Override
			public Person run(final QueryMapper qm) throws SQLException {
				if(Math.random() < 0.5) {
					System.out.println("fake fail");
					throw new SQLException("fake 50% failure rate");
				}
				return qm.toObject(query, expected.getClass(), expected.getPersonNo());
			}
		})
				.get()
				//.join()
				//.thenAccept(actual -> Assert.assertEquals(expected, actual))
				;
		/*
		System.out.println("expected: " + expected);
		System.out.println("actual:   " + actual);
		*/
		Assert.assertEquals(expected, actual);
	}

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
}
