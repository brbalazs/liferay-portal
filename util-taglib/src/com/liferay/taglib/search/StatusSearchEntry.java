/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.taglib.search;

import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.servlet.DirectRequestDispatcherFactoryUtil;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.taglib.servlet.PipingServletResponse;

import java.io.Writer;

import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Eudaldo Alonso
 */
public class StatusSearchEntry extends TextSearchEntry {

	@Override
	public Object clone() {
		StatusSearchEntry jspSearchEntry = new StatusSearchEntry();

		BeanPropertiesUtil.copyProperties(this, jspSearchEntry);

		return jspSearchEntry;
	}

	public HttpServletRequest getRequest() {
		return _request;
	}

	public HttpServletResponse getResponse() {
		return _response;
	}

	public ServletContext getServletContext() {
		if (_servletContext == null) {
			return ServletContextPool.get(PortalUtil.getServletContextName());
		}

		return _servletContext;
	}

	public int getStatus() {
		return _status;
	}

	public long getStatusByUserId() {
		return _statusByUserId;
	}

	public Date getStatusDate() {
		return _statusDate;
	}

	@Override
	public void print(
			Writer writer, HttpServletRequest request,
			HttpServletResponse response)
		throws Exception {

		request.setAttribute(
			"liferay-ui:search-container-column-status:status", _status);
		request.setAttribute(
			"liferay-ui:search-container-column-status:statusByUserId",
			_statusByUserId);
		request.setAttribute(
			"liferay-ui:search-container-column-status:statusDate",
			_statusDate);

		RequestDispatcher requestDispatcher =
			DirectRequestDispatcherFactoryUtil.getRequestDispatcher(
				getServletContext(), _PAGE);

		requestDispatcher.include(
			request, new PipingServletResponse(response, writer));
	}

	public void setRequest(HttpServletRequest request) {
		_request = request;
	}

	public void setResponse(HttpServletResponse response) {
		_response = response;
	}

	public void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	public void setStatus(int status) {
		_status = status;
	}

	public void setStatusByUserId(long statusByUserId) {
		_statusByUserId = statusByUserId;
	}

	public void setStatusDate(Date statusDate) {
		_statusDate = statusDate;
	}

	private static final String _PAGE =
		"/html/taglib/ui/search_container/status.jsp";

	private HttpServletRequest _request;
	private HttpServletResponse _response;
	private ServletContext _servletContext;
	private int _status;
	private long _statusByUserId;
	private Date _statusDate;

}