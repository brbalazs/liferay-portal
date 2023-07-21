/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.expando.taglib.servlet.taglib;

import com.liferay.expando.taglib.internal.servlet.ServletContextUtil;
import com.liferay.taglib.util.IncludeTag;

import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author Brian Wing Shun Chan
 */
public class CustomAttributeTag extends IncludeTag {

	public Set<Locale> getAvailableLocales() {
		return _availableLocales;
	}

	public void setAvailableLocales(Set<Locale> availableLocales) {
		_availableLocales = availableLocales;
	}

	public void setClassName(String className) {
		_className = className;
	}

	public void setClassPK(long classPK) {
		_classPK = classPK;
	}

	public void setEditable(boolean editable) {
		_editable = editable;
	}

	public void setLabel(boolean label) {
		_label = label;
	}

	public void setName(String name) {
		_name = name;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		servletContext = ServletContextUtil.getServletContext();
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_availableLocales = null;
		_className = null;
		_classPK = 0;
		_editable = false;
		_label = false;
		_name = null;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-expando:custom-attribute:availableLocales",
			_availableLocales);
		request.setAttribute(
			"liferay-expando:custom-attribute:className", _className);
		request.setAttribute(
			"liferay-expando:custom-attribute:classPK",
			String.valueOf(_classPK));
		request.setAttribute(
			"liferay-expando:custom-attribute:editable",
			String.valueOf(_editable));
		request.setAttribute(
			"liferay-expando:custom-attribute:label", String.valueOf(_label));
		request.setAttribute("liferay-expando:custom-attribute:name", _name);
	}

	private static final String _PAGE = "/custom_attribute/page.jsp";

	private Set<Locale> _availableLocales;
	private String _className;
	private long _classPK;
	private boolean _editable;
	private boolean _label;
	private String _name;

}