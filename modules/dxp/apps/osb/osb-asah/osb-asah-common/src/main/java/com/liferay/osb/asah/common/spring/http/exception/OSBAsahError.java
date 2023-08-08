/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.http.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Leslie Wong
 */
public class OSBAsahError {

	public OSBAsahError(String[] activeProfiles) {
		List<String> activeProfilesList = Arrays.asList(activeProfiles);

		if (activeProfilesList.contains("dev") ||
			activeProfilesList.contains("test")) {

			_errorAttributeNames.add("debugInfo");
			_errorAttributeNames.add("error");
			_errorAttributeNames.add("errors");
			_errorAttributeNames.add("exception");
			_errorAttributeNames.add("message");
			_errorAttributeNames.add("messageKey");
			_errorAttributeNames.add("path");
			_errorAttributeNames.add("status");
			_errorAttributeNames.add("timestamp");
			_errorAttributeNames.add("trace");
		}
		else {
			_errorAttributeNames.add("error");
			_errorAttributeNames.add("messageKey");
			_errorAttributeNames.add("path");
			_errorAttributeNames.add("status");
			_errorAttributeNames.add("timestamp");
		}
	}

	public Map<String, Object> getErrorAttributes() {
		Map<String, Object> errorAttributes = new HashMap<>();

		for (String errorAttributeName : _errorAttributeNames) {
			errorAttributes.put(
				errorAttributeName, _errorAttributes.get(errorAttributeName));
		}

		return errorAttributes;
	}

	public void setErrorAttribute(String attributeName, Object attributeValue) {
		_errorAttributes.put(attributeName, attributeValue);
	}

	public void setErrorAttributes(Map<String, Object> errorAttributes) {
		_errorAttributes = errorAttributes;
	}

	private final List<String> _errorAttributeNames = new ArrayList<>();
	private Map<String, Object> _errorAttributes = new HashMap<>();

}