package com.moparisthebest.jdbc;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/**
 * Created by mopar on 6/19/17.
 */
class ReflectionAccessibleObject extends AccessibleObject {
	final Field field;
	final int index;

	public ReflectionAccessibleObject(final Field field, final int index) {
		this.field = field;
		this.index = index;
	}
}
