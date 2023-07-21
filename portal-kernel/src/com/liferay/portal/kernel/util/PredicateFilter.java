/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

/**
 * @author Carlos Sierra Andrés
 */
public interface PredicateFilter<T> {

	@SuppressWarnings("rawtypes")
	public static PredicateFilter ALL = new PredicateFilter<Object>() {

		@Override
		public boolean filter(Object object) {
			return true;
		}

	};

	@SuppressWarnings("rawtypes")
	public static PredicateFilter NONE = new PredicateFilter<Object>() {

		@Override
		public boolean filter(Object object) {
			return false;
		}

	};

	public boolean filter(T t);

}