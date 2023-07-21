<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

if (Validator.isNull(redirect)) {
	PortletURL backURL = renderResponse.createRenderURL();

	redirect = backURL.toString();
}

FragmentsEditorDisplayContext fragmentsEditorDisplayContext = new FragmentsEditorDisplayContext(request, renderResponse, Layout.class.getName(), layoutsAdminDisplayContext.getSelPlid(), false);

Layout selLayout = layoutsAdminDisplayContext.getSelLayout();

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);

renderResponse.setTitle(selLayout.getName(locale));
%>

<liferay-ui:success key="layoutAdded" message="the-page-was-created-succesfully" />

<liferay-editor:resources
	editorName="alloyeditor"
/>

<soy:component-renderer
	context="<%= fragmentsEditorDisplayContext.getEditorContext() %>"
	module="js/fragments_editor/FragmentsEditor.es"
	templateNamespace="com.liferay.layout.admin.web.FragmentsEditor.render"
/>