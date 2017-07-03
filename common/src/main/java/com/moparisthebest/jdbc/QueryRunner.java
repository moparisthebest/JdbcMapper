package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.codegen.JdbcMapper;

import java.sql.SQLException;
import java.util.concurrent.*;

import static com.moparisthebest.jdbc.TryClose.tryClose;


/**
 * Created by mopar on 6/30/17.
 */
public class QueryRunner<T extends JdbcMapper> {

	private static final int defaultRetryCount = 10;
	private static final DelayStrategy defaultDelayStrategy = exponentialBackoff(1000, 30000, 2000, 10);
	private static final ExecutorService defaultExecutorService = Executors.newCachedThreadPool(); // todo: good or bad default?

	private final Factory<T> factory;
	private final DelayStrategy delayStrategy;
	private final int retryCount;
	private final ExecutorService executorService;

	public QueryRunner(final Factory<T> factory, final DelayStrategy delayStrategy, final ExecutorService executorService, final int retryCount) {
		if (factory == null)
			throw new NullPointerException("factory must be non-null");
		if (delayStrategy == null)
			throw new NullPointerException("delayStrategy must be non-null");
		if (executorService == null)
			throw new NullPointerException("executorService must be non-null");
		if (retryCount < 1)
			throw new IllegalArgumentException("retryCount must be > 0");
		this.factory = factory;
		this.delayStrategy = delayStrategy;
		this.retryCount = retryCount;
		this.executorService = Executors.newSingleThreadExecutor();
	}

	public QueryRunner(final Factory<T> factory) {
		this(factory, defaultDelayStrategy, defaultExecutorService, defaultRetryCount);
	}

	public QueryRunner(final Factory<T> factory, final DelayStrategy delayStrategy) {
		this(factory, delayStrategy, defaultExecutorService, defaultRetryCount);
	}

	public QueryRunner(final Factory<T> factory, final int retryCount) {
		this(factory, defaultDelayStrategy, defaultExecutorService, retryCount);
	}

	public QueryRunner(final Factory<T> factory, final ExecutorService executorService) {
		this(factory, defaultDelayStrategy, executorService, defaultRetryCount);
	}

	public QueryRunner(final Factory<T> factory, final DelayStrategy delayStrategy, final ExecutorService executorService) {
		this(factory, delayStrategy, executorService, defaultRetryCount);
	}

	public QueryRunner(final Factory<T> factory, final ExecutorService executorService, final int retryCount) {
		this(factory, defaultDelayStrategy, executorService, retryCount);
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
			dao.getConnection().setAutoCommit(false);
			final E ret = query.run(dao);
			dao.getConnection().commit();
			return ret;
		} catch (final Throwable e) {
			if (dao != null) {
				try {
					dao.getConnection().rollback();
				} catch (SQLException excep) {
					// ignore to throw original
				}
			}
			if (e instanceof SQLException)
				throw (SQLException) e;
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			throw new RuntimeException("odd error should never happen", e);
		} finally {
			if (dao != null) {
				try {
					dao.getConnection().setAutoCommit(true);
				} catch (SQLException excep) {
					// ignore
				}
				tryClose(dao);
			}
		}
	}

	public <E> E runRetry(final Runner<T, E> query) throws SQLException {
		SQLException lastException = null;
		int x = 0;
		do {
			try {
				return runInTransaction(query);
			} catch (SQLException e) {
				lastException = e;
				try {
					Thread.sleep(delayStrategy.getDelay(++x));
				} catch (InterruptedException e2) {
					Thread.interrupted();
				}
			}
		} while (x <= retryCount);
		throw lastException;
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

	public static interface DelayStrategy {
		long getDelay(int attempt);
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
			new DelayStrategy() {
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
			new DelayStrategy() {
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
			new DelayStrategy() {
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
	IFJAVA6_END*/

}
