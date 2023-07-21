/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.taglib.servlet.taglib;

import com.liferay.asset.taglib.internal.servlet.ServletContextUtil;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author Alvaro del Castillo
 * @author Eduardo Lundgren
 * @author Jorge Ferrer
 */
public class AssetTagsNavigationTag extends IncludeTag {

	public void setClassNameId(long classNameId) {
		_classNameId = classNameId;
	}

	public void setDisplayStyle(String displayStyle) {
		_displayStyle = displayStyle;
	}

	public void setHidePortletWhenEmpty(boolean hidePortletWhenEmpty) {
		_hidePortletWhenEmpty = hidePortletWhenEmpty;
	}

	public void setMaxAssetTags(int maxAssetTags) {
		_maxAssetTags = maxAssetTags;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		servletContext = ServletContextUtil.getServletContext();
	}

	public void setShowAssetCount(boolean showAssetCount) {
		_showAssetCount = showAssetCount;
	}

	public void setShowZeroAssetCount(boolean showZeroAssetCount) {
		_showZeroAssetCount = showZeroAssetCount;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_classNameId = 0;
		_displayStyle = "cloud";
		_hidePortletWhenEmpty = false;
		_maxAssetTags = 10;
		_showAssetCount = false;
		_showZeroAssetCount = false;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-asset:asset-tags-navigation:classNameId",
			String.valueOf(_classNameId));
		request.setAttribute(
			"liferay-asset:asset-tags-navigation:displayStyle", _displayStyle);
		request.setAttribute(
			"liferay-asset:asset-tags-navigation:hidePortletWhenEmpty",
			String.valueOf(_hidePortletWhenEmpty));
		request.setAttribute(
			"liferay-asset:asset-tags-navigation:maxAssetTags",
			String.valueOf(_maxAssetTags));
		request.setAttribute(
			"liferay-asset:asset-tags-navigation:showAssetCount",
			String.valueOf(_showAssetCount));
		request.setAttribute(
			"liferay-asset:asset-tags-navigation:showZeroAssetCount",
			String.valueOf(_showZeroAssetCount));
	}

	private static final String _PAGE = "/asset_tags_navigation/page.jsp";

	private long _classNameId;
	private String _displayStyle = "cloud";
	private boolean _hidePortletWhenEmpty;
	private int _maxAssetTags = 10;
	private boolean _showAssetCount;
	private boolean _showZeroAssetCount;

}