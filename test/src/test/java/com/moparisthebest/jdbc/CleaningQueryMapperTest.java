package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.dto.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

import static com.moparisthebest.jdbc.QueryMapperTest.fieldPerson1;
import static com.moparisthebest.jdbc.QueryMapperTest.getConnection;
import static com.moparisthebest.jdbc.QueryMapperTest.personRegular;
import static com.moparisthebest.jdbc.TryClose.tryClose;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by mopar on 6/10/14.
 */
@RunWith(Parameterized.class)
public class CleaningQueryMapperTest {

	private static Connection conn;

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

	public CleaningQueryMapperTest(final ResultSetMapper rsm) {
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
		final Cleaner<FieldPerson> personCleaner = new Cleaner<FieldPerson>() {
			@Override
			public FieldPerson clean(final FieldPerson dto) {
				return dto.cleanThyself();
			}
		};
		return Arrays.asList(new Object[][] {
				{ new CleaningResultSetMapper<FieldPerson>(personCleaner) },
				{ new CleaningCachingResultSetMapper<FieldPerson>(personCleaner) },
				{ new CleaningCompilingResultSetMapper<FieldPerson>(personCleaner, new CompilingRowToObjectMapper.Cache(true)) },
		});
	}

	// fields

	@Test
	public void testFieldRegularPerson() throws Throwable {
		final Person expected = fieldPerson1;
		final Person actual = qm.toObject(personRegular, expected.getClass(), expected.getPersonNo());
		assertEquals(expected.getFirstName() + " " + expected.getLastName(), actual.getFirstName());
		assertNull(actual.getLastName());
	}
}
