package com.moparisthebest.jdbc;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class WrappingCloseableTest {

    @Test
    public void test() throws IOException {
        final Closed parent = new SimpleCloseable("parent");
        final Closed child = new SimpleCloseable("child");

        assertFalse(parent.isClosed());
        assertEquals("parent", parent.name());

        assertFalse(child.isClosed());
        assertEquals("child", child.name());

        final Closed wrapped = WrappingCloseable.wrap(child, Closed.class, parent);
        // when wrapped is closed, it should close child and then parent

        assertFalse(parent.isClosed());
        assertEquals("parent", parent.name());

        assertFalse(child.isClosed());
        assertEquals("child", child.name());

        assertFalse(wrapped.isClosed());
        assertEquals("child", wrapped.name());

        wrapped.close();

        assertTrue(parent.isClosed());
        assertEquals("parent", parent.name());

        assertTrue(child.isClosed());
        assertEquals("child", child.name());

        assertTrue(wrapped.isClosed());
        assertEquals("child", wrapped.name());
    }
}