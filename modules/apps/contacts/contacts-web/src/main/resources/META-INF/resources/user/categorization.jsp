<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
User selUser = (User)request.getAttribute("user.selUser");
%>

<aui:model-context bean="<%= selUser %>" model="<%= User.class %>" />

<liferay-asset:asset-categories-error />

<liferay-asset:asset-tags-error />

<h3><liferay-ui:message key="categorization" /></h3>

<aui:fieldset>
	<liferay-asset:asset-categories-selector
		className="<%= User.class.getName() %>"
		classPK="<%= (selUser != null) ? selUser.getPrimaryKey() : 0 %>"
	/>

	<liferay-asset:asset-tags-selector
		className="<%= User.class.getName() %>"
		classPK="<%= (selUser != null) ? selUser.getPrimaryKey() : 0 %>"
	/>
</aui:fieldset>

<aui:script>
	function <portlet:namespace />getSuggestionsContent() {

		<%
		StringBundler sb = new StringBundler();

		if (selUser.getComments() != null) {
			sb.append(selUser.getComments());
		}

		if (selUser.getJobTitle() != null) {
			sb.append(StringPool.SPACE);
			sb.append(selUser.getJobTitle());
		}
		%>

		return '<%= HtmlUtil.escape(HtmlUtil.replaceNewLine(sb.toString())) %>'
	}
</aui:script>