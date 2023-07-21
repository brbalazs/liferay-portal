/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.comment.taglib.internal.struts;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.struts.BaseStrutsAction;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.servlet.NamespaceServletRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(
	immediate = true, property = "path=/portal/comment/discussion/get_editor",
	service = StrutsAction.class
)
public class GetEditorStrutsAction extends BaseStrutsAction {

	@Override
	public String execute(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String namespace = ParamUtil.getString(request, "namespace");

		HttpServletRequest namespacedRequest = new NamespaceServletRequest(
			request, StringPool.BLANK, namespace);

		namespacedRequest.setAttribute("aui:form:portletNamespace", namespace);

		String contents = ParamUtil.getString(namespacedRequest, "contents");

		namespacedRequest.setAttribute(
			"liferay-comment:editor:contents", contents);

		String name = ParamUtil.getString(namespacedRequest, "name");

		namespacedRequest.setAttribute("liferay-comment:editor:name", name);

		String onChangeMethod = ParamUtil.getString(
			namespacedRequest, "onChangeMethod");

		namespacedRequest.setAttribute(
			"liferay-comment:editor:onChangeMethod", onChangeMethod);

		String placeholder = ParamUtil.getString(
			namespacedRequest, "placeholder");

		namespacedRequest.setAttribute(
			"liferay-comment:editor:placeholder", placeholder);

		String portletId = ParamUtil.getString(namespacedRequest, "portletId");

		namespacedRequest.setAttribute(WebKeys.PORTLET_ID, portletId);

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher(
				"/discussion/editor_resource.jsp");

		requestDispatcher.include(namespacedRequest, response);

		return null;
	}

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.comment.taglib)",
		unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	private ServletContext _servletContext;

}