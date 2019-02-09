package com.moparisthebest.jdbc.codegen;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created by mopar on 5/24/17.
 */
public class PrestoPersonDAOTest {

	@Test
	public void testGeneratedFilesAreTheSame() throws IOException {
		final String personDaoBean = new String(
				Files.readAllBytes(Paths.get("target/generated-sources/annotations/com/moparisthebest/jdbc/codegen/PersonDAO" + JdbcMapper.beanSuffix + ".java"))
				, StandardCharsets.UTF_8);
		final String prestoPersonDaoBean = new String(
				Files.readAllBytes(Paths.get("target/generated-sources/annotations/com/moparisthebest/jdbc/codegen/PrestoPersonDAO" + JdbcMapper.beanSuffix + ".java"))
				, StandardCharsets.UTF_8).replace("Presto", "");
		assertEquals(personDaoBean, prestoPersonDaoBean);
	}
}
