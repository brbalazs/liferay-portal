/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.taglib.aui.base;

import javax.servlet.jsp.JspException;

/**
 * @author Eduardo Lundgren
 * @author Bruno Basto
 * @author Nathan Cavanaugh
 * @author Julio Camarero
 * @generated
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class BaseToolTagImpl extends javax.servlet.jsp.tagext.TagSupport {

	@Override
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

	public java.lang.String getHandler() {
		return _handler;
	}

	public java.lang.String getIcon() {
		return _icon;
	}

	@Override
	public java.lang.String getId() {
		return _id;
	}

	public void setHandler(java.lang.String handler) {
		_handler = handler;
	}

	public void setIcon(java.lang.String icon) {
		_icon = icon;
	}

	@Override
	public void setId(java.lang.String id) {
		_id = id;
	}

	protected void cleanUp() {
		_handler = null;
		_icon = null;
		_id = null;
	}

	protected String getPage() {
		return _PAGE;
	}

	private static final String _PAGE =
		"/html/taglib/aui/tool/page.jsp";

	private java.lang.String _handler = null;
	private java.lang.String _icon = null;
	private java.lang.String _id = null;

}