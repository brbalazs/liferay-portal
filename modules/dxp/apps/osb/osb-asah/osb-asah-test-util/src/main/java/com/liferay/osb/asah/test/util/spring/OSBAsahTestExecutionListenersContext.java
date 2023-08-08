/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.spring;

import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author Alejo Ceballos
 */
@TestExecutionListeners(
	mergeMode = TestExecutionListeners.MergeMode.REPLACE_DEFAULTS,
	value = {
		DependencyInjectionTestExecutionListener.class,
		MockitoTestExecutionListener.class,
		OSBAsahBQSQLTestExecutionListener.class,
		OSBAsahMessageBusTestExecutionListener.class,
		OSBAsahRepositoryTestExecutionListener.class,
		OSBAsahSQLTestExecutionListener.class,
		ResetMocksTestExecutionListener.class
	}
)
public interface OSBAsahTestExecutionListenersContext {
}