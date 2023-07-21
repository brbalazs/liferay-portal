<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

LayoutPageTemplateDisplayContext layoutPageTemplateDisplayContext = new LayoutPageTemplateDisplayContext(renderRequest, renderResponse, request);

FragmentsEditorDisplayContext fragmentsEditorDisplayContext = new FragmentsEditorDisplayContext(request, renderResponse, LayoutPageTemplateEntry.class.getName(), layoutPageTemplateDisplayContext.getLayoutPageTemplateEntryId(), layoutPageTemplateDisplayContext.isDisplayPage());

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);

renderResponse.setTitle(layoutPageTemplateDisplayContext.getLayoutPageTemplateEntryTitle());
%>

<liferay-editor:resources
	editorName="alloyeditor"
/>

<soy:component-renderer
	context="<%= fragmentsEditorDisplayContext.getEditorContext() %>"
	module="js/fragments_editor/FragmentsEditor.es"
	templateNamespace="com.liferay.layout.admin.web.FragmentsEditor.render"
/>