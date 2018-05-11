package com.moparisthebest.jdbc;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This generic abstract class is used for obtaining full generics type information
 * by sub-classing.
 * <p>
 * Class is based on ideas from
 * <a href="http://gafter.blogspot.com/2006/12/super-type-tokens.html"
 * >http://gafter.blogspot.com/2006/12/super-type-tokens.html</a>,
 * Additional idea (from a suggestion made in comments of the article)
 * is to require bogus implementation of <code>Comparable</code>
 * (any such generic interface would do, as long as it forces a method
 * with generic type to be implemented).
 * to ensure that a Type argument is indeed given.
 * <p>
 * Usage is by sub-classing: here is one way to instantiate reference
 * to generic type <code>List&lt;Integer&gt;</code>:
 * <pre>
 *  TypeReference ref = new TypeReference&lt;List&lt;Integer&gt;&gt;() { };
 * </pre>
 * which can be passed to methods that accept TypeReference
 * <p>
 * Pulled from jackson here:
 * https://raw.githubusercontent.com/FasterXML/jackson-core/3dcedd2b6838ef29abb179557b6e42479e93834b/src/main/java/com/fasterxml/jackson/core/type/TypeReference.java
 * Thankfully jackson has an identical Apache 2.0 license so no issues there
 */
public abstract class TypeReference<T> implements Comparable<TypeReference<T>> {

	private final ParameterizedType type;
	private final Class<?> rawType;
	private final boolean genericArray;

	protected TypeReference() {
		final Type superClass = getClass().getGenericSuperclass();
		if (superClass instanceof Class<?>) { // sanity check, should never happen
			throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
		}
		/* 22-Dec-2008, tatu: Not sure if this case is safe -- I suspect
		 *   it is possible to make it fail?
         *   But let's deal with specific
         *   case when we know an actual use case, and thereby suitable
         *   workarounds for valid case(s) and/or error to throw
         *   on invalid one(s).
         */
		final Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
		if (type instanceof Class<?>) {
			this.type = null;
			this.rawType = (Class<?>) type;
			this.genericArray = false;
		} else if (type instanceof ParameterizedType) {
			this.type = (ParameterizedType) type;
			this.rawType = (Class<?>) this.type.getRawType();
			this.genericArray = false;
		} else if (type instanceof GenericArrayType) {
			final Type arrayComponentType = ((GenericArrayType)type).getGenericComponentType();
			if (arrayComponentType instanceof ParameterizedType) {
				this.type = (ParameterizedType) arrayComponentType;
				this.rawType = (Class<?>) this.type.getRawType();
				this.genericArray = true;
			} else if (arrayComponentType instanceof Class<?>) {
				/*
				 * java 6 differs from every other version here in a very specific way:
				 *
				 * this code used to be:
				 * this.rawType = (Class<?>) arrayComponentType;
				 * this.genericArray = true;
				 *
				 * for new TypeReference<Long[]>() {}, these were the results:
				 *
				 * java 6 TypeReference{type=null, rawType=class java.lang.Long, genericArray=true}
				 * java 7+ TypeReference{type=null, rawType=class [Ljava.lang.Long;, genericArray=false}
				 *
				 * turns out this block only ever runs for java 6 and not 7+, which is handled automatically by the first
				 * if (type instanceof Class<?>) block, replicate that with a hack for java 6
				 */
				this.type = null;
				this.rawType = Array.newInstance((Class<?>) arrayComponentType, 0).getClass();
				this.genericArray = false;
			} else {
				throw new IllegalArgumentException("Internal error: TypeReference constructed with unknown type: '" + type + "' class: '" + type.getClass() + "'");
			}
		} else {
			throw new IllegalArgumentException("Internal error: TypeReference constructed with unknown type: '" + type + "' class: '" + type.getClass() + "'");
		}
	}

	public final ParameterizedType getType() {
		return type;
	}

	public final Class<?> getRawType() {
		return rawType;
	}

	public boolean isGenericArray() {
		return genericArray;
	}

	/**
	 * The only reason we define this method (and require implementation
	 * of <code>Comparable</code>) is to prevent constructing a
	 * reference without type information.
	 */
	@Override
	public final int compareTo(TypeReference<T> o) {
		return 0;
	}
	// just need an implementation, not a good one... hence ^^^

	@Override
	public String toString() {
		return "TypeReference{" +
				"type=" + type +
				", rawType=" + rawType +
				", genericArray=" + genericArray +
				'}';
	}
}

