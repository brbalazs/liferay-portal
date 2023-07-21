/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.product.navigation.control.menu;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;

import java.io.IOException;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Julio Camarero
 */
public abstract class BaseProductNavigationControlMenuEntry
	implements ProductNavigationControlMenuEntry {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ProductNavigationControlMenuEntry)) {
			return false;
		}

		ProductNavigationControlMenuEntry productNavigationControlMenuEntry =
			(ProductNavigationControlMenuEntry)obj;

		if (Objects.equals(
				getKey(), productNavigationControlMenuEntry.getKey())) {

			return true;
		}

		return false;
	}

	@Override
	public Map<String, Object> getData(HttpServletRequest request) {
		return Collections.emptyMap();
	}

	@Override
	public String getIcon(HttpServletRequest request) {
		return StringPool.BLANK;
	}

	@Override
	public String getIconCssClass(HttpServletRequest request) {
		return StringPool.BLANK;
	}

	@Override
	public String getKey() {
		Class<?> clazz = getClass();

		return clazz.getName();
	}

	@Override
	public String getLinkCssClass(HttpServletRequest request) {
		return StringPool.BLANK;
	}

	@Override
	public String getMarkupView(HttpServletRequest request) {
		return "lexicon";
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, getKey());
	}

	@Override
	public boolean includeBody(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		return false;
	}

	@Override
	public boolean includeIcon(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		return false;
	}

	@Override
	public boolean isShow(HttpServletRequest request) throws PortalException {
		return true;
	}

	@Override
	public boolean isUseDialog() {
		return false;
	}

}