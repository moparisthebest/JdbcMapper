package com.moparisthebest.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import static com.moparisthebest.jdbc.InList.defaultInList;

public class ListQueryMapper extends QueryMapper {

	protected final QueryMapper delegate;
	protected final boolean closeDelegate;

	public static final String inListReplace = "{inList}";

	protected ListQueryMapper(Connection conn, String jndiName, Factory<Connection> factory, final QueryMapper d, ResultSetMapper cm, InList inList, boolean closeDelegate) {
		super(d == null ? conn : d.conn, jndiName, factory, d == null ? cm : d.cm, d == null ? inList : d.inList);
		this.closeDelegate = closeDelegate;
		this.delegate = d;
	}

	protected ListQueryMapper(Connection conn, String jndiName, Factory<Connection> factory, final QueryMapper d, ResultSetMapper cm, InList inList) {
		this(conn, jndiName, factory, d, cm, inList, false);
	}

	protected ListQueryMapper(Connection conn, String jndiName, Factory<Connection> factory, QueryMapper delegate, ResultSetMapper cm) {
		this(conn, jndiName, factory, delegate, cm, defaultInList);
	}

	public ListQueryMapper(InList inList, QueryMapper delegate, boolean closeDelegate) {
		this(null, null, null, delegate, null, inList, closeDelegate);
	}

	public ListQueryMapper(QueryMapper delegate, InList inList) {
		this(null, null, null, delegate, null, inList);
	}

	public ListQueryMapper(QueryMapper delegate) {
		this(null, null, null, delegate, null, defaultInList);
	}

	public ListQueryMapper(Connection conn, InList inList) {
		this(conn, null, null, null, null, inList);
	}

	public ListQueryMapper(Connection conn, ResultSetMapper cm, InList inList) {
		this(conn, null, null, null, cm, inList);
	}

	public ListQueryMapper(Connection conn) {
		this(conn, defaultInList);
	}

	public ListQueryMapper(Connection conn, ResultSetMapper cm) {
		this(conn, cm, defaultInList);
	}

	public ListQueryMapper(String jndiName, InList inList) {
		this(null, jndiName, null, null, null, inList);
	}

	public ListQueryMapper(String jndiName, ResultSetMapper cm, InList inList) {
		this(null, jndiName, null, null, cm, inList);
	}

	public ListQueryMapper(String jndiName) {
		this(jndiName, defaultInList);
	}

	public ListQueryMapper(String jndiName, ResultSetMapper cm) {
		this(jndiName, cm, defaultInList);
	}

	public ListQueryMapper(Factory<Connection> factory, InList inList) {
		this(null, null, factory, null, null, inList);
	}

	public ListQueryMapper(Factory<Connection> factory, ResultSetMapper cm, InList inList) {
		this(null, null, factory, null, cm, inList);
	}

	public ListQueryMapper(Factory<Connection> factory) {
		this(factory, defaultInList);
	}

	public ListQueryMapper(Factory<Connection> factory, ResultSetMapper cm) {
		this(factory, cm, defaultInList);
	}

	public static ListQueryMapper of(final Factory<QueryMapper> qmFactory, final InList inList) throws SQLException {
		return new ListQueryMapper(inList, qmFactory.create(), true);
	}

	public static ListQueryMapper of(final QueryMapper qm, final InList inList) {
		return new ListQueryMapper(inList, qm, false);
	}

	public static ListQueryMapper of(final Factory<QueryMapper> qmFactory) throws SQLException {
		return of(qmFactory, defaultInList);
	}

	public static ListQueryMapper of(final QueryMapper qm) {
		return of(qm, defaultInList);
	}

	@Override
	public void close() {
		if(closeDelegate)
			delegate.close();
	}
}

