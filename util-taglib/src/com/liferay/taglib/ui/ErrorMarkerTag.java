/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.taglib.ui;

import com.liferay.portal.kernel.util.Validator;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 */
public class ErrorMarkerTag extends IncludeTag {

	public void setKey(String key) {
		_key = key;
	}

	public void setValue(String value) {
		_value = value;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_key = null;
		_value = null;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		if (Validator.isNotNull(_key) && Validator.isNotNull(_value)) {
			request.setAttribute("liferay-ui:error-marker:key", _key);
			request.setAttribute("liferay-ui:error-marker:value", _value);
		}
		else {
			request.removeAttribute("liferay-ui:error-marker:key");
			request.removeAttribute("liferay-ui:error-marker:value");
		}
	}

	private String _key;
	private String _value;

}