package com.moparisthebest.jdbc;

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
			if (arrayComponentType instanceof Class<?>) {
				this.type = null;
				this.rawType = (Class<?>) arrayComponentType;
			} else if (arrayComponentType instanceof ParameterizedType) {
				this.type = (ParameterizedType) arrayComponentType;
				this.rawType = (Class<?>) this.type.getRawType();
			} else {
				throw new IllegalArgumentException("Internal error: TypeReference constructed with unknown type: '" + type + "' class: '" + type.getClass() + "'");
			}
			this.genericArray = true;
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
}

