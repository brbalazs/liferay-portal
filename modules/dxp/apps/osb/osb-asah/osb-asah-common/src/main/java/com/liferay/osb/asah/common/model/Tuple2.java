/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

/**
 * @author Marcellus Tavares
 */
public class Tuple2<T1, T2> {

	public Tuple2(T1 t1, T2 t2) {
		_t1 = t1;
		_t2 = t2;
	}

	public T1 getT1() {
		return _t1;
	}

	public T2 getT2() {
		return _t2;
	}

	private final T1 _t1;
	private final T2 _t2;

}