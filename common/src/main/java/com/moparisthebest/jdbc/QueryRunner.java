package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.codegen.JdbcMapper;

import java.sql.SQLException;

import static com.moparisthebest.jdbc.TryClose.tryClose;


/**
 * Created by mopar on 6/30/17.
 */
public class QueryRunner<T extends JdbcMapper> {

	private final Factory<T> factory;

	public QueryRunner(final Factory<T> factory) {
		if(factory == null)
			throw new NullPointerException("factory must be non-null");
		this.factory = factory;
	}

	public <E> E run(final Runner<T, E> query) throws SQLException {
		if(query == null)
			throw new NullPointerException("query must be non-null");
		T dao = null;
		try {
			dao = factory.create();
			return query.run(dao);
		} finally {
			tryClose(dao);
		}
	}

	public <E> E runInTransaction(final Runner<T, E> query) throws SQLException {
		if(query == null)
			throw new NullPointerException("query must be non-null");
		T dao = null;
		try {
			dao = factory.create();
			dao.getConnection().setAutoCommit(false);
			final E ret = query.run(dao);
			dao.getConnection().commit();
			return ret;
		} catch (final Throwable e) {
			if (dao != null) {
				try {
					dao.getConnection().rollback();
				} catch(SQLException excep) {
					// ignore to throw original
				}
			}
			if(e instanceof SQLException)
				throw (SQLException) e;
			if(e instanceof RuntimeException)
				throw (RuntimeException) e;
			throw new RuntimeException("odd error should never happen", e);
		} finally {
			if (dao != null) {
				try {
					dao.getConnection().setAutoCommit(true);
				} catch(SQLException excep) {
					// ignore
				}
				tryClose(dao);
			}
		}
	}

	public static interface Runner<T, E> {
		E run(T dao) throws SQLException;
	}
}
