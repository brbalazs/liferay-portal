/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.transaction;

import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.portal.kernel.transaction.TransactionAttribute;
import com.liferay.portal.kernel.transaction.TransactionLifecycleListener;
import com.liferay.portal.kernel.transaction.TransactionStatus;

import java.util.ArrayDeque;
import java.util.Deque;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Shuyang Zhou
 * @deprecated As of Judson (7.1.x), replaced by {@link TransactionExecutorThreadLocal}
 */
@Deprecated
public class CurrentPlatformTransactionManagerUtil {

	public static final TransactionLifecycleListener
		TRANSACTION_LIFECYCLE_LISTENER = new TransactionLifecycleListener() {

			@Override
			public void committed(
				TransactionAttribute transactionAttribute,
				TransactionStatus transactionStatus) {

				Deque<PlatformTransactionManager> platformTransactionManagers =
					_platformTransactionManagersThreadLocal.get();

				platformTransactionManagers.pop();
			}

			@Override
			public void created(
				TransactionAttribute transactionAttribute,
				TransactionStatus transactionStatus) {

				Deque<PlatformTransactionManager> platformTransactionManagers =
					_platformTransactionManagersThreadLocal.get();

				platformTransactionManagers.push(
					(PlatformTransactionManager)
						transactionStatus.getPlatformTransactionManager());
			}

			@Override
			public void rollbacked(
				TransactionAttribute transactionAttribute,
				TransactionStatus transactionStatus, Throwable throwable) {

				Deque<PlatformTransactionManager> platformTransactionManagers =
					_platformTransactionManagersThreadLocal.get();

				platformTransactionManagers.pop();
			}

		};

	public static PlatformTransactionManager
		getCurrentPlatformTransactionManager() {

		Deque<PlatformTransactionManager> platformTransactionManagers =
			_platformTransactionManagersThreadLocal.get();

		return platformTransactionManagers.peek();
	}

	private static final ThreadLocal<Deque<PlatformTransactionManager>>
		_platformTransactionManagersThreadLocal = new CentralizedThreadLocal<>(
			CurrentPlatformTransactionManagerUtil.class +
				"._platformTransactionManagersThreadLocal",
			ArrayDeque::new, false);

}