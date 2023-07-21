/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.transaction;

import aQute.bnd.annotation.ProviderType;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Shuyang Zhou
 */
@ProviderType
public interface TransactionHandler {

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link #commit(
	 *             TransactionAttributeAdapter, TransactionStatusAdapter)}
	 */
	@Deprecated
	public void commit(
		PlatformTransactionManager platformTransactionManager,
		TransactionAttributeAdapter transactionAttributeAdapter,
		TransactionStatusAdapter transactionStatusAdapter);

	public void commit(
		TransactionAttributeAdapter transactionAttributeAdapter,
		TransactionStatusAdapter transactionStatusAdapter);

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link #rollback(
	 *             Throwable, TransactionAttributeAdapter,
	 *             TransactionStatusAdapter)}
	 */
	@Deprecated
	public void rollback(
			PlatformTransactionManager platformTransactionManager,
			Throwable throwable,
			TransactionAttributeAdapter transactionAttributeAdapter,
			TransactionStatusAdapter transactionStatusAdapter)
		throws Throwable;

	public void rollback(
			Throwable throwable,
			TransactionAttributeAdapter transactionAttributeAdapter,
			TransactionStatusAdapter transactionStatusAdapter)
		throws Throwable;

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link #start(
	 *             TransactionAttributeAdapter)}
	 */
	@Deprecated
	public TransactionStatusAdapter start(
		PlatformTransactionManager platformTransactionManager,
		TransactionAttributeAdapter transactionAttributeAdapter);

	public TransactionStatusAdapter start(
		TransactionAttributeAdapter transactionAttributeAdapter);

}