package com.moparisthebest.jdbc;

import java.sql.Connection;
import java.util.*;

/**
 * Created by mopar on 4/29/15.
 */
public class BindInList implements InList {

	private static final int defaultMaxSize = Integer.parseInt(System.getProperty("QueryMapper.BindInList.defaultMaxSize", "999"));

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

	private static <T> List<List<T>> split(final List<T> list, final int maxLength) {
		final int listSize = list.size();
		final List<List<T>> ret = new ArrayList<List<T>>();
		if (listSize < maxLength)
			ret.add(list);
		else
			for (int fromIndex = 0, toIndex = maxLength; fromIndex < listSize; fromIndex = toIndex, toIndex += maxLength)
				ret.add(list.subList(fromIndex, toIndex > listSize ? listSize : toIndex));
		return ret;
	}

	private <T> String toInList(final String fieldName, final Collection<T> items) {
		final StringBuilder sb = new StringBuilder("(");

		// do it quick if it will fit in one in-list
		if (items.size() < maxSize) // 999 or less
			return buildInList(items, sb, fieldName).append(")").toString();

		// else we need to split lists
		boolean notFirst = false;
		for (final List<T> item : split(new ArrayList<T>(items), maxSize)) {
			if (notFirst) sb.append(" OR ");
			else notFirst = true;
			buildInList(item, sb, fieldName);
		}

		return sb.append(")").toString();
	}

	private static <T> StringBuilder buildInList(Iterable<T> list, StringBuilder sb, String fieldName) {
		return oracleCommaSeparatedList(list, sb.append(fieldName).append(" IN (")).append(")");
	}

	private static <T> StringBuilder oracleCommaSeparatedList(Iterable<T> list, StringBuilder sb) {
		boolean notFirst = false;
		for (final T obj : list) {
			// DO NOT DO THIS ANYMORE: if (obj == null) continue;
			if (notFirst) sb.append(',');
			else notFirst = true;
			sb.append('?');
		}
		return sb;
	}

	public <T> InListObject inList(final Connection conn, final String columnName, final Collection<T> values) {
		return values == null || values.isEmpty() ? InListObject.empty : new BindInListObject(
				toInList(columnName, values),
				values.toArray()
		);
	}

	class BindInListObject extends InListObject {
		private final Object[] bindObjects;

		public BindInListObject(final String sql, final Object[] bindObjects) {
			super(sql);
			this.bindObjects = bindObjects;
		}

		public Object[] getBindObjects() {
			return bindObjects;
		}
	}
}
