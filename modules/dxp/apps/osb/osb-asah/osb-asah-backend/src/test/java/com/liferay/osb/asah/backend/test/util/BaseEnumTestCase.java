/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.test.util;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public abstract class BaseEnumTestCase<T> {

	@Test
	public void testValueOf() throws Exception {
		Class<? extends Enum<?>> clazz = getClazz();

		Method valuesMethod = clazz.getMethod("values");

		for (Object o : (Object[])valuesMethod.invoke(null)) {
			Method valueOfMethod = clazz.getMethod("valueOf", String.class);

			valueOfMethod.invoke(null, o.toString());
		}
	}

	protected abstract Class<? extends Enum<?>> getClazz();

}