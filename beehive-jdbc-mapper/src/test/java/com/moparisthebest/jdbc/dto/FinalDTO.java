package com.moparisthebest.jdbc.dto;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * This class is meant to test the java compiler's in-lining behavior for final variables
 */
public class FinalDTO {

	private static final boolean useReflectionNotUnsafe = true;

	private final long finalPrimitiveLongDirect = 2L;
	private long effectiveFinalPrimitiveLongDirect = 2L;
	private final long finalPrimitiveLongConstructor;

	{
		this.finalPrimitiveLongConstructor = 2L;
	}

	private final String finalStringDirect = "2";
	private String effectiveFinalStringDirect = "2";
	private final String finalStringConstructor;

	{
		this.finalStringConstructor = "2";
	}

	private final Long finalLongDirect = 2L;
	private final Long finalLongConstructor;

	{
		this.finalLongConstructor = 2L;
	}

	public long getFinalPrimitiveLongDirect() {
		return finalPrimitiveLongDirect;
	}

	public long getEffectiveFinalPrimitiveLongDirect() {
		return effectiveFinalPrimitiveLongDirect;
	}

	public long getFinalPrimitiveLongConstructor() {
		return finalPrimitiveLongConstructor;
	}

	public String getFinalStringDirect() {
		return finalStringDirect;
	}

	public String getEffectiveFinalStringDirect() {
		return effectiveFinalStringDirect;
	}

	public String getFinalStringConstructor() {
		return finalStringConstructor;
	}

	public Long getFinalLongDirect() {
		return finalLongDirect;
	}

	public Long getFinalLongConstructor() {
		return finalLongConstructor;
	}

	@Override
	public String toString() {
		return "FinalTest{" +
				"finalPrimitiveLongDirect=" + finalPrimitiveLongDirect +
				", reflection finalPrimitiveLongDirect=" + getField("finalPrimitiveLongDirect") +
				", effectiveFinalPrimitiveLongDirect=" + effectiveFinalPrimitiveLongDirect +
				", reflection effectiveFinalPrimitiveLongDirect=" + getField("effectiveFinalPrimitiveLongDirect") +
				", finalPrimitiveLongConstructor=" + finalPrimitiveLongConstructor +
				", reflection finalPrimitiveLongConstructor=" + getField("finalPrimitiveLongConstructor") +
				", finalStringDirect=" + finalStringDirect +
				", reflection finalStringDirect=" + getField("finalStringDirect") +
				", effectiveFinalStringDirect=" + effectiveFinalStringDirect +
				", reflection effectiveFinalStringDirect=" + getField("effectiveFinalStringDirect") +
				", finalStringConstructor=" + finalStringConstructor +
				", reflection finalStringConstructor=" + getField("finalStringConstructor") +
				", finalLongDirect=" + finalLongDirect +
				", reflection finalLongDirect=" + getField("finalLongDirect") +
				", finalLongConstructor=" + finalLongConstructor +
				", reflection finalLongConstructor=" + getField("finalLongConstructor") +
				'}';
	}

	public boolean directEqualsReflection() {
		return directEqualsReflection(finalPrimitiveLongDirect, "finalPrimitiveLongDirect")
				&& directEqualsReflection(effectiveFinalPrimitiveLongDirect, "effectiveFinalPrimitiveLongDirect")
				&& directEqualsReflection(finalPrimitiveLongConstructor, "finalPrimitiveLongConstructor")
				&& directEqualsReflection(finalStringDirect, "finalStringDirect")
				&& directEqualsReflection(effectiveFinalStringDirect, "effectiveFinalStringDirect")
				&& directEqualsReflection(finalStringConstructor, "finalStringConstructor")
				&& directEqualsReflection(finalLongDirect, "finalLongDirect")
				&& directEqualsReflection(finalLongConstructor, "finalLongConstructor")
				;
	}

	private boolean directEqualsReflection(final Object object, final String fieldName) {
		return Objects.equals(object, getField(fieldName));
	}

	public Object getField(final String fieldName) {
		return getField(this, fieldName);
	}

	public boolean setField(final String fieldName, final Object fieldValue) {
		setField(this, fieldName, fieldValue);
		return directEqualsReflection();
	}

	private static Object getField(final Object object, final String fieldName) {
		try {
			final Field field = object.getClass().getDeclaredField(fieldName);
			if (useReflectionNotUnsafe) {
				field.setAccessible(true);
				return field.get(object);
			} else {
				final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
				unsafeField.setAccessible(true);
				final Unsafe unsafe = (Unsafe) unsafeField.get(null);

				final long offset = unsafe.objectFieldOffset(field);

				if (field.getType().isPrimitive()) {
					if (field.getType().equals(long.class))
						return unsafe.getLong(object, offset);
					else
						throw new RuntimeException("unsupported primitive type");
				} else
					return unsafe.getObject(object, offset);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static void setField(final Object object, final String fieldName, final Object fieldValue) {
		try {
			final Field field = object.getClass().getDeclaredField(fieldName);
			if (useReflectionNotUnsafe) {
				field.setAccessible(true);
				field.set(object, fieldValue);
			} else {
				final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
				unsafeField.setAccessible(true);
				final Unsafe unsafe = (Unsafe) unsafeField.get(null);

				final long offset = unsafe.objectFieldOffset(field);

				if (field.getType().isPrimitive()) {
					if (fieldValue instanceof Long)
						unsafe.putLong(object, offset, (Long) fieldValue);
					else
						throw new RuntimeException("unsupported primitive type");
				} else
					unsafe.putObject(object, offset, fieldValue);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(final String[] args) {
		final FinalDTO object = new FinalDTO();
		System.out.println("1: " + object);
		setField(object, "finalPrimitiveLongDirect", 1L);
		System.out.println("2: " + object);
		setField(object, "finalPrimitiveLongConstructor", 1L);
		System.out.println("3: " + object);
		setField(object, "finalStringDirect", "1");
		System.out.println("4: " + object);
		setField(object, "finalStringConstructor", "1");
		System.out.println("5: " + object);
		setField(object, "finalLongDirect", 1L);
		System.out.println("6: " + object);
		setField(object, "finalLongConstructor", 1L);
		System.out.println("7: " + object);
		setField(object, "effectiveFinalPrimitiveLongDirect", 1L);
		System.out.println("8: " + object);
		setField(object, "effectiveFinalStringDirect", "1");
		System.out.println("9: " + object);
	}
}
