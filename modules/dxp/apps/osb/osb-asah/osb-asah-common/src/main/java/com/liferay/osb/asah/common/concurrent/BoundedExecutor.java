/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.Assert;

/**
 * @author Marcellus Tavares
 */
public class BoundedExecutor {

	public static BoundedExecutor newBoundedExecutor(
		int maxThreads, int threadPoolSize) {

		Assert.state(
			threadPoolSize > 0, "Thread pool size must be greater than 0");

		if (maxThreads < threadPoolSize) {
			maxThreads = threadPoolSize;
		}

		return new BoundedExecutor(
			Executors.newFixedThreadPool(threadPoolSize), maxThreads);
	}

	public void awaitPendingTasks() {
		try {
			_semaphore.acquireUninterruptibly(_maxThreads);
		}
		finally {
			_semaphore.release(_maxThreads);
		}
	}

	public int countAvailableTasks() {
		return _semaphore.availablePermits();
	}

	public int countPendingTasks() {
		return _semaphore.getQueueLength();
	}

	public void runAsync(Runnable asyncRunnable) {
		runAsync(asyncRunnable, null);
	}

	public void runAsync(Runnable asyncRunnable, ReentrantLock reentrantLock) {
		Assert.notNull(asyncRunnable, "The asynchronous runnable is null");

		_semaphore.acquireUninterruptibly();

		try {
			_executorService.execute(
				() -> {
					try {
						if (reentrantLock != null) {
							reentrantLock.lock();
						}

						asyncRunnable.run();
					}
					catch (Exception exception) {
						_log.error(
							"Uncaught exception on the asynchronous runnable",
							exception);
					}
					finally {
						if (reentrantLock != null) {
							reentrantLock.unlock();
						}

						_semaphore.release();
					}
				});
		}
		catch (RejectedExecutionException rejectedExecutionException) {
			_log.error(rejectedExecutionException, rejectedExecutionException);

			_semaphore.release();
		}
	}

	public void shutdown() {
		_executorService.shutdown();

		try {
			if (!_executorService.awaitTermination(1, TimeUnit.MINUTES)) {
				_executorService.shutdownNow();
			}
		}
		catch (InterruptedException interruptedException) {
			_log.error(
				"Interrupted while waiting for termination of executor",
				interruptedException);
		}
	}

	private BoundedExecutor(ExecutorService executorService, int maxThreads) {
		_executorService = executorService;
		_maxThreads = maxThreads;

		_semaphore = new Semaphore(maxThreads, true);
	}

	private static final Log _log = LogFactory.getLog(BoundedExecutor.class);

	private final ExecutorService _executorService;
	private final int _maxThreads;
	private final Semaphore _semaphore;

}