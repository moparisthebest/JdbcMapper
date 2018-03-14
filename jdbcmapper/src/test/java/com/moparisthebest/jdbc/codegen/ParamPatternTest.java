package com.moparisthebest.jdbc.codegen;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Matcher;

import static com.moparisthebest.jdbc.codegen.JdbcMapperProcessor.paramPattern;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ParamPatternTest {

	@Test
	public void testPattern() {
		testMatch("{personNo}", s(null, null, null, null, null, null, "personNo"));
		testMatch("{last_name IN lastNames}", s("last_name IN ", "last_name", "IN ", null, null, null, "lastNames"));
		testMatch("{last_name not in lastNames}", s("last_name not in ", "last_name", "not in ", "not ", null, null, "lastNames"));
		testMatch("{clob:comment}", s(null, null, null, null, "clob:", null, "comment"));
		testMatch("{clob: comment}", s(null, null, null, null, "clob: ", null, "comment"));
		testMatch("{blob: comment}", s(null, null, null, null, "blob: ", null, "comment"));
		testMatch("{Blob: comment}", s(null, null, null, null, "Blob: ", null, "comment"));
		testMatch("{blob:utf-16:comment}", s(null, null, null, null, "blob:utf-16:", "utf-16:", "comment"));
		testMatch("{clob:comment} {clob:action}", c(a(null, null, null, null, "clob:", null, "comment"), a(null, null, null, null, "clob:", null, "action")));
		String blobCharset = "utf-16:";
		assertEquals("utf-16", blobCharset.substring(0, blobCharset.indexOf(':')).trim());
		blobCharset = "utf-16  : ";
		assertEquals("utf-16", blobCharset.substring(0, blobCharset.indexOf(':')).trim());
	}

	private static void testMatch(final String sql, final Collection<String[]> expected) {
		final Matcher bindParamMatcher = paramPattern.matcher(sql);
		final Iterator<String[]> it = expected.iterator();
		while (bindParamMatcher.find()) {
			final String[] matches = new String[bindParamMatcher.groupCount()];
			for (int x = 0; x < matches.length; )
				matches[x] = bindParamMatcher.group(++x);
			//System.out.printf("sql: '%s', whole group: '%s', matches: '%s'%n", sql, bindParamMatcher.group(), java.util.Arrays.toString(matches));
			assertArrayEquals(bindParamMatcher.group(), it.next(), matches);
		}
		assertFalse(it.hasNext());
	}

	private static Collection<String[]> s(final String... s) {
		return Collections.singleton(s);
	}

	private static String[] a(final String... a) {
		return a;
	}

	private static Collection<String[]> c(final String[]... c) {
		return Arrays.asList(c);
	}
}
