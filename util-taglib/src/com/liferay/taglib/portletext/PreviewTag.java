/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.taglib.portletext;

import com.liferay.portal.kernel.servlet.DirectRequestDispatcherFactoryUtil;
import com.liferay.taglib.servlet.PipingServletResponse;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

/**
 * @author Brian Wing Shun Chan
 */
public class PreviewTag extends IncludeTag {

	public static void doTag(
			String portletName, String queryString, boolean showBorders,
			String width, ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		doTag(
			_PAGE, portletName, queryString, showBorders, width, servletContext,
			request, response);
	}

	public static void doTag(
			String page, String portletName, String queryString,
			boolean showBorders, String width, ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		request.setAttribute(
			"liferay-portlet:preview:portletName", portletName);
		request.setAttribute(
			"liferay-portlet:preview:queryString", queryString);
		request.setAttribute(
			"liferay-portlet:preview:showBorders", String.valueOf(showBorders));
		request.setAttribute("liferay-portlet:preview:width", width);

		RequestDispatcher requestDispatcher =
			DirectRequestDispatcherFactoryUtil.getRequestDispatcher(
				servletContext, page);

		requestDispatcher.include(request, response);
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			doTag(
				getPage(), _portletName, _queryString, _showBorders, _width,
				servletContext, request,
				PipingServletResponse.createPipingServletResponse(pageContext));

			return EVAL_PAGE;
		}
		catch (Exception e) {
			throw new JspException(e);
		}
	}

	public void setPortletName(String portletName) {
		_portletName = portletName;
	}

	public void setQueryString(String queryString) {
		_queryString = queryString;
	}

	public void setShowBorders(boolean showBorders) {
		_showBorders = showBorders;
	}

	public void setWidth(String width) {
		_width = width;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	private static final String _PAGE = "/html/taglib/portlet/preview/page.jsp";

	private String _portletName;
	private String _queryString;
	private boolean _showBorders;
	private String _width;

}