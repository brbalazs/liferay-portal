/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import java.lang.reflect.Method;

/**
 * @author Inácio Nery
 */
public class GetterAndSetterPair {

	public Method getGetMethod() {
		return _getMethod;
	}

	public Method getSetMethod() {
		return _setMethod;
	}

	public boolean hasGetMethodAndSetMethod() {
		if ((_getMethod != null) && (_setMethod != null)) {
			return true;
		}

		return false;
	}

	public void setGetMethod(Method getMethod) {
		_getMethod = getMethod;
	}

	public void setSetMethod(Method setMethod) {
		_setMethod = setMethod;
	}

	private Method _getMethod;
	private Method _setMethod;

}