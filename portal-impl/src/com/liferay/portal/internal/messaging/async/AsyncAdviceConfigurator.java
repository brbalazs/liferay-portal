/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.internal.messaging.async;

import com.liferay.portal.messaging.async.AsyncAdvice;

import java.util.Map;

/**
 * @author Shuyang Zhou
 */
public class AsyncAdviceConfigurator {

	public void afterPropertiesSet() {
		if (_asyncAdvice == null) {
			throw new IllegalArgumentException("Async advice is null");
		}

		if (_defaultDestinationName == null) {
			throw new IllegalArgumentException(
				"Default destination name is null");
		}

		_asyncAdvice.setDefaultDestinationName(_defaultDestinationName);

		if (_destinationNames != null) {
			_asyncAdvice.setDestinationNames(_destinationNames);
		}
	}

	public void setAsyncAdvice(AsyncAdvice asyncAdvice) {
		_asyncAdvice = asyncAdvice;
	}

	public void setDefaultDestinationName(String defaultDestinationName) {
		_defaultDestinationName = defaultDestinationName;
	}

	public void setDestinationNames(Map<Class<?>, String> destinationNames) {
		_destinationNames = destinationNames;
	}

	private AsyncAdvice _asyncAdvice;
	private String _defaultDestinationName;
	private Map<Class<?>, String> _destinationNames;

}