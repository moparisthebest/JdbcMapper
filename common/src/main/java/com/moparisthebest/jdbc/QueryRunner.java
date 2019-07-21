package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.codegen.JdbcMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.*;

import static com.moparisthebest.jdbc.TryClose.tryClose;


/**
 * Created by mopar on 6/30/17.
 */
public class QueryRunner<T extends JdbcMapper> {

	private static final DelayStrategy defaultDelayStrategy = exponentialBackoff(1000, 30000, 2000, 10);
	private static final ExecutorService defaultExecutorService =
			Executors.newCachedThreadPool()
			//ForkJoinPool.commonPool()
			; // todo: good or bad default?

	private final Factory<T> factory;
	private final DelayStrategy delayStrategy;
	private final int retryCount;
	private final ExecutorService executorService;

	private QueryRunner(final Factory<T> factory, final int retryCount, final DelayStrategy delayStrategy, final ExecutorService executorService) {
		if (factory == null)
			throw new NullPointerException("factory must be non-null");
		if (delayStrategy == null)
			throw new NullPointerException("delayStrategy must be non-null");
		if (executorService == null)
			throw new NullPointerException("executorService must be non-null");
		if (retryCount < 0)
			throw new IllegalArgumentException("retryCount must be >= 0");
		this.factory = factory;
		this.delayStrategy = delayStrategy;
		this.retryCount = retryCount;
		this.executorService = executorService;
	}

	public static <T extends JdbcMapper> QueryRunner<T> noRetry(final Factory<T> factory, final ExecutorService executorService) {
		return new QueryRunner<T>(factory, 0, defaultDelayStrategy, executorService);
	}

	public static <T extends JdbcMapper> QueryRunner<T> noRetry(final Factory<T> factory) {
		return noRetry(factory, defaultExecutorService);
	}

	public static <T extends JdbcMapper> QueryRunner<T> withRetry(final Factory<T> factory, final int retryCount, final DelayStrategy delayStrategy, final ExecutorService executorService) {
		return new QueryRunner<T>(factory, retryCount, delayStrategy, executorService);
	}

	public static <T extends JdbcMapper> QueryRunner<T> withRetry(final Factory<T> factory, final int retryCount, final DelayStrategy delayStrategy) {
		return withRetry(factory, retryCount, delayStrategy, defaultExecutorService);
	}

	public static <T extends JdbcMapper> QueryRunner<T> withRetry(final Factory<T> factory, final int retryCount) {
		return withRetry(factory, retryCount, defaultDelayStrategy, defaultExecutorService);
	}

	public static <T extends JdbcMapper> QueryRunner<T> withRetry(final Factory<T> factory, final int retryCount, final ExecutorService executorService) {
		return withRetry(factory, retryCount, defaultDelayStrategy, executorService);
	}

	public QueryRunner<T> withRetryCount(final int retryCount) {
		return new QueryRunner<T>(factory, retryCount, delayStrategy, executorService);
	}

	public QueryRunner<T> withDelayStrategy(final DelayStrategy delayStrategy) {
		return new QueryRunner<T>(factory, retryCount, delayStrategy, executorService);
	}

	public QueryRunner<T> withExecutorService(final ExecutorService executorService) {
		return new QueryRunner<T>(factory, retryCount, delayStrategy, executorService);
	}

	public <T extends JdbcMapper> QueryRunner<T> withFactory(final Factory<T> factory) {
		return new QueryRunner<T>(factory, retryCount, delayStrategy, executorService);
	}

	public <E> E run(final Runner<T, E> query) throws SQLException {
		if (query == null)
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
		if (query == null)
			throw new NullPointerException("query must be non-null");
		T dao = null;
		try {
			dao = factory.create();
			return runInTransaction(dao, query);
		} finally {
			if (dao != null)
				tryClose(dao);
		}
	}

