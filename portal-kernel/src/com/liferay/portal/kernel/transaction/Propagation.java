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
public enum Propagation {

	MANDATORY(TransactionDefinition.PROPAGATION_MANDATORY),
	NESTED(TransactionDefinition.PROPAGATION_NESTED),
	NEVER(TransactionDefinition.PROPAGATION_NEVER),
	NOT_SUPPORTED(TransactionDefinition.PROPAGATION_NOT_SUPPORTED),
	REQUIRED(TransactionDefinition.PROPAGATION_REQUIRED),
	REQUIRES_NEW(TransactionDefinition.PROPAGATION_REQUIRES_NEW),
	SUPPORTS(TransactionDefinition.PROPAGATION_SUPPORTS);

	public static Propagation getPropagation(int value) {
		return _propagations.get(value);
	}

	public int value() {
		return _value;
	}

	private Propagation(int value) {
		_value = value;
	}

	private static final Map<Integer, Propagation> _propagations =
		new HashMap<Integer, Propagation>() {
			{
				for (Propagation propagation :
						EnumSet.allOf(Propagation.class)) {

					put(propagation._value, propagation);
				}
			}
		};

	private final int _value;

}