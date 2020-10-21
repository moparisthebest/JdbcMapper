package com.moparisthebest.jdbc.util;

import java.lang.reflect.Field;

/**
 * Created by mopar on 6/19/17.
 */
public class ReflectionUtil {
	public static Field getAccessibleField(final Class<?> clazz, final String declaredField) {
		try {
			final Field ret = clazz.getDeclaredField(declaredField);
			ret.setAccessible(true);
			return ret;
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setValue(final Field field, final Object obj, final Object value) {
		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T getValue(final Class<?> clazz, final String declaredField, final Class<T> retClazz, final Object obj) {
		if(obj == null) return null;
		return getValue(getAccessibleField(clazz, declaredField), retClazz, obj);
	}

	public static <T> T getValue(final Field field, final Class<T> retClazz, final Object obj) {
		try {
			return retClazz.cast(field.get(obj));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
