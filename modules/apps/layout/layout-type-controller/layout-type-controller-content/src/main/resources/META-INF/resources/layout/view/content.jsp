<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/layout/view/init.jsp" %>

<%
String ppid = ParamUtil.getString(request, "p_p_id");

if ((themeDisplay.isStatePopUp() || themeDisplay.isWidget() || layoutTypePortlet.hasStateMax()) && Validator.isNotNull(ppid)) {
	String templateId = null;
	String templateContent = null;
	String langType = null;

	if (themeDisplay.isStatePopUp() || themeDisplay.isWidget()) {
		templateId = theme.getThemeId() + LayoutTemplateConstants.STANDARD_SEPARATOR + "pop_up";
		templateContent = LayoutTemplateLocalServiceUtil.getContent("pop_up", true, theme.getThemeId());
		langType = LayoutTemplateLocalServiceUtil.getLangType("pop_up", true, theme.getThemeId());
	}
	else {
		ppid = StringUtil.split(layoutTypePortlet.getStateMax())[0];

		templateId = theme.getThemeId() + LayoutTemplateConstants.STANDARD_SEPARATOR + "max";
		templateContent = LayoutTemplateLocalServiceUtil.getContent("max", true, theme.getThemeId());
		langType = LayoutTemplateLocalServiceUtil.getLangType("max", true, theme.getThemeId());
	}

	if (Validator.isNotNull(templateContent)) {
		RuntimePageUtil.processTemplate(request, response, ppid, new StringTemplateResource(templateId, templateContent), langType);
	}
}
else {
	ContentLayoutTypeControllerDisplayContext contentLayoutTypeControllerDisplayContext = new ContentLayoutTypeControllerDisplayContext(request, response);

	try {
		request.setAttribute(WebKeys.PORTLET_DECORATE, Boolean.FALSE);
%>

		<%= contentLayoutTypeControllerDisplayContext.getRenderedContent() %>

<%
	}
	finally {
		request.removeAttribute(WebKeys.PORTLET_DECORATE);
	}
}
%>

<liferay-ui:layout-common />