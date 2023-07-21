<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
int roleType = ParamUtil.getInteger(request, "roleType");
%>

<c:choose>
	<c:when test="<%= roleType == RoleConstants.TYPE_ORGANIZATION %>">
		<%@ include file="/select_organization_role.jspf" %>
	</c:when>
	<c:when test="<%= roleType == RoleConstants.TYPE_SITE %>">
		<%@ include file="/select_site_role.jspf" %>
	</c:when>
	<c:otherwise>
		<%@ include file="/select_regular_role.jspf" %>
	</c:otherwise>
</c:choose>