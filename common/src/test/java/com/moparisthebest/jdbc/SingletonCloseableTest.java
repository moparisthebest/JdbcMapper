package com.moparisthebest.jdbc;

import org.junit.Test;

import java.io.Closeable;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class SingletonCloseableTest {

    @Test
    public void testRegularCloseable() throws Exception {
        final DaoFactory factory = new DaoFactory();
        assertEquals(0, factory.totalOpened);
        assertEquals(0, factory.numStillOpen);

        {
            final Dao dao1 = factory.create();
            assertEquals(1, factory.totalOpened);
            assertEquals(1, factory.numStillOpen);

            final Closed closed1 = dao1.getClosed();
            assertEquals(1, factory.totalOpened);
            assertEquals(1, factory.numStillOpen);
            assertEquals("num1", closed1.name());
            closed1.close();
            assertTrue(closed1.isClosed());
            assertEquals(1, factory.totalOpened);
            assertEquals(1, factory.numStillOpen);

            final Closed closed2 = dao1.getClosed();
            assertEquals(1, factory.totalOpened);
            assertEquals(1, factory.numStillOpen);
            assertEquals("num2", closed2.name());
            closed2.close();
            assertTrue(closed2.isClosed());
            assertEquals(1, factory.totalOpened);
            assertEquals(1, factory.numStillOpen);

            dao1.close();
            assertEquals(1, factory.totalOpened);
            assertEquals(0, factory.numStillOpen);
            assertTrue(dao1.isClosed());
            assertEquals(0, factory.numStillOpen);
        }

        final Dao dao1 = factory.create();
        assertEquals(2, factory.totalOpened);
        assertEquals(1, factory.numStillOpen);

        final Closed closed1 = dao1.getClosed();
        assertEquals(2, factory.totalOpened);
        assertEquals(1, factory.numStillOpen);
        assertEquals("num1", closed1.name());

        final Closed closed2 = dao1.getClosed();
        assertEquals(2, factory.totalOpened);
        assertEquals(1, factory.numStillOpen);
        assertEquals("num2", closed2.name());

        assertEquals(5, dao1.getInt());
        assertEquals(2, factory.totalOpened);
        assertEquals(1, factory.numStillOpen);

        dao1.close();
        assertEquals(2, factory.totalOpened);
        assertEquals(0, factory.numStillOpen);
        assertTrue(dao1.isClosed());
        assertFalse(closed1.isClosed());
        assertFalse(closed2.isClosed());
    }

    @Test
    public void testSingletonCloseable() throws Exception {
        final DaoFactory factory = new DaoFactory();
        assertEquals(0, factory.totalOpened);
        assertEquals(0, factory.numStillOpen);
        final Dao dao1 = SingletonCloseable.of(Dao.class, factory);

        {
            assertEquals(0, factory.totalOpened);
            assertEquals(0, factory.numStillOpen);

            final Closed closed1 = dao1.getClosed();
            assertEquals(1, factory.totalOpened);
            assertEquals(1, factory.numStillOpen);
            assertEquals("num1", closed1.name());
            closed1.close();
            assertTrue(closed1.isClosed());
            assertEquals(1, factory.totalOpened);
            assertEquals(0, factory.numStillOpen);

            final Closed closed2 = dao1.getClosed();
            assertEquals(2, factory.totalOpened);
            assertEquals(1, factory.numStillOpen);
            assertEquals("num1", closed2.name());
            closed2.close();
            assertTrue(closed2.isClosed());
            assertEquals(2, factory.totalOpened);
            assertEquals(0, factory.numStillOpen);

            dao1.close();
            assertEquals(3, factory.totalOpened);
            assertEquals(0, factory.numStillOpen);
            assertFalse(dao1.isClosed()); // always appears open even though does not stay open
            assertEquals(0, factory.numStillOpen);
        }

        assertEquals(4, factory.totalOpened);
        assertEquals(0, factory.numStillOpen);

        final Closed closed1 = dao1.getClosed();
        assertEquals(5, factory.totalOpened);
        assertEquals(1, factory.numStillOpen);
        assertEquals("num1", closed1.name());

        final Closed closed2 = dao1.getClosed();
        assertEquals(6, factory.totalOpened);
        assertEquals(2, factory.numStillOpen);
        assertEquals("num1", closed2.name());

        assertEquals(5, dao1.getInt());
        assertEquals(7, factory.totalOpened);
        assertEquals(2, factory.numStillOpen);

        dao1.close();
        assertEquals(8, factory.totalOpened);
        assertEquals(2, factory.numStillOpen);
        assertFalse(dao1.isClosed());
        assertEquals(9, factory.totalOpened);
        assertEquals(2, factory.numStillOpen);
        assertFalse(closed1.isClosed());
        assertFalse(closed2.isClosed());
        assertEquals(9, factory.totalOpened);
        assertEquals(2, factory.numStillOpen);
        closed1.close();
        assertEquals(9, factory.totalOpened);
        assertEquals(1, factory.numStillOpen);
        closed2.close();
        assertEquals(9, factory.totalOpened);
        assertEquals(0, factory.numStillOpen);
    }

    class DaoFactory implements Factory<Dao> {
        int totalOpened = 0;
        int numStillOpen = 0;

        @Override
        public Dao create() throws SQLException {
            return new DaoImpl(this);
        }
    }

    class DaoImpl implements Dao {
        final DaoFactory factory;

        private int simpleCloseableCount = 0;
        private boolean closed = false;

        DaoImpl(DaoFactory factory) {
            ++factory.totalOpened;
            ++factory.numStillOpen;
            this.factory = factory;
        }

        @Override
        public boolean isClosed() {
            return closed;
        }

        @Override
        public int getInt() {
            return 5;
        }

        @Override
        public Closed getClosed() {
            return new SimpleCloseable("num" + (++simpleCloseableCount));
        }

        @Override
        public void close() {
            if (!closed) {
                --factory.numStillOpen;
                closed = true;
            }
        }
    }

    interface Dao extends Closeable {
        boolean isClosed();

        int getInt();

        Closed getClosed();
    }
}