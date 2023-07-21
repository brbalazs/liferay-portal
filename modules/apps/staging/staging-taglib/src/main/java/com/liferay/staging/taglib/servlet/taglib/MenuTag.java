/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.staging.taglib.servlet.taglib;

import com.liferay.staging.taglib.internal.servlet.ServletContextUtil;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author Brian Wing Shun Chan
 */
public class MenuTag extends IncludeTag {

	public void setCssClass(String cssClass) {
		_cssClass = cssClass;
	}

	public void setLayoutSetBranchId(long layoutSetBranchId) {
		_layoutSetBranchId = layoutSetBranchId;
	}

	public void setOnlyActions(boolean onlyActions) {
		_onlyActions = onlyActions;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		servletContext = ServletContextUtil.getServletContext();
	}

	public void setSelPlid(long selPlid) {
		_selPlid = selPlid;
	}

	public void setShowManageBranches(boolean showManageBranches) {
		_showManageBranches = showManageBranches;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_cssClass = null;
		_layoutSetBranchId = 0;
		_onlyActions = false;
		_selPlid = 0;
		_showManageBranches = false;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute("liferay-staging:menu:cssClass", _cssClass);
		request.setAttribute(
			"liferay-staging:menu:layoutSetBranchId",
			String.valueOf(_layoutSetBranchId));
		request.setAttribute(
			"liferay-staging:menu:onlyActions", String.valueOf(_onlyActions));
		request.setAttribute(
			"liferay-staging:menu:selPlid", String.valueOf(_selPlid));
		request.setAttribute(
			"liferay-staging:menu:showManageBranches",
			String.valueOf(_showManageBranches));
	}

	private static final String _PAGE = "/menu/page.jsp";

	private String _cssClass;
	private long _layoutSetBranchId;
	private boolean _onlyActions;
	private long _selPlid;
	private boolean _showManageBranches;

}