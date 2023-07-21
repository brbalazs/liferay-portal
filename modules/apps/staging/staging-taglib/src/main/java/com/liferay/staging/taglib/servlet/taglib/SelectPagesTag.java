/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.staging.taglib.servlet.taglib;

import com.liferay.petra.string.StringPool;
import com.liferay.staging.taglib.internal.servlet.ServletContextUtil;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author Máté Thurzó
 */
public class SelectPagesTag extends IncludeTag {

	public void setAction(String action) {
		if (action == null) {
			_action = StringPool.BLANK;
		}
		else {
			_action = action;
		}
	}

	public void setDisableInputs(boolean disableInputs) {
		_disableInputs = disableInputs;
	}

	public void setExportImportConfigurationId(
		long exportImportConfigurationId) {

		_exportImportConfigurationId = exportImportConfigurationId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public void setLayoutSetBranchId(long layoutSetBranchId) {
		_layoutSetBranchId = layoutSetBranchId;
	}

	public void setLayoutSetSettings(boolean layoutSetSettings) {
		_layoutSetSettings = layoutSetSettings;
	}

	public void setLogo(boolean logo) {
		_logo = logo;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		servletContext = ServletContextUtil.getServletContext();
	}

	public void setPrivateLayout(boolean privateLayout) {
		_privateLayout = privateLayout;
	}

	public void setSelectedLayoutIds(String selectedLayoutIds) {
		_selectedLayoutIds = selectedLayoutIds;
	}

	public void setShowDeleteMissingLayouts(boolean showDeleteMissingLayouts) {
		_showDeleteMissingLayouts = showDeleteMissingLayouts;
	}

	public void setThemeReference(boolean themeReference) {
		_themeReference = themeReference;
	}

	public void setTreeId(String treeId) {
		_treeId = treeId;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_action = StringPool.BLANK;
		_disableInputs = false;
		_exportImportConfigurationId = 0;
		_groupId = 0;
		_layoutSetBranchId = 0;
		_layoutSetSettings = true;
		_logo = true;
		_privateLayout = false;
		_selectedLayoutIds = StringPool.BLANK;
		_showDeleteMissingLayouts = false;
		_themeReference = true;
		_treeId = StringPool.BLANK;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute("liferay-staging:select-pages:action", _action);
		request.setAttribute(
			"liferay-staging:select-pages:disableInputs", _disableInputs);
		request.setAttribute(
			"liferay-staging:select-pages:exportImportConfigurationId",
			_exportImportConfigurationId);
		request.setAttribute("liferay-staging:select-pages:groupId", _groupId);
		request.setAttribute(
			"liferay-staging:select-pages:layoutSetBranchId",
			_layoutSetBranchId);
		request.setAttribute(
			"liferay-staging:select-pages:layoutSetSettings",
			_layoutSetSettings);
		request.setAttribute("liferay-staging:select-pages:logo", _logo);
		request.setAttribute(
			"liferay-staging:select-pages:privateLayout", _privateLayout);
		request.setAttribute(
			"liferay-staging:select-pages:selectedLayoutIds",
			_selectedLayoutIds);
		request.setAttribute(
			"liferay-staging:select-pages:showDeleteMissingLayouts",
			_showDeleteMissingLayouts);
		request.setAttribute(
			"liferay-staging:select-pages:themeReference", _themeReference);
		request.setAttribute("liferay-staging:select-pages:treeId", _treeId);
	}

	private static final String _PAGE = "/select_pages/page.jsp";

	private String _action = StringPool.BLANK;
	private boolean _disableInputs;
	private long _exportImportConfigurationId;
	private long _groupId;
	private long _layoutSetBranchId;
	private boolean _layoutSetSettings = true;
	private boolean _logo = true;
	private boolean _privateLayout;
	private String _selectedLayoutIds = StringPool.BLANK;
	private boolean _showDeleteMissingLayouts;
	private boolean _themeReference = true;
	private String _treeId = StringPool.BLANK;

}