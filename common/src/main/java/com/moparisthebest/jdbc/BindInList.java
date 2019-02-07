package com.moparisthebest.jdbc;

import java.sql.Connection;
import java.util.*;

import static com.moparisthebest.jdbc.util.InListUtil.defaultMaxSize;
import static com.moparisthebest.jdbc.util.InListUtil.toInList;
import static com.moparisthebest.jdbc.util.InListUtil.toNotInList;

/**
 * Created by mopar on 4/29/15.
 */
public class BindInList implements InList {

	private static final InList instance = new BindInList();

	public static InList instance() {
		return instance;
	}

	private final int maxSize;

	public BindInList(final int maxSize) {
		this.maxSize = maxSize;
	}

	protected BindInList() {
		this(defaultMaxSize);
	}

	@Override
	public InList instance(Connection conn) {
		return this;
	}

	public <T> InListObject inList(final Connection conn, final String columnName, final Collection<T> values) {
		return values == null || values.isEmpty() ? InListObject.inEmpty : new InListObject(
				toInList(columnName, values, this.maxSize),
				values
		);
	}

	public <T> InListObject notInList(final Connection conn, final String columnName, final Collection<T> values) {
		return values == null || values.isEmpty() ? InListObject.notInEmpty : new InListObject(
				toNotInList(columnName, values, this.maxSize),
				values
		);
	}
}
