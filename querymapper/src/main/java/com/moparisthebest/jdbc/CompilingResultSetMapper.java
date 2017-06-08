package com.moparisthebest.jdbc;

import com.moparisthebest.classgen.Compiler;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This generally follows the contract of ResultSetMapper, with the differences specified in CompilingRowToObjectMapper.
 * <p>
 * This does cache compiled code based on column name/order and DTO being mapped to, so the (rather heavy)
 * code generation/compilation/instantiation only happens once for each query/dto, and is very fast on subsequent calls.
 * <p>
 * By default this uses a plain HashMap for the cache, unbounded, and not thread-safe.  There are overloaded constructors
 * you can use to tell it to be threadSafe in which case it uses an unbounded ConcurrentHashMap, or to give it a maxEntries
 * in which case it uses a LinkedHashMap and clears out old entries in LRU order keeping only maxEntries. Lastly you can
 * send in your own custom Map implementation, CompilingResultSetMapper guarantees null will never be used for key or value.
 *
 * @see CompilingRowToObjectMapper
 */
public class CompilingResultSetMapper extends ResultSetMapper {

	protected final Compiler compiler = new Compiler();
	protected final Map<CompilingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?,?>> cache;

	/**
	 * CompilingResultSetMapper with optional maxEntries, expiring old ones in LRU fashion
	 *
	 * @param cal            optional calendar for date/time values
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 * @param maxEntries     max cached compiled entries to keep in cache, < 1 means unlimited
	 */
	public CompilingResultSetMapper(final Calendar cal, final int arrayMaxLength, final int maxEntries) {
		super(cal, arrayMaxLength);
		if (maxEntries > 0) { // we want a limited cache
			final float loadFactor = 0.75f; // default for HashMaps
			// if we set the initialCapacity this way, nothing should ever need re-sized
			final int initialCapacity = ((int) Math.ceil(maxEntries / loadFactor)) + 1;
			cache = new LinkedHashMap<CompilingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?,?>>(initialCapacity, loadFactor, true) {
				@Override
				protected boolean removeEldestEntry(final Map.Entry<CompilingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?,?>> eldest) {
					return size() > maxEntries;
				}
			};
		} else
			cache = new HashMap<CompilingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?,?>>();
	}

	/**
	 * CompilingResultSetMapper with unlimited cache
	 *
	 * @param cal            optional calendar for date/time values
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 */
	public CompilingResultSetMapper(final Calendar cal, final int arrayMaxLength) {
		this(cal, arrayMaxLength, 0); // default unlimited cache
	}

	/**
	 * CompilingResultSetMapper with unlimited cache
	 *
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 */
	public CompilingResultSetMapper(final int arrayMaxLength) {
		this(null, arrayMaxLength);
	}

	/**
	 * CompilingResultSetMapper with unlimited cache
	 */
	public CompilingResultSetMapper() {
		this(-1);
	}

	/**
	 * CompilingResultSetMapper with custom cache implementation
	 *
	 * @param cal            optional calendar for date/time values
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 * @param cache          any Map implementation for cache you wish, does not need to handle null keys or values
	 */
	public CompilingResultSetMapper(final Calendar cal, final int arrayMaxLength, final Map<CompilingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?,?>> cache) {
		super(cal, arrayMaxLength);
		if (cache == null)
			throw new IllegalArgumentException("cache cannot be null");
		this.cache = cache;
	}

	/**
	 * CompilingResultSetMapper with custom cache implementation
	 *
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 * @param cache          any Map implementation for cache you wish, does not need to handle null keys or values
	 */
	public CompilingResultSetMapper(final int arrayMaxLength, final Map<CompilingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?,?>> cache) {
		this(null, arrayMaxLength, cache);
	}

	/**
	 * CompilingResultSetMapper with custom cache implementation
	 *
	 * @param cache any Map implementation for cache you wish, does not need to handle null keys or values
	 */
	public CompilingResultSetMapper(final Map<CompilingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?,?>> cache) {
		this(-1, cache);
	}

	/**
	 * CompilingResultSetMapper with optionally threadSafe cache implementation
	 *
	 * @param cal            optional calendar for date/time values
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 * @param threadSafe     true uses a thread-safe cache implementation (currently ConcurrentHashMap), false uses regular HashMap
	 */
	public CompilingResultSetMapper(final Calendar cal, final int arrayMaxLength, final boolean threadSafe) {
		this(cal, arrayMaxLength, threadSafe ?
				new ConcurrentHashMap<CompilingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?,?>>()
				:
				new HashMap<CompilingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?,?>>()
		);
	}

	/**
	 * CompilingResultSetMapper with optionally threadSafe cache implementation
	 *
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 * @param threadSafe     true uses a thread-safe cache implementation (currently ConcurrentHashMap), false uses regular HashMap
	 */
	public CompilingResultSetMapper(final int arrayMaxLength, final boolean threadSafe) {
		this(null, arrayMaxLength, threadSafe);
	}

	/**
	 * CompilingResultSetMapper with optionally threadSafe cache implementation
	 *
	 * @param threadSafe true uses a thread-safe cache implementation (currently ConcurrentHashMap), false uses regular HashMap
	 */
	public CompilingResultSetMapper(final boolean threadSafe) {
		this(-1, threadSafe);
	}

	@Override
	public <K, T> RowMapper<K, T> getRowMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		return new CompilingRowToObjectMapper<K, T>(compiler, cache, resultSet, returnTypeClass, cal, mapValType, mapKeyType);
	}
}
