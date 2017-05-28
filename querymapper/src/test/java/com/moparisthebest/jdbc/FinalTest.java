package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.dto.FinalDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * This test ensures the way the java compiler in-lines final variables today
 * is the same way it will in-line them tomorrow, since changes in this behavior
 * might silently break classes that are filled with reflection.
 */
public class FinalTest {

	private FinalDTO object;

	@Before
	public void before(){
		object = new FinalDTO();
		assertTrue(object.directEqualsReflection());
	}

	@Test
	public void testFinalPrimitiveLongDirectCantBeSet() {
		assertFalse(object.setField("finalPrimitiveLongDirect", 1L));
	}

	@Test
	public void testFinalStringDirectCantBeSet() {
		assertFalse(object.setField("finalStringDirect", "1"));
	}

	@Test
	public void testEffectiveFinalPrimitiveLongDirectCanBeSet() {
		assertTrue(object.setField("effectiveFinalPrimitiveLongDirect", 1L));
	}

	@Test
	public void testEffectiveFinalStringDirectCanBeSet() {
		assertTrue(object.setField("effectiveFinalStringDirect", "1"));
	}

	@Test
	public void testFinalPrimitiveLongConstructorCanBeSet() {
		assertTrue(object.setField("finalPrimitiveLongConstructor", 1L));
	}

	@Test
	public void testFinalStringConstructorCanBeSet() {
		assertTrue(object.setField("finalStringConstructor", "1"));
	}

	@Test
	public void testFinalLongDirectCanBeSet() {
		assertTrue(object.setField("finalLongDirect", 1L));
	}

	@Test
	public void testFinalLongConstructorCanBeSet() {
		assertTrue(object.setField("finalLongConstructor", 1L));
	}
}
