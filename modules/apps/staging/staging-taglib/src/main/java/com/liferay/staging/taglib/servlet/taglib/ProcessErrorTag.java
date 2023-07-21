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
 * @author Péter Borkuti
 */
public class ProcessErrorTag extends IncludeTag {

	public void setAuthException(boolean authException) {
		_authException = authException;
	}

	public void setDuplicateLockException(boolean duplicateLockException) {
		_duplicateLockException = duplicateLockException;
	}

	public void setIllegalArgumentException(boolean illegalArgumentException) {
		_illegalArgumentException = illegalArgumentException;
	}

	public void setLayoutPrototypeException(boolean layoutPrototypeException) {
		_layoutPrototypeException = layoutPrototypeException;
	}

	public void setNoSuchExceptions(boolean noSuchExceptions) {
		_noSuchExceptions = noSuchExceptions;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		servletContext = ServletContextUtil.getServletContext();
	}

	public void setRemoteExportException(boolean remoteExportException) {
		_remoteExportException = remoteExportException;
	}

	public void setRemoteOptionsException(boolean remoteOptionsException) {
		_remoteOptionsException = remoteOptionsException;
	}

	public void setSystemException(boolean systemException) {
		_systemException = systemException;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_authException = false;
		_duplicateLockException = false;
		_illegalArgumentException = false;
		_layoutPrototypeException = false;
		_noSuchExceptions = false;
		_remoteExportException = false;
		_remoteOptionsException = false;
		_systemException = false;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-staging:process-error:authException", _authException);
		request.setAttribute(
			"liferay-staging:process-error:duplicateLockException",
			_duplicateLockException);
		request.setAttribute(
			"liferay-staging:process-error:illegalArgumentException",
			_illegalArgumentException);
		request.setAttribute(
			"liferay-staging:process-error:layoutPrototypeException",
			_layoutPrototypeException);
		request.setAttribute(
			"liferay-staging:process-error:noSuchExceptions",
			_noSuchExceptions);
		request.setAttribute(
			"liferay-staging:process-error:remoteExportException",
			_remoteExportException);
		request.setAttribute(
			"liferay-staging:process-error:remoteOptionsException",
			_remoteOptionsException);
		request.setAttribute(
			"liferay-staging:process-error:systemException", _systemException);
	}

	private static final String _PAGE = "/process_error/page.jsp";

	private boolean _authException;
	private boolean _duplicateLockException;
	private boolean _illegalArgumentException;
	private boolean _layoutPrototypeException;
	private boolean _noSuchExceptions;
	private boolean _remoteExportException;
	private boolean _remoteOptionsException;
	private boolean _systemException;

}