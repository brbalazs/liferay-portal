/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.transaction;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Young
 * @author Shuyang Zhou
 */
public enum Isolation {

	COUNTER(TransactionDefinition.ISOLATION_COUNTER),
	DEFAULT(TransactionDefinition.ISOLATION_DEFAULT),
	PORTAL(TransactionDefinition.ISOLATION_PORTAL),
	READ_COMMITTED(TransactionDefinition.ISOLATION_READ_COMMITTED),
	READ_UNCOMMITTED(TransactionDefinition.ISOLATION_READ_UNCOMMITTED),
	REPEATABLE_READ(TransactionDefinition.ISOLATION_REPEATABLE_READ),
	SERIALIZABLE(TransactionDefinition.ISOLATION_SERIALIZABLE);

	public static Isolation getIsolation(int value) {
		return _isolations.get(value);
	}

	public int value() {
		return _value;
	}

	private Isolation(int value) {
		_value = value;
	}

	private static final Map<Integer, Isolation> _isolations =
		new HashMap<Integer, Isolation>() {
			{
				for (Isolation isolation : EnumSet.allOf(Isolation.class)) {
					put(isolation._value, isolation);
				}
			}
		};

	private final int _value;

}