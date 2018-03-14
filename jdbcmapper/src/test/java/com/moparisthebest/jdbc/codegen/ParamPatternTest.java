package com.moparisthebest.jdbc.codegen;

import org.junit.Test;

import java.util.regex.Matcher;

import static com.moparisthebest.jdbc.codegen.JdbcMapperProcessor.paramPattern;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ParamPatternTest {

	@Test
	public void testPattern() {
		testMatch("{personNo}", null, null, null, null, null, null, "personNo");
		testMatch("{last_name IN lastNames}", "last_name IN ", "last_name", "IN ", null, null, null, "lastNames");
		testMatch("{last_name not in lastNames}", "last_name not in ", "last_name", "not in ", "not ", null, null, "lastNames");
		testMatch("{clob:comment}", null, null, null, null, "clob:", null, "comment");
		testMatch("{clob: comment}", null, null, null, null, "clob: ", null, "comment");
		testMatch("{blob: comment}", null, null, null, null, "blob: ", null, "comment");
		testMatch("{Blob: comment}", null, null, null, null, "Blob: ", null, "comment");
		testMatch("{blob:utf-16:comment}", null, null, null, null, "blob:utf-16:", "utf-16:", "comment");
		String blobCharset = "utf-16:";
		assertEquals("utf-16", blobCharset.substring(0, blobCharset.indexOf(':')).trim());
		blobCharset = "utf-16  : ";
		assertEquals("utf-16", blobCharset.substring(0, blobCharset.indexOf(':')).trim());
	}

	private static void testMatch(final String sql, final String... expected) {
		final Matcher bindParamMatcher = paramPattern.matcher(sql);
		while (bindParamMatcher.find()) {
			final String[] matches = new String[bindParamMatcher.groupCount()];
			for (int x = 0; x < matches.length; )
				matches[x] = bindParamMatcher.group(++x);
			//System.out.println(bindParamMatcher.group() + ": " + java.util.Arrays.toString(matches));
			assertArrayEquals(bindParamMatcher.group(), expected, matches);
		}
	}
}
