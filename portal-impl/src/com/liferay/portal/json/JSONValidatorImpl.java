/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.json;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONValidator;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author Pablo Carvalho
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class JSONValidatorImpl implements JSONValidator {

	public JSONValidatorImpl(String json) {
	}

	@Override
	public boolean isValid(String json) {
		if (Validator.isNull(json)) {
			return false;
		}

		try {
			json = json.trim();

			if (json.startsWith("{")) {
				new JSONObjectImpl(json);

				return true;
			}
			else if (json.startsWith("[")) {
				new JSONArrayImpl(json);

				return true;
			}
		}
		catch (JSONException jsone) {
		}

		return false;
	}

}