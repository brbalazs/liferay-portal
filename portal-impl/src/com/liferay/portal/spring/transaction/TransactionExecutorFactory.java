/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.transaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;

/**
 * @author Shuyang Zhou
 */
public class TransactionExecutorFactory {

	public static TransactionExecutor createTransactionExecutor(
		PlatformTransactionManager platformTransactionManager,
		boolean counter) {

		if (counter) {
			if (platformTransactionManager instanceof
					CallbackPreferringPlatformTransactionManager) {

				return new CounterCallbackPreferringTransactionExecutor(
					platformTransactionManager);
			}

			return new CounterTransactionExecutor(platformTransactionManager);
		}

		if (platformTransactionManager instanceof
				CallbackPreferringPlatformTransactionManager) {

			return new CallbackPreferringTransactionExecutor(
				platformTransactionManager);
		}

		return new DefaultTransactionExecutor(platformTransactionManager);
	}

}