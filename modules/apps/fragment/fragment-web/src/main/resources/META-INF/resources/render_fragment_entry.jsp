<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
long fragmentEntryId = ParamUtil.getLong(renderRequest, "fragmentEntryId");

FragmentEntry fragmentEntry = FragmentEntryLocalServiceUtil.fetchFragmentEntry(fragmentEntryId);

FragmentEntryLink fragmentEntryLink = FragmentEntryLinkLocalServiceUtil.createFragmentEntryLink(0);

fragmentEntryLink.setCss(BeanParamUtil.getString(fragmentEntry, renderRequest, "css"));
fragmentEntryLink.setHtml(BeanParamUtil.getString(fragmentEntry, renderRequest, "html"));
fragmentEntryLink.setJs(BeanParamUtil.getString(fragmentEntry, renderRequest, "js"));
fragmentEntryLink.setFragmentEntryId(fragmentEntryId);

try {
%>

	<%= FragmentEntryRenderUtil.renderFragmentEntryLink(fragmentEntryLink, FragmentEntryLinkConstants.VIEW, request, response) %>

<%
}
catch (FragmentEntryContentException fece) {
%>

	<div class="alert alert-danger">
		<liferay-ui:message key="<%= fece.getMessage() %>" />
	</div>

<%
}
%>