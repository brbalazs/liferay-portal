<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
FragmentEntry fragmentEntry = fragmentDisplayContext.getFragmentEntry();

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(fragmentDisplayContext.getRedirect());

String title = fragmentDisplayContext.getFragmentEntryTitle();

if (WorkflowConstants.STATUS_DRAFT == fragmentEntry.getStatus()) {
	title = fragmentDisplayContext.getFragmentEntryTitle() + " (" + LanguageUtil.get(request, WorkflowConstants.getStatusLabel(fragmentEntry.getStatus())) + ")";
}

renderResponse.setTitle(title);
%>

<soy:component-renderer
	context="<%= fragmentDisplayContext.getFragmentEditorDisplayContext() %>"
	module="js/FragmentEditor.es"
	templateNamespace="com.liferay.fragment.web.FragmentEditor.render"
/>