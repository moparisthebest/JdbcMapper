package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.codegen.CleaningPersonDao;
import com.moparisthebest.jdbc.codegen.JdbcMapper;
import com.moparisthebest.jdbc.codegen.JdbcMapperFactory;
import com.moparisthebest.jdbc.dto.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

import static com.moparisthebest.jdbc.QueryMapperTest.fieldPerson1;
import static com.moparisthebest.jdbc.QueryMapperTest.getConnection;
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
	protected CleaningPersonDao cleaningPersonDao;
	protected final Cleaner<FieldPerson> personCleaner;
	protected final ResultSetMapper rsm;

	public CleaningQueryMapperTest(final Cleaner<FieldPerson> personCleaner, final ResultSetMapper rsm) {
		this.personCleaner = personCleaner;
		this.rsm = rsm;
	}

	@Before
	public void open() {
		if(rsm == null)
			this.cleaningPersonDao = JdbcMapperFactory.create(CleaningPersonDao.class, conn);
		else
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
			@SuppressWarnings("unchecked")
			public <E extends FieldPerson> E clean(final E dto) {
				return (E) dto.cleanThyself();
			}
		};
		return Arrays.asList(new Object[][] {
				{ null, new CleaningResultSetMapper<FieldPerson>(personCleaner) },
				{ null, new CleaningCachingResultSetMapper<FieldPerson>(personCleaner) },
				{ null, new CleaningCompilingResultSetMapper<FieldPerson>(personCleaner, new CompilingRowToObjectMapper.Cache(true)) },
				{ personCleaner, null },
		});
	}

	// fields

	@Test
	public void testFieldRegularPerson() throws Throwable {
		final Person expected = fieldPerson1;
		final Person actual = this.cleaningPersonDao == null ?
				qm.toObject("SELECT * FROM person WHERE person_no = ?", expected.getClass(), expected.getPersonNo())
				:
				this.cleaningPersonDao.getPerson(expected.getPersonNo(), personCleaner);
		assertEquals(expected.getFirstName() + " " + expected.getLastName(), actual.getFirstName());
		assertNull(actual.getLastName());
	}
}
