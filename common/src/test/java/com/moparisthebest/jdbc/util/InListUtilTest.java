package com.moparisthebest.jdbc.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static com.moparisthebest.jdbc.util.InListUtil.toInList;
import static com.moparisthebest.jdbc.util.InListUtil.toNotInList;
import static org.junit.Assert.assertEquals;

public class InListUtilTest {

    private static Collection<Long> makeCollection(final int length) {
        final Collection<Long> ret = new ArrayList<Long>(length);
        final int loopLength = length + 1;
        for(int x = 1; x < loopLength; ++x)
            ret.add((long) x);
        return ret;
    }

    @Test
    public void testMakeCollection() {
        assertEquals(new ArrayList<Long>(Arrays.asList(1L, 2L, 3L)), makeCollection(3));
    }

    @Test
    public void testShortInList() {
        assertEquals("(column_name IN (?,?,?,?,?))", toInList("column_name", makeCollection(5), 20));
    }

    @Test
    public void testShortNotInList() {
        assertEquals("(column_name NOT IN (?,?,?,?,?))", toNotInList("column_name", makeCollection(5), 20));
    }

    @Test
    public void testExactInList() {
        assertEquals("(column_name IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?))",
                toInList("column_name", makeCollection(20), 20));
    }

    @Test
    public void testExactNotInList() {
        assertEquals("(column_name NOT IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?))",
                toNotInList("column_name", makeCollection(20), 20));
    }

    @Test
    public void testOneOverInList() {
        assertEquals("(column_name IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) OR column_name IN (?))",
                toInList("column_name", makeCollection(21), 20));
    }

    @Test
    public void testOneOverNotInList() {
        assertEquals("(column_name NOT IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) AND column_name NOT IN (?))",
                toNotInList("column_name", makeCollection(21), 20));
    }

    @Test
    public void testOneUnderInList() {
        assertEquals("(column_name IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?))",
                toInList("column_name", makeCollection(19), 20));
    }

    @Test
    public void testOneUnderNotInList() {
        assertEquals("(column_name NOT IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?))",
                toNotInList("column_name", makeCollection(19), 20));
    }

    @Test
    public void testOneOver3InList() {
        assertEquals("(column_name IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) OR column_name IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                        "OR column_name IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) OR column_name IN (?))",
                toInList("column_name", makeCollection(61), 20));
    }

    @Test
    public void testOneOver3NotInList() {
        assertEquals("(column_name NOT IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) AND column_name NOT IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                        "AND column_name NOT IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) AND column_name NOT IN (?))",
                toNotInList("column_name", makeCollection(61), 20));
    }
}
