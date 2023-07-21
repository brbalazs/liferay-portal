/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.display.web.internal.display.context;

import com.liferay.portal.kernel.util.ParamUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class AssetDisplayDisplayContext {

	public AssetDisplayDisplayContext(HttpServletRequest request) {
		_request = request;
	}

	public int getAbstractLength() {
		if (_abstractLength != null) {
			return _abstractLength;
		}

		_abstractLength = ParamUtil.getInteger(_request, "abstractLength");

		return _abstractLength;
	}

	public String getClassName() {
		if (_className != null) {
			return _className;
		}

		_className = ParamUtil.getString(_request, "className");

		return _className;
	}

	public long getClassPK() {
		if (_classPK != null) {
			return _classPK;
		}

		_classPK = ParamUtil.getLong(_request, "classPK");

		return _classPK;
	}

	public String getTemplate() {
		if (_template != null) {
			return _template;
		}

		_template = ParamUtil.getString(_request, "template");

		return _template;
	}

	public String getViewURL() {
		if (_viewURL != null) {
			return _viewURL;
		}

		_viewURL = ParamUtil.getString(_request, "viewURL");

		return _viewURL;
	}

	public boolean isShowComments() {
		if (_showComments != null) {
			return _showComments;
		}

		_showComments = ParamUtil.getBoolean(_request, "showComments");

		return _showComments;
	}

	public boolean isShowExtraInfo() {
		if (_showExtraInfo != null) {
			return _showExtraInfo;
		}

		_showExtraInfo = ParamUtil.getBoolean(_request, "showExtraInfo");

		return _showExtraInfo;
	}

	public boolean isShowHeader() {
		if (_showHeader != null) {
			return _showHeader;
		}

		_showHeader = ParamUtil.getBoolean(_request, "showHeader");

		return _showHeader;
	}

	private Integer _abstractLength;
	private String _className;
	private Long _classPK;
	private final HttpServletRequest _request;
	private Boolean _showComments;
	private Boolean _showExtraInfo;
	private Boolean _showHeader;
	private String _template;
	private String _viewURL;

}