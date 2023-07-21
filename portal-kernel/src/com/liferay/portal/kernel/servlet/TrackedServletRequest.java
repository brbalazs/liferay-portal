/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.servlet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author     Brian Wing Shun Chan
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class TrackedServletRequest extends HttpServletRequestWrapper {

	public TrackedServletRequest(HttpServletRequest request) {
		super(request);
	}

	public Set<String> getSetAttributes() {
		if (_setAttributes == null) {
			return Collections.emptySet();
		}

		return _setAttributes;
	}

	@Override
	public void setAttribute(String name, Object obj) {
		if (_setAttributes == null) {
			_setAttributes = new HashSet<>();
		}

		_setAttributes.add(name);

		super.setAttribute(name, obj);
	}

	private Set<String> _setAttributes;

}