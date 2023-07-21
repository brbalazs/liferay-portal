<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
User selUser = (User)request.getAttribute(UsersAdminWebKeys.SELECTED_USER);
%>

<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= (selUser == null) ? Constants.ADD : Constants.UPDATE %>" />

<div class="sheet-section">
	<h3 class="sheet-subtitle"><liferay-ui:message key="personal-information" /></h3>

	<liferay-util:include page="/user/details.jsp" servletContext="<%= application %>" />

	<liferay-util:include page="/user/comments.jsp" servletContext="<%= application %>" />
</div>

<div class="sheet-section">
	<h3 class="sheet-subtitle"><liferay-ui:message key="more-information" /></h3>

	<liferay-util:include page="/user/categorization.jsp" servletContext="<%= application %>" />
</div>

<c:if test="<%= CustomFieldsUtil.hasVisibleCustomFields(company.getCompanyId(), User.class) %>">
	<div class="sheet-section">
		<h4 class="sheet-tertiary-title"><liferay-ui:message key="custom-fields" /></h4>

		<liferay-util:include page="/user/custom_fields.jsp" servletContext="<%= application %>" />
	</div>
</c:if>