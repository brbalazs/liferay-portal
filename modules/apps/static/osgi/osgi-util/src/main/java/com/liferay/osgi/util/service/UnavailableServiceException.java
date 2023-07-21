/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osgi.util.service;

/**
 * @author Carlos Sierra Andrés
 */
public class UnavailableServiceException extends RuntimeException {

	public UnavailableServiceException(Class<?> unavailableServiceClass) {
		super(unavailableServiceClass.toString());

		_unavailableServiceClass = unavailableServiceClass;
	}

	public Class<?> getUnavailableServiceClass() {
		return _unavailableServiceClass;
	}

	private final Class<?> _unavailableServiceClass;

}