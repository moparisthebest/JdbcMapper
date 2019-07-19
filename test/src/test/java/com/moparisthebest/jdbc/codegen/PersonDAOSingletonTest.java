package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.Factory;
import com.moparisthebest.jdbc.QueryMapperTest;
import com.moparisthebest.jdbc.SingletonCloseable;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.moparisthebest.jdbc.QueryMapperTest.fieldPerson1;
import static com.moparisthebest.jdbc.TryClose.tryClose;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PersonDAOSingletonTest {

    public static final PersonDAO personDAO = SingletonCloseable.of(PersonDAO.class, JdbcMapperFactory.of(PersonDAO.class, new Factory<Connection>() {
        @Override
        public Connection create() throws SQLException {
            return QueryMapperTest.getConnection();
        }
    }));

    @Test
    public void testRegularPersonDAO() throws Exception {
        PersonDAO personDAO = null;
        try {
            personDAO = JdbcMapperFactory.create(PersonDAO.class, new Factory<Connection>() {
                @Override
                public Connection create() throws SQLException {
                    return QueryMapperTest.getConnection();
                }
            });
            testPerson(personDAO);
        } finally {
            tryClose(personDAO);
        }
    }

    @Test
    public void testSingletonPersonDAO() throws Exception {
        testPerson(personDAO);
    }

    private void testPerson(final PersonDAO personDAO) throws Exception {
        assertEquals(fieldPerson1, personDAO.getPerson(fieldPerson1.getPersonNo()));
        ResultSet rs = null;
        try {
            rs = personDAO.getPeopleResultSet(fieldPerson1.getLastName());
            assertTrue(rs.next());
            assertEquals(fieldPerson1.getLastName(), rs.getString("last_name"));
        } finally {
            tryClose(rs);
        }
        try {
            rs = personDAO.getPeopleResultSetCached(fieldPerson1.getLastName());
            assertTrue(rs.next());
            assertEquals(fieldPerson1.getLastName(), rs.getString("last_name"));
        } finally {
            tryClose(rs);
        }
    }
}
