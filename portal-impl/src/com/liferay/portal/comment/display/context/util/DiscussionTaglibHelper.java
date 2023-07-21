/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.comment.display.context.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Adolfo Pérez
 */
public class DiscussionTaglibHelper {

	public DiscussionTaglibHelper(HttpServletRequest request) {
		_request = request;
	}

	public String getClassName() {
		if (_className == null) {
			_className = _getAttribute("className");
		}

		return _className;
	}

	public long getClassPK() {
		if (_classPK == null) {
			_classPK = GetterUtil.getLong(_getAttribute("classPK"));
		}

		return _classPK;
	}

	public String getFormAction() {
		if (_formAction == null) {
			_formAction = _getAttribute("formAction");
		}

		return _formAction;
	}

	public String getFormName() {
		if (_formName == null) {
			_formName = _getAttribute("formName");
		}

		return _formName;
	}

	public String getPaginationURL() {
		if (_paginationURL == null) {
			_paginationURL = _getAttribute("paginationURL");
		}

		return _paginationURL;
	}

	public String getRedirect() {
		if (_redirect == null) {
			_redirect = _getAttribute("redirect");
		}

		return _redirect;
	}

	public String getSubscriptionClassName() {
		return _CLASS_NAME + StringPool.UNDERLINE + getClassName();
	}

	public long getUserId() {
		if (_userId == null) {
			_userId = GetterUtil.getLong(_getAttribute("userId"));
		}

		return _userId;
	}

	public boolean isAssetEntryVisible() {
		if (_assetEntryVisible == null) {
			_assetEntryVisible = GetterUtil.getBoolean(
				_getAttribute("assetEntryVisible"));
		}

		return _assetEntryVisible;
	}

	public boolean isHideControls() {
		if (_hideControls == null) {
			_hideControls = GetterUtil.getBoolean(
				_getAttribute("hideControls"));
		}

		return _hideControls;
	}

	public boolean isRatingsEnabled() {
		if (_ratingsEnabled == null) {
			_ratingsEnabled = GetterUtil.getBoolean(
				_getAttribute("ratingsEnabled"));
		}

		return _ratingsEnabled;
	}

	protected HttpServletRequest getRequest() {
		return _request;
	}

	private String _getAttribute(String name) {
		HttpServletRequest request = getRequest();

		String value = (String)request.getAttribute(_LEGACY_PREFIX + name);

		if (Validator.isNotNull(value)) {
			return value;
		}

		return (String)request.getAttribute(_PREFIX + name);
	}

	private static final String _CLASS_NAME =
		"com.liferay.message.boards.model.MBDiscussion";

	private static final String _LEGACY_PREFIX = "liferay-ui:discussion:";

	private static final String _PREFIX = "liferay-comment:discussion:";

	private Boolean _assetEntryVisible;
	private String _className;
	private Long _classPK;
	private String _formAction;
	private String _formName;
	private Boolean _hideControls;
	private String _paginationURL;
	private Boolean _ratingsEnabled;
	private String _redirect;
	private final HttpServletRequest _request;
	private Long _userId;

}