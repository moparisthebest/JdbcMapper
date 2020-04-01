package com.moparisthebest.jdbc.codegen;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mopar on 5/30/17.
 */
public class SimpleSQLParserTest {

	private final SQLParser factory = new SimpleSQLParser();

	public SQLParser getFactory() {
		return factory;
	}

	@Test
	public void testSingleSelect() {
		{
			final SQLParser ret = getFactory().parse("select bob from tom");
			assertTrue(ret.isSelect());
			assertArrayEquals(new String[]{null, "BOB"}, ret.columnNames());
		}
		{
			final SQLParser ret = getFactory().parse("select bob_from from tom");
			assertTrue(ret.isSelect());
			assertArrayEquals(new String[]{null, "BOB_FROM"}, ret.columnNames());
		}
		{
			final SQLParser ret = getFactory().parse("select from_bob from tom");
			assertTrue(ret.isSelect());
			assertArrayEquals(new String[]{null, "FROM_BOB"}, ret.columnNames());
		}
		{
			final SQLParser ret = getFactory().parse("select something_from_bob from tom");
			assertTrue(ret.isSelect());
			assertArrayEquals(new String[]{null, "SOMETHING_FROM_BOB"}, ret.columnNames());
		}
		{
			final SQLParser ret = getFactory().parse("select something_from_bob from( tom)");
			assertTrue(ret.isSelect());
			assertArrayEquals(new String[]{null, "SOMETHING_FROM_BOB"}, ret.columnNames());
		}
	}

	@Test
	public void testMultiSelect() {
		final String[] expected = new String[]{null, "BOB", "TOM"};
		for (final String sql : new String[]{
				"select bob, tom from tom"
				, "select some_bob bob, some_tom as tom from tom"
				, "select tom.bob, some_tom as tom from tom"
				, "select tom.bob, COALESCE(some_tom, 'UNKNOWN') as tom from tom"
				, "select tom.bob, (SELECT some_column from some_table where other_column = 'YAY') as tom from tom"
		}) {
			final SQLParser ret = getFactory().parse(sql);
			assertTrue(ret.isSelect());
			assertArrayEquals(expected, ret.columnNames());
		}
	}

	@Test
	public void testNotSelect() {
		for (final String sql : new String[]{
				"UPDATE bob SET bob = 'bob' WHERE bob_no = 1"
				, "INSERT INTO bob (bob_no, bob) VALUES (1, 'bob')"
				, "MERGE INTO bob bla bla bla"
		}) {
			final SQLParser ret = getFactory().parse(sql);
			assertFalse(ret.isSelect());
		}
	}

}