	/**
	 * For running existing JdbcMapper in transaction
	 * @param dao
	 * @param query
	 * @param <T>
	 * @param <E>
	 * @return
	 * @throws SQLException
	 */
	public static <T extends JdbcMapper, E> E runInTransaction(final T dao, final Runner<T, E> query) throws SQLException {
		if (query == null)
			throw new NullPointerException("query must be non-null");
		if (dao == null)
			throw new NullPointerException("dao must be non-null");
		if(!dao.getConnection().getAutoCommit()) {
			// if we are already in a transaction, the calling code will do the right thing
			// we don't want to change autoCommit, commit, or rollback
			return query.run(dao);
		} else {
			try {
				dao.getConnection().setAutoCommit(false);
				final E ret = query.run(dao);
				dao.getConnection().commit();
				return ret;
			} catch (final Throwable e) {
				try {
					dao.getConnection().rollback();
				} catch (SQLException excep) {
					// ignore to throw original
				}
				if (e instanceof SQLException)
					throw (SQLException) e;
				if (e instanceof RuntimeException)
					throw (RuntimeException) e;
				throw new RuntimeException("odd error should never happen", e);
			} finally {
				try {
					dao.getConnection().setAutoCommit(true);
				} catch (SQLException excep) {
					// ignore
				}
			}
		}
	}

	/**
	 * For running against an existing raw connection for things not implementing JdbcMapper
	 *
	 * this could construct a JdbcMapper instance with the Connection and re-use the method above,
	 * with a bit of a performance/allocation hit, we'll skip for now
	 *
	 * @param dao
	 * @param query
	 * @param <T>
	 * @param <E>
	 * @return
	 * @throws SQLException
	 */
	public static <T extends Connection, E> E runConnectionInTransaction(final T dao, final Runner<T, E> query) throws SQLException {
		if (query == null)
			throw new NullPointerException("query must be non-null");
		if (dao == null)
			throw new NullPointerException("dao must be non-null");
		if(!dao.getAutoCommit()) {
			// if we are already in a transaction, the calling code will do the right thing
			// we don't want to change autoCommit, commit, or rollback
			return query.run(dao);
		} else {
			try {
				dao.setAutoCommit(false);
				final E ret = query.run(dao);
				dao.commit();
				return ret;
			} catch (final Throwable e) {
				try {
					dao.rollback();
				} catch (SQLException excep) {
					// ignore to throw original
				}
				if (e instanceof SQLException)
					throw (SQLException) e;
				if (e instanceof RuntimeException)
					throw (RuntimeException) e;
				throw new RuntimeException("odd error should never happen", e);
			} finally {
				try {
					dao.setAutoCommit(true);
				} catch (SQLException excep) {
					// ignore
				}
			}
		}
	}

	public <E> E runRetry(final Runner<T, E> query) throws SQLException {
		int x = 0;
		while(true) {
			try {
				return runInTransaction(query);
			} catch (SQLException e) {
				if(x == retryCount)
					throw e;
				try {
					Thread.sleep(delayStrategy.getDelay(++x));
				} catch (InterruptedException e2) {
					Thread.interrupted();
				}
			}
		}
	}

	public <E> Future<E> runRetryFuture(final Runner<T, E> query) {
		// todo: sleeps in thread, could use ScheduledExecutorService maybe?
		return executorService.submit(
				//IFJAVA8_START
				() -> runRetry(query)
				//IFJAVA8_END
				/*IFJAVA6_START
				new Callable<E>() {
					@Override
					public E call() throws Exception {
						return runRetry(query);
					}
				}
				IFJAVA6_END*/
		);
	}

	//IFJAVA8_START

