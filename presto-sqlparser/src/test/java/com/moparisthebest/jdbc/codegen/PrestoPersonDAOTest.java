package com.moparisthebest.jdbc.codegen;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.moparisthebest.jdbc.QueryMapperTest.fieldPerson1;
import static com.moparisthebest.jdbc.QueryMapperTest.getConnection;
import static com.moparisthebest.jdbc.TryClose.tryClose;
import static org.junit.Assert.assertEquals;

/**
 * Created by mopar on 5/24/17.
 */
public class PrestoPersonDAOTest extends JdbcMapperTest {

    private static PersonDAO dao;

    @BeforeClass
    public static void setUp() throws Throwable {
        dao = JdbcMapperFactory.create(PrestoPersonDAO.class, getConnection());
        //dao = new com.moparisthebest.jdbc.codegen.PersonDAOBean(getConnection());
    }

    @AfterClass
    public static void tearDown() throws Throwable {
        tryClose(dao);
    }

    public PersonDAO getDao() {
        return dao;
    }
}
