package com.moparisthebest.jdbc.util;

import java.io.Closeable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.NoSuchElementException;
//IFJAVA8_START
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
//IFJAVA8_END

import static com.moparisthebest.jdbc.TryClose.tryClose;

/**
 * Iterable/Iterator interface over a ResultSet
 * <p>
 * This has some special semantics the caller must be aware of though:
 * <p>
 * 1. It has to be closed in a finally just like a ResultSet so it can close it's underlying ResultSet
 * 2. The .iterator() implementation just returns this, meaning you can only loop over it once, if you need to loop multiple times get a List or something
 * 3. This guarantees that the ResultSet/Calendar you send into this class with the ResultSetToObject will be the only
 * instances sent into the ResultSetToObject.toObject() method, so if you do fancy initialization of some sort you can
 * do it in the constructor based on those instances, or once on the first call to .toObject() and cache it forever.
 * Or it can be entirely stateless and re-used across multiple ResultSets, your choice.
 * 4. If you set a PreparedStatement to close, you can only set it once, and it will close it when you call .close() on this
 */
public class ResultSetIterable<T> implements Iterable<T>, Iterator<T>, Closeable {

	public static final ResultSetIterable EMPTY_RESULT_SET_ITERABLE = new EmptyResultSetIterable();

	@SuppressWarnings("unchecked")
	public static <T> ResultSetIterable<T> emptyResultSetIterable() {
		return (ResultSetIterable<T>) EMPTY_RESULT_SET_ITERABLE;
	}

	public static <T> ResultSetIterable<T> getResultSetIterable(final ResultSet rs, final ResultSetToObject<T> rsto) {
		return getResultSetIterable(rs, rsto, null);
	}

	/**
	 * This is a convenience method meant to be called like this, where rs is a ResultSet
	 * <p>
	 * ResultSetIterable<T> rsi = ResultSetIterable.getResultSetIterable(rs, rs.next() ? complicatedBuildResultSetToObject(rs) : null, cal);
	 * <p>
	 * This way you can avoid building or sending in a ResultSetToObject all together if there are no rows, therefore if rsto
	 * sent into this is null, it returns an EMPTY_RESULT_SET_ITERABLE and closes rs immediately
	 * <p>
	 * This assumes rs.next() was called once before sent into this function
	 */
	@SuppressWarnings("unchecked")
	public static <T> ResultSetIterable<T> getResultSetIterable(final ResultSet rs, final ResultSetToObject<T> rsto, final Calendar cal) {
		if (rsto == null) {
			tryClose(rs); // have to do this here...
			return (ResultSetIterable<T>) EMPTY_RESULT_SET_ITERABLE;
		}
		final ResultSetIterable<T> ret = new ResultSetIterable<T>(rs, rsto, cal);
		ret.calledNext = true;
		return ret;
	}

	/**
	 * This is a convenience method meant to be called like this, where rs is a ResultSet
	 * <p>
	 * Stream<T> rsi = ResultSetIterable.getStream(rs, rs.next() ? complicatedBuildResultSetToObject(rs) : null, cal);
	 * <p>
	 * This way you can avoid building or sending in a ResultSetToObject all together if there are no rows, therefore if rsto
	 * sent into this is null, it returns an empty Stream, rs might be closed before returning the empty Stream or on .close() to the Stream
	 * <p>
	 * The stream returned MUST be closed in a try-with-resources or finally because the ResultSet is held open until then
	 * <p>
	 * This assumes rs.next() was called once before sent into this function
	 */
	//IFJAVA8_START
	@SuppressWarnings("unchecked")
	public static <T> Stream<T> getStream(final ResultSet rs, final ResultSetToObject<T> rsto, final Calendar cal) {
		final Stream<T> ret;
		if (rsto == null) {
			tryClose(rs); // have to do this here...
			// todo: static object for this?
			return (Stream<T>) StreamSupport.stream(Spliterators.emptySpliterator(), false);
		} else {
			final ResultSetIterable<T> rsi = new ResultSetIterable<T>(rs, rsto, cal);
			rsi.calledNext = true;
			ret = StreamSupport.stream(rsi.spliterator(), false);
		}
		return ret.onClose(() -> tryClose(rs));
	}
	//IFJAVA8_END

	private final ResultSet rs;
	private final Calendar cal;
	private final ResultSetToObject<T> rsto;

	private boolean calledNext = false;
	private PreparedStatement ps = null;

	protected ResultSetIterable() {
		this.rs = null;
		this.cal = null;
		this.rsto = null;
	}

	public ResultSetIterable(final ResultSet rs, final ResultSetToObject<T> rsto) {
		this(rs, rsto, null);
	}

	public ResultSetIterable(final ResultSet rs, final ResultSetToObject<T> rsto, final Calendar cal) {
		if (rs == null || rsto == null)
			throw new NullPointerException("rs and rsto must be non-null");
		this.rs = rs;
		this.cal = cal;
		this.rsto = rsto;
	}

	public ResultSetIterable<T> setPreparedStatementToClose(final PreparedStatement ps) {
		if (this.ps != null)
			throw new IllegalStateException("can only set PreparedStatement to close once");
		this.ps = ps;
		return this;
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		if (calledNext)
			return true;
		try {
			calledNext = true;
			return rs.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public T next() {
		try {
			if (!calledNext)
				rs.next();
			else
				calledNext = false;
			return rsto.toObject(rs, cal);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove unsupported");
	}

	@Override
	public void close() {
		tryClose(rs);
		tryClose(ps);
	}

	private static class EmptyResultSetIterable extends ResultSetIterable {
		@Override
		public ResultSetIterable setPreparedStatementToClose(final PreparedStatement ps) {
			tryClose(ps);
			return this;
		}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public Object next() {
			throw new NoSuchElementException("empty ResultSet");
		}

		@Override
		public void close() {
			// do nothing
		}
	}
}