	public <E> CompletableFuture<E> runRetryCompletableFuture(final Runner<T, E> query) {
		// todo: sleeps in thread, could use ScheduledExecutorService maybe?
		return CompletableFuture.supplyAsync(() -> {
			try {
				return runRetry(query);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}, executorService);
	}

	//IFJAVA8_END

	public static interface Runner<T, E> {
		E run(T dao) throws SQLException;
	}

	public static <T> Runner<T, Void> voidToRunner(final VoidRunner<T> query) {
		//IFJAVA8_START
		return dao -> {
			query.run(dao);
			return null;
		};
		//IFJAVA8_END
		/*IFJAVA6_START
		return new Runner<T, Void>() {
			@Override
			public Void run(final T dao) throws SQLException {
				query.run(dao);
				return null;
			}
		};
		IFJAVA6_END*/
	}

	public static interface VoidRunner<T> {
		void run(T dao) throws SQLException;
	}

	public static interface DelayStrategy {
		long getDelay(int attempt);

		//IFJAVA8_START
		default DelayStrategy withJitter(final int maxJitterMs) {
			return QueryRunner.withJitter(this, maxJitterMs);
		}
		//IFJAVA8_END
		/*IFJAVA6_START
		DelayStrategy withJitter(final int maxJitterMs);
		IFJAVA6_END*/
	}

	public static DelayStrategy exponentialBackoff() {
		return defaultDelayStrategy;
	}

	/**
	 * Implements truncated binary exponential backoff to calculate retry delay per
	 * IEEE 802.3-2008 Section 1. There will be at most ten (10) contention periods
	 * of backoff, each contention period whose amount is equal to the delta backoff.
	 *
	 * @author Robert Buck (buck.robert.j@gmail.com)
	 */
	public static DelayStrategy exponentialBackoff(final long minBackoff, final long maxBackoff, final long slotTime, final long maxContentionPeriods) {
		return
		/*IFJAVA6_START
			new AbstractDelayStrategy() {
			@Override
			public long getDelay(final int attempt) {
				return
		IFJAVA6_END*/
		//IFJAVA8_START
			(attempt) ->
		//IFJAVA8_END
						attempt == 0 ? 0 : Math.min(minBackoff + ThreadLocalRandom.current().nextInt(2 << Math.min(attempt, maxContentionPeriods - 1)) * slotTime, maxBackoff);
		/*IFJAVA6_START
			}
		};
		IFJAVA6_END*/
	}

	public static DelayStrategy fixedDelay(final long delay) {
		return
		/*IFJAVA6_START
			new AbstractDelayStrategy() {
			@Override
			public long getDelay(final int attempt) {
				return
		IFJAVA6_END*/
		//IFJAVA8_START
			(attempt) ->
		//IFJAVA8_END
						delay;
		/*IFJAVA6_START
			}
		};
		IFJAVA6_END*/
	}

	public static DelayStrategy incrementalDelay(final long initialInterval, final long incrementalInterval) {
		return
		/*IFJAVA6_START
			new AbstractDelayStrategy() {
			@Override
			public long getDelay(final int attempt) {
				return
		IFJAVA6_END*/
		//IFJAVA8_START
			(attempt) ->
		//IFJAVA8_END
						initialInterval + incrementalInterval * attempt;
		/*IFJAVA6_START
			}
		};
		IFJAVA6_END*/
	}

	private static DelayStrategy withJitter(final DelayStrategy toWrap, final int maxJitterMs) {
		return
		/*IFJAVA6_START
			new AbstractDelayStrategy() {
			@Override
			public long getDelay(final int attempt) {
				return
		IFJAVA6_END*/
		//IFJAVA8_START
				(attempt) ->
		//IFJAVA8_END
						toWrap.getDelay(attempt) + ThreadLocalRandom.current().nextInt(maxJitterMs);
		/*IFJAVA6_START
			}
		};
		IFJAVA6_END*/
	}

	/*IFJAVA6_START
	// terrible, I know, use java8
	private static class ThreadLocalRandom {
		private static final ThreadLocal<java.util.Random> randomThreadLocal = new ThreadLocal<java.util.Random>() {
			@Override
			protected java.util.Random initialValue() {
				return new java.util.Random();
			}
		};

		private static java.util.Random current() {
			return randomThreadLocal.get();
		}
	}
	private static abstract class AbstractDelayStrategy implements DelayStrategy {
		public DelayStrategy withJitter(final int maxJitterMs) {
			return QueryRunner.withJitter(this, maxJitterMs);
		}
	}
	IFJAVA6_END*/

}
